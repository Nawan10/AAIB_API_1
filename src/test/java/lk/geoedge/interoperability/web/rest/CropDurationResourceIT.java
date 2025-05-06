package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropDurationAsserts.*;
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
import lk.geoedge.interoperability.domain.CropDuration;
import lk.geoedge.interoperability.domain.CropDurationCropType;
import lk.geoedge.interoperability.repository.CropDurationRepository;
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
 * Integration tests for the {@link CropDurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropDurationResourceIT {

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final Integer SMALLER_DURATION = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STAGES = "AAAAAAAAAA";
    private static final String UPDATED_STAGES = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADDED_BY = 1;
    private static final Integer UPDATED_ADDED_BY = 2;
    private static final Integer SMALLER_ADDED_BY = 1 - 1;

    private static final LocalDate DEFAULT_ADDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADDED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ADDED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/crop-durations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropDurationRepository cropDurationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropDurationMockMvc;

    private CropDuration cropDuration;

    private CropDuration insertedCropDuration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDuration createEntity() {
        return new CropDuration()
            .duration(DEFAULT_DURATION)
            .name(DEFAULT_NAME)
            .stages(DEFAULT_STAGES)
            .addedBy(DEFAULT_ADDED_BY)
            .addedDate(DEFAULT_ADDED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDuration createUpdatedEntity() {
        return new CropDuration()
            .duration(UPDATED_DURATION)
            .name(UPDATED_NAME)
            .stages(UPDATED_STAGES)
            .addedBy(UPDATED_ADDED_BY)
            .addedDate(UPDATED_ADDED_DATE);
    }

    @BeforeEach
    void initTest() {
        cropDuration = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropDuration != null) {
            cropDurationRepository.delete(insertedCropDuration);
            insertedCropDuration = null;
        }
    }

    @Test
    @Transactional
    void createCropDuration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropDuration
        var returnedCropDuration = om.readValue(
            restCropDurationMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDuration))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropDuration.class
        );

        // Validate the CropDuration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropDurationUpdatableFieldsEquals(returnedCropDuration, getPersistedCropDuration(returnedCropDuration));

        insertedCropDuration = returnedCropDuration;
    }

    @Test
    @Transactional
    void createCropDurationWithExistingId() throws Exception {
        // Create the CropDuration with an existing ID
        cropDuration.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropDurationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDuration)))
            .andExpect(status().isBadRequest());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropDurations() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList
        restCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].stages").value(hasItem(DEFAULT_STAGES)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCropDuration() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get the cropDuration
        restCropDurationMockMvc
            .perform(get(ENTITY_API_URL_ID, cropDuration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropDuration.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.stages").value(DEFAULT_STAGES))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCropDurationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        Long id = cropDuration.getId();

        defaultCropDurationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropDurationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropDurationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration equals to
        defaultCropDurationFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration in
        defaultCropDurationFiltering("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION, "duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration is not null
        defaultCropDurationFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration is greater than or equal to
        defaultCropDurationFiltering("duration.greaterThanOrEqual=" + DEFAULT_DURATION, "duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration is less than or equal to
        defaultCropDurationFiltering("duration.lessThanOrEqual=" + DEFAULT_DURATION, "duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration is less than
        defaultCropDurationFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllCropDurationsByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where duration is greater than
        defaultCropDurationFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllCropDurationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where name equals to
        defaultCropDurationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropDurationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where name in
        defaultCropDurationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropDurationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where name is not null
        defaultCropDurationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where name contains
        defaultCropDurationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropDurationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where name does not contain
        defaultCropDurationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCropDurationsByStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where stages equals to
        defaultCropDurationFiltering("stages.equals=" + DEFAULT_STAGES, "stages.equals=" + UPDATED_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDurationsByStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where stages in
        defaultCropDurationFiltering("stages.in=" + DEFAULT_STAGES + "," + UPDATED_STAGES, "stages.in=" + UPDATED_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDurationsByStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where stages is not null
        defaultCropDurationFiltering("stages.specified=true", "stages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationsByStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where stages contains
        defaultCropDurationFiltering("stages.contains=" + DEFAULT_STAGES, "stages.contains=" + UPDATED_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDurationsByStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where stages does not contain
        defaultCropDurationFiltering("stages.doesNotContain=" + UPDATED_STAGES, "stages.doesNotContain=" + DEFAULT_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy equals to
        defaultCropDurationFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy in
        defaultCropDurationFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy is not null
        defaultCropDurationFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy is greater than or equal to
        defaultCropDurationFiltering("addedBy.greaterThanOrEqual=" + DEFAULT_ADDED_BY, "addedBy.greaterThanOrEqual=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy is less than or equal to
        defaultCropDurationFiltering("addedBy.lessThanOrEqual=" + DEFAULT_ADDED_BY, "addedBy.lessThanOrEqual=" + SMALLER_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy is less than
        defaultCropDurationFiltering("addedBy.lessThan=" + UPDATED_ADDED_BY, "addedBy.lessThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedBy is greater than
        defaultCropDurationFiltering("addedBy.greaterThan=" + SMALLER_ADDED_BY, "addedBy.greaterThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate equals to
        defaultCropDurationFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate in
        defaultCropDurationFiltering("addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE, "addedDate.in=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate is not null
        defaultCropDurationFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate is greater than or equal to
        defaultCropDurationFiltering(
            "addedDate.greaterThanOrEqual=" + DEFAULT_ADDED_DATE,
            "addedDate.greaterThanOrEqual=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate is less than or equal to
        defaultCropDurationFiltering("addedDate.lessThanOrEqual=" + DEFAULT_ADDED_DATE, "addedDate.lessThanOrEqual=" + SMALLER_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate is less than
        defaultCropDurationFiltering("addedDate.lessThan=" + UPDATED_ADDED_DATE, "addedDate.lessThan=" + DEFAULT_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropDurationsByAddedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        // Get all the cropDurationList where addedDate is greater than
        defaultCropDurationFiltering("addedDate.greaterThan=" + SMALLER_ADDED_DATE, "addedDate.greaterThan=" + DEFAULT_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropDurationsByCropIsEqualToSomething() throws Exception {
        CropDurationCropType crop;
        if (TestUtil.findAll(em, CropDurationCropType.class).isEmpty()) {
            cropDurationRepository.saveAndFlush(cropDuration);
            crop = CropDurationCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, CropDurationCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        cropDuration.setCrop(crop);
        cropDurationRepository.saveAndFlush(cropDuration);
        Long cropId = crop.getId();
        // Get all the cropDurationList where crop equals to cropId
        defaultCropDurationShouldBeFound("cropId.equals=" + cropId);

        // Get all the cropDurationList where crop equals to (cropId + 1)
        defaultCropDurationShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultCropDurationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropDurationShouldBeFound(shouldBeFound);
        defaultCropDurationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropDurationShouldBeFound(String filter) throws Exception {
        restCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].stages").value(hasItem(DEFAULT_STAGES)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));

        // Check, that the count call also returns 1
        restCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropDurationShouldNotBeFound(String filter) throws Exception {
        restCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropDuration() throws Exception {
        // Get the cropDuration
        restCropDurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropDuration() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDuration
        CropDuration updatedCropDuration = cropDurationRepository.findById(cropDuration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropDuration are not directly saved in db
        em.detach(updatedCropDuration);
        updatedCropDuration
            .duration(UPDATED_DURATION)
            .name(UPDATED_NAME)
            .stages(UPDATED_STAGES)
            .addedBy(UPDATED_ADDED_BY)
            .addedDate(UPDATED_ADDED_DATE);

        restCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropDuration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropDuration))
            )
            .andExpect(status().isOk());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropDurationToMatchAllProperties(updatedCropDuration);
    }

    @Test
    @Transactional
    void putNonExistingCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDuration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropDuration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDuration)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropDurationWithPatch() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDuration using partial update
        CropDuration partialUpdatedCropDuration = new CropDuration();
        partialUpdatedCropDuration.setId(cropDuration.getId());

        partialUpdatedCropDuration.duration(UPDATED_DURATION).addedBy(UPDATED_ADDED_BY);

        restCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDuration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDuration))
            )
            .andExpect(status().isOk());

        // Validate the CropDuration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDurationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropDuration, cropDuration),
            getPersistedCropDuration(cropDuration)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropDurationWithPatch() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDuration using partial update
        CropDuration partialUpdatedCropDuration = new CropDuration();
        partialUpdatedCropDuration.setId(cropDuration.getId());

        partialUpdatedCropDuration
            .duration(UPDATED_DURATION)
            .name(UPDATED_NAME)
            .stages(UPDATED_STAGES)
            .addedBy(UPDATED_ADDED_BY)
            .addedDate(UPDATED_ADDED_DATE);

        restCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDuration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDuration))
            )
            .andExpect(status().isOk());

        // Validate the CropDuration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDurationUpdatableFieldsEquals(partialUpdatedCropDuration, getPersistedCropDuration(partialUpdatedCropDuration));
    }

    @Test
    @Transactional
    void patchNonExistingCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDuration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropDuration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropDuration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropDuration() throws Exception {
        // Initialize the database
        insertedCropDuration = cropDurationRepository.saveAndFlush(cropDuration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropDuration
        restCropDurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropDuration.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropDurationRepository.count();
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

    protected CropDuration getPersistedCropDuration(CropDuration cropDuration) {
        return cropDurationRepository.findById(cropDuration.getId()).orElseThrow();
    }

    protected void assertPersistedCropDurationToMatchAllProperties(CropDuration expectedCropDuration) {
        assertCropDurationAllPropertiesEquals(expectedCropDuration, getPersistedCropDuration(expectedCropDuration));
    }

    protected void assertPersistedCropDurationToMatchUpdatableProperties(CropDuration expectedCropDuration) {
        assertCropDurationAllUpdatablePropertiesEquals(expectedCropDuration, getPersistedCropDuration(expectedCropDuration));
    }
}
