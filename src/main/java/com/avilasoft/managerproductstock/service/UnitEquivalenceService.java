package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.UnitEquivalence;
import com.avilasoft.managerproductstock.repository.UnitEquivalenceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UnitEquivalence}.
 */
@Service
@Transactional
public class UnitEquivalenceService {

    private final Logger log = LoggerFactory.getLogger(UnitEquivalenceService.class);

    private final UnitEquivalenceRepository unitEquivalenceRepository;

    public UnitEquivalenceService(UnitEquivalenceRepository unitEquivalenceRepository) {
        this.unitEquivalenceRepository = unitEquivalenceRepository;
    }

    /**
     * Save a unitEquivalence.
     *
     * @param unitEquivalence the entity to save.
     * @return the persisted entity.
     */
    public UnitEquivalence save(UnitEquivalence unitEquivalence) {
        log.debug("Request to save UnitEquivalence : {}", unitEquivalence);
        return unitEquivalenceRepository.save(unitEquivalence);
    }

    /**
     * Partially update a unitEquivalence.
     *
     * @param unitEquivalence the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitEquivalence> partialUpdate(UnitEquivalence unitEquivalence) {
        log.debug("Request to partially update UnitEquivalence : {}", unitEquivalence);

        return unitEquivalenceRepository
            .findById(unitEquivalence.getId())
            .map(existingUnitEquivalence -> {
                if (unitEquivalence.getName() != null) {
                    existingUnitEquivalence.setName(unitEquivalence.getName());
                }
                if (unitEquivalence.getDescription() != null) {
                    existingUnitEquivalence.setDescription(unitEquivalence.getDescription());
                }

                return existingUnitEquivalence;
            })
            .map(unitEquivalenceRepository::save);
    }

    /**
     * Get all the unitEquivalences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitEquivalence> findAll(Pageable pageable) {
        log.debug("Request to get all UnitEquivalences");
        return unitEquivalenceRepository.findAll(pageable);
    }

    /**
     * Get all the unitEquivalences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UnitEquivalence> findAllWithEagerRelationships(Pageable pageable) {
        return unitEquivalenceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one unitEquivalence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UnitEquivalence> findOne(Long id) {
        log.debug("Request to get UnitEquivalence : {}", id);
        return unitEquivalenceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the unitEquivalence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UnitEquivalence : {}", id);
        unitEquivalenceRepository.deleteById(id);
    }
}
