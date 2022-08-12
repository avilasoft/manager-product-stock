package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ComputedPriceList;
import com.avilasoft.managerproductstock.repository.ComputedPriceListRepository;
import com.avilasoft.managerproductstock.service.ComputedPriceListService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ComputedPriceList}.
 */
@RestController
@RequestMapping("/api")
public class ComputedPriceListResource {

    private final Logger log = LoggerFactory.getLogger(ComputedPriceListResource.class);

    private static final String ENTITY_NAME = "computedPriceList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComputedPriceListService computedPriceListService;

    private final ComputedPriceListRepository computedPriceListRepository;

    public ComputedPriceListResource(
        ComputedPriceListService computedPriceListService,
        ComputedPriceListRepository computedPriceListRepository
    ) {
        this.computedPriceListService = computedPriceListService;
        this.computedPriceListRepository = computedPriceListRepository;
    }

    /**
     * {@code POST  /computed-price-lists} : Create a new computedPriceList.
     *
     * @param computedPriceList the computedPriceList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new computedPriceList, or with status {@code 400 (Bad Request)} if the computedPriceList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/computed-price-lists")
    public ResponseEntity<ComputedPriceList> createComputedPriceList(@Valid @RequestBody ComputedPriceList computedPriceList)
        throws URISyntaxException {
        log.debug("REST request to save ComputedPriceList : {}", computedPriceList);
        if (computedPriceList.getId() != null) {
            throw new BadRequestAlertException("A new computedPriceList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComputedPriceList result = computedPriceListService.save(computedPriceList);
        return ResponseEntity
            .created(new URI("/api/computed-price-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /computed-price-lists/:id} : Updates an existing computedPriceList.
     *
     * @param id the id of the computedPriceList to save.
     * @param computedPriceList the computedPriceList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computedPriceList,
     * or with status {@code 400 (Bad Request)} if the computedPriceList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the computedPriceList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/computed-price-lists/{id}")
    public ResponseEntity<ComputedPriceList> updateComputedPriceList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComputedPriceList computedPriceList
    ) throws URISyntaxException {
        log.debug("REST request to update ComputedPriceList : {}, {}", id, computedPriceList);
        if (computedPriceList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computedPriceList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computedPriceListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComputedPriceList result = computedPriceListService.save(computedPriceList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, computedPriceList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /computed-price-lists/:id} : Partial updates given fields of an existing computedPriceList, field will ignore if it is null
     *
     * @param id the id of the computedPriceList to save.
     * @param computedPriceList the computedPriceList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computedPriceList,
     * or with status {@code 400 (Bad Request)} if the computedPriceList is not valid,
     * or with status {@code 404 (Not Found)} if the computedPriceList is not found,
     * or with status {@code 500 (Internal Server Error)} if the computedPriceList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/computed-price-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComputedPriceList> partialUpdateComputedPriceList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ComputedPriceList computedPriceList
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComputedPriceList partially : {}, {}", id, computedPriceList);
        if (computedPriceList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computedPriceList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computedPriceListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComputedPriceList> result = computedPriceListService.partialUpdate(computedPriceList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, computedPriceList.getId().toString())
        );
    }

    /**
     * {@code GET  /computed-price-lists} : get all the computedPriceLists.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of computedPriceLists in body.
     */
    @GetMapping("/computed-price-lists")
    public ResponseEntity<List<ComputedPriceList>> getAllComputedPriceLists(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("projectitemlist-is-null".equals(filter)) {
            log.debug("REST request to get all ComputedPriceLists where projectItemList is null");
            return new ResponseEntity<>(computedPriceListService.findAllWhereProjectItemListIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of ComputedPriceLists");
        Page<ComputedPriceList> page = computedPriceListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /computed-price-lists/:id} : get the "id" computedPriceList.
     *
     * @param id the id of the computedPriceList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the computedPriceList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/computed-price-lists/{id}")
    public ResponseEntity<ComputedPriceList> getComputedPriceList(@PathVariable Long id) {
        log.debug("REST request to get ComputedPriceList : {}", id);
        Optional<ComputedPriceList> computedPriceList = computedPriceListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(computedPriceList);
    }

    /**
     * {@code DELETE  /computed-price-lists/:id} : delete the "id" computedPriceList.
     *
     * @param id the id of the computedPriceList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/computed-price-lists/{id}")
    public ResponseEntity<Void> deleteComputedPriceList(@PathVariable Long id) {
        log.debug("REST request to delete ComputedPriceList : {}", id);
        computedPriceListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
