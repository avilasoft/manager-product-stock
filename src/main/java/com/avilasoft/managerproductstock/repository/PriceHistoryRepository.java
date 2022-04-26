package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.PriceHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PriceHistory entity.
 */
@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    default Optional<PriceHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PriceHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PriceHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct priceHistory from PriceHistory priceHistory left join fetch priceHistory.parent left join fetch priceHistory.price",
        countQuery = "select count(distinct priceHistory) from PriceHistory priceHistory"
    )
    Page<PriceHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct priceHistory from PriceHistory priceHistory left join fetch priceHistory.parent left join fetch priceHistory.price"
    )
    List<PriceHistory> findAllWithToOneRelationships();

    @Query(
        "select priceHistory from PriceHistory priceHistory left join fetch priceHistory.parent left join fetch priceHistory.price where priceHistory.id =:id"
    )
    Optional<PriceHistory> findOneWithToOneRelationships(@Param("id") Long id);
}
