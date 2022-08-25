package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ProjectGroup;
import com.avilasoft.managerproductstock.repository.ProjectGroupRepository;
import com.avilasoft.managerproductstock.service.ProjectGroupService;
import com.avilasoft.managerproductstock.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ProjectGroup}.
 */
@RestController
@RequestMapping("/api")
public class ProjectGroupResource {

    private final Logger log = LoggerFactory.getLogger(ProjectGroupResource.class);

    private static final String ENTITY_NAME = "projectGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectGroupService projectGroupService;

    private final ProjectGroupRepository projectGroupRepository;

    public ProjectGroupResource(ProjectGroupService projectGroupService, ProjectGroupRepository projectGroupRepository) {
        this.projectGroupService = projectGroupService;
        this.projectGroupRepository = projectGroupRepository;
    }

    /**
     * {@code POST  /project-groups} : Create a new projectGroup.
     *
     * @param projectGroup the projectGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectGroup, or with status {@code 400 (Bad Request)} if the projectGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-groups")
    public ResponseEntity<ProjectGroup> createProjectGroup(@RequestBody ProjectGroup projectGroup) throws URISyntaxException {
        log.debug("REST request to save ProjectGroup : {}", projectGroup);
        if (projectGroup.getId() != null) {
            throw new BadRequestAlertException("A new projectGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectGroup result = projectGroupService.save(projectGroup);
        return ResponseEntity
            .created(new URI("/api/project-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-groups/:id} : Updates an existing projectGroup.
     *
     * @param id the id of the projectGroup to save.
     * @param projectGroup the projectGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectGroup,
     * or with status {@code 400 (Bad Request)} if the projectGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-groups/{id}")
    public ResponseEntity<ProjectGroup> updateProjectGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProjectGroup projectGroup
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectGroup : {}, {}", id, projectGroup);
        if (projectGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectGroup result = projectGroupService.save(projectGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-groups/:id} : Partial updates given fields of an existing projectGroup, field will ignore if it is null
     *
     * @param id the id of the projectGroup to save.
     * @param projectGroup the projectGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectGroup,
     * or with status {@code 400 (Bad Request)} if the projectGroup is not valid,
     * or with status {@code 404 (Not Found)} if the projectGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/project-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectGroup> partialUpdateProjectGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProjectGroup projectGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectGroup partially : {}, {}", id, projectGroup);
        if (projectGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectGroup> result = projectGroupService.partialUpdate(projectGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /project-groups} : get all the projectGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectGroups in body.
     */
    @GetMapping("/project-groups")
    public ResponseEntity<List<ProjectGroup>> getAllProjectGroups(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProjectGroups");
        Page<ProjectGroup> page = projectGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-groups/:id} : get the "id" projectGroup.
     *
     * @param id the id of the projectGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-groups/{id}")
    public ResponseEntity<ProjectGroup> getProjectGroup(@PathVariable Long id) {
        log.debug("REST request to get ProjectGroup : {}", id);
        Optional<ProjectGroup> projectGroup = projectGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectGroup);
    }

    /**
     * {@code DELETE  /project-groups/:id} : delete the "id" projectGroup.
     *
     * @param id the id of the projectGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-groups/{id}")
    public ResponseEntity<Void> deleteProjectGroup(@PathVariable Long id) {
        log.debug("REST request to delete ProjectGroup : {}", id);
        projectGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
