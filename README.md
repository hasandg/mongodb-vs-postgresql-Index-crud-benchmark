# MongoDB vs PostgreSQL — Index/CRUD Benchmark

Performance of MongoDB and PostgreSQL databases with/without index using Docker containers.
Indexes are created with different configurations to compare their performance in CRUD operations.

## Prerequisites

- Java 24 or later
- Maven
- Docker and Docker Compose

## Setup

1. Clone the repository
2. Start the database containers :
   ```bash
   docker compose up -d
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the three Spring-Boot modules in separate terminals (or from your IDE):
   ```bash
   # Server hosting the benchmark endpoints on port 8081
   mvn -pl mongo_vs_postgres_server spring-boot:run

   # Manual client that triggers benchmarks on port 8082
   mvn -pl performance-client spring-boot:run

   # (Optional) automated client on port 8083
   mvn -pl performance-automated-client spring-boot:run
   ```

## API Endpoints

The application provides the following endpoints to test database performance:

1. Generate Test Data:
   ```
   POST /api/performance/generate-data/{count}
   ```
   Generates the specified number of test records in both databases.

2. Test Read Performance:
   ```
   GET /api/performance/read/{count}
   ```
   Tests read performance for both databases.

3. Test Update Performance:
   ```
   PUT /api/performance/update/{count}
   ```
   Tests update performance for both databases.

4. Test Delete Performance:
   ```
   DELETE /api/performance/delete/{count}
   ```
   Tests delete performance for both databases.

## Example Usage

1. Generate 1000 test records:
   ```bash
   curl -X POST http://localhost:8081/api/performance/generate-data/1000
   ```

2. Test read performance:
   ```bash
   curl -X GET http://localhost:8081/api/performance/read/1000
   ```

3. Test update performance:
   ```bash
   curl -X PUT http://localhost:8081/api/performance/update/1000
   ```

4. Test delete performance:
   ```bash
   curl -X DELETE http://localhost:8081/api/performance/delete/1000
   ```

## Observing Results

Each endpoint returns a JSON payload with the runtime (milliseconds) for MongoDB and PostgreSQL so you can compare:
* Insert performance
* Read performance (with and without query parameters)
* Update performance
* Delete performance

## Cleanup

Remove the containers:
```bash
docker compose down -v
``` 

