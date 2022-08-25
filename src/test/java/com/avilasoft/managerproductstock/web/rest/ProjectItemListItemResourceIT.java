package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Item;
import com.avilasoft.managerproductstock.domain.ProjectItemList;
import com.avilasoft.managerproductstock.domain.ProjectItemListItem;
import com.avilasoft.managerproductstock.domain.Unit;
import com.avilasoft.managerproductstock.repository.ProjectItemListItemRepository;
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
 * Integration tests for the {@link ProjectItemListItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectItemListItemResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DIMENSION = "AAAAAAAAAA";
    private static final String UPDATED_DIMENSION = "BBBBBBBBBB";

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-item-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectItemListItemRepository projectItemListItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectItemListItemMockMvc;

    private ProjectItemListItem projectItemListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectItemListItem createEntity(EntityManager em) {
        ProjectItemListItem projectItemListItem = new ProjectItemListItem()
            .code(DEFAULT_CODE)
            .dimension(DEFAULT_DIMENSION)
            .quantity(DEFAULT_QUANTITY)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        ProjectItemList projectItemList;
        if (TestUtil.findAll(em, ProjectItemList.class).isEmpty()) {
            projectItemList = ProjectItemListResourceIT.createEntity(em);
            em.persist(projectItemList);
            em.flush();
        } else {
            projectItemList = TestUtil.findAll(em, ProjectItemList.class).get(0);
        }
        projectItemListItem.setProjectItemList(projectItemList);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        projectItemListItem.setItem(item);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        projectItemListItem.setUnit(unit);
        return projectItemListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectItemListItem createUpdatedEntity(EntityManager em) {
        ProjectItemListItem projectItemListItem = new ProjectItemListItem()
            .code(UPDATED_CODE)
            .dimension(UPDATED_DIMENSION)
            .quantity(UPDATED_QUANTITY)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        ProjectItemList projectItemList;
        if (TestUtil.findAll(em, ProjectItemList.class).isEmpty()) {
            projectItemList = ProjectItemListResourceIT.createUpdatedEntity(em);
            em.persist(projectItemList);
            em.flush();
        } else {
            projectItemList = TestUtil.findAll(em, ProjectItemList.class).get(0);
        }
        projectItemListItem.setProjectItemList(projectItemList);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        projectItemListItem.setItem(item);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        projectItemListItem.setUnit(unit);
        return projectItemListItem;
    }

    @BeforeEach
    public void initTest() {
        projectItemListItem = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectItemListItem() throws Exception {
        int databaseSizeBeforeCreate = projectItemListItemRepository.findAll().size();
        // Create the ProjectItemListItem
        restProjectItemListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isCreated());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectItemListItem testProjectItemListItem = projectItemListItemList.get(projectItemListItemList.size() - 1);
        assertThat(testProjectItemListItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjectItemListItem.getDimension()).isEqualTo(DEFAULT_DIMENSION);
        assertThat(testProjectItemListItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProjectItemListItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProjectItemListItemWithExistingId() throws Exception {
        // Create the ProjectItemListItem with an existing ID
        projectItemListItem.setId(1L);

        int databaseSizeBeforeCreate = projectItemListItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectItemListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectItemListItemRepository.findAll().size();
        // set the field null
        projectItemListItem.setCode(null);

        // Create the ProjectItemListItem, which fails.

        restProjectItemListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDimensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectItemListItemRepository.findAll().size();
        // set the field null
        projectItemListItem.setDimension(null);

        // Create the ProjectItemListItem, which fails.

        restProjectItemListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectItemListItemRepository.findAll().size();
        // set the field null
        projectItemListItem.setQuantity(null);

        // Create the ProjectItemListItem, which fails.

        restProjectItemListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectItemListItems() throws Exception {
        // Initialize the database
        projectItemListItemRepository.saveAndFlush(projectItemListItem);

        // Get all the projectItemListItemList
        restProjectItemListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectItemListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dimension").value(hasItem(DEFAULT_DIMENSION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProjectItemListItem() throws Exception {
        // Initialize the database
        projectItemListItemRepository.saveAndFlush(projectItemListItem);

        // Get the projectItemListItem
        restProjectItemListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, projectItemListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectItemListItem.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.dimension").value(DEFAULT_DIMENSION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingProjectItemListItem() throws Exception {
        // Get the projectItemListItem
        restProjectItemListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjectItemListItem() throws Exception {
        // Initialize the database
        projectItemListItemRepository.saveAndFlush(projectItemListItem);

        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();

        // Update the projectItemListItem
        ProjectItemListItem updatedProjectItemListItem = projectItemListItemRepository.findById(projectItemListItem.getId()).get();
        // Disconnect from session so that the updates on updatedProjectItemListItem are not directly saved in db
        em.detach(updatedProjectItemListItem);
        updatedProjectItemListItem
            .code(UPDATED_CODE)
            .dimension(UPDATED_DIMENSION)
            .quantity(UPDATED_QUANTITY)
            .description(UPDATED_DESCRIPTION);

        restProjectItemListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjectItemListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjectItemListItem))
            )
            .andExpect(status().isOk());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
        ProjectItemListItem testProjectItemListItem = projectItemListItemList.get(projectItemListItemList.size() - 1);
        assertThat(testProjectItemListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjectItemListItem.getDimension()).isEqualTo(UPDATED_DIMENSION);
        assertThat(testProjectItemListItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProjectItemListItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProjectItemListItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();
        projectItemListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectItemListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectItemListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectItemListItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();
        projectItemListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectItemListItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();
        projectItemListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectItemListItemWithPatch() throws Exception {
        // Initialize the database
        projectItemListItemRepository.saveAndFlush(projectItemListItem);

        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();

        // Update the projectItemListItem using partial update
        ProjectItemListItem partialUpdatedProjectItemListItem = new ProjectItemListItem();
        partialUpdatedProjectItemListItem.setId(projectItemListItem.getId());

        partialUpdatedProjectItemListItem.dimension(UPDATED_DIMENSION);

        restProjectItemListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectItemListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectItemListItem))
            )
            .andExpect(status().isOk());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
        ProjectItemListItem testProjectItemListItem = projectItemListItemList.get(projectItemListItemList.size() - 1);
        assertThat(testProjectItemListItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjectItemListItem.getDimension()).isEqualTo(UPDATED_DIMENSION);
        assertThat(testProjectItemListItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProjectItemListItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProjectItemListItemWithPatch() throws Exception {
        // Initialize the database
        projectItemListItemRepository.saveAndFlush(projectItemListItem);

        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();

        // Update the projectItemListItem using partial update
        ProjectItemListItem partialUpdatedProjectItemListItem = new ProjectItemListItem();
        partialUpdatedProjectItemListItem.setId(projectItemListItem.getId());

        partialUpdatedProjectItemListItem
            .code(UPDATED_CODE)
            .dimension(UPDATED_DIMENSION)
            .quantity(UPDATED_QUANTITY)
            .description(UPDATED_DESCRIPTION);

        restProjectItemListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectItemListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectItemListItem))
            )
            .andExpect(status().isOk());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
        ProjectItemListItem testProjectItemListItem = projectItemListItemList.get(projectItemListItemList.size() - 1);
        assertThat(testProjectItemListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjectItemListItem.getDimension()).isEqualTo(UPDATED_DIMENSION);
        assertThat(testProjectItemListItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProjectItemListItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProjectItemListItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();
        projectItemListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectItemListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectItemListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectItemListItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();
        projectItemListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectItemListItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemListItemRepository.findAll().size();
        projectItemListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectItemListItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectItemListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectItemListItem in the database
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectItemListItem() throws Exception {
        // Initialize the database
        projectItemListItemRepository.saveAndFlush(projectItemListItem);

        int databaseSizeBeforeDelete = projectItemListItemRepository.findAll().size();

        // Delete the projectItemListItem
        restProjectItemListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectItemListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectItemListItem> projectItemListItemList = projectItemListItemRepository.findAll();
        assertThat(projectItemListItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
