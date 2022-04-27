package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import com.avilasoft.managerproductstock.repository.BusinessAssociateRepository;
import com.avilasoft.managerproductstock.service.BusinessAssociateService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.BusinessAssociate}.
 */
@RestController
@RequestMapping("/api")
public class BusinessAssociateResource {

    private final Logger log = LoggerFactory.getLogger(BusinessAssociateResource.class);

    private static final String ENTITY_NAME = "businessAssociate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessAssociateService businessAssociateService;

    private final BusinessAssociateRepository businessAssociateRepository;

    public BusinessAssociateResource(
        BusinessAssociateService businessAssociateService,
        BusinessAssociateRepository businessAssociateRepository
    ) {
        this.businessAssociateService = businessAssociateService;
        this.businessAssociateRepository = businessAssociateRepository;
    }

    /**
     * {@code POST  /business-associates} : Create a new businessAssociate.
     *
     * @param businessAssociate the businessAssociate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessAssociate, or with status {@code 400 (Bad Request)} if the businessAssociate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-associates")
    public ResponseEntity<BusinessAssociate> createBusinessAssociate(@Valid @RequestBody BusinessAssociate businessAssociate)
        throws URISyntaxException {
        log.debug("REST request to save BusinessAssociate : {}", businessAssociate);
        if (businessAssociate.getId() != null) {
            throw new BadRequestAlertException("A new businessAssociate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessAssociate result = businessAssociateService.save(businessAssociate);
        return ResponseEntity
            .created(new URI("/api/business-associates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-associates/:id} : Updates an existing businessAssociate.
     *
     * @param id the id of the businessAssociate to save.
     * @param businessAssociate the businessAssociate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessAssociate,
     * or with status {@code 400 (Bad Request)} if the businessAssociate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessAssociate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-associates/{id}")
    public ResponseEntity<BusinessAssociate> updateBusinessAssociate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessAssociate businessAssociate
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessAssociate : {}, {}", id, businessAssociate);
        if (businessAssociate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessAssociate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessAssociateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessAssociate result = businessAssociateService.save(businessAssociate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessAssociate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-associates/:id} : Partial updates given fields of an existing businessAssociate, field will ignore if it is null
     *
     * @param id the id of the businessAssociate to save.
     * @param businessAssociate the businessAssociate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessAssociate,
     * or with status {@code 400 (Bad Request)} if the businessAssociate is not valid,
     * or with status {@code 404 (Not Found)} if the businessAssociate is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessAssociate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-associates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessAssociate> partialUpdateBusinessAssociate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessAssociate businessAssociate
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessAssociate partially : {}, {}", id, businessAssociate);
        if (businessAssociate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessAssociate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessAssociateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessAssociate> result = businessAssociateService.partialUpdate(businessAssociate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessAssociate.getId().toString())
        );
    }

    /**
     * {@code GET  /business-associates} : get all the businessAssociates.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessAssociates in body.
     */
    @GetMapping("/business-associates")
    public ResponseEntity<List<BusinessAssociate>> getAllBusinessAssociates(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of BusinessAssociates");
        Page<BusinessAssociate> page;
        if (eagerload) {
            page = businessAssociateService.findAllWithEagerRelationships(pageable);
        } else {
            page = businessAssociateService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-associates/:id} : get the "id" businessAssociate.
     *
     * @param id the id of the businessAssociate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessAssociate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-associates/{id}")
    public ResponseEntity<BusinessAssociate> getBusinessAssociate(@PathVariable Long id) {
        log.debug("REST request to get BusinessAssociate : {}", id);
        Optional<BusinessAssociate> businessAssociate = businessAssociateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessAssociate);
    }

    /**
     * {@code DELETE  /business-associates/:id} : delete the "id" businessAssociate.
     *
     * @param id the id of the businessAssociate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-associates/{id}")
    public ResponseEntity<Void> deleteBusinessAssociate(@PathVariable Long id) {
        log.debug("REST request to delete BusinessAssociate : {}", id);
        businessAssociateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
