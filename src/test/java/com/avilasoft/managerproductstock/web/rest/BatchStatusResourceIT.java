package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.BatchStatus;
import com.avilasoft.managerproductstock.repository.BatchStatusRepository;
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
 * Integration tests for the {@link BatchStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BatchStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/batch-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatchStatusRepository batchStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatchStatusMockMvc;

    private BatchStatus batchStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BatchStatus createEntity(EntityManager em) {
        BatchStatus batchStatus = new BatchStatus().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return batchStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BatchStatus createUpdatedEntity(EntityManager em) {
        BatchStatus batchStatus = new BatchStatus().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return batchStatus;
    }

    @BeforeEach
    public void initTest() {
        batchStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createBatchStatus() throws Exception {
        int databaseSizeBeforeCreate = batchStatusRepository.findAll().size();
        // Create the BatchStatus
        restBatchStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchStatus)))
            .andExpect(status().isCreated());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeCreate + 1);
        BatchStatus testBatchStatus = batchStatusList.get(batchStatusList.size() - 1);
        assertThat(testBatchStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatchStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBatchStatusWithExistingId() throws Exception {
        // Create the BatchStatus with an existing ID
        batchStatus.setId(1L);

        int databaseSizeBeforeCreate = batchStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchStatus)))
            .andExpect(status().isBadRequest());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = batchStatusRepository.findAll().size();
        // set the field null
        batchStatus.setName(null);

        // Create the BatchStatus, which fails.

        restBatchStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchStatus)))
            .andExpect(status().isBadRequest());

        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatchStatuses() throws Exception {
        // Initialize the database
        batchStatusRepository.saveAndFlush(batchStatus);

        // Get all the batchStatusList
        restBatchStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batchStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBatchStatus() throws Exception {
        // Initialize the database
        batchStatusRepository.saveAndFlush(batchStatus);

        // Get the batchStatus
        restBatchStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, batchStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batchStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBatchStatus() throws Exception {
        // Get the batchStatus
        restBatchStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBatchStatus() throws Exception {
        // Initialize the database
        batchStatusRepository.saveAndFlush(batchStatus);

        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();

        // Update the batchStatus
        BatchStatus updatedBatchStatus = batchStatusRepository.findById(batchStatus.getId()).get();
        // Disconnect from session so that the updates on updatedBatchStatus are not directly saved in db
        em.detach(updatedBatchStatus);
        updatedBatchStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBatchStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatchStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBatchStatus))
            )
            .andExpect(status().isOk());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
        BatchStatus testBatchStatus = batchStatusList.get(batchStatusList.size() - 1);
        assertThat(testBatchStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatchStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();
        batchStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batchStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batchStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();
        batchStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batchStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();
        batchStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatchStatusWithPatch() throws Exception {
        // Initialize the database
        batchStatusRepository.saveAndFlush(batchStatus);

        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();

        // Update the batchStatus using partial update
        BatchStatus partialUpdatedBatchStatus = new BatchStatus();
        partialUpdatedBatchStatus.setId(batchStatus.getId());

        restBatchStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatchStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatchStatus))
            )
            .andExpect(status().isOk());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
        BatchStatus testBatchStatus = batchStatusList.get(batchStatusList.size() - 1);
        assertThat(testBatchStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatchStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBatchStatusWithPatch() throws Exception {
        // Initialize the database
        batchStatusRepository.saveAndFlush(batchStatus);

        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();

        // Update the batchStatus using partial update
        BatchStatus partialUpdatedBatchStatus = new BatchStatus();
        partialUpdatedBatchStatus.setId(batchStatus.getId());

        partialUpdatedBatchStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBatchStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatchStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatchStatus))
            )
            .andExpect(status().isOk());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
        BatchStatus testBatchStatus = batchStatusList.get(batchStatusList.size() - 1);
        assertThat(testBatchStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatchStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();
        batchStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batchStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batchStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();
        batchStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batchStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatchStatus() throws Exception {
        int databaseSizeBeforeUpdate = batchStatusRepository.findAll().size();
        batchStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(batchStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BatchStatus in the database
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatchStatus() throws Exception {
        // Initialize the database
        batchStatusRepository.saveAndFlush(batchStatus);

        int databaseSizeBeforeDelete = batchStatusRepository.findAll().size();

        // Delete the batchStatus
        restBatchStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, batchStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BatchStatus> batchStatusList = batchStatusRepository.findAll();
        assertThat(batchStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
