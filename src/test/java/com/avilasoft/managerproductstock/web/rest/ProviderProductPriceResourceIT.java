package com.avilasoft.managerproductstock.web.rest;

import static com.avilasoft.managerproductstock.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.ProviderProductPrice;
import com.avilasoft.managerproductstock.domain.Unit;
import com.avilasoft.managerproductstock.repository.ProviderProductPriceRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProviderProductPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProviderProductPriceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/provider-product-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProviderProductPriceRepository providerProductPriceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProviderProductPriceMockMvc;

    private ProviderProductPrice providerProductPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProviderProductPrice createEntity(EntityManager em) {
        ProviderProductPrice providerProductPrice = new ProviderProductPrice().name(DEFAULT_NAME).value(DEFAULT_VALUE);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        providerProductPrice.setUnit(unit);
        return providerProductPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProviderProductPrice createUpdatedEntity(EntityManager em) {
        ProviderProductPrice providerProductPrice = new ProviderProductPrice().name(UPDATED_NAME).value(UPDATED_VALUE);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        providerProductPrice.setUnit(unit);
        return providerProductPrice;
    }

    @BeforeEach
    public void initTest() {
        providerProductPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createProviderProductPrice() throws Exception {
        int databaseSizeBeforeCreate = providerProductPriceRepository.findAll().size();
        // Create the ProviderProductPrice
        restProviderProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isCreated());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeCreate + 1);
        ProviderProductPrice testProviderProductPrice = providerProductPriceList.get(providerProductPriceList.size() - 1);
        assertThat(testProviderProductPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProviderProductPrice.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createProviderProductPriceWithExistingId() throws Exception {
        // Create the ProviderProductPrice with an existing ID
        providerProductPrice.setId(1L);

        int databaseSizeBeforeCreate = providerProductPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProviderProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerProductPriceRepository.findAll().size();
        // set the field null
        providerProductPrice.setName(null);

        // Create the ProviderProductPrice, which fails.

        restProviderProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerProductPriceRepository.findAll().size();
        // set the field null
        providerProductPrice.setValue(null);

        // Create the ProviderProductPrice, which fails.

        restProviderProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProviderProductPrices() throws Exception {
        // Initialize the database
        providerProductPriceRepository.saveAndFlush(providerProductPrice);

        // Get all the providerProductPriceList
        restProviderProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(providerProductPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));
    }

    @Test
    @Transactional
    void getProviderProductPrice() throws Exception {
        // Initialize the database
        providerProductPriceRepository.saveAndFlush(providerProductPrice);

        // Get the providerProductPrice
        restProviderProductPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, providerProductPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(providerProductPrice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getNonExistingProviderProductPrice() throws Exception {
        // Get the providerProductPrice
        restProviderProductPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProviderProductPrice() throws Exception {
        // Initialize the database
        providerProductPriceRepository.saveAndFlush(providerProductPrice);

        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();

        // Update the providerProductPrice
        ProviderProductPrice updatedProviderProductPrice = providerProductPriceRepository.findById(providerProductPrice.getId()).get();
        // Disconnect from session so that the updates on updatedProviderProductPrice are not directly saved in db
        em.detach(updatedProviderProductPrice);
        updatedProviderProductPrice.name(UPDATED_NAME).value(UPDATED_VALUE);

        restProviderProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProviderProductPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProviderProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
        ProviderProductPrice testProviderProductPrice = providerProductPriceList.get(providerProductPriceList.size() - 1);
        assertThat(testProviderProductPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProviderProductPrice.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingProviderProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();
        providerProductPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, providerProductPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProviderProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();
        providerProductPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProviderProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();
        providerProductPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProviderProductPriceWithPatch() throws Exception {
        // Initialize the database
        providerProductPriceRepository.saveAndFlush(providerProductPrice);

        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();

        // Update the providerProductPrice using partial update
        ProviderProductPrice partialUpdatedProviderProductPrice = new ProviderProductPrice();
        partialUpdatedProviderProductPrice.setId(providerProductPrice.getId());

        partialUpdatedProviderProductPrice.value(UPDATED_VALUE);

        restProviderProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProviderProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProviderProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
        ProviderProductPrice testProviderProductPrice = providerProductPriceList.get(providerProductPriceList.size() - 1);
        assertThat(testProviderProductPrice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProviderProductPrice.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateProviderProductPriceWithPatch() throws Exception {
        // Initialize the database
        providerProductPriceRepository.saveAndFlush(providerProductPrice);

        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();

        // Update the providerProductPrice using partial update
        ProviderProductPrice partialUpdatedProviderProductPrice = new ProviderProductPrice();
        partialUpdatedProviderProductPrice.setId(providerProductPrice.getId());

        partialUpdatedProviderProductPrice.name(UPDATED_NAME).value(UPDATED_VALUE);

        restProviderProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProviderProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProviderProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
        ProviderProductPrice testProviderProductPrice = providerProductPriceList.get(providerProductPriceList.size() - 1);
        assertThat(testProviderProductPrice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProviderProductPrice.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingProviderProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();
        providerProductPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, providerProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProviderProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();
        providerProductPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProviderProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = providerProductPriceRepository.findAll().size();
        providerProductPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(providerProductPrice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProviderProductPrice in the database
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProviderProductPrice() throws Exception {
        // Initialize the database
        providerProductPriceRepository.saveAndFlush(providerProductPrice);

        int databaseSizeBeforeDelete = providerProductPriceRepository.findAll().size();

        // Delete the providerProductPrice
        restProviderProductPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, providerProductPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProviderProductPrice> providerProductPriceList = providerProductPriceRepository.findAll();
        assertThat(providerProductPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
