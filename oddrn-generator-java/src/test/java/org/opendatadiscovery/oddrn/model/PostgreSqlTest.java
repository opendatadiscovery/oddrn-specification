package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class PostgreSqlTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .build(),
            "//postgresql/host/1.1.1.1/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .schema("public")
                .table("test")
                .column("id")
                .build(),
            "//postgresql/host/1.1.1.1/databases/dbname/schemas/public/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .table("test")
                .build(),
            EmptyPathValueException.class
        );
    }
}