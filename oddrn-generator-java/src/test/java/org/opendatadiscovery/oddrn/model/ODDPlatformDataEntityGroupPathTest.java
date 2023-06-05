package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class ODDPlatformDataEntityGroupPathTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDataEntityGroupPath() throws Exception {
        shouldGeneratePath(
                ODDPlatformDataEntityGroupPath.builder()
                        .id(1L)
                        .build(),
                "//oddplatform_deg/id/1"
        );
    }

    @Test
    public void shouldFailWhenIdIsNotSet() {
        shouldFail(
                ODDPlatformDataEntityGroupPath.builder()
                        .build(),
                EmptyPathValueException.class
        );
    }
}
