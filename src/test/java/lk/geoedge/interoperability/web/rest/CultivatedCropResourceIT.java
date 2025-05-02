package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedCropAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.CultivatedCrop;
import lk.geoedge.interoperability.domain.CultivatedCropCropType;
import lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedCropRepository;
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
 * Integration tests for the {@link CultivatedCropResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedCropResourceIT {

    private static final Double DEFAULT_CULTIVATED_EXTEND = 1D;
    private static final Double UPDATED_CULTIVATED_EXTEND = 2D;
    private static final Double SMALLER_CULTIVATED_EXTEND = 1D - 1D;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_YIELD = 1D;
    private static final Double UPDATED_YIELD = 2D;
    private static final Double SMALLER_YIELD = 1D - 1D;

    private static final String DEFAULT_UNIT_ID = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-crops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedCropRepository cultivatedCropRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedCropMockMvc;

    private CultivatedCrop cultivatedCrop;

    private CultivatedCrop insertedCultivatedCrop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCrop createEntity() {
        return new CultivatedCrop()
            .cultivatedExtend(DEFAULT_CULTIVATED_EXTEND)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .yield(DEFAULT_YIELD)
            .unitId(DEFAULT_UNIT_ID)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCrop createUpdatedEntity() {
        return new CultivatedCrop()
            .cultivatedExtend(UPDATED_CULTIVATED_EXTEND)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .yield(UPDATED_YIELD)
            .unitId(UPDATED_UNIT_ID)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedCrop = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedCrop != null) {
            cultivatedCropRepository.delete(insertedCultivatedCrop);
            insertedCultivatedCrop = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedCrop() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedCrop
        var returnedCultivatedCrop = om.readValue(
            restCultivatedCropMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedCrop))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedCrop.class
        );

        // Validate the CultivatedCrop in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedCropUpdatableFieldsEquals(returnedCultivatedCrop, getPersistedCultivatedCrop(returnedCultivatedCrop));

        insertedCultivatedCrop = returnedCultivatedCrop;
    }

    @Test
    @Transactional
    void createCultivatedCropWithExistingId() throws Exception {
        // Create the CultivatedCrop with an existing ID
        cultivatedCrop.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedCropMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedCrops() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList
        restCultivatedCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCrop.getId().intValue())))
            .andExpect(jsonPath("$.[*].cultivatedExtend").value(hasItem(DEFAULT_CULTIVATED_EXTEND)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].yield").value(hasItem(DEFAULT_YIELD)))
            .andExpect(jsonPath("$.[*].unitId").value(hasItem(DEFAULT_UNIT_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedCrop() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get the cultivatedCrop
        restCultivatedCropMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedCrop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedCrop.getId().intValue()))
            .andExpect(jsonPath("$.cultivatedExtend").value(DEFAULT_CULTIVATED_EXTEND))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.yield").value(DEFAULT_YIELD))
            .andExpect(jsonPath("$.unitId").value(DEFAULT_UNIT_ID))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedCropsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        Long id = cultivatedCrop.getId();

        defaultCultivatedCropFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedCropFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedCropFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend equals to
        defaultCultivatedCropFiltering(
            "cultivatedExtend.equals=" + DEFAULT_CULTIVATED_EXTEND,
            "cultivatedExtend.equals=" + UPDATED_CULTIVATED_EXTEND
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend in
        defaultCultivatedCropFiltering(
            "cultivatedExtend.in=" + DEFAULT_CULTIVATED_EXTEND + "," + UPDATED_CULTIVATED_EXTEND,
            "cultivatedExtend.in=" + UPDATED_CULTIVATED_EXTEND
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend is not null
        defaultCultivatedCropFiltering("cultivatedExtend.specified=true", "cultivatedExtend.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend is greater than or equal to
        defaultCultivatedCropFiltering(
            "cultivatedExtend.greaterThanOrEqual=" + DEFAULT_CULTIVATED_EXTEND,
            "cultivatedExtend.greaterThanOrEqual=" + UPDATED_CULTIVATED_EXTEND
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend is less than or equal to
        defaultCultivatedCropFiltering(
            "cultivatedExtend.lessThanOrEqual=" + DEFAULT_CULTIVATED_EXTEND,
            "cultivatedExtend.lessThanOrEqual=" + SMALLER_CULTIVATED_EXTEND
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend is less than
        defaultCultivatedCropFiltering(
            "cultivatedExtend.lessThan=" + UPDATED_CULTIVATED_EXTEND,
            "cultivatedExtend.lessThan=" + DEFAULT_CULTIVATED_EXTEND
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedExtendIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where cultivatedExtend is greater than
        defaultCultivatedCropFiltering(
            "cultivatedExtend.greaterThan=" + SMALLER_CULTIVATED_EXTEND,
            "cultivatedExtend.greaterThan=" + DEFAULT_CULTIVATED_EXTEND
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate equals to
        defaultCultivatedCropFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate in
        defaultCultivatedCropFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate is not null
        defaultCultivatedCropFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate is greater than or equal to
        defaultCultivatedCropFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate is less than or equal to
        defaultCultivatedCropFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate is less than
        defaultCultivatedCropFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where startDate is greater than
        defaultCultivatedCropFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate equals to
        defaultCultivatedCropFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate in
        defaultCultivatedCropFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate is not null
        defaultCultivatedCropFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate is greater than or equal to
        defaultCultivatedCropFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate is less than or equal to
        defaultCultivatedCropFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate is less than
        defaultCultivatedCropFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where endDate is greater than
        defaultCultivatedCropFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield equals to
        defaultCultivatedCropFiltering("yield.equals=" + DEFAULT_YIELD, "yield.equals=" + UPDATED_YIELD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield in
        defaultCultivatedCropFiltering("yield.in=" + DEFAULT_YIELD + "," + UPDATED_YIELD, "yield.in=" + UPDATED_YIELD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield is not null
        defaultCultivatedCropFiltering("yield.specified=true", "yield.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield is greater than or equal to
        defaultCultivatedCropFiltering("yield.greaterThanOrEqual=" + DEFAULT_YIELD, "yield.greaterThanOrEqual=" + UPDATED_YIELD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield is less than or equal to
        defaultCultivatedCropFiltering("yield.lessThanOrEqual=" + DEFAULT_YIELD, "yield.lessThanOrEqual=" + SMALLER_YIELD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield is less than
        defaultCultivatedCropFiltering("yield.lessThan=" + UPDATED_YIELD, "yield.lessThan=" + DEFAULT_YIELD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByYieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where yield is greater than
        defaultCultivatedCropFiltering("yield.greaterThan=" + SMALLER_YIELD, "yield.greaterThan=" + DEFAULT_YIELD);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByUnitIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where unitId equals to
        defaultCultivatedCropFiltering("unitId.equals=" + DEFAULT_UNIT_ID, "unitId.equals=" + UPDATED_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByUnitIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where unitId in
        defaultCultivatedCropFiltering("unitId.in=" + DEFAULT_UNIT_ID + "," + UPDATED_UNIT_ID, "unitId.in=" + UPDATED_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByUnitIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where unitId is not null
        defaultCultivatedCropFiltering("unitId.specified=true", "unitId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByUnitIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where unitId contains
        defaultCultivatedCropFiltering("unitId.contains=" + DEFAULT_UNIT_ID, "unitId.contains=" + UPDATED_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByUnitIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where unitId does not contain
        defaultCultivatedCropFiltering("unitId.doesNotContain=" + UPDATED_UNIT_ID, "unitId.doesNotContain=" + DEFAULT_UNIT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt equals to
        defaultCultivatedCropFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt in
        defaultCultivatedCropFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt is not null
        defaultCultivatedCropFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt is greater than or equal to
        defaultCultivatedCropFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt is less than or equal to
        defaultCultivatedCropFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt is less than
        defaultCultivatedCropFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where createdAt is greater than
        defaultCultivatedCropFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where addedBy equals to
        defaultCultivatedCropFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where addedBy in
        defaultCultivatedCropFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where addedBy is not null
        defaultCultivatedCropFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where addedBy contains
        defaultCultivatedCropFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        // Get all the cultivatedCropList where addedBy does not contain
        defaultCultivatedCropFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCultivatedLandIsEqualToSomething() throws Exception {
        CultivatedCropCultivatedLand cultivatedLand;
        if (TestUtil.findAll(em, CultivatedCropCultivatedLand.class).isEmpty()) {
            cultivatedCropRepository.saveAndFlush(cultivatedCrop);
            cultivatedLand = CultivatedCropCultivatedLandResourceIT.createEntity();
        } else {
            cultivatedLand = TestUtil.findAll(em, CultivatedCropCultivatedLand.class).get(0);
        }
        em.persist(cultivatedLand);
        em.flush();
        cultivatedCrop.setCultivatedLand(cultivatedLand);
        cultivatedCropRepository.saveAndFlush(cultivatedCrop);
        Long cultivatedLandId = cultivatedLand.getId();
        // Get all the cultivatedCropList where cultivatedLand equals to cultivatedLandId
        defaultCultivatedCropShouldBeFound("cultivatedLandId.equals=" + cultivatedLandId);

        // Get all the cultivatedCropList where cultivatedLand equals to (cultivatedLandId + 1)
        defaultCultivatedCropShouldNotBeFound("cultivatedLandId.equals=" + (cultivatedLandId + 1));
    }

    @Test
    @Transactional
    void getAllCultivatedCropsByCropIsEqualToSomething() throws Exception {
        CultivatedCropCropType crop;
        if (TestUtil.findAll(em, CultivatedCropCropType.class).isEmpty()) {
            cultivatedCropRepository.saveAndFlush(cultivatedCrop);
            crop = CultivatedCropCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, CultivatedCropCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        cultivatedCrop.setCrop(crop);
        cultivatedCropRepository.saveAndFlush(cultivatedCrop);
        Long cropId = crop.getId();
        // Get all the cultivatedCropList where crop equals to cropId
        defaultCultivatedCropShouldBeFound("cropId.equals=" + cropId);

        // Get all the cultivatedCropList where crop equals to (cropId + 1)
        defaultCultivatedCropShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultCultivatedCropFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedCropShouldBeFound(shouldBeFound);
        defaultCultivatedCropShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedCropShouldBeFound(String filter) throws Exception {
        restCultivatedCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCrop.getId().intValue())))
            .andExpect(jsonPath("$.[*].cultivatedExtend").value(hasItem(DEFAULT_CULTIVATED_EXTEND)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].yield").value(hasItem(DEFAULT_YIELD)))
            .andExpect(jsonPath("$.[*].unitId").value(hasItem(DEFAULT_UNIT_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedCropShouldNotBeFound(String filter) throws Exception {
        restCultivatedCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedCrop() throws Exception {
        // Get the cultivatedCrop
        restCultivatedCropMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedCrop() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCrop
        CultivatedCrop updatedCultivatedCrop = cultivatedCropRepository.findById(cultivatedCrop.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedCrop are not directly saved in db
        em.detach(updatedCultivatedCrop);
        updatedCultivatedCrop
            .cultivatedExtend(UPDATED_CULTIVATED_EXTEND)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .yield(UPDATED_YIELD)
            .unitId(UPDATED_UNIT_ID)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedCrop.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedCrop))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedCropToMatchAllProperties(updatedCultivatedCrop);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCrop.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedCrop.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedCrop)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedCropWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCrop using partial update
        CultivatedCrop partialUpdatedCultivatedCrop = new CultivatedCrop();
        partialUpdatedCultivatedCrop.setId(cultivatedCrop.getId());

        partialUpdatedCultivatedCrop.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).yield(UPDATED_YIELD);

        restCultivatedCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCrop))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCrop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedCrop, cultivatedCrop),
            getPersistedCultivatedCrop(cultivatedCrop)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedCropWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCrop using partial update
        CultivatedCrop partialUpdatedCultivatedCrop = new CultivatedCrop();
        partialUpdatedCultivatedCrop.setId(cultivatedCrop.getId());

        partialUpdatedCultivatedCrop
            .cultivatedExtend(UPDATED_CULTIVATED_EXTEND)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .yield(UPDATED_YIELD)
            .unitId(UPDATED_UNIT_ID)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCrop))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCrop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropUpdatableFieldsEquals(partialUpdatedCultivatedCrop, getPersistedCultivatedCrop(partialUpdatedCultivatedCrop));
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCrop.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cultivatedCrop))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedCrop() throws Exception {
        // Initialize the database
        insertedCultivatedCrop = cultivatedCropRepository.saveAndFlush(cultivatedCrop);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedCrop
        restCultivatedCropMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedCrop.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedCropRepository.count();
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

    protected CultivatedCrop getPersistedCultivatedCrop(CultivatedCrop cultivatedCrop) {
        return cultivatedCropRepository.findById(cultivatedCrop.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedCropToMatchAllProperties(CultivatedCrop expectedCultivatedCrop) {
        assertCultivatedCropAllPropertiesEquals(expectedCultivatedCrop, getPersistedCultivatedCrop(expectedCultivatedCrop));
    }

    protected void assertPersistedCultivatedCropToMatchUpdatableProperties(CultivatedCrop expectedCultivatedCrop) {
        assertCultivatedCropAllUpdatablePropertiesEquals(expectedCultivatedCrop, getPersistedCultivatedCrop(expectedCultivatedCrop));
    }
}
