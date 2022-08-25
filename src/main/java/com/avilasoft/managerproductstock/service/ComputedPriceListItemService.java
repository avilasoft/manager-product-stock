package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ComputedPriceListItem;
import com.avilasoft.managerproductstock.repository.ComputedPriceListItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ComputedPriceListItem}.
 */
@Service
@Transactional
public class ComputedPriceListItemService {

    private final Logger log = LoggerFactory.getLogger(ComputedPriceListItemService.class);

    private final ComputedPriceListItemRepository computedPriceListItemRepository;

    public ComputedPriceListItemService(ComputedPriceListItemRepository computedPriceListItemRepository) {
        this.computedPriceListItemRepository = computedPriceListItemRepository;
    }

    /**
     * Save a computedPriceListItem.
     *
     * @param computedPriceListItem the entity to save.
     * @return the persisted entity.
     */
    public ComputedPriceListItem save(ComputedPriceListItem computedPriceListItem) {
        log.debug("Request to save ComputedPriceListItem : {}", computedPriceListItem);
        return computedPriceListItemRepository.save(computedPriceListItem);
    }

    /**
     * Partially update a computedPriceListItem.
     *
     * @param computedPriceListItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComputedPriceListItem> partialUpdate(ComputedPriceListItem computedPriceListItem) {
        log.debug("Request to partially update ComputedPriceListItem : {}", computedPriceListItem);

        return computedPriceListItemRepository
            .findById(computedPriceListItem.getId())
            .map(existingComputedPriceListItem -> {
                if (computedPriceListItem.getCode() != null) {
                    existingComputedPriceListItem.setCode(computedPriceListItem.getCode());
                }
                if (computedPriceListItem.getComputedPriceTotal() != null) {
                    existingComputedPriceListItem.setComputedPriceTotal(computedPriceListItem.getComputedPriceTotal());
                }
                if (computedPriceListItem.getComputedQuantityTotal() != null) {
                    existingComputedPriceListItem.setComputedQuantityTotal(computedPriceListItem.getComputedQuantityTotal());
                }
                if (computedPriceListItem.getDescription() != null) {
                    existingComputedPriceListItem.setDescription(computedPriceListItem.getDescription());
                }

                return existingComputedPriceListItem;
            })
            .map(computedPriceListItemRepository::save);
    }

    /**
     * Get all the computedPriceListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComputedPriceListItem> findAll(Pageable pageable) {
        log.debug("Request to get all ComputedPriceListItems");
        return computedPriceListItemRepository.findAll(pageable);
    }

    /**
     * Get all the computedPriceListItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ComputedPriceListItem> findAllWithEagerRelationships(Pageable pageable) {
        return computedPriceListItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one computedPriceListItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComputedPriceListItem> findOne(Long id) {
        log.debug("Request to get ComputedPriceListItem : {}", id);
        return computedPriceListItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the computedPriceListItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ComputedPriceListItem : {}", id);
        computedPriceListItemRepository.deleteById(id);
    }
}
