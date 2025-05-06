package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropVarietyCropDurationAsserts.*;
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
import lk.geoedge.interoperability.domain.CropVarietyCropDuration;
import lk.geoedge.interoperability.repository.CropVarietyCropDurationRepository;
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
 * Integration tests for the {@link CropVarietyCropDurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropVarietyCropDurationResourceIT {

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

    private static final String ENTITY_API_URL = "/api/crop-variety-crop-durations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropVarietyCropDurationRepository cropVarietyCropDurationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropVarietyCropDurationMockMvc;

    private CropVarietyCropDuration cropVarietyCropDuration;

    private CropVarietyCropDuration insertedCropVarietyCropDuration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropVarietyCropDuration createEntity() {
        return new CropVarietyCropDuration()
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
    public static CropVarietyCropDuration createUpdatedEntity() {
        return new CropVarietyCropDuration()
            .duration(UPDATED_DURATION)
            .name(UPDATED_NAME)
            .stages(UPDATED_STAGES)
            .addedBy(UPDATED_ADDED_BY)
            .addedDate(UPDATED_ADDED_DATE);
    }

    @BeforeEach
    void initTest() {
        cropVarietyCropDuration = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropVarietyCropDuration != null) {
            cropVarietyCropDurationRepository.delete(insertedCropVarietyCropDuration);
            insertedCropVarietyCropDuration = null;
        }
    }

    @Test
    @Transactional
    void createCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropVarietyCropDuration
        var returnedCropVarietyCropDuration = om.readValue(
            restCropVarietyCropDurationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cropVarietyCropDuration))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropVarietyCropDuration.class
        );

        // Validate the CropVarietyCropDuration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropVarietyCropDurationUpdatableFieldsEquals(
            returnedCropVarietyCropDuration,
            getPersistedCropVarietyCropDuration(returnedCropVarietyCropDuration)
        );

        insertedCropVarietyCropDuration = returnedCropVarietyCropDuration;
    }

    @Test
    @Transactional
    void createCropVarietyCropDurationWithExistingId() throws Exception {
        // Create the CropVarietyCropDuration with an existing ID
        cropVarietyCropDuration.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropVarietyCropDurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurations() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList
        restCropVarietyCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVarietyCropDuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].stages").value(hasItem(DEFAULT_STAGES)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCropVarietyCropDuration() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get the cropVarietyCropDuration
        restCropVarietyCropDurationMockMvc
            .perform(get(ENTITY_API_URL_ID, cropVarietyCropDuration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropVarietyCropDuration.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.stages").value(DEFAULT_STAGES))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCropVarietyCropDurationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        Long id = cropVarietyCropDuration.getId();

        defaultCropVarietyCropDurationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropVarietyCropDurationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropVarietyCropDurationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration equals to
        defaultCropVarietyCropDurationFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration in
        defaultCropVarietyCropDurationFiltering(
            "duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION,
            "duration.in=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration is not null
        defaultCropVarietyCropDurationFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration is greater than or equal to
        defaultCropVarietyCropDurationFiltering(
            "duration.greaterThanOrEqual=" + DEFAULT_DURATION,
            "duration.greaterThanOrEqual=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration is less than or equal to
        defaultCropVarietyCropDurationFiltering(
            "duration.lessThanOrEqual=" + DEFAULT_DURATION,
            "duration.lessThanOrEqual=" + SMALLER_DURATION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration is less than
        defaultCropVarietyCropDurationFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where duration is greater than
        defaultCropVarietyCropDurationFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where name equals to
        defaultCropVarietyCropDurationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where name in
        defaultCropVarietyCropDurationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where name is not null
        defaultCropVarietyCropDurationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where name contains
        defaultCropVarietyCropDurationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where name does not contain
        defaultCropVarietyCropDurationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where stages equals to
        defaultCropVarietyCropDurationFiltering("stages.equals=" + DEFAULT_STAGES, "stages.equals=" + UPDATED_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where stages in
        defaultCropVarietyCropDurationFiltering("stages.in=" + DEFAULT_STAGES + "," + UPDATED_STAGES, "stages.in=" + UPDATED_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where stages is not null
        defaultCropVarietyCropDurationFiltering("stages.specified=true", "stages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where stages contains
        defaultCropVarietyCropDurationFiltering("stages.contains=" + DEFAULT_STAGES, "stages.contains=" + UPDATED_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where stages does not contain
        defaultCropVarietyCropDurationFiltering("stages.doesNotContain=" + UPDATED_STAGES, "stages.doesNotContain=" + DEFAULT_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy equals to
        defaultCropVarietyCropDurationFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy in
        defaultCropVarietyCropDurationFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy is not null
        defaultCropVarietyCropDurationFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy is greater than or equal to
        defaultCropVarietyCropDurationFiltering(
            "addedBy.greaterThanOrEqual=" + DEFAULT_ADDED_BY,
            "addedBy.greaterThanOrEqual=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy is less than or equal to
        defaultCropVarietyCropDurationFiltering(
            "addedBy.lessThanOrEqual=" + DEFAULT_ADDED_BY,
            "addedBy.lessThanOrEqual=" + SMALLER_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy is less than
        defaultCropVarietyCropDurationFiltering("addedBy.lessThan=" + UPDATED_ADDED_BY, "addedBy.lessThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedBy is greater than
        defaultCropVarietyCropDurationFiltering("addedBy.greaterThan=" + SMALLER_ADDED_BY, "addedBy.greaterThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate equals to
        defaultCropVarietyCropDurationFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate in
        defaultCropVarietyCropDurationFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate is not null
        defaultCropVarietyCropDurationFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate is greater than or equal to
        defaultCropVarietyCropDurationFiltering(
            "addedDate.greaterThanOrEqual=" + DEFAULT_ADDED_DATE,
            "addedDate.greaterThanOrEqual=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate is less than or equal to
        defaultCropVarietyCropDurationFiltering(
            "addedDate.lessThanOrEqual=" + DEFAULT_ADDED_DATE,
            "addedDate.lessThanOrEqual=" + SMALLER_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate is less than
        defaultCropVarietyCropDurationFiltering("addedDate.lessThan=" + UPDATED_ADDED_DATE, "addedDate.lessThan=" + DEFAULT_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropDurationsByAddedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        // Get all the cropVarietyCropDurationList where addedDate is greater than
        defaultCropVarietyCropDurationFiltering(
            "addedDate.greaterThan=" + SMALLER_ADDED_DATE,
            "addedDate.greaterThan=" + DEFAULT_ADDED_DATE
        );
    }

    private void defaultCropVarietyCropDurationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropVarietyCropDurationShouldBeFound(shouldBeFound);
        defaultCropVarietyCropDurationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropVarietyCropDurationShouldBeFound(String filter) throws Exception {
        restCropVarietyCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVarietyCropDuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].stages").value(hasItem(DEFAULT_STAGES)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));

        // Check, that the count call also returns 1
        restCropVarietyCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropVarietyCropDurationShouldNotBeFound(String filter) throws Exception {
        restCropVarietyCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropVarietyCropDurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropVarietyCropDuration() throws Exception {
        // Get the cropVarietyCropDuration
        restCropVarietyCropDurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropVarietyCropDuration() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVarietyCropDuration
        CropVarietyCropDuration updatedCropVarietyCropDuration = cropVarietyCropDurationRepository
            .findById(cropVarietyCropDuration.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCropVarietyCropDuration are not directly saved in db
        em.detach(updatedCropVarietyCropDuration);
        updatedCropVarietyCropDuration
            .duration(UPDATED_DURATION)
            .name(UPDATED_NAME)
            .stages(UPDATED_STAGES)
            .addedBy(UPDATED_ADDED_BY)
            .addedDate(UPDATED_ADDED_DATE);

        restCropVarietyCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropVarietyCropDuration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropVarietyCropDuration))
            )
            .andExpect(status().isOk());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropVarietyCropDurationToMatchAllProperties(updatedCropVarietyCropDuration);
    }

    @Test
    @Transactional
    void putNonExistingCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropDuration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropVarietyCropDuration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropDurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropVarietyCropDurationWithPatch() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVarietyCropDuration using partial update
        CropVarietyCropDuration partialUpdatedCropVarietyCropDuration = new CropVarietyCropDuration();
        partialUpdatedCropVarietyCropDuration.setId(cropVarietyCropDuration.getId());

        partialUpdatedCropVarietyCropDuration.stages(UPDATED_STAGES).addedDate(UPDATED_ADDED_DATE);

        restCropVarietyCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVarietyCropDuration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVarietyCropDuration))
            )
            .andExpect(status().isOk());

        // Validate the CropVarietyCropDuration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyCropDurationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropVarietyCropDuration, cropVarietyCropDuration),
            getPersistedCropVarietyCropDuration(cropVarietyCropDuration)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropVarietyCropDurationWithPatch() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVarietyCropDuration using partial update
        CropVarietyCropDuration partialUpdatedCropVarietyCropDuration = new CropVarietyCropDuration();
        partialUpdatedCropVarietyCropDuration.setId(cropVarietyCropDuration.getId());

        partialUpdatedCropVarietyCropDuration
            .duration(UPDATED_DURATION)
            .name(UPDATED_NAME)
            .stages(UPDATED_STAGES)
            .addedBy(UPDATED_ADDED_BY)
            .addedDate(UPDATED_ADDED_DATE);

        restCropVarietyCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVarietyCropDuration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVarietyCropDuration))
            )
            .andExpect(status().isOk());

        // Validate the CropVarietyCropDuration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyCropDurationUpdatableFieldsEquals(
            partialUpdatedCropVarietyCropDuration,
            getPersistedCropVarietyCropDuration(partialUpdatedCropVarietyCropDuration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropDuration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropVarietyCropDuration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropVarietyCropDuration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropDuration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropDurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyCropDuration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVarietyCropDuration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropVarietyCropDuration() throws Exception {
        // Initialize the database
        insertedCropVarietyCropDuration = cropVarietyCropDurationRepository.saveAndFlush(cropVarietyCropDuration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropVarietyCropDuration
        restCropVarietyCropDurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropVarietyCropDuration.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropVarietyCropDurationRepository.count();
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

    protected CropVarietyCropDuration getPersistedCropVarietyCropDuration(CropVarietyCropDuration cropVarietyCropDuration) {
        return cropVarietyCropDurationRepository.findById(cropVarietyCropDuration.getId()).orElseThrow();
    }

    protected void assertPersistedCropVarietyCropDurationToMatchAllProperties(CropVarietyCropDuration expectedCropVarietyCropDuration) {
        assertCropVarietyCropDurationAllPropertiesEquals(
            expectedCropVarietyCropDuration,
            getPersistedCropVarietyCropDuration(expectedCropVarietyCropDuration)
        );
    }

    protected void assertPersistedCropVarietyCropDurationToMatchUpdatableProperties(
        CropVarietyCropDuration expectedCropVarietyCropDuration
    ) {
        assertCropVarietyCropDurationAllUpdatablePropertiesEquals(
            expectedCropVarietyCropDuration,
            getPersistedCropVarietyCropDuration(expectedCropVarietyCropDuration)
        );
    }
}
