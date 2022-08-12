package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.UnitPriceList;
import com.avilasoft.managerproductstock.repository.UnitPriceListRepository;
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
 * Integration tests for the {@link UnitPriceListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitPriceListResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Float DEFAULT_UNIT_PRICE_LIST_TOTAL = 1F;
    private static final Float UPDATED_UNIT_PRICE_LIST_TOTAL = 2F;

    private static final String ENTITY_API_URL = "/api/unit-price-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitPriceListRepository unitPriceListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitPriceListMockMvc;

    private UnitPriceList unitPriceList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitPriceList createEntity(EntityManager em) {
        UnitPriceList unitPriceList = new UnitPriceList().code(DEFAULT_CODE).unitPriceListTotal(DEFAULT_UNIT_PRICE_LIST_TOTAL);
        return unitPriceList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitPriceList createUpdatedEntity(EntityManager em) {
        UnitPriceList unitPriceList = new UnitPriceList().code(UPDATED_CODE).unitPriceListTotal(UPDATED_UNIT_PRICE_LIST_TOTAL);
        return unitPriceList;
    }

    @BeforeEach
    public void initTest() {
        unitPriceList = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitPriceList() throws Exception {
        int databaseSizeBeforeCreate = unitPriceListRepository.findAll().size();
        // Create the UnitPriceList
        restUnitPriceListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceList)))
            .andExpect(status().isCreated());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeCreate + 1);
        UnitPriceList testUnitPriceList = unitPriceListList.get(unitPriceListList.size() - 1);
        assertThat(testUnitPriceList.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUnitPriceList.getUnitPriceListTotal()).isEqualTo(DEFAULT_UNIT_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void createUnitPriceListWithExistingId() throws Exception {
        // Create the UnitPriceList with an existing ID
        unitPriceList.setId(1L);

        int databaseSizeBeforeCreate = unitPriceListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitPriceListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceList)))
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitPriceListRepository.findAll().size();
        // set the field null
        unitPriceList.setCode(null);

        // Create the UnitPriceList, which fails.

        restUnitPriceListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceList)))
            .andExpect(status().isBadRequest());

        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitPriceLists() throws Exception {
        // Initialize the database
        unitPriceListRepository.saveAndFlush(unitPriceList);

        // Get all the unitPriceListList
        restUnitPriceListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitPriceList.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].unitPriceListTotal").value(hasItem(DEFAULT_UNIT_PRICE_LIST_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getUnitPriceList() throws Exception {
        // Initialize the database
        unitPriceListRepository.saveAndFlush(unitPriceList);

        // Get the unitPriceList
        restUnitPriceListMockMvc
            .perform(get(ENTITY_API_URL_ID, unitPriceList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitPriceList.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.unitPriceListTotal").value(DEFAULT_UNIT_PRICE_LIST_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingUnitPriceList() throws Exception {
        // Get the unitPriceList
        restUnitPriceListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUnitPriceList() throws Exception {
        // Initialize the database
        unitPriceListRepository.saveAndFlush(unitPriceList);

        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();

        // Update the unitPriceList
        UnitPriceList updatedUnitPriceList = unitPriceListRepository.findById(unitPriceList.getId()).get();
        // Disconnect from session so that the updates on updatedUnitPriceList are not directly saved in db
        em.detach(updatedUnitPriceList);
        updatedUnitPriceList.code(UPDATED_CODE).unitPriceListTotal(UPDATED_UNIT_PRICE_LIST_TOTAL);

        restUnitPriceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitPriceList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnitPriceList))
            )
            .andExpect(status().isOk());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
        UnitPriceList testUnitPriceList = unitPriceListList.get(unitPriceListList.size() - 1);
        assertThat(testUnitPriceList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUnitPriceList.getUnitPriceListTotal()).isEqualTo(UPDATED_UNIT_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingUnitPriceList() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();
        unitPriceList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitPriceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitPriceList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitPriceList() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();
        unitPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitPriceList() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();
        unitPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitPriceList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitPriceListWithPatch() throws Exception {
        // Initialize the database
        unitPriceListRepository.saveAndFlush(unitPriceList);

        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();

        // Update the unitPriceList using partial update
        UnitPriceList partialUpdatedUnitPriceList = new UnitPriceList();
        partialUpdatedUnitPriceList.setId(unitPriceList.getId());

        partialUpdatedUnitPriceList.code(UPDATED_CODE).unitPriceListTotal(UPDATED_UNIT_PRICE_LIST_TOTAL);

        restUnitPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitPriceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitPriceList))
            )
            .andExpect(status().isOk());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
        UnitPriceList testUnitPriceList = unitPriceListList.get(unitPriceListList.size() - 1);
        assertThat(testUnitPriceList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUnitPriceList.getUnitPriceListTotal()).isEqualTo(UPDATED_UNIT_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateUnitPriceListWithPatch() throws Exception {
        // Initialize the database
        unitPriceListRepository.saveAndFlush(unitPriceList);

        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();

        // Update the unitPriceList using partial update
        UnitPriceList partialUpdatedUnitPriceList = new UnitPriceList();
        partialUpdatedUnitPriceList.setId(unitPriceList.getId());

        partialUpdatedUnitPriceList.code(UPDATED_CODE).unitPriceListTotal(UPDATED_UNIT_PRICE_LIST_TOTAL);

        restUnitPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitPriceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitPriceList))
            )
            .andExpect(status().isOk());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
        UnitPriceList testUnitPriceList = unitPriceListList.get(unitPriceListList.size() - 1);
        assertThat(testUnitPriceList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUnitPriceList.getUnitPriceListTotal()).isEqualTo(UPDATED_UNIT_PRICE_LIST_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingUnitPriceList() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();
        unitPriceList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitPriceList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitPriceList() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();
        unitPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitPriceList))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitPriceList() throws Exception {
        int databaseSizeBeforeUpdate = unitPriceListRepository.findAll().size();
        unitPriceList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitPriceListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(unitPriceList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitPriceList in the database
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitPriceList() throws Exception {
        // Initialize the database
        unitPriceListRepository.saveAndFlush(unitPriceList);

        int databaseSizeBeforeDelete = unitPriceListRepository.findAll().size();

        // Delete the unitPriceList
        restUnitPriceListMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitPriceList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitPriceList> unitPriceListList = unitPriceListRepository.findAll();
        assertThat(unitPriceListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
