package de.hadri.moduleapps;

/**
 * @author Hannes Drittler
 */
public interface Module {

    /**
     * Registers this module.
     * @param register registers this module.
     */
    default void registerNewModule(ModuleRegister register) {
        register.register(this);
    }

    /**
     * Initialisation method for all modules this module is depending on.
     * @param provider provides all registered modules.
     */
    default void solveDependencies(ModuleProvider provider) {
        // no requirements
    }

    /**
     * Initialize method for configuration required by this module.
     * @param provider provides all available app config with handy filter methods.
     */
    default void initConfiguration(ConfigurationProvider provider) {
        // filter and set configuration if required
    }
}
