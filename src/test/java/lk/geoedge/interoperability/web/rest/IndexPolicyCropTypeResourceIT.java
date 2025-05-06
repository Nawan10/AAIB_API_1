package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPolicyCropTypeAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPolicyCropType;
import lk.geoedge.interoperability.repository.IndexPolicyCropTypeRepository;
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
 * Integration tests for the {@link IndexPolicyCropTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPolicyCropTypeResourceIT {

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

    private static final String ENTITY_API_URL = "/api/index-policy-crop-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPolicyCropTypeRepository indexPolicyCropTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPolicyCropTypeMockMvc;

    private IndexPolicyCropType indexPolicyCropType;

    private IndexPolicyCropType insertedIndexPolicyCropType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyCropType createEntity() {
        return new IndexPolicyCropType()
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
    public static IndexPolicyCropType createUpdatedEntity() {
        return new IndexPolicyCropType()
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
        indexPolicyCropType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPolicyCropType != null) {
            indexPolicyCropTypeRepository.delete(insertedIndexPolicyCropType);
            insertedIndexPolicyCropType = null;
        }
    }

    @Test
    @Transactional
    void createIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPolicyCropType
        var returnedIndexPolicyCropType = om.readValue(
            restIndexPolicyCropTypeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPolicyCropType))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPolicyCropType.class
        );

        // Validate the IndexPolicyCropType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPolicyCropTypeUpdatableFieldsEquals(
            returnedIndexPolicyCropType,
            getPersistedIndexPolicyCropType(returnedIndexPolicyCropType)
        );

        insertedIndexPolicyCropType = returnedIndexPolicyCropType;
    }

    @Test
    @Transactional
    void createIndexPolicyCropTypeWithExistingId() throws Exception {
        // Create the IndexPolicyCropType with an existing ID
        indexPolicyCropType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPolicyCropTypeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypes() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList
        restIndexPolicyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyCropType.getId().intValue())))
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
    void getIndexPolicyCropType() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get the indexPolicyCropType
        restIndexPolicyCropTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPolicyCropType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPolicyCropType.getId().intValue()))
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
    void getIndexPolicyCropTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        Long id = indexPolicyCropType.getId();

        defaultIndexPolicyCropTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPolicyCropTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPolicyCropTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where crop equals to
        defaultIndexPolicyCropTypeFiltering("crop.equals=" + DEFAULT_CROP, "crop.equals=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where crop in
        defaultIndexPolicyCropTypeFiltering("crop.in=" + DEFAULT_CROP + "," + UPDATED_CROP, "crop.in=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where crop is not null
        defaultIndexPolicyCropTypeFiltering("crop.specified=true", "crop.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where crop contains
        defaultIndexPolicyCropTypeFiltering("crop.contains=" + DEFAULT_CROP, "crop.contains=" + UPDATED_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where crop does not contain
        defaultIndexPolicyCropTypeFiltering("crop.doesNotContain=" + UPDATED_CROP, "crop.doesNotContain=" + DEFAULT_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where image equals to
        defaultIndexPolicyCropTypeFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where image in
        defaultIndexPolicyCropTypeFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where image is not null
        defaultIndexPolicyCropTypeFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where image contains
        defaultIndexPolicyCropTypeFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where image does not contain
        defaultIndexPolicyCropTypeFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop equals to
        defaultIndexPolicyCropTypeFiltering("mainCrop.equals=" + DEFAULT_MAIN_CROP, "mainCrop.equals=" + UPDATED_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop in
        defaultIndexPolicyCropTypeFiltering(
            "mainCrop.in=" + DEFAULT_MAIN_CROP + "," + UPDATED_MAIN_CROP,
            "mainCrop.in=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop is not null
        defaultIndexPolicyCropTypeFiltering("mainCrop.specified=true", "mainCrop.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "mainCrop.greaterThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.greaterThanOrEqual=" + UPDATED_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop is less than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "mainCrop.lessThanOrEqual=" + DEFAULT_MAIN_CROP,
            "mainCrop.lessThanOrEqual=" + SMALLER_MAIN_CROP
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop is less than
        defaultIndexPolicyCropTypeFiltering("mainCrop.lessThan=" + UPDATED_MAIN_CROP, "mainCrop.lessThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMainCropIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where mainCrop is greater than
        defaultIndexPolicyCropTypeFiltering("mainCrop.greaterThan=" + SMALLER_MAIN_CROP, "mainCrop.greaterThan=" + DEFAULT_MAIN_CROP);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropCode equals to
        defaultIndexPolicyCropTypeFiltering("cropCode.equals=" + DEFAULT_CROP_CODE, "cropCode.equals=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropCode in
        defaultIndexPolicyCropTypeFiltering(
            "cropCode.in=" + DEFAULT_CROP_CODE + "," + UPDATED_CROP_CODE,
            "cropCode.in=" + UPDATED_CROP_CODE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropCode is not null
        defaultIndexPolicyCropTypeFiltering("cropCode.specified=true", "cropCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropCode contains
        defaultIndexPolicyCropTypeFiltering("cropCode.contains=" + DEFAULT_CROP_CODE, "cropCode.contains=" + UPDATED_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropCode does not contain
        defaultIndexPolicyCropTypeFiltering("cropCode.doesNotContain=" + UPDATED_CROP_CODE, "cropCode.doesNotContain=" + DEFAULT_CROP_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where noOfStages equals to
        defaultIndexPolicyCropTypeFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where noOfStages in
        defaultIndexPolicyCropTypeFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where noOfStages is not null
        defaultIndexPolicyCropTypeFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByNoOfStagesContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where noOfStages contains
        defaultIndexPolicyCropTypeFiltering("noOfStages.contains=" + DEFAULT_NO_OF_STAGES, "noOfStages.contains=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByNoOfStagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where noOfStages does not contain
        defaultIndexPolicyCropTypeFiltering(
            "noOfStages.doesNotContain=" + UPDATED_NO_OF_STAGES,
            "noOfStages.doesNotContain=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where description equals to
        defaultIndexPolicyCropTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where description in
        defaultIndexPolicyCropTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where description is not null
        defaultIndexPolicyCropTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where description contains
        defaultIndexPolicyCropTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where description does not contain
        defaultIndexPolicyCropTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId equals to
        defaultIndexPolicyCropTypeFiltering("cropTypesId.equals=" + DEFAULT_CROP_TYPES_ID, "cropTypesId.equals=" + UPDATED_CROP_TYPES_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId in
        defaultIndexPolicyCropTypeFiltering(
            "cropTypesId.in=" + DEFAULT_CROP_TYPES_ID + "," + UPDATED_CROP_TYPES_ID,
            "cropTypesId.in=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId is not null
        defaultIndexPolicyCropTypeFiltering("cropTypesId.specified=true", "cropTypesId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "cropTypesId.greaterThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.greaterThanOrEqual=" + UPDATED_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId is less than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "cropTypesId.lessThanOrEqual=" + DEFAULT_CROP_TYPES_ID,
            "cropTypesId.lessThanOrEqual=" + SMALLER_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId is less than
        defaultIndexPolicyCropTypeFiltering(
            "cropTypesId.lessThan=" + UPDATED_CROP_TYPES_ID,
            "cropTypesId.lessThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByCropTypesIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where cropTypesId is greater than
        defaultIndexPolicyCropTypeFiltering(
            "cropTypesId.greaterThan=" + SMALLER_CROP_TYPES_ID,
            "cropTypesId.greaterThan=" + DEFAULT_CROP_TYPES_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId equals to
        defaultIndexPolicyCropTypeFiltering("unitsId.equals=" + DEFAULT_UNITS_ID, "unitsId.equals=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId in
        defaultIndexPolicyCropTypeFiltering("unitsId.in=" + DEFAULT_UNITS_ID + "," + UPDATED_UNITS_ID, "unitsId.in=" + UPDATED_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId is not null
        defaultIndexPolicyCropTypeFiltering("unitsId.specified=true", "unitsId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "unitsId.greaterThanOrEqual=" + DEFAULT_UNITS_ID,
            "unitsId.greaterThanOrEqual=" + UPDATED_UNITS_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId is less than or equal to
        defaultIndexPolicyCropTypeFiltering("unitsId.lessThanOrEqual=" + DEFAULT_UNITS_ID, "unitsId.lessThanOrEqual=" + SMALLER_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId is less than
        defaultIndexPolicyCropTypeFiltering("unitsId.lessThan=" + UPDATED_UNITS_ID, "unitsId.lessThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByUnitsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where unitsId is greater than
        defaultIndexPolicyCropTypeFiltering("unitsId.greaterThan=" + SMALLER_UNITS_ID, "unitsId.greaterThan=" + DEFAULT_UNITS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area equals to
        defaultIndexPolicyCropTypeFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area in
        defaultIndexPolicyCropTypeFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area is not null
        defaultIndexPolicyCropTypeFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area is greater than or equal to
        defaultIndexPolicyCropTypeFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area is less than or equal to
        defaultIndexPolicyCropTypeFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area is less than
        defaultIndexPolicyCropTypeFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where area is greater than
        defaultIndexPolicyCropTypeFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured equals to
        defaultIndexPolicyCropTypeFiltering("sumInsured.equals=" + DEFAULT_SUM_INSURED, "sumInsured.equals=" + UPDATED_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured in
        defaultIndexPolicyCropTypeFiltering(
            "sumInsured.in=" + DEFAULT_SUM_INSURED + "," + UPDATED_SUM_INSURED,
            "sumInsured.in=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured is not null
        defaultIndexPolicyCropTypeFiltering("sumInsured.specified=true", "sumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "sumInsured.greaterThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.greaterThanOrEqual=" + UPDATED_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured is less than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "sumInsured.lessThanOrEqual=" + DEFAULT_SUM_INSURED,
            "sumInsured.lessThanOrEqual=" + SMALLER_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured is less than
        defaultIndexPolicyCropTypeFiltering("sumInsured.lessThan=" + UPDATED_SUM_INSURED, "sumInsured.lessThan=" + DEFAULT_SUM_INSURED);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where sumInsured is greater than
        defaultIndexPolicyCropTypeFiltering(
            "sumInsured.greaterThan=" + SMALLER_SUM_INSURED,
            "sumInsured.greaterThan=" + DEFAULT_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured equals to
        defaultIndexPolicyCropTypeFiltering(
            "minSumInsured.equals=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.equals=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured in
        defaultIndexPolicyCropTypeFiltering(
            "minSumInsured.in=" + DEFAULT_MIN_SUM_INSURED + "," + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.in=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured is not null
        defaultIndexPolicyCropTypeFiltering("minSumInsured.specified=true", "minSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "minSumInsured.greaterThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.greaterThanOrEqual=" + UPDATED_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured is less than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "minSumInsured.lessThanOrEqual=" + DEFAULT_MIN_SUM_INSURED,
            "minSumInsured.lessThanOrEqual=" + SMALLER_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured is less than
        defaultIndexPolicyCropTypeFiltering(
            "minSumInsured.lessThan=" + UPDATED_MIN_SUM_INSURED,
            "minSumInsured.lessThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMinSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where minSumInsured is greater than
        defaultIndexPolicyCropTypeFiltering(
            "minSumInsured.greaterThan=" + SMALLER_MIN_SUM_INSURED,
            "minSumInsured.greaterThan=" + DEFAULT_MIN_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured equals to
        defaultIndexPolicyCropTypeFiltering(
            "maxSumInsured.equals=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.equals=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured in
        defaultIndexPolicyCropTypeFiltering(
            "maxSumInsured.in=" + DEFAULT_MAX_SUM_INSURED + "," + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.in=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured is not null
        defaultIndexPolicyCropTypeFiltering("maxSumInsured.specified=true", "maxSumInsured.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "maxSumInsured.greaterThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.greaterThanOrEqual=" + UPDATED_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured is less than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "maxSumInsured.lessThanOrEqual=" + DEFAULT_MAX_SUM_INSURED,
            "maxSumInsured.lessThanOrEqual=" + SMALLER_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured is less than
        defaultIndexPolicyCropTypeFiltering(
            "maxSumInsured.lessThan=" + UPDATED_MAX_SUM_INSURED,
            "maxSumInsured.lessThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesByMaxSumInsuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where maxSumInsured is greater than
        defaultIndexPolicyCropTypeFiltering(
            "maxSumInsured.greaterThan=" + SMALLER_MAX_SUM_INSURED,
            "maxSumInsured.greaterThan=" + DEFAULT_MAX_SUM_INSURED
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate equals to
        defaultIndexPolicyCropTypeFiltering(
            "subsidisedPremiumRate.equals=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.equals=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate in
        defaultIndexPolicyCropTypeFiltering(
            "subsidisedPremiumRate.in=" + DEFAULT_SUBSIDISED_PREMIUM_RATE + "," + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.in=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate is not null
        defaultIndexPolicyCropTypeFiltering("subsidisedPremiumRate.specified=true", "subsidisedPremiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate is greater than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "subsidisedPremiumRate.greaterThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThanOrEqual=" + UPDATED_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate is less than or equal to
        defaultIndexPolicyCropTypeFiltering(
            "subsidisedPremiumRate.lessThanOrEqual=" + DEFAULT_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThanOrEqual=" + SMALLER_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate is less than
        defaultIndexPolicyCropTypeFiltering(
            "subsidisedPremiumRate.lessThan=" + UPDATED_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.lessThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropTypesBySubsidisedPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        // Get all the indexPolicyCropTypeList where subsidisedPremiumRate is greater than
        defaultIndexPolicyCropTypeFiltering(
            "subsidisedPremiumRate.greaterThan=" + SMALLER_SUBSIDISED_PREMIUM_RATE,
            "subsidisedPremiumRate.greaterThan=" + DEFAULT_SUBSIDISED_PREMIUM_RATE
        );
    }

    private void defaultIndexPolicyCropTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPolicyCropTypeShouldBeFound(shouldBeFound);
        defaultIndexPolicyCropTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPolicyCropTypeShouldBeFound(String filter) throws Exception {
        restIndexPolicyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyCropType.getId().intValue())))
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
        restIndexPolicyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPolicyCropTypeShouldNotBeFound(String filter) throws Exception {
        restIndexPolicyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPolicyCropTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPolicyCropType() throws Exception {
        // Get the indexPolicyCropType
        restIndexPolicyCropTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPolicyCropType() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyCropType
        IndexPolicyCropType updatedIndexPolicyCropType = indexPolicyCropTypeRepository.findById(indexPolicyCropType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPolicyCropType are not directly saved in db
        em.detach(updatedIndexPolicyCropType);
        updatedIndexPolicyCropType
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

        restIndexPolicyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPolicyCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPolicyCropType))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPolicyCropTypeToMatchAllProperties(updatedIndexPolicyCropType);
    }

    @Test
    @Transactional
    void putNonExistingIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPolicyCropType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPolicyCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyCropType using partial update
        IndexPolicyCropType partialUpdatedIndexPolicyCropType = new IndexPolicyCropType();
        partialUpdatedIndexPolicyCropType.setId(indexPolicyCropType.getId());

        partialUpdatedIndexPolicyCropType
            .image(UPDATED_IMAGE)
            .cropCode(UPDATED_CROP_CODE)
            .description(UPDATED_DESCRIPTION)
            .unitsId(UPDATED_UNITS_ID)
            .area(UPDATED_AREA)
            .minSumInsured(UPDATED_MIN_SUM_INSURED)
            .maxSumInsured(UPDATED_MAX_SUM_INSURED)
            .subsidisedPremiumRate(UPDATED_SUBSIDISED_PREMIUM_RATE);

        restIndexPolicyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyCropType))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyCropTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPolicyCropType, indexPolicyCropType),
            getPersistedIndexPolicyCropType(indexPolicyCropType)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPolicyCropTypeWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyCropType using partial update
        IndexPolicyCropType partialUpdatedIndexPolicyCropType = new IndexPolicyCropType();
        partialUpdatedIndexPolicyCropType.setId(indexPolicyCropType.getId());

        partialUpdatedIndexPolicyCropType
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

        restIndexPolicyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyCropType))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyCropType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyCropTypeUpdatableFieldsEquals(
            partialUpdatedIndexPolicyCropType,
            getPersistedIndexPolicyCropType(partialUpdatedIndexPolicyCropType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPolicyCropType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPolicyCropType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyCropType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyCropType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPolicyCropType() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropType = indexPolicyCropTypeRepository.saveAndFlush(indexPolicyCropType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPolicyCropType
        restIndexPolicyCropTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPolicyCropType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPolicyCropTypeRepository.count();
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

    protected IndexPolicyCropType getPersistedIndexPolicyCropType(IndexPolicyCropType indexPolicyCropType) {
        return indexPolicyCropTypeRepository.findById(indexPolicyCropType.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPolicyCropTypeToMatchAllProperties(IndexPolicyCropType expectedIndexPolicyCropType) {
        assertIndexPolicyCropTypeAllPropertiesEquals(
            expectedIndexPolicyCropType,
            getPersistedIndexPolicyCropType(expectedIndexPolicyCropType)
        );
    }

    protected void assertPersistedIndexPolicyCropTypeToMatchUpdatableProperties(IndexPolicyCropType expectedIndexPolicyCropType) {
        assertIndexPolicyCropTypeAllUpdatablePropertiesEquals(
            expectedIndexPolicyCropType,
            getPersistedIndexPolicyCropType(expectedIndexPolicyCropType)
        );
    }
}
