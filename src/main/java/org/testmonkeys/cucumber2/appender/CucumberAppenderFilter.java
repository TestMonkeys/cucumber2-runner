package org.testmonkeys.cucumber2.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class CucumberAppenderFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        return iLoggingEvent.getLoggerName().contains("CucumberLogsFormatter") ? FilterReply.DENY : FilterReply.ACCEPT;
    }
}
