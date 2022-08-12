package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.UnitPriceList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UnitPriceList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitPriceListRepository extends JpaRepository<UnitPriceList, Long> {}
