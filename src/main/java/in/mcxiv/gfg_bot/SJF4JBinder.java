package in.mcxiv.gfg_bot;

import com.mcxiv.logger.decorations.Format;
import com.mcxiv.logger.formatted.FLog;
import com.mcxiv.logger.tables.Table;
import com.mcxiv.logger.tools.LogLevel;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class SJF4JBinder implements ILoggerFactory {

//        LogLevel.OFF    NONE
//        LogLevel.VITAL
//        LogLevel.ERROR  ERROR
//        LogLevel.WARN   WARN
//        LogLevel.NOTICE INFO
//        LogLevel.DEBUG  DEBUG
//        LogLevel.ALL    TRACE

    private final FLog log;
    private final SLF4J_to_FLOG_Bridge bridge;

    public SJF4JBinder(FLog log) {
        this.log = log;
        this.bridge = new SLF4J_to_FLOG_Bridge();
    }

    @Override
    public Logger getLogger(String name) {
        return bridge;
    }

    @Format("::slf -> :bn:")
    public final class SLF4J_to_FLOG_Bridge extends MarkerIgnoringBase {

        public void logIt(LogLevel logLevel, String msg) {
            if (logLevel.accepted())
                log.prt(msg);
        }

        public void logIt(LogLevel logLevel, String format, Object arg) {
            if (logLevel.accepted())
                log.prt(MessageFormatter.format(format, arg).getMessage());
        }

        public void logIt(LogLevel logLevel, String format, Object arg1, Object arg2) {
            if (logLevel.accepted())
                log.prt(MessageFormatter.format(format, arg1, arg2).getMessage());
        }

        public void logIt(LogLevel logLevel, String format, Object... arguments) {
            if (logLevel.accepted())
                log.prt(MessageFormatter.format(format, arguments).getMessage());
        }

        public void logIt(LogLevel logLevel, String msg, Throwable t) {
            if (logLevel.accepted()) {
                log.prt(msg);
                Table.tabulate(log, t);
            }
        }

        @Override
        public boolean isTraceEnabled() {
            return LogLevel.ALL.accepted();
        }

        @Override
        public void trace(String msg) {
            logIt(LogLevel.ALL, msg);
        }

        @Override
        public void trace(String format, Object arg) {
            logIt(LogLevel.ALL, format, arg);
        }

        @Override
        public void trace(String format, Object arg1, Object arg2) {
            logIt(LogLevel.ALL, format, arg1, arg2);
        }

        @Override
        public void trace(String format, Object... arguments) {
            logIt(LogLevel.ALL, format, arguments);
        }

        @Override
        public void trace(String msg, Throwable t) {
            logIt(LogLevel.ALL, msg, t);
        }

        @Override
        public boolean isDebugEnabled() {
            return LogLevel.DEBUG.accepted();
        }

        @Override
        public void debug(String msg) {
            logIt(LogLevel.DEBUG, msg);
        }

        @Override
        public void debug(String format, Object arg) {
            logIt(LogLevel.DEBUG, format, arg);
        }

        @Override
        public void debug(String format, Object arg1, Object arg2) {
            logIt(LogLevel.DEBUG, format, arg1, arg2);
        }

        @Override
        public void debug(String format, Object... arguments) {
            logIt(LogLevel.DEBUG, format, arguments);
        }

        @Override
        public void debug(String msg, Throwable t) {
            logIt(LogLevel.DEBUG, msg, t);
        }

        @Override
        public boolean isInfoEnabled() {
            return LogLevel.NOTICE.accepted();
        }

        @Override
        public void info(String msg) {
            logIt(LogLevel.NOTICE, msg);
        }

        @Override
        public void info(String format, Object arg) {
            logIt(LogLevel.NOTICE, format, arg);
        }

        @Override
        public void info(String format, Object arg1, Object arg2) {
            logIt(LogLevel.NOTICE, format, arg1, arg2);
        }

        @Override
        public void info(String format, Object... arguments) {
            logIt(LogLevel.NOTICE, format, arguments);
        }

        @Override
        public void info(String msg, Throwable t) {
            logIt(LogLevel.NOTICE, msg, t);
        }

        @Override
        public boolean isWarnEnabled() {
            return LogLevel.WARN.accepted();
        }

        @Override
        public void warn(String msg) {
            logIt(LogLevel.WARN, msg);
        }

        @Override
        public void warn(String format, Object arg) {
            logIt(LogLevel.WARN, format, arg);
        }

        @Override
        public void warn(String format, Object arg1, Object arg2) {
            logIt(LogLevel.WARN, format, arg1, arg2);
        }

        @Override
        public void warn(String format, Object... arguments) {
            logIt(LogLevel.WARN, format, arguments);
        }

        @Override
        public void warn(String msg, Throwable t) {
            logIt(LogLevel.WARN, msg, t);
        }

        @Override
        public boolean isErrorEnabled() {
            return LogLevel.ERROR.accepted();
        }

        @Override
        public void error(String msg) {
            logIt(LogLevel.ERROR, msg);
        }

        @Override
        public void error(String format, Object arg) {
            logIt(LogLevel.ERROR, format, arg);
        }

        @Override
        public void error(String format, Object arg1, Object arg2) {
            logIt(LogLevel.ERROR, format, arg1, arg2);
        }

        @Override
        public void error(String format, Object... arguments) {
            logIt(LogLevel.ERROR, format, arguments);
        }

        @Override
        public void error(String msg, Throwable t) {
            logIt(LogLevel.ERROR, msg, t);
        }
    }
}
