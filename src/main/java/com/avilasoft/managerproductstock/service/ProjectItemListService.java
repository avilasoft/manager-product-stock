package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ProjectItemList;
import com.avilasoft.managerproductstock.repository.ProjectItemListRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProjectItemList}.
 */
@Service
@Transactional
public class ProjectItemListService {

    private final Logger log = LoggerFactory.getLogger(ProjectItemListService.class);

    private final ProjectItemListRepository projectItemListRepository;

    public ProjectItemListService(ProjectItemListRepository projectItemListRepository) {
        this.projectItemListRepository = projectItemListRepository;
    }

    /**
     * Save a projectItemList.
     *
     * @param projectItemList the entity to save.
     * @return the persisted entity.
     */
    public ProjectItemList save(ProjectItemList projectItemList) {
        log.debug("Request to save ProjectItemList : {}", projectItemList);
        return projectItemListRepository.save(projectItemList);
    }

    /**
     * Partially update a projectItemList.
     *
     * @param projectItemList the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectItemList> partialUpdate(ProjectItemList projectItemList) {
        log.debug("Request to partially update ProjectItemList : {}", projectItemList);

        return projectItemListRepository
            .findById(projectItemList.getId())
            .map(existingProjectItemList -> {
                if (projectItemList.getName() != null) {
                    existingProjectItemList.setName(projectItemList.getName());
                }
                if (projectItemList.getDescription() != null) {
                    existingProjectItemList.setDescription(projectItemList.getDescription());
                }
                if (projectItemList.getType() != null) {
                    existingProjectItemList.setType(projectItemList.getType());
                }
                if (projectItemList.getStatus() != null) {
                    existingProjectItemList.setStatus(projectItemList.getStatus());
                }

                return existingProjectItemList;
            })
            .map(projectItemListRepository::save);
    }

    /**
     * Get all the projectItemLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectItemList> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectItemLists");
        return projectItemListRepository.findAll(pageable);
    }

    /**
     * Get all the projectItemLists with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProjectItemList> findAllWithEagerRelationships(Pageable pageable) {
        return projectItemListRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one projectItemList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectItemList> findOne(Long id) {
        log.debug("Request to get ProjectItemList : {}", id);
        return projectItemListRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the projectItemList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectItemList : {}", id);
        projectItemListRepository.deleteById(id);
    }
}
