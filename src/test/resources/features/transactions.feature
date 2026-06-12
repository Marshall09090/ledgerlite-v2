Feature: Transactions API
  As a user of LedgerLite
  I want to manage transactions through the API
  So that I can record my income and expenses
  Scenario: Get all transactions returns a successful response
    When I request all transactions
    Then the response status should be 200

  Scenario: Creating a transaction with valid data succeeds
    When I create a transaction with description "Salary" amount 5000 type "INCOME"
    Then the response status should be 201

  Scenario: Creating a transaction with invalid data is rejected
    When I create a transaction with description "" amount -50 type ""
    Then the response status should be 400