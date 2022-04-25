package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Batch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Batch entity.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    default Optional<Batch> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Batch> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Batch> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct batch from Batch batch left join fetch batch.bachStatus left join fetch batch.businessAssociate",
        countQuery = "select count(distinct batch) from Batch batch"
    )
    Page<Batch> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct batch from Batch batch left join fetch batch.bachStatus left join fetch batch.businessAssociate")
    List<Batch> findAllWithToOneRelationships();

    @Query("select batch from Batch batch left join fetch batch.bachStatus left join fetch batch.businessAssociate where batch.id =:id")
    Optional<Batch> findOneWithToOneRelationships(@Param("id") Long id);
}
