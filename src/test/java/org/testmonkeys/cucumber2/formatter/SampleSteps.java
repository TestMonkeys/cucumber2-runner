package org.testmonkeys.cucumber2.formatter;

import cucumber.api.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleSteps {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScenarioContext scenarioContext = ScenarioContext.getInstance();

    @Given("^this is a sample '(.*)' step$")
    public void thisIsASampleColumnStep(String value) {
        logger.info(value);
    }
}
