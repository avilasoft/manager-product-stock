package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.UnitType;
import com.avilasoft.managerproductstock.repository.UnitTypeRepository;
import com.avilasoft.managerproductstock.service.UnitTypeService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.UnitType}.
 */
@RestController
@RequestMapping("/api")
public class UnitTypeResource {

    private final Logger log = LoggerFactory.getLogger(UnitTypeResource.class);

    private static final String ENTITY_NAME = "unitType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitTypeService unitTypeService;

    private final UnitTypeRepository unitTypeRepository;

    public UnitTypeResource(UnitTypeService unitTypeService, UnitTypeRepository unitTypeRepository) {
        this.unitTypeService = unitTypeService;
        this.unitTypeRepository = unitTypeRepository;
    }

    /**
     * {@code POST  /unit-types} : Create a new unitType.
     *
     * @param unitType the unitType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitType, or with status {@code 400 (Bad Request)} if the unitType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unit-types")
    public ResponseEntity<UnitType> createUnitType(@Valid @RequestBody UnitType unitType) throws URISyntaxException {
        log.debug("REST request to save UnitType : {}", unitType);
        if (unitType.getId() != null) {
            throw new BadRequestAlertException("A new unitType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitType result = unitTypeService.save(unitType);
        return ResponseEntity
            .created(new URI("/api/unit-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unit-types/:id} : Updates an existing unitType.
     *
     * @param id the id of the unitType to save.
     * @param unitType the unitType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitType,
     * or with status {@code 400 (Bad Request)} if the unitType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unit-types/{id}")
    public ResponseEntity<UnitType> updateUnitType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UnitType unitType
    ) throws URISyntaxException {
        log.debug("REST request to update UnitType : {}, {}", id, unitType);
        if (unitType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitType result = unitTypeService.save(unitType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unit-types/:id} : Partial updates given fields of an existing unitType, field will ignore if it is null
     *
     * @param id the id of the unitType to save.
     * @param unitType the unitType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitType,
     * or with status {@code 400 (Bad Request)} if the unitType is not valid,
     * or with status {@code 404 (Not Found)} if the unitType is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unit-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitType> partialUpdateUnitType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UnitType unitType
    ) throws URISyntaxException {
        log.debug("REST request to partial update UnitType partially : {}, {}", id, unitType);
        if (unitType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitType> result = unitTypeService.partialUpdate(unitType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitType.getId().toString())
        );
    }

    /**
     * {@code GET  /unit-types} : get all the unitTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unitTypes in body.
     */
    @GetMapping("/unit-types")
    public ResponseEntity<List<UnitType>> getAllUnitTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UnitTypes");
        Page<UnitType> page = unitTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unit-types/:id} : get the "id" unitType.
     *
     * @param id the id of the unitType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unit-types/{id}")
    public ResponseEntity<UnitType> getUnitType(@PathVariable Long id) {
        log.debug("REST request to get UnitType : {}", id);
        Optional<UnitType> unitType = unitTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitType);
    }

    /**
     * {@code DELETE  /unit-types/:id} : delete the "id" unitType.
     *
     * @param id the id of the unitType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unit-types/{id}")
    public ResponseEntity<Void> deleteUnitType(@PathVariable Long id) {
        log.debug("REST request to delete UnitType : {}", id);
        unitTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
