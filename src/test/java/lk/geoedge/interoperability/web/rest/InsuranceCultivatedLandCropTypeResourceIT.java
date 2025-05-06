package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCropTypeRepository;
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
 * Integration tests for the {@link InsuranceCultivatedLandCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCultivatedLandCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/insurance-cultivated-land-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCultivatedLandCropTypeMockMvc;

    private InsuranceCultivatedLandCropType insuranceCultivatedLandCropType;

    private InsuranceCultivatedLandCropType insertedInsuranceCultivatedLandCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandCropType createEntity() {
        return new InsuranceCultivatedLandCropType()
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
    public static InsuranceCultivatedLandCropType createUpdatedEntity() {
        return new InsuranceCultivatedLandCropType()
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
        insuranceCultivatedLandCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCultivatedLandCropType != null) {
            insuranceCultivatedLandCropTypeRepository.delete(insertedInsuranceCultivatedLandCropType);
            insertedInsuranceCultivatedLandCropType = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCultivatedLandCropType
        var returnedInsuranceCultivatedLandCropType = om.readValue(
            restInsuranceCultivatedLandCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCultivatedLandCropType.class
        );

        // Validate the InsuranceCultivatedLandCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCultivatedLandCropTypeUpdatableFieldsEquals(
            returnedInsuranceCultivatedLandCropType,
            getPersistedInsuranceCultivatedLandCropType(returnedInsuranceCultivatedLandCropType)
        );

        insertedInsuranceCultivatedLandCropType = returnedInsuranceCultivatedLandCropType;
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCropTypeWithExistingId() throws Exception {
        // Create the InsuranceCultivatedLandCropType with an existing ID
        insuranceCultivatedLandCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypes() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCropType.getId().intValue())))
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
    void getInsuranceCultivatedLandCropType() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get the insuranceCultivatedLandCropType
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCultivatedLandCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCultivatedLandCropType.getId().intValue()))
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
    void getInsuranceCultivatedLandCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        Long id = insuranceCultivatedLandCropType.getId();

        defaultInsuranceCultivatedLandCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCultivatedLandCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCultivatedLandCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where crop equals to
        defaultInsuranceCultivatedLandCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where crop in
        defaultInsuranceCultivatedLandCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where crop is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where crop contains
        defaultInsuranceCultivatedLandCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where crop does not contain
        defaultInsuranceCultivatedLandCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where image equals to
        defaultInsuranceCultivatedLandCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where image in
        defaultInsuranceCultivatedLandCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where image is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where image contains
        defaultInsuranceCultivatedLandCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where image does not contain
        defaultInsuranceCultivatedLandCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop equals to
        defaultInsuranceCultivatedLandCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop is less than
        defaultInsuranceCultivatedLandCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where mainCrop is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "mainCrop.greaterThan=" + SMALLER_MAIN_CROP,
            "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropCode equals to
        defaultInsuranceCultivatedLandCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropCode in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropCode is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropCode contains
        defaultInsuranceCultivatedLandCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropCode does not contain
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropCode.doesNotContain=" + UPDATED_CROP_CODE,
            "cropCode.doesNotContain=" + DEFAULT_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where noOfStages equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "noOfStages.equals=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.equals=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where noOfStages in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where noOfStages is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where noOfStages contains
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "noOfStages.contains=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.contains=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where noOfStages does not contain
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where description equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "description.equals=" + DEFAULT_DESCRIPTION,
            "description.equals=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where description in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where description is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where description contains
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where description does not contain
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId is less than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where cropTypesId is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId equals to
        defaultInsuranceCultivatedLandCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID,
            "unitsId.in=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId is less than
        defaultInsuranceCultivatedLandCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where unitsId is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "unitsId.greaterThan=" + SMALLER_UNITS_ID,
            "unitsId.greaterThan=" + DEFAULT_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area equals to
        defaultInsuranceCultivatedLandCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area in
        defaultInsuranceCultivatedLandCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "area.greaterThanOrEqual=" + DEFAULT_AREA,
            "area.greaterThanOrEqual=" + UPDATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area is less than
        defaultInsuranceCultivatedLandCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where area is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "sumInsured.equals=" + DEFAULT_SUM_INSURED,
            "sumInsured.equals=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured is less than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "sumInsured.lessThan=" + UPDATED_SUM_INSURED,
            "sumInsured.lessThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where sumInsured is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured is less than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where minSumInsured is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured is less than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where maxSumInsured is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate equals to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate in
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate is not null
        defaultInsuranceCultivatedLandCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate is less than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        // Get all the insuranceCultivatedLandCropTypeList where subsidisedPremiumRate is greater than
        defaultInsuranceCultivatedLandCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultInsuranceCultivatedLandCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCultivatedLandCropTypeShouldBeFound(shouldBeFound);
        defaultInsuranceCultivatedLandCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCultivatedLandCropTypeShouldBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCropType.getId().intValue())))
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
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCultivatedLandCropTypeShouldNotBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCultivatedLandCropType() throws Exception {
        // Get the insuranceCultivatedLandCropType
        restInsuranceCultivatedLandCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCultivatedLandCropType() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCropType
        InsuranceCultivatedLandCropType updatedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository
            .findById(insuranceCultivatedLandCropType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCultivatedLandCropType are not directly saved in db
        em.detach(updatedInsuranceCultivatedLandCropType);
        updatedInsuranceCultivatedLandCropType
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

        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCultivatedLandCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCultivatedLandCropType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCultivatedLandCropTypeToMatchAllProperties(updatedInsuranceCultivatedLandCropType);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCultivatedLandCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCultivatedLandCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCropType using partial update
        InsuranceCultivatedLandCropType partialUpdatedInsuranceCultivatedLandCropType = new InsuranceCultivatedLandCropType();
        partialUpdatedInsuranceCultivatedLandCropType.setId(insuranceCultivatedLandCropType.getId());

        partialUpdatedInsuranceCultivatedLandCropType
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .description(UPDATED_DESCRIPTION)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .sumInsured(UPDATED_SUM_INSURED)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED);

        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCropType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCultivatedLandCropType, insuranceCultivatedLandCropType),
            getPersistedInsuranceCultivatedLandCropType(insuranceCultivatedLandCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCultivatedLandCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCropType using partial update
        InsuranceCultivatedLandCropType partialUpdatedInsuranceCultivatedLandCropType = new InsuranceCultivatedLandCropType();
        partialUpdatedInsuranceCultivatedLandCropType.setId(insuranceCultivatedLandCropType.getId());

        partialUpdatedInsuranceCultivatedLandCropType
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

        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCropType))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCropTypeUpdatableFieldsEquals(
            partialUpdatedInsuranceCultivatedLandCropType,
            getPersistedInsuranceCultivatedLandCropType(partialUpdatedInsuranceCultivatedLandCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCultivatedLandCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCultivatedLandCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCultivatedLandCropType() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeRepository.saveAndFlush(insuranceCultivatedLandCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCultivatedLandCropType
        restInsuranceCultivatedLandCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceCultivatedLandCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCultivatedLandCropTypeRepository.count();
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

    protected InsuranceCultivatedLandCropType getPersistedInsuranceCultivatedLandCropType(
        InsuranceCultivatedLandCropType insuranceCultivatedLandCropType
    ) {
        return insuranceCultivatedLandCropTypeRepository.findById(insuranceCultivatedLandCropType.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCultivatedLandCropTypeToMatchAllProperties(
        InsuranceCultivatedLandCropType expectedInsuranceCultivatedLandCropType
    ) {
        assertInsuranceCultivatedLandCropTypeAllPropertiesEquals(
            expectedInsuranceCultivatedLandCropType,
            getPersistedInsuranceCultivatedLandCropType(expectedInsuranceCultivatedLandCropType)
        );
    }

    protected void assertPersistedInsuranceCultivatedLandCropTypeToMatchUpdatableProperties(
        InsuranceCultivatedLandCropType expectedInsuranceCultivatedLandCropType
    ) {
        assertInsuranceCultivatedLandCropTypeAllUpdatablePropertiesEquals(
            expectedInsuranceCultivatedLandCropType,
            getPersistedInsuranceCultivatedLandCropType(expectedInsuranceCultivatedLandCropType)
        );
    }
}
