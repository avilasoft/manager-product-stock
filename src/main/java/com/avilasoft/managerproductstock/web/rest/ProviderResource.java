package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.Provider;
import com.avilasoft.managerproductstock.repository.ProviderRepository;
import com.avilasoft.managerproductstock.service.ProviderService;
import com.avilasoft.managerproductstock.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.Provider}.
 */
@RestController
@RequestMapping("/api")
public class ProviderResource {

    private final Logger log = LoggerFactory.getLogger(ProviderResource.class);

    private static final String ENTITY_NAME = "provider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviderService providerService;

    private final ProviderRepository providerRepository;

    public ProviderResource(ProviderService providerService, ProviderRepository providerRepository) {
        this.providerService = providerService;
        this.providerRepository = providerRepository;
    }

    /**
     * {@code POST  /providers} : Create a new provider.
     *
     * @param provider the provider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provider, or with status {@code 400 (Bad Request)} if the provider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/providers")
    public ResponseEntity<Provider> createProvider(@Valid @RequestBody Provider provider) throws URISyntaxException {
        log.debug("REST request to save Provider : {}", provider);
        if (provider.getId() != null) {
            throw new BadRequestAlertException("A new provider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Provider result = providerService.save(provider);
        return ResponseEntity
            .created(new URI("/api/providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /providers/:id} : Updates an existing provider.
     *
     * @param id the id of the provider to save.
     * @param provider the provider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provider,
     * or with status {@code 400 (Bad Request)} if the provider is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/providers/{id}")
    public ResponseEntity<Provider> updateProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Provider provider
    ) throws URISyntaxException {
        log.debug("REST request to update Provider : {}, {}", id, provider);
        if (provider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Provider result = providerService.save(provider);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provider.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /providers/:id} : Partial updates given fields of an existing provider, field will ignore if it is null
     *
     * @param id the id of the provider to save.
     * @param provider the provider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provider,
     * or with status {@code 400 (Bad Request)} if the provider is not valid,
     * or with status {@code 404 (Not Found)} if the provider is not found,
     * or with status {@code 500 (Internal Server Error)} if the provider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/providers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Provider> partialUpdateProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Provider provider
    ) throws URISyntaxException {
        log.debug("REST request to partial update Provider partially : {}, {}", id, provider);
        if (provider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Provider> result = providerService.partialUpdate(provider);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provider.getId().toString())
        );
    }

    /**
     * {@code GET  /providers} : get all the providers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of providers in body.
     */
    @GetMapping("/providers")
    public ResponseEntity<List<Provider>> getAllProviders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Providers");
        Page<Provider> page;
        if (eagerload) {
            page = providerService.findAllWithEagerRelationships(pageable);
        } else {
            page = providerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /providers/:id} : get the "id" provider.
     *
     * @param id the id of the provider to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provider, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/providers/{id}")
    public ResponseEntity<Provider> getProvider(@PathVariable Long id) {
        log.debug("REST request to get Provider : {}", id);
        Optional<Provider> provider = providerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provider);
    }

    /**
     * {@code DELETE  /providers/:id} : delete the "id" provider.
     *
     * @param id the id of the provider to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/providers/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        log.debug("REST request to delete Provider : {}", id);
        providerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}