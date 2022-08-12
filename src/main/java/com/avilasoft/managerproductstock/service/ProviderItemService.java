package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ProviderItem;
import com.avilasoft.managerproductstock.repository.ProviderItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProviderItem}.
 */
@Service
@Transactional
public class ProviderItemService {

    private final Logger log = LoggerFactory.getLogger(ProviderItemService.class);

    private final ProviderItemRepository providerItemRepository;

    public ProviderItemService(ProviderItemRepository providerItemRepository) {
        this.providerItemRepository = providerItemRepository;
    }

    /**
     * Save a providerItem.
     *
     * @param providerItem the entity to save.
     * @return the persisted entity.
     */
    public ProviderItem save(ProviderItem providerItem) {
        log.debug("Request to save ProviderItem : {}", providerItem);
        return providerItemRepository.save(providerItem);
    }

    /**
     * Partially update a providerItem.
     *
     * @param providerItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProviderItem> partialUpdate(ProviderItem providerItem) {
        log.debug("Request to partially update ProviderItem : {}", providerItem);

        return providerItemRepository
            .findById(providerItem.getId())
            .map(existingProviderItem -> {
                if (providerItem.getName() != null) {
                    existingProviderItem.setName(providerItem.getName());
                }
                if (providerItem.getDescription() != null) {
                    existingProviderItem.setDescription(providerItem.getDescription());
                }
                if (providerItem.getCost() != null) {
                    existingProviderItem.setCost(providerItem.getCost());
                }

                return existingProviderItem;
            })
            .map(providerItemRepository::save);
    }

    /**
     * Get all the providerItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProviderItem> findAll(Pageable pageable) {
        log.debug("Request to get all ProviderItems");
        return providerItemRepository.findAll(pageable);
    }

    /**
     * Get all the providerItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProviderItem> findAllWithEagerRelationships(Pageable pageable) {
        return providerItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one providerItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProviderItem> findOne(Long id) {
        log.debug("Request to get ProviderItem : {}", id);
        return providerItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the providerItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProviderItem : {}", id);
        providerItemRepository.deleteById(id);
    }
}
