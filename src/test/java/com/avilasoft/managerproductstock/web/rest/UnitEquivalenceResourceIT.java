package com.avilasoft.managerproductstock.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avilasoft.managerproductstock.IntegrationTest;
import com.avilasoft.managerproductstock.domain.Unit;
import com.avilasoft.managerproductstock.domain.UnitEquivalence;
import com.avilasoft.managerproductstock.repository.UnitEquivalenceRepository;
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
 * Integration tests for the {@link UnitEquivalenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitEquivalenceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unit-equivalences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitEquivalenceRepository unitEquivalenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitEquivalenceMockMvc;

    private UnitEquivalence unitEquivalence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitEquivalence createEntity(EntityManager em) {
        UnitEquivalence unitEquivalence = new UnitEquivalence().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        unitEquivalence.setUnit(unit);
        return unitEquivalence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitEquivalence createUpdatedEntity(EntityManager em) {
        UnitEquivalence unitEquivalence = new UnitEquivalence().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        unitEquivalence.setUnit(unit);
        return unitEquivalence;
    }

    @BeforeEach
    public void initTest() {
        unitEquivalence = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitEquivalence() throws Exception {
        int databaseSizeBeforeCreate = unitEquivalenceRepository.findAll().size();
        // Create the UnitEquivalence
        restUnitEquivalenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isCreated());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeCreate + 1);
        UnitEquivalence testUnitEquivalence = unitEquivalenceList.get(unitEquivalenceList.size() - 1);
        assertThat(testUnitEquivalence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitEquivalence.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUnitEquivalenceWithExistingId() throws Exception {
        // Create the UnitEquivalence with an existing ID
        unitEquivalence.setId(1L);

        int databaseSizeBeforeCreate = unitEquivalenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitEquivalenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitEquivalenceRepository.findAll().size();
        // set the field null
        unitEquivalence.setName(null);

        // Create the UnitEquivalence, which fails.

        restUnitEquivalenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isBadRequest());

        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitEquivalences() throws Exception {
        // Initialize the database
        unitEquivalenceRepository.saveAndFlush(unitEquivalence);

        // Get all the unitEquivalenceList
        restUnitEquivalenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitEquivalence.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUnitEquivalence() throws Exception {
        // Initialize the database
        unitEquivalenceRepository.saveAndFlush(unitEquivalence);

        // Get the unitEquivalence
        restUnitEquivalenceMockMvc
            .perform(get(ENTITY_API_URL_ID, unitEquivalence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitEquivalence.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingUnitEquivalence() throws Exception {
        // Get the unitEquivalence
        restUnitEquivalenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUnitEquivalence() throws Exception {
        // Initialize the database
        unitEquivalenceRepository.saveAndFlush(unitEquivalence);

        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();

        // Update the unitEquivalence
        UnitEquivalence updatedUnitEquivalence = unitEquivalenceRepository.findById(unitEquivalence.getId()).get();
        // Disconnect from session so that the updates on updatedUnitEquivalence are not directly saved in db
        em.detach(updatedUnitEquivalence);
        updatedUnitEquivalence.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUnitEquivalenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnitEquivalence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnitEquivalence))
            )
            .andExpect(status().isOk());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
        UnitEquivalence testUnitEquivalence = unitEquivalenceList.get(unitEquivalenceList.size() - 1);
        assertThat(testUnitEquivalence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitEquivalence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUnitEquivalence() throws Exception {
        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();
        unitEquivalence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitEquivalenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitEquivalence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitEquivalence() throws Exception {
        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();
        unitEquivalence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitEquivalenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitEquivalence() throws Exception {
        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();
        unitEquivalence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitEquivalenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitEquivalenceWithPatch() throws Exception {
        // Initialize the database
        unitEquivalenceRepository.saveAndFlush(unitEquivalence);

        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();

        // Update the unitEquivalence using partial update
        UnitEquivalence partialUpdatedUnitEquivalence = new UnitEquivalence();
        partialUpdatedUnitEquivalence.setId(unitEquivalence.getId());

        partialUpdatedUnitEquivalence.description(UPDATED_DESCRIPTION);

        restUnitEquivalenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitEquivalence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitEquivalence))
            )
            .andExpect(status().isOk());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
        UnitEquivalence testUnitEquivalence = unitEquivalenceList.get(unitEquivalenceList.size() - 1);
        assertThat(testUnitEquivalence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitEquivalence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUnitEquivalenceWithPatch() throws Exception {
        // Initialize the database
        unitEquivalenceRepository.saveAndFlush(unitEquivalence);

        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();

        // Update the unitEquivalence using partial update
        UnitEquivalence partialUpdatedUnitEquivalence = new UnitEquivalence();
        partialUpdatedUnitEquivalence.setId(unitEquivalence.getId());

        partialUpdatedUnitEquivalence.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUnitEquivalenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitEquivalence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitEquivalence))
            )
            .andExpect(status().isOk());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
        UnitEquivalence testUnitEquivalence = unitEquivalenceList.get(unitEquivalenceList.size() - 1);
        assertThat(testUnitEquivalence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitEquivalence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUnitEquivalence() throws Exception {
        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();
        unitEquivalence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitEquivalenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitEquivalence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitEquivalence() throws Exception {
        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();
        unitEquivalence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitEquivalenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitEquivalence() throws Exception {
        int databaseSizeBeforeUpdate = unitEquivalenceRepository.findAll().size();
        unitEquivalence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitEquivalenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitEquivalence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitEquivalence in the database
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitEquivalence() throws Exception {
        // Initialize the database
        unitEquivalenceRepository.saveAndFlush(unitEquivalence);

        int databaseSizeBeforeDelete = unitEquivalenceRepository.findAll().size();

        // Delete the unitEquivalence
        restUnitEquivalenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitEquivalence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitEquivalence> unitEquivalenceList = unitEquivalenceRepository.findAll();
        assertThat(unitEquivalenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
