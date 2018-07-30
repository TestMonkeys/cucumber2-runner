package org.testmonkeys.cucumber2.formatter;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import org.junit.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reporting {
    private static final String PROJECT_NAME = "Sample Project";
    private static final boolean PARALLEL_TESTING = false;
    private static final boolean RUN_WITH_JENKINS = false;
    private static String jsonPath = "target/json-report";
    private static String targetPath = "target";

    public static void main(String[] args) {
        List<String> cukeJsonFiles = new ArrayList<>();

        for (File file : Objects.requireNonNull(new File(jsonPath).listFiles())) {
            cukeJsonFiles.add(file.getAbsolutePath());
        }

        File reportOutputDirectory = new File(targetPath);

        Configuration configuration = new Configuration(reportOutputDirectory, PROJECT_NAME);

        configuration.setParallelTesting(PARALLEL_TESTING);
        configuration.setRunWithJenkins(RUN_WITH_JENKINS);
        configuration.setBuildNumber("local");

        ReportBuilder reportBuilder = new ReportBuilder(cukeJsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();

        Assert.assertNotNull(result);
    }
}
