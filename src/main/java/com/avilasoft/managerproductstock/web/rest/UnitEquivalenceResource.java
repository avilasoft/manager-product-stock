package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.UnitEquivalence;
import com.avilasoft.managerproductstock.repository.UnitEquivalenceRepository;
import com.avilasoft.managerproductstock.service.UnitEquivalenceService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.UnitEquivalence}.
 */
@RestController
@RequestMapping("/api")
public class UnitEquivalenceResource {

    private final Logger log = LoggerFactory.getLogger(UnitEquivalenceResource.class);

    private static final String ENTITY_NAME = "unitEquivalence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitEquivalenceService unitEquivalenceService;

    private final UnitEquivalenceRepository unitEquivalenceRepository;

    public UnitEquivalenceResource(UnitEquivalenceService unitEquivalenceService, UnitEquivalenceRepository unitEquivalenceRepository) {
        this.unitEquivalenceService = unitEquivalenceService;
        this.unitEquivalenceRepository = unitEquivalenceRepository;
    }

    /**
     * {@code POST  /unit-equivalences} : Create a new unitEquivalence.
     *
     * @param unitEquivalence the unitEquivalence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitEquivalence, or with status {@code 400 (Bad Request)} if the unitEquivalence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-equivalences")
    public ResponseEntity<UnitEquivalence> createUnitEquivalence(@Valid @RequestBody UnitEquivalence unitEquivalence)
        throws URISyntaxException {
        log.debug("REST request to save UnitEquivalence : {}", unitEquivalence);
        if (unitEquivalence.getId() != null) {
            throw new BadRequestAlertException("A new unitEquivalence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitEquivalence result = unitEquivalenceService.save(unitEquivalence);
        return ResponseEntity
            .created(new URI("/api/unit-equivalences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-equivalences/:id} : Updates an existing unitEquivalence.
     *
     * @param id the id of the unitEquivalence to save.
     * @param unitEquivalence the unitEquivalence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitEquivalence,
     * or with status {@code 400 (Bad Request)} if the unitEquivalence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitEquivalence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-equivalences/{id}")
    public ResponseEntity<UnitEquivalence> updateUnitEquivalence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitEquivalence unitEquivalence
    ) throws URISyntaxException {
        log.debug("REST request to update UnitEquivalence : {}, {}", id, unitEquivalence);
        if (unitEquivalence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitEquivalence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitEquivalenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitEquivalence result = unitEquivalenceService.save(unitEquivalence);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitEquivalence.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-equivalences/:id} : Partial updates given fields of an existing unitEquivalence, field will ignore if it is null
     *
     * @param id the id of the unitEquivalence to save.
     * @param unitEquivalence the unitEquivalence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitEquivalence,
     * or with status {@code 400 (Bad Request)} if the unitEquivalence is not valid,
     * or with status {@code 404 (Not Found)} if the unitEquivalence is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitEquivalence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-equivalences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitEquivalence> partialUpdateUnitEquivalence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitEquivalence unitEquivalence
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitEquivalence partially : {}, {}", id, unitEquivalence);
        if (unitEquivalence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitEquivalence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitEquivalenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitEquivalence> result = unitEquivalenceService.partialUpdate(unitEquivalence);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitEquivalence.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-equivalences} : get all the unitEquivalences.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitEquivalences in body.
     */
    @GetMapping("/unit-equivalences")
    public ResponseEntity<List<UnitEquivalence>> getAllUnitEquivalences(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UnitEquivalences");
        Page<UnitEquivalence> page;
        if (eagerload) {
            page = unitEquivalenceService.findAllWithEagerRelationships(pageable);
        } else {
            page = unitEquivalenceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-equivalences/:id} : get the "id" unitEquivalence.
     *
     * @param id the id of the unitEquivalence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitEquivalence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-equivalences/{id}")
    public ResponseEntity<UnitEquivalence> getUnitEquivalence(@PathVariable Long id) {
        log.debug("REST request to get UnitEquivalence : {}", id);
        Optional<UnitEquivalence> unitEquivalence = unitEquivalenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitEquivalence);
    }

    /**
     * {@code DELETE  /unit-equivalences/:id} : delete the "id" unitEquivalence.
     *
     * @param id the id of the unitEquivalence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-equivalences/{id}")
    public ResponseEntity<Void> deleteUnitEquivalence(@PathVariable Long id) {
        log.debug("REST request to delete UnitEquivalence : {}", id);
        unitEquivalenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
