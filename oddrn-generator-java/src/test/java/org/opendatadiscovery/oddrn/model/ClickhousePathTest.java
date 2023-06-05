package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;

public class ClickhousePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabaseName() throws Exception {
        shouldGeneratePath(
            ClickhousePath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .build(),
            "//clickhouse/host/1.1.1.1/databases/dbname"
        );
    }

    @Test
    public void shouldGenerateTableName1() throws Exception {
        shouldGeneratePath(
            ClickhousePath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .table("table")
                .build(),
            "//clickhouse/host/1.1.1.1/databases/dbname/tables/table"
        );

        shouldGeneratePath(
            ClickhousePath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .view("table")
                .build(),
            "//clickhouse/host/1.1.1.1/databases/dbname/views/table"
        );
    }

    @Test
    public void shouldGenerateColumnName() throws Exception {
        shouldGeneratePath(
            ClickhousePath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .table("table")
                .tableColumn("col1")
                .build(),
            "//clickhouse/host/1.1.1.1/databases/dbname/tables/table/tables_columns/col1"
        );

        shouldGeneratePath(
            ClickhousePath.builder()
                .host("1.1.1.1")
                .database("dbname")
                .view("table")
                .viewColumn("col1")
                .build(),
            "//clickhouse/host/1.1.1.1/databases/dbname/views/table/views_columns/col1"
        );
    }
}
