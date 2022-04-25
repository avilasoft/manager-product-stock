package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ProviderProduct;
import com.avilasoft.managerproductstock.repository.ProviderProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProviderProduct}.
 */
@Service
@Transactional
public class ProviderProductService {

    private final Logger log = LoggerFactory.getLogger(ProviderProductService.class);

    private final ProviderProductRepository providerProductRepository;

    public ProviderProductService(ProviderProductRepository providerProductRepository) {
        this.providerProductRepository = providerProductRepository;
    }

    /**
     * Save a providerProduct.
     *
     * @param providerProduct the entity to save.
     * @return the persisted entity.
     */
    public ProviderProduct save(ProviderProduct providerProduct) {
        log.debug("Request to save ProviderProduct : {}", providerProduct);
        return providerProductRepository.save(providerProduct);
    }

    /**
     * Partially update a providerProduct.
     *
     * @param providerProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProviderProduct> partialUpdate(ProviderProduct providerProduct) {
        log.debug("Request to partially update ProviderProduct : {}", providerProduct);

        return providerProductRepository
            .findById(providerProduct.getId())
            .map(existingProviderProduct -> {
                if (providerProduct.getName() != null) {
                    existingProviderProduct.setName(providerProduct.getName());
                }
                if (providerProduct.getDescription() != null) {
                    existingProviderProduct.setDescription(providerProduct.getDescription());
                }

                return existingProviderProduct;
            })
            .map(providerProductRepository::save);
    }

    /**
     * Get all the providerProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProviderProduct> findAll(Pageable pageable) {
        log.debug("Request to get all ProviderProducts");
        return providerProductRepository.findAll(pageable);
    }

    /**
     * Get all the providerProducts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProviderProduct> findAllWithEagerRelationships(Pageable pageable) {
        return providerProductRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the providerProducts where ProviderProductPrice is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProviderProduct> findAllWhereProviderProductPriceIsNull() {
        log.debug("Request to get all providerProducts where ProviderProductPrice is null");
        return StreamSupport
            .stream(providerProductRepository.findAll().spliterator(), false)
            .filter(providerProduct -> providerProduct.getProviderProductPrice() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one providerProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProviderProduct> findOne(Long id) {
        log.debug("Request to get ProviderProduct : {}", id);
        return providerProductRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the providerProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProviderProduct : {}", id);
        providerProductRepository.deleteById(id);
    }
}
