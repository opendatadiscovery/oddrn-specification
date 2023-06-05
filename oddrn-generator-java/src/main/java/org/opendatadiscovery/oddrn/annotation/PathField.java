package org.opendatadiscovery.oddrn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PathField {
    String[] dependency() default "";

    String prefix() default "";

    boolean nullable() default false;
}
