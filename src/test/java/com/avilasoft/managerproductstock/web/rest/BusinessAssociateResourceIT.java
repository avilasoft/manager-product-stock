package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.BusinessAssociate;
import com.avilasoft.managerproductstock.domain.User;
import com.avilasoft.managerproductstock.domain.enumeration.BusinessAssociateType;
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

    private static final String DEFAULT_COMERCIAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMERCIAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BusinessAssociateType DEFAULT_TYPE = BusinessAssociateType.PROVIDER;
    private static final BusinessAssociateType UPDATED_TYPE = BusinessAssociateType.CLIENT;

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
            .comercialName(DEFAULT_COMERCIAL_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE);
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
            .comercialName(UPDATED_COMERCIAL_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE);
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
        assertThat(testBusinessAssociate.getComercialName()).isEqualTo(DEFAULT_COMERCIAL_NAME);
        assertThat(testBusinessAssociate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessAssociate.getType()).isEqualTo(DEFAULT_TYPE);
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
    void checkComercialNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setComercialName(null);

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
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAssociateRepository.findAll().size();
        // set the field null
        businessAssociate.setType(null);

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
            .andExpect(jsonPath("$.[*].comercialName").value(hasItem(DEFAULT_COMERCIAL_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
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
            .andExpect(jsonPath("$.comercialName").value(DEFAULT_COMERCIAL_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
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
        updatedBusinessAssociate.comercialName(UPDATED_COMERCIAL_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE);

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
        assertThat(testBusinessAssociate.getComercialName()).isEqualTo(UPDATED_COMERCIAL_NAME);
        assertThat(testBusinessAssociate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessAssociate.getType()).isEqualTo(UPDATED_TYPE);
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

        partialUpdatedBusinessAssociate.comercialName(UPDATED_COMERCIAL_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE);

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
        assertThat(testBusinessAssociate.getComercialName()).isEqualTo(UPDATED_COMERCIAL_NAME);
        assertThat(testBusinessAssociate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessAssociate.getType()).isEqualTo(UPDATED_TYPE);
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

        partialUpdatedBusinessAssociate.comercialName(UPDATED_COMERCIAL_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE);

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
        assertThat(testBusinessAssociate.getComercialName()).isEqualTo(UPDATED_COMERCIAL_NAME);
        assertThat(testBusinessAssociate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessAssociate.getType()).isEqualTo(UPDATED_TYPE);
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
