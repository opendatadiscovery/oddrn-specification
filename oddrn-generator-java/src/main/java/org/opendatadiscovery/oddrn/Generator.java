package org.opendatadiscovery.oddrn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.opendatadiscovery.oddrn.annotation.PathField;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;
import org.opendatadiscovery.oddrn.exception.GenerateException;
import org.opendatadiscovery.oddrn.exception.PathDoesntExistException;
import org.opendatadiscovery.oddrn.model.OddrnPath;
import org.opendatadiscovery.oddrn.util.GeneratorUtil;
import org.reflections.Reflections;

import static java.util.Locale.ENGLISH;
import static java.util.function.Function.identity;

public class Generator {
    static final String GET_PREFIX = "get";

    private static final Map<Class<?>, Function<String, ?>> RETURN_TYPE_MAPPING = new HashMap<>();

    private static class LazyHolder {
        static final Generator INSTANCE = new Generator();
    }

    public static Generator getInstance() {
        return LazyHolder.INSTANCE;
    }

    static {
        RETURN_TYPE_MAPPING.put(String.class, identity());
        RETURN_TYPE_MAPPING.put(Integer.class, Integer::parseInt);
        RETURN_TYPE_MAPPING.put(Long.class, Long::parseLong);
        RETURN_TYPE_MAPPING.put(Double.class, Double::parseDouble);
        RETURN_TYPE_MAPPING.put(Float.class, Float::parseFloat);
    }

    private final Map<Class<? extends OddrnPath>, ModelDescription> cache =
        new Reflections("org.opendatadiscovery.oddrn.model").getSubTypesOf(OddrnPath.class)
                .stream()
                .collect(Collectors.toMap(c -> c, this::generateModel));

    public static String capitalize(final String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

    public void register(final Class<? extends OddrnPath> clazz) {
        this.generateModel(clazz);
    }

    public Optional<OddrnPath> parse(final String oddrn)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Optional<OddrnPath> result = Optional.empty();

        for (final ModelDescription description : this.cache.values()) {
            if (oddrn.startsWith(description.prefix + "/")) {
                final String withoutPrefix = oddrn.substring(description.prefix.length());
                final Object builder = description.builderMethod.invoke(null);
                int nextFieldPos = 0;

                do {
                    final int fieldNamePos = withoutPrefix.indexOf("/", nextFieldPos);
                    final int valuePos = withoutPrefix.indexOf("/", fieldNamePos + 1);
                    if (valuePos == -1) {
                        break;
                    }
                    nextFieldPos = withoutPrefix.indexOf("/", valuePos + 1);

                    if (fieldNamePos >= 0) {
                        final String fieldName = withoutPrefix.substring(fieldNamePos + 1, valuePos);
                        final String stringValue;
                        if (nextFieldPos > 0) {
                            stringValue = withoutPrefix.substring(valuePos + 1, nextFieldPos);
                        } else {
                            stringValue = withoutPrefix.substring(valuePos + 1);
                        }
                        final ModelField modelField = description.prefixes.get(fieldName);

                        if (modelField != null) {
                            final Class<?> returnType = modelField.getReadMethod().getReturnType();

                            final Function<String, ?> mapper = RETURN_TYPE_MAPPING.get(returnType);

                            if (mapper == null) {
                                throw new IllegalArgumentException(
                                    String.format("Field path of type %s is not supported", returnType));
                            }

                            modelField.setMethod.invoke(builder, mapper.apply(GeneratorUtil.unescape(stringValue)));
                        }
                    }
                } while (nextFieldPos >= 0);

                return Optional.ofNullable(
                    (OddrnPath) builder.getClass().getMethod("build").invoke(builder)
                );
            }
        }

        return result;
    }

