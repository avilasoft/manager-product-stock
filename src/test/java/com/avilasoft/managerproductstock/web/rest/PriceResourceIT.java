package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Price;
import com.avilasoft.managerproductstock.repository.PriceRepository;
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
 * Integration tests for the {@link PriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceMockMvc;

    private Price price;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createEntity(EntityManager em) {
        Price price = new Price().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return price;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createUpdatedEntity(EntityManager em) {
        Price price = new Price().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return price;
    }

    @BeforeEach
    public void initTest() {
        price = createEntity(em);
    }

    @Test
    @Transactional
    void createPrice() throws Exception {
        int databaseSizeBeforeCreate = priceRepository.findAll().size();
        // Create the Price
        restPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(price)))
            .andExpect(status().isCreated());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate + 1);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPriceWithExistingId() throws Exception {
        // Create the Price with an existing ID
        price.setId(1L);

        int databaseSizeBeforeCreate = priceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(price)))
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setName(null);

        // Create the Price, which fails.

        restPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(price)))
            .andExpect(status().isBadRequest());

        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrices() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList
        restPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get the price
        restPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, price.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(price.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPrice() throws Exception {
        // Get the price
        restPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price
        Price updatedPrice = priceRepository.findById(price.getId()).get();
        // Disconnect from session so that the updates on updatedPrice are not directly saved in db
        em.detach(updatedPrice);
        updatedPrice.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, price.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(price))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(price))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(price)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePriceWithPatch() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price using partial update
        Price partialUpdatedPrice = new Price();
        partialUpdatedPrice.setId(price.getId());

        partialUpdatedPrice.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrice))
            )
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, price.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(price))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(price))
            )
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();
        price.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(price)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeDelete = priceRepository.findAll().size();

        // Delete the price
        restPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, price.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
