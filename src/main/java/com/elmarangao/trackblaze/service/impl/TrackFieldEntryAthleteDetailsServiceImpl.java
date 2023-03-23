package com.elmarangao.trackblaze.service.impl;

import com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails;
import com.elmarangao.trackblaze.repository.TrackFieldEntryAthleteDetailsRepository;
import com.elmarangao.trackblaze.service.TrackFieldEntryAthleteDetailsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TrackFieldEntryAthleteDetails}.
 */
@Service
@Transactional
public class TrackFieldEntryAthleteDetailsServiceImpl implements TrackFieldEntryAthleteDetailsService {

    private final Logger log = LoggerFactory.getLogger(TrackFieldEntryAthleteDetailsServiceImpl.class);

    private final TrackFieldEntryAthleteDetailsRepository trackFieldEntryAthleteDetailsRepository;

    public TrackFieldEntryAthleteDetailsServiceImpl(TrackFieldEntryAthleteDetailsRepository trackFieldEntryAthleteDetailsRepository) {
        this.trackFieldEntryAthleteDetailsRepository = trackFieldEntryAthleteDetailsRepository;
    }

    @Override
    public TrackFieldEntryAthleteDetails save(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        log.debug("Request to save TrackFieldEntryAthleteDetails : {}", trackFieldEntryAthleteDetails);
        return trackFieldEntryAthleteDetailsRepository.save(trackFieldEntryAthleteDetails);
    }

    @Override
    public TrackFieldEntryAthleteDetails update(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        log.debug("Request to update TrackFieldEntryAthleteDetails : {}", trackFieldEntryAthleteDetails);
        return trackFieldEntryAthleteDetailsRepository.save(trackFieldEntryAthleteDetails);
    }

    @Override
    public Optional<TrackFieldEntryAthleteDetails> partialUpdate(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        log.debug("Request to partially update TrackFieldEntryAthleteDetails : {}", trackFieldEntryAthleteDetails);

        return trackFieldEntryAthleteDetailsRepository
            .findById(trackFieldEntryAthleteDetails.getId())
            .map(existingTrackFieldEntryAthleteDetails -> {
                if (trackFieldEntryAthleteDetails.getRank() != null) {
                    existingTrackFieldEntryAthleteDetails.setRank(trackFieldEntryAthleteDetails.getRank());
                }
                if (trackFieldEntryAthleteDetails.getTimeHeightDistance() != null) {
                    existingTrackFieldEntryAthleteDetails.setTimeHeightDistance(trackFieldEntryAthleteDetails.getTimeHeightDistance());
                }
                if (trackFieldEntryAthleteDetails.getRemarks() != null) {
                    existingTrackFieldEntryAthleteDetails.setRemarks(trackFieldEntryAthleteDetails.getRemarks());
                }

                return existingTrackFieldEntryAthleteDetails;
            })
            .map(trackFieldEntryAthleteDetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrackFieldEntryAthleteDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TrackFieldEntryAthleteDetails");
        return trackFieldEntryAthleteDetailsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrackFieldEntryAthleteDetails> findOne(Long id) {
        log.debug("Request to get TrackFieldEntryAthleteDetails : {}", id);
        return trackFieldEntryAthleteDetailsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrackFieldEntryAthleteDetails : {}", id);
        trackFieldEntryAthleteDetailsRepository.deleteById(id);
    }
}
