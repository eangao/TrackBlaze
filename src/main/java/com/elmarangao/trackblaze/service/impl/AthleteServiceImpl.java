package com.elmarangao.trackblaze.service.impl;

import com.elmarangao.trackblaze.domain.Athlete;
import com.elmarangao.trackblaze.repository.AthleteRepository;
import com.elmarangao.trackblaze.service.AthleteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Athlete}.
 */
@Service
@Transactional
public class AthleteServiceImpl implements AthleteService {

    private final Logger log = LoggerFactory.getLogger(AthleteServiceImpl.class);

    private final AthleteRepository athleteRepository;

    public AthleteServiceImpl(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    @Override
    public Athlete save(Athlete athlete) {
        log.debug("Request to save Athlete : {}", athlete);
        return athleteRepository.save(athlete);
    }

    @Override
    public Athlete update(Athlete athlete) {
        log.debug("Request to update Athlete : {}", athlete);
        return athleteRepository.save(athlete);
    }

    @Override
    public Optional<Athlete> partialUpdate(Athlete athlete) {
        log.debug("Request to partially update Athlete : {}", athlete);

        return athleteRepository
            .findById(athlete.getId())
            .map(existingAthlete -> {
                if (athlete.getFirstName() != null) {
                    existingAthlete.setFirstName(athlete.getFirstName());
                }
                if (athlete.getMiddleName() != null) {
                    existingAthlete.setMiddleName(athlete.getMiddleName());
                }
                if (athlete.getLastName() != null) {
                    existingAthlete.setLastName(athlete.getLastName());
                }
                if (athlete.getBirthDate() != null) {
                    existingAthlete.setBirthDate(athlete.getBirthDate());
                }
                if (athlete.getGender() != null) {
                    existingAthlete.setGender(athlete.getGender());
                }
                if (athlete.getPicture() != null) {
                    existingAthlete.setPicture(athlete.getPicture());
                }
                if (athlete.getPictureContentType() != null) {
                    existingAthlete.setPictureContentType(athlete.getPictureContentType());
                }
                if (athlete.getUserId() != null) {
                    existingAthlete.setUserId(athlete.getUserId());
                }

                return existingAthlete;
            })
            .map(athleteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Athlete> findAll(Pageable pageable) {
        log.debug("Request to get all Athletes");
        return athleteRepository.findAll(pageable);
    }

    public Page<Athlete> findAllWithEagerRelationships(Pageable pageable) {
        return athleteRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Athlete> findOne(Long id) {
        log.debug("Request to get Athlete : {}", id);
        return athleteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Athlete : {}", id);
        athleteRepository.deleteById(id);
    }
}
