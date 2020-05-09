package de.hadri.moduleapps;

import de.hadri.moduleapps.log.Logger;
import de.hadri.moduleapps.log.LoggerModule;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author Hannes Drittler
 */
public class ModuleManager implements ModuleInitializer, ModuleRegister, ModuleProvider {

    private static final Logger LOGGER = LoggerModule.FACTORY.getLogger(ModuleManager.class);
    public static final String LOG_CHANNEL = "di";
    public static final String DI_PROPERTIES = "di.properties";
    private Properties dependencyInjectionConfig;
    private Map<Class<? extends Module>, List<Module>> moduleDependencySupport;

    private ConfigurationProvider appConfigProvider;

    public ModuleManager() {}

    public ModuleProvider initializeModules(Collection<Module> modules) {
        moduleDependencySupport = new HashMap<>();
        modules.forEach(module -> module.registerNewModule(this));
        modules.forEach(module -> module.initConfiguration(appConfigProvider));
        modules.forEach(module -> module.solveDependencies(this));
        return this;
    }

    @Override
    public void register(Module providedModule, Class<? extends Module> providedFor) {
        List<Module> dependencySupport = new ArrayList<>();
        if (moduleDependencySupport.containsKey(providedFor)) {
            dependencySupport = moduleDependencySupport.get(providedFor);
        }
        dependencySupport.add(providedModule);
        moduleDependencySupport.put(providedFor, dependencySupport);
    }

    /**
     *
     * @param requestedClass
     * @param requesterClass
     * @param <DEP>
     * @param <REQ>
     * @return
     */
    @Override
    public <DEP extends Module, REQ extends Module> Optional<DEP> requestModule(Class<DEP> requestedClass, Class<REQ> requesterClass) {
        Optional<DEP> confDependency = requestModuleByConfig(requestedClass, requesterClass, getDiProperties());
        if (confDependency.isPresent()) {
            return confDependency;
        }
        return (Optional<DEP>) getSolvedDependencyStream(requesterClass)
                .filter(requestedClass::isInstance)
                .findFirst().or(() -> {
                    LOGGER.warn("Cannot find " + requestedClass + " for " + requesterClass, LOG_CHANNEL);
                    return Optional.empty();
                });
    }

    protected Stream<? extends Module> getSolvedDependencyStream(Class<? extends Module> requesterClass) {
        return getAllParents(requesterClass).stream()
                .filter(reqrClassParent -> moduleDependencySupport.containsKey(reqrClassParent))
                .flatMap(reqrClassParent -> moduleDependencySupport.get(reqrClassParent).stream());
    }

    protected List<Class<?>> getAllParents(Class<?> cls) {
        Collection<Class<?>> parentsFound = new LinkedHashSet<>();
        getAllParents(cls, parentsFound);
        return new ArrayList<>(parentsFound);
    }

    protected static void getAllParents(Class<?> cls, Collection<Class<?>> parentsFound) {
        while(cls != null) {
            parentsFound.add(cls);
            Class<?>[] interfaces = cls.getInterfaces();
            for (Class<?> i : interfaces) {
                if (parentsFound.add(i)) {
                    getAllParents(i, parentsFound);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    /**
     *
     * @param requestedClass
     * @param requesterClass will be ignored
     * @param config
     * @param <DEP>
     * @param <REQ>
     * @return
     */
    @Override
    public <DEP extends Module, REQ extends Module> Optional<DEP> requestModuleByConfig(Class<DEP> requestedClass, Class<REQ> requesterClass, Properties config) {
        String configRequestedClassName = config.getProperty(requesterClass.getName() + "/" + requestedClass.getName());
        if (configRequestedClassName == null) {
            return Optional.empty();
        }
        return (Optional<DEP>) getSolvedDependencyStream(requesterClass)
                .filter(module -> module.getClass().getName().equals(configRequestedClassName))
                .filter(requestedClass::isInstance)
                .findFirst();
    }

    @Override
    public ModuleInitializer setConfigurationProvider(ConfigurationProvider provider) {
        appConfigProvider = provider;
        return this;
    }

    /**
     * Checkout https://stackoverflow.com/questions/3231797/specify-system-property-to-maven-project to introduce dependencies defined in pom.xml
     * @return Format: class.of.Required = class.of.Resolved
     */
    public Properties getDiProperties() {
        if (dependencyInjectionConfig == null) {
            dependencyInjectionConfig = new Properties();
            try {
                dependencyInjectionConfig.load(getClass().getClassLoader().getResourceAsStream(DI_PROPERTIES));
            } catch (Exception e) {
                // no di.properties available
            }
        }
        return dependencyInjectionConfig;
    }
}
