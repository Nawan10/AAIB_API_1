package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListSeasonAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPayoutEventListSeason;
import lk.geoedge.interoperability.repository.IndexPayoutEventListSeasonRepository;
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
 * Integration tests for the {@link IndexPayoutEventListSeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPayoutEventListSeasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/index-payout-event-list-seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPayoutEventListSeasonMockMvc;

    private IndexPayoutEventListSeason indexPayoutEventListSeason;

    private IndexPayoutEventListSeason insertedIndexPayoutEventListSeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPayoutEventListSeason createEntity() {
        return new IndexPayoutEventListSeason().name(DEFAULT_NAME).period(DEFAULT_PERIOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPayoutEventListSeason createUpdatedEntity() {
        return new IndexPayoutEventListSeason().name(UPDATED_NAME).period(UPDATED_PERIOD);
    }

    @BeforeEach
    void initTest() {
        indexPayoutEventListSeason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPayoutEventListSeason != null) {
            indexPayoutEventListSeasonRepository.delete(insertedIndexPayoutEventListSeason);
            insertedIndexPayoutEventListSeason = null;
        }
    }

    @Test
    @Transactional
    void createIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPayoutEventListSeason
        var returnedIndexPayoutEventListSeason = om.readValue(
            restIndexPayoutEventListSeasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPayoutEventListSeason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPayoutEventListSeason.class
        );

        // Validate the IndexPayoutEventListSeason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPayoutEventListSeasonUpdatableFieldsEquals(
            returnedIndexPayoutEventListSeason,
            getPersistedIndexPayoutEventListSeason(returnedIndexPayoutEventListSeason)
        );

        insertedIndexPayoutEventListSeason = returnedIndexPayoutEventListSeason;
    }

    @Test
    @Transactional
    void createIndexPayoutEventListSeasonWithExistingId() throws Exception {
        // Create the IndexPayoutEventListSeason with an existing ID
        indexPayoutEventListSeason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasons() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList
        restIndexPayoutEventListSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventListSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    void getIndexPayoutEventListSeason() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get the indexPayoutEventListSeason
        restIndexPayoutEventListSeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPayoutEventListSeason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPayoutEventListSeason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD));
    }

    @Test
    @Transactional
    void getIndexPayoutEventListSeasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        Long id = indexPayoutEventListSeason.getId();

        defaultIndexPayoutEventListSeasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPayoutEventListSeasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPayoutEventListSeasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where name equals to
        defaultIndexPayoutEventListSeasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where name in
        defaultIndexPayoutEventListSeasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where name is not null
        defaultIndexPayoutEventListSeasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where name contains
        defaultIndexPayoutEventListSeasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where name does not contain
        defaultIndexPayoutEventListSeasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where period equals to
        defaultIndexPayoutEventListSeasonFiltering("period.equals=" + DEFAULT_PERIOD, "period.equals=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where period in
        defaultIndexPayoutEventListSeasonFiltering("period.in=" + DEFAULT_PERIOD + "," + UPDATED_PERIOD, "period.in=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where period is not null
        defaultIndexPayoutEventListSeasonFiltering("period.specified=true", "period.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where period contains
        defaultIndexPayoutEventListSeasonFiltering("period.contains=" + DEFAULT_PERIOD, "period.contains=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListSeasonsByPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        // Get all the indexPayoutEventListSeasonList where period does not contain
        defaultIndexPayoutEventListSeasonFiltering("period.doesNotContain=" + UPDATED_PERIOD, "period.doesNotContain=" + DEFAULT_PERIOD);
    }

    private void defaultIndexPayoutEventListSeasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPayoutEventListSeasonShouldBeFound(shouldBeFound);
        defaultIndexPayoutEventListSeasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPayoutEventListSeasonShouldBeFound(String filter) throws Exception {
        restIndexPayoutEventListSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventListSeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));

        // Check, that the count call also returns 1
        restIndexPayoutEventListSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPayoutEventListSeasonShouldNotBeFound(String filter) throws Exception {
        restIndexPayoutEventListSeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPayoutEventListSeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPayoutEventListSeason() throws Exception {
        // Get the indexPayoutEventListSeason
        restIndexPayoutEventListSeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPayoutEventListSeason() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListSeason
        IndexPayoutEventListSeason updatedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository
            .findById(indexPayoutEventListSeason.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPayoutEventListSeason are not directly saved in db
        em.detach(updatedIndexPayoutEventListSeason);
        updatedIndexPayoutEventListSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restIndexPayoutEventListSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPayoutEventListSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPayoutEventListSeason))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPayoutEventListSeasonToMatchAllProperties(updatedIndexPayoutEventListSeason);
    }

    @Test
    @Transactional
    void putNonExistingIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPayoutEventListSeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPayoutEventListSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListSeason using partial update
        IndexPayoutEventListSeason partialUpdatedIndexPayoutEventListSeason = new IndexPayoutEventListSeason();
        partialUpdatedIndexPayoutEventListSeason.setId(indexPayoutEventListSeason.getId());

        partialUpdatedIndexPayoutEventListSeason.period(UPDATED_PERIOD);

        restIndexPayoutEventListSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventListSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventListSeason))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListSeasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPayoutEventListSeason, indexPayoutEventListSeason),
            getPersistedIndexPayoutEventListSeason(indexPayoutEventListSeason)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPayoutEventListSeasonWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListSeason using partial update
        IndexPayoutEventListSeason partialUpdatedIndexPayoutEventListSeason = new IndexPayoutEventListSeason();
        partialUpdatedIndexPayoutEventListSeason.setId(indexPayoutEventListSeason.getId());

        partialUpdatedIndexPayoutEventListSeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restIndexPayoutEventListSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventListSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventListSeason))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListSeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListSeasonUpdatableFieldsEquals(
            partialUpdatedIndexPayoutEventListSeason,
            getPersistedIndexPayoutEventListSeason(partialUpdatedIndexPayoutEventListSeason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListSeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPayoutEventListSeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPayoutEventListSeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListSeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListSeasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListSeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventListSeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPayoutEventListSeason() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListSeason = indexPayoutEventListSeasonRepository.saveAndFlush(indexPayoutEventListSeason);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPayoutEventListSeason
        restIndexPayoutEventListSeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPayoutEventListSeason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPayoutEventListSeasonRepository.count();
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

    protected IndexPayoutEventListSeason getPersistedIndexPayoutEventListSeason(IndexPayoutEventListSeason indexPayoutEventListSeason) {
        return indexPayoutEventListSeasonRepository.findById(indexPayoutEventListSeason.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPayoutEventListSeasonToMatchAllProperties(
        IndexPayoutEventListSeason expectedIndexPayoutEventListSeason
    ) {
        assertIndexPayoutEventListSeasonAllPropertiesEquals(
            expectedIndexPayoutEventListSeason,
            getPersistedIndexPayoutEventListSeason(expectedIndexPayoutEventListSeason)
        );
    }

    protected void assertPersistedIndexPayoutEventListSeasonToMatchUpdatableProperties(
        IndexPayoutEventListSeason expectedIndexPayoutEventListSeason
    ) {
        assertIndexPayoutEventListSeasonAllUpdatablePropertiesEquals(
            expectedIndexPayoutEventListSeason,
            getPersistedIndexPayoutEventListSeason(expectedIndexPayoutEventListSeason)
        );
    }
}
