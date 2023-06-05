package org.opendatadiscovery.oddrn.processor;

import org.opendatadiscovery.oddrn.model.ClickhousePath;
import org.opendatadiscovery.oddrn.model.OddrnPath;

public class ClickhouseJdbcProcessor implements JdbcProcessor<ClickhousePath> {
    public static final String PREFIX = "clickhouse";

    @Override
    public OddrnPath path(final String host, final String database) {
        return ClickhousePath.builder()
            .host(host)
            .database(database)
            .build();
    }

    @Override
    public String url(final ClickhousePath path, final int port) {
        return String.format("jdbc:%s://%s:%d/%s", PREFIX, path.getHost(), port, path.getDatabase());
    }
}
