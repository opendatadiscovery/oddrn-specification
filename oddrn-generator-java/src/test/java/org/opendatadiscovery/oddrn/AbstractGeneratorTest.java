package org.opendatadiscovery.oddrn;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.opendatadiscovery.oddrn.exception.GenerateException;
import org.opendatadiscovery.oddrn.model.OddrnPath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbstractGeneratorTest {
    public void shouldGeneratePath(final OddrnPath path, final String expected)
            throws GenerateException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        final String oddrn = path.oddrn();
        assertEquals(expected, oddrn);
        final Optional<OddrnPath> parse = Generator.getInstance().parse(oddrn);
        assertNotNull(parse);
        assertEquals(path, parse.get());
    }

    public void shouldFail(final OddrnPath path, final Class<? extends Exception> exception) {
        assertThrows(exception, path::oddrn);
    }
}
