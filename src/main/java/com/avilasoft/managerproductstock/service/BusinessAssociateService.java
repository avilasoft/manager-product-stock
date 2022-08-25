package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import com.avilasoft.managerproductstock.repository.BusinessAssociateRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BusinessAssociate}.
 */
@Service
@Transactional
public class BusinessAssociateService {

    private final Logger log = LoggerFactory.getLogger(BusinessAssociateService.class);

    private final BusinessAssociateRepository businessAssociateRepository;

    public BusinessAssociateService(BusinessAssociateRepository businessAssociateRepository) {
        this.businessAssociateRepository = businessAssociateRepository;
    }

    /**
     * Save a businessAssociate.
     *
     * @param businessAssociate the entity to save.
     * @return the persisted entity.
     */
    public BusinessAssociate save(BusinessAssociate businessAssociate) {
        log.debug("Request to save BusinessAssociate : {}", businessAssociate);
        return businessAssociateRepository.save(businessAssociate);
    }

    /**
     * Partially update a businessAssociate.
     *
     * @param businessAssociate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessAssociate> partialUpdate(BusinessAssociate businessAssociate) {
        log.debug("Request to partially update BusinessAssociate : {}", businessAssociate);

        return businessAssociateRepository
            .findById(businessAssociate.getId())
            .map(existingBusinessAssociate -> {
                if (businessAssociate.getComercialName() != null) {
                    existingBusinessAssociate.setComercialName(businessAssociate.getComercialName());
                }
                if (businessAssociate.getDescription() != null) {
                    existingBusinessAssociate.setDescription(businessAssociate.getDescription());
                }
                if (businessAssociate.getType() != null) {
                    existingBusinessAssociate.setType(businessAssociate.getType());
                }

                return existingBusinessAssociate;
            })
            .map(businessAssociateRepository::save);
    }

    /**
     * Get all the businessAssociates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessAssociate> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessAssociates");
        return businessAssociateRepository.findAll(pageable);
    }

    /**
     * Get all the businessAssociates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BusinessAssociate> findAllWithEagerRelationships(Pageable pageable) {
        return businessAssociateRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one businessAssociate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessAssociate> findOne(Long id) {
        log.debug("Request to get BusinessAssociate : {}", id);
        return businessAssociateRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the businessAssociate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessAssociate : {}", id);
        businessAssociateRepository.deleteById(id);
    }
}