    public String generate(final OddrnPath path) throws GenerateException {
        try {
            final ModelDescription modelDescription = cache.computeIfAbsent(
                path.getClass(),
                this::generateModel
            );

            // find last none empty field
            final Iterator<ModelField> iterator = modelDescription.fields.iterator();
            ModelField field = null;
            while (iterator.hasNext()) {
                field = iterator.next();
                final Object result = field.readMethod.invoke(path);
                if (result != null) {
                    break;
                }
            }

            if (field != null) {
                return generate(path, modelDescription, field);
            } else {
                throw new GenerateException("All fields are empty");
            }
        } catch (Exception e) {
            if (e instanceof GenerateException) {
                throw (GenerateException) e;
            } else {
                throw new GenerateException("Generate error", e);
            }
        }
    }

    public String generate(final OddrnPath path, final ModelDescription description, final ModelField field)
            throws GenerateException {
        try {
            validatePath(path, description, field);

            final Map<String, ModelField> fields = description.fieldsMap;

            final List<ModelField> pathFields = new ArrayList<>();

            ModelField currentField = field;
            pathFields.add(currentField);

            while (currentField.pathField.dependency().length > 0
                && !currentField.pathField.dependency()[0].isEmpty()
            ) {
                for (final String dependency : currentField.pathField.dependency()) {
                    if (!dependency.isEmpty()) {
                        final Optional<ModelField> find = Optional.ofNullable(fields.get(dependency));

                        if (find.isPresent()) {
                            currentField = find.get();
                            pathFields.add(currentField);
                            break;
                        } else {
                            throw new PathDoesntExistException(
                                String.format("Path %s doesn't exist in generator",
                                    String.join(" ,", currentField.pathField.dependency())
                                )
                            );
                        }
                    }
                }
            }

            Collections.reverse(pathFields);
            final StringBuilder builder = new StringBuilder();
            builder.append(path.prefix());
            for (final ModelField modelField : pathFields) {
                final String prefix = modelField.pathField.prefix().isEmpty()
                    ? modelField.getField().getName()
                    : modelField.pathField.prefix();

                builder.append("/");
                builder.append(prefix);
                builder.append("/");
                builder.append(GeneratorUtil.escape(modelField.readMethod.invoke(path).toString()));
            }

            return builder.toString();
        } catch (Exception e) {
            if (e instanceof GenerateException) {
                throw (GenerateException) e;
            } else {
                throw new GenerateException("Generate error", e);
            }
        }
    }

    public void validateAllPaths(final OddrnPath model)
            throws GenerateException {
        final ModelDescription modelDescription = cache.computeIfAbsent(
            model.getClass(),
            this::generateModel
        );

        for (final ModelField field : modelDescription.fields) {
            this.validatePath(model, modelDescription, field);
        }
    }

    public void validatePath(final OddrnPath path, final ModelDescription description, final ModelField field)
            throws GenerateException {
        try {
            final String fieldName = field.name;

            boolean allFailed = true;
            Exception lastException = null;

            if (field.pathField.dependency().length > 0) {
                for (final String dependency : field.pathField.dependency()) {
                    if (!dependency.isEmpty()) {
                        final ModelField modelField =
                            description.fields.stream().filter(f -> f.name.equals(dependency)).findFirst()
                                .orElseThrow(() -> new PathDoesntExistException(dependency));
                        try {
                            validatePath(path, description, modelField);
                            allFailed = false;
                        } catch (Exception e) {
                            lastException = e;
                        }
                    }
                }
            }

            if (allFailed && lastException != null) {
                throw lastException;
            }

            if (!field.pathField.nullable()
                && (field.getReadMethod().invoke(path) == null
                || field.getReadMethod().invoke(path).toString().trim().isEmpty())
            ) {
                throw new EmptyPathValueException(
                    String.format("'Attribute %s' is empty",
                        fieldName
                    )
                );
            }
        } catch (Exception e) {
            if (e instanceof GenerateException) {
                throw (GenerateException) e;
            } else {
                throw new GenerateException("Generate error", e);
            }
        }
    }

