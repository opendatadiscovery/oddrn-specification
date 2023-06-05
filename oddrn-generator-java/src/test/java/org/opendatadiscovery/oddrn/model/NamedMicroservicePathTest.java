package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;

public class NamedMicroservicePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateImagePath() throws Exception {
        shouldGeneratePath(
            NamedMicroservicePath.builder()
                .name("odd-platform:latest")
                .build(),
            "//microservice/named/name/odd-platform:latest"
        );
    }
}