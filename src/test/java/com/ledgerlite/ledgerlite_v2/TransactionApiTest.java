package com.ledgerlite.ledgerlite_v2;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateAndGetTransaction() {
        // Create a transaction via POST
        given()
                .contentType("application/json")
                .body("{\"description\":\"Salary\",\"amount\":5000,\"type\":\"INCOME\"}")
                .when()
                .post("/api/transactions")
                .then()
                .statusCode(200)
                .body("description", equalTo("Salary"));
    }
}
