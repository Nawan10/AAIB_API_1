package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLand;
import lk.geoedge.interoperability.domain.Farmer;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLand;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandRepository;
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
 * Integration tests for the {@link InsuranceCultivatedLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCultivatedLandResourceIT {

    private static final String DEFAULT_CROP_DURATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CROP_DURATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INSURANCE_POLICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_POLICE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUM_INSURED_PER_ACRE = 1;
    private static final Integer UPDATED_SUM_INSURED_PER_ACRE = 2;
    private static final Integer SMALLER_SUM_INSURED_PER_ACRE = 1 - 1;

    private static final Integer DEFAULT_INSURANCE_EXTENT = 1;
    private static final Integer UPDATED_INSURANCE_EXTENT = 2;
    private static final Integer SMALLER_INSURANCE_EXTENT = 1 - 1;

    private static final Integer DEFAULT_SUM_AMOUNT = 1;
    private static final Integer UPDATED_SUM_AMOUNT = 2;
    private static final Integer SMALLER_SUM_AMOUNT = 1 - 1;

    private static final String DEFAULT_INSURANCE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurance-cultivated-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCultivatedLandRepository insuranceCultivatedLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCultivatedLandMockMvc;

    private InsuranceCultivatedLand insuranceCultivatedLand;

    private InsuranceCultivatedLand insertedInsuranceCultivatedLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLand createEntity() {
        return new InsuranceCultivatedLand()
            .cropDurationId(DEFAULT_CROP_DURATION_ID)
            .insurancePoliceId(DEFAULT_INSURANCE_POLICE_ID)
            .sumInsuredPerAcre(DEFAULT_SUM_INSURED_PER_ACRE)
            .insuranceExtent(DEFAULT_INSURANCE_EXTENT)
            .sumAmount(DEFAULT_SUM_AMOUNT)
            .insuranceStatus(DEFAULT_INSURANCE_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLand createUpdatedEntity() {
        return new InsuranceCultivatedLand()
            .cropDurationId(UPDATED_CROP_DURATION_ID)
            .insurancePoliceId(UPDATED_INSURANCE_POLICE_ID)
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .insuranceExtent(UPDATED_INSURANCE_EXTENT)
            .sumAmount(UPDATED_SUM_AMOUNT)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        insuranceCultivatedLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCultivatedLand != null) {
            insuranceCultivatedLandRepository.delete(insertedInsuranceCultivatedLand);
            insertedInsuranceCultivatedLand = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCultivatedLand
        var returnedInsuranceCultivatedLand = om.readValue(
            restInsuranceCultivatedLandMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCultivatedLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCultivatedLand.class
        );

        // Validate the InsuranceCultivatedLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCultivatedLandUpdatableFieldsEquals(
            returnedInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLand(returnedInsuranceCultivatedLand)
        );

        insertedInsuranceCultivatedLand = returnedInsuranceCultivatedLand;
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandWithExistingId() throws Exception {
        // Create the InsuranceCultivatedLand with an existing ID
        insuranceCultivatedLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCultivatedLandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLands() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList
        restInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].cropDurationId").value(hasItem(DEFAULT_CROP_DURATION_ID)))
            .andExpect(jsonPath("$.[*].insurancePoliceId").value(hasItem(DEFAULT_INSURANCE_POLICE_ID)))
            .andExpect(jsonPath("$.[*].sumInsuredPerAcre").value(hasItem(DEFAULT_SUM_INSURED_PER_ACRE)))
            .andExpect(jsonPath("$.[*].insuranceExtent").value(hasItem(DEFAULT_INSURANCE_EXTENT)))
            .andExpect(jsonPath("$.[*].sumAmount").value(hasItem(DEFAULT_SUM_AMOUNT)))
            .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get the insuranceCultivatedLand
        restInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCultivatedLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCultivatedLand.getId().intValue()))
            .andExpect(jsonPath("$.cropDurationId").value(DEFAULT_CROP_DURATION_ID))
            .andExpect(jsonPath("$.insurancePoliceId").value(DEFAULT_INSURANCE_POLICE_ID))
            .andExpect(jsonPath("$.sumInsuredPerAcre").value(DEFAULT_SUM_INSURED_PER_ACRE))
            .andExpect(jsonPath("$.insuranceExtent").value(DEFAULT_INSURANCE_EXTENT))
            .andExpect(jsonPath("$.sumAmount").value(DEFAULT_SUM_AMOUNT))
            .andExpect(jsonPath("$.insuranceStatus").value(DEFAULT_INSURANCE_STATUS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        Long id = insuranceCultivatedLand.getId();

        defaultInsuranceCultivatedLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCultivatedLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCultivatedLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCropDurationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where cropDurationId equals to
        defaultInsuranceCultivatedLandFiltering(
            "cropDurationId.equals=" + DEFAULT_CROP_DURATION_ID,
            "cropDurationId.equals=" + UPDATED_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCropDurationIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where cropDurationId in
        defaultInsuranceCultivatedLandFiltering(
            "cropDurationId.in=" + DEFAULT_CROP_DURATION_ID + "," + UPDATED_CROP_DURATION_ID,
            "cropDurationId.in=" + UPDATED_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCropDurationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where cropDurationId is not null
        defaultInsuranceCultivatedLandFiltering("cropDurationId.specified=true", "cropDurationId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCropDurationIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where cropDurationId contains
        defaultInsuranceCultivatedLandFiltering(
            "cropDurationId.contains=" + DEFAULT_CROP_DURATION_ID,
            "cropDurationId.contains=" + UPDATED_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCropDurationIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where cropDurationId does not contain
        defaultInsuranceCultivatedLandFiltering(
            "cropDurationId.doesNotContain=" + UPDATED_CROP_DURATION_ID,
            "cropDurationId.doesNotContain=" + DEFAULT_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsurancePoliceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insurancePoliceId equals to
        defaultInsuranceCultivatedLandFiltering(
            "insurancePoliceId.equals=" + DEFAULT_INSURANCE_POLICE_ID,
            "insurancePoliceId.equals=" + UPDATED_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsurancePoliceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insurancePoliceId in
        defaultInsuranceCultivatedLandFiltering(
            "insurancePoliceId.in=" + DEFAULT_INSURANCE_POLICE_ID + "," + UPDATED_INSURANCE_POLICE_ID,
            "insurancePoliceId.in=" + UPDATED_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsurancePoliceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insurancePoliceId is not null
        defaultInsuranceCultivatedLandFiltering("insurancePoliceId.specified=true", "insurancePoliceId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsurancePoliceIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insurancePoliceId contains
        defaultInsuranceCultivatedLandFiltering(
            "insurancePoliceId.contains=" + DEFAULT_INSURANCE_POLICE_ID,
            "insurancePoliceId.contains=" + UPDATED_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsurancePoliceIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insurancePoliceId does not contain
        defaultInsuranceCultivatedLandFiltering(
            "insurancePoliceId.doesNotContain=" + UPDATED_INSURANCE_POLICE_ID,
            "insurancePoliceId.doesNotContain=" + DEFAULT_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre equals to
        defaultInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.equals=" + DEFAULT_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.equals=" + UPDATED_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre in
        defaultInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.in=" + DEFAULT_SUM_INSURED_PER_ACRE + "," + UPDATED_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.in=" + UPDATED_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre is not null
        defaultInsuranceCultivatedLandFiltering("sumInsuredPerAcre.specified=true", "sumInsuredPerAcre.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre is greater than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.greaterThanOrEqual=" + DEFAULT_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.greaterThanOrEqual=" + UPDATED_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre is less than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.lessThanOrEqual=" + DEFAULT_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.lessThanOrEqual=" + SMALLER_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre is less than
        defaultInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.lessThan=" + UPDATED_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.lessThan=" + DEFAULT_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumInsuredPerAcreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumInsuredPerAcre is greater than
        defaultInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.greaterThan=" + SMALLER_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.greaterThan=" + DEFAULT_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent equals to
        defaultInsuranceCultivatedLandFiltering(
            "insuranceExtent.equals=" + DEFAULT_INSURANCE_EXTENT,
            "insuranceExtent.equals=" + UPDATED_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent in
        defaultInsuranceCultivatedLandFiltering(
            "insuranceExtent.in=" + DEFAULT_INSURANCE_EXTENT + "," + UPDATED_INSURANCE_EXTENT,
            "insuranceExtent.in=" + UPDATED_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent is not null
        defaultInsuranceCultivatedLandFiltering("insuranceExtent.specified=true", "insuranceExtent.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent is greater than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "insuranceExtent.greaterThanOrEqual=" + DEFAULT_INSURANCE_EXTENT,
            "insuranceExtent.greaterThanOrEqual=" + UPDATED_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent is less than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "insuranceExtent.lessThanOrEqual=" + DEFAULT_INSURANCE_EXTENT,
            "insuranceExtent.lessThanOrEqual=" + SMALLER_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent is less than
        defaultInsuranceCultivatedLandFiltering(
            "insuranceExtent.lessThan=" + UPDATED_INSURANCE_EXTENT,
            "insuranceExtent.lessThan=" + DEFAULT_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceExtentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceExtent is greater than
        defaultInsuranceCultivatedLandFiltering(
            "insuranceExtent.greaterThan=" + SMALLER_INSURANCE_EXTENT,
            "insuranceExtent.greaterThan=" + DEFAULT_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount equals to
        defaultInsuranceCultivatedLandFiltering("sumAmount.equals=" + DEFAULT_SUM_AMOUNT, "sumAmount.equals=" + UPDATED_SUM_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount in
        defaultInsuranceCultivatedLandFiltering(
            "sumAmount.in=" + DEFAULT_SUM_AMOUNT + "," + UPDATED_SUM_AMOUNT,
            "sumAmount.in=" + UPDATED_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount is not null
        defaultInsuranceCultivatedLandFiltering("sumAmount.specified=true", "sumAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount is greater than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "sumAmount.greaterThanOrEqual=" + DEFAULT_SUM_AMOUNT,
            "sumAmount.greaterThanOrEqual=" + UPDATED_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount is less than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "sumAmount.lessThanOrEqual=" + DEFAULT_SUM_AMOUNT,
            "sumAmount.lessThanOrEqual=" + SMALLER_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount is less than
        defaultInsuranceCultivatedLandFiltering("sumAmount.lessThan=" + UPDATED_SUM_AMOUNT, "sumAmount.lessThan=" + DEFAULT_SUM_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsBySumAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where sumAmount is greater than
        defaultInsuranceCultivatedLandFiltering(
            "sumAmount.greaterThan=" + SMALLER_SUM_AMOUNT,
            "sumAmount.greaterThan=" + DEFAULT_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceStatus equals to
        defaultInsuranceCultivatedLandFiltering(
            "insuranceStatus.equals=" + DEFAULT_INSURANCE_STATUS,
            "insuranceStatus.equals=" + UPDATED_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceStatus in
        defaultInsuranceCultivatedLandFiltering(
            "insuranceStatus.in=" + DEFAULT_INSURANCE_STATUS + "," + UPDATED_INSURANCE_STATUS,
            "insuranceStatus.in=" + UPDATED_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceStatus is not null
        defaultInsuranceCultivatedLandFiltering("insuranceStatus.specified=true", "insuranceStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceStatus contains
        defaultInsuranceCultivatedLandFiltering(
            "insuranceStatus.contains=" + DEFAULT_INSURANCE_STATUS,
            "insuranceStatus.contains=" + UPDATED_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByInsuranceStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where insuranceStatus does not contain
        defaultInsuranceCultivatedLandFiltering(
            "insuranceStatus.doesNotContain=" + UPDATED_INSURANCE_STATUS,
            "insuranceStatus.doesNotContain=" + DEFAULT_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt equals to
        defaultInsuranceCultivatedLandFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt in
        defaultInsuranceCultivatedLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt is not null
        defaultInsuranceCultivatedLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt is greater than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt is less than or equal to
        defaultInsuranceCultivatedLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt is less than
        defaultInsuranceCultivatedLandFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where createdAt is greater than
        defaultInsuranceCultivatedLandFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where addedBy equals to
        defaultInsuranceCultivatedLandFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where addedBy in
        defaultInsuranceCultivatedLandFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where addedBy is not null
        defaultInsuranceCultivatedLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where addedBy contains
        defaultInsuranceCultivatedLandFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        // Get all the insuranceCultivatedLandList where addedBy does not contain
        defaultInsuranceCultivatedLandFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByFarmerIsEqualToSomething() throws Exception {
        Farmer farmer;
        if (TestUtil.findAll(em, Farmer.class).isEmpty()) {
            insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);
            farmer = FarmerResourceIT.createEntity();
        } else {
            farmer = TestUtil.findAll(em, Farmer.class).get(0);
        }
        em.persist(farmer);
        em.flush();
        insuranceCultivatedLand.setFarmer(farmer);
        insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);
        Long farmerId = farmer.getId();
        // Get all the insuranceCultivatedLandList where farmer equals to farmerId
        defaultInsuranceCultivatedLandShouldBeFound("farmerId.equals=" + farmerId);

        // Get all the insuranceCultivatedLandList where farmer equals to (farmerId + 1)
        defaultInsuranceCultivatedLandShouldNotBeFound("farmerId.equals=" + (farmerId + 1));
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCultivatedLandIsEqualToSomething() throws Exception {
        CultivatedLand cultivatedLand;
        if (TestUtil.findAll(em, CultivatedLand.class).isEmpty()) {
            insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);
            cultivatedLand = CultivatedLandResourceIT.createEntity();
        } else {
            cultivatedLand = TestUtil.findAll(em, CultivatedLand.class).get(0);
        }
        em.persist(cultivatedLand);
        em.flush();
        insuranceCultivatedLand.setCultivatedLand(cultivatedLand);
        insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);
        Long cultivatedLandId = cultivatedLand.getId();
        // Get all the insuranceCultivatedLandList where cultivatedLand equals to cultivatedLandId
        defaultInsuranceCultivatedLandShouldBeFound("cultivatedLandId.equals=" + cultivatedLandId);

        // Get all the insuranceCultivatedLandList where cultivatedLand equals to (cultivatedLandId + 1)
        defaultInsuranceCultivatedLandShouldNotBeFound("cultivatedLandId.equals=" + (cultivatedLandId + 1));
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandsByCropIsEqualToSomething() throws Exception {
        InsuranceCultivatedLandCropType crop;
        if (TestUtil.findAll(em, InsuranceCultivatedLandCropType.class).isEmpty()) {
            insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);
            crop = InsuranceCultivatedLandCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, InsuranceCultivatedLandCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        insuranceCultivatedLand.setCrop(crop);
        insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);
        Long cropId = crop.getId();
        // Get all the insuranceCultivatedLandList where crop equals to cropId
        defaultInsuranceCultivatedLandShouldBeFound("cropId.equals=" + cropId);

        // Get all the insuranceCultivatedLandList where crop equals to (cropId + 1)
        defaultInsuranceCultivatedLandShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultInsuranceCultivatedLandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCultivatedLandShouldBeFound(shouldBeFound);
        defaultInsuranceCultivatedLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCultivatedLandShouldBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].cropDurationId").value(hasItem(DEFAULT_CROP_DURATION_ID)))
            .andExpect(jsonPath("$.[*].insurancePoliceId").value(hasItem(DEFAULT_INSURANCE_POLICE_ID)))
            .andExpect(jsonPath("$.[*].sumInsuredPerAcre").value(hasItem(DEFAULT_SUM_INSURED_PER_ACRE)))
            .andExpect(jsonPath("$.[*].insuranceExtent").value(hasItem(DEFAULT_INSURANCE_EXTENT)))
            .andExpect(jsonPath("$.[*].sumAmount").value(hasItem(DEFAULT_SUM_AMOUNT)))
            .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCultivatedLandShouldNotBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCultivatedLand() throws Exception {
        // Get the insuranceCultivatedLand
        restInsuranceCultivatedLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLand
        InsuranceCultivatedLand updatedInsuranceCultivatedLand = insuranceCultivatedLandRepository
            .findById(insuranceCultivatedLand.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCultivatedLand are not directly saved in db
        em.detach(updatedInsuranceCultivatedLand);
        updatedInsuranceCultivatedLand
            .cropDurationId(UPDATED_CROP_DURATION_ID)
            .insurancePoliceId(UPDATED_INSURANCE_POLICE_ID)
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .insuranceExtent(UPDATED_INSURANCE_EXTENT)
            .sumAmount(UPDATED_SUM_AMOUNT)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCultivatedLandToMatchAllProperties(updatedInsuranceCultivatedLand);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLand using partial update
        InsuranceCultivatedLand partialUpdatedInsuranceCultivatedLand = new InsuranceCultivatedLand();
        partialUpdatedInsuranceCultivatedLand.setId(insuranceCultivatedLand.getId());

        partialUpdatedInsuranceCultivatedLand
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .sumAmount(UPDATED_SUM_AMOUNT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCultivatedLand, insuranceCultivatedLand),
            getPersistedInsuranceCultivatedLand(insuranceCultivatedLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLand using partial update
        InsuranceCultivatedLand partialUpdatedInsuranceCultivatedLand = new InsuranceCultivatedLand();
        partialUpdatedInsuranceCultivatedLand.setId(insuranceCultivatedLand.getId());

        partialUpdatedInsuranceCultivatedLand
            .cropDurationId(UPDATED_CROP_DURATION_ID)
            .insurancePoliceId(UPDATED_INSURANCE_POLICE_ID)
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .insuranceExtent(UPDATED_INSURANCE_EXTENT)
            .sumAmount(UPDATED_SUM_AMOUNT)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandUpdatableFieldsEquals(
            partialUpdatedInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLand(partialUpdatedInsuranceCultivatedLand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLand = insuranceCultivatedLandRepository.saveAndFlush(insuranceCultivatedLand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCultivatedLand
        restInsuranceCultivatedLandMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceCultivatedLand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCultivatedLandRepository.count();
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

    protected InsuranceCultivatedLand getPersistedInsuranceCultivatedLand(InsuranceCultivatedLand insuranceCultivatedLand) {
        return insuranceCultivatedLandRepository.findById(insuranceCultivatedLand.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCultivatedLandToMatchAllProperties(InsuranceCultivatedLand expectedInsuranceCultivatedLand) {
        assertInsuranceCultivatedLandAllPropertiesEquals(
            expectedInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLand(expectedInsuranceCultivatedLand)
        );
    }

    protected void assertPersistedInsuranceCultivatedLandToMatchUpdatableProperties(
        InsuranceCultivatedLand expectedInsuranceCultivatedLand
    ) {
        assertInsuranceCultivatedLandAllUpdatablePropertiesEquals(
            expectedInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLand(expectedInsuranceCultivatedLand)
        );
    }
}
