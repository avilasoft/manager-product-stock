package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.PriceHistory;
import com.avilasoft.managerproductstock.repository.PriceHistoryRepository;
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
 * Service Implementation for managing {@link PriceHistory}.
 */
@Service
@Transactional
public class PriceHistoryService {

    private final Logger log = LoggerFactory.getLogger(PriceHistoryService.class);

    private final PriceHistoryRepository priceHistoryRepository;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    /**
     * Save a priceHistory.
     *
     * @param priceHistory the entity to save.
     * @return the persisted entity.
     */
    public PriceHistory save(PriceHistory priceHistory) {
        log.debug("Request to save PriceHistory : {}", priceHistory);
        return priceHistoryRepository.save(priceHistory);
    }

    /**
     * Partially update a priceHistory.
     *
     * @param priceHistory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PriceHistory> partialUpdate(PriceHistory priceHistory) {
        log.debug("Request to partially update PriceHistory : {}", priceHistory);

        return priceHistoryRepository
            .findById(priceHistory.getId())
            .map(existingPriceHistory -> {
                if (priceHistory.getName() != null) {
                    existingPriceHistory.setName(priceHistory.getName());
                }
                if (priceHistory.getDescription() != null) {
                    existingPriceHistory.setDescription(priceHistory.getDescription());
                }

                return existingPriceHistory;
            })
            .map(priceHistoryRepository::save);
    }

    /**
     * Get all the priceHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceHistory> findAll(Pageable pageable) {
        log.debug("Request to get all PriceHistories");
        return priceHistoryRepository.findAll(pageable);
    }

    /**
     * Get all the priceHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PriceHistory> findAllWithEagerRelationships(Pageable pageable) {
        return priceHistoryRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the priceHistories where Child is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PriceHistory> findAllWhereChildIsNull() {
        log.debug("Request to get all priceHistories where Child is null");
        return StreamSupport
            .stream(priceHistoryRepository.findAll().spliterator(), false)
            .filter(priceHistory -> priceHistory.getChild() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one priceHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PriceHistory> findOne(Long id) {
        log.debug("Request to get PriceHistory : {}", id);
        return priceHistoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the priceHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PriceHistory : {}", id);
        priceHistoryRepository.deleteById(id);
    }
}
