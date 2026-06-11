package com.ledgerlite.ledgerlite_v2.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionSteps {

    private Response response;

    @When("I request all transactions")
    public void i_request_all_transactions() {
        response = RestAssured
                .given()
                .accept("application/json")
                .when()
                .get("http://localhost:8081/api/transactions");
    }

    @When("I create a transaction with description {string} amount {int} type {string}")
    public void i_create_a_transaction(String description, int amount, String type) {
        String body = "{ \"description\": \"" + description + "\", \"amount\": " + amount
                + ", \"type\": \"" + type + "\", \"date\": \"2026-01-15\" }";

        response = RestAssured
                .given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("http://localhost:8081/api/transactions");
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
    }
}