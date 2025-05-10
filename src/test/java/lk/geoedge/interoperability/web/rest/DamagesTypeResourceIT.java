package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamagesTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.DamagesType;
import lk.geoedge.interoperability.repository.DamagesTypeRepository;
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
 * Integration tests for the {@link DamagesTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamagesTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/damages-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamagesTypeRepository damagesTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamagesTypeMockMvc;

    private DamagesType damagesType;

    private DamagesType insertedDamagesType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamagesType createEntity() {
        return new DamagesType().typeName(DEFAULT_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamagesType createUpdatedEntity() {
        return new DamagesType().typeName(UPDATED_TYPE_NAME);
    }

    @BeforeEach
    void initTest() {
        damagesType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamagesType != null) {
            damagesTypeRepository.delete(insertedDamagesType);
            insertedDamagesType = null;
        }
    }

    @Test
    @Transactional
    void createDamagesType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DamagesType
        var returnedDamagesType = om.readValue(
            restDamagesTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DamagesType.class
        );

        // Validate the DamagesType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamagesTypeUpdatableFieldsEquals(returnedDamagesType, getPersistedDamagesType(returnedDamagesType));

        insertedDamagesType = returnedDamagesType;
    }

    @Test
    @Transactional
    void createDamagesTypeWithExistingId() throws Exception {
        // Create the DamagesType with an existing ID
        damagesType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamagesTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesType)))
            .andExpect(status().isBadRequest());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamagesTypes() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get all the damagesTypeList
        restDamagesTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damagesType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getDamagesType() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get the damagesType
        restDamagesTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, damagesType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damagesType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getDamagesTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        Long id = damagesType.getId();

        defaultDamagesTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamagesTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamagesTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamagesTypesByTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get all the damagesTypeList where typeName equals to
        defaultDamagesTypeFiltering("typeName.equals=" + DEFAULT_TYPE_NAME, "typeName.equals=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesTypesByTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get all the damagesTypeList where typeName in
        defaultDamagesTypeFiltering("typeName.in=" + DEFAULT_TYPE_NAME + "," + UPDATED_TYPE_NAME, "typeName.in=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesTypesByTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get all the damagesTypeList where typeName is not null
        defaultDamagesTypeFiltering("typeName.specified=true", "typeName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesTypesByTypeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get all the damagesTypeList where typeName contains
        defaultDamagesTypeFiltering("typeName.contains=" + DEFAULT_TYPE_NAME, "typeName.contains=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesTypesByTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        // Get all the damagesTypeList where typeName does not contain
        defaultDamagesTypeFiltering("typeName.doesNotContain=" + UPDATED_TYPE_NAME, "typeName.doesNotContain=" + DEFAULT_TYPE_NAME);
    }

    private void defaultDamagesTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamagesTypeShouldBeFound(shouldBeFound);
        defaultDamagesTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamagesTypeShouldBeFound(String filter) throws Exception {
        restDamagesTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damagesType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));

        // Check, that the count call also returns 1
        restDamagesTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamagesTypeShouldNotBeFound(String filter) throws Exception {
        restDamagesTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamagesTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamagesType() throws Exception {
        // Get the damagesType
        restDamagesTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamagesType() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesType
        DamagesType updatedDamagesType = damagesTypeRepository.findById(damagesType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamagesType are not directly saved in db
        em.detach(updatedDamagesType);
        updatedDamagesType.typeName(UPDATED_TYPE_NAME);

        restDamagesTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamagesType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamagesType))
            )
            .andExpect(status().isOk());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamagesTypeToMatchAllProperties(updatedDamagesType);
    }

    @Test
    @Transactional
    void putNonExistingDamagesType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamagesTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damagesType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damagesType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamagesType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damagesType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamagesType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesTypeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamagesTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesType using partial update
        DamagesType partialUpdatedDamagesType = new DamagesType();
        partialUpdatedDamagesType.setId(damagesType.getId());

        partialUpdatedDamagesType.typeName(UPDATED_TYPE_NAME);

        restDamagesTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamagesType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamagesType))
            )
            .andExpect(status().isOk());

        // Validate the DamagesType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamagesTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDamagesType, damagesType),
            getPersistedDamagesType(damagesType)
        );
    }

    @Test
    @Transactional
    void fullUpdateDamagesTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesType using partial update
        DamagesType partialUpdatedDamagesType = new DamagesType();
        partialUpdatedDamagesType.setId(damagesType.getId());

        partialUpdatedDamagesType.typeName(UPDATED_TYPE_NAME);

        restDamagesTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamagesType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamagesType))
            )
            .andExpect(status().isOk());

        // Validate the DamagesType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamagesTypeUpdatableFieldsEquals(partialUpdatedDamagesType, getPersistedDamagesType(partialUpdatedDamagesType));
    }

    @Test
    @Transactional
    void patchNonExistingDamagesType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamagesTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damagesType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamagesType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamagesType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(damagesType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamagesType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamagesType() throws Exception {
        // Initialize the database
        insertedDamagesType = damagesTypeRepository.saveAndFlush(damagesType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damagesType
        restDamagesTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, damagesType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damagesTypeRepository.count();
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

    protected DamagesType getPersistedDamagesType(DamagesType damagesType) {
        return damagesTypeRepository.findById(damagesType.getId()).orElseThrow();
    }

    protected void assertPersistedDamagesTypeToMatchAllProperties(DamagesType expectedDamagesType) {
        assertDamagesTypeAllPropertiesEquals(expectedDamagesType, getPersistedDamagesType(expectedDamagesType));
    }

    protected void assertPersistedDamagesTypeToMatchUpdatableProperties(DamagesType expectedDamagesType) {
        assertDamagesTypeAllUpdatablePropertiesEquals(expectedDamagesType, getPersistedDamagesType(expectedDamagesType));
    }
}
