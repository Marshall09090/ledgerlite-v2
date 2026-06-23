# LedgerLite v2 — Test Plan

## 1. Purpose
This document describes the testing strategy for the LedgerLite v2
transaction API. It covers what is tested, at which level, and how the
tests are run locally and in CI.

## 2. Scope
In scope: the Transaction REST API (create, read, delete) and its
service and repository layers.
Out of scope: authentication, the React Native client, and reporting.

## 3. Test Levels

| Level | What it checks | Tooling | Example |
|-------|----------------|---------|---------|
| Unit | A single class in isolation, dependencies mocked | JUnit 5 + Mockito | `TransactionServiceTest` |
| Integration | Controller and repository against a real Spring context and H2 database | JUnit 5 + Spring Boot Test + MockMvc | `TransactionControllerTest`, `TransactionRepositoryTest` |
| Acceptance | The running API end to end, described in business language | Cucumber + JUnit Platform | `transactions.feature` |
| UI | The transaction page in a real browser | Selenium WebDriver | `TransactionUiTest` |

## 4. Acceptance Scenarios
1. Request all transactions returns a success response.
2. Creating a transaction with valid data returns 201 Created.
3. Creating a transaction with invalid data returns 400 Bad Request.
4. Deleting a transaction that does not exist returns 404 Not Found.

## 5. How to Run

Run the full suite:
./mvnw test

Run only the acceptance tests:
./mvnw test -Dtest=CucumberRunnerTest

## 6. Continuous Integration
Every push and pull request triggers the GitHub Actions pipeline
(`.github/workflows/ci.yml`), which runs the full test suite on a clean
Ubuntu runner with Java 21. The Selenium UI test is disabled in CI
(via `@DisabledIfEnvironmentVariable(named = "CI", matches = "true")`)
because the runner has no browser; it runs locally only.

## 7. Environment
- Java 21 (Temurin)
- Spring Boot, runs on port 8081 locally
- H2 in-memory database for tests