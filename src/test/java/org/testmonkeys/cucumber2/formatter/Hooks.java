package org.testmonkeys.cucumber2.formatter;


import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testmonkeys.cucumber2.appender.CucumberScenarioContext;

import java.io.IOException;

public class Hooks {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ScenarioContext scenarioContext = ScenarioContext.getInstance();

    @Before
    public void before(Scenario scenario) {
        CucumberScenarioContext.getInstance().setScenario(scenario);
        logger.info("[BEFORE]");
    }

    @After
    public void after(Scenario scenario) throws IOException {
        logger.info("[AFTER]");
        if (scenario.isFailed()) {
            //TODO add here failure handling
        }
    }


}
