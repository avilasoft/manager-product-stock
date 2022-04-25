package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Batch;
import com.avilasoft.managerproductstock.domain.BatchStatus;
import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import com.avilasoft.managerproductstock.repository.BatchRepository;
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
 * Integration tests for the {@link BatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BatchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatchMockMvc;

    private Batch batch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createEntity(EntityManager em) {
        Batch batch = new Batch().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        BatchStatus batchStatus;
        if (TestUtil.findAll(em, BatchStatus.class).isEmpty()) {
            batchStatus = BatchStatusResourceIT.createEntity(em);
            em.persist(batchStatus);
            em.flush();
        } else {
            batchStatus = TestUtil.findAll(em, BatchStatus.class).get(0);
        }
        batch.setBachStatus(batchStatus);
        // Add required entity
        BusinessAssociate businessAssociate;
        if (TestUtil.findAll(em, BusinessAssociate.class).isEmpty()) {
            businessAssociate = BusinessAssociateResourceIT.createEntity(em);
            em.persist(businessAssociate);
            em.flush();
        } else {
            businessAssociate = TestUtil.findAll(em, BusinessAssociate.class).get(0);
        }
        batch.setBusinessAssociate(businessAssociate);
        return batch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Batch createUpdatedEntity(EntityManager em) {
        Batch batch = new Batch().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        BatchStatus batchStatus;
        if (TestUtil.findAll(em, BatchStatus.class).isEmpty()) {
            batchStatus = BatchStatusResourceIT.createUpdatedEntity(em);
            em.persist(batchStatus);
            em.flush();
        } else {
            batchStatus = TestUtil.findAll(em, BatchStatus.class).get(0);
        }
        batch.setBachStatus(batchStatus);
        // Add required entity
        BusinessAssociate businessAssociate;
        if (TestUtil.findAll(em, BusinessAssociate.class).isEmpty()) {
            businessAssociate = BusinessAssociateResourceIT.createUpdatedEntity(em);
            em.persist(businessAssociate);
            em.flush();
        } else {
            businessAssociate = TestUtil.findAll(em, BusinessAssociate.class).get(0);
        }
        batch.setBusinessAssociate(businessAssociate);
        return batch;
    }

    @BeforeEach
    public void initTest() {
        batch = createEntity(em);
    }

    @Test
    @Transactional
    void createBatch() throws Exception {
        int databaseSizeBeforeCreate = batchRepository.findAll().size();
        // Create the Batch
        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isCreated());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeCreate + 1);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBatchWithExistingId() throws Exception {
        // Create the Batch with an existing ID
        batch.setId(1L);

        int databaseSizeBeforeCreate = batchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = batchRepository.findAll().size();
        // set the field null
        batch.setName(null);

        // Create the Batch, which fails.

        restBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isBadRequest());

        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatches() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get all the batchList
        restBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        // Get the batch
        restBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, batch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBatch() throws Exception {
        // Get the batch
        restBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Update the batch
        Batch updatedBatch = batchRepository.findById(batch.getId()).get();
        // Disconnect from session so that the updates on updatedBatch are not directly saved in db
        em.detach(updatedBatch);
        updatedBatch.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();
        batch.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();
        batch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();
        batch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatchWithPatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Update the batch using partial update
        Batch partialUpdatedBatch = new Batch();
        partialUpdatedBatch.setId(batch.getId());

        partialUpdatedBatch.description(UPDATED_DESCRIPTION);

        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBatchWithPatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        int databaseSizeBeforeUpdate = batchRepository.findAll().size();

        // Update the batch using partial update
        Batch partialUpdatedBatch = new Batch();
        partialUpdatedBatch.setId(batch.getId());

        partialUpdatedBatch.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatch))
            )
            .andExpect(status().isOk());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
        Batch testBatch = batchList.get(batchList.size() - 1);
        assertThat(testBatch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();
        batch.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();
        batch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatch() throws Exception {
        int databaseSizeBeforeUpdate = batchRepository.findAll().size();
        batch.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(batch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Batch in the database
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatch() throws Exception {
        // Initialize the database
        batchRepository.saveAndFlush(batch);

        int databaseSizeBeforeDelete = batchRepository.findAll().size();

        // Delete the batch
        restBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, batch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Batch> batchList = batchRepository.findAll();
        assertThat(batchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
