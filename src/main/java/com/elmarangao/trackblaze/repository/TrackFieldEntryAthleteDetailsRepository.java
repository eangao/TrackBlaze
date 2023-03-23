package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.TrackFieldEntryAthleteDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrackFieldEntryAthleteDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackFieldEntryAthleteDetailsRepository extends JpaRepository<TrackFieldEntryAthleteDetails, Long> {}
