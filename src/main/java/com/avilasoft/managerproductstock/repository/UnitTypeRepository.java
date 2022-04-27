package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.UnitType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UnitType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitTypeRepository extends JpaRepository<UnitType, Long> {}
