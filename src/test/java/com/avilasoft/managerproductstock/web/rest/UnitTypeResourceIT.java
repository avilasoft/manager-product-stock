package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Unit;
import com.avilasoft.managerproductstock.domain.UnitType;
import com.avilasoft.managerproductstock.repository.UnitTypeRepository;
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
 * Integration tests for the {@link UnitTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unit-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitTypeRepository unitTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitTypeMockMvc;

    private UnitType unitType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitType createEntity(EntityManager em) {
        UnitType unitType = new UnitType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        unitType.getUnits().add(unit);
        return unitType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitType createUpdatedEntity(EntityManager em) {
        UnitType unitType = new UnitType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        unitType.getUnits().add(unit);
        return unitType;
    }

    @BeforeEach
    public void initTest() {
        unitType = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitType() throws Exception {
        int databaseSizeBeforeCreate = unitTypeRepository.findAll().size();
        // Create the UnitType
        restUnitTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitType)))
            .andExpect(status().isCreated());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeCreate + 1);
        UnitType testUnitType = unitTypeList.get(unitTypeList.size() - 1);
        assertThat(testUnitType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUnitTypeWithExistingId() throws Exception {
        // Create the UnitType with an existing ID
        unitType.setId(1L);

        int databaseSizeBeforeCreate = unitTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitType)))
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitTypeRepository.findAll().size();
        // set the field null
        unitType.setName(null);

        // Create the UnitType, which fails.

        restUnitTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitType)))
            .andExpect(status().isBadRequest());

        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitTypes() throws Exception {
        // Initialize the database
        unitTypeRepository.saveAndFlush(unitType);

        // Get all the unitTypeList
        restUnitTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUnitType() throws Exception {
        // Initialize the database
        unitTypeRepository.saveAndFlush(unitType);

        // Get the unitType
        restUnitTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, unitType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingUnitType() throws Exception {
        // Get the unitType
        restUnitTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUnitType() throws Exception {
        // Initialize the database
        unitTypeRepository.saveAndFlush(unitType);

        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();

        // Update the unitType
        UnitType updatedUnitType = unitTypeRepository.findById(unitType.getId()).get();
        // Disconnect from session so that the updates on updatedUnitType are not directly saved in db
        em.detach(updatedUnitType);
        updatedUnitType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUnitTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnitType))
            )
            .andExpect(status().isOk());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
        UnitType testUnitType = unitTypeList.get(unitTypeList.size() - 1);
        assertThat(testUnitType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUnitType() throws Exception {
        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();
        unitType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitType() throws Exception {
        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();
        unitType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitType() throws Exception {
        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();
        unitType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitTypeWithPatch() throws Exception {
        // Initialize the database
        unitTypeRepository.saveAndFlush(unitType);

        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();

        // Update the unitType using partial update
        UnitType partialUpdatedUnitType = new UnitType();
        partialUpdatedUnitType.setId(unitType.getId());

        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitType))
            )
            .andExpect(status().isOk());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
        UnitType testUnitType = unitTypeList.get(unitTypeList.size() - 1);
        assertThat(testUnitType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUnitTypeWithPatch() throws Exception {
        // Initialize the database
        unitTypeRepository.saveAndFlush(unitType);

        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();

        // Update the unitType using partial update
        UnitType partialUpdatedUnitType = new UnitType();
        partialUpdatedUnitType.setId(unitType.getId());

        partialUpdatedUnitType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitType))
            )
            .andExpect(status().isOk());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
        UnitType testUnitType = unitTypeList.get(unitTypeList.size() - 1);
        assertThat(testUnitType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUnitType() throws Exception {
        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();
        unitType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitType() throws Exception {
        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();
        unitType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitType))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitType() throws Exception {
        int databaseSizeBeforeUpdate = unitTypeRepository.findAll().size();
        unitType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(unitType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitType in the database
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitType() throws Exception {
        // Initialize the database
        unitTypeRepository.saveAndFlush(unitType);

        int databaseSizeBeforeDelete = unitTypeRepository.findAll().size();

        // Delete the unitType
        restUnitTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitType> unitTypeList = unitTypeRepository.findAll();
        assertThat(unitTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
