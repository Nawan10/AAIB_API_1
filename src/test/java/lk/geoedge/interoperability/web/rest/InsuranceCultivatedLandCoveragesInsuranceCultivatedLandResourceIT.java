package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandAsserts.*;
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
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
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
 * Integration tests for the {@link InsuranceCultivatedLandCoveragesInsuranceCultivatedLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandResourceIT {

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

    private static final String ENTITY_API_URL = "/api/insurance-cultivated-land-coverages-insurance-cultivated-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc;

    private InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand;

    private InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandCoveragesInsuranceCultivatedLand createEntity() {
        return new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand()
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
    public static InsuranceCultivatedLandCoveragesInsuranceCultivatedLand createUpdatedEntity() {
        return new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand()
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
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand != null) {
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.delete(
                insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );
            insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand
        var returnedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand = om.readValue(
            restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCultivatedLandCoveragesInsuranceCultivatedLand.class
        );

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCultivatedLandCoveragesInsuranceCultivatedLandUpdatableFieldsEquals(
            returnedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
                returnedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            )
        );

        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand = returnedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCoveragesInsuranceCultivatedLandWithExistingId() throws Exception {
        // Create the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand with an existing ID
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLands() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId().intValue())))
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
    void getInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get the insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId().intValue()))
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
    void getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        Long id = insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId();

        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCropDurationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where cropDurationId equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "cropDurationId.equals=" + DEFAULT_CROP_DURATION_ID,
            "cropDurationId.equals=" + UPDATED_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCropDurationIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where cropDurationId in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "cropDurationId.in=" + DEFAULT_CROP_DURATION_ID + "," + UPDATED_CROP_DURATION_ID,
            "cropDurationId.in=" + UPDATED_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCropDurationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where cropDurationId is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "cropDurationId.specified=true",
            "cropDurationId.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCropDurationIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where cropDurationId contains
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "cropDurationId.contains=" + DEFAULT_CROP_DURATION_ID,
            "cropDurationId.contains=" + UPDATED_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCropDurationIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where cropDurationId does not contain
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "cropDurationId.doesNotContain=" + UPDATED_CROP_DURATION_ID,
            "cropDurationId.doesNotContain=" + DEFAULT_CROP_DURATION_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsurancePoliceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insurancePoliceId equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insurancePoliceId.equals=" + DEFAULT_INSURANCE_POLICE_ID,
            "insurancePoliceId.equals=" + UPDATED_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsurancePoliceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insurancePoliceId in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insurancePoliceId.in=" + DEFAULT_INSURANCE_POLICE_ID + "," + UPDATED_INSURANCE_POLICE_ID,
            "insurancePoliceId.in=" + UPDATED_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsurancePoliceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insurancePoliceId is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insurancePoliceId.specified=true",
            "insurancePoliceId.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsurancePoliceIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insurancePoliceId contains
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insurancePoliceId.contains=" + DEFAULT_INSURANCE_POLICE_ID,
            "insurancePoliceId.contains=" + UPDATED_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsurancePoliceIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insurancePoliceId does not contain
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insurancePoliceId.doesNotContain=" + UPDATED_INSURANCE_POLICE_ID,
            "insurancePoliceId.doesNotContain=" + DEFAULT_INSURANCE_POLICE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.equals=" + DEFAULT_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.equals=" + UPDATED_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.in=" + DEFAULT_SUM_INSURED_PER_ACRE + "," + UPDATED_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.in=" + UPDATED_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.specified=true",
            "sumInsuredPerAcre.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsGreaterThanOrEqualToSomething()
        throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre is greater than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.greaterThanOrEqual=" + DEFAULT_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.greaterThanOrEqual=" + UPDATED_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre is less than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.lessThanOrEqual=" + DEFAULT_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.lessThanOrEqual=" + SMALLER_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre is less than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.lessThan=" + UPDATED_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.lessThan=" + DEFAULT_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumInsuredPerAcreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumInsuredPerAcre is greater than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumInsuredPerAcre.greaterThan=" + SMALLER_SUM_INSURED_PER_ACRE,
            "sumInsuredPerAcre.greaterThan=" + DEFAULT_SUM_INSURED_PER_ACRE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.equals=" + DEFAULT_INSURANCE_EXTENT,
            "insuranceExtent.equals=" + UPDATED_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.in=" + DEFAULT_INSURANCE_EXTENT + "," + UPDATED_INSURANCE_EXTENT,
            "insuranceExtent.in=" + UPDATED_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.specified=true",
            "insuranceExtent.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent is greater than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.greaterThanOrEqual=" + DEFAULT_INSURANCE_EXTENT,
            "insuranceExtent.greaterThanOrEqual=" + UPDATED_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent is less than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.lessThanOrEqual=" + DEFAULT_INSURANCE_EXTENT,
            "insuranceExtent.lessThanOrEqual=" + SMALLER_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent is less than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.lessThan=" + UPDATED_INSURANCE_EXTENT,
            "insuranceExtent.lessThan=" + DEFAULT_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceExtentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceExtent is greater than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceExtent.greaterThan=" + SMALLER_INSURANCE_EXTENT,
            "insuranceExtent.greaterThan=" + DEFAULT_INSURANCE_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumAmount.equals=" + DEFAULT_SUM_AMOUNT,
            "sumAmount.equals=" + UPDATED_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumAmount.in=" + DEFAULT_SUM_AMOUNT + "," + UPDATED_SUM_AMOUNT,
            "sumAmount.in=" + UPDATED_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering("sumAmount.specified=true", "sumAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount is greater than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumAmount.greaterThanOrEqual=" + DEFAULT_SUM_AMOUNT,
            "sumAmount.greaterThanOrEqual=" + UPDATED_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount is less than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumAmount.lessThanOrEqual=" + DEFAULT_SUM_AMOUNT,
            "sumAmount.lessThanOrEqual=" + SMALLER_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount is less than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumAmount.lessThan=" + UPDATED_SUM_AMOUNT,
            "sumAmount.lessThan=" + DEFAULT_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsBySumAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where sumAmount is greater than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "sumAmount.greaterThan=" + SMALLER_SUM_AMOUNT,
            "sumAmount.greaterThan=" + DEFAULT_SUM_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceStatus equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceStatus.equals=" + DEFAULT_INSURANCE_STATUS,
            "insuranceStatus.equals=" + UPDATED_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceStatus in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceStatus.in=" + DEFAULT_INSURANCE_STATUS + "," + UPDATED_INSURANCE_STATUS,
            "insuranceStatus.in=" + UPDATED_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceStatus is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceStatus.specified=true",
            "insuranceStatus.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceStatus contains
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceStatus.contains=" + DEFAULT_INSURANCE_STATUS,
            "insuranceStatus.contains=" + UPDATED_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByInsuranceStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where insuranceStatus does not contain
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "insuranceStatus.doesNotContain=" + UPDATED_INSURANCE_STATUS,
            "insuranceStatus.doesNotContain=" + DEFAULT_INSURANCE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "createdAt.equals=" + DEFAULT_CREATED_AT,
            "createdAt.equals=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt is greater than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt is less than or equal to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt is less than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "createdAt.lessThan=" + UPDATED_CREATED_AT,
            "createdAt.lessThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where createdAt is greater than
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where addedBy equals to
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "addedBy.equals=" + DEFAULT_ADDED_BY,
            "addedBy.equals=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where addedBy in
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where addedBy is not null
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where addedBy contains
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "addedBy.contains=" + DEFAULT_ADDED_BY,
            "addedBy.contains=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        // Get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLandList where addedBy does not contain
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    private void defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandFiltering(String shouldBeFound, String shouldNotBeFound)
        throws Exception {
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandShouldBeFound(shouldBeFound);
        defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandShouldBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].cropDurationId").value(hasItem(DEFAULT_CROP_DURATION_ID)))
            .andExpect(jsonPath("$.[*].insurancePoliceId").value(hasItem(DEFAULT_INSURANCE_POLICE_ID)))
            .andExpect(jsonPath("$.[*].sumInsuredPerAcre").value(hasItem(DEFAULT_SUM_INSURED_PER_ACRE)))
            .andExpect(jsonPath("$.[*].insuranceExtent").value(hasItem(DEFAULT_INSURANCE_EXTENT)))
            .andExpect(jsonPath("$.[*].sumAmount").value(hasItem(DEFAULT_SUM_AMOUNT)))
            .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCultivatedLandCoveragesInsuranceCultivatedLandShouldNotBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        // Get the insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository
                .findById(insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand are not directly saved in db
        em.detach(updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand);
        updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            .cropDurationId(UPDATED_CROP_DURATION_ID)
            .insurancePoliceId(UPDATED_INSURANCE_POLICE_ID)
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .insuranceExtent(UPDATED_INSURANCE_EXTENT)
            .sumAmount(UPDATED_SUM_AMOUNT)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLandToMatchAllProperties(
            updatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCultivatedLandCoveragesInsuranceCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCoveragesInsuranceCultivatedLand using partial update
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand();
        partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId()
        );

        partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            .cropDurationId(UPDATED_CROP_DURATION_ID)
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .insuranceExtent(UPDATED_INSURANCE_EXTENT)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCoveragesInsuranceCultivatedLandUpdatableFieldsEquals(
            createUpdateProxyForBean(
                partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand,
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            ),
            getPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(insuranceCultivatedLandCoveragesInsuranceCultivatedLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCultivatedLandCoveragesInsuranceCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCoveragesInsuranceCultivatedLand using partial update
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand();
        partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId()
        );

        partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            .cropDurationId(UPDATED_CROP_DURATION_ID)
            .insurancePoliceId(UPDATED_INSURANCE_POLICE_ID)
            .sumInsuredPerAcre(UPDATED_SUM_INSURED_PER_ACRE)
            .insuranceExtent(UPDATED_INSURANCE_EXTENT)
            .sumAmount(UPDATED_SUM_AMOUNT)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCoveragesInsuranceCultivatedLandUpdatableFieldsEquals(
            partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
                partialUpdatedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            )
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoveragesInsuranceCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCultivatedLandCoveragesInsuranceCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.saveAndFlush(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        restInsuranceCultivatedLandCoveragesInsuranceCultivatedLandMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.count();
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

    protected InsuranceCultivatedLandCoveragesInsuranceCultivatedLand getPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository
            .findById(insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
            .orElseThrow();
    }

    protected void assertPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLandToMatchAllProperties(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand expectedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        assertInsuranceCultivatedLandCoveragesInsuranceCultivatedLandAllPropertiesEquals(
            expectedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
                expectedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            )
        );
    }

    protected void assertPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLandToMatchUpdatableProperties(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand expectedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        assertInsuranceCultivatedLandCoveragesInsuranceCultivatedLandAllUpdatablePropertiesEquals(
            expectedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand,
            getPersistedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
                expectedInsuranceCultivatedLandCoveragesInsuranceCultivatedLand
            )
        );
    }
}
