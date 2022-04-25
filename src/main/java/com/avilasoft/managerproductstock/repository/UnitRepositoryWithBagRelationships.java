package com.avilasoft.managerproductstock.repository;

import com.avilasoft.managerproductstock.domain.Unit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UnitRepositoryWithBagRelationships {
    Optional<Unit> fetchBagRelationships(Optional<Unit> unit);

    List<Unit> fetchBagRelationships(List<Unit> units);

    Page<Unit> fetchBagRelationships(Page<Unit> units);
}
