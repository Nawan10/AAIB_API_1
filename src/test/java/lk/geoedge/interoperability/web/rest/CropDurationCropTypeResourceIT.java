package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropDurationCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CropDurationCropType;
import lk.geoedge.interoperability.repository.CropDurationCropTypeRepository;
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
 * Integration tests for the {@link CropDurationCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropDurationCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/crop-duration-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropDurationCropTypeRepository cropDurationCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropDurationCropTypeMockMvc;

    private CropDurationCropType cropDurationCropType;

    private CropDurationCropType insertedCropDurationCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDurationCropType createEntity() {
        return new CropDurationCropType()
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
    public static CropDurationCropType createUpdatedEntity() {
        return new CropDurationCropType()
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
        cropDurationCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropDurationCropType != null) {
            cropDurationCropTypeRepository.delete(insertedCropDurationCropType);
            insertedCropDurationCropType = null;
        }
    }

    @Test
    @Transactional
    void createCropDurationCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropDurationCropType
        var returnedCropDurationCropType = om.readValue(
            restCropDurationCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cropDurationCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropDurationCropType.class
        );

        // Validate the CropDurationCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropDurationCropTypeUpdatableFieldsEquals(
            returnedCropDurationCropType,
            getPersistedCropDurationCropType(returnedCropDurationCropType)
        );

        insertedCropDurationCropType = returnedCropDurationCropType;
    }

    @Test
    @Transactional
    void createCropDurationCropTypeWithExistingId() throws Exception {
        // Create the CropDurationCropType with an existing ID
        cropDurationCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropDurationCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypes() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList
        restCropDurationCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDurationCropType.getId().intValue())))
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
    void getCropDurationCropType() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get the cropDurationCropType
        restCropDurationCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cropDurationCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropDurationCropType.getId().intValue()))
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
    void getCropDurationCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        Long id = cropDurationCropType.getId();

        defaultCropDurationCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropDurationCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropDurationCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where crop equals to
        defaultCropDurationCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where crop in
        defaultCropDurationCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where crop is not null
        defaultCropDurationCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where crop contains
        defaultCropDurationCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where crop does not contain
        defaultCropDurationCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where image equals to
        defaultCropDurationCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where image in
        defaultCropDurationCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where image is not null
        defaultCropDurationCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where image contains
        defaultCropDurationCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where image does not contain
        defaultCropDurationCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop equals to
        defaultCropDurationCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop in
        defaultCropDurationCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop is not null
        defaultCropDurationCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop is less than or equal to
        defaultCropDurationCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop is less than
        defaultCropDurationCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where mainCrop is greater than
        defaultCropDurationCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropCode equals to
        defaultCropDurationCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropCode in
        defaultCropDurationCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropCode is not null
        defaultCropDurationCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropCode contains
        defaultCropDurationCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropCode does not contain
        defaultCropDurationCropTypeFiltering(
            "cropCode.doesNotContain=" + UPDATED_CROP_CODE,
            "cropCode.doesNotContain=" + DEFAULT_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where noOfStages equals to
        defaultCropDurationCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where noOfStages in
        defaultCropDurationCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where noOfStages is not null
        defaultCropDurationCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where noOfStages contains
        defaultCropDurationCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where noOfStages does not contain
        defaultCropDurationCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where description equals to
        defaultCropDurationCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where description in
        defaultCropDurationCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where description is not null
        defaultCropDurationCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where description contains
        defaultCropDurationCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where description does not contain
        defaultCropDurationCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId equals to
        defaultCropDurationCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId in
        defaultCropDurationCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId is not null
        defaultCropDurationCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId is less than or equal to
        defaultCropDurationCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId is less than
        defaultCropDurationCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where cropTypesId is greater than
        defaultCropDurationCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId equals to
        defaultCropDurationCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId in
        defaultCropDurationCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId is not null
        defaultCropDurationCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId is less than or equal to
        defaultCropDurationCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId is less than
        defaultCropDurationCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where unitsId is greater than
        defaultCropDurationCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area equals to
        defaultCropDurationCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area in
        defaultCropDurationCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area is not null
        defaultCropDurationCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area is greater than or equal to
        defaultCropDurationCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area is less than or equal to
        defaultCropDurationCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area is less than
        defaultCropDurationCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where area is greater than
        defaultCropDurationCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured equals to
        defaultCropDurationCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured in
        defaultCropDurationCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured is not null
        defaultCropDurationCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured is less than or equal to
        defaultCropDurationCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured is less than
        defaultCropDurationCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where sumInsured is greater than
        defaultCropDurationCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured equals to
        defaultCropDurationCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured in
        defaultCropDurationCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured is not null
        defaultCropDurationCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured is less than or equal to
        defaultCropDurationCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured is less than
        defaultCropDurationCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where minSumInsured is greater than
        defaultCropDurationCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured equals to
        defaultCropDurationCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured in
        defaultCropDurationCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured is not null
        defaultCropDurationCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured is less than or equal to
        defaultCropDurationCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured is less than
        defaultCropDurationCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where maxSumInsured is greater than
        defaultCropDurationCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate equals to
        defaultCropDurationCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate in
        defaultCropDurationCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate is not null
        defaultCropDurationCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultCropDurationCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultCropDurationCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate is less than
        defaultCropDurationCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDurationCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        // Get all the cropDurationCropTypeList where subsidisedPremiumRate is greater than
        defaultCropDurationCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultCropDurationCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropDurationCropTypeShouldBeFound(shouldBeFound);
        defaultCropDurationCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropDurationCropTypeShouldBeFound(String filter) throws Exception {
        restCropDurationCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDurationCropType.getId().intValue())))
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
        restCropDurationCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropDurationCropTypeShouldNotBeFound(String filter) throws Exception {
        restCropDurationCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropDurationCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropDurationCropType() throws Exception {
        // Get the cropDurationCropType
        restCropDurationCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropDurationCropType() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDurationCropType
        CropDurationCropType updatedCropDurationCropType = cropDurationCropTypeRepository
            .findById(cropDurationCropType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCropDurationCropType are not directly saved in db
        em.detach(updatedCropDurationCropType);
        updatedCropDurationCropType
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

        restCropDurationCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropDurationCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropDurationCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropDurationCropTypeToMatchAllProperties(updatedCropDurationCropType);
    }

    @Test
    @Transactional
    void putNonExistingCropDurationCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDurationCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDurationCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropDurationCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropDurationCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDurationCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropDurationCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDurationCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropDurationCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDurationCropType using partial update
        CropDurationCropType partialUpdatedCropDurationCropType = new CropDurationCropType();
        partialUpdatedCropDurationCropType.setId(cropDurationCropType.getId());

        partialUpdatedCropDurationCropType
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .description(UPDATED_DESCRIPTION)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCropDurationCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDurationCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDurationCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropDurationCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDurationCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropDurationCropType, cropDurationCropType),
            getPersistedCropDurationCropType(cropDurationCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropDurationCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDurationCropType using partial update
        CropDurationCropType partialUpdatedCropDurationCropType = new CropDurationCropType();
        partialUpdatedCropDurationCropType.setId(cropDurationCropType.getId());

        partialUpdatedCropDurationCropType
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

        restCropDurationCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDurationCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDurationCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropDurationCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDurationCropTypeUpdatableFieldsEquals(
            partialUpdatedCropDurationCropType,
            getPersistedCropDurationCropType(partialUpdatedCropDurationCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCropDurationCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDurationCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDurationCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropDurationCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropDurationCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDurationCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropDurationCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDurationCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDurationCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDurationCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDurationCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropDurationCropType() throws Exception {
        // Initialize the database
        insertedCropDurationCropType = cropDurationCropTypeRepository.saveAndFlush(cropDurationCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropDurationCropType
        restCropDurationCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropDurationCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropDurationCropTypeRepository.count();
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

    protected CropDurationCropType getPersistedCropDurationCropType(CropDurationCropType cropDurationCropType) {
        return cropDurationCropTypeRepository.findById(cropDurationCropType.getId()).orElseThrow();
    }

    protected void assertPersistedCropDurationCropTypeToMatchAllProperties(CropDurationCropType expectedCropDurationCropType) {
        assertCropDurationCropTypeAllPropertiesEquals(
            expectedCropDurationCropType,
            getPersistedCropDurationCropType(expectedCropDurationCropType)
        );
    }

    protected void assertPersistedCropDurationCropTypeToMatchUpdatableProperties(CropDurationCropType expectedCropDurationCropType) {
        assertCropDurationCropTypeAllUpdatablePropertiesEquals(
            expectedCropDurationCropType,
            getPersistedCropDurationCropType(expectedCropDurationCropType)
        );
    }
}
