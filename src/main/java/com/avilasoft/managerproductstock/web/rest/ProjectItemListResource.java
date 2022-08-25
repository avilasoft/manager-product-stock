package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ProjectItemList;
import com.avilasoft.managerproductstock.repository.ProjectItemListRepository;
import com.avilasoft.managerproductstock.service.ProjectItemListService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ProjectItemList}.
 */
@RestController
@RequestMapping("/api")
public class ProjectItemListResource {

    private final Logger log = LoggerFactory.getLogger(ProjectItemListResource.class);

    private static final String ENTITY_NAME = "projectItemList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectItemListService projectItemListService;

    private final ProjectItemListRepository projectItemListRepository;

    public ProjectItemListResource(ProjectItemListService projectItemListService, ProjectItemListRepository projectItemListRepository) {
        this.projectItemListService = projectItemListService;
        this.projectItemListRepository = projectItemListRepository;
    }

    /**
     * {@code POST  /project-item-lists} : Create a new projectItemList.
     *
     * @param projectItemList the projectItemList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectItemList, or with status {@code 400 (Bad Request)} if the projectItemList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-item-lists")
    public ResponseEntity<ProjectItemList> createProjectItemList(@Valid @RequestBody ProjectItemList projectItemList)
        throws URISyntaxException {
        log.debug("REST request to save ProjectItemList : {}", projectItemList);
        if (projectItemList.getId() != null) {
            throw new BadRequestAlertException("A new projectItemList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectItemList result = projectItemListService.save(projectItemList);
        return ResponseEntity
            .created(new URI("/api/project-item-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-item-lists/:id} : Updates an existing projectItemList.
     *
     * @param id the id of the projectItemList to save.
     * @param projectItemList the projectItemList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectItemList,
     * or with status {@code 400 (Bad Request)} if the projectItemList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectItemList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-item-lists/{id}")
    public ResponseEntity<ProjectItemList> updateProjectItemList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectItemList projectItemList
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectItemList : {}, {}", id, projectItemList);
        if (projectItemList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectItemList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectItemListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectItemList result = projectItemListService.save(projectItemList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectItemList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-item-lists/:id} : Partial updates given fields of an existing projectItemList, field will ignore if it is null
     *
     * @param id the id of the projectItemList to save.
     * @param projectItemList the projectItemList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectItemList,
     * or with status {@code 400 (Bad Request)} if the projectItemList is not valid,
     * or with status {@code 404 (Not Found)} if the projectItemList is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectItemList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/project-item-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectItemList> partialUpdateProjectItemList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectItemList projectItemList
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectItemList partially : {}, {}", id, projectItemList);
        if (projectItemList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectItemList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectItemListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectItemList> result = projectItemListService.partialUpdate(projectItemList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectItemList.getId().toString())
        );
    }

    /**
     * {@code GET  /project-item-lists} : get all the projectItemLists.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectItemLists in body.
     */
    @GetMapping("/project-item-lists")
    public ResponseEntity<List<ProjectItemList>> getAllProjectItemLists(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProjectItemLists");
        Page<ProjectItemList> page;
        if (eagerload) {
            page = projectItemListService.findAllWithEagerRelationships(pageable);
        } else {
            page = projectItemListService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-item-lists/:id} : get the "id" projectItemList.
     *
     * @param id the id of the projectItemList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectItemList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-item-lists/{id}")
    public ResponseEntity<ProjectItemList> getProjectItemList(@PathVariable Long id) {
        log.debug("REST request to get ProjectItemList : {}", id);
        Optional<ProjectItemList> projectItemList = projectItemListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectItemList);
    }

    /**
     * {@code DELETE  /project-item-lists/:id} : delete the "id" projectItemList.
     *
     * @param id the id of the projectItemList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-item-lists/{id}")
    public ResponseEntity<Void> deleteProjectItemList(@PathVariable Long id) {
        log.debug("REST request to delete ProjectItemList : {}", id);
        projectItemListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
