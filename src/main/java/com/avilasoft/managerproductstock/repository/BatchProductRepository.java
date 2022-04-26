package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.BatchProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BatchProduct entity.
 */
@Repository
public interface BatchProductRepository extends JpaRepository<BatchProduct, Long> {
    default Optional<BatchProduct> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BatchProduct> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BatchProduct> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct batchProduct from BatchProduct batchProduct left join fetch batchProduct.product",
        countQuery = "select count(distinct batchProduct) from BatchProduct batchProduct"
    )
    Page<BatchProduct> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct batchProduct from BatchProduct batchProduct left join fetch batchProduct.product")
    List<BatchProduct> findAllWithToOneRelationships();

    @Query("select batchProduct from BatchProduct batchProduct left join fetch batchProduct.product where batchProduct.id =:id")
    Optional<BatchProduct> findOneWithToOneRelationships(@Param("id") Long id);
}
