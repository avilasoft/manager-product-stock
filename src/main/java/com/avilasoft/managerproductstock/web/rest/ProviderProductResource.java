package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ProviderProduct;
import com.avilasoft.managerproductstock.repository.ProviderProductRepository;
import com.avilasoft.managerproductstock.service.ProviderProductService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ProviderProduct}.
 */
@RestController
@RequestMapping("/api")
public class ProviderProductResource {

    private final Logger log = LoggerFactory.getLogger(ProviderProductResource.class);

    private static final String ENTITY_NAME = "providerProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviderProductService providerProductService;

    private final ProviderProductRepository providerProductRepository;

    public ProviderProductResource(ProviderProductService providerProductService, ProviderProductRepository providerProductRepository) {
        this.providerProductService = providerProductService;
        this.providerProductRepository = providerProductRepository;
    }

    /**
     * {@code POST  /provider-products} : Create a new providerProduct.
     *
     * @param providerProduct the providerProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new providerProduct, or with status {@code 400 (Bad Request)} if the providerProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provider-products")
    public ResponseEntity<ProviderProduct> createProviderProduct(@Valid @RequestBody ProviderProduct providerProduct)
        throws URISyntaxException {
        log.debug("REST request to save ProviderProduct : {}", providerProduct);
        if (providerProduct.getId() != null) {
            throw new BadRequestAlertException("A new providerProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProviderProduct result = providerProductService.save(providerProduct);
        return ResponseEntity
            .created(new URI("/api/provider-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provider-products/:id} : Updates an existing providerProduct.
     *
     * @param id the id of the providerProduct to save.
     * @param providerProduct the providerProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated providerProduct,
     * or with status {@code 400 (Bad Request)} if the providerProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the providerProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provider-products/{id}")
    public ResponseEntity<ProviderProduct> updateProviderProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProviderProduct providerProduct
    ) throws URISyntaxException {
        log.debug("REST request to update ProviderProduct : {}, {}", id, providerProduct);
        if (providerProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, providerProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProviderProduct result = providerProductService.save(providerProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, providerProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /provider-products/:id} : Partial updates given fields of an existing providerProduct, field will ignore if it is null
     *
     * @param id the id of the providerProduct to save.
     * @param providerProduct the providerProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated providerProduct,
     * or with status {@code 400 (Bad Request)} if the providerProduct is not valid,
     * or with status {@code 404 (Not Found)} if the providerProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the providerProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/provider-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProviderProduct> partialUpdateProviderProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProviderProduct providerProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProviderProduct partially : {}, {}", id, providerProduct);
        if (providerProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, providerProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProviderProduct> result = providerProductService.partialUpdate(providerProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, providerProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /provider-products} : get all the providerProducts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of providerProducts in body.
     */
    @GetMapping("/provider-products")
    public ResponseEntity<List<ProviderProduct>> getAllProviderProducts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("providerproductprice-is-null".equals(filter)) {
            log.debug("REST request to get all ProviderProducts where providerProductPrice is null");
            return new ResponseEntity<>(providerProductService.findAllWhereProviderProductPriceIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of ProviderProducts");
        Page<ProviderProduct> page;
        if (eagerload) {
            page = providerProductService.findAllWithEagerRelationships(pageable);
        } else {
            page = providerProductService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provider-products/:id} : get the "id" providerProduct.
     *
     * @param id the id of the providerProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the providerProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provider-products/{id}")
    public ResponseEntity<ProviderProduct> getProviderProduct(@PathVariable Long id) {
        log.debug("REST request to get ProviderProduct : {}", id);
        Optional<ProviderProduct> providerProduct = providerProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(providerProduct);
    }

    /**
     * {@code DELETE  /provider-products/:id} : delete the "id" providerProduct.
     *
     * @param id the id of the providerProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provider-products/{id}")
    public ResponseEntity<Void> deleteProviderProduct(@PathVariable Long id) {
        log.debug("REST request to delete ProviderProduct : {}", id);
        providerProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
