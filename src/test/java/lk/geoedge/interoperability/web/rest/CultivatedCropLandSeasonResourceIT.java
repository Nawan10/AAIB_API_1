package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedCropLandSeasonAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedCropLandSeason;
import lk.geoedge.interoperability.repository.CultivatedCropLandSeasonRepository;
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
 * Integration tests for the {@link CultivatedCropLandSeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedCropLandSeasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-crop-land-seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedCropLandSeasonMockMvc;

    private CultivatedCropLandSeason cultivatedCropLandSeason;

    private CultivatedCropLandSeason insertedCultivatedCropLandSeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCropLandSeason createEntity() {
        return new CultivatedCropLandSeason().name(DEFAULT_NAME).period(DEFAULT_PERIOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCropLandSeason createUpdatedEntity() {
        return new CultivatedCropLandSeason().name(UPDATED_NAME).period(UPDATED_PERIOD);
    }

    @BeforeEach
    void initTest() {
        cultivatedCropLandSeason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedCropLandSeason != null) {
            cultivatedCropLandSeasonRepository.delete(insertedCultivatedCropLandSeason);
            insertedCultivatedCropLandSeason = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedCropLandSeason
        var returnedCultivatedCropLandSeason = om.readValue(
            restCultivatedCropLandSeasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedCropLandSeason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedCropLandSeason.class
        );

        // Validate the CultivatedCropLandSeason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedCropLandSeasonUpdatableFieldsEquals(
            returnedCultivatedCropLandSeason,
            getPersistedCultivatedCropLandSeason(returnedCultivatedCropLandSeason)
        );

        insertedCultivatedCropLandSeason = returnedCultivatedCropLandSeason;
    }

    @Test
    @Transactional
    void createCultivatedCropLandSeasonWithExistingId() throws Exception {
        // Create the CultivatedCropLandSeason with an existing ID
        cultivatedCropLandSeason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedCropLandSeasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasons() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList
        restCultivatedCropLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCropLandSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    void getCultivatedCropLandSeason() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get the cultivatedCropLandSeason
        restCultivatedCropLandSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedCropLandSeason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedCropLandSeason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD));
    }

    @Test
    @Transactional
    void getCultivatedCropLandSeasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        Long id = cultivatedCropLandSeason.getId();

        defaultCultivatedCropLandSeasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedCropLandSeasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedCropLandSeasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where name equals to
        defaultCultivatedCropLandSeasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where name in
        defaultCultivatedCropLandSeasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where name is not null
        defaultCultivatedCropLandSeasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where name contains
        defaultCultivatedCropLandSeasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where name does not contain
        defaultCultivatedCropLandSeasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where period equals to
        defaultCultivatedCropLandSeasonFiltering("period.equals=" + DEFAULT_PERIOD, "period.equals=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where period in
        defaultCultivatedCropLandSeasonFiltering("period.in=" + DEFAULT_PERIOD + "," + UPDATED_PERIOD, "period.in=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where period is not null
        defaultCultivatedCropLandSeasonFiltering("period.specified=true", "period.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where period contains
        defaultCultivatedCropLandSeasonFiltering("period.contains=" + DEFAULT_PERIOD, "period.contains=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropLandSeasonsByPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        // Get all the cultivatedCropLandSeasonList where period does not contain
        defaultCultivatedCropLandSeasonFiltering("period.doesNotContain=" + UPDATED_PERIOD, "period.doesNotContain=" + DEFAULT_PERIOD);
    }

    private void defaultCultivatedCropLandSeasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedCropLandSeasonShouldBeFound(shouldBeFound);
        defaultCultivatedCropLandSeasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedCropLandSeasonShouldBeFound(String filter) throws Exception {
        restCultivatedCropLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCropLandSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));

        // Check, that the count call also returns 1
        restCultivatedCropLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedCropLandSeasonShouldNotBeFound(String filter) throws Exception {
        restCultivatedCropLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedCropLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedCropLandSeason() throws Exception {
        // Get the cultivatedCropLandSeason
        restCultivatedCropLandSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedCropLandSeason() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropLandSeason
        CultivatedCropLandSeason updatedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository
            .findById(cultivatedCropLandSeason.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedCropLandSeason are not directly saved in db
        em.detach(updatedCultivatedCropLandSeason);
        updatedCultivatedCropLandSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCultivatedCropLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedCropLandSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedCropLandSeason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedCropLandSeasonToMatchAllProperties(updatedCultivatedCropLandSeason);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropLandSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedCropLandSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedCropLandSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropLandSeason using partial update
        CultivatedCropLandSeason partialUpdatedCultivatedCropLandSeason = new CultivatedCropLandSeason();
        partialUpdatedCultivatedCropLandSeason.setId(cultivatedCropLandSeason.getId());

        partialUpdatedCultivatedCropLandSeason.name(UPDATED_NAME);

        restCultivatedCropLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCropLandSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCropLandSeason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropLandSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropLandSeasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedCropLandSeason, cultivatedCropLandSeason),
            getPersistedCultivatedCropLandSeason(cultivatedCropLandSeason)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedCropLandSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropLandSeason using partial update
        CultivatedCropLandSeason partialUpdatedCultivatedCropLandSeason = new CultivatedCropLandSeason();
        partialUpdatedCultivatedCropLandSeason.setId(cultivatedCropLandSeason.getId());

        partialUpdatedCultivatedCropLandSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCultivatedCropLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCropLandSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCropLandSeason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropLandSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropLandSeasonUpdatableFieldsEquals(
            partialUpdatedCultivatedCropLandSeason,
            getPersistedCultivatedCropLandSeason(partialUpdatedCultivatedCropLandSeason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropLandSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedCropLandSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedCropLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropLandSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCropLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedCropLandSeason() throws Exception {
        // Initialize the database
        insertedCultivatedCropLandSeason = cultivatedCropLandSeasonRepository.saveAndFlush(cultivatedCropLandSeason);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedCropLandSeason
        restCultivatedCropLandSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedCropLandSeason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedCropLandSeasonRepository.count();
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

    protected CultivatedCropLandSeason getPersistedCultivatedCropLandSeason(CultivatedCropLandSeason cultivatedCropLandSeason) {
        return cultivatedCropLandSeasonRepository.findById(cultivatedCropLandSeason.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedCropLandSeasonToMatchAllProperties(CultivatedCropLandSeason expectedCultivatedCropLandSeason) {
        assertCultivatedCropLandSeasonAllPropertiesEquals(
            expectedCultivatedCropLandSeason,
            getPersistedCultivatedCropLandSeason(expectedCultivatedCropLandSeason)
        );
    }

    protected void assertPersistedCultivatedCropLandSeasonToMatchUpdatableProperties(
        CultivatedCropLandSeason expectedCultivatedCropLandSeason
    ) {
        assertCultivatedCropLandSeasonAllUpdatablePropertiesEquals(
            expectedCultivatedCropLandSeason,
            getPersistedCultivatedCropLandSeason(expectedCultivatedCropLandSeason)
        );
    }
}
