package com.ledgerlite.ledgerlite_v2.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TransactionSteps {

    private Response response;
    @LocalServerPort
    private int port;

    private int statusCode;

    @When("I request all transactions")
    public void i_request_all_transactions() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/transactions"))
                .GET()
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        statusCode = httpResponse.statusCode();
    }
    @When("I delete a transaction that does not exist")
    public void i_delete_a_transaction_that_does_not_exist() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/transactions/999999"))
                .DELETE()
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        statusCode = httpResponse.statusCode();
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
                .post("http://localhost:" + port + "/api/transactions");
        statusCode = response.getStatusCode();
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        assertEquals(expectedStatus, statusCode);
    }
}
