package org.opendatadiscovery.oddrn;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcProcessorsTest {
    @Test
    public void testJdbcGenerator()  {
        final String url = new JdbcProcessors().url(
            PostgreSqlPath.builder()
                .host("1.1.1.1")
                .database("test")
                .build(),
            5432
        );

        assertEquals("jdbc:postgresql://1.1.1.1:5432/test", url);
    }
}