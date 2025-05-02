package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamageCategoryAsserts.*;
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
import lk.geoedge.interoperability.domain.DamageCategory;
import lk.geoedge.interoperability.repository.DamageCategoryRepository;
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
 * Integration tests for the {@link DamageCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamageCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/damage-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamageCategoryRepository damageCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamageCategoryMockMvc;

    private DamageCategory damageCategory;

    private DamageCategory insertedDamageCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamageCategory createEntity() {
        return new DamageCategory().categoryName(DEFAULT_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamageCategory createUpdatedEntity() {
        return new DamageCategory().categoryName(UPDATED_CATEGORY_NAME);
    }

    @BeforeEach
    void initTest() {
        damageCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamageCategory != null) {
            damageCategoryRepository.delete(insertedDamageCategory);
            insertedDamageCategory = null;
        }
    }

    @Test
    @Transactional
    void createDamageCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DamageCategory
        var returnedDamageCategory = om.readValue(
            restDamageCategoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageCategory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DamageCategory.class
        );

        // Validate the DamageCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamageCategoryUpdatableFieldsEquals(returnedDamageCategory, getPersistedDamageCategory(returnedDamageCategory));

        insertedDamageCategory = returnedDamageCategory;
    }

    @Test
    @Transactional
    void createDamageCategoryWithExistingId() throws Exception {
        // Create the DamageCategory with an existing ID
        damageCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamageCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamageCategories() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get all the damageCategoryList
        restDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damageCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getDamageCategory() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get the damageCategory
        restDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, damageCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damageCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getDamageCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        Long id = damageCategory.getId();

        defaultDamageCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamageCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamageCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamageCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get all the damageCategoryList where categoryName equals to
        defaultDamageCategoryFiltering("categoryName.equals=" + DEFAULT_CATEGORY_NAME, "categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllDamageCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get all the damageCategoryList where categoryName in
        defaultDamageCategoryFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllDamageCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get all the damageCategoryList where categoryName is not null
        defaultDamageCategoryFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get all the damageCategoryList where categoryName contains
        defaultDamageCategoryFiltering("categoryName.contains=" + DEFAULT_CATEGORY_NAME, "categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllDamageCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        // Get all the damageCategoryList where categoryName does not contain
        defaultDamageCategoryFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    private void defaultDamageCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamageCategoryShouldBeFound(shouldBeFound);
        defaultDamageCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamageCategoryShouldBeFound(String filter) throws Exception {
        restDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damageCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));

        // Check, that the count call also returns 1
        restDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamageCategoryShouldNotBeFound(String filter) throws Exception {
        restDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamageCategory() throws Exception {
        // Get the damageCategory
        restDamageCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamageCategory() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageCategory
        DamageCategory updatedDamageCategory = damageCategoryRepository.findById(damageCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamageCategory are not directly saved in db
        em.detach(updatedDamageCategory);
        updatedDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamageCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamageCategoryToMatchAllProperties(updatedDamageCategory);
    }

    @Test
    @Transactional
    void putNonExistingDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damageCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageCategoryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamageCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageCategory using partial update
        DamageCategory partialUpdatedDamageCategory = new DamageCategory();
        partialUpdatedDamageCategory.setId(damageCategory.getId());

        restDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the DamageCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDamageCategory, damageCategory),
            getPersistedDamageCategory(damageCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateDamageCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageCategory using partial update
        DamageCategory partialUpdatedDamageCategory = new DamageCategory();
        partialUpdatedDamageCategory.setId(damageCategory.getId());

        partialUpdatedDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the DamageCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageCategoryUpdatableFieldsEquals(partialUpdatedDamageCategory, getPersistedDamageCategory(partialUpdatedDamageCategory));
    }

    @Test
    @Transactional
    void patchNonExistingDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(damageCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamageCategory() throws Exception {
        // Initialize the database
        insertedDamageCategory = damageCategoryRepository.saveAndFlush(damageCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damageCategory
        restDamageCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, damageCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damageCategoryRepository.count();
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

    protected DamageCategory getPersistedDamageCategory(DamageCategory damageCategory) {
        return damageCategoryRepository.findById(damageCategory.getId()).orElseThrow();
    }

    protected void assertPersistedDamageCategoryToMatchAllProperties(DamageCategory expectedDamageCategory) {
        assertDamageCategoryAllPropertiesEquals(expectedDamageCategory, getPersistedDamageCategory(expectedDamageCategory));
    }

    protected void assertPersistedDamageCategoryToMatchUpdatableProperties(DamageCategory expectedDamageCategory) {
        assertDamageCategoryAllUpdatablePropertiesEquals(expectedDamageCategory, getPersistedDamageCategory(expectedDamageCategory));
    }
}
