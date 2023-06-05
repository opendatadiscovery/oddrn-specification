package org.opendatadiscovery.oddrn.processor;

import org.opendatadiscovery.oddrn.model.OddrnPath;

public interface JdbcProcessor<T extends OddrnPath> {
    OddrnPath path(String host, String database);

    String url(T path, int port);
}
