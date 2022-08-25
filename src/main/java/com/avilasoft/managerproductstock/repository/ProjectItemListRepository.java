package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ProjectItemList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProjectItemList entity.
 */
@Repository
public interface ProjectItemListRepository extends JpaRepository<ProjectItemList, Long> {
    default Optional<ProjectItemList> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProjectItemList> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProjectItemList> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct projectItemList from ProjectItemList projectItemList left join fetch projectItemList.unitPriceList left join fetch projectItemList.computedPriceList left join fetch projectItemList.project",
        countQuery = "select count(distinct projectItemList) from ProjectItemList projectItemList"
    )
    Page<ProjectItemList> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct projectItemList from ProjectItemList projectItemList left join fetch projectItemList.unitPriceList left join fetch projectItemList.computedPriceList left join fetch projectItemList.project"
    )
    List<ProjectItemList> findAllWithToOneRelationships();

    @Query(
        "select projectItemList from ProjectItemList projectItemList left join fetch projectItemList.unitPriceList left join fetch projectItemList.computedPriceList left join fetch projectItemList.project where projectItemList.id =:id"
    )
    Optional<ProjectItemList> findOneWithToOneRelationships(@Param("id") Long id);
}
