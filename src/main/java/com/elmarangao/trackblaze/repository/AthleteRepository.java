package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.Athlete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Athlete entity.
 *
 * When extending this class, extend AthleteRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AthleteRepository extends AthleteRepositoryWithBagRelationships, JpaRepository<Athlete, Long> {
    default Optional<Athlete> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Athlete> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Athlete> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct athlete from Athlete athlete left join fetch athlete.school",
        countQuery = "select count(distinct athlete) from Athlete athlete"
    )
    Page<Athlete> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct athlete from Athlete athlete left join fetch athlete.school")
    List<Athlete> findAllWithToOneRelationships();

    @Query("select athlete from Athlete athlete left join fetch athlete.school where athlete.id =:id")
    Optional<Athlete> findOneWithToOneRelationships(@Param("id") Long id);
}
