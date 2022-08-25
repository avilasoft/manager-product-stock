package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.ProjectGroup;
import com.avilasoft.managerproductstock.repository.ProjectGroupRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProjectGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectGroupRepository projectGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectGroupMockMvc;

    private ProjectGroup projectGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectGroup createEntity(EntityManager em) {
        ProjectGroup projectGroup = new ProjectGroup().name(DEFAULT_NAME);
        return projectGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectGroup createUpdatedEntity(EntityManager em) {
        ProjectGroup projectGroup = new ProjectGroup().name(UPDATED_NAME);
        return projectGroup;
    }

    @BeforeEach
    public void initTest() {
        projectGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectGroup() throws Exception {
        int databaseSizeBeforeCreate = projectGroupRepository.findAll().size();
        // Create the ProjectGroup
        restProjectGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectGroup)))
            .andExpect(status().isCreated());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectGroup testProjectGroup = projectGroupList.get(projectGroupList.size() - 1);
        assertThat(testProjectGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createProjectGroupWithExistingId() throws Exception {
        // Create the ProjectGroup with an existing ID
        projectGroup.setId(1L);

        int databaseSizeBeforeCreate = projectGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProjectGroups() throws Exception {
        // Initialize the database
        projectGroupRepository.saveAndFlush(projectGroup);

        // Get all the projectGroupList
        restProjectGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProjectGroup() throws Exception {
        // Initialize the database
        projectGroupRepository.saveAndFlush(projectGroup);

        // Get the projectGroup
        restProjectGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, projectGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProjectGroup() throws Exception {
        // Get the projectGroup
        restProjectGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjectGroup() throws Exception {
        // Initialize the database
        projectGroupRepository.saveAndFlush(projectGroup);

        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();

        // Update the projectGroup
        ProjectGroup updatedProjectGroup = projectGroupRepository.findById(projectGroup.getId()).get();
        // Disconnect from session so that the updates on updatedProjectGroup are not directly saved in db
        em.detach(updatedProjectGroup);
        updatedProjectGroup.name(UPDATED_NAME);

        restProjectGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjectGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjectGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
        ProjectGroup testProjectGroup = projectGroupList.get(projectGroupList.size() - 1);
        assertThat(testProjectGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProjectGroup() throws Exception {
        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();
        projectGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectGroup() throws Exception {
        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();
        projectGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectGroup() throws Exception {
        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();
        projectGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectGroupWithPatch() throws Exception {
        // Initialize the database
        projectGroupRepository.saveAndFlush(projectGroup);

        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();

        // Update the projectGroup using partial update
        ProjectGroup partialUpdatedProjectGroup = new ProjectGroup();
        partialUpdatedProjectGroup.setId(projectGroup.getId());

        restProjectGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
        ProjectGroup testProjectGroup = projectGroupList.get(projectGroupList.size() - 1);
        assertThat(testProjectGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProjectGroupWithPatch() throws Exception {
        // Initialize the database
        projectGroupRepository.saveAndFlush(projectGroup);

        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();

        // Update the projectGroup using partial update
        ProjectGroup partialUpdatedProjectGroup = new ProjectGroup();
        partialUpdatedProjectGroup.setId(projectGroup.getId());

        partialUpdatedProjectGroup.name(UPDATED_NAME);

        restProjectGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
        ProjectGroup testProjectGroup = projectGroupList.get(projectGroupList.size() - 1);
        assertThat(testProjectGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProjectGroup() throws Exception {
        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();
        projectGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectGroup() throws Exception {
        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();
        projectGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectGroup() throws Exception {
        int databaseSizeBeforeUpdate = projectGroupRepository.findAll().size();
        projectGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projectGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectGroup in the database
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectGroup() throws Exception {
        // Initialize the database
        projectGroupRepository.saveAndFlush(projectGroup);

        int databaseSizeBeforeDelete = projectGroupRepository.findAll().size();

        // Delete the projectGroup
        restProjectGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectGroup> projectGroupList = projectGroupRepository.findAll();
        assertThat(projectGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
