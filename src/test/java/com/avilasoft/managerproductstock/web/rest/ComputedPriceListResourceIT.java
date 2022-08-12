package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.ComputedPriceList;
import com.avilasoft.managerproductstock.repository.ComputedPriceListRepository;
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
 * Integration tests for the {@link ComputedPriceListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComputedPriceListResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_COMPUTED_PRICE_LIST_TOTAL = 1F;
    private static final Float UPDATED_COMPUTED_PRICE_LIST_TOTAL = 2F;

    private static final String ENTITY_API_URL = "/api/computed-price-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComputedPriceListRepository computedPriceListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComputedPriceListMockMvc;

    private ComputedPriceList computedPriceList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComputedPriceList createEntity(EntityManager em) {
        ComputedPriceList computedPriceList = new ComputedPriceList()
            .code(DEFAULT_CODE)
            .computedPriceListTotal(DEFAULT_COMPUTED_PRICE_LIST_TOTAL);
        return computedPriceList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComputedPriceList createUpdatedEntity(EntityManager em) {
        ComputedPriceList computedPriceList = new ComputedPriceList()
            .code(UPDATED_CODE)
            .computedPriceListTotal(UPDATED_COMPUTED_PRICE_LIST_TOTAL);
        return computedPriceList;
    }

    @BeforeEach
    public void initTest() {
        computedPriceList = createEntity(em);
    }

    @Test
    @Transactional
    void createComputedPriceList() throws Exception {
        int databaseSizeBeforeCreate = computedPriceListRepository.findAll().size();
        // Create the ComputedPriceList
        restComputedPriceListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isCreated());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeCreate + 1);
        ComputedPriceList testComputedPriceList = computedPriceListList.get(computedPriceListList.size() - 1);
        assertThat(testComputedPriceList.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testComputedPriceList.getComputedPriceListTotal()).isEqualTo(DEFAULT_COMPUTED_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void createComputedPriceListWithExistingId() throws Exception {
        // Create the ComputedPriceList with an existing ID
        computedPriceList.setId(1L);

        int databaseSizeBeforeCreate = computedPriceListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComputedPriceListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = computedPriceListRepository.findAll().size();
        // set the field null
        computedPriceList.setCode(null);

        // Create the ComputedPriceList, which fails.

        restComputedPriceListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isBadRequest());

        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComputedPriceLists() throws Exception {
        // Initialize the database
        computedPriceListRepository.saveAndFlush(computedPriceList);

        // Get all the computedPriceListList
        restComputedPriceListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(computedPriceList.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].computedPriceListTotal").value(hasItem(DEFAULT_COMPUTED_PRICE_LIST_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getComputedPriceList() throws Exception {
        // Initialize the database
        computedPriceListRepository.saveAndFlush(computedPriceList);

        // Get the computedPriceList
        restComputedPriceListMockMvc
            .perform(get(ENTITY_API_URL_ID, computedPriceList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(computedPriceList.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.computedPriceListTotal").value(DEFAULT_COMPUTED_PRICE_LIST_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingComputedPriceList() throws Exception {
        // Get the computedPriceList
        restComputedPriceListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComputedPriceList() throws Exception {
        // Initialize the database
        computedPriceListRepository.saveAndFlush(computedPriceList);

        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();

        // Update the computedPriceList
        ComputedPriceList updatedComputedPriceList = computedPriceListRepository.findById(computedPriceList.getId()).get();
        // Disconnect from session so that the updates on updatedComputedPriceList are not directly saved in db
        em.detach(updatedComputedPriceList);
        updatedComputedPriceList.code(UPDATED_CODE).computedPriceListTotal(UPDATED_COMPUTED_PRICE_LIST_TOTAL);

        restComputedPriceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComputedPriceList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComputedPriceList))
            )
            .andExpect(status().isOk());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
        ComputedPriceList testComputedPriceList = computedPriceListList.get(computedPriceListList.size() - 1);
        assertThat(testComputedPriceList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testComputedPriceList.getComputedPriceListTotal()).isEqualTo(UPDATED_COMPUTED_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingComputedPriceList() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();
        computedPriceList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputedPriceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, computedPriceList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComputedPriceList() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();
        computedPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComputedPriceList() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();
        computedPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComputedPriceListWithPatch() throws Exception {
        // Initialize the database
        computedPriceListRepository.saveAndFlush(computedPriceList);

        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();

        // Update the computedPriceList using partial update
        ComputedPriceList partialUpdatedComputedPriceList = new ComputedPriceList();
        partialUpdatedComputedPriceList.setId(computedPriceList.getId());

        partialUpdatedComputedPriceList.code(UPDATED_CODE);

        restComputedPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputedPriceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputedPriceList))
            )
            .andExpect(status().isOk());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
        ComputedPriceList testComputedPriceList = computedPriceListList.get(computedPriceListList.size() - 1);
        assertThat(testComputedPriceList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testComputedPriceList.getComputedPriceListTotal()).isEqualTo(DEFAULT_COMPUTED_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateComputedPriceListWithPatch() throws Exception {
        // Initialize the database
        computedPriceListRepository.saveAndFlush(computedPriceList);

        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();

        // Update the computedPriceList using partial update
        ComputedPriceList partialUpdatedComputedPriceList = new ComputedPriceList();
        partialUpdatedComputedPriceList.setId(computedPriceList.getId());

        partialUpdatedComputedPriceList.code(UPDATED_CODE).computedPriceListTotal(UPDATED_COMPUTED_PRICE_LIST_TOTAL);

        restComputedPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputedPriceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputedPriceList))
            )
            .andExpect(status().isOk());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
        ComputedPriceList testComputedPriceList = computedPriceListList.get(computedPriceListList.size() - 1);
        assertThat(testComputedPriceList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testComputedPriceList.getComputedPriceListTotal()).isEqualTo(UPDATED_COMPUTED_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingComputedPriceList() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();
        computedPriceList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputedPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, computedPriceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComputedPriceList() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();
        computedPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComputedPriceList() throws Exception {
        int databaseSizeBeforeUpdate = computedPriceListRepository.findAll().size();
        computedPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputedPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computedPriceList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComputedPriceList in the database
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComputedPriceList() throws Exception {
        // Initialize the database
        computedPriceListRepository.saveAndFlush(computedPriceList);

        int databaseSizeBeforeDelete = computedPriceListRepository.findAll().size();

        // Delete the computedPriceList
        restComputedPriceListMockMvc
            .perform(delete(ENTITY_API_URL_ID, computedPriceList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComputedPriceList> computedPriceListList = computedPriceListRepository.findAll();
        assertThat(computedPriceListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
