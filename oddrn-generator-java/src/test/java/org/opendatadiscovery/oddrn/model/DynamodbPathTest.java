package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class DynamodbPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            DynamodbPath.builder()
                .account("7771111")
                .region("eu-central-1")
                .table("dtable")
                .build(),
            "//dynamodb/cloud/aws/account/7771111/region/eu-central-1/tables/dtable"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            DynamodbPath.builder()
                .account("7771111")
                .region("eu-central-1")
                .table("dtable")
                .column("id")
                .build(),
            "//dynamodb/cloud/aws/account/7771111/region/eu-central-1/tables/dtable/columns/id"
        );
    }

    @Test
    public void shouldFailColumnPath() {
        shouldFail(
            DynamodbPath.builder()
                .account("7771111")
                .region("eu-central-1")
                .column("id")
                .build(),
            EmptyPathValueException.class
        );
    }
}
