package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ProviderProductPrice;
import com.avilasoft.managerproductstock.repository.ProviderProductPriceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProviderProductPrice}.
 */
@Service
@Transactional
public class ProviderProductPriceService {

    private final Logger log = LoggerFactory.getLogger(ProviderProductPriceService.class);

    private final ProviderProductPriceRepository providerProductPriceRepository;

    public ProviderProductPriceService(ProviderProductPriceRepository providerProductPriceRepository) {
        this.providerProductPriceRepository = providerProductPriceRepository;
    }

    /**
     * Save a providerProductPrice.
     *
     * @param providerProductPrice the entity to save.
     * @return the persisted entity.
     */
    public ProviderProductPrice save(ProviderProductPrice providerProductPrice) {
        log.debug("Request to save ProviderProductPrice : {}", providerProductPrice);
        return providerProductPriceRepository.save(providerProductPrice);
    }

    /**
     * Partially update a providerProductPrice.
     *
     * @param providerProductPrice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProviderProductPrice> partialUpdate(ProviderProductPrice providerProductPrice) {
        log.debug("Request to partially update ProviderProductPrice : {}", providerProductPrice);

        return providerProductPriceRepository
            .findById(providerProductPrice.getId())
            .map(existingProviderProductPrice -> {
                if (providerProductPrice.getPrice() != null) {
                    existingProviderProductPrice.setPrice(providerProductPrice.getPrice());
                }

                return existingProviderProductPrice;
            })
            .map(providerProductPriceRepository::save);
    }

    /**
     * Get all the providerProductPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProviderProductPrice> findAll(Pageable pageable) {
        log.debug("Request to get all ProviderProductPrices");
        return providerProductPriceRepository.findAll(pageable);
    }

    /**
     * Get all the providerProductPrices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProviderProductPrice> findAllWithEagerRelationships(Pageable pageable) {
        return providerProductPriceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one providerProductPrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProviderProductPrice> findOne(Long id) {
        log.debug("Request to get ProviderProductPrice : {}", id);
        return providerProductPriceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the providerProductPrice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProviderProductPrice : {}", id);
        providerProductPriceRepository.deleteById(id);
    }
}
