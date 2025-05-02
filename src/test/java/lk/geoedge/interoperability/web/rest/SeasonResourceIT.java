package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.SeasonAsserts.*;
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
import lk.geoedge.interoperability.domain.Season;
import lk.geoedge.interoperability.repository.SeasonRepository;
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
 * Integration tests for the {@link SeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeasonMockMvc;

    private Season season;

    private Season insertedSeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createEntity() {
        return new Season().name(DEFAULT_NAME).period(DEFAULT_PERIOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createUpdatedEntity() {
        return new Season().name(UPDATED_NAME).period(UPDATED_PERIOD);
    }

    @BeforeEach
    void initTest() {
        season = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSeason != null) {
            seasonRepository.delete(insertedSeason);
            insertedSeason = null;
        }
    }

    @Test
    @Transactional
    void createSeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Season
        var returnedSeason = om.readValue(
            restSeasonMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(season)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Season.class
        );

        // Validate the Season in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSeasonUpdatableFieldsEquals(returnedSeason, getPersistedSeason(returnedSeason));

        insertedSeason = returnedSeason;
    }

    @Test
    @Transactional
    void createSeasonWithExistingId() throws Exception {
        // Create the Season with an existing ID
        season.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeasonMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(season)))
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeasons() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    void getSeason() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get the season
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, season.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(season.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD));
    }

    @Test
    @Transactional
    void getSeasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        Long id = season.getId();

        defaultSeasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSeasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSeasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSeasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where name equals to
        defaultSeasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSeasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where name in
        defaultSeasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSeasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where name is not null
        defaultSeasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSeasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where name contains
        defaultSeasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSeasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where name does not contain
        defaultSeasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSeasonsByPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where period equals to
        defaultSeasonFiltering("period.equals=" + DEFAULT_PERIOD, "period.equals=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllSeasonsByPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where period in
        defaultSeasonFiltering("period.in=" + DEFAULT_PERIOD + "," + UPDATED_PERIOD, "period.in=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllSeasonsByPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where period is not null
        defaultSeasonFiltering("period.specified=true", "period.specified=false");
    }

    @Test
    @Transactional
    void getAllSeasonsByPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where period contains
        defaultSeasonFiltering("period.contains=" + DEFAULT_PERIOD, "period.contains=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllSeasonsByPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        // Get all the seasonList where period does not contain
        defaultSeasonFiltering("period.doesNotContain=" + UPDATED_PERIOD, "period.doesNotContain=" + DEFAULT_PERIOD);
    }

    private void defaultSeasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSeasonShouldBeFound(shouldBeFound);
        defaultSeasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSeasonShouldBeFound(String filter) throws Exception {
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));

        // Check, that the count call also returns 1
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSeasonShouldNotBeFound(String filter) throws Exception {
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeason() throws Exception {
        // Get the season
        restSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeason() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the season
        Season updatedSeason = seasonRepository.findById(season.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeason are not directly saved in db
        em.detach(updatedSeason);
        updatedSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSeasonToMatchAllProperties(updatedSeason);
    }

    @Test
    @Transactional
    void putNonExistingSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, season.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(season))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(season))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(season)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the season using partial update
        Season partialUpdatedSeason = new Season();
        partialUpdatedSeason.setId(season.getId());

        partialUpdatedSeason.period(UPDATED_PERIOD);

        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeasonUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSeason, season), getPersistedSeason(season));
    }

    @Test
    @Transactional
    void fullUpdateSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the season using partial update
        Season partialUpdatedSeason = new Season();
        partialUpdatedSeason.setId(season.getId());

        partialUpdatedSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeason))
            )
            .andExpect(status().isOk());

        // Validate the Season in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeasonUpdatableFieldsEquals(partialUpdatedSeason, getPersistedSeason(partialUpdatedSeason));
    }

    @Test
    @Transactional
    void patchNonExistingSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, season.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(season))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(season))
            )
            .andExpect(status().isBadRequest());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        season.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeasonMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(season)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Season in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeason() throws Exception {
        // Initialize the database
        insertedSeason = seasonRepository.saveAndFlush(season);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the season
        restSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, season.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return seasonRepository.count();
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

    protected Season getPersistedSeason(Season season) {
        return seasonRepository.findById(season.getId()).orElseThrow();
    }

    protected void assertPersistedSeasonToMatchAllProperties(Season expectedSeason) {
        assertSeasonAllPropertiesEquals(expectedSeason, getPersistedSeason(expectedSeason));
    }

    protected void assertPersistedSeasonToMatchUpdatableProperties(Season expectedSeason) {
        assertSeasonAllUpdatablePropertiesEquals(expectedSeason, getPersistedSeason(expectedSeason));
    }
}
