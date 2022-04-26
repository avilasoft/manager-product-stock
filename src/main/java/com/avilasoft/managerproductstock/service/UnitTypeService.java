package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.UnitType;
import com.avilasoft.managerproductstock.repository.UnitTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UnitType}.
 */
@Service
@Transactional
public class UnitTypeService {

    private final Logger log = LoggerFactory.getLogger(UnitTypeService.class);

    private final UnitTypeRepository unitTypeRepository;

    public UnitTypeService(UnitTypeRepository unitTypeRepository) {
        this.unitTypeRepository = unitTypeRepository;
    }

    /**
     * Save a unitType.
     *
     * @param unitType the entity to save.
     * @return the persisted entity.
     */
    public UnitType save(UnitType unitType) {
        log.debug("Request to save UnitType : {}", unitType);
        return unitTypeRepository.save(unitType);
    }

    /**
     * Partially update a unitType.
     *
     * @param unitType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitType> partialUpdate(UnitType unitType) {
        log.debug("Request to partially update UnitType : {}", unitType);

        return unitTypeRepository
            .findById(unitType.getId())
            .map(existingUnitType -> {
                if (unitType.getName() != null) {
                    existingUnitType.setName(unitType.getName());
                }
                if (unitType.getDescription() != null) {
                    existingUnitType.setDescription(unitType.getDescription());
                }

                return existingUnitType;
            })
            .map(unitTypeRepository::save);
    }

    /**
     * Get all the unitTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitType> findAll(Pageable pageable) {
        log.debug("Request to get all UnitTypes");
        return unitTypeRepository.findAll(pageable);
    }

    /**
     * Get one unitType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UnitType> findOne(Long id) {
        log.debug("Request to get UnitType : {}", id);
        return unitTypeRepository.findById(id);
    }

    /**
     * Delete the unitType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UnitType : {}", id);
        unitTypeRepository.deleteById(id);
    }
}
