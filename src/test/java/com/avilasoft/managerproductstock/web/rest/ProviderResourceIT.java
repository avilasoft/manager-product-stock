package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Provider;
import com.avilasoft.managerproductstock.domain.User;
import com.avilasoft.managerproductstock.repository.ProviderRepository;
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
 * Integration tests for the {@link ProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "<,_c@-.8";
    private static final String UPDATED_EMAIL = "1:9Tq@lLn(.4$";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProviderMockMvc;

    private Provider provider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provider createEntity(EntityManager em) {
        Provider provider = new Provider()
            .name(DEFAULT_NAME)
            .descrption(DEFAULT_DESCRPTION)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        provider.setUser(user);
        return provider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Provider createUpdatedEntity(EntityManager em) {
        Provider provider = new Provider()
            .name(UPDATED_NAME)
            .descrption(UPDATED_DESCRPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        provider.setUser(user);
        return provider;
    }

    @BeforeEach
    public void initTest() {
        provider = createEntity(em);
    }

    @Test
    @Transactional
    void createProvider() throws Exception {
        int databaseSizeBeforeCreate = providerRepository.findAll().size();
        // Create the Provider
        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isCreated());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeCreate + 1);
        Provider testProvider = providerList.get(providerList.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvider.getDescrption()).isEqualTo(DEFAULT_DESCRPTION);
        assertThat(testProvider.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProvider.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testProvider.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testProvider.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testProvider.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProvider.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createProviderWithExistingId() throws Exception {
        // Create the Provider with an existing ID
        provider.setId(1L);

        int databaseSizeBeforeCreate = providerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setName(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescrptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setDescrption(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setEmail(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setPhone(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setAddressLine1(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setCity(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = providerRepository.findAll().size();
        // set the field null
        provider.setCountry(null);

        // Create the Provider, which fails.

        restProviderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isBadRequest());

        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProviders() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get all the providerList
        restProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descrption").value(hasItem(DEFAULT_DESCRPTION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        // Get the provider
        restProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, provider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(provider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.descrption").value(DEFAULT_DESCRPTION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingProvider() throws Exception {
        // Get the provider
        restProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Update the provider
        Provider updatedProvider = providerRepository.findById(provider.getId()).get();
        // Disconnect from session so that the updates on updatedProvider are not directly saved in db
        em.detach(updatedProvider);
        updatedProvider
            .name(UPDATED_NAME)
            .descrption(UPDATED_DESCRPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProvider))
            )
            .andExpect(status().isOk());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
        Provider testProvider = providerList.get(providerList.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvider.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
        assertThat(testProvider.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProvider.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testProvider.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testProvider.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testProvider.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProvider.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingProvider() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();
        provider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, provider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProvider() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();
        provider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProvider() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();
        provider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProviderWithPatch() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Update the provider using partial update
        Provider partialUpdatedProvider = new Provider();
        partialUpdatedProvider.setId(provider.getId());

        partialUpdatedProvider
            .name(UPDATED_NAME)
            .descrption(UPDATED_DESCRPTION)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvider))
            )
            .andExpect(status().isOk());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
        Provider testProvider = providerList.get(providerList.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvider.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
        assertThat(testProvider.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProvider.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testProvider.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testProvider.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testProvider.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProvider.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateProviderWithPatch() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        int databaseSizeBeforeUpdate = providerRepository.findAll().size();

        // Update the provider using partial update
        Provider partialUpdatedProvider = new Provider();
        partialUpdatedProvider.setId(provider.getId());

        partialUpdatedProvider
            .name(UPDATED_NAME)
            .descrption(UPDATED_DESCRPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProvider))
            )
            .andExpect(status().isOk());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
        Provider testProvider = providerList.get(providerList.size() - 1);
        assertThat(testProvider.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvider.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
        assertThat(testProvider.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProvider.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testProvider.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testProvider.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testProvider.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProvider.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingProvider() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();
        provider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, provider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProvider() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();
        provider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(provider))
            )
            .andExpect(status().isBadRequest());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProvider() throws Exception {
        int databaseSizeBeforeUpdate = providerRepository.findAll().size();
        provider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProviderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(provider)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Provider in the database
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProvider() throws Exception {
        // Initialize the database
        providerRepository.saveAndFlush(provider);

        int databaseSizeBeforeDelete = providerRepository.findAll().size();

        // Delete the provider
        restProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, provider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Provider> providerList = providerRepository.findAll();
        assertThat(providerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
