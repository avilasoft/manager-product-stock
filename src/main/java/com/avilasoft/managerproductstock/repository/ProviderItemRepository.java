package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ProviderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProviderItem entity.
 */
@Repository
public interface ProviderItemRepository extends JpaRepository<ProviderItem, Long> {
    default Optional<ProviderItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProviderItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProviderItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct providerItem from ProviderItem providerItem left join fetch providerItem.provider left join fetch providerItem.unit left join fetch providerItem.item",
        countQuery = "select count(distinct providerItem) from ProviderItem providerItem"
    )
    Page<ProviderItem> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct providerItem from ProviderItem providerItem left join fetch providerItem.provider left join fetch providerItem.unit left join fetch providerItem.item"
    )
    List<ProviderItem> findAllWithToOneRelationships();

    @Query(
        "select providerItem from ProviderItem providerItem left join fetch providerItem.provider left join fetch providerItem.unit left join fetch providerItem.item where providerItem.id =:id"
    )
    Optional<ProviderItem> findOneWithToOneRelationships(@Param("id") Long id);
}
