package com.elmarangao.trackblaze.service;

import com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TrackFieldEntryAthleteDetails}.
 */
public interface TrackFieldEntryAthleteDetailsService {
    /**
     * Save a trackFieldEntryAthleteDetails.
     *
     * @param trackFieldEntryAthleteDetails the entity to save.
     * @return the persisted entity.
     */
    TrackFieldEntryAthleteDetails save(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails);

    /**
     * Updates a trackFieldEntryAthleteDetails.
     *
     * @param trackFieldEntryAthleteDetails the entity to update.
     * @return the persisted entity.
     */
    TrackFieldEntryAthleteDetails update(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails);

    /**
     * Partially updates a trackFieldEntryAthleteDetails.
     *
     * @param trackFieldEntryAthleteDetails the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrackFieldEntryAthleteDetails> partialUpdate(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails);

    /**
     * Get all the trackFieldEntryAthleteDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrackFieldEntryAthleteDetails> findAll(Pageable pageable);

    /**
     * Get the "id" trackFieldEntryAthleteDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrackFieldEntryAthleteDetails> findOne(Long id);

    /**
     * Delete the "id" trackFieldEntryAthleteDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
