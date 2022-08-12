package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Project;
import com.avilasoft.managerproductstock.domain.ProjectItemList;
import com.avilasoft.managerproductstock.domain.enumeration.ProjectItemListStatus;
import com.avilasoft.managerproductstock.domain.enumeration.ProjectItemListType;
import com.avilasoft.managerproductstock.repository.ProjectItemListRepository;
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
 * Integration tests for the {@link ProjectItemListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectItemListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ProjectItemListType DEFAULT_TYPE = ProjectItemListType.EXCAVACION;
    private static final ProjectItemListType UPDATED_TYPE = ProjectItemListType.DE;

    private static final ProjectItemListStatus DEFAULT_STATUS = ProjectItemListStatus.DRAFT;
    private static final ProjectItemListStatus UPDATED_STATUS = ProjectItemListStatus.COMPLETE;

    private static final String ENTITY_API_URL = "/api/project-item-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectItemListRepository projectItemListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectItemListMockMvc;

    private ProjectItemList projectItemList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectItemList createEntity(EntityManager em) {
        ProjectItemList projectItemList = new ProjectItemList()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        projectItemList.setProject(project);
        return projectItemList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectItemList createUpdatedEntity(EntityManager em) {
        ProjectItemList projectItemList = new ProjectItemList()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        projectItemList.setProject(project);
        return projectItemList;
    }

    @BeforeEach
    public void initTest() {
        projectItemList = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectItemList() throws Exception {
        int databaseSizeBeforeCreate = projectItemListRepository.findAll().size();
        // Create the ProjectItemList
        restProjectItemListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isCreated());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectItemList testProjectItemList = projectItemListList.get(projectItemListList.size() - 1);
        assertThat(testProjectItemList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectItemList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjectItemList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProjectItemList.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createProjectItemListWithExistingId() throws Exception {
        // Create the ProjectItemList with an existing ID
        projectItemList.setId(1L);

        int databaseSizeBeforeCreate = projectItemListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectItemListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectItemListRepository.findAll().size();
        // set the field null
        projectItemList.setName(null);

        // Create the ProjectItemList, which fails.

        restProjectItemListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectItemListRepository.findAll().size();
        // set the field null
        projectItemList.setType(null);

        // Create the ProjectItemList, which fails.

        restProjectItemListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectItemListRepository.findAll().size();
        // set the field null
        projectItemList.setStatus(null);

        // Create the ProjectItemList, which fails.

        restProjectItemListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectItemLists() throws Exception {
        // Initialize the database
        projectItemListRepository.saveAndFlush(projectItemList);

        // Get all the projectItemListList
        restProjectItemListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectItemList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getProjectItemList() throws Exception {
        // Initialize the database
        projectItemListRepository.saveAndFlush(projectItemList);

        // Get the projectItemList
        restProjectItemListMockMvc
            .perform(get(ENTITY_API_URL_ID, projectItemList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectItemList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProjectItemList() throws Exception {
        // Get the projectItemList
        restProjectItemListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjectItemList() throws Exception {
        // Initialize the database
        projectItemListRepository.saveAndFlush(projectItemList);

        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();

        // Update the projectItemList
        ProjectItemList updatedProjectItemList = projectItemListRepository.findById(projectItemList.getId()).get();
        // Disconnect from session so that the updates on updatedProjectItemList are not directly saved in db
        em.detach(updatedProjectItemList);
        updatedProjectItemList.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).status(UPDATED_STATUS);

        restProjectItemListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjectItemList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjectItemList))
            )
            .andExpect(status().isOk());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
        ProjectItemList testProjectItemList = projectItemListList.get(projectItemListList.size() - 1);
        assertThat(testProjectItemList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectItemList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjectItemList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjectItemList.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingProjectItemList() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();
        projectItemList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectItemListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectItemList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectItemList() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();
        projectItemList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectItemList() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();
        projectItemList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectItemListWithPatch() throws Exception {
        // Initialize the database
        projectItemListRepository.saveAndFlush(projectItemList);

        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();

        // Update the projectItemList using partial update
        ProjectItemList partialUpdatedProjectItemList = new ProjectItemList();
        partialUpdatedProjectItemList.setId(projectItemList.getId());

        partialUpdatedProjectItemList.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restProjectItemListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectItemList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectItemList))
            )
            .andExpect(status().isOk());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
        ProjectItemList testProjectItemList = projectItemListList.get(projectItemListList.size() - 1);
        assertThat(testProjectItemList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectItemList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjectItemList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProjectItemList.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateProjectItemListWithPatch() throws Exception {
        // Initialize the database
        projectItemListRepository.saveAndFlush(projectItemList);

        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();

        // Update the projectItemList using partial update
        ProjectItemList partialUpdatedProjectItemList = new ProjectItemList();
        partialUpdatedProjectItemList.setId(projectItemList.getId());

        partialUpdatedProjectItemList.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).status(UPDATED_STATUS);

        restProjectItemListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectItemList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectItemList))
            )
            .andExpect(status().isOk());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
        ProjectItemList testProjectItemList = projectItemListList.get(projectItemListList.size() - 1);
        assertThat(testProjectItemList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectItemList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjectItemList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjectItemList.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingProjectItemList() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();
        projectItemList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectItemListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectItemList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectItemList() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();
        projectItemList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectItemList() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListRepository.findAll().size();
        projectItemList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectItemList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectItemList in the database
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectItemList() throws Exception {
        // Initialize the database
        projectItemListRepository.saveAndFlush(projectItemList);

        int databaseSizeBeforeDelete = projectItemListRepository.findAll().size();

        // Delete the projectItemList
        restProjectItemListMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectItemList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectItemList> projectItemListList = projectItemListRepository.findAll();
        assertThat(projectItemListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
