package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandSeasonAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import lk.geoedge.interoperability.repository.CultivatedLandSeasonRepository;
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
 * Integration tests for the {@link CultivatedLandSeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandSeasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandSeasonRepository cultivatedLandSeasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandSeasonMockMvc;

    private CultivatedLandSeason cultivatedLandSeason;

    private CultivatedLandSeason insertedCultivatedLandSeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandSeason createEntity() {
        return new CultivatedLandSeason().name(DEFAULT_NAME).period(DEFAULT_PERIOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandSeason createUpdatedEntity() {
        return new CultivatedLandSeason().name(UPDATED_NAME).period(UPDATED_PERIOD);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandSeason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandSeason != null) {
            cultivatedLandSeasonRepository.delete(insertedCultivatedLandSeason);
            insertedCultivatedLandSeason = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandSeason
        var returnedCultivatedLandSeason = om.readValue(
            restCultivatedLandSeasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandSeason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandSeason.class
        );

        // Validate the CultivatedLandSeason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandSeasonUpdatableFieldsEquals(
            returnedCultivatedLandSeason,
            getPersistedCultivatedLandSeason(returnedCultivatedLandSeason)
        );

        insertedCultivatedLandSeason = returnedCultivatedLandSeason;
    }

    @Test
    @Transactional
    void createCultivatedLandSeasonWithExistingId() throws Exception {
        // Create the CultivatedLandSeason with an existing ID
        cultivatedLandSeason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandSeasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasons() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList
        restCultivatedLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    void getCultivatedLandSeason() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get the cultivatedLandSeason
        restCultivatedLandSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandSeason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandSeason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD));
    }

    @Test
    @Transactional
    void getCultivatedLandSeasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        Long id = cultivatedLandSeason.getId();

        defaultCultivatedLandSeasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandSeasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandSeasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where name equals to
        defaultCultivatedLandSeasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where name in
        defaultCultivatedLandSeasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where name is not null
        defaultCultivatedLandSeasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where name contains
        defaultCultivatedLandSeasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where name does not contain
        defaultCultivatedLandSeasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where period equals to
        defaultCultivatedLandSeasonFiltering("period.equals=" + DEFAULT_PERIOD, "period.equals=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where period in
        defaultCultivatedLandSeasonFiltering("period.in=" + DEFAULT_PERIOD + "," + UPDATED_PERIOD, "period.in=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where period is not null
        defaultCultivatedLandSeasonFiltering("period.specified=true", "period.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where period contains
        defaultCultivatedLandSeasonFiltering("period.contains=" + DEFAULT_PERIOD, "period.contains=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCultivatedLandSeasonsByPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        // Get all the cultivatedLandSeasonList where period does not contain
        defaultCultivatedLandSeasonFiltering("period.doesNotContain=" + UPDATED_PERIOD, "period.doesNotContain=" + DEFAULT_PERIOD);
    }

    private void defaultCultivatedLandSeasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandSeasonShouldBeFound(shouldBeFound);
        defaultCultivatedLandSeasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandSeasonShouldBeFound(String filter) throws Exception {
        restCultivatedLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));

        // Check, that the count call also returns 1
        restCultivatedLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandSeasonShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandSeason() throws Exception {
        // Get the cultivatedLandSeason
        restCultivatedLandSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandSeason() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandSeason
        CultivatedLandSeason updatedCultivatedLandSeason = cultivatedLandSeasonRepository
            .findById(cultivatedLandSeason.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandSeason are not directly saved in db
        em.detach(updatedCultivatedLandSeason);
        updatedCultivatedLandSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCultivatedLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandSeason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandSeasonToMatchAllProperties(updatedCultivatedLandSeason);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandSeasonMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandSeason using partial update
        CultivatedLandSeason partialUpdatedCultivatedLandSeason = new CultivatedLandSeason();
        partialUpdatedCultivatedLandSeason.setId(cultivatedLandSeason.getId());

        partialUpdatedCultivatedLandSeason.name(UPDATED_NAME);

        restCultivatedLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandSeason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandSeasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandSeason, cultivatedLandSeason),
            getPersistedCultivatedLandSeason(cultivatedLandSeason)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandSeason using partial update
        CultivatedLandSeason partialUpdatedCultivatedLandSeason = new CultivatedLandSeason();
        partialUpdatedCultivatedLandSeason.setId(cultivatedLandSeason.getId());

        partialUpdatedCultivatedLandSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCultivatedLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandSeason))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandSeasonUpdatableFieldsEquals(
            partialUpdatedCultivatedLandSeason,
            getPersistedCultivatedLandSeason(partialUpdatedCultivatedLandSeason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandSeason() throws Exception {
        // Initialize the database
        insertedCultivatedLandSeason = cultivatedLandSeasonRepository.saveAndFlush(cultivatedLandSeason);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandSeason
        restCultivatedLandSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandSeason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandSeasonRepository.count();
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

    protected CultivatedLandSeason getPersistedCultivatedLandSeason(CultivatedLandSeason cultivatedLandSeason) {
        return cultivatedLandSeasonRepository.findById(cultivatedLandSeason.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandSeasonToMatchAllProperties(CultivatedLandSeason expectedCultivatedLandSeason) {
        assertCultivatedLandSeasonAllPropertiesEquals(
            expectedCultivatedLandSeason,
            getPersistedCultivatedLandSeason(expectedCultivatedLandSeason)
        );
    }

    protected void assertPersistedCultivatedLandSeasonToMatchUpdatableProperties(CultivatedLandSeason expectedCultivatedLandSeason) {
        assertCultivatedLandSeasonAllUpdatablePropertiesEquals(
            expectedCultivatedLandSeason,
            getPersistedCultivatedLandSeason(expectedCultivatedLandSeason)
        );
    }
}
