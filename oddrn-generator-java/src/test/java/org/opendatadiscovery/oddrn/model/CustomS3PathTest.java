package org.opendatadiscovery.oddrn.model;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class CustomS3PathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateBucketPath() throws Exception {
        shouldGeneratePath(
                CustomS3Path.builder()
                        .endpoint("http://172.27.0.2:9000")
                        .bucket("my_bucket")
                        .build(),
                "//s3-custom/endpoint/http:\\\\\\\\172.27.0.2:9000/buckets/my_bucket"
        );
    }

    @Test
    public void shouldGenerateKeyPath() throws Exception {
        shouldGeneratePath(
                CustomS3Path.builder()
                        .endpoint("http://172.27.0.2:9000")
                        .bucket("my_bucket")
                        .key("file1")
                        .build(),
                "//s3-custom/endpoint/http:\\\\\\\\172.27.0.2:9000/buckets/my_bucket/keys/file1"
        );
    }

    @Test
    public void shouldGeneratePathKeyPath() throws Exception {
        shouldGeneratePath(
                CustomS3Path.builder()
                        .endpoint("http://172.27.0.2:9000")
                        .bucket("my_bucket")
                        .key("path/to/file1")
                        .build(),
                "//s3-custom/endpoint/http:\\\\\\\\172.27.0.2:9000/buckets/my_bucket/keys/path\\\\to\\\\file1"
        );
    }

    @Test
    public void shouldFailKeyPath() {
        shouldFail(
                CustomS3Path.builder()
                        .endpoint("http://172.27.0.2:9000")
                        .key(UUID.randomUUID().toString())
                        .build(),
                EmptyPathValueException.class
        );
    }
}
