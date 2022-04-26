package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.Price;
import com.avilasoft.managerproductstock.repository.PriceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Price}.
 */
@Service
@Transactional
public class PriceService {

    private final Logger log = LoggerFactory.getLogger(PriceService.class);

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Save a price.
     *
     * @param price the entity to save.
     * @return the persisted entity.
     */
    public Price save(Price price) {
        log.debug("Request to save Price : {}", price);
        return priceRepository.save(price);
    }

    /**
     * Partially update a price.
     *
     * @param price the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Price> partialUpdate(Price price) {
        log.debug("Request to partially update Price : {}", price);

        return priceRepository
            .findById(price.getId())
            .map(existingPrice -> {
                if (price.getName() != null) {
                    existingPrice.setName(price.getName());
                }
                if (price.getDescription() != null) {
                    existingPrice.setDescription(price.getDescription());
                }

                return existingPrice;
            })
            .map(priceRepository::save);
    }

    /**
     * Get all the prices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Price> findAll(Pageable pageable) {
        log.debug("Request to get all Prices");
        return priceRepository.findAll(pageable);
    }

    /**
     * Get one price by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Price> findOne(Long id) {
        log.debug("Request to get Price : {}", id);
        return priceRepository.findById(id);
    }

    /**
     * Delete the price by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Price : {}", id);
        priceRepository.deleteById(id);
    }
}
