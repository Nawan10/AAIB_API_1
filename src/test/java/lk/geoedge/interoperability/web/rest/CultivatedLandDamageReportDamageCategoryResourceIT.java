package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategoryAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportDamageCategoryRepository;
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
 * Integration tests for the {@link CultivatedLandDamageReportDamageCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandDamageReportDamageCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-damage-report-damage-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandDamageReportDamageCategoryRepository cultivatedLandDamageReportDamageCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandDamageReportDamageCategoryMockMvc;

    private CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory;

    private CultivatedLandDamageReportDamageCategory insertedCultivatedLandDamageReportDamageCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReportDamageCategory createEntity() {
        return new CultivatedLandDamageReportDamageCategory().categoryName(DEFAULT_CATEGORY_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReportDamageCategory createUpdatedEntity() {
        return new CultivatedLandDamageReportDamageCategory().categoryName(UPDATED_CATEGORY_NAME);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandDamageReportDamageCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandDamageReportDamageCategory != null) {
            cultivatedLandDamageReportDamageCategoryRepository.delete(insertedCultivatedLandDamageReportDamageCategory);
            insertedCultivatedLandDamageReportDamageCategory = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandDamageReportDamageCategory
        var returnedCultivatedLandDamageReportDamageCategory = om.readValue(
            restCultivatedLandDamageReportDamageCategoryMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandDamageReportDamageCategory.class
        );

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandDamageReportDamageCategoryUpdatableFieldsEquals(
            returnedCultivatedLandDamageReportDamageCategory,
            getPersistedCultivatedLandDamageReportDamageCategory(returnedCultivatedLandDamageReportDamageCategory)
        );

        insertedCultivatedLandDamageReportDamageCategory = returnedCultivatedLandDamageReportDamageCategory;
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReportDamageCategoryWithExistingId() throws Exception {
        // Create the CultivatedLandDamageReportDamageCategory with an existing ID
        cultivatedLandDamageReportDamageCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageCategories() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get all the cultivatedLandDamageReportDamageCategoryList
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReportDamageCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReportDamageCategory() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get the cultivatedLandDamageReportDamageCategory
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandDamageReportDamageCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReportDamageCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        Long id = cultivatedLandDamageReportDamageCategory.getId();

        defaultCultivatedLandDamageReportDamageCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandDamageReportDamageCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandDamageReportDamageCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get all the cultivatedLandDamageReportDamageCategoryList where categoryName equals to
        defaultCultivatedLandDamageReportDamageCategoryFiltering(
            "categoryName.equals=" + DEFAULT_CATEGORY_NAME,
            "categoryName.equals=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get all the cultivatedLandDamageReportDamageCategoryList where categoryName in
        defaultCultivatedLandDamageReportDamageCategoryFiltering(
            "categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME,
            "categoryName.in=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get all the cultivatedLandDamageReportDamageCategoryList where categoryName is not null
        defaultCultivatedLandDamageReportDamageCategoryFiltering("categoryName.specified=true", "categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get all the cultivatedLandDamageReportDamageCategoryList where categoryName contains
        defaultCultivatedLandDamageReportDamageCategoryFiltering(
            "categoryName.contains=" + DEFAULT_CATEGORY_NAME,
            "categoryName.contains=" + UPDATED_CATEGORY_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportDamageCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        // Get all the cultivatedLandDamageReportDamageCategoryList where categoryName does not contain
        defaultCultivatedLandDamageReportDamageCategoryFiltering(
            "categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME,
            "categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME
        );
    }

    private void defaultCultivatedLandDamageReportDamageCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandDamageReportDamageCategoryShouldBeFound(shouldBeFound);
        defaultCultivatedLandDamageReportDamageCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandDamageReportDamageCategoryShouldBeFound(String filter) throws Exception {
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReportDamageCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));

        // Check, that the count call also returns 1
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandDamageReportDamageCategoryShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandDamageReportDamageCategory() throws Exception {
        // Get the cultivatedLandDamageReportDamageCategory
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandDamageReportDamageCategory() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReportDamageCategory
        CultivatedLandDamageReportDamageCategory updatedCultivatedLandDamageReportDamageCategory =
            cultivatedLandDamageReportDamageCategoryRepository.findById(cultivatedLandDamageReportDamageCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandDamageReportDamageCategory are not directly saved in db
        em.detach(updatedCultivatedLandDamageReportDamageCategory);
        updatedCultivatedLandDamageReportDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandDamageReportDamageCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandDamageReportDamageCategoryToMatchAllProperties(updatedCultivatedLandDamageReportDamageCategory);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandDamageReportDamageCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReportDamageCategory using partial update
        CultivatedLandDamageReportDamageCategory partialUpdatedCultivatedLandDamageReportDamageCategory =
            new CultivatedLandDamageReportDamageCategory();
        partialUpdatedCultivatedLandDamageReportDamageCategory.setId(cultivatedLandDamageReportDamageCategory.getId());

        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReportDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReportDamageCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReportDamageCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandDamageReportDamageCategory, cultivatedLandDamageReportDamageCategory),
            getPersistedCultivatedLandDamageReportDamageCategory(cultivatedLandDamageReportDamageCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandDamageReportDamageCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReportDamageCategory using partial update
        CultivatedLandDamageReportDamageCategory partialUpdatedCultivatedLandDamageReportDamageCategory =
            new CultivatedLandDamageReportDamageCategory();
        partialUpdatedCultivatedLandDamageReportDamageCategory.setId(cultivatedLandDamageReportDamageCategory.getId());

        partialUpdatedCultivatedLandDamageReportDamageCategory.categoryName(UPDATED_CATEGORY_NAME);

        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReportDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReportDamageCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReportDamageCategoryUpdatableFieldsEquals(
            partialUpdatedCultivatedLandDamageReportDamageCategory,
            getPersistedCultivatedLandDamageReportDamageCategory(partialUpdatedCultivatedLandDamageReportDamageCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandDamageReportDamageCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReportDamageCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReportDamageCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReportDamageCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandDamageReportDamageCategory() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryRepository.saveAndFlush(
            cultivatedLandDamageReportDamageCategory
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandDamageReportDamageCategory
        restCultivatedLandDamageReportDamageCategoryMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, cultivatedLandDamageReportDamageCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandDamageReportDamageCategoryRepository.count();
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

    protected CultivatedLandDamageReportDamageCategory getPersistedCultivatedLandDamageReportDamageCategory(
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) {
        return cultivatedLandDamageReportDamageCategoryRepository.findById(cultivatedLandDamageReportDamageCategory.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandDamageReportDamageCategoryToMatchAllProperties(
        CultivatedLandDamageReportDamageCategory expectedCultivatedLandDamageReportDamageCategory
    ) {
        assertCultivatedLandDamageReportDamageCategoryAllPropertiesEquals(
            expectedCultivatedLandDamageReportDamageCategory,
            getPersistedCultivatedLandDamageReportDamageCategory(expectedCultivatedLandDamageReportDamageCategory)
        );
    }

    protected void assertPersistedCultivatedLandDamageReportDamageCategoryToMatchUpdatableProperties(
        CultivatedLandDamageReportDamageCategory expectedCultivatedLandDamageReportDamageCategory
    ) {
        assertCultivatedLandDamageReportDamageCategoryAllUpdatablePropertiesEquals(
            expectedCultivatedLandDamageReportDamageCategory,
            getPersistedCultivatedLandDamageReportDamageCategory(expectedCultivatedLandDamageReportDamageCategory)
        );
    }
}
