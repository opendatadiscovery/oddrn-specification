# Open Data Discovery Resource Name Generator
## Requirements
Java >= 11
## Installation
```
TBD
```
## Usage and configuration
### Available generators
* postgresql - PostgreSqlPath
* mysql - MysqlPath
* kafka - KafkaPath
* kafkaconnect - KafkaConnectorPath
* snowflake - SnowflakePath
* airflow - AirflowPath
* hive - HivePath
* dynamodb - DynamodbPath
* grpc - GrpcServicePath
* spark - SparkPath
* s3-aws - AwsS3Path
* s3-custom - CustomS3Path
* hdfs - HdfsPath

### Generator methods
* generate(OddrnPath path, String field) - Get oddrn string by path.
* validatePath(OddrnPath path, String field) - Validate oddrn path
* parse(String oddrn) - Parse oddrn and get OddrnPath

### Example usage
```java
# postgresql

final String oddrn = generator.generate(
    PostgreSqlPath.builder()
        .host("1.1.1.1")
        .database("dbname")
        .schema("public")
        .table("test")
        .column("id")
        .build(),
    , "column");

# //postgresql/host/1.1.1.1/databases/dbname/schemas/public/tables/test/columns/id

```

### Exceptions
* WrongPathOrderException - raises when trying set path that depends on another path
```java
final String oddrn = generator.generate(
    PostgreSqlPath.builder()
        .host("1.1.1.1")
        .database("dbname")
        .schema("public")
        .column("id")
        .build(),
    , "column");
# WrongPathOrderException: 'column' can not be without 'table' attribute
```
* PathDoestExistException - raises when trying to get not existing oddrn path
```java
final String oddrn = generator.generate(
    PostgreSqlPath.builder()
        .host("1.1.1.1")
        .database("dbname")
        .schema("public")
        .column("id")
        .build(),
    , "job");

# PathDoestExistException: Path 'job' doesn't exist in path
```