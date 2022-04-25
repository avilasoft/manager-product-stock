package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import com.avilasoft.managerproductstock.domain.User;
import com.avilasoft.managerproductstock.repository.BusinessAssociateRepository;
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
 * Integration tests for the {@link BusinessAssociateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessAssociateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "\\]UX\"@ctu.T\\s{L";
    private static final String UPDATED_EMAIL = "hN9@y[:Bj:._q";

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

    private static final String ENTITY_API_URL = "/api/business-associates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessAssociateRepository businessAssociateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessAssociateMockMvc;

    private BusinessAssociate businessAssociate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessAssociate createEntity(EntityManager em) {
        BusinessAssociate businessAssociate = new BusinessAssociate()
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
        businessAssociate.setUser(user);
        return businessAssociate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessAssociate createUpdatedEntity(EntityManager em) {
        BusinessAssociate businessAssociate = new BusinessAssociate()
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
        businessAssociate.setUser(user);
        return businessAssociate;
    }

    @BeforeEach
    public void initTest() {
        businessAssociate = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessAssociate() throws Exception {
        int databaseSizeBeforeCreate = businessAssociateRepository.findAll().size();
        // Create the BusinessAssociate
        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessAssociate testBusinessAssociate = businessAssociateList.get(businessAssociateList.size() - 1);
        assertThat(testBusinessAssociate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessAssociate.getDescrption()).isEqualTo(DEFAULT_DESCRPTION);
        assertThat(testBusinessAssociate.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testBusinessAssociate.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testBusinessAssociate.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testBusinessAssociate.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testBusinessAssociate.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBusinessAssociate.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createBusinessAssociateWithExistingId() throws Exception {
        // Create the BusinessAssociate with an existing ID
        businessAssociate.setId(1L);

        int databaseSizeBeforeCreate = businessAssociateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setName(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescrptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setDescrption(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setEmail(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setPhone(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setAddressLine1(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setCity(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setCountry(null);

        // Create the BusinessAssociate, which fails.

        restBusinessAssociateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessAssociates() throws Exception {
        // Initialize the database
        businessAssociateRepository.saveAndFlush(businessAssociate);

        // Get all the businessAssociateList
        restBusinessAssociateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessAssociate.getId().intValue())))
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
    void getBusinessAssociate() throws Exception {
        // Initialize the database
        businessAssociateRepository.saveAndFlush(businessAssociate);

        // Get the businessAssociate
        restBusinessAssociateMockMvc
            .perform(get(ENTITY_API_URL_ID, businessAssociate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessAssociate.getId().intValue()))
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
    void getNonExistingBusinessAssociate() throws Exception {
        // Get the businessAssociate
        restBusinessAssociateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusinessAssociate() throws Exception {
        // Initialize the database
        businessAssociateRepository.saveAndFlush(businessAssociate);

        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();

        // Update the businessAssociate
        BusinessAssociate updatedBusinessAssociate = businessAssociateRepository.findById(businessAssociate.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessAssociate are not directly saved in db
        em.detach(updatedBusinessAssociate);
        updatedBusinessAssociate
            .name(UPDATED_NAME)
            .descrption(UPDATED_DESCRPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restBusinessAssociateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBusinessAssociate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBusinessAssociate))
            )
            .andExpect(status().isOk());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
        BusinessAssociate testBusinessAssociate = businessAssociateList.get(businessAssociateList.size() - 1);
        assertThat(testBusinessAssociate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessAssociate.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
        assertThat(testBusinessAssociate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBusinessAssociate.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBusinessAssociate.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testBusinessAssociate.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testBusinessAssociate.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBusinessAssociate.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingBusinessAssociate() throws Exception {
        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();
        businessAssociate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessAssociateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessAssociate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessAssociate() throws Exception {
        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();
        businessAssociate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessAssociateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessAssociate() throws Exception {
        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();
        businessAssociate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessAssociateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessAssociateWithPatch() throws Exception {
        // Initialize the database
        businessAssociateRepository.saveAndFlush(businessAssociate);

        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();

        // Update the businessAssociate using partial update
        BusinessAssociate partialUpdatedBusinessAssociate = new BusinessAssociate();
        partialUpdatedBusinessAssociate.setId(businessAssociate.getId());

        partialUpdatedBusinessAssociate
            .descrption(UPDATED_DESCRPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restBusinessAssociateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessAssociate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessAssociate))
            )
            .andExpect(status().isOk());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
        BusinessAssociate testBusinessAssociate = businessAssociateList.get(businessAssociateList.size() - 1);
        assertThat(testBusinessAssociate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusinessAssociate.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
        assertThat(testBusinessAssociate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBusinessAssociate.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBusinessAssociate.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testBusinessAssociate.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testBusinessAssociate.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBusinessAssociate.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateBusinessAssociateWithPatch() throws Exception {
        // Initialize the database
        businessAssociateRepository.saveAndFlush(businessAssociate);

        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();

        // Update the businessAssociate using partial update
        BusinessAssociate partialUpdatedBusinessAssociate = new BusinessAssociate();
        partialUpdatedBusinessAssociate.setId(businessAssociate.getId());

        partialUpdatedBusinessAssociate
            .name(UPDATED_NAME)
            .descrption(UPDATED_DESCRPTION)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restBusinessAssociateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessAssociate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessAssociate))
            )
            .andExpect(status().isOk());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
        BusinessAssociate testBusinessAssociate = businessAssociateList.get(businessAssociateList.size() - 1);
        assertThat(testBusinessAssociate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusinessAssociate.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
        assertThat(testBusinessAssociate.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBusinessAssociate.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBusinessAssociate.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testBusinessAssociate.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testBusinessAssociate.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBusinessAssociate.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessAssociate() throws Exception {
        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();
        businessAssociate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessAssociateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessAssociate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessAssociate() throws Exception {
        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();
        businessAssociate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessAssociateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessAssociate() throws Exception {
        int databaseSizeBeforeUpdate = businessAssociateRepository.findAll().size();
        businessAssociate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessAssociateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessAssociate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessAssociate in the database
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessAssociate() throws Exception {
        // Initialize the database
        businessAssociateRepository.saveAndFlush(businessAssociate);

        int databaseSizeBeforeDelete = businessAssociateRepository.findAll().size();

        // Delete the businessAssociate
        restBusinessAssociateMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessAssociate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessAssociate> businessAssociateList = businessAssociateRepository.findAll();
        assertThat(businessAssociateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
