package org.opendatadiscovery.oddrn.processor;

import org.opendatadiscovery.oddrn.model.OddrnPath;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;

public class PostgreSqlJdbcProcessor implements JdbcProcessor<PostgreSqlPath> {

    public static final String PREFIX = "postgresql";

    @Override
    public OddrnPath path(final String host, final String database) {
        return PostgreSqlPath.builder()
            .host(host)
            .database(database)
            .build();
    }

    @Override
    public String url(final PostgreSqlPath path, final int port) {
        return String.format("jdbc:%s://%s:%d/%s", PREFIX, path.getHost(), port, path.getDatabase());
    }
}
