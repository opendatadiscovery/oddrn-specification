package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class HttpServicePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            HttpServicePath.builder()
                .host("1.1.1.1")
                .method("GET")
                .path("/entites/1")
                .build(),
            "//http/host/1.1.1.1/methods/GET/paths/\\\\entites\\\\1"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            HttpServicePath.builder()
                .host("1.1.1.1")
                .path("/entites/1")
                .build(),
            EmptyPathValueException.class
        );
    }
}