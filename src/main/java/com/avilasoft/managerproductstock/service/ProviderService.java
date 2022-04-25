package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.Provider;
import com.avilasoft.managerproductstock.repository.ProviderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Provider}.
 */
@Service
@Transactional
public class ProviderService {

    private final Logger log = LoggerFactory.getLogger(ProviderService.class);

    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    /**
     * Save a provider.
     *
     * @param provider the entity to save.
     * @return the persisted entity.
     */
    public Provider save(Provider provider) {
        log.debug("Request to save Provider : {}", provider);
        return providerRepository.save(provider);
    }

    /**
     * Partially update a provider.
     *
     * @param provider the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Provider> partialUpdate(Provider provider) {
        log.debug("Request to partially update Provider : {}", provider);

        return providerRepository
            .findById(provider.getId())
            .map(existingProvider -> {
                if (provider.getName() != null) {
                    existingProvider.setName(provider.getName());
                }
                if (provider.getDescrption() != null) {
                    existingProvider.setDescrption(provider.getDescrption());
                }
                if (provider.getEmail() != null) {
                    existingProvider.setEmail(provider.getEmail());
                }
                if (provider.getPhone() != null) {
                    existingProvider.setPhone(provider.getPhone());
                }
                if (provider.getAddressLine1() != null) {
                    existingProvider.setAddressLine1(provider.getAddressLine1());
                }
                if (provider.getAddressLine2() != null) {
                    existingProvider.setAddressLine2(provider.getAddressLine2());
                }
                if (provider.getCity() != null) {
                    existingProvider.setCity(provider.getCity());
                }
                if (provider.getCountry() != null) {
                    existingProvider.setCountry(provider.getCountry());
                }

                return existingProvider;
            })
            .map(providerRepository::save);
    }

    /**
     * Get all the providers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Provider> findAll(Pageable pageable) {
        log.debug("Request to get all Providers");
        return providerRepository.findAll(pageable);
    }

    /**
     * Get all the providers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Provider> findAllWithEagerRelationships(Pageable pageable) {
        return providerRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one provider by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Provider> findOne(Long id) {
        log.debug("Request to get Provider : {}", id);
        return providerRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the provider by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Provider : {}", id);
        providerRepository.deleteById(id);
    }
}
