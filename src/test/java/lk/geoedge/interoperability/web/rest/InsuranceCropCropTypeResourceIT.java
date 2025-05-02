package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCropCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.InsuranceCropCropType;
import lk.geoedge.interoperability.repository.InsuranceCropCropTypeRepository;
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
 * Integration tests for the {@link InsuranceCropCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCropCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/insurance-crop-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCropCropTypeRepository insuranceCropCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCropCropTypeMockMvc;

    private InsuranceCropCropType insuranceCropCropType;

    private InsuranceCropCropType insertedInsuranceCropCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCropCropType createEntity() {
        return new InsuranceCropCropType()
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
    public static InsuranceCropCropType createUpdatedEntity() {
        return new InsuranceCropCropType()
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
        insuranceCropCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCropCropType != null) {
            insuranceCropCropTypeRepository.delete(insertedInsuranceCropCropType);
            insertedInsuranceCropCropType = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCropCropType
        var returnedInsuranceCropCropType = om.readValue(
            restInsuranceCropCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCropCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCropCropType.class
        );

        // Validate the InsuranceCropCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCropCropTypeUpdatableFieldsEquals(
            returnedInsuranceCropCropType,
            getPersistedInsuranceCropCropType(returnedInsuranceCropCropType)
        );

        insertedInsuranceCropCropType = returnedInsuranceCropCropType;
    }

    @Test
    @Transactional
    void createInsuranceCropCropTypeWithExistingId() throws Exception {
        // Create the InsuranceCropCropType with an existing ID
        insuranceCropCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCropCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypes() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList
        restInsuranceCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCropCropType.getId().intValue())))
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
    void getInsuranceCropCropType() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get the insuranceCropCropType
        restInsuranceCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCropCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCropCropType.getId().intValue()))
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
    void getInsuranceCropCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        Long id = insuranceCropCropType.getId();

        defaultInsuranceCropCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCropCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCropCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where crop equals to
        defaultInsuranceCropCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where crop in
        defaultInsuranceCropCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where crop is not null
        defaultInsuranceCropCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where crop contains
        defaultInsuranceCropCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where crop does not contain
        defaultInsuranceCropCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where image equals to
        defaultInsuranceCropCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where image in
        defaultInsuranceCropCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where image is not null
        defaultInsuranceCropCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where image contains
        defaultInsuranceCropCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where image does not contain
        defaultInsuranceCropCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop equals to
        defaultInsuranceCropCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop in
        defaultInsuranceCropCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop is not null
        defaultInsuranceCropCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop is less than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop is less than
        defaultInsuranceCropCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where mainCrop is greater than
        defaultInsuranceCropCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropCode equals to
        defaultInsuranceCropCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropCode in
        defaultInsuranceCropCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropCode is not null
        defaultInsuranceCropCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropCode contains
        defaultInsuranceCropCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropCode does not contain
        defaultInsuranceCropCropTypeFiltering(
            "cropCode.doesNotContain=" + UPDATED_CROP_CODE,
            "cropCode.doesNotContain=" + DEFAULT_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where noOfStages equals to
        defaultInsuranceCropCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where noOfStages in
        defaultInsuranceCropCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where noOfStages is not null
        defaultInsuranceCropCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where noOfStages contains
        defaultInsuranceCropCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where noOfStages does not contain
        defaultInsuranceCropCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where description equals to
        defaultInsuranceCropCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where description in
        defaultInsuranceCropCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where description is not null
        defaultInsuranceCropCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where description contains
        defaultInsuranceCropCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where description does not contain
        defaultInsuranceCropCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId equals to
        defaultInsuranceCropCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId in
        defaultInsuranceCropCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId is not null
        defaultInsuranceCropCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId is less than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId is less than
        defaultInsuranceCropCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where cropTypesId is greater than
        defaultInsuranceCropCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId equals to
        defaultInsuranceCropCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId in
        defaultInsuranceCropCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId is not null
        defaultInsuranceCropCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId is less than or equal to
        defaultInsuranceCropCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId is less than
        defaultInsuranceCropCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where unitsId is greater than
        defaultInsuranceCropCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area equals to
        defaultInsuranceCropCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area in
        defaultInsuranceCropCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area is not null
        defaultInsuranceCropCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area is greater than or equal to
        defaultInsuranceCropCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area is less than or equal to
        defaultInsuranceCropCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area is less than
        defaultInsuranceCropCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where area is greater than
        defaultInsuranceCropCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured equals to
        defaultInsuranceCropCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured in
        defaultInsuranceCropCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured is not null
        defaultInsuranceCropCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured is less than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured is less than
        defaultInsuranceCropCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where sumInsured is greater than
        defaultInsuranceCropCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured equals to
        defaultInsuranceCropCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured in
        defaultInsuranceCropCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured is not null
        defaultInsuranceCropCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured is less than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured is less than
        defaultInsuranceCropCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where minSumInsured is greater than
        defaultInsuranceCropCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured equals to
        defaultInsuranceCropCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured in
        defaultInsuranceCropCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured is not null
        defaultInsuranceCropCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured is less than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured is less than
        defaultInsuranceCropCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where maxSumInsured is greater than
        defaultInsuranceCropCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate equals to
        defaultInsuranceCropCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate in
        defaultInsuranceCropCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate is not null
        defaultInsuranceCropCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultInsuranceCropCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate is less than
        defaultInsuranceCropCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        // Get all the insuranceCropCropTypeList where subsidisedPremiumRate is greater than
        defaultInsuranceCropCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultInsuranceCropCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCropCropTypeShouldBeFound(shouldBeFound);
        defaultInsuranceCropCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCropCropTypeShouldBeFound(String filter) throws Exception {
        restInsuranceCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCropCropType.getId().intValue())))
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
        restInsuranceCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCropCropTypeShouldNotBeFound(String filter) throws Exception {
        restInsuranceCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCropCropType() throws Exception {
        // Get the insuranceCropCropType
        restInsuranceCropCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCropCropType() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCropCropType
        InsuranceCropCropType updatedInsuranceCropCropType = insuranceCropCropTypeRepository
            .findById(insuranceCropCropType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCropCropType are not directly saved in db
        em.detach(updatedInsuranceCropCropType);
        updatedInsuranceCropCropType
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

        restInsuranceCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCropCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCropCropTypeToMatchAllProperties(updatedInsuranceCropCropType);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCropCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCropCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCropCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCropCropType using partial update
        InsuranceCropCropType partialUpdatedInsuranceCropCropType = new InsuranceCropCropType();
        partialUpdatedInsuranceCropCropType.setId(insuranceCropCropType.getId());

        partialUpdatedInsuranceCropCropType
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .minSumInsured(UPDATED_MIN_SUM_INSURED);

        restInsuranceCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCropCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCropCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCropCropType, insuranceCropCropType),
            getPersistedInsuranceCropCropType(insuranceCropCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCropCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCropCropType using partial update
        InsuranceCropCropType partialUpdatedInsuranceCropCropType = new InsuranceCropCropType();
        partialUpdatedInsuranceCropCropType.setId(insuranceCropCropType.getId());

        partialUpdatedInsuranceCropCropType
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

        restInsuranceCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCropCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCropCropTypeUpdatableFieldsEquals(
            partialUpdatedInsuranceCropCropType,
            getPersistedInsuranceCropCropType(partialUpdatedInsuranceCropCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCropCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCropCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCropCropType() throws Exception {
        // Initialize the database
        insertedInsuranceCropCropType = insuranceCropCropTypeRepository.saveAndFlush(insuranceCropCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCropCropType
        restInsuranceCropCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceCropCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCropCropTypeRepository.count();
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

    protected InsuranceCropCropType getPersistedInsuranceCropCropType(InsuranceCropCropType insuranceCropCropType) {
        return insuranceCropCropTypeRepository.findById(insuranceCropCropType.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCropCropTypeToMatchAllProperties(InsuranceCropCropType expectedInsuranceCropCropType) {
        assertInsuranceCropCropTypeAllPropertiesEquals(
            expectedInsuranceCropCropType,
            getPersistedInsuranceCropCropType(expectedInsuranceCropCropType)
        );
    }

    protected void assertPersistedInsuranceCropCropTypeToMatchUpdatableProperties(InsuranceCropCropType expectedInsuranceCropCropType) {
        assertInsuranceCropCropTypeAllUpdatablePropertiesEquals(
            expectedInsuranceCropCropType,
            getPersistedInsuranceCropCropType(expectedInsuranceCropCropType)
        );
    }
}
