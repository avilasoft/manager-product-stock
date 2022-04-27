package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Batch;
import com.avilasoft.managerproductstock.domain.BatchProduct;
import com.avilasoft.managerproductstock.domain.Product;
import com.avilasoft.managerproductstock.repository.BatchProductRepository;
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
 * Integration tests for the {@link BatchProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BatchProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/batch-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BatchProductRepository batchProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBatchProductMockMvc;

    private BatchProduct batchProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BatchProduct createEntity(EntityManager em) {
        BatchProduct batchProduct = new BatchProduct().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        batchProduct.setProduct(product);
        // Add required entity
        Batch batch;
        if (TestUtil.findAll(em, Batch.class).isEmpty()) {
            batch = BatchResourceIT.createEntity(em);
            em.persist(batch);
            em.flush();
        } else {
            batch = TestUtil.findAll(em, Batch.class).get(0);
        }
        batchProduct.setBatch(batch);
        return batchProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BatchProduct createUpdatedEntity(EntityManager em) {
        BatchProduct batchProduct = new BatchProduct().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        batchProduct.setProduct(product);
        // Add required entity
        Batch batch;
        if (TestUtil.findAll(em, Batch.class).isEmpty()) {
            batch = BatchResourceIT.createUpdatedEntity(em);
            em.persist(batch);
            em.flush();
        } else {
            batch = TestUtil.findAll(em, Batch.class).get(0);
        }
        batchProduct.setBatch(batch);
        return batchProduct;
    }

    @BeforeEach
    public void initTest() {
        batchProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createBatchProduct() throws Exception {
        int databaseSizeBeforeCreate = batchProductRepository.findAll().size();
        // Create the BatchProduct
        restBatchProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchProduct)))
            .andExpect(status().isCreated());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeCreate + 1);
        BatchProduct testBatchProduct = batchProductList.get(batchProductList.size() - 1);
        assertThat(testBatchProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatchProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBatchProductWithExistingId() throws Exception {
        // Create the BatchProduct with an existing ID
        batchProduct.setId(1L);

        int databaseSizeBeforeCreate = batchProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchProduct)))
            .andExpect(status().isBadRequest());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = batchProductRepository.findAll().size();
        // set the field null
        batchProduct.setName(null);

        // Create the BatchProduct, which fails.

        restBatchProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchProduct)))
            .andExpect(status().isBadRequest());

        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBatchProducts() throws Exception {
        // Initialize the database
        batchProductRepository.saveAndFlush(batchProduct);

        // Get all the batchProductList
        restBatchProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batchProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBatchProduct() throws Exception {
        // Initialize the database
        batchProductRepository.saveAndFlush(batchProduct);

        // Get the batchProduct
        restBatchProductMockMvc
            .perform(get(ENTITY_API_URL_ID, batchProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(batchProduct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingBatchProduct() throws Exception {
        // Get the batchProduct
        restBatchProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBatchProduct() throws Exception {
        // Initialize the database
        batchProductRepository.saveAndFlush(batchProduct);

        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();

        // Update the batchProduct
        BatchProduct updatedBatchProduct = batchProductRepository.findById(batchProduct.getId()).get();
        // Disconnect from session so that the updates on updatedBatchProduct are not directly saved in db
        em.detach(updatedBatchProduct);
        updatedBatchProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBatchProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBatchProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBatchProduct))
            )
            .andExpect(status().isOk());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
        BatchProduct testBatchProduct = batchProductList.get(batchProductList.size() - 1);
        assertThat(testBatchProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatchProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();
        batchProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, batchProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batchProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();
        batchProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(batchProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();
        batchProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(batchProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBatchProductWithPatch() throws Exception {
        // Initialize the database
        batchProductRepository.saveAndFlush(batchProduct);

        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();

        // Update the batchProduct using partial update
        BatchProduct partialUpdatedBatchProduct = new BatchProduct();
        partialUpdatedBatchProduct.setId(batchProduct.getId());

        partialUpdatedBatchProduct.description(UPDATED_DESCRIPTION);

        restBatchProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatchProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatchProduct))
            )
            .andExpect(status().isOk());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
        BatchProduct testBatchProduct = batchProductList.get(batchProductList.size() - 1);
        assertThat(testBatchProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBatchProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBatchProductWithPatch() throws Exception {
        // Initialize the database
        batchProductRepository.saveAndFlush(batchProduct);

        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();

        // Update the batchProduct using partial update
        BatchProduct partialUpdatedBatchProduct = new BatchProduct();
        partialUpdatedBatchProduct.setId(batchProduct.getId());

        partialUpdatedBatchProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restBatchProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBatchProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBatchProduct))
            )
            .andExpect(status().isOk());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
        BatchProduct testBatchProduct = batchProductList.get(batchProductList.size() - 1);
        assertThat(testBatchProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBatchProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();
        batchProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, batchProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batchProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();
        batchProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(batchProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = batchProductRepository.findAll().size();
        batchProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBatchProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(batchProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BatchProduct in the database
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBatchProduct() throws Exception {
        // Initialize the database
        batchProductRepository.saveAndFlush(batchProduct);

        int databaseSizeBeforeDelete = batchProductRepository.findAll().size();

        // Delete the batchProduct
        restBatchProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, batchProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BatchProduct> batchProductList = batchProductRepository.findAll();
        assertThat(batchProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
