package de.hadri.moduleapps;

import de.hadri.moduleapps.log.Logger;
import de.hadri.moduleapps.log.LoggerModule;

import java.util.Properties;

/**
 * @author Hannes Drittler
 */
public interface App extends Module {

    Logger LOGGER = LoggerModule.FACTORY.getLogger(App.class);

    /**
     *
     * @return
     */
    ModuleProvider getModuleProvider();

    /**
     *
     * @return
     */
    Properties getConfiguration();
}
