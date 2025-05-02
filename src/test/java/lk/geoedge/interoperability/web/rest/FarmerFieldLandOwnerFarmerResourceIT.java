package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmerAsserts.*;
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
import lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerFarmerRepository;
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
 * Integration tests for the {@link FarmerFieldLandOwnerFarmerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmerFieldLandOwnerFarmerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/farmer-field-land-owner-farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmerFieldLandOwnerFarmerRepository farmerFieldLandOwnerFarmerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmerFieldLandOwnerFarmerMockMvc;

    private FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer;

    private FarmerFieldLandOwnerFarmer insertedFarmerFieldLandOwnerFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FarmerFieldLandOwnerFarmer createEntity() {
        return new FarmerFieldLandOwnerFarmer()
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
    public static FarmerFieldLandOwnerFarmer createUpdatedEntity() {
        return new FarmerFieldLandOwnerFarmer()
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
        farmerFieldLandOwnerFarmer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFarmerFieldLandOwnerFarmer != null) {
            farmerFieldLandOwnerFarmerRepository.delete(insertedFarmerFieldLandOwnerFarmer);
            insertedFarmerFieldLandOwnerFarmer = null;
        }
    }

    @Test
    @Transactional
    void createFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FarmerFieldLandOwnerFarmer
        var returnedFarmerFieldLandOwnerFarmer = om.readValue(
            restFarmerFieldLandOwnerFarmerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FarmerFieldLandOwnerFarmer.class
        );

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFarmerFieldLandOwnerFarmerUpdatableFieldsEquals(
            returnedFarmerFieldLandOwnerFarmer,
            getPersistedFarmerFieldLandOwnerFarmer(returnedFarmerFieldLandOwnerFarmer)
        );

        insertedFarmerFieldLandOwnerFarmer = returnedFarmerFieldLandOwnerFarmer;
    }

    @Test
    @Transactional
    void createFarmerFieldLandOwnerFarmerWithExistingId() throws Exception {
        // Create the FarmerFieldLandOwnerFarmer with an existing ID
        farmerFieldLandOwnerFarmer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmers() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldLandOwnerFarmer.getId().intValue())))
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
    void getFarmerFieldLandOwnerFarmer() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get the farmerFieldLandOwnerFarmer
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, farmerFieldLandOwnerFarmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmerFieldLandOwnerFarmer.getId().intValue()))
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
    void getFarmerFieldLandOwnerFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        Long id = farmerFieldLandOwnerFarmer.getId();

        defaultFarmerFieldLandOwnerFarmerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFarmerFieldLandOwnerFarmerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFarmerFieldLandOwnerFarmerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerId equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("farmerId.equals=" + DEFAULT_FARMER_ID, "farmerId.equals=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerId in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "farmerId.in=" + DEFAULT_FARMER_ID + "," + UPDATED_FARMER_ID,
            "farmerId.in=" + UPDATED_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerId is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("farmerId.specified=true", "farmerId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerId contains
        defaultFarmerFieldLandOwnerFarmerFiltering("farmerId.contains=" + DEFAULT_FARMER_ID, "farmerId.contains=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerId does not contain
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "farmerId.doesNotContain=" + UPDATED_FARMER_ID,
            "farmerId.doesNotContain=" + DEFAULT_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerName equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("farmerName.equals=" + DEFAULT_FARMER_NAME, "farmerName.equals=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerName in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "farmerName.in=" + DEFAULT_FARMER_NAME + "," + UPDATED_FARMER_NAME,
            "farmerName.in=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerName is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("farmerName.specified=true", "farmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerName contains
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "farmerName.contains=" + DEFAULT_FARMER_NAME,
            "farmerName.contains=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByFarmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where farmerName does not contain
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "farmerName.doesNotContain=" + UPDATED_FARMER_NAME,
            "farmerName.doesNotContain=" + DEFAULT_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByNicNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where nicNo equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("nicNo.equals=" + DEFAULT_NIC_NO, "nicNo.equals=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByNicNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where nicNo in
        defaultFarmerFieldLandOwnerFarmerFiltering("nicNo.in=" + DEFAULT_NIC_NO + "," + UPDATED_NIC_NO, "nicNo.in=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByNicNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where nicNo is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("nicNo.specified=true", "nicNo.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByNicNoContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where nicNo contains
        defaultFarmerFieldLandOwnerFarmerFiltering("nicNo.contains=" + DEFAULT_NIC_NO, "nicNo.contains=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByNicNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where nicNo does not contain
        defaultFarmerFieldLandOwnerFarmerFiltering("nicNo.doesNotContain=" + UPDATED_NIC_NO, "nicNo.doesNotContain=" + DEFAULT_NIC_NO);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddressFirstLineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addressFirstLine equals to
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "addressFirstLine.equals=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.equals=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddressFirstLineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addressFirstLine in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "addressFirstLine.in=" + DEFAULT_ADDRESS_FIRST_LINE + "," + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.in=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddressFirstLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addressFirstLine is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("addressFirstLine.specified=true", "addressFirstLine.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddressFirstLineContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addressFirstLine contains
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "addressFirstLine.contains=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.contains=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddressFirstLineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addressFirstLine does not contain
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "addressFirstLine.doesNotContain=" + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.doesNotContain=" + DEFAULT_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByContactNoEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where contactNoEmail equals to
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "contactNoEmail.equals=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.equals=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByContactNoEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where contactNoEmail in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "contactNoEmail.in=" + DEFAULT_CONTACT_NO_EMAIL + "," + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.in=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByContactNoEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where contactNoEmail is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("contactNoEmail.specified=true", "contactNoEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByContactNoEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where contactNoEmail contains
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "contactNoEmail.contains=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.contains=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByContactNoEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where contactNoEmail does not contain
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "contactNoEmail.doesNotContain=" + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.doesNotContain=" + DEFAULT_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId is greater than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId is less than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId is less than
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "provinceId.lessThan=" + UPDATED_PROVINCE_ID,
            "provinceId.lessThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where provinceId is greater than
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "provinceId.greaterThan=" + SMALLER_PROVINCE_ID,
            "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId is greater than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId is less than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId is less than
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "districtId.lessThan=" + UPDATED_DISTRICT_ID,
            "districtId.lessThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where districtId is greater than
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "districtId.greaterThan=" + SMALLER_DISTRICT_ID,
            "districtId.greaterThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId in
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId is greater than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.greaterThanOrEqual=" + DEFAULT_DS_ID, "dsId.greaterThanOrEqual=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId is less than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId is less than
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where dsId is greater than
        defaultFarmerFieldLandOwnerFarmerFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId in
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId is greater than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.greaterThanOrEqual=" + DEFAULT_GN_ID, "gnId.greaterThanOrEqual=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId is less than or equal to
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId is less than
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where gnId is greater than
        defaultFarmerFieldLandOwnerFarmerFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where city equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where city in
        defaultFarmerFieldLandOwnerFarmerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where city is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where city contains
        defaultFarmerFieldLandOwnerFarmerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where city does not contain
        defaultFarmerFieldLandOwnerFarmerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addedDate equals to
        defaultFarmerFieldLandOwnerFarmerFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addedDate in
        defaultFarmerFieldLandOwnerFarmerFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnerFarmersByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        // Get all the farmerFieldLandOwnerFarmerList where addedDate is not null
        defaultFarmerFieldLandOwnerFarmerFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    private void defaultFarmerFieldLandOwnerFarmerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFarmerFieldLandOwnerFarmerShouldBeFound(shouldBeFound);
        defaultFarmerFieldLandOwnerFarmerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmerFieldLandOwnerFarmerShouldBeFound(String filter) throws Exception {
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldLandOwnerFarmer.getId().intValue())))
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
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmerFieldLandOwnerFarmerShouldNotBeFound(String filter) throws Exception {
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmerFieldLandOwnerFarmer() throws Exception {
        // Get the farmerFieldLandOwnerFarmer
        restFarmerFieldLandOwnerFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmerFieldLandOwnerFarmer() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldLandOwnerFarmer
        FarmerFieldLandOwnerFarmer updatedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository
            .findById(farmerFieldLandOwnerFarmer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedFarmerFieldLandOwnerFarmer are not directly saved in db
        em.detach(updatedFarmerFieldLandOwnerFarmer);
        updatedFarmerFieldLandOwnerFarmer
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

        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFarmerFieldLandOwnerFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFarmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmerFieldLandOwnerFarmerToMatchAllProperties(updatedFarmerFieldLandOwnerFarmer);
    }

    @Test
    @Transactional
    void putNonExistingFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwnerFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmerFieldLandOwnerFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwnerFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwnerFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmerFieldLandOwnerFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldLandOwnerFarmer using partial update
        FarmerFieldLandOwnerFarmer partialUpdatedFarmerFieldLandOwnerFarmer = new FarmerFieldLandOwnerFarmer();
        partialUpdatedFarmerFieldLandOwnerFarmer.setId(farmerFieldLandOwnerFarmer.getId());

        partialUpdatedFarmerFieldLandOwnerFarmer
            .addressFirstLine(UPDATED_ADDRESS_FIRST_LINE)
            .provinceId(UPDATED_PROVINCE_ID)
            .city(UPDATED_CITY);

        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldLandOwnerFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldLandOwnerFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldLandOwnerFarmerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFarmerFieldLandOwnerFarmer, farmerFieldLandOwnerFarmer),
            getPersistedFarmerFieldLandOwnerFarmer(farmerFieldLandOwnerFarmer)
        );
    }

    @Test
    @Transactional
    void fullUpdateFarmerFieldLandOwnerFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldLandOwnerFarmer using partial update
        FarmerFieldLandOwnerFarmer partialUpdatedFarmerFieldLandOwnerFarmer = new FarmerFieldLandOwnerFarmer();
        partialUpdatedFarmerFieldLandOwnerFarmer.setId(farmerFieldLandOwnerFarmer.getId());

        partialUpdatedFarmerFieldLandOwnerFarmer
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

        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldLandOwnerFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldLandOwnerFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldLandOwnerFarmerUpdatableFieldsEquals(
            partialUpdatedFarmerFieldLandOwnerFarmer,
            getPersistedFarmerFieldLandOwnerFarmer(partialUpdatedFarmerFieldLandOwnerFarmer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwnerFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmerFieldLandOwnerFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwnerFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmerFieldLandOwnerFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwnerFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldLandOwnerFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldLandOwnerFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmerFieldLandOwnerFarmer() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerRepository.saveAndFlush(farmerFieldLandOwnerFarmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmerFieldLandOwnerFarmer
        restFarmerFieldLandOwnerFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmerFieldLandOwnerFarmer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmerFieldLandOwnerFarmerRepository.count();
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

    protected FarmerFieldLandOwnerFarmer getPersistedFarmerFieldLandOwnerFarmer(FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer) {
        return farmerFieldLandOwnerFarmerRepository.findById(farmerFieldLandOwnerFarmer.getId()).orElseThrow();
    }

    protected void assertPersistedFarmerFieldLandOwnerFarmerToMatchAllProperties(
        FarmerFieldLandOwnerFarmer expectedFarmerFieldLandOwnerFarmer
    ) {
        assertFarmerFieldLandOwnerFarmerAllPropertiesEquals(
            expectedFarmerFieldLandOwnerFarmer,
            getPersistedFarmerFieldLandOwnerFarmer(expectedFarmerFieldLandOwnerFarmer)
        );
    }

    protected void assertPersistedFarmerFieldLandOwnerFarmerToMatchUpdatableProperties(
        FarmerFieldLandOwnerFarmer expectedFarmerFieldLandOwnerFarmer
    ) {
        assertFarmerFieldLandOwnerFarmerAllUpdatablePropertiesEquals(
            expectedFarmerFieldLandOwnerFarmer,
            getPersistedFarmerFieldLandOwnerFarmer(expectedFarmerFieldLandOwnerFarmer)
        );
    }
}
