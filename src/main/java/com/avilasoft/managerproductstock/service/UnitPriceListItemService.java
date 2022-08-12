package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.UnitPriceListItem;
import com.avilasoft.managerproductstock.repository.UnitPriceListItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UnitPriceListItem}.
 */
@Service
@Transactional
public class UnitPriceListItemService {

    private final Logger log = LoggerFactory.getLogger(UnitPriceListItemService.class);

    private final UnitPriceListItemRepository unitPriceListItemRepository;

    public UnitPriceListItemService(UnitPriceListItemRepository unitPriceListItemRepository) {
        this.unitPriceListItemRepository = unitPriceListItemRepository;
    }

    /**
     * Save a unitPriceListItem.
     *
     * @param unitPriceListItem the entity to save.
     * @return the persisted entity.
     */
    public UnitPriceListItem save(UnitPriceListItem unitPriceListItem) {
        log.debug("Request to save UnitPriceListItem : {}", unitPriceListItem);
        return unitPriceListItemRepository.save(unitPriceListItem);
    }

    /**
     * Partially update a unitPriceListItem.
     *
     * @param unitPriceListItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitPriceListItem> partialUpdate(UnitPriceListItem unitPriceListItem) {
        log.debug("Request to partially update UnitPriceListItem : {}", unitPriceListItem);

        return unitPriceListItemRepository
            .findById(unitPriceListItem.getId())
            .map(existingUnitPriceListItem -> {
                if (unitPriceListItem.getCode() != null) {
                    existingUnitPriceListItem.setCode(unitPriceListItem.getCode());
                }
                if (unitPriceListItem.getName() != null) {
                    existingUnitPriceListItem.setName(unitPriceListItem.getName());
                }
                if (unitPriceListItem.getUnitCostTotal() != null) {
                    existingUnitPriceListItem.setUnitCostTotal(unitPriceListItem.getUnitCostTotal());
                }

                return existingUnitPriceListItem;
            })
            .map(unitPriceListItemRepository::save);
    }

    /**
     * Get all the unitPriceListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitPriceListItem> findAll(Pageable pageable) {
        log.debug("Request to get all UnitPriceListItems");
        return unitPriceListItemRepository.findAll(pageable);
    }

    /**
     * Get all the unitPriceListItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UnitPriceListItem> findAllWithEagerRelationships(Pageable pageable) {
        return unitPriceListItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one unitPriceListItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UnitPriceListItem> findOne(Long id) {
        log.debug("Request to get UnitPriceListItem : {}", id);
        return unitPriceListItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the unitPriceListItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UnitPriceListItem : {}", id);
        unitPriceListItemRepository.deleteById(id);
    }
}
