package com.avilasoft.managerproductstock.service;

import com.avilasoft.managerproductstock.domain.ProjectGroup;
import com.avilasoft.managerproductstock.repository.ProjectGroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProjectGroup}.
 */
@Service
@Transactional
public class ProjectGroupService {

    private final Logger log = LoggerFactory.getLogger(ProjectGroupService.class);

    private final ProjectGroupRepository projectGroupRepository;

    public ProjectGroupService(ProjectGroupRepository projectGroupRepository) {
        this.projectGroupRepository = projectGroupRepository;
    }

    /**
     * Save a projectGroup.
     *
     * @param projectGroup the entity to save.
     * @return the persisted entity.
     */
    public ProjectGroup save(ProjectGroup projectGroup) {
        log.debug("Request to save ProjectGroup : {}", projectGroup);
        return projectGroupRepository.save(projectGroup);
    }

    /**
     * Partially update a projectGroup.
     *
     * @param projectGroup the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectGroup> partialUpdate(ProjectGroup projectGroup) {
        log.debug("Request to partially update ProjectGroup : {}", projectGroup);

        return projectGroupRepository
            .findById(projectGroup.getId())
            .map(existingProjectGroup -> {
                if (projectGroup.getName() != null) {
                    existingProjectGroup.setName(projectGroup.getName());
                }

                return existingProjectGroup;
            })
            .map(projectGroupRepository::save);
    }

    /**
     * Get all the projectGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectGroup> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectGroups");
        return projectGroupRepository.findAll(pageable);
    }

    /**
     * Get one projectGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectGroup> findOne(Long id) {
        log.debug("Request to get ProjectGroup : {}", id);
        return projectGroupRepository.findById(id);
    }

    /**
     * Delete the projectGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectGroup : {}", id);
        projectGroupRepository.deleteById(id);
    }
}
