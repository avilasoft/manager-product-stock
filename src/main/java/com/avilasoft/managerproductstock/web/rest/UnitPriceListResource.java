package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.UnitPriceList;
import com.avilasoft.managerproductstock.repository.UnitPriceListRepository;
import com.avilasoft.managerproductstock.service.UnitPriceListService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.UnitPriceList}.
 */
@RestController
@RequestMapping("/api")
public class UnitPriceListResource {

    private final Logger log = LoggerFactory.getLogger(UnitPriceListResource.class);

    private static final String ENTITY_NAME = "unitPriceList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitPriceListService unitPriceListService;

    private final UnitPriceListRepository unitPriceListRepository;

    public UnitPriceListResource(UnitPriceListService unitPriceListService, UnitPriceListRepository unitPriceListRepository) {
        this.unitPriceListService = unitPriceListService;
        this.unitPriceListRepository = unitPriceListRepository;
    }

    /**
     * {@code POST  /unit-price-lists} : Create a new unitPriceList.
     *
     * @param unitPriceList the unitPriceList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitPriceList, or with status {@code 400 (Bad Request)} if the unitPriceList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-price-lists")
    public ResponseEntity<UnitPriceList> createUnitPriceList(@Valid @RequestBody UnitPriceList unitPriceList) throws URISyntaxException {
        log.debug("REST request to save UnitPriceList : {}", unitPriceList);
        if (unitPriceList.getId() != null) {
            throw new BadRequestAlertException("A new unitPriceList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitPriceList result = unitPriceListService.save(unitPriceList);
        return ResponseEntity
            .created(new URI("/api/unit-price-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-price-lists/:id} : Updates an existing unitPriceList.
     *
     * @param id the id of the unitPriceList to save.
     * @param unitPriceList the unitPriceList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitPriceList,
     * or with status {@code 400 (Bad Request)} if the unitPriceList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitPriceList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-price-lists/{id}")
    public ResponseEntity<UnitPriceList> updateUnitPriceList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitPriceList unitPriceList
    ) throws URISyntaxException {
        log.debug("REST request to update UnitPriceList : {}, {}", id, unitPriceList);
        if (unitPriceList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitPriceList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitPriceListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitPriceList result = unitPriceListService.save(unitPriceList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitPriceList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-price-lists/:id} : Partial updates given fields of an existing unitPriceList, field will ignore if it is null
     *
     * @param id the id of the unitPriceList to save.
     * @param unitPriceList the unitPriceList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitPriceList,
     * or with status {@code 400 (Bad Request)} if the unitPriceList is not valid,
     * or with status {@code 404 (Not Found)} if the unitPriceList is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitPriceList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-price-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitPriceList> partialUpdateUnitPriceList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitPriceList unitPriceList
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitPriceList partially : {}, {}", id, unitPriceList);
        if (unitPriceList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitPriceList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitPriceListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitPriceList> result = unitPriceListService.partialUpdate(unitPriceList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitPriceList.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-price-lists} : get all the unitPriceLists.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitPriceLists in body.
     */
    @GetMapping("/unit-price-lists")
    public ResponseEntity<List<UnitPriceList>> getAllUnitPriceLists(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("projectitemlist-is-null".equals(filter)) {
            log.debug("REST request to get all UnitPriceLists where projectItemList is null");
            return new ResponseEntity<>(unitPriceListService.findAllWhereProjectItemListIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of UnitPriceLists");
        Page<UnitPriceList> page = unitPriceListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-price-lists/:id} : get the "id" unitPriceList.
     *
     * @param id the id of the unitPriceList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitPriceList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-price-lists/{id}")
    public ResponseEntity<UnitPriceList> getUnitPriceList(@PathVariable Long id) {
        log.debug("REST request to get UnitPriceList : {}", id);
        Optional<UnitPriceList> unitPriceList = unitPriceListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitPriceList);
    }

    /**
     * {@code DELETE  /unit-price-lists/:id} : delete the "id" unitPriceList.
     *
     * @param id the id of the unitPriceList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-price-lists/{id}")
    public ResponseEntity<Void> deleteUnitPriceList(@PathVariable Long id) {
        log.debug("REST request to delete UnitPriceList : {}", id);
        unitPriceListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
