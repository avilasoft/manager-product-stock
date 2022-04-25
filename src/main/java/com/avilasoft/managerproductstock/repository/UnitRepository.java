package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Unit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Unit entity.
 */
@Repository
public interface UnitRepository extends UnitRepositoryWithBagRelationships, JpaRepository<Unit, Long> {
    default Optional<Unit> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Unit> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Unit> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct unit from Unit unit left join fetch unit.unitType left join fetch unit.providerProductPrice left join fetch unit.product",
        countQuery = "select count(distinct unit) from Unit unit"
    )
    Page<Unit> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct unit from Unit unit left join fetch unit.unitType left join fetch unit.providerProductPrice left join fetch unit.product"
    )
    List<Unit> findAllWithToOneRelationships();

    @Query(
        "select unit from Unit unit left join fetch unit.unitType left join fetch unit.providerProductPrice left join fetch unit.product where unit.id =:id"
    )
    Optional<Unit> findOneWithToOneRelationships(@Param("id") Long id);
}
