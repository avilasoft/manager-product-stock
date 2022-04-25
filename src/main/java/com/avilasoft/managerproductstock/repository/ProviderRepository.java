package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Provider;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Provider entity.
 */
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    default Optional<Provider> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Provider> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Provider> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct provider from Provider provider left join fetch provider.user",
        countQuery = "select count(distinct provider) from Provider provider"
    )
    Page<Provider> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct provider from Provider provider left join fetch provider.user")
    List<Provider> findAllWithToOneRelationships();

    @Query("select provider from Provider provider left join fetch provider.user where provider.id =:id")
    Optional<Provider> findOneWithToOneRelationships(@Param("id") Long id);
}
