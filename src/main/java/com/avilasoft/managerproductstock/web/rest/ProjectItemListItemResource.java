package com.avilasoft.managerproductstock.web.rest;

import com.avilasoft.managerproductstock.domain.ProjectItemListItem;
import com.avilasoft.managerproductstock.repository.ProjectItemListItemRepository;
import com.avilasoft.managerproductstock.service.ProjectItemListItemService;
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
 * REST controller for managing {@link com.avilasoft.managerproductstock.domain.ProjectItemListItem}.
 */
@RestController
@RequestMapping("/api")
public class ProjectItemListItemResource {

    private final Logger log = LoggerFactory.getLogger(ProjectItemListItemResource.class);

    private static final String ENTITY_NAME = "projectItemListItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectItemListItemService projectItemListItemService;

    private final ProjectItemListItemRepository projectItemListItemRepository;

    public ProjectItemListItemResource(
        ProjectItemListItemService projectItemListItemService,
        ProjectItemListItemRepository projectItemListItemRepository
    ) {
        this.projectItemListItemService = projectItemListItemService;
        this.projectItemListItemRepository = projectItemListItemRepository;
    }

    /**
     * {@code POST  /project-item-list-items} : Create a new projectItemListItem.
     *
     * @param projectItemListItem the projectItemListItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectItemListItem, or with status {@code 400 (Bad Request)} if the projectItemListItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-item-list-items")
    public ResponseEntity<ProjectItemListItem> createProjectItemListItem(@Valid @RequestBody ProjectItemListItem projectItemListItem)
        throws URISyntaxException {
        log.debug("REST request to save ProjectItemListItem : {}", projectItemListItem);
        if (projectItemListItem.getId() != null) {
            throw new BadRequestAlertException("A new projectItemListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectItemListItem result = projectItemListItemService.save(projectItemListItem);
        return ResponseEntity
            .created(new URI("/api/project-item-list-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-item-list-items/:id} : Updates an existing projectItemListItem.
     *
     * @param id the id of the projectItemListItem to save.
     * @param projectItemListItem the projectItemListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectItemListItem,
     * or with status {@code 400 (Bad Request)} if the projectItemListItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectItemListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-item-list-items/{id}")
    public ResponseEntity<ProjectItemListItem> updateProjectItemListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectItemListItem projectItemListItem
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectItemListItem : {}, {}", id, projectItemListItem);
        if (projectItemListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectItemListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectItemListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectItemListItem result = projectItemListItemService.save(projectItemListItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectItemListItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-item-list-items/:id} : Partial updates given fields of an existing projectItemListItem, field will ignore if it is null
     *
     * @param id the id of the projectItemListItem to save.
     * @param projectItemListItem the projectItemListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectItemListItem,
     * or with status {@code 400 (Bad Request)} if the projectItemListItem is not valid,
     * or with status {@code 404 (Not Found)} if the projectItemListItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectItemListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/project-item-list-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectItemListItem> partialUpdateProjectItemListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectItemListItem projectItemListItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectItemListItem partially : {}, {}", id, projectItemListItem);
        if (projectItemListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectItemListItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectItemListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectItemListItem> result = projectItemListItemService.partialUpdate(projectItemListItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectItemListItem.getId().toString())
        );
    }

    /**
     * {@code GET  /project-item-list-items} : get all the projectItemListItems.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectItemListItems in body.
     */
    @GetMapping("/project-item-list-items")
    public ResponseEntity<List<ProjectItemListItem>> getAllProjectItemListItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProjectItemListItems");
        Page<ProjectItemListItem> page;
        if (eagerload) {
            page = projectItemListItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = projectItemListItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-item-list-items/:id} : get the "id" projectItemListItem.
     *
     * @param id the id of the projectItemListItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectItemListItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-item-list-items/{id}")
    public ResponseEntity<ProjectItemListItem> getProjectItemListItem(@PathVariable Long id) {
        log.debug("REST request to get ProjectItemListItem : {}", id);
        Optional<ProjectItemListItem> projectItemListItem = projectItemListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectItemListItem);
    }

    /**
     * {@code DELETE  /project-item-list-items/:id} : delete the "id" projectItemListItem.
     *
     * @param id the id of the projectItemListItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-item-list-items/{id}")
    public ResponseEntity<Void> deleteProjectItemListItem(@PathVariable Long id) {
        log.debug("REST request to delete ProjectItemListItem : {}", id);
        projectItemListItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
