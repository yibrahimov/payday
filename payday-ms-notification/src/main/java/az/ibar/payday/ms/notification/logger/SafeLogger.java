package az.ibar.payday.ms.notification.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;

/**
 * Safe logger
 */
public class SafeLogger {

    private final Logger logger;

    private SafeLogger(Logger logger) {
        this.logger = logger;
    }

    public static SafeLogger getLogger(Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        return new SafeLogger(logger);
    }

    private Object[] filterValues(Object... argArray) {
        return Arrays.stream(argArray)
                .map(a -> a == null ? null : filterValue(a)).toArray();
    }

    private Object filterValue(Object a) {
        return a instanceof Throwable ? a : HtmlUtils.htmlEscape(a.toString())
                .replace("\r", "&cr;")
                .replace("\n", "&lf;");
    }

    public String getName() {
        return logger.getName();
    }

    public void trace(String s, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(s, filterValues(args));
        }
    }

    public void debug(String s, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(s, filterValues(args));
        }
    }

    public void info(String s, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(s, filterValues(args));
        }
    }

    public void warn(String s, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(s, filterValues(args));
        }
    }

    public void error(String s, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(s, filterValues(args));
        }
    }
}
