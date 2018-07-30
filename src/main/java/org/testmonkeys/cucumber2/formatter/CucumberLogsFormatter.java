package org.testmonkeys.cucumber2.formatter;


import cucumber.api.PickleStepTestStep;
import cucumber.api.Plugin;
import cucumber.api.Result;
import cucumber.api.event.*;
import cucumber.api.formatter.Formatter;
import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ParserException;
import gherkin.TokenMatcher;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static cucumber.api.Result.Type.FAILED;

public class CucumberLogsFormatter implements Formatter, Plugin {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RunContext context = RunContext.getInstance();

    private EventHandler<TestSourceRead> testSourceReadHandler = this::handleTestSourceRead;
    private EventHandler<TestCaseStarted> caseStartedHandler = this::handleTestCaseStarted;
    private EventHandler<TestCaseFinished> caseFinisheddHandler = this::handleTestCaseFinished;
    private EventHandler<TestStepStarted> stepStartedHandler = this::handleTestStepStarted;
    private EventHandler<TestStepFinished> stepFinishedHandler = this::handleTestStepFinished;
    private EventHandler<TestRunFinished> runFinishedHandler = event -> finishReport();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, caseFinisheddHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        Feature feature = getFeature(event.source);
        context.setCurrentFeature(feature);
        logger.info("[FEATURE STARTED]:" + feature.getName() + "[" + event.uri + "]");
    }

    private Feature getFeature(String source) {
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        TokenMatcher matcher = new TokenMatcher();
        GherkinDocument gherkinDocument;
        try {
            gherkinDocument = parser.parse(source, matcher);
        } catch (ParserException e) {
            return null;
        }
        return gherkinDocument.getFeature();
    }

    private void finishReport() {
        //todo log execution summary
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.testStep instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;

            Optional<Step> first = context.getCurrentScenario().getSteps().stream()
                    .filter(s -> s.getLocation().getLine() == testStep.getStepLine()).findFirst();
            if (!first.isPresent())
                throw new RuntimeException("Failed to found step");

            Result.Type status = event.result.getStatus();
            //todo log duration
            if (status.equals(FAILED))
                logger.info("[STEP FAILED]:" + first.get().getKeyword() + testStep.getStepText(), event.result.getError());
            else
                logger.info("[STEP " + status.name() + "]:" + first.get().getKeyword() + testStep.getStepText() + System.lineSeparator());
        }
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.testStep instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            Optional<Step> first = context.getCurrentScenario().getSteps().stream()
                    .filter(s -> s.getLocation().getLine() == testStep.getStepLine()).findFirst();
            if (!first.isPresent())
                throw new RuntimeException("Failed to found step");

            logger.info("[STEP STARTED]:" + first.get().getKeyword() + testStep.getStepText());
        }

    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        ScenarioDefinition scenario = context.getScenario(event.testCase);
        int line = scenario.getLocation().getLine();
        if (scenario.getKeyword().equals("Scenario Outline")) {
            line = event.testCase.getLine();
        }
        context.setCurrentScenario(scenario);
        TestLogHelper.stopTestLogging();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        TestLogHelper.startTestLogging(format.format(new Date().getTime()) + "_" + scenario.getName() + "_" + line);
        logger.info("[TEST STARTED]:" + event.testCase.getName());
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        if (event.result.getStatus().equals(FAILED))
            logger.info("[TEST FAILED]" + event.testCase.getName(), event.result.getError());
        else
            logger.info("[TEST " + event.result.getStatus().name() + "]" + event.testCase.getName() + System.lineSeparator());
    }
}
