# Testing

This project includes two automated test suites.

## 1. Integration test (JUnit + Spring Boot Test)
TransactionRepositoryTest verifies the repository layer saves and
retrieves transactions against an in-memory H2 database, using the
@DataJpaTest annotation. It follows the Arrange, Act, Assert pattern.

## 2. API test (REST Assured)
TransactionApiTest starts the full application on a random port and
sends a real HTTP POST request to /api/transactions. It then asserts
that the response status code is 200 and that the returned JSON
contains the saved transaction, using a given/when/then structure.

## How to run the tests
Run all tests from the project root with:

    ./mvnw test