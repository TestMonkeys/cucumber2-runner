package org.testmonkeys.cucumber2.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TestLogHelper {

    private static final Logger logger = LoggerFactory.getLogger(TestLogHelper.class);
    private static final String TEST_NAME_KEY = "testname";
    public static String CURRENT_LOG_NAME = "";

    private TestLogHelper() {
    }

    public static void startTestLogging(String name) {
        MDC.put(TEST_NAME_KEY, name);
        CURRENT_LOG_NAME = name;
    }

    public static String stopTestLogging() {
        String name = MDC.get(TEST_NAME_KEY);
        MDC.remove(TEST_NAME_KEY);
        return name;
    }

    public static String getCurrentLogName() {
        String testname = MDC.get(TEST_NAME_KEY);
        return testname == null ? "test" : testname;
    }
}
