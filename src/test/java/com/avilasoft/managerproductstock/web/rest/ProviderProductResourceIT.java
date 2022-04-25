package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Provider;
import com.avilasoft.managerproductstock.domain.ProviderProduct;
import com.avilasoft.managerproductstock.repository.ProviderProductRepository;
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
 * Integration tests for the {@link ProviderProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProviderProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provider-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProviderProductRepository providerProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProviderProductMockMvc;

    private ProviderProduct providerProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProviderProduct createEntity(EntityManager em) {
        ProviderProduct providerProduct = new ProviderProduct().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        providerProduct.setProvider(provider);
        return providerProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProviderProduct createUpdatedEntity(EntityManager em) {
        ProviderProduct providerProduct = new ProviderProduct().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createUpdatedEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        providerProduct.setProvider(provider);
        return providerProduct;
    }

    @BeforeEach
    public void initTest() {
        providerProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createProviderProduct() throws Exception {
        int databaseSizeBeforeCreate = providerProductRepository.findAll().size();
        // Create the ProviderProduct
        restProviderProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isCreated());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeCreate + 1);
        ProviderProduct testProviderProduct = providerProductList.get(providerProductList.size() - 1);
        assertThat(testProviderProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProviderProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProviderProductWithExistingId() throws Exception {
        // Create the ProviderProduct with an existing ID
        providerProduct.setId(1L);

        int databaseSizeBeforeCreate = providerProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProviderProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerProductRepository.findAll().size();
        // set the field null
        providerProduct.setName(null);

        // Create the ProviderProduct, which fails.

        restProviderProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isBadRequest());

        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProviderProducts() throws Exception {
        // Initialize the database
        providerProductRepository.saveAndFlush(providerProduct);

        // Get all the providerProductList
        restProviderProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(providerProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProviderProduct() throws Exception {
        // Initialize the database
        providerProductRepository.saveAndFlush(providerProduct);

        // Get the providerProduct
        restProviderProductMockMvc
            .perform(get(ENTITY_API_URL_ID, providerProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(providerProduct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingProviderProduct() throws Exception {
        // Get the providerProduct
        restProviderProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProviderProduct() throws Exception {
        // Initialize the database
        providerProductRepository.saveAndFlush(providerProduct);

        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();

        // Update the providerProduct
        ProviderProduct updatedProviderProduct = providerProductRepository.findById(providerProduct.getId()).get();
        // Disconnect from session so that the updates on updatedProviderProduct are not directly saved in db
        em.detach(updatedProviderProduct);
        updatedProviderProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProviderProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProviderProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProviderProduct))
            )
            .andExpect(status().isOk());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
        ProviderProduct testProviderProduct = providerProductList.get(providerProductList.size() - 1);
        assertThat(testProviderProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProviderProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProviderProduct() throws Exception {
        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();
        providerProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, providerProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProviderProduct() throws Exception {
        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();
        providerProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProviderProduct() throws Exception {
        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();
        providerProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProviderProductWithPatch() throws Exception {
        // Initialize the database
        providerProductRepository.saveAndFlush(providerProduct);

        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();

        // Update the providerProduct using partial update
        ProviderProduct partialUpdatedProviderProduct = new ProviderProduct();
        partialUpdatedProviderProduct.setId(providerProduct.getId());

        partialUpdatedProviderProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProviderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProviderProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProviderProduct))
            )
            .andExpect(status().isOk());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
        ProviderProduct testProviderProduct = providerProductList.get(providerProductList.size() - 1);
        assertThat(testProviderProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProviderProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProviderProductWithPatch() throws Exception {
        // Initialize the database
        providerProductRepository.saveAndFlush(providerProduct);

        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();

        // Update the providerProduct using partial update
        ProviderProduct partialUpdatedProviderProduct = new ProviderProduct();
        partialUpdatedProviderProduct.setId(providerProduct.getId());

        partialUpdatedProviderProduct.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProviderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProviderProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProviderProduct))
            )
            .andExpect(status().isOk());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
        ProviderProduct testProviderProduct = providerProductList.get(providerProductList.size() - 1);
        assertThat(testProviderProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProviderProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProviderProduct() throws Exception {
        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();
        providerProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, providerProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProviderProduct() throws Exception {
        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();
        providerProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProviderProduct() throws Exception {
        int databaseSizeBeforeUpdate = providerProductRepository.findAll().size();
        providerProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProviderProduct in the database
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProviderProduct() throws Exception {
        // Initialize the database
        providerProductRepository.saveAndFlush(providerProduct);

        int databaseSizeBeforeDelete = providerProductRepository.findAll().size();

        // Delete the providerProduct
        restProviderProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, providerProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProviderProduct> providerProductList = providerProductRepository.findAll();
        assertThat(providerProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
