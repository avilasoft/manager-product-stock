package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ProjectGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProjectGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {}
