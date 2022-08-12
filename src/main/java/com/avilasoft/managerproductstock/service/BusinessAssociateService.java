package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import com.avilasoft.managerproductstock.repository.BusinessAssociateRepository;
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
                if (businessAssociate.getNickname() != null) {
                    existingBusinessAssociate.setNickname(businessAssociate.getNickname());
                }
                if (businessAssociate.getName() != null) {
                    existingBusinessAssociate.setName(businessAssociate.getName());
                }
                if (businessAssociate.getLastname() != null) {
                    existingBusinessAssociate.setLastname(businessAssociate.getLastname());
                }
                if (businessAssociate.getComercialName() != null) {
                    existingBusinessAssociate.setComercialName(businessAssociate.getComercialName());
                }
                if (businessAssociate.getPhone() != null) {
                    existingBusinessAssociate.setPhone(businessAssociate.getPhone());
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
     *  Get all the businessAssociates where Provider is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessAssociate> findAllWhereProviderIsNull() {
        log.debug("Request to get all businessAssociates where Provider is null");
        return StreamSupport
            .stream(businessAssociateRepository.findAll().spliterator(), false)
            .filter(businessAssociate -> businessAssociate.getProvider() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the businessAssociates where Project is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessAssociate> findAllWhereProjectIsNull() {
        log.debug("Request to get all businessAssociates where Project is null");
        return StreamSupport
            .stream(businessAssociateRepository.findAll().spliterator(), false)
            .filter(businessAssociate -> businessAssociate.getProject() == null)
            .collect(Collectors.toList());
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
