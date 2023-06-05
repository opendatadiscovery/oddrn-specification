package org.opendatadiscovery.oddrn.processor;

import org.opendatadiscovery.oddrn.model.HivePath;
import org.opendatadiscovery.oddrn.model.OddrnPath;

public class Hive2JdbcProcessor implements JdbcProcessor<HivePath> {

    public static final String PREFIX = "hive2";

    @Override
    public OddrnPath path(final String host, final String database) {
        return HivePath.builder()
            .host(host)
            .database(database)
            .build();
    }

    @Override
    public String url(final HivePath path, final int port) {
        return String.format("jdbc:%s://%s:%d/%s", PREFIX, path.getHost(), port, path.getDatabase());
    }
}
