package org.opendatadiscovery.internal.oddrn

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import org.yaml.snakeyaml.Yaml

class OddrnGenerateTask extends DefaultTask {
    private static final def YAML = new Yaml()

    @Internal
    final oddrnGenerateExtension = project.extensions.getByType(OddrnGenerateExtension.class)

    @TaskAction
    def execute() {
        final Mustache mustache = new DefaultMustacheFactory()
            .compile(new FileReader(oddrnGenerateExtension.mustacheTemplate), "template")

        final def content = readContract().each { fileContent ->
            fileContent['fields']?.each {
                it['dependency'] = it['dependencies']
                    ?.collect { it = "\"${it}\"" }
                    ?.join(', ')
                it['type'] = defineType((String) it['type'])
            }
        }

        for (final def data in content) {
            final StringWriter writer = new StringWriter()
            mustache.execute(writer, data).flush()

            final def modelPackagePath = oddrnGenerateExtension.modelPackage.replace(".", "/")
            final def targetPath = "${oddrnGenerateExtension.outputPath}/${modelPackagePath}/${data['meta']['java']['class_name']}.java"

            // TODO: proper log with debug
            println("Generating ${targetPath}")

            // FIXME: two slashes in the path problem
            final def targetFile = new File(targetPath)

            if (!targetFile.getParentFile().exists()) {
                targetFile.parentFile.mkdirs()
            }

            targetFile.write(writer.toString())
        }
    }

    def readContract() {
        return new File(oddrnGenerateExtension.contractPath).listFiles()
            .findAll { it.name.endsWith('.yaml') || it.name.endsWith('.yml') }
            .collect { YAML.load(it.text) }
            .collect { it + ['package': oddrnGenerateExtension.modelPackage] }
    }

    static def defineType(String type) {
        switch (type) {
            case "long":
                return "Long"
            default:
                return "String"
        }
    }
}
