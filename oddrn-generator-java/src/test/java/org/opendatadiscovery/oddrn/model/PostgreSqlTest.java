package org.opendatadiscovery.oddrn.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.Generator;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void shouldParsePartialOddrn()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final PostgreSqlPath expected = PostgreSqlPath.builder().build();
        shouldParse("//postgresql/", expected);
    }

    @Test
    public void shouldParsePartialOddrnWithHost()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final PostgreSqlPath expected = PostgreSqlPath.builder()
                .host("1.1.1.1")
                .build();
        shouldParse("//postgresql/host/1.1.1.1/dbname", expected);
    }
}