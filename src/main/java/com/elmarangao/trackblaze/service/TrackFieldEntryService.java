package com.elmarangao.trackblaze.service;

import com.elmarangao.trackblaze.domain.TrackFieldEntry;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TrackFieldEntry}.
 */
public interface TrackFieldEntryService {
    /**
     * Save a trackFieldEntry.
     *
     * @param trackFieldEntry the entity to save.
     * @return the persisted entity.
     */
    TrackFieldEntry save(TrackFieldEntry trackFieldEntry);

    /**
     * Updates a trackFieldEntry.
     *
     * @param trackFieldEntry the entity to update.
     * @return the persisted entity.
     */
    TrackFieldEntry update(TrackFieldEntry trackFieldEntry);

    /**
     * Partially updates a trackFieldEntry.
     *
     * @param trackFieldEntry the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrackFieldEntry> partialUpdate(TrackFieldEntry trackFieldEntry);

    /**
     * Get all the trackFieldEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrackFieldEntry> findAll(Pageable pageable);

    /**
     * Get all the trackFieldEntries with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrackFieldEntry> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" trackFieldEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrackFieldEntry> findOne(Long id);

    /**
     * Delete the "id" trackFieldEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
