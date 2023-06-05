package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;

public class DockerMicroservicePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateImagePath() throws Exception {
        shouldGeneratePath(
            DockerMicroservicePath.builder()
                .image("ghcr.io/opendatadiscovery/odd-platform:latest")
                .build(),
            "//microservice/docker/image/ghcr.io\\\\opendatadiscovery\\\\odd-platform:latest"
        );
    }
}