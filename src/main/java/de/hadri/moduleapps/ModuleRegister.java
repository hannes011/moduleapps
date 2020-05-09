package de.hadri.moduleapps;

/**
 * @author Hannes Drittler
 */
public interface ModuleRegister {

    /**
     * This method is used by {@link Module#registerNewModule(ModuleRegister)} to provide their own interface implementation
     * to other modules
     * @param providedModule Module instance to provide solving dependencies of other modules (of the currently registering module)
     * @param providedFor Class or interface to provide the providedModule for (this applies also for its children). It
     *                    can be used to restrict the provided module instance to the usage of special modules.
     */
    void register(Module providedModule, Class<? extends Module> providedFor);

    /**
     * This method is used by {@link Module#registerNewModule(ModuleRegister)} to provide their own interface implementation
     * to other modules
     * @param providedModule Module instance to provide solving dependencies of other modules (of the currently registering module)
     */
    default void register(Module providedModule) {
        register(providedModule, Module.class);
    }

}
