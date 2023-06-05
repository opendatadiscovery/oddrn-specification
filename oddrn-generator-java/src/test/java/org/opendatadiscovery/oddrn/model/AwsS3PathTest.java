package org.opendatadiscovery.oddrn.model;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.opendatadiscovery.oddrn.AbstractGeneratorTest;
import org.opendatadiscovery.oddrn.exception.EmptyPathValueException;

public class AwsS3PathTest extends AbstractGeneratorTest {
    @Test
    public void shouldGenerateBucketPath() throws Exception {
        shouldGeneratePath(
                AwsS3Path.builder()
                        .bucket("my_bucket")
                        .build(),
                "//s3/cloud/aws/buckets/my_bucket"
        );
    }

    @Test
    public void shouldGenerateKeyPath() throws Exception {
        shouldGeneratePath(
                AwsS3Path.builder()
                        .bucket("my_bucket")
                        .key("file1")
                        .build(),
                "//s3/cloud/aws/buckets/my_bucket/keys/file1"
        );
    }

    @Test
    public void shouldGeneratePathKeyPath() throws Exception {
        shouldGeneratePath(
                AwsS3Path.builder()
                        .bucket("my_bucket")
                        .key("path/to/file1.csv")
                        .build(),
                "//s3/cloud/aws/buckets/my_bucket/keys/path\\\\to\\\\file1.csv"
        );
    }

    @Test
    public void shouldFailKeyPath() {
        shouldFail(
                AwsS3Path.builder()
                        .key(UUID.randomUUID().toString())
                        .build(),
            EmptyPathValueException.class
        );
    }
}
