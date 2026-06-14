package com.ledgerlite.ledgerlite_v2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
public class TransactionUiTest {


    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void addingATransactionShowsItInTheList() {
        driver.get("http://localhost:8081");

        driver.findElement(By.id("description")).sendKeys("Selenium Salary");
        driver.findElement(By.id("amount")).sendKeys("7777");

        driver.findElement(By.id("add-btn")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("transaction-item")));

        List<WebElement> items = driver.findElements(By.className("transaction-item"));
        boolean found = items.stream()
                .anyMatch(item -> item.getText().contains("Selenium Salary"));

        assertTrue(found, "Expected to find 'Selenium Salary' in the transaction list");
    }
}