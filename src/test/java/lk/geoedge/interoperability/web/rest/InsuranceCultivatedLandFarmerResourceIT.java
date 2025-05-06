package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmerAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandFarmerRepository;
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
 * Integration tests for the {@link InsuranceCultivatedLandFarmerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCultivatedLandFarmerResourceIT {

    private static final String DEFAULT_FARMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_FARMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FARMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FARMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIC_NO = "AAAAAAAAAA";
    private static final String UPDATED_NIC_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_FIRST_LINE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_FIRST_LINE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NO_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_PROVINCE_ID = 1;
    private static final Integer UPDATED_PROVINCE_ID = 2;
    private static final Integer SMALLER_PROVINCE_ID = 1 - 1;

    private static final Integer DEFAULT_DISTRICT_ID = 1;
    private static final Integer UPDATED_DISTRICT_ID = 2;
    private static final Integer SMALLER_DISTRICT_ID = 1 - 1;

    private static final Integer DEFAULT_DS_ID = 1;
    private static final Integer UPDATED_DS_ID = 2;
    private static final Integer SMALLER_DS_ID = 1 - 1;

    private static final Integer DEFAULT_GN_ID = 1;
    private static final Integer UPDATED_GN_ID = 2;
    private static final Integer SMALLER_GN_ID = 1 - 1;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Instant DEFAULT_ADDED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/insurance-cultivated-land-farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCultivatedLandFarmerMockMvc;

    private InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer;

    private InsuranceCultivatedLandFarmer insertedInsuranceCultivatedLandFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandFarmer createEntity() {
        return new InsuranceCultivatedLandFarmer()
            .farmerId(DEFAULT_FARMER_ID)
            .farmerName(DEFAULT_FARMER_NAME)
            .nicNo(DEFAULT_NIC_NO)
            .addressFirstLine(DEFAULT_ADDRESS_FIRST_LINE)
            .contactNoEmail(DEFAULT_CONTACT_NO_EMAIL)
            .provinceId(DEFAULT_PROVINCE_ID)
            .districtId(DEFAULT_DISTRICT_ID)
            .dsId(DEFAULT_DS_ID)
            .gnId(DEFAULT_GN_ID)
            .city(DEFAULT_CITY)
            .addedDate(DEFAULT_ADDED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandFarmer createUpdatedEntity() {
        return new InsuranceCultivatedLandFarmer()
            .farmerId(UPDATED_FARMER_ID)
            .farmerName(UPDATED_FARMER_NAME)
            .nicNo(UPDATED_NIC_NO)
            .addressFirstLine(UPDATED_ADDRESS_FIRST_LINE)
            .contactNoEmail(UPDATED_CONTACT_NO_EMAIL)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .city(UPDATED_CITY)
            .addedDate(UPDATED_ADDED_DATE);
    }

    @BeforeEach
    void initTest() {
        insuranceCultivatedLandFarmer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCultivatedLandFarmer != null) {
            insuranceCultivatedLandFarmerRepository.delete(insertedInsuranceCultivatedLandFarmer);
            insertedInsuranceCultivatedLandFarmer = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCultivatedLandFarmer
        var returnedInsuranceCultivatedLandFarmer = om.readValue(
            restInsuranceCultivatedLandFarmerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCultivatedLandFarmer.class
        );

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCultivatedLandFarmerUpdatableFieldsEquals(
            returnedInsuranceCultivatedLandFarmer,
            getPersistedInsuranceCultivatedLandFarmer(returnedInsuranceCultivatedLandFarmer)
        );

        insertedInsuranceCultivatedLandFarmer = returnedInsuranceCultivatedLandFarmer;
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandFarmerWithExistingId() throws Exception {
        // Create the InsuranceCultivatedLandFarmer with an existing ID
        insuranceCultivatedLandFarmer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmers() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandFarmer.getId().intValue())))
            .andExpect(jsonPath("$.[*].farmerId").value(hasItem(DEFAULT_FARMER_ID)))
            .andExpect(jsonPath("$.[*].farmerName").value(hasItem(DEFAULT_FARMER_NAME)))
            .andExpect(jsonPath("$.[*].nicNo").value(hasItem(DEFAULT_NIC_NO)))
            .andExpect(jsonPath("$.[*].addressFirstLine").value(hasItem(DEFAULT_ADDRESS_FIRST_LINE)))
            .andExpect(jsonPath("$.[*].contactNoEmail").value(hasItem(DEFAULT_CONTACT_NO_EMAIL)))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].dsId").value(hasItem(DEFAULT_DS_ID)))
            .andExpect(jsonPath("$.[*].gnId").value(hasItem(DEFAULT_GN_ID)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandFarmer() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get the insuranceCultivatedLandFarmer
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCultivatedLandFarmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCultivatedLandFarmer.getId().intValue()))
            .andExpect(jsonPath("$.farmerId").value(DEFAULT_FARMER_ID))
            .andExpect(jsonPath("$.farmerName").value(DEFAULT_FARMER_NAME))
            .andExpect(jsonPath("$.nicNo").value(DEFAULT_NIC_NO))
            .andExpect(jsonPath("$.addressFirstLine").value(DEFAULT_ADDRESS_FIRST_LINE))
            .andExpect(jsonPath("$.contactNoEmail").value(DEFAULT_CONTACT_NO_EMAIL))
            .andExpect(jsonPath("$.provinceId").value(DEFAULT_PROVINCE_ID))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID))
            .andExpect(jsonPath("$.dsId").value(DEFAULT_DS_ID))
            .andExpect(jsonPath("$.gnId").value(DEFAULT_GN_ID))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        Long id = insuranceCultivatedLandFarmer.getId();

        defaultInsuranceCultivatedLandFarmerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCultivatedLandFarmerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCultivatedLandFarmerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerId equals to
        defaultInsuranceCultivatedLandFarmerFiltering("farmerId.equals=" + DEFAULT_FARMER_ID, "farmerId.equals=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerId in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "farmerId.in=" + DEFAULT_FARMER_ID + "," + UPDATED_FARMER_ID,
            "farmerId.in=" + UPDATED_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerId is not null
        defaultInsuranceCultivatedLandFarmerFiltering("farmerId.specified=true", "farmerId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerId contains
        defaultInsuranceCultivatedLandFarmerFiltering("farmerId.contains=" + DEFAULT_FARMER_ID, "farmerId.contains=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerId does not contain
        defaultInsuranceCultivatedLandFarmerFiltering(
            "farmerId.doesNotContain=" + UPDATED_FARMER_ID,
            "farmerId.doesNotContain=" + DEFAULT_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerName equals to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "farmerName.equals=" + DEFAULT_FARMER_NAME,
            "farmerName.equals=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerName in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "farmerName.in=" + DEFAULT_FARMER_NAME + "," + UPDATED_FARMER_NAME,
            "farmerName.in=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerName is not null
        defaultInsuranceCultivatedLandFarmerFiltering("farmerName.specified=true", "farmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerName contains
        defaultInsuranceCultivatedLandFarmerFiltering(
            "farmerName.contains=" + DEFAULT_FARMER_NAME,
            "farmerName.contains=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByFarmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where farmerName does not contain
        defaultInsuranceCultivatedLandFarmerFiltering(
            "farmerName.doesNotContain=" + UPDATED_FARMER_NAME,
            "farmerName.doesNotContain=" + DEFAULT_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByNicNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where nicNo equals to
        defaultInsuranceCultivatedLandFarmerFiltering("nicNo.equals=" + DEFAULT_NIC_NO, "nicNo.equals=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByNicNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where nicNo in
        defaultInsuranceCultivatedLandFarmerFiltering("nicNo.in=" + DEFAULT_NIC_NO + "," + UPDATED_NIC_NO, "nicNo.in=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByNicNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where nicNo is not null
        defaultInsuranceCultivatedLandFarmerFiltering("nicNo.specified=true", "nicNo.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByNicNoContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where nicNo contains
        defaultInsuranceCultivatedLandFarmerFiltering("nicNo.contains=" + DEFAULT_NIC_NO, "nicNo.contains=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByNicNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where nicNo does not contain
        defaultInsuranceCultivatedLandFarmerFiltering("nicNo.doesNotContain=" + UPDATED_NIC_NO, "nicNo.doesNotContain=" + DEFAULT_NIC_NO);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddressFirstLineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addressFirstLine equals to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "addressFirstLine.equals=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.equals=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddressFirstLineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addressFirstLine in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "addressFirstLine.in=" + DEFAULT_ADDRESS_FIRST_LINE + "," + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.in=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddressFirstLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addressFirstLine is not null
        defaultInsuranceCultivatedLandFarmerFiltering("addressFirstLine.specified=true", "addressFirstLine.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddressFirstLineContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addressFirstLine contains
        defaultInsuranceCultivatedLandFarmerFiltering(
            "addressFirstLine.contains=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.contains=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddressFirstLineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addressFirstLine does not contain
        defaultInsuranceCultivatedLandFarmerFiltering(
            "addressFirstLine.doesNotContain=" + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.doesNotContain=" + DEFAULT_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByContactNoEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where contactNoEmail equals to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "contactNoEmail.equals=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.equals=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByContactNoEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where contactNoEmail in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "contactNoEmail.in=" + DEFAULT_CONTACT_NO_EMAIL + "," + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.in=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByContactNoEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where contactNoEmail is not null
        defaultInsuranceCultivatedLandFarmerFiltering("contactNoEmail.specified=true", "contactNoEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByContactNoEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where contactNoEmail contains
        defaultInsuranceCultivatedLandFarmerFiltering(
            "contactNoEmail.contains=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.contains=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByContactNoEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where contactNoEmail does not contain
        defaultInsuranceCultivatedLandFarmerFiltering(
            "contactNoEmail.doesNotContain=" + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.doesNotContain=" + DEFAULT_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId equals to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "provinceId.equals=" + DEFAULT_PROVINCE_ID,
            "provinceId.equals=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId is not null
        defaultInsuranceCultivatedLandFarmerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId is greater than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId is less than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId is less than
        defaultInsuranceCultivatedLandFarmerFiltering(
            "provinceId.lessThan=" + UPDATED_PROVINCE_ID,
            "provinceId.lessThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where provinceId is greater than
        defaultInsuranceCultivatedLandFarmerFiltering(
            "provinceId.greaterThan=" + SMALLER_PROVINCE_ID,
            "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId equals to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "districtId.equals=" + DEFAULT_DISTRICT_ID,
            "districtId.equals=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId is not null
        defaultInsuranceCultivatedLandFarmerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId is greater than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId is less than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId is less than
        defaultInsuranceCultivatedLandFarmerFiltering(
            "districtId.lessThan=" + UPDATED_DISTRICT_ID,
            "districtId.lessThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where districtId is greater than
        defaultInsuranceCultivatedLandFarmerFiltering(
            "districtId.greaterThan=" + SMALLER_DISTRICT_ID,
            "districtId.greaterThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId equals to
        defaultInsuranceCultivatedLandFarmerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId in
        defaultInsuranceCultivatedLandFarmerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId is not null
        defaultInsuranceCultivatedLandFarmerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId is greater than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "dsId.greaterThanOrEqual=" + DEFAULT_DS_ID,
            "dsId.greaterThanOrEqual=" + UPDATED_DS_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId is less than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId is less than
        defaultInsuranceCultivatedLandFarmerFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where dsId is greater than
        defaultInsuranceCultivatedLandFarmerFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId equals to
        defaultInsuranceCultivatedLandFarmerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId in
        defaultInsuranceCultivatedLandFarmerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId is not null
        defaultInsuranceCultivatedLandFarmerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId is greater than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering(
            "gnId.greaterThanOrEqual=" + DEFAULT_GN_ID,
            "gnId.greaterThanOrEqual=" + UPDATED_GN_ID
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId is less than or equal to
        defaultInsuranceCultivatedLandFarmerFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId is less than
        defaultInsuranceCultivatedLandFarmerFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where gnId is greater than
        defaultInsuranceCultivatedLandFarmerFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where city equals to
        defaultInsuranceCultivatedLandFarmerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where city in
        defaultInsuranceCultivatedLandFarmerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where city is not null
        defaultInsuranceCultivatedLandFarmerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where city contains
        defaultInsuranceCultivatedLandFarmerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where city does not contain
        defaultInsuranceCultivatedLandFarmerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addedDate equals to
        defaultInsuranceCultivatedLandFarmerFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addedDate in
        defaultInsuranceCultivatedLandFarmerFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandFarmersByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        // Get all the insuranceCultivatedLandFarmerList where addedDate is not null
        defaultInsuranceCultivatedLandFarmerFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    private void defaultInsuranceCultivatedLandFarmerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCultivatedLandFarmerShouldBeFound(shouldBeFound);
        defaultInsuranceCultivatedLandFarmerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCultivatedLandFarmerShouldBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandFarmer.getId().intValue())))
            .andExpect(jsonPath("$.[*].farmerId").value(hasItem(DEFAULT_FARMER_ID)))
            .andExpect(jsonPath("$.[*].farmerName").value(hasItem(DEFAULT_FARMER_NAME)))
            .andExpect(jsonPath("$.[*].nicNo").value(hasItem(DEFAULT_NIC_NO)))
            .andExpect(jsonPath("$.[*].addressFirstLine").value(hasItem(DEFAULT_ADDRESS_FIRST_LINE)))
            .andExpect(jsonPath("$.[*].contactNoEmail").value(hasItem(DEFAULT_CONTACT_NO_EMAIL)))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].dsId").value(hasItem(DEFAULT_DS_ID)))
            .andExpect(jsonPath("$.[*].gnId").value(hasItem(DEFAULT_GN_ID)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));

        // Check, that the count call also returns 1
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCultivatedLandFarmerShouldNotBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCultivatedLandFarmer() throws Exception {
        // Get the insuranceCultivatedLandFarmer
        restInsuranceCultivatedLandFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCultivatedLandFarmer() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandFarmer
        InsuranceCultivatedLandFarmer updatedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository
            .findById(insuranceCultivatedLandFarmer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCultivatedLandFarmer are not directly saved in db
        em.detach(updatedInsuranceCultivatedLandFarmer);
        updatedInsuranceCultivatedLandFarmer
            .farmerId(UPDATED_FARMER_ID)
            .farmerName(UPDATED_FARMER_NAME)
            .nicNo(UPDATED_NIC_NO)
            .addressFirstLine(UPDATED_ADDRESS_FIRST_LINE)
            .contactNoEmail(UPDATED_CONTACT_NO_EMAIL)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .city(UPDATED_CITY)
            .addedDate(UPDATED_ADDED_DATE);

        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCultivatedLandFarmer))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCultivatedLandFarmerToMatchAllProperties(updatedInsuranceCultivatedLandFarmer);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCultivatedLandFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandFarmer using partial update
        InsuranceCultivatedLandFarmer partialUpdatedInsuranceCultivatedLandFarmer = new InsuranceCultivatedLandFarmer();
        partialUpdatedInsuranceCultivatedLandFarmer.setId(insuranceCultivatedLandFarmer.getId());

        partialUpdatedInsuranceCultivatedLandFarmer
            .farmerId(UPDATED_FARMER_ID)
            .dsId(UPDATED_DS_ID)
            .city(UPDATED_CITY)
            .addedDate(UPDATED_ADDED_DATE);

        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandFarmer))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandFarmerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCultivatedLandFarmer, insuranceCultivatedLandFarmer),
            getPersistedInsuranceCultivatedLandFarmer(insuranceCultivatedLandFarmer)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCultivatedLandFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandFarmer using partial update
        InsuranceCultivatedLandFarmer partialUpdatedInsuranceCultivatedLandFarmer = new InsuranceCultivatedLandFarmer();
        partialUpdatedInsuranceCultivatedLandFarmer.setId(insuranceCultivatedLandFarmer.getId());

        partialUpdatedInsuranceCultivatedLandFarmer
            .farmerId(UPDATED_FARMER_ID)
            .farmerName(UPDATED_FARMER_NAME)
            .nicNo(UPDATED_NIC_NO)
            .addressFirstLine(UPDATED_ADDRESS_FIRST_LINE)
            .contactNoEmail(UPDATED_CONTACT_NO_EMAIL)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .city(UPDATED_CITY)
            .addedDate(UPDATED_ADDED_DATE);

        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandFarmer))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandFarmerUpdatableFieldsEquals(
            partialUpdatedInsuranceCultivatedLandFarmer,
            getPersistedInsuranceCultivatedLandFarmer(partialUpdatedInsuranceCultivatedLandFarmer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCultivatedLandFarmer() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerRepository.saveAndFlush(insuranceCultivatedLandFarmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCultivatedLandFarmer
        restInsuranceCultivatedLandFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceCultivatedLandFarmer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCultivatedLandFarmerRepository.count();
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

    protected InsuranceCultivatedLandFarmer getPersistedInsuranceCultivatedLandFarmer(
        InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer
    ) {
        return insuranceCultivatedLandFarmerRepository.findById(insuranceCultivatedLandFarmer.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCultivatedLandFarmerToMatchAllProperties(
        InsuranceCultivatedLandFarmer expectedInsuranceCultivatedLandFarmer
    ) {
        assertInsuranceCultivatedLandFarmerAllPropertiesEquals(
            expectedInsuranceCultivatedLandFarmer,
            getPersistedInsuranceCultivatedLandFarmer(expectedInsuranceCultivatedLandFarmer)
        );
    }

    protected void assertPersistedInsuranceCultivatedLandFarmerToMatchUpdatableProperties(
        InsuranceCultivatedLandFarmer expectedInsuranceCultivatedLandFarmer
    ) {
        assertInsuranceCultivatedLandFarmerAllUpdatablePropertiesEquals(
            expectedInsuranceCultivatedLandFarmer,
            getPersistedInsuranceCultivatedLandFarmer(expectedInsuranceCultivatedLandFarmer)
        );
    }
}
