package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.Unit;
import com.avilasoft.managerproductstock.repository.UnitRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Unit}.
 */
@Service
@Transactional
public class UnitService {

    private final Logger log = LoggerFactory.getLogger(UnitService.class);

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * Save a unit.
     *
     * @param unit the entity to save.
     * @return the persisted entity.
     */
    public Unit save(Unit unit) {
        log.debug("Request to save Unit : {}", unit);
        return unitRepository.save(unit);
    }

    /**
     * Partially update a unit.
     *
     * @param unit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Unit> partialUpdate(Unit unit) {
        log.debug("Request to partially update Unit : {}", unit);

        return unitRepository
            .findById(unit.getId())
            .map(existingUnit -> {
                if (unit.getName() != null) {
                    existingUnit.setName(unit.getName());
                }
                if (unit.getDescription() != null) {
                    existingUnit.setDescription(unit.getDescription());
                }
                if (unit.getSymbol() != null) {
                    existingUnit.setSymbol(unit.getSymbol());
                }
                if (unit.getIsBase() != null) {
                    existingUnit.setIsBase(unit.getIsBase());
                }

                return existingUnit;
            })
            .map(unitRepository::save);
    }

    /**
     * Get all the units.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Unit> findAll(Pageable pageable) {
        log.debug("Request to get all Units");
        return unitRepository.findAll(pageable);
    }

    /**
     * Get all the units with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Unit> findAllWithEagerRelationships(Pageable pageable) {
        return unitRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one unit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Unit> findOne(Long id) {
        log.debug("Request to get Unit : {}", id);
        return unitRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the unit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Unit : {}", id);
        unitRepository.deleteById(id);
    }
}
