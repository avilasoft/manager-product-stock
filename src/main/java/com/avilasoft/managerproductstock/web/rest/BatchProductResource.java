package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.BatchProduct;
import com.avilasoft.managerproductstock.repository.BatchProductRepository;
import com.avilasoft.managerproductstock.service.BatchProductService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.BatchProduct}.
 */
@RestController
@RequestMapping("/api")
public class BatchProductResource {

    private final Logger log = LoggerFactory.getLogger(BatchProductResource.class);

    private static final String ENTITY_NAME = "batchProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BatchProductService batchProductService;

    private final BatchProductRepository batchProductRepository;

    public BatchProductResource(BatchProductService batchProductService, BatchProductRepository batchProductRepository) {
        this.batchProductService = batchProductService;
        this.batchProductRepository = batchProductRepository;
    }

    /**
     * {@code POST  /batch-products} : Create a new batchProduct.
     *
     * @param batchProduct the batchProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new batchProduct, or with status {@code 400 (Bad Request)} if the batchProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/batch-products")
    public ResponseEntity<BatchProduct> createBatchProduct(@Valid @RequestBody BatchProduct batchProduct) throws URISyntaxException {
        log.debug("REST request to save BatchProduct : {}", batchProduct);
        if (batchProduct.getId() != null) {
            throw new BadRequestAlertException("A new batchProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BatchProduct result = batchProductService.save(batchProduct);
        return ResponseEntity
            .created(new URI("/api/batch-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /batch-products/:id} : Updates an existing batchProduct.
     *
     * @param id the id of the batchProduct to save.
     * @param batchProduct the batchProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batchProduct,
     * or with status {@code 400 (Bad Request)} if the batchProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the batchProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/batch-products/{id}")
    public ResponseEntity<BatchProduct> updateBatchProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BatchProduct batchProduct
    ) throws URISyntaxException {
        log.debug("REST request to update BatchProduct : {}, {}", id, batchProduct);
        if (batchProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batchProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batchProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BatchProduct result = batchProductService.save(batchProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batchProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /batch-products/:id} : Partial updates given fields of an existing batchProduct, field will ignore if it is null
     *
     * @param id the id of the batchProduct to save.
     * @param batchProduct the batchProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated batchProduct,
     * or with status {@code 400 (Bad Request)} if the batchProduct is not valid,
     * or with status {@code 404 (Not Found)} if the batchProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the batchProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/batch-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BatchProduct> partialUpdateBatchProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BatchProduct batchProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update BatchProduct partially : {}, {}", id, batchProduct);
        if (batchProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, batchProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!batchProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BatchProduct> result = batchProductService.partialUpdate(batchProduct);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, batchProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /batch-products} : get all the batchProducts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of batchProducts in body.
     */
    @GetMapping("/batch-products")
    public ResponseEntity<List<BatchProduct>> getAllBatchProducts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of BatchProducts");
        Page<BatchProduct> page;
        if (eagerload) {
            page = batchProductService.findAllWithEagerRelationships(pageable);
        } else {
            page = batchProductService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /batch-products/:id} : get the "id" batchProduct.
     *
     * @param id the id of the batchProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the batchProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/batch-products/{id}")
    public ResponseEntity<BatchProduct> getBatchProduct(@PathVariable Long id) {
        log.debug("REST request to get BatchProduct : {}", id);
        Optional<BatchProduct> batchProduct = batchProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(batchProduct);
    }

    /**
     * {@code DELETE  /batch-products/:id} : delete the "id" batchProduct.
     *
     * @param id the id of the batchProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/batch-products/{id}")
    public ResponseEntity<Void> deleteBatchProduct(@PathVariable Long id) {
        log.debug("REST request to delete BatchProduct : {}", id);
        batchProductService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
