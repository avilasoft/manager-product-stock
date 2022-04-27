package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Price;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Price entity.
 */
@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    default Optional<Price> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Price> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Price> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct price from Price price left join fetch price.unit",
        countQuery = "select count(distinct price) from Price price"
    )
    Page<Price> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct price from Price price left join fetch price.unit")
    List<Price> findAllWithToOneRelationships();

    @Query("select price from Price price left join fetch price.unit where price.id =:id")
    Optional<Price> findOneWithToOneRelationships(@Param("id") Long id);
}
