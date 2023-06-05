package org.opendatadiscovery.oddrn.model;

import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class GrpcServicePathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateDatabasePath() throws Exception {
        shouldGeneratePath(
            GrpcServicePath.builder()
                .host("1.1.1.1")
                .service("helloworld")
                .method("call")
                .build(),
            "//grpc/host/1.1.1.1/services/helloworld/methods/call"
        );
    }

    @Test
    public void shouldFailTablePath() {
        shouldFail(
            GrpcServicePath.builder()
                .host("1.1.1.1")
                .method("call")
                .build(),
            EmptyPathValueException.class
        );
    }
}
