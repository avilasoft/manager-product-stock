package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.UnitPriceListItem;
import com.avilasoft.managerproductstock.repository.UnitPriceListItemRepository;
import com.avilasoft.managerproductstock.service.UnitPriceListItemService;
import com.avilasoft.managerproductstock.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.UnitPriceListItem}.
 */
@RestController
@RequestMapping("/api")
public class UnitPriceListItemResource {

    private final Logger log = LoggerFactory.getLogger(UnitPriceListItemResource.class);

    private static final String ENTITY_NAME = "unitPriceListItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitPriceListItemService unitPriceListItemService;

    private final UnitPriceListItemRepository unitPriceListItemRepository;

    public UnitPriceListItemResource(
        UnitPriceListItemService unitPriceListItemService,
        UnitPriceListItemRepository unitPriceListItemRepository
    ) {
        this.unitPriceListItemService = unitPriceListItemService;
        this.unitPriceListItemRepository = unitPriceListItemRepository;
    }

    /**
     * {@code POST  /unit-price-list-items} : Create a new unitPriceListItem.
     *
     * @param unitPriceListItem the unitPriceListItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitPriceListItem, or with status {@code 400 (Bad Request)} if the unitPriceListItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-price-list-items")
    public ResponseEntity<UnitPriceListItem> createUnitPriceListItem(@Valid @RequestBody UnitPriceListItem unitPriceListItem)
        throws URISyntaxException {
        log.debug("REST request to save UnitPriceListItem : {}", unitPriceListItem);
        if (unitPriceListItem.getId() != null) {
            throw new BadRequestAlertException("A new unitPriceListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitPriceListItem result = unitPriceListItemService.save(unitPriceListItem);
        return ResponseEntity
            .created(new URI("/api/unit-price-list-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-price-list-items/:id} : Updates an existing unitPriceListItem.
     *
     * @param id the id of the unitPriceListItem to save.
     * @param unitPriceListItem the unitPriceListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitPriceListItem,
     * or with status {@code 400 (Bad Request)} if the unitPriceListItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitPriceListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-price-list-items/{id}")
    public ResponseEntity<UnitPriceListItem> updateUnitPriceListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitPriceListItem unitPriceListItem
    ) throws URISyntaxException {
        log.debug("REST request to update UnitPriceListItem : {}, {}", id, unitPriceListItem);
        if (unitPriceListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitPriceListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitPriceListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitPriceListItem result = unitPriceListItemService.save(unitPriceListItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitPriceListItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-price-list-items/:id} : Partial updates given fields of an existing unitPriceListItem, field will ignore if it is null
     *
     * @param id the id of the unitPriceListItem to save.
     * @param unitPriceListItem the unitPriceListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitPriceListItem,
     * or with status {@code 400 (Bad Request)} if the unitPriceListItem is not valid,
     * or with status {@code 404 (Not Found)} if the unitPriceListItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitPriceListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-price-list-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitPriceListItem> partialUpdateUnitPriceListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitPriceListItem unitPriceListItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitPriceListItem partially : {}, {}", id, unitPriceListItem);
        if (unitPriceListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitPriceListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitPriceListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitPriceListItem> result = unitPriceListItemService.partialUpdate(unitPriceListItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitPriceListItem.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-price-list-items} : get all the unitPriceListItems.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitPriceListItems in body.
     */
    @GetMapping("/unit-price-list-items")
    public ResponseEntity<List<UnitPriceListItem>> getAllUnitPriceListItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("projectitemlistitem-is-null".equals(filter)) {
            log.debug("REST request to get all UnitPriceListItems where projectItemListItem is null");
            return new ResponseEntity<>(unitPriceListItemService.findAllWhereProjectItemListItemIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of UnitPriceListItems");
        Page<UnitPriceListItem> page;
        if (eagerload) {
            page = unitPriceListItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = unitPriceListItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-price-list-items/:id} : get the "id" unitPriceListItem.
     *
     * @param id the id of the unitPriceListItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitPriceListItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-price-list-items/{id}")
    public ResponseEntity<UnitPriceListItem> getUnitPriceListItem(@PathVariable Long id) {
        log.debug("REST request to get UnitPriceListItem : {}", id);
        Optional<UnitPriceListItem> unitPriceListItem = unitPriceListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitPriceListItem);
    }

    /**
     * {@code DELETE  /unit-price-list-items/:id} : delete the "id" unitPriceListItem.
     *
     * @param id the id of the unitPriceListItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-price-list-items/{id}")
    public ResponseEntity<Void> deleteUnitPriceListItem(@PathVariable Long id) {
        log.debug("REST request to delete UnitPriceListItem : {}", id);
        unitPriceListItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
