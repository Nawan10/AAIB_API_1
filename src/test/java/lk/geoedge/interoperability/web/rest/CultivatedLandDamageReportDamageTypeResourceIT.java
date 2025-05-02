package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportDamageTypeRepository;
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
 * Integration tests for the {@link CultivatedLandDamageReportDamageTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandDamageReportDamageTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-damage-report-damage-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandDamageReportDamageTypeRepository cultivatedLandDamageReportDamageTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandDamageReportDamageTypeMockMvc;

    private CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType;

    private CultivatedLandDamageReportDamageType insertedCultivatedLandDamageReportDamageType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReportDamageType createEntity() {
        return new CultivatedLandDamageReportDamageType().typeName(DEFAULT_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReportDamageType createUpdatedEntity() {
        return new CultivatedLandDamageReportDamageType().typeName(UPDATED_TYPE_NAME);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandDamageReportDamageType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandDamageReportDamageType != null) {
            cultivatedLandDamageReportDamageTypeRepository.delete(insertedCultivatedLandDamageReportDamageType);
            insertedCultivatedLandDamageReportDamageType = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandDamageReportDamageType
        var returnedCultivatedLandDamageReportDamageType = om.readValue(
            restCultivatedLandDamageReportDamageTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandDamageReportDamageType.class
        );

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandDamageReportDamageTypeUpdatableFieldsEquals(
            returnedCultivatedLandDamageReportDamageType,
            getPersistedCultivatedLandDamageReportDamageType(returnedCultivatedLandDamageReportDamageType)
        );

        insertedCultivatedLandDamageReportDamageType = returnedCultivatedLandDamageReportDamageType;
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReportDamageTypeWithExistingId() throws Exception {
        // Create the CultivatedLandDamageReportDamageType with an existing ID
        cultivatedLandDamageReportDamageType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageTypes() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get all the cultivatedLandDamageReportDamageTypeList
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReportDamageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReportDamageType() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get the cultivatedLandDamageReportDamageType
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandDamageReportDamageType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReportDamageTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        Long id = cultivatedLandDamageReportDamageType.getId();

        defaultCultivatedLandDamageReportDamageTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandDamageReportDamageTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandDamageReportDamageTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageTypesByTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get all the cultivatedLandDamageReportDamageTypeList where typeName equals to
        defaultCultivatedLandDamageReportDamageTypeFiltering(
            "typeName.equals=" + DEFAULT_TYPE_NAME,
            "typeName.equals=" + UPDATED_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageTypesByTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get all the cultivatedLandDamageReportDamageTypeList where typeName in
        defaultCultivatedLandDamageReportDamageTypeFiltering(
            "typeName.in=" + DEFAULT_TYPE_NAME + "," + UPDATED_TYPE_NAME,
            "typeName.in=" + UPDATED_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageTypesByTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get all the cultivatedLandDamageReportDamageTypeList where typeName is not null
        defaultCultivatedLandDamageReportDamageTypeFiltering("typeName.specified=true", "typeName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageTypesByTypeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get all the cultivatedLandDamageReportDamageTypeList where typeName contains
        defaultCultivatedLandDamageReportDamageTypeFiltering(
            "typeName.contains=" + DEFAULT_TYPE_NAME,
            "typeName.contains=" + UPDATED_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageTypesByTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        // Get all the cultivatedLandDamageReportDamageTypeList where typeName does not contain
        defaultCultivatedLandDamageReportDamageTypeFiltering(
            "typeName.doesNotContain=" + UPDATED_TYPE_NAME,
            "typeName.doesNotContain=" + DEFAULT_TYPE_NAME
        );
    }

    private void defaultCultivatedLandDamageReportDamageTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandDamageReportDamageTypeShouldBeFound(shouldBeFound);
        defaultCultivatedLandDamageReportDamageTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandDamageReportDamageTypeShouldBeFound(String filter) throws Exception {
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReportDamageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));

        // Check, that the count call also returns 1
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandDamageReportDamageTypeShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandDamageReportDamageType() throws Exception {
        // Get the cultivatedLandDamageReportDamageType
        restCultivatedLandDamageReportDamageTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandDamageReportDamageType() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReportDamageType
        CultivatedLandDamageReportDamageType updatedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository
            .findById(cultivatedLandDamageReportDamageType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandDamageReportDamageType are not directly saved in db
        em.detach(updatedCultivatedLandDamageReportDamageType);
        updatedCultivatedLandDamageReportDamageType.typeName(UPDATED_TYPE_NAME);

        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandDamageReportDamageType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandDamageReportDamageTypeToMatchAllProperties(updatedCultivatedLandDamageReportDamageType);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandDamageReportDamageTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReportDamageType using partial update
        CultivatedLandDamageReportDamageType partialUpdatedCultivatedLandDamageReportDamageType =
            new CultivatedLandDamageReportDamageType();
        partialUpdatedCultivatedLandDamageReportDamageType.setId(cultivatedLandDamageReportDamageType.getId());

        partialUpdatedCultivatedLandDamageReportDamageType.typeName(UPDATED_TYPE_NAME);

        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReportDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReportDamageType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReportDamageTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandDamageReportDamageType, cultivatedLandDamageReportDamageType),
            getPersistedCultivatedLandDamageReportDamageType(cultivatedLandDamageReportDamageType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandDamageReportDamageTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReportDamageType using partial update
        CultivatedLandDamageReportDamageType partialUpdatedCultivatedLandDamageReportDamageType =
            new CultivatedLandDamageReportDamageType();
        partialUpdatedCultivatedLandDamageReportDamageType.setId(cultivatedLandDamageReportDamageType.getId());

        partialUpdatedCultivatedLandDamageReportDamageType.typeName(UPDATED_TYPE_NAME);

        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReportDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReportDamageType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReportDamageTypeUpdatableFieldsEquals(
            partialUpdatedCultivatedLandDamageReportDamageType,
            getPersistedCultivatedLandDamageReportDamageType(partialUpdatedCultivatedLandDamageReportDamageType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandDamageReportDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReportDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandDamageReportDamageType() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReportDamageType
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandDamageReportDamageType
        restCultivatedLandDamageReportDamageTypeMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandDamageReportDamageTypeRepository.count();
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

    protected CultivatedLandDamageReportDamageType getPersistedCultivatedLandDamageReportDamageType(
        CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType
    ) {
        return cultivatedLandDamageReportDamageTypeRepository.findById(cultivatedLandDamageReportDamageType.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandDamageReportDamageTypeToMatchAllProperties(
        CultivatedLandDamageReportDamageType expectedCultivatedLandDamageReportDamageType
    ) {
        assertCultivatedLandDamageReportDamageTypeAllPropertiesEquals(
            expectedCultivatedLandDamageReportDamageType,
            getPersistedCultivatedLandDamageReportDamageType(expectedCultivatedLandDamageReportDamageType)
        );
    }

    protected void assertPersistedCultivatedLandDamageReportDamageTypeToMatchUpdatableProperties(
        CultivatedLandDamageReportDamageType expectedCultivatedLandDamageReportDamageType
    ) {
        assertCultivatedLandDamageReportDamageTypeAllUpdatablePropertiesEquals(
            expectedCultivatedLandDamageReportDamageType,
            getPersistedCultivatedLandDamageReportDamageType(expectedCultivatedLandDamageReportDamageType)
        );
    }
}
