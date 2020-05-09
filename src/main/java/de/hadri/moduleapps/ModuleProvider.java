package de.hadri.moduleapps;

import java.util.Optional;
import java.util.Properties;

/**
 * @author Hannes Drittler
 */
public interface ModuleProvider {

    /**
     * This method is supposed to solve all dependencies of a module. The returned {@link Optional} contains either an
     * instance of the given {@code requestedClass} or gives you the opportunity to implement a fallback action in case
     * that the module is missing.
     * The instance is the one which was initially handed over as first parameter to {@link ModuleRegister#register(Module, Class)}.
     * The second parameter {@code providedFor} must fit to the parameter {@code requesterClass} or its implemented interfaces/superclasses.
     *
     * The default implementation of this interface is checking first {@link #requestModuleByConfig(Class, Class, Properties)}
     * with {@code di.properties} as parameter {@code config} in order to solve configured dependencies first.
     *
     * @param requestedClass This interface (or class) be implemented by the requested dependency.
     * @param requesterClass The class of the requesting module. It (or its parents) must meet the {@code requesterClass}
     *                       handed over to {@link ModuleRegister#register(Module, Class)}.
     * @param <DEP> Requested dependency
     * @param <REQ> Requesting class
     * @return An {@link Optional} containing the instance of the requested class if available.
     */
    <DEP extends Module, REQ extends Module> Optional<DEP> requestModule(Class<DEP> requestedClass, Class<REQ> requesterClass);

    /**
     * This method is supposed to solve all dependencies of a module. The returned {@link Optional} contains either an
     * instance of the given {@code requestedClass} or gives you the opportunity to implement a fallback action in case
     * that the module is missing.
     * The instance is the one which was initially handed over as first parameter to {@link ModuleRegister#register(Module, Class)}.
     *
     * @param requestedClass This interface (or class) be implemented by the requested dependency.
     * @param requesterClass The class of the requesting module. (Depending on the implementation this parameter might be ignored.)
     * @param config This properties are used to map dependencies to requester. Format: requester.class.Name/requested.class.Name = solving.requested.class.Name
     * @param <DEP> Requested dependency
     * @param <REQ> Requesting class
     * @return An {@link Optional} containing the instance of the requested class if available.
     */
    <DEP extends Module, REQ extends Module> Optional<DEP> requestModuleByConfig(Class<DEP> requestedClass, Class<REQ> requesterClass, Properties config);
}
