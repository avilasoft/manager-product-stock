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
public interface UnitRepository extends JpaRepository<Unit, Long> {
    default Optional<Unit> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Unit> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Unit> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct unit from Unit unit left join fetch unit.unitType",
        countQuery = "select count(distinct unit) from Unit unit"
    )
    Page<Unit> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct unit from Unit unit left join fetch unit.unitType")
    List<Unit> findAllWithToOneRelationships();

    @Query("select unit from Unit unit left join fetch unit.unitType where unit.id =:id")
    Optional<Unit> findOneWithToOneRelationships(@Param("id") Long id);
}
