package org.opendatadiscovery.oddrn.processor;

import org.opendatadiscovery.oddrn.model.OddrnPath;
import org.opendatadiscovery.oddrn.model.SnowflakePath;

public class SnowflakeJdbcProcessor implements JdbcProcessor<SnowflakePath> {

    public static final String PREFIX = "snowflake";

    @Override
    public OddrnPath path(final String host, final String database) {
        return SnowflakePath.builder()
            .account(host.substring(0, host.indexOf(".")))
            .build();
    }

    @Override
    public String url(final SnowflakePath path, final int port) {
        return String.format("jdbc:%s://%s.snowflakecomputing.com", PREFIX, path.getAccount());
    }
}