    @SneakyThrows
    private ModelDescription generateModel(final Class<? extends OddrnPath> clazz) {
        final ModelDescription.ModelDescriptionBuilder descriptionBuilder = ModelDescription.builder();

        final Map<String, ModelField> fieldsMap = new HashMap<>();
        final Map<String, ModelField> prefixes = new HashMap<>();

        Class<?> currentClazz = clazz;
        final Class<?> builderClazz = clazz.getMethod("builder").getReturnType();
        final Method builderMethod = clazz.getMethod("builder");
        final Object builder = builderMethod.invoke(null);
        final Object build = builder.getClass().getMethod("build").invoke(builder);
        final String prefix = build.getClass().getMethod("prefix").invoke(build).toString();

        descriptionBuilder.prefix(prefix);
        descriptionBuilder.builderMethod(clazz.getMethod("builder"));

        while (OddrnPath.class.isAssignableFrom(currentClazz)) {
            for (final Field field : currentClazz.getDeclaredFields()) {
                final PathField[] pathFields = field.getAnnotationsByType(PathField.class);

                if (pathFields.length > 0) {
                    final Method getMethod = clazz.getMethod(GET_PREFIX + capitalize(field.getName()));
                    final Method setMethod = builderClazz.getMethod(field.getName(), getMethod.getReturnType());

                    final ModelField model = ModelField.builder()
                        .name(field.getName())
                        .field(field)
                        .pathField(pathFields[0])
                        .readMethod(getMethod)
                        .setMethod(setMethod)
                        .build();

                    final PathField pathField = pathFields[0];
                    final String fieldPrefix = pathField.prefix().isEmpty() ? field.getName() : pathField.prefix();
                    fieldsMap.put(field.getName(), model);
                    prefixes.put(
                        fieldPrefix,
                        model
                    );
                }
            }
            currentClazz = currentClazz.getSuperclass();
        }

        final LinkedList<ModelField> fields = new LinkedList<>();
        final LinkedList<String> fieldNames = new LinkedList<>();
        final Set<String> processedFields = new HashSet<>();
        final Deque<String> fieldsToProcess = new LinkedList<>(fieldsMap.keySet());

        while (!fieldsToProcess.isEmpty()) {
            final String fieldName = fieldsToProcess.pop();
            if (!processedFields.contains(fieldName)) {
                final ModelField modelField = fieldsMap.get(fieldName);
                final String[] dependency = modelField.pathField.dependency();
                final List<String> dependencies = new ArrayList<>();

                if (dependency != null && dependency.length > 0) {
                    boolean restart = false;
                    for (final String dependencyName : dependency) {
                        if (!dependencyName.isEmpty()) {
                            dependencies.add(dependencyName);
                            if (!processedFields.contains(dependencyName)) {
                                fieldsToProcess.push(fieldName);
                                fieldsToProcess.push(dependencyName);
                                restart = true;
                            }
                        }
                    }
                    if (restart) {
                        continue;
                    }
                }

                final int pos = dependencies.stream().mapToInt(fieldNames::indexOf).max().orElse(0);
                fields.add(pos, fieldsMap.get(fieldName));
                fieldNames.add(pos, fieldName);
                processedFields.add(fieldName);
            }
        }

        descriptionBuilder.prefixes(prefixes);
        descriptionBuilder.fields(fields);
        descriptionBuilder.fieldsMap(fieldsMap);
        return descriptionBuilder.build();
    }

    @Data
    @Builder
    private static class ModelDescription {
        private final Map<String, ModelField> prefixes;
        private final Map<String, ModelField> fieldsMap;
        private final List<ModelField> fields;
        private final String prefix;
        private final Method builderMethod;
    }

    @Data
    @Builder
    private static class ModelField {
        private final String name;
        private final Field field;
        private final Method readMethod;
        private final Method setMethod;
        private final PathField pathField;
    }
}
