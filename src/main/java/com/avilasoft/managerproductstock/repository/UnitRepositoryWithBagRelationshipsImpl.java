package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Unit;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UnitRepositoryWithBagRelationshipsImpl implements UnitRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Unit> fetchBagRelationships(Optional<Unit> unit) {
        return unit.map(this::fetchParents);
    }

    @Override
    public Page<Unit> fetchBagRelationships(Page<Unit> units) {
        return new PageImpl<>(fetchBagRelationships(units.getContent()), units.getPageable(), units.getTotalElements());
    }

    @Override
    public List<Unit> fetchBagRelationships(List<Unit> units) {
        return Optional.of(units).map(this::fetchParents).get();
    }

    Unit fetchParents(Unit result) {
        return entityManager
            .createQuery("select unit from Unit unit left join fetch unit.parents where unit is :unit", Unit.class)
            .setParameter("unit", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Unit> fetchParents(List<Unit> units) {
        return entityManager
            .createQuery("select distinct unit from Unit unit left join fetch unit.parents where unit in :units", Unit.class)
            .setParameter("units", units)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
