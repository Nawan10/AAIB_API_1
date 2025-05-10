package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamagesCategoryAsserts.*;
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
import lk.geoedge.interoperability.domain.DamagesCategory;
import lk.geoedge.interoperability.repository.DamagesCategoryRepository;
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
 * Integration tests for the {@link DamagesCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamagesCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/damages-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamagesCategoryRepository damagesCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamagesCategoryMockMvc;

    private DamagesCategory damagesCategory;

    private DamagesCategory insertedDamagesCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamagesCategory createEntity() {
        return new DamagesCategory().categoryName(DEFAULT_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamagesCategory createUpdatedEntity() {
        return new DamagesCategory().categoryName(UPDATED_CATEGORY_NAME);
    }

    @BeforeEach
    void initTest() {
        damagesCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamagesCategory != null) {
            damagesCategoryRepository.delete(insertedDamagesCategory);
            insertedDamagesCategory = null;
        }
    }

    @Test
    @Transactional
    void createDamagesCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DamagesCategory
        var returnedDamagesCategory = om.readValue(
            restDamagesCategoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesCategory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DamagesCategory.class
        );

        // Validate the DamagesCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamagesCategoryUpdatableFieldsEquals(returnedDamagesCategory, getPersistedDamagesCategory(returnedDamagesCategory));

        insertedDamagesCategory = returnedDamagesCategory;
    }

    @Test
    @Transactional
    void createDamagesCategoryWithExistingId() throws Exception {
        // Create the DamagesCategory with an existing ID
        damagesCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamagesCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamagesCategories() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get all the damagesCategoryList
        restDamagesCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damagesCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getDamagesCategory() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get the damagesCategory
        restDamagesCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, damagesCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damagesCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getDamagesCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        Long id = damagesCategory.getId();

        defaultDamagesCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamagesCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamagesCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamagesCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get all the damagesCategoryList where categoryName equals to
        defaultDamagesCategoryFiltering("categoryName.equals=" + DEFAULT_CATEGORY_NAME, "categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get all the damagesCategoryList where categoryName in
        defaultDamagesCategoryFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllDamagesCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get all the damagesCategoryList where categoryName is not null
        defaultDamagesCategoryFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get all the damagesCategoryList where categoryName contains
        defaultDamagesCategoryFiltering("categoryName.contains=" + DEFAULT_CATEGORY_NAME, "categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        // Get all the damagesCategoryList where categoryName does not contain
        defaultDamagesCategoryFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    private void defaultDamagesCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamagesCategoryShouldBeFound(shouldBeFound);
        defaultDamagesCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamagesCategoryShouldBeFound(String filter) throws Exception {
        restDamagesCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damagesCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));

        // Check, that the count call also returns 1
        restDamagesCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamagesCategoryShouldNotBeFound(String filter) throws Exception {
        restDamagesCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamagesCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamagesCategory() throws Exception {
        // Get the damagesCategory
        restDamagesCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamagesCategory() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesCategory
        DamagesCategory updatedDamagesCategory = damagesCategoryRepository.findById(damagesCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamagesCategory are not directly saved in db
        em.detach(updatedDamagesCategory);
        updatedDamagesCategory.categoryName(UPDATED_CATEGORY_NAME);

        restDamagesCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamagesCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamagesCategory))
            )
            .andExpect(status().isOk());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamagesCategoryToMatchAllProperties(updatedDamagesCategory);
    }

    @Test
    @Transactional
    void putNonExistingDamagesCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamagesCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damagesCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamagesCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamagesCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamagesCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesCategory using partial update
        DamagesCategory partialUpdatedDamagesCategory = new DamagesCategory();
        partialUpdatedDamagesCategory.setId(damagesCategory.getId());

        partialUpdatedDamagesCategory.categoryName(UPDATED_CATEGORY_NAME);

        restDamagesCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamagesCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamagesCategory))
            )
            .andExpect(status().isOk());

        // Validate the DamagesCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamagesCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDamagesCategory, damagesCategory),
            getPersistedDamagesCategory(damagesCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateDamagesCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesCategory using partial update
        DamagesCategory partialUpdatedDamagesCategory = new DamagesCategory();
        partialUpdatedDamagesCategory.setId(damagesCategory.getId());

        partialUpdatedDamagesCategory.categoryName(UPDATED_CATEGORY_NAME);

        restDamagesCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamagesCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamagesCategory))
            )
            .andExpect(status().isOk());

        // Validate the DamagesCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamagesCategoryUpdatableFieldsEquals(
            partialUpdatedDamagesCategory,
            getPersistedDamagesCategory(partialUpdatedDamagesCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDamagesCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamagesCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damagesCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamagesCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamagesCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamagesCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamagesCategory() throws Exception {
        // Initialize the database
        insertedDamagesCategory = damagesCategoryRepository.saveAndFlush(damagesCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damagesCategory
        restDamagesCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, damagesCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damagesCategoryRepository.count();
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

    protected DamagesCategory getPersistedDamagesCategory(DamagesCategory damagesCategory) {
        return damagesCategoryRepository.findById(damagesCategory.getId()).orElseThrow();
    }

    protected void assertPersistedDamagesCategoryToMatchAllProperties(DamagesCategory expectedDamagesCategory) {
        assertDamagesCategoryAllPropertiesEquals(expectedDamagesCategory, getPersistedDamagesCategory(expectedDamagesCategory));
    }

    protected void assertPersistedDamagesCategoryToMatchUpdatableProperties(DamagesCategory expectedDamagesCategory) {
        assertDamagesCategoryAllUpdatablePropertiesEquals(expectedDamagesCategory, getPersistedDamagesCategory(expectedDamagesCategory));
    }
}
