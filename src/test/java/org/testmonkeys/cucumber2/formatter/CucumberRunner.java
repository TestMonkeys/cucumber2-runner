package org.testmonkeys.cucumber2.formatter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Unit test for simple App.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"org.testmonkeys.cucumber2.formatter"},
        plugin = {
//                "org.testmonkeys.cucumber2.formatter.CucumberLogsFormatter",
                "json:target/json-report/cucumber.json"
//                "org.testmonkeys.cucumber.ext.formatters.json.PerFeatureFormatter:target/json-report",
        }
)
public class CucumberRunner {

}
