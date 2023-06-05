package org.opendatadiscovery.oddrn.processor;

import org.opendatadiscovery.oddrn.model.MysqlPath;
import org.opendatadiscovery.oddrn.model.OddrnPath;

public class MysqlJdbcProcessor implements JdbcProcessor<MysqlPath> {

    public static final String PREFIX = "mysql";

    @Override
    public OddrnPath path(final String host, final String database) {
        return MysqlPath.builder()
            .host(host)
            .database(database)
            .build();
    }

    @Override
    public String url(final MysqlPath path, final int port) {
        return String.format("jdbc:%s://%s:%d/%s", PREFIX, path.getHost(), port, path.getDatabase());
    }
}
