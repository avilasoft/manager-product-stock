package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ComputedPriceList;
import com.avilasoft.managerproductstock.repository.ComputedPriceListRepository;
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
 * Service Implementation for managing {@link ComputedPriceList}.
 */
@Service
@Transactional
public class ComputedPriceListService {

    private final Logger log = LoggerFactory.getLogger(ComputedPriceListService.class);

    private final ComputedPriceListRepository computedPriceListRepository;

    public ComputedPriceListService(ComputedPriceListRepository computedPriceListRepository) {
        this.computedPriceListRepository = computedPriceListRepository;
    }

    /**
     * Save a computedPriceList.
     *
     * @param computedPriceList the entity to save.
     * @return the persisted entity.
     */
    public ComputedPriceList save(ComputedPriceList computedPriceList) {
        log.debug("Request to save ComputedPriceList : {}", computedPriceList);
        return computedPriceListRepository.save(computedPriceList);
    }

    /**
     * Partially update a computedPriceList.
     *
     * @param computedPriceList the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComputedPriceList> partialUpdate(ComputedPriceList computedPriceList) {
        log.debug("Request to partially update ComputedPriceList : {}", computedPriceList);

        return computedPriceListRepository
            .findById(computedPriceList.getId())
            .map(existingComputedPriceList -> {
                if (computedPriceList.getCode() != null) {
                    existingComputedPriceList.setCode(computedPriceList.getCode());
                }
                if (computedPriceList.getComputedPriceListTotal() != null) {
                    existingComputedPriceList.setComputedPriceListTotal(computedPriceList.getComputedPriceListTotal());
                }
                if (computedPriceList.getDescription() != null) {
                    existingComputedPriceList.setDescription(computedPriceList.getDescription());
                }

                return existingComputedPriceList;
            })
            .map(computedPriceListRepository::save);
    }

    /**
     * Get all the computedPriceLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComputedPriceList> findAll(Pageable pageable) {
        log.debug("Request to get all ComputedPriceLists");
        return computedPriceListRepository.findAll(pageable);
    }

    /**
     *  Get all the computedPriceLists where ProjectItemList is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ComputedPriceList> findAllWhereProjectItemListIsNull() {
        log.debug("Request to get all computedPriceLists where ProjectItemList is null");
        return StreamSupport
            .stream(computedPriceListRepository.findAll().spliterator(), false)
            .filter(computedPriceList -> computedPriceList.getProjectItemList() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one computedPriceList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComputedPriceList> findOne(Long id) {
        log.debug("Request to get ComputedPriceList : {}", id);
        return computedPriceListRepository.findById(id);
    }

    /**
     * Delete the computedPriceList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ComputedPriceList : {}", id);
        computedPriceListRepository.deleteById(id);
    }
}
