package org.testmonkeys.cucumber2.appender;

import cucumber.api.Scenario;

public class CucumberScenarioContext {

    private static CucumberScenarioContext instance;
    private Scenario scenario;

    private CucumberScenarioContext() {

    }

    public static synchronized CucumberScenarioContext getInstance() {
        if (instance == null)
            instance = new CucumberScenarioContext();

        return instance;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public void attachScreenshot(byte[] screenshot) {
        this.scenario.embed(screenshot, "image/png");
    }
}
