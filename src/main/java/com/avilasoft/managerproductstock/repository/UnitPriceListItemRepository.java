package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.UnitPriceListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UnitPriceListItem entity.
 */
@Repository
public interface UnitPriceListItemRepository extends JpaRepository<UnitPriceListItem, Long> {
    default Optional<UnitPriceListItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UnitPriceListItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UnitPriceListItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct unitPriceListItem from UnitPriceListItem unitPriceListItem left join fetch unitPriceListItem.unitPriceList",
        countQuery = "select count(distinct unitPriceListItem) from UnitPriceListItem unitPriceListItem"
    )
    Page<UnitPriceListItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct unitPriceListItem from UnitPriceListItem unitPriceListItem left join fetch unitPriceListItem.unitPriceList")
    List<UnitPriceListItem> findAllWithToOneRelationships();

    @Query(
        "select unitPriceListItem from UnitPriceListItem unitPriceListItem left join fetch unitPriceListItem.unitPriceList where unitPriceListItem.id =:id"
    )
    Optional<UnitPriceListItem> findOneWithToOneRelationships(@Param("id") Long id);
}
