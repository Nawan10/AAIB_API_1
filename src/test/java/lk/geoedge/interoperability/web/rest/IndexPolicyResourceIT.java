package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPolicyAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPolicy;
import lk.geoedge.interoperability.domain.IndexPolicyCropType;
import lk.geoedge.interoperability.domain.IndexPolicyCropVariety;
import lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy;
import lk.geoedge.interoperability.domain.IndexPolicySeason;
import lk.geoedge.interoperability.domain.IndexPolicyWeatherStation;
import lk.geoedge.interoperability.repository.IndexPolicyRepository;
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
 * Integration tests for the {@link IndexPolicyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPolicyResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_STAGE_NO = 1;
    private static final Integer UPDATED_STAGE_NO = 2;
    private static final Integer SMALLER_STAGE_NO = 1 - 1;

    private static final Integer DEFAULT_INDEX_STATUS = 1;
    private static final Integer UPDATED_INDEX_STATUS = 2;
    private static final Integer SMALLER_INDEX_STATUS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/index-policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPolicyRepository indexPolicyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPolicyMockMvc;

    private IndexPolicy indexPolicy;

    private IndexPolicy insertedIndexPolicy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicy createEntity() {
        return new IndexPolicy()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .stageNo(DEFAULT_STAGE_NO)
            .indexStatus(DEFAULT_INDEX_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicy createUpdatedEntity() {
        return new IndexPolicy()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .stageNo(UPDATED_STAGE_NO)
            .indexStatus(UPDATED_INDEX_STATUS);
    }

    @BeforeEach
    void initTest() {
        indexPolicy = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPolicy != null) {
            indexPolicyRepository.delete(insertedIndexPolicy);
            insertedIndexPolicy = null;
        }
    }

    @Test
    @Transactional
    void createIndexPolicy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPolicy
        var returnedIndexPolicy = om.readValue(
            restIndexPolicyMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicy))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPolicy.class
        );

        // Validate the IndexPolicy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPolicyUpdatableFieldsEquals(returnedIndexPolicy, getPersistedIndexPolicy(returnedIndexPolicy));

        insertedIndexPolicy = returnedIndexPolicy;
    }

    @Test
    @Transactional
    void createIndexPolicyWithExistingId() throws Exception {
        // Create the IndexPolicy with an existing ID
        indexPolicy.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPolicyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicy)))
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPolicies() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList
        restIndexPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].stageNo").value(hasItem(DEFAULT_STAGE_NO)))
            .andExpect(jsonPath("$.[*].indexStatus").value(hasItem(DEFAULT_INDEX_STATUS)));
    }

    @Test
    @Transactional
    void getIndexPolicy() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get the indexPolicy
        restIndexPolicyMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPolicy.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.stageNo").value(DEFAULT_STAGE_NO))
            .andExpect(jsonPath("$.indexStatus").value(DEFAULT_INDEX_STATUS));
    }

    @Test
    @Transactional
    void getIndexPoliciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        Long id = indexPolicy.getId();

        defaultIndexPolicyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPolicyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPolicyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate equals to
        defaultIndexPolicyFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate in
        defaultIndexPolicyFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate is not null
        defaultIndexPolicyFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate is greater than or equal to
        defaultIndexPolicyFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate is less than or equal to
        defaultIndexPolicyFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate is less than
        defaultIndexPolicyFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where startDate is greater than
        defaultIndexPolicyFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate equals to
        defaultIndexPolicyFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate in
        defaultIndexPolicyFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate is not null
        defaultIndexPolicyFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate is greater than or equal to
        defaultIndexPolicyFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate is less than or equal to
        defaultIndexPolicyFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate is less than
        defaultIndexPolicyFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where endDate is greater than
        defaultIndexPolicyFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo equals to
        defaultIndexPolicyFiltering("stageNo.equals=" + DEFAULT_STAGE_NO, "stageNo.equals=" + UPDATED_STAGE_NO);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo in
        defaultIndexPolicyFiltering("stageNo.in=" + DEFAULT_STAGE_NO + "," + UPDATED_STAGE_NO, "stageNo.in=" + UPDATED_STAGE_NO);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo is not null
        defaultIndexPolicyFiltering("stageNo.specified=true", "stageNo.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo is greater than or equal to
        defaultIndexPolicyFiltering("stageNo.greaterThanOrEqual=" + DEFAULT_STAGE_NO, "stageNo.greaterThanOrEqual=" + UPDATED_STAGE_NO);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo is less than or equal to
        defaultIndexPolicyFiltering("stageNo.lessThanOrEqual=" + DEFAULT_STAGE_NO, "stageNo.lessThanOrEqual=" + SMALLER_STAGE_NO);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo is less than
        defaultIndexPolicyFiltering("stageNo.lessThan=" + UPDATED_STAGE_NO, "stageNo.lessThan=" + DEFAULT_STAGE_NO);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByStageNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where stageNo is greater than
        defaultIndexPolicyFiltering("stageNo.greaterThan=" + SMALLER_STAGE_NO, "stageNo.greaterThan=" + DEFAULT_STAGE_NO);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus equals to
        defaultIndexPolicyFiltering("indexStatus.equals=" + DEFAULT_INDEX_STATUS, "indexStatus.equals=" + UPDATED_INDEX_STATUS);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus in
        defaultIndexPolicyFiltering(
            "indexStatus.in=" + DEFAULT_INDEX_STATUS + "," + UPDATED_INDEX_STATUS,
            "indexStatus.in=" + UPDATED_INDEX_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus is not null
        defaultIndexPolicyFiltering("indexStatus.specified=true", "indexStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus is greater than or equal to
        defaultIndexPolicyFiltering(
            "indexStatus.greaterThanOrEqual=" + DEFAULT_INDEX_STATUS,
            "indexStatus.greaterThanOrEqual=" + UPDATED_INDEX_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus is less than or equal to
        defaultIndexPolicyFiltering(
            "indexStatus.lessThanOrEqual=" + DEFAULT_INDEX_STATUS,
            "indexStatus.lessThanOrEqual=" + SMALLER_INDEX_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus is less than
        defaultIndexPolicyFiltering("indexStatus.lessThan=" + UPDATED_INDEX_STATUS, "indexStatus.lessThan=" + DEFAULT_INDEX_STATUS);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByIndexStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        // Get all the indexPolicyList where indexStatus is greater than
        defaultIndexPolicyFiltering("indexStatus.greaterThan=" + SMALLER_INDEX_STATUS, "indexStatus.greaterThan=" + DEFAULT_INDEX_STATUS);
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByPolicyIsEqualToSomething() throws Exception {
        IndexPolicyInsurancePolicy policy;
        if (TestUtil.findAll(em, IndexPolicyInsurancePolicy.class).isEmpty()) {
            indexPolicyRepository.saveAndFlush(indexPolicy);
            policy = IndexPolicyInsurancePolicyResourceIT.createEntity();
        } else {
            policy = TestUtil.findAll(em, IndexPolicyInsurancePolicy.class).get(0);
        }
        em.persist(policy);
        em.flush();
        indexPolicy.setPolicy(policy);
        indexPolicyRepository.saveAndFlush(indexPolicy);
        Long policyId = policy.getId();
        // Get all the indexPolicyList where policy equals to policyId
        defaultIndexPolicyShouldBeFound("policyId.equals=" + policyId);

        // Get all the indexPolicyList where policy equals to (policyId + 1)
        defaultIndexPolicyShouldNotBeFound("policyId.equals=" + (policyId + 1));
    }

    @Test
    @Transactional
    void getAllIndexPoliciesBySeasonIsEqualToSomething() throws Exception {
        IndexPolicySeason season;
        if (TestUtil.findAll(em, IndexPolicySeason.class).isEmpty()) {
            indexPolicyRepository.saveAndFlush(indexPolicy);
            season = IndexPolicySeasonResourceIT.createEntity();
        } else {
            season = TestUtil.findAll(em, IndexPolicySeason.class).get(0);
        }
        em.persist(season);
        em.flush();
        indexPolicy.setSeason(season);
        indexPolicyRepository.saveAndFlush(indexPolicy);
        Long seasonId = season.getId();
        // Get all the indexPolicyList where season equals to seasonId
        defaultIndexPolicyShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the indexPolicyList where season equals to (seasonId + 1)
        defaultIndexPolicyShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByCropVarietyIsEqualToSomething() throws Exception {
        IndexPolicyCropVariety cropVariety;
        if (TestUtil.findAll(em, IndexPolicyCropVariety.class).isEmpty()) {
            indexPolicyRepository.saveAndFlush(indexPolicy);
            cropVariety = IndexPolicyCropVarietyResourceIT.createEntity();
        } else {
            cropVariety = TestUtil.findAll(em, IndexPolicyCropVariety.class).get(0);
        }
        em.persist(cropVariety);
        em.flush();
        indexPolicy.setCropVariety(cropVariety);
        indexPolicyRepository.saveAndFlush(indexPolicy);
        Long cropVarietyId = cropVariety.getId();
        // Get all the indexPolicyList where cropVariety equals to cropVarietyId
        defaultIndexPolicyShouldBeFound("cropVarietyId.equals=" + cropVarietyId);

        // Get all the indexPolicyList where cropVariety equals to (cropVarietyId + 1)
        defaultIndexPolicyShouldNotBeFound("cropVarietyId.equals=" + (cropVarietyId + 1));
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByCropIsEqualToSomething() throws Exception {
        IndexPolicyCropType crop;
        if (TestUtil.findAll(em, IndexPolicyCropType.class).isEmpty()) {
            indexPolicyRepository.saveAndFlush(indexPolicy);
            crop = IndexPolicyCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, IndexPolicyCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        indexPolicy.setCrop(crop);
        indexPolicyRepository.saveAndFlush(indexPolicy);
        Long cropId = crop.getId();
        // Get all the indexPolicyList where crop equals to cropId
        defaultIndexPolicyShouldBeFound("cropId.equals=" + cropId);

        // Get all the indexPolicyList where crop equals to (cropId + 1)
        defaultIndexPolicyShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    @Test
    @Transactional
    void getAllIndexPoliciesByWeatherStationIsEqualToSomething() throws Exception {
        IndexPolicyWeatherStation weatherStation;
        if (TestUtil.findAll(em, IndexPolicyWeatherStation.class).isEmpty()) {
            indexPolicyRepository.saveAndFlush(indexPolicy);
            weatherStation = IndexPolicyWeatherStationResourceIT.createEntity();
        } else {
            weatherStation = TestUtil.findAll(em, IndexPolicyWeatherStation.class).get(0);
        }
        em.persist(weatherStation);
        em.flush();
        indexPolicy.setWeatherStation(weatherStation);
        indexPolicyRepository.saveAndFlush(indexPolicy);
        Long weatherStationId = weatherStation.getId();
        // Get all the indexPolicyList where weatherStation equals to weatherStationId
        defaultIndexPolicyShouldBeFound("weatherStationId.equals=" + weatherStationId);

        // Get all the indexPolicyList where weatherStation equals to (weatherStationId + 1)
        defaultIndexPolicyShouldNotBeFound("weatherStationId.equals=" + (weatherStationId + 1));
    }

    private void defaultIndexPolicyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPolicyShouldBeFound(shouldBeFound);
        defaultIndexPolicyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPolicyShouldBeFound(String filter) throws Exception {
        restIndexPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].stageNo").value(hasItem(DEFAULT_STAGE_NO)))
            .andExpect(jsonPath("$.[*].indexStatus").value(hasItem(DEFAULT_INDEX_STATUS)));

        // Check, that the count call also returns 1
        restIndexPolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPolicyShouldNotBeFound(String filter) throws Exception {
        restIndexPolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPolicy() throws Exception {
        // Get the indexPolicy
        restIndexPolicyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPolicy() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicy
        IndexPolicy updatedIndexPolicy = indexPolicyRepository.findById(indexPolicy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPolicy are not directly saved in db
        em.detach(updatedIndexPolicy);
        updatedIndexPolicy
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .stageNo(UPDATED_STAGE_NO)
            .indexStatus(UPDATED_INDEX_STATUS);

        restIndexPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPolicy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPolicy))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPolicyToMatchAllProperties(updatedIndexPolicy);
    }

    @Test
    @Transactional
    void putNonExistingIndexPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPolicy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPolicy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPolicyWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicy using partial update
        IndexPolicy partialUpdatedIndexPolicy = new IndexPolicy();
        partialUpdatedIndexPolicy.setId(indexPolicy.getId());

        partialUpdatedIndexPolicy.startDate(UPDATED_START_DATE).stageNo(UPDATED_STAGE_NO).indexStatus(UPDATED_INDEX_STATUS);

        restIndexPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicy))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPolicy, indexPolicy),
            getPersistedIndexPolicy(indexPolicy)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPolicyWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicy using partial update
        IndexPolicy partialUpdatedIndexPolicy = new IndexPolicy();
        partialUpdatedIndexPolicy.setId(indexPolicy.getId());

        partialUpdatedIndexPolicy
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .stageNo(UPDATED_STAGE_NO)
            .indexStatus(UPDATED_INDEX_STATUS);

        restIndexPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicy))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyUpdatableFieldsEquals(partialUpdatedIndexPolicy, getPersistedIndexPolicy(partialUpdatedIndexPolicy));
    }

    @Test
    @Transactional
    void patchNonExistingIndexPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(indexPolicy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPolicy() throws Exception {
        // Initialize the database
        insertedIndexPolicy = indexPolicyRepository.saveAndFlush(indexPolicy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPolicy
        restIndexPolicyMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPolicy.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPolicyRepository.count();
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

    protected IndexPolicy getPersistedIndexPolicy(IndexPolicy indexPolicy) {
        return indexPolicyRepository.findById(indexPolicy.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPolicyToMatchAllProperties(IndexPolicy expectedIndexPolicy) {
        assertIndexPolicyAllPropertiesEquals(expectedIndexPolicy, getPersistedIndexPolicy(expectedIndexPolicy));
    }

    protected void assertPersistedIndexPolicyToMatchUpdatableProperties(IndexPolicy expectedIndexPolicy) {
        assertIndexPolicyAllUpdatablePropertiesEquals(expectedIndexPolicy, getPersistedIndexPolicy(expectedIndexPolicy));
    }
}
