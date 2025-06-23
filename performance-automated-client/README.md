# Automated Performance Testing Client

A Spring Boot application that automatically performs comprehensive performance testing between MongoDB and PostgreSQL databases.

## Overview

This automated client application provides a hands-off approach to performance testing by automatically running tests with different data sizes and operations. It stores test results in an H2 database and provides detailed metrics for analysis.

## Features

- Automatic test execution with configurable intervals
- Progressive test sizes (100, 500, 1000, 5000, 10000 records)
- All operations tested (generate-data, read, update, delete)
- Persistent storage of test results
- Detailed logging and metrics
- Error handling and recovery

## Prerequisites

- Java 24 or higher
- Maven 3.11 or higher
- Main application running on port 8081
- MongoDB and PostgreSQL instances

## Configuration

The application is configured to run on port 8083 and connect to the main application on port 8081. Configuration can be modified in `application.yml`:

```yaml
server:
  port: 8083

spring:
  application:
    name: performance-automated-client
  datasource:
    url: jdbc:h2:file:./test-results
    username: sa
    password: 
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true

logging:
  level:
    com.example.performance.automated: DEBUG
    org.springframework.web: INFO
```

## My Manual Test Results

### ✅ Test Summary for 1.000.000 Records
| Index Type                        | MongoDB Insert | Postgres Insert | MongoDB Query | Postgres Query |
| --------------------------------- | -------------- | --------------- | ------------- | -------------- |
| Without Index                     | 36,880 ms      | 260,064 ms      | 390 ms        | 38 ms          |
| Single Indexes                    | 39,580 ms      | 266,171 ms      | 50 ms         | 19 ms          |
| Composite Index (name → category) | 37,654 ms      | 259,112 ms      | 48 ms         | 18 ms          |
| Composite Index (category → name) | 37,122 ms      | 261,852 ms      | 35 ms         | 12 ms          |


```bash
curl -X POST http://localhost:8082/api/performance-test/run/generate-data/1000000
curl -X POST http://localhost:8082/api/performance-test/run/read-with-params/0\?name\=Product%2010\&category\=Category%202
```

### Storage Size

| Postgres Size | Mongo Size Total | Mongo Storage Size | Mongo Index Size |
|---------------|------------------|---------------------|------------------|
| 171MB         | 230MB            | 41MB                | 11MB             |
| 231MB         | 230MB            | 41MB                | 28MB             |
| 256MB         | 230MB            | 41MB                | 35MB             |
| 254MB         | 230MB            | 41MB                | 24MB             |

### ✅ Test Summary for 30,000 Records
| Index Type | MongoDB Insert | Postgres Insert | MongoDB Query | Postgres Query | MongoDB Update | Postgres Update | MongoDB Delete | Postgres Delete |
|------------|----------------|------------------|----------------|-----------------|----------------|------------------|----------------|------------------|
| Without Index                     | 1522ms         | 8006ms           | 26ms           | 5ms             | 14699ms        | 539288ms         | 11380ms        | 303312ms         |
| Single indexes                    | 1286ms         | 7752ms           | 4ms            | 3ms             | 15056ms        | 530238ms         | 12669ms        | 306047ms         |
| Composite index (name → category) | 1191ms         | 7910ms           | 4ms            | 3ms             | 15706ms        | 533981ms         | 12708ms        | 314685ms         |
| Composite index (category → name) | 1261ms         | 8332ms           | 5ms            | 4ms             | 14838ms        | 577630ms         | 12214ms        | 310329ms         |


```bash
curl -X POST http://localhost:8082/api/performance-test/run/generate-data/30000
curl -X POST http://localhost:8082/api/performance-test/run/read-with-params/0?name=Product%2010&category=Category%202
curl -X POST http://localhost:8082/api/performance-test/run/update/30000
curl -X POST http://localhost:8082/api/performance-test/run/delete/30000
```

## Auto Test Phases

The automated client runs tests in phases, with each phase increasing the number of records:

1. Phase 1: 100 records
2. Phase 2: 500 records
3. Phase 3: 1000 records
4. Phase 4: 5000 records
5. Phase 5: 10000 records

For each phase, the following operations are tested:
- Generate test data
- Read performance
- Update performance
- Delete performance

## Test Execution

Tests are automatically executed every 5 seconds (configurable in `TestSchedulerService`). The client:
1. Runs one test at a time
2. Stores results in H2 database
3. Moves to the next operation
4. After completing all operations, moves to the next phase
5. Logs all activities and results

## Building and Running

1. Build the application:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

## Dependencies

* Spring Boot WebFlux
* Spring Boot Data JPA
* H2 Database
* Jackson Databind

## Test Results

Test results are stored in the H2 database and include:
- Operation type
- Record count
- MongoDB execution time
- PostgreSQL execution time
- Timestamp
- Status
- Error message (if any)
- Test phase
- Performance ratio

## Monitoring

This module no longer exports Prometheus metrics. Hook up your own monitoring stack if required.

## Error Handling

The application includes comprehensive error handling:
- Failed tests are logged with detailed error messages
- Test results include status and error information
- Asynchronous execution prevents test failures from affecting other tests
- Automatic retry mechanism for failed tests

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 