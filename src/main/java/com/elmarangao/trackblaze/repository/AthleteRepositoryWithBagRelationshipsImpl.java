package com.elmarangao.trackblaze.repository;

import com.elmarangao.trackblaze.domain.Athlete;
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
public class AthleteRepositoryWithBagRelationshipsImpl implements AthleteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Athlete> fetchBagRelationships(Optional<Athlete> athlete) {
        return athlete.map(this::fetchEvents);
    }

    @Override
    public Page<Athlete> fetchBagRelationships(Page<Athlete> athletes) {
        return new PageImpl<>(fetchBagRelationships(athletes.getContent()), athletes.getPageable(), athletes.getTotalElements());
    }

    @Override
    public List<Athlete> fetchBagRelationships(List<Athlete> athletes) {
        return Optional.of(athletes).map(this::fetchEvents).orElse(Collections.emptyList());
    }

    Athlete fetchEvents(Athlete result) {
        return entityManager
            .createQuery("select athlete from Athlete athlete left join fetch athlete.events where athlete is :athlete", Athlete.class)
            .setParameter("athlete", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Athlete> fetchEvents(List<Athlete> athletes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, athletes.size()).forEach(index -> order.put(athletes.get(index).getId(), index));
        List<Athlete> result = entityManager
            .createQuery(
                "select distinct athlete from Athlete athlete left join fetch athlete.events where athlete in :athletes",
                Athlete.class
            )
            .setParameter("athletes", athletes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
