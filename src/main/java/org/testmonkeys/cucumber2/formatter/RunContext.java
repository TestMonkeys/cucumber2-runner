package org.testmonkeys.cucumber2.formatter;

import cucumber.api.TestCase;
import gherkin.ast.*;

import java.util.List;

public class RunContext {


    private static RunContext instance;

    private Feature currentFeature;
    private ScenarioDefinition currentScenario;

    private RunContext() {

    }

    public static RunContext getInstance() {
        if (instance == null)
            instance = new RunContext();

        return instance;
    }

    <T extends ScenarioDefinition> T getScenario(TestCase testCase) {
        List<ScenarioDefinition> featureScenarios = currentFeature.getChildren();
        for (ScenarioDefinition scenario : featureScenarios) {
            if (scenario instanceof Background) {
                continue;
            }
            if (testCase.getLine() == scenario.getLocation().getLine() && testCase.getName().equals(scenario.getName())) {
                return (T) scenario;
            } else {
                if (scenario instanceof ScenarioOutline) {
                    for (Examples example : ((ScenarioOutline) scenario).getExamples()) {
                        for (TableRow tableRow : example.getTableBody()) {
                            if (tableRow.getLocation().getLine() == testCase.getLine()) {
                                return (T) scenario;
                            }
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("Scenario can't be null!");
    }


    public static void setInstance(RunContext instance) {
        RunContext.instance = instance;
    }

    public Feature getCurrentFeature() {
        return currentFeature;
    }

    public void setCurrentFeature(Feature currentFeature) {
        this.currentFeature = currentFeature;
    }

    public void setCurrentScenario(ScenarioDefinition currentScenario) {
        this.currentScenario = currentScenario;
    }

    public ScenarioDefinition getCurrentScenario() {
        return currentScenario;
    }
}
