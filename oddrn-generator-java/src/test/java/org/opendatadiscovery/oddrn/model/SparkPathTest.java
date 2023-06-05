package org.opendatadiscovery.oddrn.model;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class SparkPathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateJobPath() throws Exception {
        shouldGeneratePath(
            SparkPath.builder()
                .host("127.0.0.1")
                .job("etl")
                .build(),
            "//spark/host/127.0.0.1/jobs/etl"
        );
    }

    @Test
    public void shouldGenerateRunPath() throws Exception {
        shouldGeneratePath(
            SparkPath.builder()
                .host("127.0.0.1")
                .job("etl")
                .run("affjfkhx2p")
                .build(),
            "//spark/host/127.0.0.1/jobs/etl/runs/affjfkhx2p"
        );
    }

    @Test
    public void shouldFailRunPath() {
        shouldFail(
            SparkPath.builder()
                .host("127.0.0.1")
                .run(UUID.randomUUID().toString())
                .build(),
            EmptyPathValueException.class
        );
    }
}
