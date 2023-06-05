package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class MysqlTest extends AbstractGeneratorTest {

    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            MysqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .build(),
            "//mysql/host/1.1.1.1/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateColumnPath() throws Exception {
        shouldGeneratePath(
            MysqlPath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .table("test")
                .column("id")
                .build(),
            "//mysql/host/1.1.1.1/databases/dbname/tables/test/columns/id"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            MysqlPath.builder()
                .host("1.1.1.1")
                .table("test")
                .build(),
            EmptyPathValueException.class
        );
    }
}
