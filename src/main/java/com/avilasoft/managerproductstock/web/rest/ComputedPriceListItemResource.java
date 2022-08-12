package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ComputedPriceListItem;
import com.avilasoft.managerproductstock.repository.ComputedPriceListItemRepository;
import com.avilasoft.managerproductstock.service.ComputedPriceListItemService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ComputedPriceListItem}.
 */
@RestController
@RequestMapping("/api")
public class ComputedPriceListItemResource {

    private final Logger log = LoggerFactory.getLogger(ComputedPriceListItemResource.class);

    private static final String ENTITY_NAME = "computedPriceListItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComputedPriceListItemService computedPriceListItemService;

    private final ComputedPriceListItemRepository computedPriceListItemRepository;

    public ComputedPriceListItemResource(
        ComputedPriceListItemService computedPriceListItemService,
        ComputedPriceListItemRepository computedPriceListItemRepository
    ) {
        this.computedPriceListItemService = computedPriceListItemService;
        this.computedPriceListItemRepository = computedPriceListItemRepository;
    }

    /**
     * {@code POST  /computed-price-list-items} : Create a new computedPriceListItem.
     *
     * @param computedPriceListItem the computedPriceListItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new computedPriceListItem, or with status {@code 400 (Bad Request)} if the computedPriceListItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/computed-price-list-items")
    public ResponseEntity<ComputedPriceListItem> createComputedPriceListItem(
        @Valid @RequestBody ComputedPriceListItem computedPriceListItem
    ) throws URISyntaxException {
        log.debug("REST request to save ComputedPriceListItem : {}", computedPriceListItem);
        if (computedPriceListItem.getId() != null) {
            throw new BadRequestAlertException("A new computedPriceListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComputedPriceListItem result = computedPriceListItemService.save(computedPriceListItem);
        return ResponseEntity
            .created(new URI("/api/computed-price-list-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /computed-price-list-items/:id} : Updates an existing computedPriceListItem.
     *
     * @param id the id of the computedPriceListItem to save.
     * @param computedPriceListItem the computedPriceListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computedPriceListItem,
     * or with status {@code 400 (Bad Request)} if the computedPriceListItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the computedPriceListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/computed-price-list-items/{id}")
    public ResponseEntity<ComputedPriceListItem> updateComputedPriceListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComputedPriceListItem computedPriceListItem
    ) throws URISyntaxException {
        log.debug("REST request to update ComputedPriceListItem : {}, {}", id, computedPriceListItem);
        if (computedPriceListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computedPriceListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computedPriceListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComputedPriceListItem result = computedPriceListItemService.save(computedPriceListItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, computedPriceListItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /computed-price-list-items/:id} : Partial updates given fields of an existing computedPriceListItem, field will ignore if it is null
     *
     * @param id the id of the computedPriceListItem to save.
     * @param computedPriceListItem the computedPriceListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computedPriceListItem,
     * or with status {@code 400 (Bad Request)} if the computedPriceListItem is not valid,
     * or with status {@code 404 (Not Found)} if the computedPriceListItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the computedPriceListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/computed-price-list-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComputedPriceListItem> partialUpdateComputedPriceListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComputedPriceListItem computedPriceListItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComputedPriceListItem partially : {}, {}", id, computedPriceListItem);
        if (computedPriceListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computedPriceListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computedPriceListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComputedPriceListItem> result = computedPriceListItemService.partialUpdate(computedPriceListItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, computedPriceListItem.getId().toString())
        );
    }

    /**
     * {@code GET  /computed-price-list-items} : get all the computedPriceListItems.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of computedPriceListItems in body.
     */
    @GetMapping("/computed-price-list-items")
    public ResponseEntity<List<ComputedPriceListItem>> getAllComputedPriceListItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ComputedPriceListItems");
        Page<ComputedPriceListItem> page;
        if (eagerload) {
            page = computedPriceListItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = computedPriceListItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /computed-price-list-items/:id} : get the "id" computedPriceListItem.
     *
     * @param id the id of the computedPriceListItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the computedPriceListItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/computed-price-list-items/{id}")
    public ResponseEntity<ComputedPriceListItem> getComputedPriceListItem(@PathVariable Long id) {
        log.debug("REST request to get ComputedPriceListItem : {}", id);
        Optional<ComputedPriceListItem> computedPriceListItem = computedPriceListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(computedPriceListItem);
    }

    /**
     * {@code DELETE  /computed-price-list-items/:id} : delete the "id" computedPriceListItem.
     *
     * @param id the id of the computedPriceListItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/computed-price-list-items/{id}")
    public ResponseEntity<Void> deleteComputedPriceListItem(@PathVariable Long id) {
        log.debug("REST request to delete ComputedPriceListItem : {}", id);
        computedPriceListItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
