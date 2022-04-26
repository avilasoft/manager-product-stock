package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Price;
import com.avilasoft.managerproductstock.domain.PriceHistory;
import com.avilasoft.managerproductstock.repository.PriceHistoryRepository;
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
 * Integration tests for the {@link PriceHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceHistoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/price-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceHistoryMockMvc;

    private PriceHistory priceHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceHistory createEntity(EntityManager em) {
        PriceHistory priceHistory = new PriceHistory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Price price;
        if (TestUtil.findAll(em, Price.class).isEmpty()) {
            price = PriceResourceIT.createEntity(em);
            em.persist(price);
            em.flush();
        } else {
            price = TestUtil.findAll(em, Price.class).get(0);
        }
        priceHistory.setPrice(price);
        return priceHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceHistory createUpdatedEntity(EntityManager em) {
        PriceHistory priceHistory = new PriceHistory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        Price price;
        if (TestUtil.findAll(em, Price.class).isEmpty()) {
            price = PriceResourceIT.createUpdatedEntity(em);
            em.persist(price);
            em.flush();
        } else {
            price = TestUtil.findAll(em, Price.class).get(0);
        }
        priceHistory.setPrice(price);
        return priceHistory;
    }

    @BeforeEach
    public void initTest() {
        priceHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createPriceHistory() throws Exception {
        int databaseSizeBeforeCreate = priceHistoryRepository.findAll().size();
        // Create the PriceHistory
        restPriceHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistory)))
            .andExpect(status().isCreated());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPriceHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPriceHistoryWithExistingId() throws Exception {
        // Create the PriceHistory with an existing ID
        priceHistory.setId(1L);

        int databaseSizeBeforeCreate = priceHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistory)))
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceHistoryRepository.findAll().size();
        // set the field null
        priceHistory.setName(null);

        // Create the PriceHistory, which fails.

        restPriceHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistory)))
            .andExpect(status().isBadRequest());

        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPriceHistories() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        // Get all the priceHistoryList
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        // Get the priceHistory
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, priceHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceHistory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPriceHistory() throws Exception {
        // Get the priceHistory
        restPriceHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory
        PriceHistory updatedPriceHistory = priceHistoryRepository.findById(priceHistory.getId()).get();
        // Disconnect from session so that the updates on updatedPriceHistory are not directly saved in db
        em.detach(updatedPriceHistory);
        updatedPriceHistory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPriceHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPriceHistory))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriceHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceHistoryWithPatch() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory using partial update
        PriceHistory partialUpdatedPriceHistory = new PriceHistory();
        partialUpdatedPriceHistory.setId(priceHistory.getId());

        partialUpdatedPriceHistory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceHistory))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriceHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePriceHistoryWithPatch() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory using partial update
        PriceHistory partialUpdatedPriceHistory = new PriceHistory();
        partialUpdatedPriceHistory.setId(priceHistory.getId());

        partialUpdatedPriceHistory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceHistory))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistory testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPriceHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(priceHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceHistory in the database
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistory);

        int databaseSizeBeforeDelete = priceHistoryRepository.findAll().size();

        // Delete the priceHistory
        restPriceHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
