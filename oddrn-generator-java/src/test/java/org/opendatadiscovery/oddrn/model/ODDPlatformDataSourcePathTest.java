package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class ODDPlatformDataSourcePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDataSourcePath() throws Exception {
        shouldGeneratePath(
            ODDPlatformDataSourcePath.builder()
                .datasourceId(1L)
                .build(),
            "//oddplatform/datasources/1"
        );
    }

    @Test
    public void shouldFailWhenDataSourceIsNotSet() {
        shouldFail(
            ODDPlatformDataSourcePath.builder()
                .build(),
            EmptyPathValueException.class
        );
    }
}
