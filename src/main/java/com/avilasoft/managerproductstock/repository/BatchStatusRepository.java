package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.BatchStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BatchStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatchStatusRepository extends JpaRepository<BatchStatus, Long> {}
