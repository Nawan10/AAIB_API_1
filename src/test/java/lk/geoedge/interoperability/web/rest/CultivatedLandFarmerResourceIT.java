package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandFarmer;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerRepository;
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
 * Integration tests for the {@link CultivatedLandFarmerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandFarmerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-land-farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandFarmerRepository cultivatedLandFarmerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandFarmerMockMvc;

    private CultivatedLandFarmer cultivatedLandFarmer;

    private CultivatedLandFarmer insertedCultivatedLandFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandFarmer createEntity() {
        return new CultivatedLandFarmer()
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
    public static CultivatedLandFarmer createUpdatedEntity() {
        return new CultivatedLandFarmer()
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
        cultivatedLandFarmer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandFarmer != null) {
            cultivatedLandFarmerRepository.delete(insertedCultivatedLandFarmer);
            insertedCultivatedLandFarmer = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandFarmer
        var returnedCultivatedLandFarmer = om.readValue(
            restCultivatedLandFarmerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandFarmer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandFarmer.class
        );

        // Validate the CultivatedLandFarmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandFarmerUpdatableFieldsEquals(
            returnedCultivatedLandFarmer,
            getPersistedCultivatedLandFarmer(returnedCultivatedLandFarmer)
        );

        insertedCultivatedLandFarmer = returnedCultivatedLandFarmer;
    }

    @Test
    @Transactional
    void createCultivatedLandFarmerWithExistingId() throws Exception {
        // Create the CultivatedLandFarmer with an existing ID
        cultivatedLandFarmer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandFarmerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmers() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList
        restCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmer.getId().intValue())))
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
    void getCultivatedLandFarmer() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get the cultivatedLandFarmer
        restCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandFarmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandFarmer.getId().intValue()))
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
    void getCultivatedLandFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        Long id = cultivatedLandFarmer.getId();

        defaultCultivatedLandFarmerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandFarmerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandFarmerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerId equals to
        defaultCultivatedLandFarmerFiltering("farmerId.equals=" + DEFAULT_FARMER_ID, "farmerId.equals=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerId in
        defaultCultivatedLandFarmerFiltering(
            "farmerId.in=" + DEFAULT_FARMER_ID + "," + UPDATED_FARMER_ID,
            "farmerId.in=" + UPDATED_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerId is not null
        defaultCultivatedLandFarmerFiltering("farmerId.specified=true", "farmerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerId contains
        defaultCultivatedLandFarmerFiltering("farmerId.contains=" + DEFAULT_FARMER_ID, "farmerId.contains=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerId does not contain
        defaultCultivatedLandFarmerFiltering(
            "farmerId.doesNotContain=" + UPDATED_FARMER_ID,
            "farmerId.doesNotContain=" + DEFAULT_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerName equals to
        defaultCultivatedLandFarmerFiltering("farmerName.equals=" + DEFAULT_FARMER_NAME, "farmerName.equals=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerName in
        defaultCultivatedLandFarmerFiltering(
            "farmerName.in=" + DEFAULT_FARMER_NAME + "," + UPDATED_FARMER_NAME,
            "farmerName.in=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerName is not null
        defaultCultivatedLandFarmerFiltering("farmerName.specified=true", "farmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerName contains
        defaultCultivatedLandFarmerFiltering("farmerName.contains=" + DEFAULT_FARMER_NAME, "farmerName.contains=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where farmerName does not contain
        defaultCultivatedLandFarmerFiltering(
            "farmerName.doesNotContain=" + UPDATED_FARMER_NAME,
            "farmerName.doesNotContain=" + DEFAULT_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByNicNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where nicNo equals to
        defaultCultivatedLandFarmerFiltering("nicNo.equals=" + DEFAULT_NIC_NO, "nicNo.equals=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByNicNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where nicNo in
        defaultCultivatedLandFarmerFiltering("nicNo.in=" + DEFAULT_NIC_NO + "," + UPDATED_NIC_NO, "nicNo.in=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByNicNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where nicNo is not null
        defaultCultivatedLandFarmerFiltering("nicNo.specified=true", "nicNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByNicNoContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where nicNo contains
        defaultCultivatedLandFarmerFiltering("nicNo.contains=" + DEFAULT_NIC_NO, "nicNo.contains=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByNicNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where nicNo does not contain
        defaultCultivatedLandFarmerFiltering("nicNo.doesNotContain=" + UPDATED_NIC_NO, "nicNo.doesNotContain=" + DEFAULT_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddressFirstLineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addressFirstLine equals to
        defaultCultivatedLandFarmerFiltering(
            "addressFirstLine.equals=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.equals=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddressFirstLineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addressFirstLine in
        defaultCultivatedLandFarmerFiltering(
            "addressFirstLine.in=" + DEFAULT_ADDRESS_FIRST_LINE + "," + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.in=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddressFirstLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addressFirstLine is not null
        defaultCultivatedLandFarmerFiltering("addressFirstLine.specified=true", "addressFirstLine.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddressFirstLineContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addressFirstLine contains
        defaultCultivatedLandFarmerFiltering(
            "addressFirstLine.contains=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.contains=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddressFirstLineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addressFirstLine does not contain
        defaultCultivatedLandFarmerFiltering(
            "addressFirstLine.doesNotContain=" + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.doesNotContain=" + DEFAULT_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByContactNoEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where contactNoEmail equals to
        defaultCultivatedLandFarmerFiltering(
            "contactNoEmail.equals=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.equals=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByContactNoEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where contactNoEmail in
        defaultCultivatedLandFarmerFiltering(
            "contactNoEmail.in=" + DEFAULT_CONTACT_NO_EMAIL + "," + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.in=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByContactNoEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where contactNoEmail is not null
        defaultCultivatedLandFarmerFiltering("contactNoEmail.specified=true", "contactNoEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByContactNoEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where contactNoEmail contains
        defaultCultivatedLandFarmerFiltering(
            "contactNoEmail.contains=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.contains=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByContactNoEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where contactNoEmail does not contain
        defaultCultivatedLandFarmerFiltering(
            "contactNoEmail.doesNotContain=" + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.doesNotContain=" + DEFAULT_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId equals to
        defaultCultivatedLandFarmerFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId in
        defaultCultivatedLandFarmerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId is not null
        defaultCultivatedLandFarmerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId is greater than or equal to
        defaultCultivatedLandFarmerFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId is less than or equal to
        defaultCultivatedLandFarmerFiltering(
            "provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId is less than
        defaultCultivatedLandFarmerFiltering("provinceId.lessThan=" + UPDATED_PROVINCE_ID, "provinceId.lessThan=" + DEFAULT_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where provinceId is greater than
        defaultCultivatedLandFarmerFiltering(
            "provinceId.greaterThan=" + SMALLER_PROVINCE_ID,
            "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId equals to
        defaultCultivatedLandFarmerFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId in
        defaultCultivatedLandFarmerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId is not null
        defaultCultivatedLandFarmerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId is greater than or equal to
        defaultCultivatedLandFarmerFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId is less than or equal to
        defaultCultivatedLandFarmerFiltering(
            "districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId is less than
        defaultCultivatedLandFarmerFiltering("districtId.lessThan=" + UPDATED_DISTRICT_ID, "districtId.lessThan=" + DEFAULT_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where districtId is greater than
        defaultCultivatedLandFarmerFiltering(
            "districtId.greaterThan=" + SMALLER_DISTRICT_ID,
            "districtId.greaterThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId equals to
        defaultCultivatedLandFarmerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId in
        defaultCultivatedLandFarmerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId is not null
        defaultCultivatedLandFarmerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId is greater than or equal to
        defaultCultivatedLandFarmerFiltering("dsId.greaterThanOrEqual=" + DEFAULT_DS_ID, "dsId.greaterThanOrEqual=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId is less than or equal to
        defaultCultivatedLandFarmerFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId is less than
        defaultCultivatedLandFarmerFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where dsId is greater than
        defaultCultivatedLandFarmerFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId equals to
        defaultCultivatedLandFarmerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId in
        defaultCultivatedLandFarmerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId is not null
        defaultCultivatedLandFarmerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId is greater than or equal to
        defaultCultivatedLandFarmerFiltering("gnId.greaterThanOrEqual=" + DEFAULT_GN_ID, "gnId.greaterThanOrEqual=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId is less than or equal to
        defaultCultivatedLandFarmerFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId is less than
        defaultCultivatedLandFarmerFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where gnId is greater than
        defaultCultivatedLandFarmerFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where city equals to
        defaultCultivatedLandFarmerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where city in
        defaultCultivatedLandFarmerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where city is not null
        defaultCultivatedLandFarmerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where city contains
        defaultCultivatedLandFarmerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where city does not contain
        defaultCultivatedLandFarmerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addedDate equals to
        defaultCultivatedLandFarmerFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addedDate in
        defaultCultivatedLandFarmerFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        // Get all the cultivatedLandFarmerList where addedDate is not null
        defaultCultivatedLandFarmerFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    private void defaultCultivatedLandFarmerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandFarmerShouldBeFound(shouldBeFound);
        defaultCultivatedLandFarmerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandFarmerShouldBeFound(String filter) throws Exception {
        restCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmer.getId().intValue())))
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
        restCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandFarmerShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandFarmer() throws Exception {
        // Get the cultivatedLandFarmer
        restCultivatedLandFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandFarmer() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmer
        CultivatedLandFarmer updatedCultivatedLandFarmer = cultivatedLandFarmerRepository
            .findById(cultivatedLandFarmer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandFarmer are not directly saved in db
        em.detach(updatedCultivatedLandFarmer);
        updatedCultivatedLandFarmer
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

        restCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandFarmer))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandFarmerToMatchAllProperties(updatedCultivatedLandFarmer);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmer using partial update
        CultivatedLandFarmer partialUpdatedCultivatedLandFarmer = new CultivatedLandFarmer();
        partialUpdatedCultivatedLandFarmer.setId(cultivatedLandFarmer.getId());

        partialUpdatedCultivatedLandFarmer
            .farmerId(UPDATED_FARMER_ID)
            .farmerName(UPDATED_FARMER_NAME)
            .nicNo(UPDATED_NIC_NO)
            .addressFirstLine(UPDATED_ADDRESS_FIRST_LINE)
            .contactNoEmail(UPDATED_CONTACT_NO_EMAIL)
            .provinceId(UPDATED_PROVINCE_ID)
            .gnId(UPDATED_GN_ID)
            .city(UPDATED_CITY);

        restCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmer))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandFarmer, cultivatedLandFarmer),
            getPersistedCultivatedLandFarmer(cultivatedLandFarmer)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmer using partial update
        CultivatedLandFarmer partialUpdatedCultivatedLandFarmer = new CultivatedLandFarmer();
        partialUpdatedCultivatedLandFarmer.setId(cultivatedLandFarmer.getId());

        partialUpdatedCultivatedLandFarmer
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

        restCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmer))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmerUpdatableFieldsEquals(
            partialUpdatedCultivatedLandFarmer,
            getPersistedCultivatedLandFarmer(partialUpdatedCultivatedLandFarmer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandFarmer() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmer = cultivatedLandFarmerRepository.saveAndFlush(cultivatedLandFarmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandFarmer
        restCultivatedLandFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandFarmer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandFarmerRepository.count();
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

    protected CultivatedLandFarmer getPersistedCultivatedLandFarmer(CultivatedLandFarmer cultivatedLandFarmer) {
        return cultivatedLandFarmerRepository.findById(cultivatedLandFarmer.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandFarmerToMatchAllProperties(CultivatedLandFarmer expectedCultivatedLandFarmer) {
        assertCultivatedLandFarmerAllPropertiesEquals(
            expectedCultivatedLandFarmer,
            getPersistedCultivatedLandFarmer(expectedCultivatedLandFarmer)
        );
    }

    protected void assertPersistedCultivatedLandFarmerToMatchUpdatableProperties(CultivatedLandFarmer expectedCultivatedLandFarmer) {
        assertCultivatedLandFarmerAllUpdatablePropertiesEquals(
            expectedCultivatedLandFarmer,
            getPersistedCultivatedLandFarmer(expectedCultivatedLandFarmer)
        );
    }
}
