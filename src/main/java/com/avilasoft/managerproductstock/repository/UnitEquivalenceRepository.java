package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.UnitEquivalence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UnitEquivalence entity.
 */
@Repository
public interface UnitEquivalenceRepository extends JpaRepository<UnitEquivalence, Long> {
    default Optional<UnitEquivalence> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UnitEquivalence> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UnitEquivalence> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct unitEquivalence from UnitEquivalence unitEquivalence left join fetch unitEquivalence.unit left join fetch unitEquivalence.child",
        countQuery = "select count(distinct unitEquivalence) from UnitEquivalence unitEquivalence"
    )
    Page<UnitEquivalence> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct unitEquivalence from UnitEquivalence unitEquivalence left join fetch unitEquivalence.unit left join fetch unitEquivalence.child"
    )
    List<UnitEquivalence> findAllWithToOneRelationships();

    @Query(
        "select unitEquivalence from UnitEquivalence unitEquivalence left join fetch unitEquivalence.unit left join fetch unitEquivalence.child where unitEquivalence.id =:id"
    )
    Optional<UnitEquivalence> findOneWithToOneRelationships(@Param("id") Long id);
}
