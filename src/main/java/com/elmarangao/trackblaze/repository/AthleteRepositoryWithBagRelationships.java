package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.Athlete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AthleteRepositoryWithBagRelationships {
    Optional<Athlete> fetchBagRelationships(Optional<Athlete> athlete);

    List<Athlete> fetchBagRelationships(List<Athlete> athletes);

    Page<Athlete> fetchBagRelationships(Page<Athlete> athletes);
}
