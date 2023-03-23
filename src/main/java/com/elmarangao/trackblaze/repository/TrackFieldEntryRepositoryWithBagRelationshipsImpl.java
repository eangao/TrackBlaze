package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.TrackFieldEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TrackFieldEntryRepositoryWithBagRelationshipsImpl implements TrackFieldEntryRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrackFieldEntry> fetchBagRelationships(Optional<TrackFieldEntry> trackFieldEntry) {
        return trackFieldEntry.map(this::fetchDetails);
    }

    @Override
    public Page<TrackFieldEntry> fetchBagRelationships(Page<TrackFieldEntry> trackFieldEntries) {
        return new PageImpl<>(
            fetchBagRelationships(trackFieldEntries.getContent()),
            trackFieldEntries.getPageable(),
            trackFieldEntries.getTotalElements()
        );
    }

    @Override
    public List<TrackFieldEntry> fetchBagRelationships(List<TrackFieldEntry> trackFieldEntries) {
        return Optional.of(trackFieldEntries).map(this::fetchDetails).orElse(Collections.emptyList());
    }

    TrackFieldEntry fetchDetails(TrackFieldEntry result) {
        return entityManager
            .createQuery(
                "select trackFieldEntry from TrackFieldEntry trackFieldEntry left join fetch trackFieldEntry.details where trackFieldEntry is :trackFieldEntry",
                TrackFieldEntry.class
            )
            .setParameter("trackFieldEntry", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TrackFieldEntry> fetchDetails(List<TrackFieldEntry> trackFieldEntries) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, trackFieldEntries.size()).forEach(index -> order.put(trackFieldEntries.get(index).getId(), index));
        List<TrackFieldEntry> result = entityManager
            .createQuery(
                "select distinct trackFieldEntry from TrackFieldEntry trackFieldEntry left join fetch trackFieldEntry.details where trackFieldEntry in :trackFieldEntries",
                TrackFieldEntry.class
            )
            .setParameter("trackFieldEntries", trackFieldEntries)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
