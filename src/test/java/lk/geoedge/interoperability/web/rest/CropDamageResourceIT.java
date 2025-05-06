package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropDamageAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.CropDamage;
import lk.geoedge.interoperability.domain.CropDamageCropType;
import lk.geoedge.interoperability.domain.CropDamageDamage;
import lk.geoedge.interoperability.repository.CropDamageRepository;
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
 * Integration tests for the {@link CropDamageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropDamageResourceIT {

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/crop-damages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropDamageRepository cropDamageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropDamageMockMvc;

    private CropDamage cropDamage;

    private CropDamage insertedCropDamage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDamage createEntity() {
        return new CropDamage().addedBy(DEFAULT_ADDED_BY).createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDamage createUpdatedEntity() {
        return new CropDamage().addedBy(UPDATED_ADDED_BY).createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        cropDamage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropDamage != null) {
            cropDamageRepository.delete(insertedCropDamage);
            insertedCropDamage = null;
        }
    }

    @Test
    @Transactional
    void createCropDamage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropDamage
        var returnedCropDamage = om.readValue(
            restCropDamageMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamage))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropDamage.class
        );

        // Validate the CropDamage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropDamageUpdatableFieldsEquals(returnedCropDamage, getPersistedCropDamage(returnedCropDamage));

        insertedCropDamage = returnedCropDamage;
    }

    @Test
    @Transactional
    void createCropDamageWithExistingId() throws Exception {
        // Create the CropDamage with an existing ID
        cropDamage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropDamageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamage)))
            .andExpect(status().isBadRequest());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropDamages() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList
        restCropDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDamage.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCropDamage() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get the cropDamage
        restCropDamageMockMvc
            .perform(get(ENTITY_API_URL_ID, cropDamage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropDamage.getId().intValue()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCropDamagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        Long id = cropDamage.getId();

        defaultCropDamageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropDamageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropDamageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropDamagesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where addedBy equals to
        defaultCropDamageFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamagesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where addedBy in
        defaultCropDamageFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamagesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where addedBy is not null
        defaultCropDamageFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamagesByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where addedBy contains
        defaultCropDamageFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamagesByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where addedBy does not contain
        defaultCropDamageFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt equals to
        defaultCropDamageFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt in
        defaultCropDamageFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt is not null
        defaultCropDamageFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt is greater than or equal to
        defaultCropDamageFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt is less than or equal to
        defaultCropDamageFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt is less than
        defaultCropDamageFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamagesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        // Get all the cropDamageList where createdAt is greater than
        defaultCropDamageFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamagesByCropIsEqualToSomething() throws Exception {
        CropDamageCropType crop;
        if (TestUtil.findAll(em, CropDamageCropType.class).isEmpty()) {
            cropDamageRepository.saveAndFlush(cropDamage);
            crop = CropDamageCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, CropDamageCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        cropDamage.setCrop(crop);
        cropDamageRepository.saveAndFlush(cropDamage);
        Long cropId = crop.getId();
        // Get all the cropDamageList where crop equals to cropId
        defaultCropDamageShouldBeFound("cropId.equals=" + cropId);

        // Get all the cropDamageList where crop equals to (cropId + 1)
        defaultCropDamageShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    @Test
    @Transactional
    void getAllCropDamagesByDamageIsEqualToSomething() throws Exception {
        CropDamageDamage damage;
        if (TestUtil.findAll(em, CropDamageDamage.class).isEmpty()) {
            cropDamageRepository.saveAndFlush(cropDamage);
            damage = CropDamageDamageResourceIT.createEntity();
        } else {
            damage = TestUtil.findAll(em, CropDamageDamage.class).get(0);
        }
        em.persist(damage);
        em.flush();
        cropDamage.setDamage(damage);
        cropDamageRepository.saveAndFlush(cropDamage);
        Long damageId = damage.getId();
        // Get all the cropDamageList where damage equals to damageId
        defaultCropDamageShouldBeFound("damageId.equals=" + damageId);

        // Get all the cropDamageList where damage equals to (damageId + 1)
        defaultCropDamageShouldNotBeFound("damageId.equals=" + (damageId + 1));
    }

    private void defaultCropDamageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropDamageShouldBeFound(shouldBeFound);
        defaultCropDamageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropDamageShouldBeFound(String filter) throws Exception {
        restCropDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDamage.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restCropDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropDamageShouldNotBeFound(String filter) throws Exception {
        restCropDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropDamage() throws Exception {
        // Get the cropDamage
        restCropDamageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropDamage() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamage
        CropDamage updatedCropDamage = cropDamageRepository.findById(cropDamage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropDamage are not directly saved in db
        em.detach(updatedCropDamage);
        updatedCropDamage.addedBy(UPDATED_ADDED_BY).createdAt(UPDATED_CREATED_AT);

        restCropDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropDamage))
            )
            .andExpect(status().isOk());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropDamageToMatchAllProperties(updatedCropDamage);
    }

    @Test
    @Transactional
    void putNonExistingCropDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropDamageWithPatch() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamage using partial update
        CropDamage partialUpdatedCropDamage = new CropDamage();
        partialUpdatedCropDamage.setId(cropDamage.getId());

        partialUpdatedCropDamage.addedBy(UPDATED_ADDED_BY).createdAt(UPDATED_CREATED_AT);

        restCropDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDamage))
            )
            .andExpect(status().isOk());

        // Validate the CropDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDamageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropDamage, cropDamage),
            getPersistedCropDamage(cropDamage)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropDamageWithPatch() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamage using partial update
        CropDamage partialUpdatedCropDamage = new CropDamage();
        partialUpdatedCropDamage.setId(cropDamage.getId());

        partialUpdatedCropDamage.addedBy(UPDATED_ADDED_BY).createdAt(UPDATED_CREATED_AT);

        restCropDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDamage))
            )
            .andExpect(status().isOk());

        // Validate the CropDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDamageUpdatableFieldsEquals(partialUpdatedCropDamage, getPersistedCropDamage(partialUpdatedCropDamage));
    }

    @Test
    @Transactional
    void patchNonExistingCropDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropDamage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropDamage() throws Exception {
        // Initialize the database
        insertedCropDamage = cropDamageRepository.saveAndFlush(cropDamage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropDamage
        restCropDamageMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropDamage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropDamageRepository.count();
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

    protected CropDamage getPersistedCropDamage(CropDamage cropDamage) {
        return cropDamageRepository.findById(cropDamage.getId()).orElseThrow();
    }

    protected void assertPersistedCropDamageToMatchAllProperties(CropDamage expectedCropDamage) {
        assertCropDamageAllPropertiesEquals(expectedCropDamage, getPersistedCropDamage(expectedCropDamage));
    }

    protected void assertPersistedCropDamageToMatchUpdatableProperties(CropDamage expectedCropDamage) {
        assertCropDamageAllUpdatablePropertiesEquals(expectedCropDamage, getPersistedCropDamage(expectedCropDamage));
    }
}
