package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.model.HivePath;
import org.opendatadiscovery.oddrn.model.MysqlPath;
import org.opendatadiscovery.oddrn.model.OddrnPath;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;
import org.opendatadiscovery.oddrn.model.SnowflakePath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcUrlParserTest {
    @Test
    public void testPostgreSqlJdbcUrlParser() {
        test(
            "jdbc:postgresql://localhost/dvdrental",
            PostgreSqlPath.builder()
                .host("localhost")
                .database("dvdrental")
                .build()
        );

        test(
            "jdbc:postgresql://localhost/dvdrental?sslmode=true",
            PostgreSqlPath.builder()
                .host("localhost")
                .database("dvdrental")
                .build()
        );
    }

    @Test
    public void testMysqlJdbcUrlParser() {
        test(
            "jdbc:mysql://user:password@localhost/dvdrental?sslmode=true",
            MysqlPath.builder()
                .host("localhost")
                .database("dvdrental")
                .build()
        );

        test(
            "jdbc:mysql://user:password@localhost/?sslmode=true",
            MysqlPath.builder()
                .host("localhost")
                .database("user")
                .build()
        );

        test(
            "jdbc:mysql://localhost/dvdrental",
            MysqlPath.builder()
                .host("localhost")
                .database("dvdrental")
                .build()
        );

        test(
            "jdbc:mysql://localhost/dvdrental?sslmode=true",
            MysqlPath.builder()
                .host("localhost")
                .database("dvdrental")
                .build()
        );

        testWithUser(
            "jdbc:mysql://localhost", "admin",
            MysqlPath.builder()
                .host("localhost")
                .database("admin")
                .build()
        );

        testWithUser(
            "jdbc:mysql://localhost?sslmode=true", "admin",
            MysqlPath.builder()
                .host("localhost")
                .database("admin")
                .build()
        );
    }

    @Test
    public void testSnowflake() {
        test(
            "jdbc:snowflake://myorganization-myaccount.snowflakecomputing.com",
            SnowflakePath.builder()
                .account("myorganization-myaccount")
                .build()
        );

        test(
            "jdbc:snowflake://myorganization-myaccount.snowflakecomputing.com?params=1",
            SnowflakePath.builder()
                .account("myorganization-myaccount")
                .build()
        );
    }

    @Test
    public void testHive() {
        test(
            "jdbc:hive2://m1.hdp.local:10011/dbname;transportMode=http;httpPath=cliservice",
            HivePath.builder()
                .host("m1.hdp.local")
                .database("dbname")
                .build()
        );

        test(
            "jdbc:hive2://m1.hdp.local:2181,m2.hdp.local:2181,m3.hdp.local:2181/database",
            HivePath.builder()
                .host("m1.hdp.local,m2.hdp.local,m3.hdp.local")
                .database("database")
                .build()
        );
    }

    private void test(final String url, final OddrnPath expected) {
        final OddrnPath path = new JdbcUrlParser().parse(url);
        assertNotNull(path);
        assertEquals(expected, path);
    }

    private void testWithUser(final String url, final String user, final OddrnPath expected) {
        final OddrnPath path = new JdbcUrlParser().parse(url, user);
        assertNotNull(path);
        assertEquals(expected, path);
    }
}