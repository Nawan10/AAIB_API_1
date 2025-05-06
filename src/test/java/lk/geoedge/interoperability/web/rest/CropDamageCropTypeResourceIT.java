package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropDamageCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CropDamageCropType;
import lk.geoedge.interoperability.repository.CropDamageCropTypeRepository;
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
 * Integration tests for the {@link CropDamageCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropDamageCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/crop-damage-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropDamageCropTypeRepository cropDamageCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropDamageCropTypeMockMvc;

    private CropDamageCropType cropDamageCropType;

    private CropDamageCropType insertedCropDamageCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDamageCropType createEntity() {
        return new CropDamageCropType()
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
    public static CropDamageCropType createUpdatedEntity() {
        return new CropDamageCropType()
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
        cropDamageCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropDamageCropType != null) {
            cropDamageCropTypeRepository.delete(insertedCropDamageCropType);
            insertedCropDamageCropType = null;
        }
    }

    @Test
    @Transactional
    void createCropDamageCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropDamageCropType
        var returnedCropDamageCropType = om.readValue(
            restCropDamageCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cropDamageCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropDamageCropType.class
        );

        // Validate the CropDamageCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropDamageCropTypeUpdatableFieldsEquals(
            returnedCropDamageCropType,
            getPersistedCropDamageCropType(returnedCropDamageCropType)
        );

        insertedCropDamageCropType = returnedCropDamageCropType;
    }

    @Test
    @Transactional
    void createCropDamageCropTypeWithExistingId() throws Exception {
        // Create the CropDamageCropType with an existing ID
        cropDamageCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropDamageCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypes() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList
        restCropDamageCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDamageCropType.getId().intValue())))
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
    void getCropDamageCropType() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get the cropDamageCropType
        restCropDamageCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cropDamageCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropDamageCropType.getId().intValue()))
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
    void getCropDamageCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        Long id = cropDamageCropType.getId();

        defaultCropDamageCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropDamageCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropDamageCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where crop equals to
        defaultCropDamageCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where crop in
        defaultCropDamageCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where crop is not null
        defaultCropDamageCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where crop contains
        defaultCropDamageCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where crop does not contain
        defaultCropDamageCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where image equals to
        defaultCropDamageCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where image in
        defaultCropDamageCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where image is not null
        defaultCropDamageCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where image contains
        defaultCropDamageCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where image does not contain
        defaultCropDamageCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop equals to
        defaultCropDamageCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop in
        defaultCropDamageCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop is not null
        defaultCropDamageCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop is less than or equal to
        defaultCropDamageCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop is less than
        defaultCropDamageCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where mainCrop is greater than
        defaultCropDamageCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropCode equals to
        defaultCropDamageCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropCode in
        defaultCropDamageCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropCode is not null
        defaultCropDamageCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropCode contains
        defaultCropDamageCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropCode does not contain
        defaultCropDamageCropTypeFiltering("cropCode.doesNotContain=" + UPDATED_CROP_CODE, "cropCode.doesNotContain=" + DEFAULT_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where noOfStages equals to
        defaultCropDamageCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where noOfStages in
        defaultCropDamageCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where noOfStages is not null
        defaultCropDamageCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where noOfStages contains
        defaultCropDamageCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where noOfStages does not contain
        defaultCropDamageCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where description equals to
        defaultCropDamageCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where description in
        defaultCropDamageCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where description is not null
        defaultCropDamageCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where description contains
        defaultCropDamageCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where description does not contain
        defaultCropDamageCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId equals to
        defaultCropDamageCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId in
        defaultCropDamageCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId is not null
        defaultCropDamageCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId is less than or equal to
        defaultCropDamageCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId is less than
        defaultCropDamageCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where cropTypesId is greater than
        defaultCropDamageCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId equals to
        defaultCropDamageCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId in
        defaultCropDamageCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId is not null
        defaultCropDamageCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId is less than or equal to
        defaultCropDamageCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId is less than
        defaultCropDamageCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where unitsId is greater than
        defaultCropDamageCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area equals to
        defaultCropDamageCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area in
        defaultCropDamageCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area is not null
        defaultCropDamageCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area is greater than or equal to
        defaultCropDamageCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area is less than or equal to
        defaultCropDamageCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area is less than
        defaultCropDamageCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where area is greater than
        defaultCropDamageCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured equals to
        defaultCropDamageCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured in
        defaultCropDamageCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured is not null
        defaultCropDamageCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured is less than or equal to
        defaultCropDamageCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured is less than
        defaultCropDamageCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where sumInsured is greater than
        defaultCropDamageCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured equals to
        defaultCropDamageCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured in
        defaultCropDamageCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured is not null
        defaultCropDamageCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured is less than or equal to
        defaultCropDamageCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured is less than
        defaultCropDamageCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where minSumInsured is greater than
        defaultCropDamageCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured equals to
        defaultCropDamageCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured in
        defaultCropDamageCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured is not null
        defaultCropDamageCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured is less than or equal to
        defaultCropDamageCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured is less than
        defaultCropDamageCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where maxSumInsured is greater than
        defaultCropDamageCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate equals to
        defaultCropDamageCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate in
        defaultCropDamageCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate is not null
        defaultCropDamageCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultCropDamageCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultCropDamageCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate is less than
        defaultCropDamageCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        // Get all the cropDamageCropTypeList where subsidisedPremiumRate is greater than
        defaultCropDamageCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultCropDamageCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropDamageCropTypeShouldBeFound(shouldBeFound);
        defaultCropDamageCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropDamageCropTypeShouldBeFound(String filter) throws Exception {
        restCropDamageCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDamageCropType.getId().intValue())))
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
        restCropDamageCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropDamageCropTypeShouldNotBeFound(String filter) throws Exception {
        restCropDamageCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropDamageCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropDamageCropType() throws Exception {
        // Get the cropDamageCropType
        restCropDamageCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropDamageCropType() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamageCropType
        CropDamageCropType updatedCropDamageCropType = cropDamageCropTypeRepository.findById(cropDamageCropType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropDamageCropType are not directly saved in db
        em.detach(updatedCropDamageCropType);
        updatedCropDamageCropType
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

        restCropDamageCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropDamageCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropDamageCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropDamageCropTypeToMatchAllProperties(updatedCropDamageCropType);
    }

    @Test
    @Transactional
    void putNonExistingCropDamageCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDamageCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropDamageCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropDamageCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropDamageCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropDamageCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamageCropType using partial update
        CropDamageCropType partialUpdatedCropDamageCropType = new CropDamageCropType();
        partialUpdatedCropDamageCropType.setId(cropDamageCropType.getId());

        partialUpdatedCropDamageCropType
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .description(UPDATED_DESCRIPTION)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCropDamageCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDamageCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDamageCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropDamageCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDamageCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropDamageCropType, cropDamageCropType),
            getPersistedCropDamageCropType(cropDamageCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropDamageCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamageCropType using partial update
        CropDamageCropType partialUpdatedCropDamageCropType = new CropDamageCropType();
        partialUpdatedCropDamageCropType.setId(cropDamageCropType.getId());

        partialUpdatedCropDamageCropType
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

        restCropDamageCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDamageCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDamageCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropDamageCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDamageCropTypeUpdatableFieldsEquals(
            partialUpdatedCropDamageCropType,
            getPersistedCropDamageCropType(partialUpdatedCropDamageCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCropDamageCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDamageCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropDamageCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropDamageCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropDamageCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamageCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDamageCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropDamageCropType() throws Exception {
        // Initialize the database
        insertedCropDamageCropType = cropDamageCropTypeRepository.saveAndFlush(cropDamageCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropDamageCropType
        restCropDamageCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropDamageCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropDamageCropTypeRepository.count();
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

    protected CropDamageCropType getPersistedCropDamageCropType(CropDamageCropType cropDamageCropType) {
        return cropDamageCropTypeRepository.findById(cropDamageCropType.getId()).orElseThrow();
    }

    protected void assertPersistedCropDamageCropTypeToMatchAllProperties(CropDamageCropType expectedCropDamageCropType) {
        assertCropDamageCropTypeAllPropertiesEquals(expectedCropDamageCropType, getPersistedCropDamageCropType(expectedCropDamageCropType));
    }

    protected void assertPersistedCropDamageCropTypeToMatchUpdatableProperties(CropDamageCropType expectedCropDamageCropType) {
        assertCropDamageCropTypeAllUpdatablePropertiesEquals(
            expectedCropDamageCropType,
            getPersistedCropDamageCropType(expectedCropDamageCropType)
        );
    }
}
