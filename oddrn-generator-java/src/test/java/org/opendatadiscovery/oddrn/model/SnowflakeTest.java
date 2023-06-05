package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class SnowflakeTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            SnowflakePath.builder()
                .account("wh")
                .database("dbname")
                .build(),
            "//snowflake/account/wh/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            SnowflakePath.builder()
                .account("wh")
                .database("dbname")
                .schema("test")
                .table("test")
                .column("id")
                .build(),
            "//snowflake/account/wh/databases/dbname/schemas/test/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            SnowflakePath.builder()
                .account("wh")
                .database("dbname")
                .table("test")
                .build(),
            EmptyPathValueException.class
        );
    }
}
