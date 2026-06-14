# LedgerLite v2 — Acceptance Testing Guide

This document explains the Cucumber BDD acceptance test suite on the `cucumber-tests` branch: what the pieces are, how they fit together, how to run them, and why certain technical decisions were made.

> For unit and controller-layer tests (Mockito, MockMvc), see `TESTING.md`.

---

## Overview

The acceptance tests verify the Transactions REST API end-to-end by:

1. Booting the **real Spring Boot application** on a **random free port** (no manually started server required)
2. Firing **real HTTP requests** at the live app
3. Asserting on the **actual HTTP status codes** returned

Because the full application stack runs (controller → service → repository → H2 database), these are true **acceptance tests**, not unit tests.

**Current status: 4 scenarios, all passing.**

| Scenario | Request | Expected Status |
|---|---|---|
| Get all transactions returns a successful response | `GET /api/transactions` | 200 |
| Creating a transaction with valid data succeeds | `POST /api/transactions` (valid body) | 201 |
| Creating a transaction with invalid data is rejected | `POST /api/transactions` (empty description, negative amount) | 400 |
| Deleting a transaction that does not exist is rejected | `DELETE /api/transactions/{id}` (nonexistent id) | 404 |

---

## How to Run

No server needs to be running. The tests start (and stop) the application themselves.

**From the terminal:**

```bash
./mvnw clean test -Dtest=CucumberRunnerTest
```

**From IntelliJ:** run the `CucumberRunnerTest` class (green ▶).

Expected result: `Tests run: 4, Failures: 0, Errors: 0` and `BUILD SUCCESS`.

---

## The Three Layers

### 1. Feature file — *what should happen*

`src/test/resources/features/transactions.feature`

Written in Gherkin (plain English). Example:

```gherkin
Scenario: Creating a transaction with valid data succeeds
  When I create a transaction with description "Salary" amount 5000 type "INCOME"
  Then the response status should be 201
```

Non-developers can read this file and understand exactly what the API promises.

### 2. Step definitions — *how to do it*

`src/test/java/com/ledgerlite/ledgerlite_v2/steps/TransactionSteps.java`

Each Gherkin line maps to a Java method through annotations:

- `@When("I request all transactions")` → performs the GET
- `@When("I create a transaction with description {string} amount {int} type {string}")` → performs the POST; the `{string}` / `{int}` placeholders capture values directly from the Gherkin sentence
- `@Then("the response status should be {int}")` → runs the assertion

### 3. Runner — *the ignition switch*

`src/test/java/com/ledgerlite/ledgerlite_v2/CucumberRunnerTest.java`

A JUnit Platform Suite class:

- `@Suite` + `@IncludeEngines("cucumber")` — makes the suite runnable by JUnit/Maven
- `@SelectClasspathResource("features")` — where the `.feature` files live
- `@ConfigurationParameter(GLUE_PROPERTY_NAME, ...)` — the package containing step definitions
- `@ConfigurationParameter(FILTER_TAGS_PROPERTY_NAME, "not @wip")` — skips any scenario tagged `@wip` (none currently; kept as a tool for parking in-progress scenarios)

---

## Spring Boot Bootstrap (the key design decision)

`src/test/java/com/ledgerlite/ledgerlite_v2/steps/CucumberSpringConfiguration.java`

```java
@CucumberContextConfiguration
@SpringBootTest(
        classes = LedgerliteV2Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CucumberSpringConfiguration {
}
```

- `@CucumberContextConfiguration` — tells Cucumber this class defines the Spring test context
- `@SpringBootTest(webEnvironment = RANDOM_PORT)` — boots the actual application on a random free port before scenarios run, and shuts it down after

This requires the `cucumber-spring` dependency (same version as the other Cucumber artifacts — Cucumber modules must share one version).

**Why random port instead of a manually started server on 8081:** an externally started server can be stale (serving old code), absent, or port-conflicted. Self-bootstrapping guarantees the tests always exercise the current code and can run anywhere — including CI — with zero setup.

The step class receives the port via injection:

```java
@LocalServerPort
private int port;
```

URLs are then built as `"http://localhost:" + port + "/api/transactions"`.

---

## HTTP Clients: why there are two

| Step | Client | Reason |
|-|-|---|
| POST scenarios | REST Assured | Standard choice; works correctly for these requests |
| GET and DELETE scenarios | JDK `java.net.http.HttpClient`| REST Assured's Groovy-based engine threw an internal `NullPointerException` (`Class.isAssignableFrom` inside `CsrfFilter`/`RequestSpecificationImpl`) on this GET in our environment. The failure persisted across REST Assured 5.3.2, 5.5.0, and 5.5.6, against a verified-healthy endpoint (confirmed via `curl`), indicating a library/environment incompatibility rather than an application bug. The JDK's built-in client has no external dependencies and resolved it cleanly. |

Both clients write the resulting status into one shared field:

```java
private int statusCode;
```

…and the single `@Then` step asserts against that field. This "shared whiteboard" pattern lets two different HTTP libraries feed one assertion step.

### Note for Spring Boot 4 users

Two Boot 4 module changes encountered (and worked around) during this work:

- `TestRestTemplate` moved to `org.springframework.boot.resttestclient` and its runtime support (`ClientHttpRequestFactoryBuilder`) lives in a module not present on this project's test classpath — constructing it threw `NoClassDefFoundError`. The JDK HttpClient avoids the issue entirely.
- `@LocalServerPort` import: `org.springframework.boot.test.web.server.LocalServerPort` (works as-is on this project).

---

## Runtime flow

```
./mvnw test -Dtest=CucumberRunnerTest
        │
        ▼
JUnit Platform starts the Cucumber engine (runner class)
        │
        ▼
cucumber-spring finds @CucumberContextConfiguration
        │
        ▼
Spring Boot starts LedgerliteV2Application on a random port
        │
        ▼
Cucumber reads transactions.feature, matches each line to a step method
        │
        ▼
Steps fire real HTTP requests at the live app; statuses recorded in statusCode
        │
        ▼
@Then asserts expected vs actual; app shuts down; results reported
```

---

## Adding a new scenario

1. Add the Gherkin scenario to `transactions.feature`
2. Run the suite — Cucumber prints a snippet for any unmatched step
3. Implement the new step method in `TransactionSteps` (write the resulting status into `statusCode` so the existing `@Then` works)
4. Re-run until green; commit only on green

Tag a scenario `@wip` to exclude it from the run while it's in progress.

---

## Troubleshooting

- **All scenarios fail at once** → almost always a Spring context startup failure, not a test logic failure. Find the deepest `Caused by:` in the stack trace; it names the root cause.
- **`NoSuchBeanDefinitionException` on an `@Autowired` field** → the bean isn't auto-configured in this Boot version; construct the object directly or add the providing module.
- **Stale results in IntelliJ after a `pom.xml` change** → reload Maven, or run from the terminal (`./mvnw clean test ...`), which always resolves dependencies fresh.
- **Port already in use** → shouldn't happen with `RANDOM_PORT`; if it does, ensure no leftover app instance is running.
