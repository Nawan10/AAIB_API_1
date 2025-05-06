package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CanlendarCropSeasonAsserts.*;
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
import lk.geoedge.interoperability.domain.CanlendarCropSeason;
import lk.geoedge.interoperability.repository.CanlendarCropSeasonRepository;
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
 * Integration tests for the {@link CanlendarCropSeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CanlendarCropSeasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/canlendar-crop-seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CanlendarCropSeasonRepository canlendarCropSeasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCanlendarCropSeasonMockMvc;

    private CanlendarCropSeason canlendarCropSeason;

    private CanlendarCropSeason insertedCanlendarCropSeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanlendarCropSeason createEntity() {
        return new CanlendarCropSeason().name(DEFAULT_NAME).period(DEFAULT_PERIOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanlendarCropSeason createUpdatedEntity() {
        return new CanlendarCropSeason().name(UPDATED_NAME).period(UPDATED_PERIOD);
    }

    @BeforeEach
    void initTest() {
        canlendarCropSeason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCanlendarCropSeason != null) {
            canlendarCropSeasonRepository.delete(insertedCanlendarCropSeason);
            insertedCanlendarCropSeason = null;
        }
    }

    @Test
    @Transactional
    void createCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CanlendarCropSeason
        var returnedCanlendarCropSeason = om.readValue(
            restCanlendarCropSeasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(canlendarCropSeason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CanlendarCropSeason.class
        );

        // Validate the CanlendarCropSeason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCanlendarCropSeasonUpdatableFieldsEquals(
            returnedCanlendarCropSeason,
            getPersistedCanlendarCropSeason(returnedCanlendarCropSeason)
        );

        insertedCanlendarCropSeason = returnedCanlendarCropSeason;
    }

    @Test
    @Transactional
    void createCanlendarCropSeasonWithExistingId() throws Exception {
        // Create the CanlendarCropSeason with an existing ID
        canlendarCropSeason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCanlendarCropSeasonMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasons() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList
        restCanlendarCropSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canlendarCropSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    void getCanlendarCropSeason() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get the canlendarCropSeason
        restCanlendarCropSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, canlendarCropSeason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(canlendarCropSeason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD));
    }

    @Test
    @Transactional
    void getCanlendarCropSeasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        Long id = canlendarCropSeason.getId();

        defaultCanlendarCropSeasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCanlendarCropSeasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCanlendarCropSeasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where name equals to
        defaultCanlendarCropSeasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where name in
        defaultCanlendarCropSeasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where name is not null
        defaultCanlendarCropSeasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where name contains
        defaultCanlendarCropSeasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where name does not contain
        defaultCanlendarCropSeasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where period equals to
        defaultCanlendarCropSeasonFiltering("period.equals=" + DEFAULT_PERIOD, "period.equals=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where period in
        defaultCanlendarCropSeasonFiltering("period.in=" + DEFAULT_PERIOD + "," + UPDATED_PERIOD, "period.in=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where period is not null
        defaultCanlendarCropSeasonFiltering("period.specified=true", "period.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where period contains
        defaultCanlendarCropSeasonFiltering("period.contains=" + DEFAULT_PERIOD, "period.contains=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllCanlendarCropSeasonsByPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        // Get all the canlendarCropSeasonList where period does not contain
        defaultCanlendarCropSeasonFiltering("period.doesNotContain=" + UPDATED_PERIOD, "period.doesNotContain=" + DEFAULT_PERIOD);
    }

    private void defaultCanlendarCropSeasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCanlendarCropSeasonShouldBeFound(shouldBeFound);
        defaultCanlendarCropSeasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCanlendarCropSeasonShouldBeFound(String filter) throws Exception {
        restCanlendarCropSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canlendarCropSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));

        // Check, that the count call also returns 1
        restCanlendarCropSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCanlendarCropSeasonShouldNotBeFound(String filter) throws Exception {
        restCanlendarCropSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCanlendarCropSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCanlendarCropSeason() throws Exception {
        // Get the canlendarCropSeason
        restCanlendarCropSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCanlendarCropSeason() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCropSeason
        CanlendarCropSeason updatedCanlendarCropSeason = canlendarCropSeasonRepository.findById(canlendarCropSeason.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCanlendarCropSeason are not directly saved in db
        em.detach(updatedCanlendarCropSeason);
        updatedCanlendarCropSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCanlendarCropSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCanlendarCropSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCanlendarCropSeason))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCanlendarCropSeasonToMatchAllProperties(updatedCanlendarCropSeason);
    }

    @Test
    @Transactional
    void putNonExistingCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanlendarCropSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canlendarCropSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropSeasonMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCanlendarCropSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCropSeason using partial update
        CanlendarCropSeason partialUpdatedCanlendarCropSeason = new CanlendarCropSeason();
        partialUpdatedCanlendarCropSeason.setId(canlendarCropSeason.getId());

        partialUpdatedCanlendarCropSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCanlendarCropSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanlendarCropSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanlendarCropSeason))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCropSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanlendarCropSeasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCanlendarCropSeason, canlendarCropSeason),
            getPersistedCanlendarCropSeason(canlendarCropSeason)
        );
    }

    @Test
    @Transactional
    void fullUpdateCanlendarCropSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCropSeason using partial update
        CanlendarCropSeason partialUpdatedCanlendarCropSeason = new CanlendarCropSeason();
        partialUpdatedCanlendarCropSeason.setId(canlendarCropSeason.getId());

        partialUpdatedCanlendarCropSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restCanlendarCropSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanlendarCropSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanlendarCropSeason))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCropSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanlendarCropSeasonUpdatableFieldsEquals(
            partialUpdatedCanlendarCropSeason,
            getPersistedCanlendarCropSeason(partialUpdatedCanlendarCropSeason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanlendarCropSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, canlendarCropSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCanlendarCropSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCropSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanlendarCropSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCanlendarCropSeason() throws Exception {
        // Initialize the database
        insertedCanlendarCropSeason = canlendarCropSeasonRepository.saveAndFlush(canlendarCropSeason);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the canlendarCropSeason
        restCanlendarCropSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, canlendarCropSeason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return canlendarCropSeasonRepository.count();
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

    protected CanlendarCropSeason getPersistedCanlendarCropSeason(CanlendarCropSeason canlendarCropSeason) {
        return canlendarCropSeasonRepository.findById(canlendarCropSeason.getId()).orElseThrow();
    }

    protected void assertPersistedCanlendarCropSeasonToMatchAllProperties(CanlendarCropSeason expectedCanlendarCropSeason) {
        assertCanlendarCropSeasonAllPropertiesEquals(
            expectedCanlendarCropSeason,
            getPersistedCanlendarCropSeason(expectedCanlendarCropSeason)
        );
    }

    protected void assertPersistedCanlendarCropSeasonToMatchUpdatableProperties(CanlendarCropSeason expectedCanlendarCropSeason) {
        assertCanlendarCropSeasonAllUpdatablePropertiesEquals(
            expectedCanlendarCropSeason,
            getPersistedCanlendarCropSeason(expectedCanlendarCropSeason)
        );
    }
}
