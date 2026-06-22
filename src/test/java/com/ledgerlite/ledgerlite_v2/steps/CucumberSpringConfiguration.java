package com.ledgerlite.ledgerlite_v2.steps;

import com.ledgerlite.ledgerlite_v2.LedgerliteV2Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;



@CucumberContextConfiguration
@SpringBootTest(
        classes = LedgerliteV2Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CucumberSpringConfiguration {
}