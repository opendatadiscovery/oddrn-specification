package org.opendatadiscovery.oddrn;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;
import org.opendatadiscovery.oddrn.model.OddrnPath;

public class JdbcUrlParser {
    private static final String JDBC_PREFIX = "jdbc:";
    private static final String SEPARATOR = "://";
    private static final String DEFAULT_DB_NAME = "default";

    private final JdbcProcessors jdbcProcessors = new JdbcProcessors();

    public OddrnPath parse(final String url) {
        return parse(url, DEFAULT_DB_NAME);
    }

    public OddrnPath parse(final String url, final String user) {
        if (url == null || !url.startsWith(JDBC_PREFIX) || url.indexOf(':', JDBC_PREFIX.length()) == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        final int separatorPos = url.indexOf(SEPARATOR, JDBC_PREFIX.length());
        if (separatorPos == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        final String urlSecondPart = url.substring(separatorPos + SEPARATOR.length());
        final String driver = url.substring(JDBC_PREFIX.length(), separatorPos);
        final int dbPos = urlSecondPart.indexOf("/");
        int paramsPos = urlSecondPart.indexOf("?");
        final int semicolonParamsPos = urlSecondPart.indexOf(";");

        if (semicolonParamsPos > 0 && semicolonParamsPos < paramsPos) {
            paramsPos = semicolonParamsPos;
        } else if (paramsPos == -1 && semicolonParamsPos > 0) {
            paramsPos = semicolonParamsPos;
        }

        final String host;
        final String database;

        if (dbPos < 0 && paramsPos > 0) {
            // cases:
            // jdbc:mysql://localhost?params

            host = urlSecondPart.substring(0, paramsPos);
            database = user;
        } else if (dbPos > 0 && paramsPos > 0) {
            // cases: jdbc:mysql://localhost/dbName?params

            host = urlSecondPart.substring(0, dbPos);
            database = urlSecondPart.substring(dbPos + 1, paramsPos);
        } else if (dbPos > 0 && paramsPos < 0) {
            // cases: jdbc:mysql://localhost/dbName

            host = urlSecondPart.substring(0, dbPos);
            database = urlSecondPart.substring(dbPos + 1);
        } else {
            // cases: jdbc:mysql://localhost

            host = urlSecondPart;
            database = null;
        }

        final HostUserPath hostUserPath = parseHostUserPath(host);
        final String generatedHost = hostUserPath.host;
        final String generatedUser = hostUserPath.user != null ? hostUserPath.user : user;
        final String generatedDatabase;
        if ((database == null || database.isEmpty()) && generatedUser != null) {
            generatedDatabase = generatedUser;
        } else {
            generatedDatabase = database;
        }

        return jdbcProcessors.path(driver, generatedHost, generatedDatabase);
    }

    private HostUserPath parseHostUserPath(final String host) {
        final int userPassPos = host.indexOf("@");
        final String joinedHost;
        if (host.contains(",")) {
            final String[] hosts = host.split(",");
            joinedHost = Arrays.stream(hosts)
                .map(h -> replacePort(h, userPassPos))
                .collect(Collectors.joining(","));
        } else {
            joinedHost = replacePort(host, userPassPos);
        }

        if (userPassPos > 0) {
            final String newHost = joinedHost.substring(userPassPos + 1);
            final String userPass = joinedHost.substring(0, userPassPos);
            final int passPos = userPass.indexOf(":");
            final String user = passPos > 0 ? userPass.substring(0, passPos) : userPass;
            return new HostUserPath(newHost, user);
        } else {
            return new HostUserPath(joinedHost, null);
        }
    }

    private String replacePort(final String host, final int userPassPos) {
        final int portPos = host.lastIndexOf(":");
        return portPos > userPassPos ? host.substring(0, portPos) : host;
    }

    @Data
    private static class HostUserPath {
        private final String host;
        private final String user;
    }
}
