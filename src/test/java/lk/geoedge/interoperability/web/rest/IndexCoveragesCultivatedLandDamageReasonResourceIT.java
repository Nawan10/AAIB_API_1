package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReasonAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.IndexCoveragesCultivatedLandDamageReasonRepository;
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
 * Integration tests for the {@link IndexCoveragesCultivatedLandDamageReasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexCoveragesCultivatedLandDamageReasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/index-coverages-cultivated-land-damage-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexCoveragesCultivatedLandDamageReasonMockMvc;

    private IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason;

    private IndexCoveragesCultivatedLandDamageReason insertedIndexCoveragesCultivatedLandDamageReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexCoveragesCultivatedLandDamageReason createEntity() {
        return new IndexCoveragesCultivatedLandDamageReason().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexCoveragesCultivatedLandDamageReason createUpdatedEntity() {
        return new IndexCoveragesCultivatedLandDamageReason().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        indexCoveragesCultivatedLandDamageReason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexCoveragesCultivatedLandDamageReason != null) {
            indexCoveragesCultivatedLandDamageReasonRepository.delete(insertedIndexCoveragesCultivatedLandDamageReason);
            insertedIndexCoveragesCultivatedLandDamageReason = null;
        }
    }

    @Test
    @Transactional
    void createIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexCoveragesCultivatedLandDamageReason
        var returnedIndexCoveragesCultivatedLandDamageReason = om.readValue(
            restIndexCoveragesCultivatedLandDamageReasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexCoveragesCultivatedLandDamageReason.class
        );

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexCoveragesCultivatedLandDamageReasonUpdatableFieldsEquals(
            returnedIndexCoveragesCultivatedLandDamageReason,
            getPersistedIndexCoveragesCultivatedLandDamageReason(returnedIndexCoveragesCultivatedLandDamageReason)
        );

        insertedIndexCoveragesCultivatedLandDamageReason = returnedIndexCoveragesCultivatedLandDamageReason;
    }

    @Test
    @Transactional
    void createIndexCoveragesCultivatedLandDamageReasonWithExistingId() throws Exception {
        // Create the IndexCoveragesCultivatedLandDamageReason with an existing ID
        indexCoveragesCultivatedLandDamageReason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesCultivatedLandDamageReasons() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get all the indexCoveragesCultivatedLandDamageReasonList
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexCoveragesCultivatedLandDamageReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIndexCoveragesCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get the indexCoveragesCultivatedLandDamageReason
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, indexCoveragesCultivatedLandDamageReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexCoveragesCultivatedLandDamageReason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getIndexCoveragesCultivatedLandDamageReasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        Long id = indexCoveragesCultivatedLandDamageReason.getId();

        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesCultivatedLandDamageReasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get all the indexCoveragesCultivatedLandDamageReasonList where name equals to
        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesCultivatedLandDamageReasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get all the indexCoveragesCultivatedLandDamageReasonList where name in
        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesCultivatedLandDamageReasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get all the indexCoveragesCultivatedLandDamageReasonList where name is not null
        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexCoveragesCultivatedLandDamageReasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get all the indexCoveragesCultivatedLandDamageReasonList where name contains
        defaultIndexCoveragesCultivatedLandDamageReasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesCultivatedLandDamageReasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        // Get all the indexCoveragesCultivatedLandDamageReasonList where name does not contain
        defaultIndexCoveragesCultivatedLandDamageReasonFiltering(
            "name.doesNotContain=" + UPDATED_NAME,
            "name.doesNotContain=" + DEFAULT_NAME
        );
    }

    private void defaultIndexCoveragesCultivatedLandDamageReasonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexCoveragesCultivatedLandDamageReasonShouldBeFound(shouldBeFound);
        defaultIndexCoveragesCultivatedLandDamageReasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexCoveragesCultivatedLandDamageReasonShouldBeFound(String filter) throws Exception {
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexCoveragesCultivatedLandDamageReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexCoveragesCultivatedLandDamageReasonShouldNotBeFound(String filter) throws Exception {
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexCoveragesCultivatedLandDamageReason() throws Exception {
        // Get the indexCoveragesCultivatedLandDamageReason
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexCoveragesCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexCoveragesCultivatedLandDamageReason
        IndexCoveragesCultivatedLandDamageReason updatedIndexCoveragesCultivatedLandDamageReason =
            indexCoveragesCultivatedLandDamageReasonRepository.findById(indexCoveragesCultivatedLandDamageReason.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndexCoveragesCultivatedLandDamageReason are not directly saved in db
        em.detach(updatedIndexCoveragesCultivatedLandDamageReason);
        updatedIndexCoveragesCultivatedLandDamageReason.name(UPDATED_NAME);

        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexCoveragesCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexCoveragesCultivatedLandDamageReasonToMatchAllProperties(updatedIndexCoveragesCultivatedLandDamageReason);
    }

    @Test
    @Transactional
    void putNonExistingIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoveragesCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexCoveragesCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoveragesCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoveragesCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexCoveragesCultivatedLandDamageReasonWithPatch() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexCoveragesCultivatedLandDamageReason using partial update
        IndexCoveragesCultivatedLandDamageReason partialUpdatedIndexCoveragesCultivatedLandDamageReason =
            new IndexCoveragesCultivatedLandDamageReason();
        partialUpdatedIndexCoveragesCultivatedLandDamageReason.setId(indexCoveragesCultivatedLandDamageReason.getId());

        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexCoveragesCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexCoveragesCultivatedLandDamageReasonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexCoveragesCultivatedLandDamageReason, indexCoveragesCultivatedLandDamageReason),
            getPersistedIndexCoveragesCultivatedLandDamageReason(indexCoveragesCultivatedLandDamageReason)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexCoveragesCultivatedLandDamageReasonWithPatch() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexCoveragesCultivatedLandDamageReason using partial update
        IndexCoveragesCultivatedLandDamageReason partialUpdatedIndexCoveragesCultivatedLandDamageReason =
            new IndexCoveragesCultivatedLandDamageReason();
        partialUpdatedIndexCoveragesCultivatedLandDamageReason.setId(indexCoveragesCultivatedLandDamageReason.getId());

        partialUpdatedIndexCoveragesCultivatedLandDamageReason.name(UPDATED_NAME);

        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexCoveragesCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexCoveragesCultivatedLandDamageReasonUpdatableFieldsEquals(
            partialUpdatedIndexCoveragesCultivatedLandDamageReason,
            getPersistedIndexCoveragesCultivatedLandDamageReason(partialUpdatedIndexCoveragesCultivatedLandDamageReason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoveragesCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexCoveragesCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoveragesCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexCoveragesCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoveragesCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexCoveragesCultivatedLandDamageReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexCoveragesCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexCoveragesCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedIndexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonRepository.saveAndFlush(
            indexCoveragesCultivatedLandDamageReason
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexCoveragesCultivatedLandDamageReason
        restIndexCoveragesCultivatedLandDamageReasonMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, indexCoveragesCultivatedLandDamageReason.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexCoveragesCultivatedLandDamageReasonRepository.count();
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

    protected IndexCoveragesCultivatedLandDamageReason getPersistedIndexCoveragesCultivatedLandDamageReason(
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) {
        return indexCoveragesCultivatedLandDamageReasonRepository.findById(indexCoveragesCultivatedLandDamageReason.getId()).orElseThrow();
    }

    protected void assertPersistedIndexCoveragesCultivatedLandDamageReasonToMatchAllProperties(
        IndexCoveragesCultivatedLandDamageReason expectedIndexCoveragesCultivatedLandDamageReason
    ) {
        assertIndexCoveragesCultivatedLandDamageReasonAllPropertiesEquals(
            expectedIndexCoveragesCultivatedLandDamageReason,
            getPersistedIndexCoveragesCultivatedLandDamageReason(expectedIndexCoveragesCultivatedLandDamageReason)
        );
    }

    protected void assertPersistedIndexCoveragesCultivatedLandDamageReasonToMatchUpdatableProperties(
        IndexCoveragesCultivatedLandDamageReason expectedIndexCoveragesCultivatedLandDamageReason
    ) {
        assertIndexCoveragesCultivatedLandDamageReasonAllUpdatablePropertiesEquals(
            expectedIndexCoveragesCultivatedLandDamageReason,
            getPersistedIndexCoveragesCultivatedLandDamageReason(expectedIndexCoveragesCultivatedLandDamageReason)
        );
    }
}
