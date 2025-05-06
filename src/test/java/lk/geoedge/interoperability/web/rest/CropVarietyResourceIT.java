package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropVarietyAsserts.*;
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
import lk.geoedge.interoperability.domain.CropVariety;
import lk.geoedge.interoperability.domain.CropVarietyCropDuration;
import lk.geoedge.interoperability.domain.CropVarietyCropType;
import lk.geoedge.interoperability.repository.CropVarietyRepository;
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
 * Integration tests for the {@link CropVarietyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropVarietyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NO_OF_STAGES = 1;
    private static final Integer UPDATED_NO_OF_STAGES = 2;
    private static final Integer SMALLER_NO_OF_STAGES = 1 - 1;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADDED_BY = 1;
    private static final Integer UPDATED_ADDED_BY = 2;
    private static final Integer SMALLER_ADDED_BY = 1 - 1;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/crop-varieties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropVarietyRepository cropVarietyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropVarietyMockMvc;

    private CropVariety cropVariety;

    private CropVariety insertedCropVariety;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropVariety createEntity() {
        return new CropVariety()
            .name(DEFAULT_NAME)
            .noOfStages(DEFAULT_NO_OF_STAGES)
            .image(DEFAULT_IMAGE)
            .description(DEFAULT_DESCRIPTION)
            .addedBy(DEFAULT_ADDED_BY)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropVariety createUpdatedEntity() {
        return new CropVariety()
            .name(UPDATED_NAME)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        cropVariety = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropVariety != null) {
            cropVarietyRepository.delete(insertedCropVariety);
            insertedCropVariety = null;
        }
    }

    @Test
    @Transactional
    void createCropVariety() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropVariety
        var returnedCropVariety = om.readValue(
            restCropVarietyMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVariety))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropVariety.class
        );

        // Validate the CropVariety in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropVarietyUpdatableFieldsEquals(returnedCropVariety, getPersistedCropVariety(returnedCropVariety));

        insertedCropVariety = returnedCropVariety;
    }

    @Test
    @Transactional
    void createCropVarietyWithExistingId() throws Exception {
        // Create the CropVariety with an existing ID
        cropVariety.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropVarietyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVariety)))
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropVarieties() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVariety.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].noOfStages").value(hasItem(DEFAULT_NO_OF_STAGES)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCropVariety() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get the cropVariety
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL_ID, cropVariety.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropVariety.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.noOfStages").value(DEFAULT_NO_OF_STAGES))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCropVarietiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        Long id = cropVariety.getId();

        defaultCropVarietyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropVarietyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropVarietyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name equals to
        defaultCropVarietyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name in
        defaultCropVarietyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name is not null
        defaultCropVarietyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name contains
        defaultCropVarietyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name does not contain
        defaultCropVarietyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages equals to
        defaultCropVarietyFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages in
        defaultCropVarietyFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages is not null
        defaultCropVarietyFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages is greater than or equal to
        defaultCropVarietyFiltering(
            "noOfStages.greaterThanOrEqual=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.greaterThanOrEqual=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages is less than or equal to
        defaultCropVarietyFiltering(
            "noOfStages.lessThanOrEqual=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.lessThanOrEqual=" + SMALLER_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages is less than
        defaultCropVarietyFiltering("noOfStages.lessThan=" + UPDATED_NO_OF_STAGES, "noOfStages.lessThan=" + DEFAULT_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNoOfStagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where noOfStages is greater than
        defaultCropVarietyFiltering("noOfStages.greaterThan=" + SMALLER_NO_OF_STAGES, "noOfStages.greaterThan=" + DEFAULT_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where image equals to
        defaultCropVarietyFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where image in
        defaultCropVarietyFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where image is not null
        defaultCropVarietyFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where image contains
        defaultCropVarietyFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where image does not contain
        defaultCropVarietyFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where description equals to
        defaultCropVarietyFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where description in
        defaultCropVarietyFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where description is not null
        defaultCropVarietyFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where description contains
        defaultCropVarietyFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where description does not contain
        defaultCropVarietyFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy equals to
        defaultCropVarietyFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy in
        defaultCropVarietyFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy is not null
        defaultCropVarietyFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy is greater than or equal to
        defaultCropVarietyFiltering("addedBy.greaterThanOrEqual=" + DEFAULT_ADDED_BY, "addedBy.greaterThanOrEqual=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy is less than or equal to
        defaultCropVarietyFiltering("addedBy.lessThanOrEqual=" + DEFAULT_ADDED_BY, "addedBy.lessThanOrEqual=" + SMALLER_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy is less than
        defaultCropVarietyFiltering("addedBy.lessThan=" + UPDATED_ADDED_BY, "addedBy.lessThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByAddedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where addedBy is greater than
        defaultCropVarietyFiltering("addedBy.greaterThan=" + SMALLER_ADDED_BY, "addedBy.greaterThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt equals to
        defaultCropVarietyFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt in
        defaultCropVarietyFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt is not null
        defaultCropVarietyFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt is greater than or equal to
        defaultCropVarietyFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt is less than or equal to
        defaultCropVarietyFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt is less than
        defaultCropVarietyFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where createdAt is greater than
        defaultCropVarietyFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCropIsEqualToSomething() throws Exception {
        CropVarietyCropType crop;
        if (TestUtil.findAll(em, CropVarietyCropType.class).isEmpty()) {
            cropVarietyRepository.saveAndFlush(cropVariety);
            crop = CropVarietyCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, CropVarietyCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        cropVariety.setCrop(crop);
        cropVarietyRepository.saveAndFlush(cropVariety);
        Long cropId = crop.getId();
        // Get all the cropVarietyList where crop equals to cropId
        defaultCropVarietyShouldBeFound("cropId.equals=" + cropId);

        // Get all the cropVarietyList where crop equals to (cropId + 1)
        defaultCropVarietyShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCropDurationIsEqualToSomething() throws Exception {
        CropVarietyCropDuration cropDuration;
        if (TestUtil.findAll(em, CropVarietyCropDuration.class).isEmpty()) {
            cropVarietyRepository.saveAndFlush(cropVariety);
            cropDuration = CropVarietyCropDurationResourceIT.createEntity();
        } else {
            cropDuration = TestUtil.findAll(em, CropVarietyCropDuration.class).get(0);
        }
        em.persist(cropDuration);
        em.flush();
        cropVariety.setCropDuration(cropDuration);
        cropVarietyRepository.saveAndFlush(cropVariety);
        Long cropDurationId = cropDuration.getId();
        // Get all the cropVarietyList where cropDuration equals to cropDurationId
        defaultCropVarietyShouldBeFound("cropDurationId.equals=" + cropDurationId);

        // Get all the cropVarietyList where cropDuration equals to (cropDurationId + 1)
        defaultCropVarietyShouldNotBeFound("cropDurationId.equals=" + (cropDurationId + 1));
    }

    private void defaultCropVarietyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropVarietyShouldBeFound(shouldBeFound);
        defaultCropVarietyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropVarietyShouldBeFound(String filter) throws Exception {
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVariety.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].noOfStages").value(hasItem(DEFAULT_NO_OF_STAGES)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropVarietyShouldNotBeFound(String filter) throws Exception {
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropVariety() throws Exception {
        // Get the cropVariety
        restCropVarietyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropVariety() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVariety
        CropVariety updatedCropVariety = cropVarietyRepository.findById(cropVariety.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropVariety are not directly saved in db
        em.detach(updatedCropVariety);
        updatedCropVariety
            .name(UPDATED_NAME)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropVariety.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropVarietyToMatchAllProperties(updatedCropVariety);
    }

    @Test
    @Transactional
    void putNonExistingCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropVariety.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVariety)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropVarietyWithPatch() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVariety using partial update
        CropVariety partialUpdatedCropVariety = new CropVariety();
        partialUpdatedCropVariety.setId(cropVariety.getId());

        partialUpdatedCropVariety.name(UPDATED_NAME).noOfStages(UPDATED_NO_OF_STAGES);

        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVariety.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the CropVariety in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropVariety, cropVariety),
            getPersistedCropVariety(cropVariety)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropVarietyWithPatch() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVariety using partial update
        CropVariety partialUpdatedCropVariety = new CropVariety();
        partialUpdatedCropVariety.setId(cropVariety.getId());

        partialUpdatedCropVariety
            .name(UPDATED_NAME)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVariety.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the CropVariety in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyUpdatableFieldsEquals(partialUpdatedCropVariety, getPersistedCropVariety(partialUpdatedCropVariety));
    }

    @Test
    @Transactional
    void patchNonExistingCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropVariety.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropVariety))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropVariety() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropVariety
        restCropVarietyMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropVariety.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropVarietyRepository.count();
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

    protected CropVariety getPersistedCropVariety(CropVariety cropVariety) {
        return cropVarietyRepository.findById(cropVariety.getId()).orElseThrow();
    }

    protected void assertPersistedCropVarietyToMatchAllProperties(CropVariety expectedCropVariety) {
        assertCropVarietyAllPropertiesEquals(expectedCropVariety, getPersistedCropVariety(expectedCropVariety));
    }

    protected void assertPersistedCropVarietyToMatchUpdatableProperties(CropVariety expectedCropVariety) {
        assertCropVarietyAllUpdatablePropertiesEquals(expectedCropVariety, getPersistedCropVariety(expectedCropVariety));
    }
}
