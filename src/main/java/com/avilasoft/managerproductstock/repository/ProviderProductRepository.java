package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ProviderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProviderProduct entity.
 */
@Repository
public interface ProviderProductRepository extends JpaRepository<ProviderProduct, Long> {
    default Optional<ProviderProduct> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProviderProduct> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProviderProduct> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct providerProduct from ProviderProduct providerProduct left join fetch providerProduct.provider left join fetch providerProduct.providerProductPrice left join fetch providerProduct.product",
        countQuery = "select count(distinct providerProduct) from ProviderProduct providerProduct"
    )
    Page<ProviderProduct> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct providerProduct from ProviderProduct providerProduct left join fetch providerProduct.provider left join fetch providerProduct.providerProductPrice left join fetch providerProduct.product"
    )
    List<ProviderProduct> findAllWithToOneRelationships();

    @Query(
        "select providerProduct from ProviderProduct providerProduct left join fetch providerProduct.provider left join fetch providerProduct.providerProductPrice left join fetch providerProduct.product where providerProduct.id =:id"
    )
    Optional<ProviderProduct> findOneWithToOneRelationships(@Param("id") Long id);
}
