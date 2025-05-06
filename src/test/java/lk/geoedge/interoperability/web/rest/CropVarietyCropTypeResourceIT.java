package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropVarietyCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CropVarietyCropType;
import lk.geoedge.interoperability.repository.CropVarietyCropTypeRepository;
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
 * Integration tests for the {@link CropVarietyCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropVarietyCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/crop-variety-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropVarietyCropTypeRepository cropVarietyCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropVarietyCropTypeMockMvc;

    private CropVarietyCropType cropVarietyCropType;

    private CropVarietyCropType insertedCropVarietyCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropVarietyCropType createEntity() {
        return new CropVarietyCropType()
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
    public static CropVarietyCropType createUpdatedEntity() {
        return new CropVarietyCropType()
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
        cropVarietyCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropVarietyCropType != null) {
            cropVarietyCropTypeRepository.delete(insertedCropVarietyCropType);
            insertedCropVarietyCropType = null;
        }
    }

    @Test
    @Transactional
    void createCropVarietyCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropVarietyCropType
        var returnedCropVarietyCropType = om.readValue(
            restCropVarietyCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cropVarietyCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropVarietyCropType.class
        );

        // Validate the CropVarietyCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropVarietyCropTypeUpdatableFieldsEquals(
            returnedCropVarietyCropType,
            getPersistedCropVarietyCropType(returnedCropVarietyCropType)
        );

        insertedCropVarietyCropType = returnedCropVarietyCropType;
    }

    @Test
    @Transactional
    void createCropVarietyCropTypeWithExistingId() throws Exception {
        // Create the CropVarietyCropType with an existing ID
        cropVarietyCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropVarietyCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypes() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList
        restCropVarietyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVarietyCropType.getId().intValue())))
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
    void getCropVarietyCropType() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get the cropVarietyCropType
        restCropVarietyCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cropVarietyCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropVarietyCropType.getId().intValue()))
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
    void getCropVarietyCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        Long id = cropVarietyCropType.getId();

        defaultCropVarietyCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropVarietyCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropVarietyCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where crop equals to
        defaultCropVarietyCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where crop in
        defaultCropVarietyCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where crop is not null
        defaultCropVarietyCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where crop contains
        defaultCropVarietyCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where crop does not contain
        defaultCropVarietyCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where image equals to
        defaultCropVarietyCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where image in
        defaultCropVarietyCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where image is not null
        defaultCropVarietyCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where image contains
        defaultCropVarietyCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where image does not contain
        defaultCropVarietyCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop equals to
        defaultCropVarietyCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop in
        defaultCropVarietyCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop is not null
        defaultCropVarietyCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop is less than or equal to
        defaultCropVarietyCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop is less than
        defaultCropVarietyCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where mainCrop is greater than
        defaultCropVarietyCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropCode equals to
        defaultCropVarietyCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropCode in
        defaultCropVarietyCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropCode is not null
        defaultCropVarietyCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropCode contains
        defaultCropVarietyCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropCode does not contain
        defaultCropVarietyCropTypeFiltering("cropCode.doesNotContain=" + UPDATED_CROP_CODE, "cropCode.doesNotContain=" + DEFAULT_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where noOfStages equals to
        defaultCropVarietyCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where noOfStages in
        defaultCropVarietyCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where noOfStages is not null
        defaultCropVarietyCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where noOfStages contains
        defaultCropVarietyCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where noOfStages does not contain
        defaultCropVarietyCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where description equals to
        defaultCropVarietyCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where description in
        defaultCropVarietyCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where description is not null
        defaultCropVarietyCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where description contains
        defaultCropVarietyCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where description does not contain
        defaultCropVarietyCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId equals to
        defaultCropVarietyCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId in
        defaultCropVarietyCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId is not null
        defaultCropVarietyCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId is less than or equal to
        defaultCropVarietyCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId is less than
        defaultCropVarietyCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where cropTypesId is greater than
        defaultCropVarietyCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId equals to
        defaultCropVarietyCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId in
        defaultCropVarietyCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId is not null
        defaultCropVarietyCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId is less than or equal to
        defaultCropVarietyCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId is less than
        defaultCropVarietyCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where unitsId is greater than
        defaultCropVarietyCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area equals to
        defaultCropVarietyCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area in
        defaultCropVarietyCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area is not null
        defaultCropVarietyCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area is greater than or equal to
        defaultCropVarietyCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area is less than or equal to
        defaultCropVarietyCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area is less than
        defaultCropVarietyCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where area is greater than
        defaultCropVarietyCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured equals to
        defaultCropVarietyCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured in
        defaultCropVarietyCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured is not null
        defaultCropVarietyCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured is less than or equal to
        defaultCropVarietyCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured is less than
        defaultCropVarietyCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where sumInsured is greater than
        defaultCropVarietyCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured equals to
        defaultCropVarietyCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured in
        defaultCropVarietyCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured is not null
        defaultCropVarietyCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured is less than or equal to
        defaultCropVarietyCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured is less than
        defaultCropVarietyCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where minSumInsured is greater than
        defaultCropVarietyCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured equals to
        defaultCropVarietyCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured in
        defaultCropVarietyCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured is not null
        defaultCropVarietyCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured is less than or equal to
        defaultCropVarietyCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured is less than
        defaultCropVarietyCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where maxSumInsured is greater than
        defaultCropVarietyCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate equals to
        defaultCropVarietyCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate in
        defaultCropVarietyCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate is not null
        defaultCropVarietyCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultCropVarietyCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultCropVarietyCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate is less than
        defaultCropVarietyCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietyCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        // Get all the cropVarietyCropTypeList where subsidisedPremiumRate is greater than
        defaultCropVarietyCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultCropVarietyCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropVarietyCropTypeShouldBeFound(shouldBeFound);
        defaultCropVarietyCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropVarietyCropTypeShouldBeFound(String filter) throws Exception {
        restCropVarietyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVarietyCropType.getId().intValue())))
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
        restCropVarietyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropVarietyCropTypeShouldNotBeFound(String filter) throws Exception {
        restCropVarietyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropVarietyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropVarietyCropType() throws Exception {
        // Get the cropVarietyCropType
        restCropVarietyCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropVarietyCropType() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVarietyCropType
        CropVarietyCropType updatedCropVarietyCropType = cropVarietyCropTypeRepository.findById(cropVarietyCropType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropVarietyCropType are not directly saved in db
        em.detach(updatedCropVarietyCropType);
        updatedCropVarietyCropType
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

        restCropVarietyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropVarietyCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropVarietyCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropVarietyCropTypeToMatchAllProperties(updatedCropVarietyCropType);
    }

    @Test
    @Transactional
    void putNonExistingCropVarietyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropVarietyCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropVarietyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropVarietyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropVarietyCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVarietyCropType using partial update
        CropVarietyCropType partialUpdatedCropVarietyCropType = new CropVarietyCropType();
        partialUpdatedCropVarietyCropType.setId(cropVarietyCropType.getId());

        partialUpdatedCropVarietyCropType
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCropVarietyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVarietyCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVarietyCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropVarietyCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropVarietyCropType, cropVarietyCropType),
            getPersistedCropVarietyCropType(cropVarietyCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropVarietyCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVarietyCropType using partial update
        CropVarietyCropType partialUpdatedCropVarietyCropType = new CropVarietyCropType();
        partialUpdatedCropVarietyCropType.setId(cropVarietyCropType.getId());

        partialUpdatedCropVarietyCropType
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

        restCropVarietyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVarietyCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVarietyCropType))
            )
            .andExpect(status().isOk());

        // Validate the CropVarietyCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyCropTypeUpdatableFieldsEquals(
            partialUpdatedCropVarietyCropType,
            getPersistedCropVarietyCropType(partialUpdatedCropVarietyCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCropVarietyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropVarietyCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropVarietyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropVarietyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVarietyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVarietyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropVarietyCropType() throws Exception {
        // Initialize the database
        insertedCropVarietyCropType = cropVarietyCropTypeRepository.saveAndFlush(cropVarietyCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropVarietyCropType
        restCropVarietyCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropVarietyCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropVarietyCropTypeRepository.count();
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

    protected CropVarietyCropType getPersistedCropVarietyCropType(CropVarietyCropType cropVarietyCropType) {
        return cropVarietyCropTypeRepository.findById(cropVarietyCropType.getId()).orElseThrow();
    }

    protected void assertPersistedCropVarietyCropTypeToMatchAllProperties(CropVarietyCropType expectedCropVarietyCropType) {
        assertCropVarietyCropTypeAllPropertiesEquals(
            expectedCropVarietyCropType,
            getPersistedCropVarietyCropType(expectedCropVarietyCropType)
        );
    }

    protected void assertPersistedCropVarietyCropTypeToMatchUpdatableProperties(CropVarietyCropType expectedCropVarietyCropType) {
        assertCropVarietyCropTypeAllUpdatablePropertiesEquals(
            expectedCropVarietyCropType,
            getPersistedCropVarietyCropType(expectedCropVarietyCropType)
        );
    }
}
