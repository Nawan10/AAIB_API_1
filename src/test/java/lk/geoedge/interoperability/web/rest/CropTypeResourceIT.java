package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CropType;
import lk.geoedge.interoperability.repository.CropTypeRepository;
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
 * Integration tests for the {@link CropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropTypeResourceIT {

    private static final String DEFAULT_CROP = "AAAAAAAAAA";
    private static final String UPDATED_CROP = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAIN_CROP = 1;
    private static final Integer UPDATED_MAIN_CROP = 2;
    private static final Integer SMALLER_MAIN_CROP = 1 - 1;

    private static final String DEFAULT_CROP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CROP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NO_OF_STAGES = "AAAAAAAAAA";
    private static final String UPDATED_NO_OF_STAGES = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CROP_TYPES_ID = 1;
    private static final Integer UPDATED_CROP_TYPES_ID = 2;
    private static final Integer SMALLER_CROP_TYPES_ID = 1 - 1;

    private static final Integer DEFAULT_UNITS_ID = 1;
    private static final Integer UPDATED_UNITS_ID = 2;
    private static final Integer SMALLER_UNITS_ID = 1 - 1;

    private static final Double DEFAULT_AREA = 1D;
    private static final Double UPDATED_AREA = 2D;
    private static final Double SMALLER_AREA = 1D - 1D;

    private static final Double DEFAULT_SUM_INSURED = 1D;
    private static final Double UPDATED_SUM_INSURED = 2D;
    private static final Double SMALLER_SUM_INSURED = 1D - 1D;

    private static final Double DEFAULT_MIN_SUM_INSURED = 1D;
    private static final Double UPDATED_MIN_SUM_INSURED = 2D;
    private static final Double SMALLER_MIN_SUM_INSURED = 1D - 1D;

    private static final Double DEFAULT_MAX_SUM_INSURED = 1D;
    private static final Double UPDATED_MAX_SUM_INSURED = 2D;
    private static final Double SMALLER_MAX_SUM_INSURED = 1D - 1D;

    private static final Double DEFAULT_SUBSIDISED_PREMIUM_RATE = 1D;
    private static final Double UPDATED_SUBSIDISED_PREMIUM_RATE = 2D;
    private static final Double SMALLER_SUBSIDISED_PREMIUM_RATE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropTypeRepository cropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropTypeMockMvc;

    private CropType cropType;

    private CropType insertedCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropType createEntity() {
        return new CropType()
            .crop(DEFAULT_CROP)
            .image(DEFAULT_IMAGE)
            .mainCrop(DEFAULT_MAIN_CROP)
            .cropCode(DEFAULT_CROP_CODE)
            .noOfStages(DEFAULT_NO_OF_STAGES)
            .description(DEFAULT_DESCRIPTION)
            .cropTypesId(DEFAULT_CROP_TYPES_ID)
            .unitsId(DEFAULT_UNITS_ID)
            .area(DEFAULT_AREA)
            .sumInsured(DEFAULT_SUM_INSURED)
            .minSumInsured(DEFAULT_MIN_SUM_INSURED)
            .maxSumInsured(DEFAULT_MAX_SUM_INSURED)
            .subsidisedPremiumRate(DEFAULT_SUBSIDISED_PREMIUM_RATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropType createUpdatedEntity() {
        return new CropType()
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .description(UPDATED_DESCRIPTION)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);
    }

    @BeforeEach
    void initTest() {
        cropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropType != null) {
            cropTypeRepository.delete(insertedCropType);
            insertedCropType = null;
        }
    }

    @Test
    @Transactional
    void createCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropType
        var returnedCropType = om.readValue(
            restCropTypeMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropType.class
        );

        // Validate the CropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropTypeUpdatableFieldsEquals(returnedCropType, getPersistedCropType(returnedCropType));

        insertedCropType = returnedCropType;
    }

    @Test
    @Transactional
    void createCropTypeWithExistingId() throws Exception {
        // Create the CropType with an existing ID
        cropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropTypeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropType)))
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropTypes() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropType.getId().intValue())))
            .andExpect(jsonPath("$.[*].crop").value(hasItem(DEFAULT_CROP)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].mainCrop").value(hasItem(DEFAULT_MAIN_CROP)))
            .andExpect(jsonPath("$.[*].cropCode").value(hasItem(DEFAULT_CROP_CODE)))
            .andExpect(jsonPath("$.[*].noOfStages").value(hasItem(DEFAULT_NO_OF_STAGES)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cropTypesId").value(hasItem(DEFAULT_CROP_TYPES_ID)))
            .andExpect(jsonPath("$.[*].unitsId").value(hasItem(DEFAULT_UNITS_ID)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].sumInsured").value(hasItem(DEFAULT_SUM_INSURED)))
            .andExpect(jsonPath("$.[*].minSumInsured").value(hasItem(DEFAULT_MIN_SUM_INSURED)))
            .andExpect(jsonPath("$.[*].maxSumInsured").value(hasItem(DEFAULT_MAX_SUM_INSURED)))
            .andExpect(jsonPath("$.[*].subsidisedPremiumRate").value(hasItem(DEFAULT_SUBSIDISED_PREMIUM_RATE)));
    }

    @Test
    @Transactional
    void getCropType() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get the cropType
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropType.getId().intValue()))
            .andExpect(jsonPath("$.crop").value(DEFAULT_CROP))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.mainCrop").value(DEFAULT_MAIN_CROP))
            .andExpect(jsonPath("$.cropCode").value(DEFAULT_CROP_CODE))
            .andExpect(jsonPath("$.noOfStages").value(DEFAULT_NO_OF_STAGES))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cropTypesId").value(DEFAULT_CROP_TYPES_ID))
            .andExpect(jsonPath("$.unitsId").value(DEFAULT_UNITS_ID))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
            .andExpect(jsonPath("$.sumInsured").value(DEFAULT_SUM_INSURED))
            .andExpect(jsonPath("$.minSumInsured").value(DEFAULT_MIN_SUM_INSURED))
            .andExpect(jsonPath("$.maxSumInsured").value(DEFAULT_MAX_SUM_INSURED))
            .andExpect(jsonPath("$.subsidisedPremiumRate").value(DEFAULT_SUBSIDISED_PREMIUM_RATE));
    }

    @Test
    @Transactional
    void getCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        Long id = cropType.getId();

        defaultCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where crop equals to
        defaultCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where crop in
        defaultCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where crop is not null
        defaultCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where crop contains
        defaultCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where crop does not contain
        defaultCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where image equals to
        defaultCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where image in
        defaultCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where image is not null
        defaultCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where image contains
        defaultCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where image does not contain
        defaultCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop equals to
        defaultCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop in
        defaultCropTypeFiltering("mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP, "mainCrop.in=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop is not null
        defaultCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop is greater than or equal to
        defaultCropTypeFiltering("mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP, "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop is less than or equal to
        defaultCropTypeFiltering("mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP, "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop is less than
        defaultCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where mainCrop is greater than
        defaultCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropCode equals to
        defaultCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropCode in
        defaultCropTypeFiltering("cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE, "cropCode.in=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropCode is not null
        defaultCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropCode contains
        defaultCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropCode does not contain
        defaultCropTypeFiltering("cropCode.doesNotContain=" + UPDATED_CROP_CODE, "cropCode.doesNotContain=" + DEFAULT_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where noOfStages equals to
        defaultCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where noOfStages in
        defaultCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where noOfStages is not null
        defaultCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where noOfStages contains
        defaultCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where noOfStages does not contain
        defaultCropTypeFiltering("noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES, "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where description equals to
        defaultCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where description in
        defaultCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where description is not null
        defaultCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where description contains
        defaultCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where description does not contain
        defaultCropTypeFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId equals to
        defaultCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId in
        defaultCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId is not null
        defaultCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId is greater than or equal to
        defaultCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId is less than or equal to
        defaultCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId is less than
        defaultCropTypeFiltering("cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID, "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where cropTypesId is greater than
        defaultCropTypeFiltering("cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID, "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId equals to
        defaultCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId in
        defaultCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId is not null
        defaultCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId is greater than or equal to
        defaultCropTypeFiltering("unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId is less than or equal to
        defaultCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId is less than
        defaultCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where unitsId is greater than
        defaultCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area equals to
        defaultCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area in
        defaultCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area is not null
        defaultCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area is greater than or equal to
        defaultCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area is less than or equal to
        defaultCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area is less than
        defaultCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where area is greater than
        defaultCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured equals to
        defaultCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured in
        defaultCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured is not null
        defaultCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured is greater than or equal to
        defaultCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured is less than or equal to
        defaultCropTypeFiltering("sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED, "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured is less than
        defaultCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where sumInsured is greater than
        defaultCropTypeFiltering("sumInsured.greaterThan=" + SMALLER_SUM_INSURED, "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured equals to
        defaultCropTypeFiltering("minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED, "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured in
        defaultCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured is not null
        defaultCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured is greater than or equal to
        defaultCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured is less than or equal to
        defaultCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured is less than
        defaultCropTypeFiltering("minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED, "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where minSumInsured is greater than
        defaultCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured equals to
        defaultCropTypeFiltering("maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED, "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured in
        defaultCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured is not null
        defaultCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured is greater than or equal to
        defaultCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured is less than or equal to
        defaultCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured is less than
        defaultCropTypeFiltering("maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED, "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where maxSumInsured is greater than
        defaultCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate equals to
        defaultCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate in
        defaultCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate is not null
        defaultCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate is less than or equal to
        defaultCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate is less than
        defaultCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        // Get all the cropTypeList where subsidisedPremiumRate is greater than
        defaultCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropTypeShouldBeFound(shouldBeFound);
        defaultCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropTypeShouldBeFound(String filter) throws Exception {
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropType.getId().intValue())))
            .andExpect(jsonPath("$.[*].crop").value(hasItem(DEFAULT_CROP)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].mainCrop").value(hasItem(DEFAULT_MAIN_CROP)))
            .andExpect(jsonPath("$.[*].cropCode").value(hasItem(DEFAULT_CROP_CODE)))
            .andExpect(jsonPath("$.[*].noOfStages").value(hasItem(DEFAULT_NO_OF_STAGES)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cropTypesId").value(hasItem(DEFAULT_CROP_TYPES_ID)))
            .andExpect(jsonPath("$.[*].unitsId").value(hasItem(DEFAULT_UNITS_ID)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].sumInsured").value(hasItem(DEFAULT_SUM_INSURED)))
            .andExpect(jsonPath("$.[*].minSumInsured").value(hasItem(DEFAULT_MIN_SUM_INSURED)))
            .andExpect(jsonPath("$.[*].maxSumInsured").value(hasItem(DEFAULT_MAX_SUM_INSURED)))
            .andExpect(jsonPath("$.[*].subsidisedPremiumRate").value(hasItem(DEFAULT_SUBSIDISED_PREMIUM_RATE)));

        // Check, that the count call also returns 1
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropTypeShouldNotBeFound(String filter) throws Exception {
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropType() throws Exception {
        // Get the cropType
        restCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropType() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropType
        CropType updatedCropType = cropTypeRepository.findById(cropType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropType are not directly saved in db
        em.detach(updatedCropType);
        updatedCropType
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .description(UPDATED_DESCRIPTION)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropTypeToMatchAllProperties(updatedCropType);
    }

    @Test
    @Transactional
    void putNonExistingCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropType using partial update
        CropType partialUpdatedCropType = new CropType();
        partialUpdatedCropType.setId(cropType.getId());

        partialUpdatedCropType
            .image(UPDATED_IMAGE)
            .cropCode(UPDATED_CROP_CODE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .description(UPDATED_DESCRIPTION)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED);

        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCropType, cropType), getPersistedCropType(cropType));
    }

    @Test
    @Transactional
    void fullUpdateCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropType using partial update
        CropType partialUpdatedCropType = new CropType();
        partialUpdatedCropType.setId(cropType.getId());

        partialUpdatedCropType
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .description(UPDATED_DESCRIPTION)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropTypeUpdatableFieldsEquals(partialUpdatedCropType, getPersistedCropType(partialUpdatedCropType));
    }

    @Test
    @Transactional
    void patchNonExistingCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropTypeMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropType() throws Exception {
        // Initialize the database
        insertedCropType = cropTypeRepository.saveAndFlush(cropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropType
        restCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropTypeRepository.count();
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

    protected CropType getPersistedCropType(CropType cropType) {
        return cropTypeRepository.findById(cropType.getId()).orElseThrow();
    }

    protected void assertPersistedCropTypeToMatchAllProperties(CropType expectedCropType) {
        assertCropTypeAllPropertiesEquals(expectedCropType, getPersistedCropType(expectedCropType));
    }

    protected void assertPersistedCropTypeToMatchUpdatableProperties(CropType expectedCropType) {
        assertCropTypeAllUpdatablePropertiesEquals(expectedCropType, getPersistedCropType(expectedCropType));
    }
}
