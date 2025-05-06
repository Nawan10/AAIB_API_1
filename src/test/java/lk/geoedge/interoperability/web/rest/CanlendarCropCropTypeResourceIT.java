package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CanlendarCropCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.CanlendarCropCropType;
import lk.geoedge.interoperability.repository.CanlendarCropCropTypeRepository;
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
 * Integration tests for the {@link CanlendarCropCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CanlendarCropCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/canlendar-crop-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CanlendarCropCropTypeRepository canlendarCropCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCanlendarCropCropTypeMockMvc;

    private CanlendarCropCropType canlendarCropCropType;

    private CanlendarCropCropType insertedCanlendarCropCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanlendarCropCropType createEntity() {
        return new CanlendarCropCropType()
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
    public static CanlendarCropCropType createUpdatedEntity() {
        return new CanlendarCropCropType()
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
        canlendarCropCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCanlendarCropCropType != null) {
            canlendarCropCropTypeRepository.delete(insertedCanlendarCropCropType);
            insertedCanlendarCropCropType = null;
        }
    }

    @Test
    @Transactional
    void createCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CanlendarCropCropType
        var returnedCanlendarCropCropType = om.readValue(
            restCanlendarCropCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(canlendarCropCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CanlendarCropCropType.class
        );

        // Validate the CanlendarCropCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCanlendarCropCropTypeUpdatableFieldsEquals(
            returnedCanlendarCropCropType,
            getPersistedCanlendarCropCropType(returnedCanlendarCropCropType)
        );

        insertedCanlendarCropCropType = returnedCanlendarCropCropType;
    }

    @Test
    @Transactional
    void createCanlendarCropCropTypeWithExistingId() throws Exception {
        // Create the CanlendarCropCropType with an existing ID
        canlendarCropCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCanlendarCropCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypes() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList
        restCanlendarCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canlendarCropCropType.getId().intValue())))
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
    void getCanlendarCropCropType() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get the canlendarCropCropType
        restCanlendarCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, canlendarCropCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(canlendarCropCropType.getId().intValue()))
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
    void getCanlendarCropCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        Long id = canlendarCropCropType.getId();

        defaultCanlendarCropCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCanlendarCropCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCanlendarCropCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where crop equals to
        defaultCanlendarCropCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where crop in
        defaultCanlendarCropCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where crop is not null
        defaultCanlendarCropCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where crop contains
        defaultCanlendarCropCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where crop does not contain
        defaultCanlendarCropCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where image equals to
        defaultCanlendarCropCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where image in
        defaultCanlendarCropCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where image is not null
        defaultCanlendarCropCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where image contains
        defaultCanlendarCropCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where image does not contain
        defaultCanlendarCropCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop equals to
        defaultCanlendarCropCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop in
        defaultCanlendarCropCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop is not null
        defaultCanlendarCropCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop is less than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop is less than
        defaultCanlendarCropCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where mainCrop is greater than
        defaultCanlendarCropCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropCode equals to
        defaultCanlendarCropCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropCode in
        defaultCanlendarCropCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropCode is not null
        defaultCanlendarCropCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropCode contains
        defaultCanlendarCropCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropCode does not contain
        defaultCanlendarCropCropTypeFiltering(
            "cropCode.doesNotContain=" + UPDATED_CROP_CODE,
            "cropCode.doesNotContain=" + DEFAULT_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where noOfStages equals to
        defaultCanlendarCropCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where noOfStages in
        defaultCanlendarCropCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where noOfStages is not null
        defaultCanlendarCropCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where noOfStages contains
        defaultCanlendarCropCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where noOfStages does not contain
        defaultCanlendarCropCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where description equals to
        defaultCanlendarCropCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where description in
        defaultCanlendarCropCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where description is not null
        defaultCanlendarCropCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where description contains
        defaultCanlendarCropCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where description does not contain
        defaultCanlendarCropCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId equals to
        defaultCanlendarCropCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId in
        defaultCanlendarCropCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId is not null
        defaultCanlendarCropCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId is less than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId is less than
        defaultCanlendarCropCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where cropTypesId is greater than
        defaultCanlendarCropCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId equals to
        defaultCanlendarCropCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId in
        defaultCanlendarCropCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId is not null
        defaultCanlendarCropCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId is less than or equal to
        defaultCanlendarCropCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId is less than
        defaultCanlendarCropCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where unitsId is greater than
        defaultCanlendarCropCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area equals to
        defaultCanlendarCropCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area in
        defaultCanlendarCropCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area is not null
        defaultCanlendarCropCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area is greater than or equal to
        defaultCanlendarCropCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area is less than or equal to
        defaultCanlendarCropCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area is less than
        defaultCanlendarCropCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where area is greater than
        defaultCanlendarCropCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured equals to
        defaultCanlendarCropCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured in
        defaultCanlendarCropCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured is not null
        defaultCanlendarCropCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured is less than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured is less than
        defaultCanlendarCropCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where sumInsured is greater than
        defaultCanlendarCropCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured equals to
        defaultCanlendarCropCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured in
        defaultCanlendarCropCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured is not null
        defaultCanlendarCropCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured is less than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured is less than
        defaultCanlendarCropCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where minSumInsured is greater than
        defaultCanlendarCropCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured equals to
        defaultCanlendarCropCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured in
        defaultCanlendarCropCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured is not null
        defaultCanlendarCropCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured is less than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured is less than
        defaultCanlendarCropCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where maxSumInsured is greater than
        defaultCanlendarCropCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate equals to
        defaultCanlendarCropCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate in
        defaultCanlendarCropCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate is not null
        defaultCanlendarCropCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultCanlendarCropCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate is less than
        defaultCanlendarCropCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        // Get all the canlendarCropCropTypeList where subsidisedPremiumRate is greater than
        defaultCanlendarCropCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultCanlendarCropCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCanlendarCropCropTypeShouldBeFound(shouldBeFound);
        defaultCanlendarCropCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCanlendarCropCropTypeShouldBeFound(String filter) throws Exception {
        restCanlendarCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canlendarCropCropType.getId().intValue())))
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
        restCanlendarCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCanlendarCropCropTypeShouldNotBeFound(String filter) throws Exception {
        restCanlendarCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCanlendarCropCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCanlendarCropCropType() throws Exception {
        // Get the canlendarCropCropType
        restCanlendarCropCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCanlendarCropCropType() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCropCropType
        CanlendarCropCropType updatedCanlendarCropCropType = canlendarCropCropTypeRepository
            .findById(canlendarCropCropType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCanlendarCropCropType are not directly saved in db
        em.detach(updatedCanlendarCropCropType);
        updatedCanlendarCropCropType
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

        restCanlendarCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCanlendarCropCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCanlendarCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCanlendarCropCropTypeToMatchAllProperties(updatedCanlendarCropCropType);
    }

    @Test
    @Transactional
    void putNonExistingCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanlendarCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canlendarCropCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCanlendarCropCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCropCropType using partial update
        CanlendarCropCropType partialUpdatedCanlendarCropCropType = new CanlendarCropCropType();
        partialUpdatedCanlendarCropCropType.setId(canlendarCropCropType.getId());

        partialUpdatedCanlendarCropCropType
            .crop(UPDATED_CROP)
            .mainCrop(UPDATED_MAIN_CROP)
            .cropCode(UPDATED_CROP_CODE)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .description(UPDATED_DESCRIPTION)
            .cropTypesId(UPDATED_CROP_TYPES_ID)
            .unitsId(UPDATED_UNITS_ID)
            .sumInsured(UPDATED_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restCanlendarCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanlendarCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanlendarCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCropCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanlendarCropCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCanlendarCropCropType, canlendarCropCropType),
            getPersistedCanlendarCropCropType(canlendarCropCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateCanlendarCropCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCropCropType using partial update
        CanlendarCropCropType partialUpdatedCanlendarCropCropType = new CanlendarCropCropType();
        partialUpdatedCanlendarCropCropType.setId(canlendarCropCropType.getId());

        partialUpdatedCanlendarCropCropType
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

        restCanlendarCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanlendarCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanlendarCropCropType))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCropCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanlendarCropCropTypeUpdatableFieldsEquals(
            partialUpdatedCanlendarCropCropType,
            getPersistedCanlendarCropCropType(partialUpdatedCanlendarCropCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanlendarCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, canlendarCropCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCanlendarCropCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCropCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCropCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanlendarCropCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCanlendarCropCropType() throws Exception {
        // Initialize the database
        insertedCanlendarCropCropType = canlendarCropCropTypeRepository.saveAndFlush(canlendarCropCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the canlendarCropCropType
        restCanlendarCropCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, canlendarCropCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return canlendarCropCropTypeRepository.count();
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

    protected CanlendarCropCropType getPersistedCanlendarCropCropType(CanlendarCropCropType canlendarCropCropType) {
        return canlendarCropCropTypeRepository.findById(canlendarCropCropType.getId()).orElseThrow();
    }

    protected void assertPersistedCanlendarCropCropTypeToMatchAllProperties(CanlendarCropCropType expectedCanlendarCropCropType) {
        assertCanlendarCropCropTypeAllPropertiesEquals(
            expectedCanlendarCropCropType,
            getPersistedCanlendarCropCropType(expectedCanlendarCropCropType)
        );
    }

    protected void assertPersistedCanlendarCropCropTypeToMatchUpdatableProperties(CanlendarCropCropType expectedCanlendarCropCropType) {
        assertCanlendarCropCropTypeAllUpdatablePropertiesEquals(
            expectedCanlendarCropCropType,
            getPersistedCanlendarCropCropType(expectedCanlendarCropCropType)
        );
    }
}
