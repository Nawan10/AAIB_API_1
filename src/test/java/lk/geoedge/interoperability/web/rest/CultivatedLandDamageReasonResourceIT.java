package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReason;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CultivatedLandDamageReasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandDamageReasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-damage-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandDamageReasonMockMvc;

    private CultivatedLandDamageReason cultivatedLandDamageReason;

    private CultivatedLandDamageReason insertedCultivatedLandDamageReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReason createEntity() {
        return new CultivatedLandDamageReason().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReason createUpdatedEntity() {
        return new CultivatedLandDamageReason().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandDamageReason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandDamageReason != null) {
            cultivatedLandDamageReasonRepository.delete(insertedCultivatedLandDamageReason);
            insertedCultivatedLandDamageReason = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandDamageReason
        var returnedCultivatedLandDamageReason = om.readValue(
            restCultivatedLandDamageReasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandDamageReason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandDamageReason.class
        );

        // Validate the CultivatedLandDamageReason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandDamageReasonUpdatableFieldsEquals(
            returnedCultivatedLandDamageReason,
            getPersistedCultivatedLandDamageReason(returnedCultivatedLandDamageReason)
        );

        insertedCultivatedLandDamageReason = returnedCultivatedLandDamageReason;
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReasonWithExistingId() throws Exception {
        // Create the CultivatedLandDamageReason with an existing ID
        cultivatedLandDamageReason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandDamageReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasons() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get all the cultivatedLandDamageReasonList
        restCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get the cultivatedLandDamageReason
        restCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandDamageReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandDamageReason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        Long id = cultivatedLandDamageReason.getId();

        defaultCultivatedLandDamageReasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandDamageReasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandDamageReasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get all the cultivatedLandDamageReasonList where name equals to
        defaultCultivatedLandDamageReasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get all the cultivatedLandDamageReasonList where name in
        defaultCultivatedLandDamageReasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get all the cultivatedLandDamageReasonList where name is not null
        defaultCultivatedLandDamageReasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get all the cultivatedLandDamageReasonList where name contains
        defaultCultivatedLandDamageReasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        // Get all the cultivatedLandDamageReasonList where name does not contain
        defaultCultivatedLandDamageReasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByDamageCategoryIsEqualToSomething() throws Exception {
        CultivatedLandDamageReasonDamageCategory damageCategory;
        if (TestUtil.findAll(em, CultivatedLandDamageReasonDamageCategory.class).isEmpty()) {
            cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);
            damageCategory = CultivatedLandDamageReasonDamageCategoryResourceIT.createEntity();
        } else {
            damageCategory = TestUtil.findAll(em, CultivatedLandDamageReasonDamageCategory.class).get(0);
        }
        em.persist(damageCategory);
        em.flush();
        cultivatedLandDamageReason.setDamageCategory(damageCategory);
        cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);
        Long damageCategoryId = damageCategory.getId();
        // Get all the cultivatedLandDamageReasonList where damageCategory equals to damageCategoryId
        defaultCultivatedLandDamageReasonShouldBeFound("damageCategoryId.equals=" + damageCategoryId);

        // Get all the cultivatedLandDamageReasonList where damageCategory equals to (damageCategoryId + 1)
        defaultCultivatedLandDamageReasonShouldNotBeFound("damageCategoryId.equals=" + (damageCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonsByDamageTypeIsEqualToSomething() throws Exception {
        CultivatedLandDamageReasonDamageType damageType;
        if (TestUtil.findAll(em, CultivatedLandDamageReasonDamageType.class).isEmpty()) {
            cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);
            damageType = CultivatedLandDamageReasonDamageTypeResourceIT.createEntity();
        } else {
            damageType = TestUtil.findAll(em, CultivatedLandDamageReasonDamageType.class).get(0);
        }
        em.persist(damageType);
        em.flush();
        cultivatedLandDamageReason.setDamageType(damageType);
        cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);
        Long damageTypeId = damageType.getId();
        // Get all the cultivatedLandDamageReasonList where damageType equals to damageTypeId
        defaultCultivatedLandDamageReasonShouldBeFound("damageTypeId.equals=" + damageTypeId);

        // Get all the cultivatedLandDamageReasonList where damageType equals to (damageTypeId + 1)
        defaultCultivatedLandDamageReasonShouldNotBeFound("damageTypeId.equals=" + (damageTypeId + 1));
    }

    private void defaultCultivatedLandDamageReasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandDamageReasonShouldBeFound(shouldBeFound);
        defaultCultivatedLandDamageReasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandDamageReasonShouldBeFound(String filter) throws Exception {
        restCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandDamageReasonShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandDamageReason() throws Exception {
        // Get the cultivatedLandDamageReason
        restCultivatedLandDamageReasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReason
        CultivatedLandDamageReason updatedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository
            .findById(cultivatedLandDamageReason.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandDamageReason are not directly saved in db
        em.detach(updatedCultivatedLandDamageReason);
        updatedCultivatedLandDamageReason.name(UPDATED_NAME);

        restCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandDamageReasonToMatchAllProperties(updatedCultivatedLandDamageReason);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandDamageReasonWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReason using partial update
        CultivatedLandDamageReason partialUpdatedCultivatedLandDamageReason = new CultivatedLandDamageReason();
        partialUpdatedCultivatedLandDamageReason.setId(cultivatedLandDamageReason.getId());

        restCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandDamageReason, cultivatedLandDamageReason),
            getPersistedCultivatedLandDamageReason(cultivatedLandDamageReason)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandDamageReasonWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReason using partial update
        CultivatedLandDamageReason partialUpdatedCultivatedLandDamageReason = new CultivatedLandDamageReason();
        partialUpdatedCultivatedLandDamageReason.setId(cultivatedLandDamageReason.getId());

        partialUpdatedCultivatedLandDamageReason.name(UPDATED_NAME);

        restCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReasonUpdatableFieldsEquals(
            partialUpdatedCultivatedLandDamageReason,
            getPersistedCultivatedLandDamageReason(partialUpdatedCultivatedLandDamageReason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReason = cultivatedLandDamageReasonRepository.saveAndFlush(cultivatedLandDamageReason);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandDamageReason
        restCultivatedLandDamageReasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandDamageReason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandDamageReasonRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CultivatedLandDamageReason getPersistedCultivatedLandDamageReason(CultivatedLandDamageReason cultivatedLandDamageReason) {
        return cultivatedLandDamageReasonRepository.findById(cultivatedLandDamageReason.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandDamageReasonToMatchAllProperties(
        CultivatedLandDamageReason expectedCultivatedLandDamageReason
    ) {
        assertCultivatedLandDamageReasonAllPropertiesEquals(
            expectedCultivatedLandDamageReason,
            getPersistedCultivatedLandDamageReason(expectedCultivatedLandDamageReason)
        );
    }

    protected void assertPersistedCultivatedLandDamageReasonToMatchUpdatableProperties(
        CultivatedLandDamageReason expectedCultivatedLandDamageReason
    ) {
        assertCultivatedLandDamageReasonAllUpdatablePropertiesEquals(
            expectedCultivatedLandDamageReason,
            getPersistedCultivatedLandDamageReason(expectedCultivatedLandDamageReason)
        );
    }
}
