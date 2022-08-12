package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ProviderItem;
import com.avilasoft.managerproductstock.repository.ProviderItemRepository;
import com.avilasoft.managerproductstock.service.ProviderItemService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ProviderItem}.
 */
@RestController
@RequestMapping("/api")
public class ProviderItemResource {

    private final Logger log = LoggerFactory.getLogger(ProviderItemResource.class);

    private static final String ENTITY_NAME = "providerItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviderItemService providerItemService;

    private final ProviderItemRepository providerItemRepository;

    public ProviderItemResource(ProviderItemService providerItemService, ProviderItemRepository providerItemRepository) {
        this.providerItemService = providerItemService;
        this.providerItemRepository = providerItemRepository;
    }

    /**
     * {@code POST  /provider-items} : Create a new providerItem.
     *
     * @param providerItem the providerItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new providerItem, or with status {@code 400 (Bad Request)} if the providerItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provider-items")
    public ResponseEntity<ProviderItem> createProviderItem(@Valid @RequestBody ProviderItem providerItem) throws URISyntaxException {
        log.debug("REST request to save ProviderItem : {}", providerItem);
        if (providerItem.getId() != null) {
            throw new BadRequestAlertException("A new providerItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProviderItem result = providerItemService.save(providerItem);
        return ResponseEntity
            .created(new URI("/api/provider-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provider-items/:id} : Updates an existing providerItem.
     *
     * @param id the id of the providerItem to save.
     * @param providerItem the providerItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated providerItem,
     * or with status {@code 400 (Bad Request)} if the providerItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the providerItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provider-items/{id}")
    public ResponseEntity<ProviderItem> updateProviderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProviderItem providerItem
    ) throws URISyntaxException {
        log.debug("REST request to update ProviderItem : {}, {}", id, providerItem);
        if (providerItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, providerItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProviderItem result = providerItemService.save(providerItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, providerItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /provider-items/:id} : Partial updates given fields of an existing providerItem, field will ignore if it is null
     *
     * @param id the id of the providerItem to save.
     * @param providerItem the providerItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated providerItem,
     * or with status {@code 400 (Bad Request)} if the providerItem is not valid,
     * or with status {@code 404 (Not Found)} if the providerItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the providerItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/provider-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProviderItem> partialUpdateProviderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProviderItem providerItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProviderItem partially : {}, {}", id, providerItem);
        if (providerItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, providerItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProviderItem> result = providerItemService.partialUpdate(providerItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, providerItem.getId().toString())
        );
    }

    /**
     * {@code GET  /provider-items} : get all the providerItems.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of providerItems in body.
     */
    @GetMapping("/provider-items")
    public ResponseEntity<List<ProviderItem>> getAllProviderItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProviderItems");
        Page<ProviderItem> page;
        if (eagerload) {
            page = providerItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = providerItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provider-items/:id} : get the "id" providerItem.
     *
     * @param id the id of the providerItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the providerItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provider-items/{id}")
    public ResponseEntity<ProviderItem> getProviderItem(@PathVariable Long id) {
        log.debug("REST request to get ProviderItem : {}", id);
        Optional<ProviderItem> providerItem = providerItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(providerItem);
    }

    /**
     * {@code DELETE  /provider-items/:id} : delete the "id" providerItem.
     *
     * @param id the id of the providerItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provider-items/{id}")
    public ResponseEntity<Void> deleteProviderItem(@PathVariable Long id) {
        log.debug("REST request to delete ProviderItem : {}", id);
        providerItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
