package de.hadri.moduleapps;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Hannes Drittler
 */
public interface ConfigurationProvider {

    String SCOPE_SEPARATOR = ".";

    Properties getAllConfigProperties();

    default Map<String, String> getFilteredConfig(Predicate<String> filter) {
        return getFilteredConfig(filter, key -> key);
    }

    default Map<String, String> getFilteredConfig(Predicate<String> filter, Function<String, String> mapper) {
        Properties prop = getAllConfigProperties();
        return prop.keySet().stream()
                .filter(key -> key instanceof String)
                .filter(key -> filter.test((String) key))
                .collect(Collectors.toMap(key -> mapper.apply((String) key), key -> prop.getProperty((String) key)));
    }

    default Map<String, String> getScopedConfig(String scopePrefix) {
        return getFilteredConfig(key -> key.startsWith(scopePrefix), key -> key.substring(scopePrefix.length()));
    }

    /*
        [siteID, routeID, content]
        # source.<siteID>.<routeID>.<attr> = value
        # source.<siteID>.route.<attr> = 1st fallback value
        # source.route.<attr> = 2nd fallback value
     */

    /**
     *
     * e.g.
     * source.siteID.routeID.content.attr = value
     * source.siteID.route.content.attr = 1st fallback value
     * source.route.content.attr = 2nd fallback value
     *
     * @param configPath  e.g. [ siteID, routeID, content]
     * @param prefix  e.g. "source."
     * @param scopePath  e.g. [ site, route, content]
     * @return  e.g. {"attr": "value"}
     */
    default Map<String, String> mergeScopedFallbackConfig(String[] configPath, String prefix, String... scopePath) {
        final String finalConfigPart;
        if (scopePath.length == configPath.length) {
            finalConfigPart = scopePath[scopePath.length - 1] + SCOPE_SEPARATOR;
        } else if (configPath.length > 0) {
            finalConfigPart = configPath[configPath.length - 1] + SCOPE_SEPARATOR;
        } else {
            finalConfigPart = "";
        }
        Map<String, String> config = new HashMap<>();
        StringBuilder prefixSB = new StringBuilder(prefix);
        for (String s : configPath) {
            config.putAll(this.getScopedConfig(prefixSB.toString() + finalConfigPart));
            prefixSB.append(s).append(SCOPE_SEPARATOR);
        }
        config.putAll(this.getScopedConfig(prefixSB.toString()));
        return config;
    }
}
