package org.opendatadiscovery.oddrn.model;

public interface RdsPath {
    String getHost();

    String getDatabase();

    String getTable();

    String getView();

    String getColumn();
}
