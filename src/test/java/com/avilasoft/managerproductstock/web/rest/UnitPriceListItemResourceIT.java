package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.ProjectItemListItem;
import com.avilasoft.managerproductstock.domain.UnitPriceList;
import com.avilasoft.managerproductstock.domain.UnitPriceListItem;
import com.avilasoft.managerproductstock.repository.UnitPriceListItemRepository;
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
 * Integration tests for the {@link UnitPriceListItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitPriceListItemResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_UNIT_COST_TOTAL = 1F;
    private static final Float UPDATED_UNIT_COST_TOTAL = 2F;

    private static final String ENTITY_API_URL = "/api/unit-price-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitPriceListItemRepository unitPriceListItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitPriceListItemMockMvc;

    private UnitPriceListItem unitPriceListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitPriceListItem createEntity(EntityManager em) {
        UnitPriceListItem unitPriceListItem = new UnitPriceListItem()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .unitCostTotal(DEFAULT_UNIT_COST_TOTAL);
        // Add required entity
        ProjectItemListItem projectItemListItem;
        if (TestUtil.findAll(em, ProjectItemListItem.class).isEmpty()) {
            projectItemListItem = ProjectItemListItemResourceIT.createEntity(em);
            em.persist(projectItemListItem);
            em.flush();
        } else {
            projectItemListItem = TestUtil.findAll(em, ProjectItemListItem.class).get(0);
        }
        unitPriceListItem.setProjectItemListItem(projectItemListItem);
        // Add required entity
        UnitPriceList unitPriceList;
        if (TestUtil.findAll(em, UnitPriceList.class).isEmpty()) {
            unitPriceList = UnitPriceListResourceIT.createEntity(em);
            em.persist(unitPriceList);
            em.flush();
        } else {
            unitPriceList = TestUtil.findAll(em, UnitPriceList.class).get(0);
        }
        unitPriceListItem.setUnitPriceList(unitPriceList);
        return unitPriceListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitPriceListItem createUpdatedEntity(EntityManager em) {
        UnitPriceListItem unitPriceListItem = new UnitPriceListItem()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .unitCostTotal(UPDATED_UNIT_COST_TOTAL);
        // Add required entity
        ProjectItemListItem projectItemListItem;
        if (TestUtil.findAll(em, ProjectItemListItem.class).isEmpty()) {
            projectItemListItem = ProjectItemListItemResourceIT.createUpdatedEntity(em);
            em.persist(projectItemListItem);
            em.flush();
        } else {
            projectItemListItem = TestUtil.findAll(em, ProjectItemListItem.class).get(0);
        }
        unitPriceListItem.setProjectItemListItem(projectItemListItem);
        // Add required entity
        UnitPriceList unitPriceList;
        if (TestUtil.findAll(em, UnitPriceList.class).isEmpty()) {
            unitPriceList = UnitPriceListResourceIT.createUpdatedEntity(em);
            em.persist(unitPriceList);
            em.flush();
        } else {
            unitPriceList = TestUtil.findAll(em, UnitPriceList.class).get(0);
        }
        unitPriceListItem.setUnitPriceList(unitPriceList);
        return unitPriceListItem;
    }

    @BeforeEach
    public void initTest() {
        unitPriceListItem = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitPriceListItem() throws Exception {
        int databaseSizeBeforeCreate = unitPriceListItemRepository.findAll().size();
        // Create the UnitPriceListItem
        restUnitPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isCreated());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeCreate + 1);
        UnitPriceListItem testUnitPriceListItem = unitPriceListItemList.get(unitPriceListItemList.size() - 1);
        assertThat(testUnitPriceListItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUnitPriceListItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitPriceListItem.getUnitCostTotal()).isEqualTo(DEFAULT_UNIT_COST_TOTAL);
    }

    @Test
    @Transactional
    void createUnitPriceListItemWithExistingId() throws Exception {
        // Create the UnitPriceListItem with an existing ID
        unitPriceListItem.setId(1L);

        int databaseSizeBeforeCreate = unitPriceListItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitPriceListItemRepository.findAll().size();
        // set the field null
        unitPriceListItem.setCode(null);

        // Create the UnitPriceListItem, which fails.

        restUnitPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitCostTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitPriceListItemRepository.findAll().size();
        // set the field null
        unitPriceListItem.setUnitCostTotal(null);

        // Create the UnitPriceListItem, which fails.

        restUnitPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitPriceListItems() throws Exception {
        // Initialize the database
        unitPriceListItemRepository.saveAndFlush(unitPriceListItem);

        // Get all the unitPriceListItemList
        restUnitPriceListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitPriceListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unitCostTotal").value(hasItem(DEFAULT_UNIT_COST_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getUnitPriceListItem() throws Exception {
        // Initialize the database
        unitPriceListItemRepository.saveAndFlush(unitPriceListItem);

        // Get the unitPriceListItem
        restUnitPriceListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, unitPriceListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitPriceListItem.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unitCostTotal").value(DEFAULT_UNIT_COST_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingUnitPriceListItem() throws Exception {
        // Get the unitPriceListItem
        restUnitPriceListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUnitPriceListItem() throws Exception {
        // Initialize the database
        unitPriceListItemRepository.saveAndFlush(unitPriceListItem);

        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();

        // Update the unitPriceListItem
        UnitPriceListItem updatedUnitPriceListItem = unitPriceListItemRepository.findById(unitPriceListItem.getId()).get();
        // Disconnect from session so that the updates on updatedUnitPriceListItem are not directly saved in db
        em.detach(updatedUnitPriceListItem);
        updatedUnitPriceListItem.code(UPDATED_CODE).name(UPDATED_NAME).unitCostTotal(UPDATED_UNIT_COST_TOTAL);

        restUnitPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitPriceListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnitPriceListItem))
            )
            .andExpect(status().isOk());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
        UnitPriceListItem testUnitPriceListItem = unitPriceListItemList.get(unitPriceListItemList.size() - 1);
        assertThat(testUnitPriceListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUnitPriceListItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitPriceListItem.getUnitCostTotal()).isEqualTo(UPDATED_UNIT_COST_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingUnitPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();
        unitPriceListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitPriceListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();
        unitPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();
        unitPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitPriceListItemWithPatch() throws Exception {
        // Initialize the database
        unitPriceListItemRepository.saveAndFlush(unitPriceListItem);

        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();

        // Update the unitPriceListItem using partial update
        UnitPriceListItem partialUpdatedUnitPriceListItem = new UnitPriceListItem();
        partialUpdatedUnitPriceListItem.setId(unitPriceListItem.getId());

        partialUpdatedUnitPriceListItem.code(UPDATED_CODE).name(UPDATED_NAME);

        restUnitPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitPriceListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitPriceListItem))
            )
            .andExpect(status().isOk());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
        UnitPriceListItem testUnitPriceListItem = unitPriceListItemList.get(unitPriceListItemList.size() - 1);
        assertThat(testUnitPriceListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUnitPriceListItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitPriceListItem.getUnitCostTotal()).isEqualTo(DEFAULT_UNIT_COST_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateUnitPriceListItemWithPatch() throws Exception {
        // Initialize the database
        unitPriceListItemRepository.saveAndFlush(unitPriceListItem);

        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();

        // Update the unitPriceListItem using partial update
        UnitPriceListItem partialUpdatedUnitPriceListItem = new UnitPriceListItem();
        partialUpdatedUnitPriceListItem.setId(unitPriceListItem.getId());

        partialUpdatedUnitPriceListItem.code(UPDATED_CODE).name(UPDATED_NAME).unitCostTotal(UPDATED_UNIT_COST_TOTAL);

        restUnitPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitPriceListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitPriceListItem))
            )
            .andExpect(status().isOk());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
        UnitPriceListItem testUnitPriceListItem = unitPriceListItemList.get(unitPriceListItemList.size() - 1);
        assertThat(testUnitPriceListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUnitPriceListItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitPriceListItem.getUnitCostTotal()).isEqualTo(UPDATED_UNIT_COST_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingUnitPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();
        unitPriceListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitPriceListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();
        unitPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListItemRepository.findAll().size();
        unitPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitPriceListItem in the database
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitPriceListItem() throws Exception {
        // Initialize the database
        unitPriceListItemRepository.saveAndFlush(unitPriceListItem);

        int databaseSizeBeforeDelete = unitPriceListItemRepository.findAll().size();

        // Delete the unitPriceListItem
        restUnitPriceListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitPriceListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitPriceListItem> unitPriceListItemList = unitPriceListItemRepository.findAll();
        assertThat(unitPriceListItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
