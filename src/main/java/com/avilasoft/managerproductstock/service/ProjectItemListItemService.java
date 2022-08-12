package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ProjectItemListItem;
import com.avilasoft.managerproductstock.repository.ProjectItemListItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProjectItemListItem}.
 */
@Service
@Transactional
public class ProjectItemListItemService {

    private final Logger log = LoggerFactory.getLogger(ProjectItemListItemService.class);

    private final ProjectItemListItemRepository projectItemListItemRepository;

    public ProjectItemListItemService(ProjectItemListItemRepository projectItemListItemRepository) {
        this.projectItemListItemRepository = projectItemListItemRepository;
    }

    /**
     * Save a projectItemListItem.
     *
     * @param projectItemListItem the entity to save.
     * @return the persisted entity.
     */
    public ProjectItemListItem save(ProjectItemListItem projectItemListItem) {
        log.debug("Request to save ProjectItemListItem : {}", projectItemListItem);
        return projectItemListItemRepository.save(projectItemListItem);
    }

    /**
     * Partially update a projectItemListItem.
     *
     * @param projectItemListItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectItemListItem> partialUpdate(ProjectItemListItem projectItemListItem) {
        log.debug("Request to partially update ProjectItemListItem : {}", projectItemListItem);

        return projectItemListItemRepository
            .findById(projectItemListItem.getId())
            .map(existingProjectItemListItem -> {
                if (projectItemListItem.getCode() != null) {
                    existingProjectItemListItem.setCode(projectItemListItem.getCode());
                }
                if (projectItemListItem.getName() != null) {
                    existingProjectItemListItem.setName(projectItemListItem.getName());
                }
                if (projectItemListItem.getDimension() != null) {
                    existingProjectItemListItem.setDimension(projectItemListItem.getDimension());
                }
                if (projectItemListItem.getQuantity() != null) {
                    existingProjectItemListItem.setQuantity(projectItemListItem.getQuantity());
                }

                return existingProjectItemListItem;
            })
            .map(projectItemListItemRepository::save);
    }

    /**
     * Get all the projectItemListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectItemListItem> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectItemListItems");
        return projectItemListItemRepository.findAll(pageable);
    }

    /**
     * Get all the projectItemListItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProjectItemListItem> findAllWithEagerRelationships(Pageable pageable) {
        return projectItemListItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one projectItemListItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectItemListItem> findOne(Long id) {
        log.debug("Request to get ProjectItemListItem : {}", id);
        return projectItemListItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the projectItemListItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectItemListItem : {}", id);
        projectItemListItemRepository.deleteById(id);
    }
}
