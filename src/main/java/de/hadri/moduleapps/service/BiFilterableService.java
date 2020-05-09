package de.hadri.moduleapps.service;

/**
 * FilterableService - implemented by classes which should be loadable by {@link java.util.ServiceLoader}
 * @param <ID1> Type of identifier to distinct whether the service applies or not.
 * @param <ID2> Type of identifier to distinct whether the service applies or not.
 *
 * @author Hannes Drittler
 */
public interface BiFilterableService<ID1, ID2> extends FilterableService<ID1> {

    /**
     * Checks whether the service may be used for the current case identified by the given filterId.
     *
     * @param filterId1 identifier to filter what use case this service is applying
     * @param filterId2 identifier to filter what use case this service is applying
     * @return true when service applies for the given ID
     */
    default boolean appliesFor(ID1 filterId1, ID2 filterId2) {
        return appliesFor(filterId1);
    }
}
