package de.hadri.moduleapps;

import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * @author Hannes Drittler
 */
public interface ModuleInitializer {

    ModuleProvider initializeModules(Collection<Module> modules);

    default ModuleProvider initializeModules() {
        return initializeModules(loadModules());
    }

    static List<Module> loadModules() {
        return ServiceLoader.load(Module.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    default ModuleInitializer setConfigurationProvider(ConfigurationProvider provider) {
        // optional implementation - only needed when #initializeModules() requires this
        return this;
    }
}
