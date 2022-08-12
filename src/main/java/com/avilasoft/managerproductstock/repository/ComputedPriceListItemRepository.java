package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ComputedPriceListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComputedPriceListItem entity.
 */
@Repository
public interface ComputedPriceListItemRepository extends JpaRepository<ComputedPriceListItem, Long> {
    default Optional<ComputedPriceListItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ComputedPriceListItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ComputedPriceListItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct computedPriceListItem from ComputedPriceListItem computedPriceListItem left join fetch computedPriceListItem.unitPriceList left join fetch computedPriceListItem.computedPriceList",
        countQuery = "select count(distinct computedPriceListItem) from ComputedPriceListItem computedPriceListItem"
    )
    Page<ComputedPriceListItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct computedPriceListItem from ComputedPriceListItem computedPriceListItem left join fetch computedPriceListItem.unitPriceList left join fetch computedPriceListItem.computedPriceList"
    )
    List<ComputedPriceListItem> findAllWithToOneRelationships();

    @Query(
        "select computedPriceListItem from ComputedPriceListItem computedPriceListItem left join fetch computedPriceListItem.unitPriceList left join fetch computedPriceListItem.computedPriceList where computedPriceListItem.id =:id"
    )
    Optional<ComputedPriceListItem> findOneWithToOneRelationships(@Param("id") Long id);
}
