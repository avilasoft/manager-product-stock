package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.BatchStatus;
import com.avilasoft.managerproductstock.repository.BatchStatusRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BatchStatus}.
 */
@Service
@Transactional
public class BatchStatusService {

    private final Logger log = LoggerFactory.getLogger(BatchStatusService.class);

    private final BatchStatusRepository batchStatusRepository;

    public BatchStatusService(BatchStatusRepository batchStatusRepository) {
        this.batchStatusRepository = batchStatusRepository;
    }

    /**
     * Save a batchStatus.
     *
     * @param batchStatus the entity to save.
     * @return the persisted entity.
     */
    public BatchStatus save(BatchStatus batchStatus) {
        log.debug("Request to save BatchStatus : {}", batchStatus);
        return batchStatusRepository.save(batchStatus);
    }

    /**
     * Partially update a batchStatus.
     *
     * @param batchStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BatchStatus> partialUpdate(BatchStatus batchStatus) {
        log.debug("Request to partially update BatchStatus : {}", batchStatus);

        return batchStatusRepository
            .findById(batchStatus.getId())
            .map(existingBatchStatus -> {
                if (batchStatus.getName() != null) {
                    existingBatchStatus.setName(batchStatus.getName());
                }
                if (batchStatus.getDescription() != null) {
                    existingBatchStatus.setDescription(batchStatus.getDescription());
                }

                return existingBatchStatus;
            })
            .map(batchStatusRepository::save);
    }

    /**
     * Get all the batchStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BatchStatus> findAll(Pageable pageable) {
        log.debug("Request to get all BatchStatuses");
        return batchStatusRepository.findAll(pageable);
    }

    /**
     * Get one batchStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BatchStatus> findOne(Long id) {
        log.debug("Request to get BatchStatus : {}", id);
        return batchStatusRepository.findById(id);
    }

    /**
     * Delete the batchStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BatchStatus : {}", id);
        batchStatusRepository.deleteById(id);
    }
}
