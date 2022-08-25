package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Item;
import com.avilasoft.managerproductstock.domain.Provider;
import com.avilasoft.managerproductstock.domain.ProviderItem;
import com.avilasoft.managerproductstock.domain.Unit;
import com.avilasoft.managerproductstock.repository.ProviderItemRepository;
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
 * Integration tests for the {@link ProviderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProviderItemResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provider-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProviderItemRepository providerItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProviderItemMockMvc;

    private ProviderItem providerItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProviderItem createEntity(EntityManager em) {
        ProviderItem providerItem = new ProviderItem().code(DEFAULT_CODE).cost(DEFAULT_COST).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        providerItem.setProvider(provider);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        providerItem.setUnit(unit);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        providerItem.setItem(item);
        return providerItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProviderItem createUpdatedEntity(EntityManager em) {
        ProviderItem providerItem = new ProviderItem().code(UPDATED_CODE).cost(UPDATED_COST).description(UPDATED_DESCRIPTION);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createUpdatedEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        providerItem.setProvider(provider);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        providerItem.setUnit(unit);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        providerItem.setItem(item);
        return providerItem;
    }

    @BeforeEach
    public void initTest() {
        providerItem = createEntity(em);
    }

    @Test
    @Transactional
    void createProviderItem() throws Exception {
        int databaseSizeBeforeCreate = providerItemRepository.findAll().size();
        // Create the ProviderItem
        restProviderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerItem)))
            .andExpect(status().isCreated());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeCreate + 1);
        ProviderItem testProviderItem = providerItemList.get(providerItemList.size() - 1);
        assertThat(testProviderItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProviderItem.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testProviderItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProviderItemWithExistingId() throws Exception {
        // Create the ProviderItem with an existing ID
        providerItem.setId(1L);

        int databaseSizeBeforeCreate = providerItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProviderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerItem)))
            .andExpect(status().isBadRequest());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerItemRepository.findAll().size();
        // set the field null
        providerItem.setCode(null);

        // Create the ProviderItem, which fails.

        restProviderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerItem)))
            .andExpect(status().isBadRequest());

        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerItemRepository.findAll().size();
        // set the field null
        providerItem.setCost(null);

        // Create the ProviderItem, which fails.

        restProviderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerItem)))
            .andExpect(status().isBadRequest());

        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProviderItems() throws Exception {
        // Initialize the database
        providerItemRepository.saveAndFlush(providerItem);

        // Get all the providerItemList
        restProviderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(providerItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProviderItem() throws Exception {
        // Initialize the database
        providerItemRepository.saveAndFlush(providerItem);

        // Get the providerItem
        restProviderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, providerItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(providerItem.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingProviderItem() throws Exception {
        // Get the providerItem
        restProviderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProviderItem() throws Exception {
        // Initialize the database
        providerItemRepository.saveAndFlush(providerItem);

        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();

        // Update the providerItem
        ProviderItem updatedProviderItem = providerItemRepository.findById(providerItem.getId()).get();
        // Disconnect from session so that the updates on updatedProviderItem are not directly saved in db
        em.detach(updatedProviderItem);
        updatedProviderItem.code(UPDATED_CODE).cost(UPDATED_COST).description(UPDATED_DESCRIPTION);

        restProviderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProviderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProviderItem))
            )
            .andExpect(status().isOk());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
        ProviderItem testProviderItem = providerItemList.get(providerItemList.size() - 1);
        assertThat(testProviderItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProviderItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testProviderItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProviderItem() throws Exception {
        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();
        providerItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, providerItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProviderItem() throws Exception {
        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();
        providerItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProviderItem() throws Exception {
        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();
        providerItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProviderItemWithPatch() throws Exception {
        // Initialize the database
        providerItemRepository.saveAndFlush(providerItem);

        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();

        // Update the providerItem using partial update
        ProviderItem partialUpdatedProviderItem = new ProviderItem();
        partialUpdatedProviderItem.setId(providerItem.getId());

        partialUpdatedProviderItem.code(UPDATED_CODE).cost(UPDATED_COST);

        restProviderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProviderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProviderItem))
            )
            .andExpect(status().isOk());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
        ProviderItem testProviderItem = providerItemList.get(providerItemList.size() - 1);
        assertThat(testProviderItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProviderItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testProviderItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProviderItemWithPatch() throws Exception {
        // Initialize the database
        providerItemRepository.saveAndFlush(providerItem);

        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();

        // Update the providerItem using partial update
        ProviderItem partialUpdatedProviderItem = new ProviderItem();
        partialUpdatedProviderItem.setId(providerItem.getId());

        partialUpdatedProviderItem.code(UPDATED_CODE).cost(UPDATED_COST).description(UPDATED_DESCRIPTION);

        restProviderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProviderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProviderItem))
            )
            .andExpect(status().isOk());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
        ProviderItem testProviderItem = providerItemList.get(providerItemList.size() - 1);
        assertThat(testProviderItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProviderItem.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testProviderItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProviderItem() throws Exception {
        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();
        providerItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, providerItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProviderItem() throws Exception {
        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();
        providerItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProviderItem() throws Exception {
        int databaseSizeBeforeUpdate = providerItemRepository.findAll().size();
        providerItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(providerItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProviderItem in the database
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProviderItem() throws Exception {
        // Initialize the database
        providerItemRepository.saveAndFlush(providerItem);

        int databaseSizeBeforeDelete = providerItemRepository.findAll().size();

        // Delete the providerItem
        restProviderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, providerItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProviderItem> providerItemList = providerItemRepository.findAll();
        assertThat(providerItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
