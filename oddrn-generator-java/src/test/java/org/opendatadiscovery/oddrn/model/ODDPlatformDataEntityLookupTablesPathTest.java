package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class ODDPlatformDataEntityLookupTablesPathTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDataEntityGroupPath() throws Exception {
        shouldGeneratePath(
                ODDPlatformDataEntityLookupTablesPath.builder()
                        .id(1L)
                        .build(),
                "//oddplatform_lookup_tables/id/1"
        );
    }

    @Test
    public void shouldFailWhenIdIsNotSet() {
        shouldFail(
                ODDPlatformDataEntityLookupTablesPath.builder()
                        .build(),
                EmptyPathValueException.class
        );
    }
}
