package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ProviderProductPrice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProviderProductPrice entity.
 */
@Repository
public interface ProviderProductPriceRepository extends JpaRepository<ProviderProductPrice, Long> {
    default Optional<ProviderProductPrice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProviderProductPrice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProviderProductPrice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct providerProductPrice from ProviderProductPrice providerProductPrice left join fetch providerProductPrice.providerProduct",
        countQuery = "select count(distinct providerProductPrice) from ProviderProductPrice providerProductPrice"
    )
    Page<ProviderProductPrice> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct providerProductPrice from ProviderProductPrice providerProductPrice left join fetch providerProductPrice.providerProduct"
    )
    List<ProviderProductPrice> findAllWithToOneRelationships();

    @Query(
        "select providerProductPrice from ProviderProductPrice providerProductPrice left join fetch providerProductPrice.providerProduct where providerProductPrice.id =:id"
    )
    Optional<ProviderProductPrice> findOneWithToOneRelationships(@Param("id") Long id);
}
