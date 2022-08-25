package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.UnitPriceList;
import com.avilasoft.managerproductstock.repository.UnitPriceListRepository;
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
 * Service Implementation for managing {@link UnitPriceList}.
 */
@Service
@Transactional
public class UnitPriceListService {

    private final Logger log = LoggerFactory.getLogger(UnitPriceListService.class);

    private final UnitPriceListRepository unitPriceListRepository;

    public UnitPriceListService(UnitPriceListRepository unitPriceListRepository) {
        this.unitPriceListRepository = unitPriceListRepository;
    }

    /**
     * Save a unitPriceList.
     *
     * @param unitPriceList the entity to save.
     * @return the persisted entity.
     */
    public UnitPriceList save(UnitPriceList unitPriceList) {
        log.debug("Request to save UnitPriceList : {}", unitPriceList);
        return unitPriceListRepository.save(unitPriceList);
    }

    /**
     * Partially update a unitPriceList.
     *
     * @param unitPriceList the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UnitPriceList> partialUpdate(UnitPriceList unitPriceList) {
        log.debug("Request to partially update UnitPriceList : {}", unitPriceList);

        return unitPriceListRepository
            .findById(unitPriceList.getId())
            .map(existingUnitPriceList -> {
                if (unitPriceList.getCode() != null) {
                    existingUnitPriceList.setCode(unitPriceList.getCode());
                }
                if (unitPriceList.getUnitPriceListTotal() != null) {
                    existingUnitPriceList.setUnitPriceListTotal(unitPriceList.getUnitPriceListTotal());
                }
                if (unitPriceList.getDescription() != null) {
                    existingUnitPriceList.setDescription(unitPriceList.getDescription());
                }

                return existingUnitPriceList;
            })
            .map(unitPriceListRepository::save);
    }

    /**
     * Get all the unitPriceLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UnitPriceList> findAll(Pageable pageable) {
        log.debug("Request to get all UnitPriceLists");
        return unitPriceListRepository.findAll(pageable);
    }

    /**
     *  Get all the unitPriceLists where ProjectItemList is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UnitPriceList> findAllWhereProjectItemListIsNull() {
        log.debug("Request to get all unitPriceLists where ProjectItemList is null");
        return StreamSupport
            .stream(unitPriceListRepository.findAll().spliterator(), false)
            .filter(unitPriceList -> unitPriceList.getProjectItemList() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one unitPriceList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UnitPriceList> findOne(Long id) {
        log.debug("Request to get UnitPriceList : {}", id);
        return unitPriceListRepository.findById(id);
    }

    /**
     * Delete the unitPriceList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UnitPriceList : {}", id);
        unitPriceListRepository.deleteById(id);
    }
}
