package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ProviderProductPrice;
import com.avilasoft.managerproductstock.repository.ProviderProductPriceRepository;
import com.avilasoft.managerproductstock.service.ProviderProductPriceService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ProviderProductPrice}.
 */
@RestController
@RequestMapping("/api")
public class ProviderProductPriceResource {

    private final Logger log = LoggerFactory.getLogger(ProviderProductPriceResource.class);

    private static final String ENTITY_NAME = "providerProductPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviderProductPriceService providerProductPriceService;

    private final ProviderProductPriceRepository providerProductPriceRepository;

    public ProviderProductPriceResource(
        ProviderProductPriceService providerProductPriceService,
        ProviderProductPriceRepository providerProductPriceRepository
    ) {
        this.providerProductPriceService = providerProductPriceService;
        this.providerProductPriceRepository = providerProductPriceRepository;
    }

    /**
     * {@code POST  /provider-product-prices} : Create a new providerProductPrice.
     *
     * @param providerProductPrice the providerProductPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new providerProductPrice, or with status {@code 400 (Bad Request)} if the providerProductPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provider-product-prices")
    public ResponseEntity<ProviderProductPrice> createProviderProductPrice(@Valid @RequestBody ProviderProductPrice providerProductPrice)
        throws URISyntaxException {
        log.debug("REST request to save ProviderProductPrice : {}", providerProductPrice);
        if (providerProductPrice.getId() != null) {
            throw new BadRequestAlertException("A new providerProductPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProviderProductPrice result = providerProductPriceService.save(providerProductPrice);
        return ResponseEntity
            .created(new URI("/api/provider-product-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provider-product-prices/:id} : Updates an existing providerProductPrice.
     *
     * @param id the id of the providerProductPrice to save.
     * @param providerProductPrice the providerProductPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated providerProductPrice,
     * or with status {@code 400 (Bad Request)} if the providerProductPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the providerProductPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provider-product-prices/{id}")
    public ResponseEntity<ProviderProductPrice> updateProviderProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProviderProductPrice providerProductPrice
    ) throws URISyntaxException {
        log.debug("REST request to update ProviderProductPrice : {}, {}", id, providerProductPrice);
        if (providerProductPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, providerProductPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerProductPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProviderProductPrice result = providerProductPriceService.save(providerProductPrice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, providerProductPrice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /provider-product-prices/:id} : Partial updates given fields of an existing providerProductPrice, field will ignore if it is null
     *
     * @param id the id of the providerProductPrice to save.
     * @param providerProductPrice the providerProductPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated providerProductPrice,
     * or with status {@code 400 (Bad Request)} if the providerProductPrice is not valid,
     * or with status {@code 404 (Not Found)} if the providerProductPrice is not found,
     * or with status {@code 500 (Internal Server Error)} if the providerProductPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/provider-product-prices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProviderProductPrice> partialUpdateProviderProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProviderProductPrice providerProductPrice
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProviderProductPrice partially : {}, {}", id, providerProductPrice);
        if (providerProductPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, providerProductPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerProductPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProviderProductPrice> result = providerProductPriceService.partialUpdate(providerProductPrice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, providerProductPrice.getId().toString())
        );
    }

    /**
     * {@code GET  /provider-product-prices} : get all the providerProductPrices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of providerProductPrices in body.
     */
    @GetMapping("/provider-product-prices")
    public ResponseEntity<List<ProviderProductPrice>> getAllProviderProductPrices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProviderProductPrices");
        Page<ProviderProductPrice> page;
        if (eagerload) {
            page = providerProductPriceService.findAllWithEagerRelationships(pageable);
        } else {
            page = providerProductPriceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provider-product-prices/:id} : get the "id" providerProductPrice.
     *
     * @param id the id of the providerProductPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the providerProductPrice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provider-product-prices/{id}")
    public ResponseEntity<ProviderProductPrice> getProviderProductPrice(@PathVariable Long id) {
        log.debug("REST request to get ProviderProductPrice : {}", id);
        Optional<ProviderProductPrice> providerProductPrice = providerProductPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(providerProductPrice);
    }

    /**
     * {@code DELETE  /provider-product-prices/:id} : delete the "id" providerProductPrice.
     *
     * @param id the id of the providerProductPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provider-product-prices/{id}")
    public ResponseEntity<Void> deleteProviderProductPrice(@PathVariable Long id) {
        log.debug("REST request to delete ProviderProductPrice : {}", id);
        providerProductPriceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
