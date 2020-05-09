package de.hadri.moduleapps;

import java.util.ServiceLoader;

/**
 * @author Hannes Drittler
 */
public abstract class AbstractApp implements App {

    private ModuleProvider moduleProvider;
    private ConfigurationProvider configProvider;

    public ModuleProvider initModuleProvider() {
        ModuleManager moduleManager = new ModuleManager();
        return ServiceLoader.load(ModuleInitializer.class).findFirst()
                .orElseGet(() -> moduleManager)
                .setConfigurationProvider(getConfigurationProvider())
                .initializeModules();
    }

    public ModuleProvider getModuleProvider() {
        if (moduleProvider == null) {
            moduleProvider = initModuleProvider();
        }
        return moduleProvider;
    }

    public ConfigurationProvider getConfigurationProvider() {
        if (configProvider == null) {
            configProvider = ServiceLoader.load(ConfigurationProvider.class).findFirst()
                    .orElseGet(() -> this::getConfiguration);
        }
        return configProvider;
    }
}
