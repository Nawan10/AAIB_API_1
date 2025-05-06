package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPolicySeasonAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPolicySeason;
import lk.geoedge.interoperability.repository.IndexPolicySeasonRepository;
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
 * Integration tests for the {@link IndexPolicySeasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPolicySeasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_PERIOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/index-policy-seasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPolicySeasonRepository indexPolicySeasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPolicySeasonMockMvc;

    private IndexPolicySeason indexPolicySeason;

    private IndexPolicySeason insertedIndexPolicySeason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicySeason createEntity() {
        return new IndexPolicySeason().name(DEFAULT_NAME).period(DEFAULT_PERIOD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicySeason createUpdatedEntity() {
        return new IndexPolicySeason().name(UPDATED_NAME).period(UPDATED_PERIOD);
    }

    @BeforeEach
    void initTest() {
        indexPolicySeason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPolicySeason != null) {
            indexPolicySeasonRepository.delete(insertedIndexPolicySeason);
            insertedIndexPolicySeason = null;
        }
    }

    @Test
    @Transactional
    void createIndexPolicySeason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPolicySeason
        var returnedIndexPolicySeason = om.readValue(
            restIndexPolicySeasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPolicySeason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPolicySeason.class
        );

        // Validate the IndexPolicySeason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPolicySeasonUpdatableFieldsEquals(returnedIndexPolicySeason, getPersistedIndexPolicySeason(returnedIndexPolicySeason));

        insertedIndexPolicySeason = returnedIndexPolicySeason;
    }

    @Test
    @Transactional
    void createIndexPolicySeasonWithExistingId() throws Exception {
        // Create the IndexPolicySeason with an existing ID
        indexPolicySeason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPolicySeasonMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasons() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList
        restIndexPolicySeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicySeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));
    }

    @Test
    @Transactional
    void getIndexPolicySeason() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get the indexPolicySeason
        restIndexPolicySeasonMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPolicySeason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPolicySeason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD));
    }

    @Test
    @Transactional
    void getIndexPolicySeasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        Long id = indexPolicySeason.getId();

        defaultIndexPolicySeasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPolicySeasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPolicySeasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where name equals to
        defaultIndexPolicySeasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where name in
        defaultIndexPolicySeasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where name is not null
        defaultIndexPolicySeasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where name contains
        defaultIndexPolicySeasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where name does not contain
        defaultIndexPolicySeasonFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where period equals to
        defaultIndexPolicySeasonFiltering("period.equals=" + DEFAULT_PERIOD, "period.equals=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where period in
        defaultIndexPolicySeasonFiltering("period.in=" + DEFAULT_PERIOD + "," + UPDATED_PERIOD, "period.in=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where period is not null
        defaultIndexPolicySeasonFiltering("period.specified=true", "period.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByPeriodContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where period contains
        defaultIndexPolicySeasonFiltering("period.contains=" + DEFAULT_PERIOD, "period.contains=" + UPDATED_PERIOD);
    }

    @Test
    @Transactional
    void getAllIndexPolicySeasonsByPeriodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        // Get all the indexPolicySeasonList where period does not contain
        defaultIndexPolicySeasonFiltering("period.doesNotContain=" + UPDATED_PERIOD, "period.doesNotContain=" + DEFAULT_PERIOD);
    }

    private void defaultIndexPolicySeasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPolicySeasonShouldBeFound(shouldBeFound);
        defaultIndexPolicySeasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPolicySeasonShouldBeFound(String filter) throws Exception {
        restIndexPolicySeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicySeason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)));

        // Check, that the count call also returns 1
        restIndexPolicySeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPolicySeasonShouldNotBeFound(String filter) throws Exception {
        restIndexPolicySeasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPolicySeasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPolicySeason() throws Exception {
        // Get the indexPolicySeason
        restIndexPolicySeasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPolicySeason() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicySeason
        IndexPolicySeason updatedIndexPolicySeason = indexPolicySeasonRepository.findById(indexPolicySeason.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPolicySeason are not directly saved in db
        em.detach(updatedIndexPolicySeason);
        updatedIndexPolicySeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restIndexPolicySeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPolicySeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPolicySeason))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPolicySeasonToMatchAllProperties(updatedIndexPolicySeason);
    }

    @Test
    @Transactional
    void putNonExistingIndexPolicySeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicySeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicySeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPolicySeason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPolicySeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicySeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicySeasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPolicySeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicySeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicySeasonMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPolicySeasonWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicySeason using partial update
        IndexPolicySeason partialUpdatedIndexPolicySeason = new IndexPolicySeason();
        partialUpdatedIndexPolicySeason.setId(indexPolicySeason.getId());

        partialUpdatedIndexPolicySeason.name(UPDATED_NAME);

        restIndexPolicySeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicySeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicySeason))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicySeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicySeasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPolicySeason, indexPolicySeason),
            getPersistedIndexPolicySeason(indexPolicySeason)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPolicySeasonWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicySeason using partial update
        IndexPolicySeason partialUpdatedIndexPolicySeason = new IndexPolicySeason();
        partialUpdatedIndexPolicySeason.setId(indexPolicySeason.getId());

        partialUpdatedIndexPolicySeason.name(UPDATED_NAME).period(UPDATED_PERIOD);

        restIndexPolicySeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicySeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicySeason))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicySeason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicySeasonUpdatableFieldsEquals(
            partialUpdatedIndexPolicySeason,
            getPersistedIndexPolicySeason(partialUpdatedIndexPolicySeason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPolicySeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicySeason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicySeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPolicySeason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPolicySeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicySeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicySeasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPolicySeason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicySeason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicySeasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicySeason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicySeason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPolicySeason() throws Exception {
        // Initialize the database
        insertedIndexPolicySeason = indexPolicySeasonRepository.saveAndFlush(indexPolicySeason);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPolicySeason
        restIndexPolicySeasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPolicySeason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPolicySeasonRepository.count();
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

    protected IndexPolicySeason getPersistedIndexPolicySeason(IndexPolicySeason indexPolicySeason) {
        return indexPolicySeasonRepository.findById(indexPolicySeason.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPolicySeasonToMatchAllProperties(IndexPolicySeason expectedIndexPolicySeason) {
        assertIndexPolicySeasonAllPropertiesEquals(expectedIndexPolicySeason, getPersistedIndexPolicySeason(expectedIndexPolicySeason));
    }

    protected void assertPersistedIndexPolicySeasonToMatchUpdatableProperties(IndexPolicySeason expectedIndexPolicySeason) {
        assertIndexPolicySeasonAllUpdatablePropertiesEquals(
            expectedIndexPolicySeason,
            getPersistedIndexPolicySeason(expectedIndexPolicySeason)
        );
    }
}
