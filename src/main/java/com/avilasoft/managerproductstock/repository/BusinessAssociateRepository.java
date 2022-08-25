package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusinessAssociate entity.
 */
@Repository
public interface BusinessAssociateRepository extends JpaRepository<BusinessAssociate, Long> {
    @Query("select businessAssociate from BusinessAssociate businessAssociate where businessAssociate.user.login = ?#{principal.username}")
    List<BusinessAssociate> findByUserIsCurrentUser();

    default Optional<BusinessAssociate> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BusinessAssociate> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BusinessAssociate> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct businessAssociate from BusinessAssociate businessAssociate left join fetch businessAssociate.user",
        countQuery = "select count(distinct businessAssociate) from BusinessAssociate businessAssociate"
    )
    Page<BusinessAssociate> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct businessAssociate from BusinessAssociate businessAssociate left join fetch businessAssociate.user")
    List<BusinessAssociate> findAllWithToOneRelationships();

    @Query(
        "select businessAssociate from BusinessAssociate businessAssociate left join fetch businessAssociate.user where businessAssociate.id =:id"
    )
    Optional<BusinessAssociate> findOneWithToOneRelationships(@Param("id") Long id);
}
