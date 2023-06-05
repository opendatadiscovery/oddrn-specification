package org.opendatadiscovery.oddrn.util;

public class GeneratorUtil {
    public static String escape(final String value) {
        return value.replace("/", "\\\\");
    }

    public static String unescape(final String value) {
        return value.replace("\\\\", "/");
    }
}
