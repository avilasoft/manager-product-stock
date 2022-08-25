package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.ComputedPriceList;
import com.avilasoft.managerproductstock.domain.ComputedPriceListItem;
import com.avilasoft.managerproductstock.domain.UnitPriceList;
import com.avilasoft.managerproductstock.repository.ComputedPriceListItemRepository;
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
 * Integration tests for the {@link ComputedPriceListItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComputedPriceListItemResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_COMPUTED_PRICE_TOTAL = 1F;
    private static final Float UPDATED_COMPUTED_PRICE_TOTAL = 2F;

    private static final Float DEFAULT_COMPUTED_QUANTITY_TOTAL = 1F;
    private static final Float UPDATED_COMPUTED_QUANTITY_TOTAL = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/computed-price-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComputedPriceListItemRepository computedPriceListItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComputedPriceListItemMockMvc;

    private ComputedPriceListItem computedPriceListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComputedPriceListItem createEntity(EntityManager em) {
        ComputedPriceListItem computedPriceListItem = new ComputedPriceListItem()
            .code(DEFAULT_CODE)
            .computedPriceTotal(DEFAULT_COMPUTED_PRICE_TOTAL)
            .computedQuantityTotal(DEFAULT_COMPUTED_QUANTITY_TOTAL)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        UnitPriceList unitPriceList;
        if (TestUtil.findAll(em, UnitPriceList.class).isEmpty()) {
            unitPriceList = UnitPriceListResourceIT.createEntity(em);
            em.persist(unitPriceList);
            em.flush();
        } else {
            unitPriceList = TestUtil.findAll(em, UnitPriceList.class).get(0);
        }
        computedPriceListItem.setUnitPriceList(unitPriceList);
        // Add required entity
        ComputedPriceList computedPriceList;
        if (TestUtil.findAll(em, ComputedPriceList.class).isEmpty()) {
            computedPriceList = ComputedPriceListResourceIT.createEntity(em);
            em.persist(computedPriceList);
            em.flush();
        } else {
            computedPriceList = TestUtil.findAll(em, ComputedPriceList.class).get(0);
        }
        computedPriceListItem.setComputedPriceList(computedPriceList);
        return computedPriceListItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComputedPriceListItem createUpdatedEntity(EntityManager em) {
        ComputedPriceListItem computedPriceListItem = new ComputedPriceListItem()
            .code(UPDATED_CODE)
            .computedPriceTotal(UPDATED_COMPUTED_PRICE_TOTAL)
            .computedQuantityTotal(UPDATED_COMPUTED_QUANTITY_TOTAL)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        UnitPriceList unitPriceList;
        if (TestUtil.findAll(em, UnitPriceList.class).isEmpty()) {
            unitPriceList = UnitPriceListResourceIT.createUpdatedEntity(em);
            em.persist(unitPriceList);
            em.flush();
        } else {
            unitPriceList = TestUtil.findAll(em, UnitPriceList.class).get(0);
        }
        computedPriceListItem.setUnitPriceList(unitPriceList);
        // Add required entity
        ComputedPriceList computedPriceList;
        if (TestUtil.findAll(em, ComputedPriceList.class).isEmpty()) {
            computedPriceList = ComputedPriceListResourceIT.createUpdatedEntity(em);
            em.persist(computedPriceList);
            em.flush();
        } else {
            computedPriceList = TestUtil.findAll(em, ComputedPriceList.class).get(0);
        }
        computedPriceListItem.setComputedPriceList(computedPriceList);
        return computedPriceListItem;
    }

    @BeforeEach
    public void initTest() {
        computedPriceListItem = createEntity(em);
    }

    @Test
    @Transactional
    void createComputedPriceListItem() throws Exception {
        int databaseSizeBeforeCreate = computedPriceListItemRepository.findAll().size();
        // Create the ComputedPriceListItem
        restComputedPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isCreated());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeCreate + 1);
        ComputedPriceListItem testComputedPriceListItem = computedPriceListItemList.get(computedPriceListItemList.size() - 1);
        assertThat(testComputedPriceListItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testComputedPriceListItem.getComputedPriceTotal()).isEqualTo(DEFAULT_COMPUTED_PRICE_TOTAL);
        assertThat(testComputedPriceListItem.getComputedQuantityTotal()).isEqualTo(DEFAULT_COMPUTED_QUANTITY_TOTAL);
        assertThat(testComputedPriceListItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createComputedPriceListItemWithExistingId() throws Exception {
        // Create the ComputedPriceListItem with an existing ID
        computedPriceListItem.setId(1L);

        int databaseSizeBeforeCreate = computedPriceListItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComputedPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = computedPriceListItemRepository.findAll().size();
        // set the field null
        computedPriceListItem.setCode(null);

        // Create the ComputedPriceListItem, which fails.

        restComputedPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComputedPriceTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = computedPriceListItemRepository.findAll().size();
        // set the field null
        computedPriceListItem.setComputedPriceTotal(null);

        // Create the ComputedPriceListItem, which fails.

        restComputedPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComputedQuantityTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = computedPriceListItemRepository.findAll().size();
        // set the field null
        computedPriceListItem.setComputedQuantityTotal(null);

        // Create the ComputedPriceListItem, which fails.

        restComputedPriceListItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComputedPriceListItems() throws Exception {
        // Initialize the database
        computedPriceListItemRepository.saveAndFlush(computedPriceListItem);

        // Get all the computedPriceListItemList
        restComputedPriceListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(computedPriceListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].computedPriceTotal").value(hasItem(DEFAULT_COMPUTED_PRICE_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].computedQuantityTotal").value(hasItem(DEFAULT_COMPUTED_QUANTITY_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getComputedPriceListItem() throws Exception {
        // Initialize the database
        computedPriceListItemRepository.saveAndFlush(computedPriceListItem);

        // Get the computedPriceListItem
        restComputedPriceListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, computedPriceListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(computedPriceListItem.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.computedPriceTotal").value(DEFAULT_COMPUTED_PRICE_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.computedQuantityTotal").value(DEFAULT_COMPUTED_QUANTITY_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingComputedPriceListItem() throws Exception {
        // Get the computedPriceListItem
        restComputedPriceListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComputedPriceListItem() throws Exception {
        // Initialize the database
        computedPriceListItemRepository.saveAndFlush(computedPriceListItem);

        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();

        // Update the computedPriceListItem
        ComputedPriceListItem updatedComputedPriceListItem = computedPriceListItemRepository.findById(computedPriceListItem.getId()).get();
        // Disconnect from session so that the updates on updatedComputedPriceListItem are not directly saved in db
        em.detach(updatedComputedPriceListItem);
        updatedComputedPriceListItem
            .code(UPDATED_CODE)
            .computedPriceTotal(UPDATED_COMPUTED_PRICE_TOTAL)
            .computedQuantityTotal(UPDATED_COMPUTED_QUANTITY_TOTAL)
            .description(UPDATED_DESCRIPTION);

        restComputedPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComputedPriceListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComputedPriceListItem))
            )
            .andExpect(status().isOk());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
        ComputedPriceListItem testComputedPriceListItem = computedPriceListItemList.get(computedPriceListItemList.size() - 1);
        assertThat(testComputedPriceListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testComputedPriceListItem.getComputedPriceTotal()).isEqualTo(UPDATED_COMPUTED_PRICE_TOTAL);
        assertThat(testComputedPriceListItem.getComputedQuantityTotal()).isEqualTo(UPDATED_COMPUTED_QUANTITY_TOTAL);
        assertThat(testComputedPriceListItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingComputedPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();
        computedPriceListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputedPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, computedPriceListItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComputedPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();
        computedPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComputedPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();
        computedPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComputedPriceListItemWithPatch() throws Exception {
        // Initialize the database
        computedPriceListItemRepository.saveAndFlush(computedPriceListItem);

        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();

        // Update the computedPriceListItem using partial update
        ComputedPriceListItem partialUpdatedComputedPriceListItem = new ComputedPriceListItem();
        partialUpdatedComputedPriceListItem.setId(computedPriceListItem.getId());

        partialUpdatedComputedPriceListItem
            .code(UPDATED_CODE)
            .computedPriceTotal(UPDATED_COMPUTED_PRICE_TOTAL)
            .description(UPDATED_DESCRIPTION);

        restComputedPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputedPriceListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputedPriceListItem))
            )
            .andExpect(status().isOk());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
        ComputedPriceListItem testComputedPriceListItem = computedPriceListItemList.get(computedPriceListItemList.size() - 1);
        assertThat(testComputedPriceListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testComputedPriceListItem.getComputedPriceTotal()).isEqualTo(UPDATED_COMPUTED_PRICE_TOTAL);
        assertThat(testComputedPriceListItem.getComputedQuantityTotal()).isEqualTo(DEFAULT_COMPUTED_QUANTITY_TOTAL);
        assertThat(testComputedPriceListItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateComputedPriceListItemWithPatch() throws Exception {
        // Initialize the database
        computedPriceListItemRepository.saveAndFlush(computedPriceListItem);

        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();

        // Update the computedPriceListItem using partial update
        ComputedPriceListItem partialUpdatedComputedPriceListItem = new ComputedPriceListItem();
        partialUpdatedComputedPriceListItem.setId(computedPriceListItem.getId());

        partialUpdatedComputedPriceListItem
            .code(UPDATED_CODE)
            .computedPriceTotal(UPDATED_COMPUTED_PRICE_TOTAL)
            .computedQuantityTotal(UPDATED_COMPUTED_QUANTITY_TOTAL)
            .description(UPDATED_DESCRIPTION);

        restComputedPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputedPriceListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputedPriceListItem))
            )
            .andExpect(status().isOk());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
        ComputedPriceListItem testComputedPriceListItem = computedPriceListItemList.get(computedPriceListItemList.size() - 1);
        assertThat(testComputedPriceListItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testComputedPriceListItem.getComputedPriceTotal()).isEqualTo(UPDATED_COMPUTED_PRICE_TOTAL);
        assertThat(testComputedPriceListItem.getComputedQuantityTotal()).isEqualTo(UPDATED_COMPUTED_QUANTITY_TOTAL);
        assertThat(testComputedPriceListItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingComputedPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();
        computedPriceListItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputedPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, computedPriceListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComputedPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();
        computedPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComputedPriceListItem() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListItemRepository.findAll().size();
        computedPriceListItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceListItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComputedPriceListItem in the database
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComputedPriceListItem() throws Exception {
        // Initialize the database
        computedPriceListItemRepository.saveAndFlush(computedPriceListItem);

        int databaseSizeBeforeDelete = computedPriceListItemRepository.findAll().size();

        // Delete the computedPriceListItem
        restComputedPriceListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, computedPriceListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComputedPriceListItem> computedPriceListItemList = computedPriceListItemRepository.findAll();
        assertThat(computedPriceListItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
