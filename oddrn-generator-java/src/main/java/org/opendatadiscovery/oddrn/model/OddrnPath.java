package org.opendatadiscovery.oddrn.model;

import org.opendatadiscovery.oddrn.Generator;
import org.opendatadiscovery.oddrn.exception.GenerateException;

public interface OddrnPath {
    String prefix();

    String name();

    default String oddrn() throws GenerateException {
        return Generator.getInstance().generate(this);
    }
}
