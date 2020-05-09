package de.hadri.moduleapps.log;

import java.util.Objects;

/**
 * @author Hannes Drittler
 */
public interface Logger {

    int DEBUG = 5;
    int INFO = 4;
    int WARN = 3;
    int ERROR = 2;
    int CRITICAL = 1;

    void log(String msg, int level, String channel);

    default void log(String msg) {
        log(msg, INFO, null);
    }

    default void log(Object msg, int level, String channel) {
        log(Objects.toString(msg), level, channel);
    }

    default void log(Object msg) {
        log(msg, INFO, null);
    }

    default void debug(Object msg) {
        log(msg, DEBUG, null);
    }
    default void info(Object msg) {
        log(msg, INFO, null);
    }
    default void warn(Object msg) {
        log(msg, WARN, null);
    }
    default void error(Object msg) {
        log(msg, ERROR, null);
    }
    default void critical(Object msg) {
        log(msg, CRITICAL, null);
    }

    default void debug(Object msg, String channel) {
        log(msg, DEBUG, channel);
    }
    default void info(Object msg, String channel) {
        log(msg, INFO, channel);
    }
    default void warn(Object msg, String channel) {
        log(msg, WARN, channel);
    }
    default void error(Object msg, String channel) {
        log(msg, ERROR, channel);
    }
    default void critical(Object msg, String channel) {
        log(msg, CRITICAL, channel);
    }
}
