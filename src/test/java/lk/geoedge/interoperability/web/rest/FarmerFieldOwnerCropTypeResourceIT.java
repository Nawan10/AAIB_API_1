package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.FarmerFieldOwnerCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerCropTypeRepository;
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
 * Integration tests for the {@link FarmerFieldOwnerCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmerFieldOwnerCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/farmer-field-owner-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmerFieldOwnerCropTypeMockMvc;

    private FarmerFieldOwnerCropType farmerFieldOwnerCropType;

    private FarmerFieldOwnerCropType insertedFarmerFieldOwnerCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FarmerFieldOwnerCropType createEntity() {
        return new FarmerFieldOwnerCropType()
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
    public static FarmerFieldOwnerCropType createUpdatedEntity() {
        return new FarmerFieldOwnerCropType()
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
        farmerFieldOwnerCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFarmerFieldOwnerCropType != null) {
            farmerFieldOwnerCropTypeRepository.delete(insertedFarmerFieldOwnerCropType);
            insertedFarmerFieldOwnerCropType = null;
        }
    }

    @Test
    @Transactional
    void createFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FarmerFieldOwnerCropType
        var returnedFarmerFieldOwnerCropType = om.readValue(
            restFarmerFieldOwnerCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FarmerFieldOwnerCropType.class
        );

        // Validate the FarmerFieldOwnerCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFarmerFieldOwnerCropTypeUpdatableFieldsEquals(
            returnedFarmerFieldOwnerCropType,
            getPersistedFarmerFieldOwnerCropType(returnedFarmerFieldOwnerCropType)
        );

        insertedFarmerFieldOwnerCropType = returnedFarmerFieldOwnerCropType;
    }

    @Test
    @Transactional
    void createFarmerFieldOwnerCropTypeWithExistingId() throws Exception {
        // Create the FarmerFieldOwnerCropType with an existing ID
        farmerFieldOwnerCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypes() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldOwnerCropType.getId().intValue())))
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
    void getFarmerFieldOwnerCropType() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get the farmerFieldOwnerCropType
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, farmerFieldOwnerCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmerFieldOwnerCropType.getId().intValue()))
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
    void getFarmerFieldOwnerCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        Long id = farmerFieldOwnerCropType.getId();

        defaultFarmerFieldOwnerCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFarmerFieldOwnerCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFarmerFieldOwnerCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where crop equals to
        defaultFarmerFieldOwnerCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where crop in
        defaultFarmerFieldOwnerCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where crop is not null
        defaultFarmerFieldOwnerCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where crop contains
        defaultFarmerFieldOwnerCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where crop does not contain
        defaultFarmerFieldOwnerCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where image equals to
        defaultFarmerFieldOwnerCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where image in
        defaultFarmerFieldOwnerCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where image is not null
        defaultFarmerFieldOwnerCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where image contains
        defaultFarmerFieldOwnerCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where image does not contain
        defaultFarmerFieldOwnerCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop equals to
        defaultFarmerFieldOwnerCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop is not null
        defaultFarmerFieldOwnerCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop is less than
        defaultFarmerFieldOwnerCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where mainCrop is greater than
        defaultFarmerFieldOwnerCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropCode equals to
        defaultFarmerFieldOwnerCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropCode in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropCode is not null
        defaultFarmerFieldOwnerCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropCode contains
        defaultFarmerFieldOwnerCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropCode does not contain
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropCode.doesNotContain=" + UPDATED_CROP_CODE,
            "cropCode.doesNotContain=" + DEFAULT_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where noOfStages equals to
        defaultFarmerFieldOwnerCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where noOfStages in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where noOfStages is not null
        defaultFarmerFieldOwnerCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where noOfStages contains
        defaultFarmerFieldOwnerCropTypeFiltering(
            "noOfStages.contains=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.contains=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where noOfStages does not contain
        defaultFarmerFieldOwnerCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where description equals to
        defaultFarmerFieldOwnerCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where description in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where description is not null
        defaultFarmerFieldOwnerCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where description contains
        defaultFarmerFieldOwnerCropTypeFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where description does not contain
        defaultFarmerFieldOwnerCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId equals to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId is not null
        defaultFarmerFieldOwnerCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId is less than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where cropTypesId is greater than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId equals to
        defaultFarmerFieldOwnerCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID,
            "unitsId.in=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId is not null
        defaultFarmerFieldOwnerCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId is less than
        defaultFarmerFieldOwnerCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where unitsId is greater than
        defaultFarmerFieldOwnerCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area equals to
        defaultFarmerFieldOwnerCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area in
        defaultFarmerFieldOwnerCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area is not null
        defaultFarmerFieldOwnerCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area is less than
        defaultFarmerFieldOwnerCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where area is greater than
        defaultFarmerFieldOwnerCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured equals to
        defaultFarmerFieldOwnerCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured is not null
        defaultFarmerFieldOwnerCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured is less than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "sumInsured.lessThan=" + UPDATED_SUM_INSURED,
            "sumInsured.lessThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where sumInsured is greater than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured equals to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured is not null
        defaultFarmerFieldOwnerCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured is less than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where minSumInsured is greater than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured equals to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured is not null
        defaultFarmerFieldOwnerCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured is less than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where maxSumInsured is greater than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate equals to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate in
        defaultFarmerFieldOwnerCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate is not null
        defaultFarmerFieldOwnerCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultFarmerFieldOwnerCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate is less than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnerCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        // Get all the farmerFieldOwnerCropTypeList where subsidisedPremiumRate is greater than
        defaultFarmerFieldOwnerCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultFarmerFieldOwnerCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFarmerFieldOwnerCropTypeShouldBeFound(shouldBeFound);
        defaultFarmerFieldOwnerCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmerFieldOwnerCropTypeShouldBeFound(String filter) throws Exception {
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldOwnerCropType.getId().intValue())))
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
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmerFieldOwnerCropTypeShouldNotBeFound(String filter) throws Exception {
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmerFieldOwnerCropType() throws Exception {
        // Get the farmerFieldOwnerCropType
        restFarmerFieldOwnerCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmerFieldOwnerCropType() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldOwnerCropType
        FarmerFieldOwnerCropType updatedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository
            .findById(farmerFieldOwnerCropType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedFarmerFieldOwnerCropType are not directly saved in db
        em.detach(updatedFarmerFieldOwnerCropType);
        updatedFarmerFieldOwnerCropType
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

        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFarmerFieldOwnerCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFarmerFieldOwnerCropType))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmerFieldOwnerCropTypeToMatchAllProperties(updatedFarmerFieldOwnerCropType);
    }

    @Test
    @Transactional
    void putNonExistingFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwnerCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmerFieldOwnerCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwnerCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwnerCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmerFieldOwnerCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldOwnerCropType using partial update
        FarmerFieldOwnerCropType partialUpdatedFarmerFieldOwnerCropType = new FarmerFieldOwnerCropType();
        partialUpdatedFarmerFieldOwnerCropType.setId(farmerFieldOwnerCropType.getId());

        partialUpdatedFarmerFieldOwnerCropType
            .description(UPDATED_DESCRIPTION)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED);

        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldOwnerCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldOwnerCropType))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldOwnerCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldOwnerCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFarmerFieldOwnerCropType, farmerFieldOwnerCropType),
            getPersistedFarmerFieldOwnerCropType(farmerFieldOwnerCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFarmerFieldOwnerCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldOwnerCropType using partial update
        FarmerFieldOwnerCropType partialUpdatedFarmerFieldOwnerCropType = new FarmerFieldOwnerCropType();
        partialUpdatedFarmerFieldOwnerCropType.setId(farmerFieldOwnerCropType.getId());

        partialUpdatedFarmerFieldOwnerCropType
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

        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldOwnerCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldOwnerCropType))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldOwnerCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldOwnerCropTypeUpdatableFieldsEquals(
            partialUpdatedFarmerFieldOwnerCropType,
            getPersistedFarmerFieldOwnerCropType(partialUpdatedFarmerFieldOwnerCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwnerCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmerFieldOwnerCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwnerCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmerFieldOwnerCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwnerCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldOwnerCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldOwnerCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmerFieldOwnerCropType() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwnerCropType = farmerFieldOwnerCropTypeRepository.saveAndFlush(farmerFieldOwnerCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmerFieldOwnerCropType
        restFarmerFieldOwnerCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmerFieldOwnerCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmerFieldOwnerCropTypeRepository.count();
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

    protected FarmerFieldOwnerCropType getPersistedFarmerFieldOwnerCropType(FarmerFieldOwnerCropType farmerFieldOwnerCropType) {
        return farmerFieldOwnerCropTypeRepository.findById(farmerFieldOwnerCropType.getId()).orElseThrow();
    }

    protected void assertPersistedFarmerFieldOwnerCropTypeToMatchAllProperties(FarmerFieldOwnerCropType expectedFarmerFieldOwnerCropType) {
        assertFarmerFieldOwnerCropTypeAllPropertiesEquals(
            expectedFarmerFieldOwnerCropType,
            getPersistedFarmerFieldOwnerCropType(expectedFarmerFieldOwnerCropType)
        );
    }

    protected void assertPersistedFarmerFieldOwnerCropTypeToMatchUpdatableProperties(
        FarmerFieldOwnerCropType expectedFarmerFieldOwnerCropType
    ) {
        assertFarmerFieldOwnerCropTypeAllUpdatablePropertiesEquals(
            expectedFarmerFieldOwnerCropType,
            getPersistedFarmerFieldOwnerCropType(expectedFarmerFieldOwnerCropType)
        );
    }
}
