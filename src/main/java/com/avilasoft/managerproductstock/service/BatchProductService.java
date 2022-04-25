package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.BatchProduct;
import com.avilasoft.managerproductstock.repository.BatchProductRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BatchProduct}.
 */
@Service
@Transactional
public class BatchProductService {

    private final Logger log = LoggerFactory.getLogger(BatchProductService.class);

    private final BatchProductRepository batchProductRepository;

    public BatchProductService(BatchProductRepository batchProductRepository) {
        this.batchProductRepository = batchProductRepository;
    }

    /**
     * Save a batchProduct.
     *
     * @param batchProduct the entity to save.
     * @return the persisted entity.
     */
    public BatchProduct save(BatchProduct batchProduct) {
        log.debug("Request to save BatchProduct : {}", batchProduct);
        return batchProductRepository.save(batchProduct);
    }

    /**
     * Partially update a batchProduct.
     *
     * @param batchProduct the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BatchProduct> partialUpdate(BatchProduct batchProduct) {
        log.debug("Request to partially update BatchProduct : {}", batchProduct);

        return batchProductRepository
            .findById(batchProduct.getId())
            .map(existingBatchProduct -> {
                if (batchProduct.getName() != null) {
                    existingBatchProduct.setName(batchProduct.getName());
                }
                if (batchProduct.getDescription() != null) {
                    existingBatchProduct.setDescription(batchProduct.getDescription());
                }

                return existingBatchProduct;
            })
            .map(batchProductRepository::save);
    }

    /**
     * Get all the batchProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BatchProduct> findAll(Pageable pageable) {
        log.debug("Request to get all BatchProducts");
        return batchProductRepository.findAll(pageable);
    }

    /**
     * Get all the batchProducts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BatchProduct> findAllWithEagerRelationships(Pageable pageable) {
        return batchProductRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one batchProduct by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BatchProduct> findOne(Long id) {
        log.debug("Request to get BatchProduct : {}", id);
        return batchProductRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the batchProduct by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BatchProduct : {}", id);
        batchProductRepository.deleteById(id);
    }
}
