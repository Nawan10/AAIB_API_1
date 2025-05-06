package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageTypeRepository;
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
 * Integration tests for the {@link CultivatedLandDamageReasonDamageTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandDamageReasonDamageTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-damage-reason-damage-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandDamageReasonDamageTypeRepository cultivatedLandDamageReasonDamageTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandDamageReasonDamageTypeMockMvc;

    private CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType;

    private CultivatedLandDamageReasonDamageType insertedCultivatedLandDamageReasonDamageType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReasonDamageType createEntity() {
        return new CultivatedLandDamageReasonDamageType().typeName(DEFAULT_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReasonDamageType createUpdatedEntity() {
        return new CultivatedLandDamageReasonDamageType().typeName(UPDATED_TYPE_NAME);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandDamageReasonDamageType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandDamageReasonDamageType != null) {
            cultivatedLandDamageReasonDamageTypeRepository.delete(insertedCultivatedLandDamageReasonDamageType);
            insertedCultivatedLandDamageReasonDamageType = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandDamageReasonDamageType
        var returnedCultivatedLandDamageReasonDamageType = om.readValue(
            restCultivatedLandDamageReasonDamageTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandDamageReasonDamageType.class
        );

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandDamageReasonDamageTypeUpdatableFieldsEquals(
            returnedCultivatedLandDamageReasonDamageType,
            getPersistedCultivatedLandDamageReasonDamageType(returnedCultivatedLandDamageReasonDamageType)
        );

        insertedCultivatedLandDamageReasonDamageType = returnedCultivatedLandDamageReasonDamageType;
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReasonDamageTypeWithExistingId() throws Exception {
        // Create the CultivatedLandDamageReasonDamageType with an existing ID
        cultivatedLandDamageReasonDamageType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageTypes() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get all the cultivatedLandDamageReasonDamageTypeList
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReasonDamageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReasonDamageType() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get the cultivatedLandDamageReasonDamageType
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandDamageReasonDamageType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReasonDamageTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        Long id = cultivatedLandDamageReasonDamageType.getId();

        defaultCultivatedLandDamageReasonDamageTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandDamageReasonDamageTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandDamageReasonDamageTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageTypesByTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get all the cultivatedLandDamageReasonDamageTypeList where typeName equals to
        defaultCultivatedLandDamageReasonDamageTypeFiltering(
            "typeName.equals=" + DEFAULT_TYPE_NAME,
            "typeName.equals=" + UPDATED_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageTypesByTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get all the cultivatedLandDamageReasonDamageTypeList where typeName in
        defaultCultivatedLandDamageReasonDamageTypeFiltering(
            "typeName.in=" + DEFAULT_TYPE_NAME + "," + UPDATED_TYPE_NAME,
            "typeName.in=" + UPDATED_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageTypesByTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get all the cultivatedLandDamageReasonDamageTypeList where typeName is not null
        defaultCultivatedLandDamageReasonDamageTypeFiltering("typeName.specified=true", "typeName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageTypesByTypeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get all the cultivatedLandDamageReasonDamageTypeList where typeName contains
        defaultCultivatedLandDamageReasonDamageTypeFiltering(
            "typeName.contains=" + DEFAULT_TYPE_NAME,
            "typeName.contains=" + UPDATED_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageTypesByTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        // Get all the cultivatedLandDamageReasonDamageTypeList where typeName does not contain
        defaultCultivatedLandDamageReasonDamageTypeFiltering(
            "typeName.doesNotContain=" + UPDATED_TYPE_NAME,
            "typeName.doesNotContain=" + DEFAULT_TYPE_NAME
        );
    }

    private void defaultCultivatedLandDamageReasonDamageTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandDamageReasonDamageTypeShouldBeFound(shouldBeFound);
        defaultCultivatedLandDamageReasonDamageTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandDamageReasonDamageTypeShouldBeFound(String filter) throws Exception {
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReasonDamageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));

        // Check, that the count call also returns 1
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandDamageReasonDamageTypeShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandDamageReasonDamageType() throws Exception {
        // Get the cultivatedLandDamageReasonDamageType
        restCultivatedLandDamageReasonDamageTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandDamageReasonDamageType() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReasonDamageType
        CultivatedLandDamageReasonDamageType updatedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository
            .findById(cultivatedLandDamageReasonDamageType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandDamageReasonDamageType are not directly saved in db
        em.detach(updatedCultivatedLandDamageReasonDamageType);
        updatedCultivatedLandDamageReasonDamageType.typeName(UPDATED_TYPE_NAME);

        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandDamageReasonDamageType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandDamageReasonDamageTypeToMatchAllProperties(updatedCultivatedLandDamageReasonDamageType);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandDamageReasonDamageTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReasonDamageType using partial update
        CultivatedLandDamageReasonDamageType partialUpdatedCultivatedLandDamageReasonDamageType =
            new CultivatedLandDamageReasonDamageType();
        partialUpdatedCultivatedLandDamageReasonDamageType.setId(cultivatedLandDamageReasonDamageType.getId());

        partialUpdatedCultivatedLandDamageReasonDamageType.typeName(UPDATED_TYPE_NAME);

        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReasonDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReasonDamageType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReasonDamageTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandDamageReasonDamageType, cultivatedLandDamageReasonDamageType),
            getPersistedCultivatedLandDamageReasonDamageType(cultivatedLandDamageReasonDamageType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandDamageReasonDamageTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReasonDamageType using partial update
        CultivatedLandDamageReasonDamageType partialUpdatedCultivatedLandDamageReasonDamageType =
            new CultivatedLandDamageReasonDamageType();
        partialUpdatedCultivatedLandDamageReasonDamageType.setId(cultivatedLandDamageReasonDamageType.getId());

        partialUpdatedCultivatedLandDamageReasonDamageType.typeName(UPDATED_TYPE_NAME);

        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReasonDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReasonDamageType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReasonDamageTypeUpdatableFieldsEquals(
            partialUpdatedCultivatedLandDamageReasonDamageType,
            getPersistedCultivatedLandDamageReasonDamageType(partialUpdatedCultivatedLandDamageReasonDamageType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandDamageReasonDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReasonDamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandDamageReasonDamageType() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageType
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandDamageReasonDamageType
        restCultivatedLandDamageReasonDamageTypeMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandDamageReasonDamageTypeRepository.count();
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

    protected CultivatedLandDamageReasonDamageType getPersistedCultivatedLandDamageReasonDamageType(
        CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType
    ) {
        return cultivatedLandDamageReasonDamageTypeRepository.findById(cultivatedLandDamageReasonDamageType.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandDamageReasonDamageTypeToMatchAllProperties(
        CultivatedLandDamageReasonDamageType expectedCultivatedLandDamageReasonDamageType
    ) {
        assertCultivatedLandDamageReasonDamageTypeAllPropertiesEquals(
            expectedCultivatedLandDamageReasonDamageType,
            getPersistedCultivatedLandDamageReasonDamageType(expectedCultivatedLandDamageReasonDamageType)
        );
    }

    protected void assertPersistedCultivatedLandDamageReasonDamageTypeToMatchUpdatableProperties(
        CultivatedLandDamageReasonDamageType expectedCultivatedLandDamageReasonDamageType
    ) {
        assertCultivatedLandDamageReasonDamageTypeAllUpdatablePropertiesEquals(
            expectedCultivatedLandDamageReasonDamageType,
            getPersistedCultivatedLandDamageReasonDamageType(expectedCultivatedLandDamageReasonDamageType)
        );
    }
}
