package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.ComputedPriceList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComputedPriceList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComputedPriceListRepository extends JpaRepository<ComputedPriceList, Long> {}
