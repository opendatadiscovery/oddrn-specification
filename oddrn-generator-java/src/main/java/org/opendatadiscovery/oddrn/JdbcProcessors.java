package org.opendatadiscovery.oddrn;

import java.util.Map;
import org.opendatadiscovery.oddrn.model.ClickhousePath;
import org.opendatadiscovery.oddrn.model.HivePath;
import org.opendatadiscovery.oddrn.model.MysqlPath;
import org.opendatadiscovery.oddrn.model.OddrnPath;
import org.opendatadiscovery.oddrn.model.PostgreSqlPath;
import org.opendatadiscovery.oddrn.model.SnowflakePath;
import org.opendatadiscovery.oddrn.processor.ClickhouseJdbcProcessor;
import org.opendatadiscovery.oddrn.processor.Hive2JdbcProcessor;
import org.opendatadiscovery.oddrn.processor.JdbcProcessor;
import org.opendatadiscovery.oddrn.processor.MysqlJdbcProcessor;
import org.opendatadiscovery.oddrn.processor.PostgreSqlJdbcProcessor;
import org.opendatadiscovery.oddrn.processor.SnowflakeJdbcProcessor;

public class JdbcProcessors {
    private final Map<String, JdbcProcessor<? extends OddrnPath>> processorMap = Map.of(
        MysqlJdbcProcessor.PREFIX, new MysqlJdbcProcessor(),
        PostgreSqlJdbcProcessor.PREFIX, new PostgreSqlJdbcProcessor(),
        SnowflakeJdbcProcessor.PREFIX, new SnowflakeJdbcProcessor(),
        Hive2JdbcProcessor.PREFIX, new Hive2JdbcProcessor(),
        ClickhouseJdbcProcessor.PREFIX, new ClickhouseJdbcProcessor()
    );

    private final Map<Class<? extends OddrnPath>, JdbcProcessor<? extends OddrnPath>> processorMapByClass = Map.of(
        MysqlPath.class, new MysqlJdbcProcessor(),
        PostgreSqlPath.class, new PostgreSqlJdbcProcessor(),
        SnowflakePath.class, new SnowflakeJdbcProcessor(),
        HivePath.class, new Hive2JdbcProcessor(),
        ClickhousePath.class, new ClickhouseJdbcProcessor()
    );

    public OddrnPath path(final String driver, final String host, final String database) {
        final JdbcProcessor<?> processor = processorMap.get(driver);
        if (processor != null) {
            return processor.path(host, database);
        } else {
            return null;
        }
    }

    public  <T extends OddrnPath> String url(final T path, final int port) {
        final JdbcProcessor<T> processor = (JdbcProcessor<T>) processorMapByClass.get(path.getClass());
        if (processor != null) {
            return processor.url(path, port);
        } else {
            return null;
        }
    }
}
