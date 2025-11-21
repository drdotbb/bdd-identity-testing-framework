package com.automation.identity.tests.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.automation.identity.tests.stepdefinitions","com.automation.identity.tests.hooks"},
    plugin = {"pretty", "html:target/cucumber-reports.html"},
    monochrome = true
)
public class CucumberTestRunner {
}
