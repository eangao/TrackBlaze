package com.elmarangao.trackblaze.service.impl;

import com.elmarangao.trackblaze.domain.TrackFieldEntry;
import com.elmarangao.trackblaze.repository.TrackFieldEntryRepository;
import com.elmarangao.trackblaze.service.TrackFieldEntryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TrackFieldEntry}.
 */
@Service
@Transactional
public class TrackFieldEntryServiceImpl implements TrackFieldEntryService {

    private final Logger log = LoggerFactory.getLogger(TrackFieldEntryServiceImpl.class);

    private final TrackFieldEntryRepository trackFieldEntryRepository;

    public TrackFieldEntryServiceImpl(TrackFieldEntryRepository trackFieldEntryRepository) {
        this.trackFieldEntryRepository = trackFieldEntryRepository;
    }

    @Override
    public TrackFieldEntry save(TrackFieldEntry trackFieldEntry) {
        log.debug("Request to save TrackFieldEntry : {}", trackFieldEntry);
        return trackFieldEntryRepository.save(trackFieldEntry);
    }

    @Override
    public TrackFieldEntry update(TrackFieldEntry trackFieldEntry) {
        log.debug("Request to update TrackFieldEntry : {}", trackFieldEntry);
        return trackFieldEntryRepository.save(trackFieldEntry);
    }

    @Override
    public Optional<TrackFieldEntry> partialUpdate(TrackFieldEntry trackFieldEntry) {
        log.debug("Request to partially update TrackFieldEntry : {}", trackFieldEntry);

        return trackFieldEntryRepository
            .findById(trackFieldEntry.getId())
            .map(existingTrackFieldEntry -> {
                if (trackFieldEntry.getDescription() != null) {
                    existingTrackFieldEntry.setDescription(trackFieldEntry.getDescription());
                }
                if (trackFieldEntry.getCategory() != null) {
                    existingTrackFieldEntry.setCategory(trackFieldEntry.getCategory());
                }

                return existingTrackFieldEntry;
            })
            .map(trackFieldEntryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrackFieldEntry> findAll(Pageable pageable) {
        log.debug("Request to get all TrackFieldEntries");
        return trackFieldEntryRepository.findAll(pageable);
    }

    public Page<TrackFieldEntry> findAllWithEagerRelationships(Pageable pageable) {
        return trackFieldEntryRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrackFieldEntry> findOne(Long id) {
        log.debug("Request to get TrackFieldEntry : {}", id);
        return trackFieldEntryRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrackFieldEntry : {}", id);
        trackFieldEntryRepository.deleteById(id);
    }
}
