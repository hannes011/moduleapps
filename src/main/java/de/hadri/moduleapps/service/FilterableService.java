package de.hadri.moduleapps.service;

/**
 * FilterableService - implemented by classes which should be loadable by {@link java.util.ServiceLoader}
 * @param <ID> Type of identifier to distinct whether the service applies or not.
 *
 * @author Hannes Drittler
 */
public interface FilterableService<ID> {

    /**
     * Checks whether the service may be used for the current case identified by the given filterId.
     *
     * @param filterId identifier to filter what use case this service is applying
     * @return true when service applies for the given ID
     */
    default boolean appliesFor(ID filterId) {
        return true; // applies to all
    }
}
