package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.TrackFieldEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrackFieldEntry entity.
 *
 * When extending this class, extend TrackFieldEntryRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TrackFieldEntryRepository extends TrackFieldEntryRepositoryWithBagRelationships, JpaRepository<TrackFieldEntry, Long> {
    default Optional<TrackFieldEntry> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<TrackFieldEntry> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<TrackFieldEntry> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct trackFieldEntry from TrackFieldEntry trackFieldEntry left join fetch trackFieldEntry.event",
        countQuery = "select count(distinct trackFieldEntry) from TrackFieldEntry trackFieldEntry"
    )
    Page<TrackFieldEntry> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct trackFieldEntry from TrackFieldEntry trackFieldEntry left join fetch trackFieldEntry.event")
    List<TrackFieldEntry> findAllWithToOneRelationships();

    @Query(
        "select trackFieldEntry from TrackFieldEntry trackFieldEntry left join fetch trackFieldEntry.event where trackFieldEntry.id =:id"
    )
    Optional<TrackFieldEntry> findOneWithToOneRelationships(@Param("id") Long id);
}
