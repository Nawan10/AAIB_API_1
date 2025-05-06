package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategoryAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageCategoryRepository;
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
 * Integration tests for the {@link CultivatedLandDamageReasonDamageCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandDamageReasonDamageCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-damage-reason-damage-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandDamageReasonDamageCategoryMockMvc;

    private CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory;

    private CultivatedLandDamageReasonDamageCategory insertedCultivatedLandDamageReasonDamageCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReasonDamageCategory createEntity() {
        return new CultivatedLandDamageReasonDamageCategory().categoryName(DEFAULT_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReasonDamageCategory createUpdatedEntity() {
        return new CultivatedLandDamageReasonDamageCategory().categoryName(UPDATED_CATEGORY_NAME);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandDamageReasonDamageCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandDamageReasonDamageCategory != null) {
            cultivatedLandDamageReasonDamageCategoryRepository.delete(insertedCultivatedLandDamageReasonDamageCategory);
            insertedCultivatedLandDamageReasonDamageCategory = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandDamageReasonDamageCategory
        var returnedCultivatedLandDamageReasonDamageCategory = om.readValue(
            restCultivatedLandDamageReasonDamageCategoryMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandDamageReasonDamageCategory.class
        );

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandDamageReasonDamageCategoryUpdatableFieldsEquals(
            returnedCultivatedLandDamageReasonDamageCategory,
            getPersistedCultivatedLandDamageReasonDamageCategory(returnedCultivatedLandDamageReasonDamageCategory)
        );

        insertedCultivatedLandDamageReasonDamageCategory = returnedCultivatedLandDamageReasonDamageCategory;
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReasonDamageCategoryWithExistingId() throws Exception {
        // Create the CultivatedLandDamageReasonDamageCategory with an existing ID
        cultivatedLandDamageReasonDamageCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageCategories() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get all the cultivatedLandDamageReasonDamageCategoryList
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReasonDamageCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReasonDamageCategory() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get the cultivatedLandDamageReasonDamageCategory
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandDamageReasonDamageCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReasonDamageCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        Long id = cultivatedLandDamageReasonDamageCategory.getId();

        defaultCultivatedLandDamageReasonDamageCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandDamageReasonDamageCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandDamageReasonDamageCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get all the cultivatedLandDamageReasonDamageCategoryList where categoryName equals to
        defaultCultivatedLandDamageReasonDamageCategoryFiltering(
            "categoryName.equals=" + DEFAULT_CATEGORY_NAME,
            "categoryName.equals=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get all the cultivatedLandDamageReasonDamageCategoryList where categoryName in
        defaultCultivatedLandDamageReasonDamageCategoryFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get all the cultivatedLandDamageReasonDamageCategoryList where categoryName is not null
        defaultCultivatedLandDamageReasonDamageCategoryFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get all the cultivatedLandDamageReasonDamageCategoryList where categoryName contains
        defaultCultivatedLandDamageReasonDamageCategoryFiltering(
            "categoryName.contains=" + DEFAULT_CATEGORY_NAME,
            "categoryName.contains=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReasonDamageCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        // Get all the cultivatedLandDamageReasonDamageCategoryList where categoryName does not contain
        defaultCultivatedLandDamageReasonDamageCategoryFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    private void defaultCultivatedLandDamageReasonDamageCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandDamageReasonDamageCategoryShouldBeFound(shouldBeFound);
        defaultCultivatedLandDamageReasonDamageCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandDamageReasonDamageCategoryShouldBeFound(String filter) throws Exception {
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReasonDamageCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));

        // Check, that the count call also returns 1
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandDamageReasonDamageCategoryShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandDamageReasonDamageCategory() throws Exception {
        // Get the cultivatedLandDamageReasonDamageCategory
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandDamageReasonDamageCategory() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReasonDamageCategory
        CultivatedLandDamageReasonDamageCategory updatedCultivatedLandDamageReasonDamageCategory =
            cultivatedLandDamageReasonDamageCategoryRepository.findById(cultivatedLandDamageReasonDamageCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandDamageReasonDamageCategory are not directly saved in db
        em.detach(updatedCultivatedLandDamageReasonDamageCategory);
        updatedCultivatedLandDamageReasonDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandDamageReasonDamageCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandDamageReasonDamageCategoryToMatchAllProperties(updatedCultivatedLandDamageReasonDamageCategory);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandDamageReasonDamageCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReasonDamageCategory using partial update
        CultivatedLandDamageReasonDamageCategory partialUpdatedCultivatedLandDamageReasonDamageCategory =
            new CultivatedLandDamageReasonDamageCategory();
        partialUpdatedCultivatedLandDamageReasonDamageCategory.setId(cultivatedLandDamageReasonDamageCategory.getId());

        partialUpdatedCultivatedLandDamageReasonDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReasonDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReasonDamageCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandDamageReasonDamageCategory, cultivatedLandDamageReasonDamageCategory),
            getPersistedCultivatedLandDamageReasonDamageCategory(cultivatedLandDamageReasonDamageCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandDamageReasonDamageCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReasonDamageCategory using partial update
        CultivatedLandDamageReasonDamageCategory partialUpdatedCultivatedLandDamageReasonDamageCategory =
            new CultivatedLandDamageReasonDamageCategory();
        partialUpdatedCultivatedLandDamageReasonDamageCategory.setId(cultivatedLandDamageReasonDamageCategory.getId());

        partialUpdatedCultivatedLandDamageReasonDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReasonDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReasonDamageCategoryUpdatableFieldsEquals(
            partialUpdatedCultivatedLandDamageReasonDamageCategory,
            getPersistedCultivatedLandDamageReasonDamageCategory(partialUpdatedCultivatedLandDamageReasonDamageCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandDamageReasonDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReasonDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReasonDamageCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReasonDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandDamageReasonDamageCategory() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReasonDamageCategory = cultivatedLandDamageReasonDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReasonDamageCategory
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandDamageReasonDamageCategory
        restCultivatedLandDamageReasonDamageCategoryMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, cultivatedLandDamageReasonDamageCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandDamageReasonDamageCategoryRepository.count();
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

    protected CultivatedLandDamageReasonDamageCategory getPersistedCultivatedLandDamageReasonDamageCategory(
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory
    ) {
        return cultivatedLandDamageReasonDamageCategoryRepository.findById(cultivatedLandDamageReasonDamageCategory.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandDamageReasonDamageCategoryToMatchAllProperties(
        CultivatedLandDamageReasonDamageCategory expectedCultivatedLandDamageReasonDamageCategory
    ) {
        assertCultivatedLandDamageReasonDamageCategoryAllPropertiesEquals(
            expectedCultivatedLandDamageReasonDamageCategory,
            getPersistedCultivatedLandDamageReasonDamageCategory(expectedCultivatedLandDamageReasonDamageCategory)
        );
    }

    protected void assertPersistedCultivatedLandDamageReasonDamageCategoryToMatchUpdatableProperties(
        CultivatedLandDamageReasonDamageCategory expectedCultivatedLandDamageReasonDamageCategory
    ) {
        assertCultivatedLandDamageReasonDamageCategoryAllUpdatablePropertiesEquals(
            expectedCultivatedLandDamageReasonDamageCategory,
            getPersistedCultivatedLandDamageReasonDamageCategory(expectedCultivatedLandDamageReasonDamageCategory)
        );
    }
}
