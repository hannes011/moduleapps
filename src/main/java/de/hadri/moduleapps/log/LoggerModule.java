package de.hadri.moduleapps.log;

import de.hadri.moduleapps.Module;

import java.util.ServiceLoader;

/**
 * @author Hannes Drittler
 */
public interface LoggerModule extends Module {

    LoggerModule FACTORY = ServiceLoader.load(LoggerModule.class).findFirst()
            .orElse(src -> (msg, level, channel) -> (level < Logger.WARN ? System.err : System.out).println(msg));

    Logger getLogger(Class logSrcClass);
}
