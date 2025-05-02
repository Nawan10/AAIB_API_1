package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedCropCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedCropCropType;
import lk.geoedge.interoperability.repository.CultivatedCropCropTypeRepository;
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
 * Integration tests for the {@link CultivatedCropCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedCropCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-crop-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedCropCropTypeMockMvc;

    private CultivatedCropCropType cultivatedCropCropType;

    private CultivatedCropCropType insertedCultivatedCropCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCropCropType createEntity() {
        return new CultivatedCropCropType()
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
    public static CultivatedCropCropType createUpdatedEntity() {
        return new CultivatedCropCropType()
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
        cultivatedCropCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedCropCropType != null) {
            cultivatedCropCropTypeRepository.delete(insertedCultivatedCropCropType);
            insertedCultivatedCropCropType = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedCropCropType
        var returnedCultivatedCropCropType = om.readValue(
            restCultivatedCropCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedCropCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedCropCropType.class
        );

        // Validate the CultivatedCropCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedCropCropTypeUpdatableFieldsEquals(
            returnedCultivatedCropCropType,
            getPersistedCultivatedCropCropType(returnedCultivatedCropCropType)
        );

        insertedCultivatedCropCropType = returnedCultivatedCropCropType;
    }

    @Test
    @Transactional
    void createCultivatedCropCropTypeWithExistingId() throws Exception {
        // Create the CultivatedCropCropType with an existing ID
        cultivatedCropCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedCropCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypes() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList
        restCultivatedCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCropCropType.getId().intValue())))
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
    void getCultivatedCropCropType() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get the cultivatedCropCropType
        restCultivatedCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedCropCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedCropCropType.getId().intValue()))
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
    void getCultivatedCropCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        Long id = cultivatedCropCropType.getId();

        defaultCultivatedCropCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedCropCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedCropCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where crop equals to
        defaultCultivatedCropCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where crop in
        defaultCultivatedCropCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where crop is not null
        defaultCultivatedCropCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where crop contains
        defaultCultivatedCropCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where crop does not contain
        defaultCultivatedCropCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where image equals to
        defaultCultivatedCropCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where image in
        defaultCultivatedCropCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where image is not null
        defaultCultivatedCropCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where image contains
        defaultCultivatedCropCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where image does not contain
        defaultCultivatedCropCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop equals to
        defaultCultivatedCropCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop in
        defaultCultivatedCropCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop is not null
        defaultCultivatedCropCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop is less than
        defaultCultivatedCropCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where mainCrop is greater than
        defaultCultivatedCropCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropCode equals to
        defaultCultivatedCropCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropCode in
        defaultCultivatedCropCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropCode is not null
        defaultCultivatedCropCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropCode contains
        defaultCultivatedCropCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropCode does not contain
        defaultCultivatedCropCropTypeFiltering(
            "cropCode.doesNotContain=" + UPDATED_CROP_CODE,
            "cropCode.doesNotContain=" + DEFAULT_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where noOfStages equals to
        defaultCultivatedCropCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where noOfStages in
        defaultCultivatedCropCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where noOfStages is not null
        defaultCultivatedCropCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where noOfStages contains
        defaultCultivatedCropCropTypeFiltering(
            "noOfStages.contains=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.contains=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where noOfStages does not contain
        defaultCultivatedCropCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where description equals to
        defaultCultivatedCropCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where description in
        defaultCultivatedCropCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where description is not null
        defaultCultivatedCropCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where description contains
        defaultCultivatedCropCropTypeFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where description does not contain
        defaultCultivatedCropCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId equals to
        defaultCultivatedCropCropTypeFiltering(
            "cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId in
        defaultCultivatedCropCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId is not null
        defaultCultivatedCropCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId is less than
        defaultCultivatedCropCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where cropTypesId is greater than
        defaultCultivatedCropCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId equals to
        defaultCultivatedCropCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId in
        defaultCultivatedCropCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId is not null
        defaultCultivatedCropCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId is less than
        defaultCultivatedCropCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where unitsId is greater than
        defaultCultivatedCropCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area equals to
        defaultCultivatedCropCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area in
        defaultCultivatedCropCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area is not null
        defaultCultivatedCropCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area is greater than or equal to
        defaultCultivatedCropCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area is less than or equal to
        defaultCultivatedCropCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area is less than
        defaultCultivatedCropCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where area is greater than
        defaultCultivatedCropCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured equals to
        defaultCultivatedCropCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured in
        defaultCultivatedCropCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured is not null
        defaultCultivatedCropCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured is less than
        defaultCultivatedCropCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where sumInsured is greater than
        defaultCultivatedCropCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured equals to
        defaultCultivatedCropCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured in
        defaultCultivatedCropCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured is not null
        defaultCultivatedCropCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured is less than
        defaultCultivatedCropCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where minSumInsured is greater than
        defaultCultivatedCropCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured equals to
        defaultCultivatedCropCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured in
        defaultCultivatedCropCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured is not null
        defaultCultivatedCropCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured is less than
        defaultCultivatedCropCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where maxSumInsured is greater than
        defaultCultivatedCropCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate equals to
        defaultCultivatedCropCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate in
        defaultCultivatedCropCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate is not null
        defaultCultivatedCropCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultCultivatedCropCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate is less than
        defaultCultivatedCropCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        // Get all the cultivatedCropCropTypeList where subsidisedPremiumRate is greater than
        defaultCultivatedCropCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultCultivatedCropCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedCropCropTypeShouldBeFound(shouldBeFound);
        defaultCultivatedCropCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedCropCropTypeShouldBeFound(String filter) throws Exception {
        restCultivatedCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCropCropType.getId().intValue())))
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
        restCultivatedCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedCropCropTypeShouldNotBeFound(String filter) throws Exception {
        restCultivatedCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedCropCropType() throws Exception {
        // Get the cultivatedCropCropType
        restCultivatedCropCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedCropCropType() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropCropType
        CultivatedCropCropType updatedCultivatedCropCropType = cultivatedCropCropTypeRepository
            .findById(cultivatedCropCropType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedCropCropType are not directly saved in db
        em.detach(updatedCultivatedCropCropType);
        updatedCultivatedCropCropType
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

        restCultivatedCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedCropCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedCropCropTypeToMatchAllProperties(updatedCultivatedCropCropType);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedCropCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedCropCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropCropType using partial update
        CultivatedCropCropType partialUpdatedCultivatedCropCropType = new CultivatedCropCropType();
        partialUpdatedCultivatedCropCropType.setId(cultivatedCropCropType.getId());

        partialUpdatedCultivatedCropCropType
            .crop(UPDATED_CROP)
            .image(UPDATED_IMAGE)
            .mainCrop(UPDATED_MAIN_CROP)
            .description(UPDATED_DESCRIPTION)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCultivatedCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedCropCropType, cultivatedCropCropType),
            getPersistedCultivatedCropCropType(cultivatedCropCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedCropCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropCropType using partial update
        CultivatedCropCropType partialUpdatedCultivatedCropCropType = new CultivatedCropCropType();
        partialUpdatedCultivatedCropCropType.setId(cultivatedCropCropType.getId());

        partialUpdatedCultivatedCropCropType
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

        restCultivatedCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropCropTypeUpdatableFieldsEquals(
            partialUpdatedCultivatedCropCropType,
            getPersistedCultivatedCropCropType(partialUpdatedCultivatedCropCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedCropCropType() throws Exception {
        // Initialize the database
        insertedCultivatedCropCropType = cultivatedCropCropTypeRepository.saveAndFlush(cultivatedCropCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedCropCropType
        restCultivatedCropCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedCropCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedCropCropTypeRepository.count();
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

    protected CultivatedCropCropType getPersistedCultivatedCropCropType(CultivatedCropCropType cultivatedCropCropType) {
        return cultivatedCropCropTypeRepository.findById(cultivatedCropCropType.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedCropCropTypeToMatchAllProperties(CultivatedCropCropType expectedCultivatedCropCropType) {
        assertCultivatedCropCropTypeAllPropertiesEquals(
            expectedCultivatedCropCropType,
            getPersistedCultivatedCropCropType(expectedCultivatedCropCropType)
        );
    }

    protected void assertPersistedCultivatedCropCropTypeToMatchUpdatableProperties(CultivatedCropCropType expectedCultivatedCropCropType) {
        assertCultivatedCropCropTypeAllUpdatablePropertiesEquals(
            expectedCultivatedCropCropType,
            getPersistedCultivatedCropCropType(expectedCultivatedCropCropType)
        );
    }
}
