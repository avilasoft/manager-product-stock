package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ProjectItemListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProjectItemListItem entity.
 */
@Repository
public interface ProjectItemListItemRepository extends JpaRepository<ProjectItemListItem, Long> {
    default Optional<ProjectItemListItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProjectItemListItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProjectItemListItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct projectItemListItem from ProjectItemListItem projectItemListItem left join fetch projectItemListItem.unitPriceListItem left join fetch projectItemListItem.projectItemList left join fetch projectItemListItem.item left join fetch projectItemListItem.providerItem left join fetch projectItemListItem.unit",
        countQuery = "select count(distinct projectItemListItem) from ProjectItemListItem projectItemListItem"
    )
    Page<ProjectItemListItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct projectItemListItem from ProjectItemListItem projectItemListItem left join fetch projectItemListItem.unitPriceListItem left join fetch projectItemListItem.projectItemList left join fetch projectItemListItem.item left join fetch projectItemListItem.providerItem left join fetch projectItemListItem.unit"
    )
    List<ProjectItemListItem> findAllWithToOneRelationships();

    @Query(
        "select projectItemListItem from ProjectItemListItem projectItemListItem left join fetch projectItemListItem.unitPriceListItem left join fetch projectItemListItem.projectItemList left join fetch projectItemListItem.item left join fetch projectItemListItem.providerItem left join fetch projectItemListItem.unit where projectItemListItem.id =:id"
    )
    Optional<ProjectItemListItem> findOneWithToOneRelationships(@Param("id") Long id);
}
