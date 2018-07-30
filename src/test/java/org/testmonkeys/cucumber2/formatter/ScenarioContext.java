package org.testmonkeys.cucumber2.formatter;

public class ScenarioContext {

    private static ScenarioContext instance;

    private ScenarioContext() {

    }

    public static ScenarioContext getInstance() {
        if (instance == null)
            instance = new ScenarioContext();
        return instance;
    }
}
