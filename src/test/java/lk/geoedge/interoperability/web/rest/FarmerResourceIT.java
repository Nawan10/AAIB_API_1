package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.FarmerAsserts.*;
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
import lk.geoedge.interoperability.domain.Farmer;
import lk.geoedge.interoperability.repository.FarmerRepository;
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
 * Integration tests for the {@link FarmerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmerMockMvc;

    private Farmer farmer;

    private Farmer insertedFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmer createEntity() {
        return new Farmer()
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
    public static Farmer createUpdatedEntity() {
        return new Farmer()
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
        farmer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFarmer != null) {
            farmerRepository.delete(insertedFarmer);
            insertedFarmer = null;
        }
    }

    @Test
    @Transactional
    void createFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Farmer
        var returnedFarmer = om.readValue(
            restFarmerMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Farmer.class
        );

        // Validate the Farmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFarmerUpdatableFieldsEquals(returnedFarmer, getPersistedFarmer(returnedFarmer));

        insertedFarmer = returnedFarmer;
    }

    @Test
    @Transactional
    void createFarmerWithExistingId() throws Exception {
        // Create the Farmer with an existing ID
        farmer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmerMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmer)))
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFarmers() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmer.getId().intValue())))
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
    void getFarmer() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get the farmer
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, farmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmer.getId().intValue()))
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
    void getFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        Long id = farmer.getId();

        defaultFarmerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFarmerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFarmerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerId equals to
        defaultFarmerFiltering("farmerId.equals=" + DEFAULT_FARMER_ID, "farmerId.equals=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerId in
        defaultFarmerFiltering("farmerId.in=" + DEFAULT_FARMER_ID + "," + UPDATED_FARMER_ID, "farmerId.in=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerId is not null
        defaultFarmerFiltering("farmerId.specified=true", "farmerId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerId contains
        defaultFarmerFiltering("farmerId.contains=" + DEFAULT_FARMER_ID, "farmerId.contains=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerId does not contain
        defaultFarmerFiltering("farmerId.doesNotContain=" + UPDATED_FARMER_ID, "farmerId.doesNotContain=" + DEFAULT_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerName equals to
        defaultFarmerFiltering("farmerName.equals=" + DEFAULT_FARMER_NAME, "farmerName.equals=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerName in
        defaultFarmerFiltering("farmerName.in=" + DEFAULT_FARMER_NAME + "," + UPDATED_FARMER_NAME, "farmerName.in=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerName is not null
        defaultFarmerFiltering("farmerName.specified=true", "farmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerName contains
        defaultFarmerFiltering("farmerName.contains=" + DEFAULT_FARMER_NAME, "farmerName.contains=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllFarmersByFarmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where farmerName does not contain
        defaultFarmerFiltering("farmerName.doesNotContain=" + UPDATED_FARMER_NAME, "farmerName.doesNotContain=" + DEFAULT_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllFarmersByNicNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where nicNo equals to
        defaultFarmerFiltering("nicNo.equals=" + DEFAULT_NIC_NO, "nicNo.equals=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmersByNicNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where nicNo in
        defaultFarmerFiltering("nicNo.in=" + DEFAULT_NIC_NO + "," + UPDATED_NIC_NO, "nicNo.in=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmersByNicNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where nicNo is not null
        defaultFarmerFiltering("nicNo.specified=true", "nicNo.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByNicNoContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where nicNo contains
        defaultFarmerFiltering("nicNo.contains=" + DEFAULT_NIC_NO, "nicNo.contains=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmersByNicNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where nicNo does not contain
        defaultFarmerFiltering("nicNo.doesNotContain=" + UPDATED_NIC_NO, "nicNo.doesNotContain=" + DEFAULT_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmersByAddressFirstLineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addressFirstLine equals to
        defaultFarmerFiltering(
            "addressFirstLine.equals=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.equals=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmersByAddressFirstLineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addressFirstLine in
        defaultFarmerFiltering(
            "addressFirstLine.in=" + DEFAULT_ADDRESS_FIRST_LINE + "," + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.in=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmersByAddressFirstLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addressFirstLine is not null
        defaultFarmerFiltering("addressFirstLine.specified=true", "addressFirstLine.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByAddressFirstLineContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addressFirstLine contains
        defaultFarmerFiltering(
            "addressFirstLine.contains=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.contains=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmersByAddressFirstLineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addressFirstLine does not contain
        defaultFarmerFiltering(
            "addressFirstLine.doesNotContain=" + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.doesNotContain=" + DEFAULT_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmersByContactNoEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where contactNoEmail equals to
        defaultFarmerFiltering("contactNoEmail.equals=" + DEFAULT_CONTACT_NO_EMAIL, "contactNoEmail.equals=" + UPDATED_CONTACT_NO_EMAIL);
    }

    @Test
    @Transactional
    void getAllFarmersByContactNoEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where contactNoEmail in
        defaultFarmerFiltering(
            "contactNoEmail.in=" + DEFAULT_CONTACT_NO_EMAIL + "," + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.in=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmersByContactNoEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where contactNoEmail is not null
        defaultFarmerFiltering("contactNoEmail.specified=true", "contactNoEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByContactNoEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where contactNoEmail contains
        defaultFarmerFiltering(
            "contactNoEmail.contains=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.contains=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmersByContactNoEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where contactNoEmail does not contain
        defaultFarmerFiltering(
            "contactNoEmail.doesNotContain=" + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.doesNotContain=" + DEFAULT_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId equals to
        defaultFarmerFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId in
        defaultFarmerFiltering("provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID, "provinceId.in=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId is not null
        defaultFarmerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId is greater than or equal to
        defaultFarmerFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId is less than or equal to
        defaultFarmerFiltering("provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID, "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId is less than
        defaultFarmerFiltering("provinceId.lessThan=" + UPDATED_PROVINCE_ID, "provinceId.lessThan=" + DEFAULT_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where provinceId is greater than
        defaultFarmerFiltering("provinceId.greaterThan=" + SMALLER_PROVINCE_ID, "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId equals to
        defaultFarmerFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId in
        defaultFarmerFiltering("districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID, "districtId.in=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId is not null
        defaultFarmerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId is greater than or equal to
        defaultFarmerFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId is less than or equal to
        defaultFarmerFiltering("districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID, "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId is less than
        defaultFarmerFiltering("districtId.lessThan=" + UPDATED_DISTRICT_ID, "districtId.lessThan=" + DEFAULT_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where districtId is greater than
        defaultFarmerFiltering("districtId.greaterThan=" + SMALLER_DISTRICT_ID, "districtId.greaterThan=" + DEFAULT_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId equals to
        defaultFarmerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId in
        defaultFarmerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId is not null
        defaultFarmerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId is greater than or equal to
        defaultFarmerFiltering("dsId.greaterThanOrEqual=" + DEFAULT_DS_ID, "dsId.greaterThanOrEqual=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId is less than or equal to
        defaultFarmerFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId is less than
        defaultFarmerFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where dsId is greater than
        defaultFarmerFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId equals to
        defaultFarmerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId in
        defaultFarmerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId is not null
        defaultFarmerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId is greater than or equal to
        defaultFarmerFiltering("gnId.greaterThanOrEqual=" + DEFAULT_GN_ID, "gnId.greaterThanOrEqual=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId is less than or equal to
        defaultFarmerFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId is less than
        defaultFarmerFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where gnId is greater than
        defaultFarmerFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where city equals to
        defaultFarmerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllFarmersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where city in
        defaultFarmerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllFarmersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where city is not null
        defaultFarmerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where city contains
        defaultFarmerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllFarmersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where city does not contain
        defaultFarmerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllFarmersByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addedDate equals to
        defaultFarmerFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllFarmersByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addedDate in
        defaultFarmerFiltering("addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE, "addedDate.in=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllFarmersByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        // Get all the farmerList where addedDate is not null
        defaultFarmerFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    private void defaultFarmerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFarmerShouldBeFound(shouldBeFound);
        defaultFarmerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmerShouldBeFound(String filter) throws Exception {
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmer.getId().intValue())))
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
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmerShouldNotBeFound(String filter) throws Exception {
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmer() throws Exception {
        // Get the farmer
        restFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmer() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmer
        Farmer updatedFarmer = farmerRepository.findById(farmer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFarmer are not directly saved in db
        em.detach(updatedFarmer);
        updatedFarmer
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

        restFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFarmer))
            )
            .andExpect(status().isOk());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmerToMatchAllProperties(updatedFarmer);
    }

    @Test
    @Transactional
    void putNonExistingFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmer using partial update
        Farmer partialUpdatedFarmer = new Farmer();
        partialUpdatedFarmer.setId(farmer.getId());

        partialUpdatedFarmer
            .contactNoEmail(UPDATED_CONTACT_NO_EMAIL)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID);

        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmer))
            )
            .andExpect(status().isOk());

        // Validate the Farmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFarmer, farmer), getPersistedFarmer(farmer));
    }

    @Test
    @Transactional
    void fullUpdateFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmer using partial update
        Farmer partialUpdatedFarmer = new Farmer();
        partialUpdatedFarmer.setId(farmer.getId());

        partialUpdatedFarmer
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

        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmer))
            )
            .andExpect(status().isOk());

        // Validate the Farmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerUpdatableFieldsEquals(partialUpdatedFarmer, getPersistedFarmer(partialUpdatedFarmer));
    }

    @Test
    @Transactional
    void patchNonExistingFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(farmer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmer() throws Exception {
        // Initialize the database
        insertedFarmer = farmerRepository.saveAndFlush(farmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmer
        restFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmerRepository.count();
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

    protected Farmer getPersistedFarmer(Farmer farmer) {
        return farmerRepository.findById(farmer.getId()).orElseThrow();
    }

    protected void assertPersistedFarmerToMatchAllProperties(Farmer expectedFarmer) {
        assertFarmerAllPropertiesEquals(expectedFarmer, getPersistedFarmer(expectedFarmer));
    }

    protected void assertPersistedFarmerToMatchUpdatableProperties(Farmer expectedFarmer) {
        assertFarmerAllUpdatablePropertiesEquals(expectedFarmer, getPersistedFarmer(expectedFarmer));
    }
}
