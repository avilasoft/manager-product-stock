package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Item;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {}
