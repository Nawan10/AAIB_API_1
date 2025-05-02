package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamageTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.DamageType;
import lk.geoedge.interoperability.repository.DamageTypeRepository;
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
 * Integration tests for the {@link DamageTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamageTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/damage-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamageTypeRepository damageTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamageTypeMockMvc;

    private DamageType damageType;

    private DamageType insertedDamageType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamageType createEntity() {
        return new DamageType().typeName(DEFAULT_TYPE_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamageType createUpdatedEntity() {
        return new DamageType().typeName(UPDATED_TYPE_NAME);
    }

    @BeforeEach
    void initTest() {
        damageType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamageType != null) {
            damageTypeRepository.delete(insertedDamageType);
            insertedDamageType = null;
        }
    }

    @Test
    @Transactional
    void createDamageType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DamageType
        var returnedDamageType = om.readValue(
            restDamageTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DamageType.class
        );

        // Validate the DamageType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamageTypeUpdatableFieldsEquals(returnedDamageType, getPersistedDamageType(returnedDamageType));

        insertedDamageType = returnedDamageType;
    }

    @Test
    @Transactional
    void createDamageTypeWithExistingId() throws Exception {
        // Create the DamageType with an existing ID
        damageType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamageTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageType)))
            .andExpect(status().isBadRequest());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamageTypes() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get all the damageTypeList
        restDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getDamageType() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get the damageType
        restDamageTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, damageType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damageType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getDamageTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        Long id = damageType.getId();

        defaultDamageTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamageTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamageTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamageTypesByTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get all the damageTypeList where typeName equals to
        defaultDamageTypeFiltering("typeName.equals=" + DEFAULT_TYPE_NAME, "typeName.equals=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllDamageTypesByTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get all the damageTypeList where typeName in
        defaultDamageTypeFiltering("typeName.in=" + DEFAULT_TYPE_NAME + "," + UPDATED_TYPE_NAME, "typeName.in=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllDamageTypesByTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get all the damageTypeList where typeName is not null
        defaultDamageTypeFiltering("typeName.specified=true", "typeName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageTypesByTypeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get all the damageTypeList where typeName contains
        defaultDamageTypeFiltering("typeName.contains=" + DEFAULT_TYPE_NAME, "typeName.contains=" + UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllDamageTypesByTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        // Get all the damageTypeList where typeName does not contain
        defaultDamageTypeFiltering("typeName.doesNotContain=" + UPDATED_TYPE_NAME, "typeName.doesNotContain=" + DEFAULT_TYPE_NAME);
    }

    private void defaultDamageTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamageTypeShouldBeFound(shouldBeFound);
        defaultDamageTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamageTypeShouldBeFound(String filter) throws Exception {
        restDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damageType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));

        // Check, that the count call also returns 1
        restDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamageTypeShouldNotBeFound(String filter) throws Exception {
        restDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamageTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamageType() throws Exception {
        // Get the damageType
        restDamageTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamageType() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageType
        DamageType updatedDamageType = damageTypeRepository.findById(damageType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamageType are not directly saved in db
        em.detach(updatedDamageType);
        updatedDamageType.typeName(UPDATED_TYPE_NAME);

        restDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamageType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamageType))
            )
            .andExpect(status().isOk());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamageTypeToMatchAllProperties(updatedDamageType);
    }

    @Test
    @Transactional
    void putNonExistingDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damageType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageTypeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamageTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageType using partial update
        DamageType partialUpdatedDamageType = new DamageType();
        partialUpdatedDamageType.setId(damageType.getId());

        partialUpdatedDamageType.typeName(UPDATED_TYPE_NAME);

        restDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamageType))
            )
            .andExpect(status().isOk());

        // Validate the DamageType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDamageType, damageType),
            getPersistedDamageType(damageType)
        );
    }

    @Test
    @Transactional
    void fullUpdateDamageTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageType using partial update
        DamageType partialUpdatedDamageType = new DamageType();
        partialUpdatedDamageType.setId(damageType.getId());

        partialUpdatedDamageType.typeName(UPDATED_TYPE_NAME);

        restDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamageType))
            )
            .andExpect(status().isOk());

        // Validate the DamageType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageTypeUpdatableFieldsEquals(partialUpdatedDamageType, getPersistedDamageType(partialUpdatedDamageType));
    }

    @Test
    @Transactional
    void patchNonExistingDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damageType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damageType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamageType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(damageType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamageType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamageType() throws Exception {
        // Initialize the database
        insertedDamageType = damageTypeRepository.saveAndFlush(damageType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damageType
        restDamageTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, damageType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damageTypeRepository.count();
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

    protected DamageType getPersistedDamageType(DamageType damageType) {
        return damageTypeRepository.findById(damageType.getId()).orElseThrow();
    }

    protected void assertPersistedDamageTypeToMatchAllProperties(DamageType expectedDamageType) {
        assertDamageTypeAllPropertiesEquals(expectedDamageType, getPersistedDamageType(expectedDamageType));
    }

    protected void assertPersistedDamageTypeToMatchUpdatableProperties(DamageType expectedDamageType) {
        assertDamageTypeAllUpdatablePropertiesEquals(expectedDamageType, getPersistedDamageType(expectedDamageType));
    }
}
