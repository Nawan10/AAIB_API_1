package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmerAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersFarmerRepository;
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
 * Integration tests for the {@link CultivatedLandFarmersFarmerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandFarmersFarmerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-land-farmers-farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandFarmersFarmerMockMvc;

    private CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer;

    private CultivatedLandFarmersFarmer insertedCultivatedLandFarmersFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandFarmersFarmer createEntity() {
        return new CultivatedLandFarmersFarmer()
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
    public static CultivatedLandFarmersFarmer createUpdatedEntity() {
        return new CultivatedLandFarmersFarmer()
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
        cultivatedLandFarmersFarmer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandFarmersFarmer != null) {
            cultivatedLandFarmersFarmerRepository.delete(insertedCultivatedLandFarmersFarmer);
            insertedCultivatedLandFarmersFarmer = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandFarmersFarmer
        var returnedCultivatedLandFarmersFarmer = om.readValue(
            restCultivatedLandFarmersFarmerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandFarmersFarmer.class
        );

        // Validate the CultivatedLandFarmersFarmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandFarmersFarmerUpdatableFieldsEquals(
            returnedCultivatedLandFarmersFarmer,
            getPersistedCultivatedLandFarmersFarmer(returnedCultivatedLandFarmersFarmer)
        );

        insertedCultivatedLandFarmersFarmer = returnedCultivatedLandFarmersFarmer;
    }

    @Test
    @Transactional
    void createCultivatedLandFarmersFarmerWithExistingId() throws Exception {
        // Create the CultivatedLandFarmersFarmer with an existing ID
        cultivatedLandFarmersFarmer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmers() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList
        restCultivatedLandFarmersFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmersFarmer.getId().intValue())))
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
    void getCultivatedLandFarmersFarmer() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get the cultivatedLandFarmersFarmer
        restCultivatedLandFarmersFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandFarmersFarmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandFarmersFarmer.getId().intValue()))
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
    void getCultivatedLandFarmersFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        Long id = cultivatedLandFarmersFarmer.getId();

        defaultCultivatedLandFarmersFarmerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandFarmersFarmerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandFarmersFarmerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerId equals to
        defaultCultivatedLandFarmersFarmerFiltering("farmerId.equals=" + DEFAULT_FARMER_ID, "farmerId.equals=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerId in
        defaultCultivatedLandFarmersFarmerFiltering(
            "farmerId.in=" + DEFAULT_FARMER_ID + "," + UPDATED_FARMER_ID,
            "farmerId.in=" + UPDATED_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerId is not null
        defaultCultivatedLandFarmersFarmerFiltering("farmerId.specified=true", "farmerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerId contains
        defaultCultivatedLandFarmersFarmerFiltering("farmerId.contains=" + DEFAULT_FARMER_ID, "farmerId.contains=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerId does not contain
        defaultCultivatedLandFarmersFarmerFiltering(
            "farmerId.doesNotContain=" + UPDATED_FARMER_ID,
            "farmerId.doesNotContain=" + DEFAULT_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerName equals to
        defaultCultivatedLandFarmersFarmerFiltering("farmerName.equals=" + DEFAULT_FARMER_NAME, "farmerName.equals=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerName in
        defaultCultivatedLandFarmersFarmerFiltering(
            "farmerName.in=" + DEFAULT_FARMER_NAME + "," + UPDATED_FARMER_NAME,
            "farmerName.in=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerName is not null
        defaultCultivatedLandFarmersFarmerFiltering("farmerName.specified=true", "farmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerName contains
        defaultCultivatedLandFarmersFarmerFiltering(
            "farmerName.contains=" + DEFAULT_FARMER_NAME,
            "farmerName.contains=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByFarmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where farmerName does not contain
        defaultCultivatedLandFarmersFarmerFiltering(
            "farmerName.doesNotContain=" + UPDATED_FARMER_NAME,
            "farmerName.doesNotContain=" + DEFAULT_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByNicNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where nicNo equals to
        defaultCultivatedLandFarmersFarmerFiltering("nicNo.equals=" + DEFAULT_NIC_NO, "nicNo.equals=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByNicNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where nicNo in
        defaultCultivatedLandFarmersFarmerFiltering("nicNo.in=" + DEFAULT_NIC_NO + "," + UPDATED_NIC_NO, "nicNo.in=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByNicNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where nicNo is not null
        defaultCultivatedLandFarmersFarmerFiltering("nicNo.specified=true", "nicNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByNicNoContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where nicNo contains
        defaultCultivatedLandFarmersFarmerFiltering("nicNo.contains=" + DEFAULT_NIC_NO, "nicNo.contains=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByNicNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where nicNo does not contain
        defaultCultivatedLandFarmersFarmerFiltering("nicNo.doesNotContain=" + UPDATED_NIC_NO, "nicNo.doesNotContain=" + DEFAULT_NIC_NO);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddressFirstLineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addressFirstLine equals to
        defaultCultivatedLandFarmersFarmerFiltering(
            "addressFirstLine.equals=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.equals=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddressFirstLineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addressFirstLine in
        defaultCultivatedLandFarmersFarmerFiltering(
            "addressFirstLine.in=" + DEFAULT_ADDRESS_FIRST_LINE + "," + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.in=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddressFirstLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addressFirstLine is not null
        defaultCultivatedLandFarmersFarmerFiltering("addressFirstLine.specified=true", "addressFirstLine.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddressFirstLineContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addressFirstLine contains
        defaultCultivatedLandFarmersFarmerFiltering(
            "addressFirstLine.contains=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.contains=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddressFirstLineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addressFirstLine does not contain
        defaultCultivatedLandFarmersFarmerFiltering(
            "addressFirstLine.doesNotContain=" + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.doesNotContain=" + DEFAULT_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByContactNoEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where contactNoEmail equals to
        defaultCultivatedLandFarmersFarmerFiltering(
            "contactNoEmail.equals=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.equals=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByContactNoEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where contactNoEmail in
        defaultCultivatedLandFarmersFarmerFiltering(
            "contactNoEmail.in=" + DEFAULT_CONTACT_NO_EMAIL + "," + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.in=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByContactNoEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where contactNoEmail is not null
        defaultCultivatedLandFarmersFarmerFiltering("contactNoEmail.specified=true", "contactNoEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByContactNoEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where contactNoEmail contains
        defaultCultivatedLandFarmersFarmerFiltering(
            "contactNoEmail.contains=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.contains=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByContactNoEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where contactNoEmail does not contain
        defaultCultivatedLandFarmersFarmerFiltering(
            "contactNoEmail.doesNotContain=" + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.doesNotContain=" + DEFAULT_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId equals to
        defaultCultivatedLandFarmersFarmerFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId in
        defaultCultivatedLandFarmersFarmerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId is not null
        defaultCultivatedLandFarmersFarmerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId is greater than or equal to
        defaultCultivatedLandFarmersFarmerFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId is less than or equal to
        defaultCultivatedLandFarmersFarmerFiltering(
            "provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId is less than
        defaultCultivatedLandFarmersFarmerFiltering(
            "provinceId.lessThan=" + UPDATED_PROVINCE_ID,
            "provinceId.lessThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where provinceId is greater than
        defaultCultivatedLandFarmersFarmerFiltering(
            "provinceId.greaterThan=" + SMALLER_PROVINCE_ID,
            "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId equals to
        defaultCultivatedLandFarmersFarmerFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId in
        defaultCultivatedLandFarmersFarmerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId is not null
        defaultCultivatedLandFarmersFarmerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId is greater than or equal to
        defaultCultivatedLandFarmersFarmerFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId is less than or equal to
        defaultCultivatedLandFarmersFarmerFiltering(
            "districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId is less than
        defaultCultivatedLandFarmersFarmerFiltering(
            "districtId.lessThan=" + UPDATED_DISTRICT_ID,
            "districtId.lessThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where districtId is greater than
        defaultCultivatedLandFarmersFarmerFiltering(
            "districtId.greaterThan=" + SMALLER_DISTRICT_ID,
            "districtId.greaterThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId equals to
        defaultCultivatedLandFarmersFarmerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId in
        defaultCultivatedLandFarmersFarmerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId is not null
        defaultCultivatedLandFarmersFarmerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId is greater than or equal to
        defaultCultivatedLandFarmersFarmerFiltering("dsId.greaterThanOrEqual=" + DEFAULT_DS_ID, "dsId.greaterThanOrEqual=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId is less than or equal to
        defaultCultivatedLandFarmersFarmerFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId is less than
        defaultCultivatedLandFarmersFarmerFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where dsId is greater than
        defaultCultivatedLandFarmersFarmerFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId equals to
        defaultCultivatedLandFarmersFarmerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId in
        defaultCultivatedLandFarmersFarmerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId is not null
        defaultCultivatedLandFarmersFarmerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId is greater than or equal to
        defaultCultivatedLandFarmersFarmerFiltering("gnId.greaterThanOrEqual=" + DEFAULT_GN_ID, "gnId.greaterThanOrEqual=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId is less than or equal to
        defaultCultivatedLandFarmersFarmerFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId is less than
        defaultCultivatedLandFarmersFarmerFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where gnId is greater than
        defaultCultivatedLandFarmersFarmerFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where city equals to
        defaultCultivatedLandFarmersFarmerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where city in
        defaultCultivatedLandFarmersFarmerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where city is not null
        defaultCultivatedLandFarmersFarmerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where city contains
        defaultCultivatedLandFarmersFarmerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where city does not contain
        defaultCultivatedLandFarmersFarmerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addedDate equals to
        defaultCultivatedLandFarmersFarmerFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addedDate in
        defaultCultivatedLandFarmersFarmerFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersFarmersByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        // Get all the cultivatedLandFarmersFarmerList where addedDate is not null
        defaultCultivatedLandFarmersFarmerFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    private void defaultCultivatedLandFarmersFarmerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandFarmersFarmerShouldBeFound(shouldBeFound);
        defaultCultivatedLandFarmersFarmerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandFarmersFarmerShouldBeFound(String filter) throws Exception {
        restCultivatedLandFarmersFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmersFarmer.getId().intValue())))
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
        restCultivatedLandFarmersFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandFarmersFarmerShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandFarmersFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandFarmersFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandFarmersFarmer() throws Exception {
        // Get the cultivatedLandFarmersFarmer
        restCultivatedLandFarmersFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandFarmersFarmer() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmersFarmer
        CultivatedLandFarmersFarmer updatedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository
            .findById(cultivatedLandFarmersFarmer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandFarmersFarmer are not directly saved in db
        em.detach(updatedCultivatedLandFarmersFarmer);
        updatedCultivatedLandFarmersFarmer
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

        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandFarmersFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandFarmersFarmer))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandFarmersFarmerToMatchAllProperties(updatedCultivatedLandFarmersFarmer);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmersFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandFarmersFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmersFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmersFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandFarmersFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmersFarmer using partial update
        CultivatedLandFarmersFarmer partialUpdatedCultivatedLandFarmersFarmer = new CultivatedLandFarmersFarmer();
        partialUpdatedCultivatedLandFarmersFarmer.setId(cultivatedLandFarmersFarmer.getId());

        partialUpdatedCultivatedLandFarmersFarmer
            .farmerId(UPDATED_FARMER_ID)
            .addressFirstLine(UPDATED_ADDRESS_FIRST_LINE)
            .contactNoEmail(UPDATED_CONTACT_NO_EMAIL)
            .provinceId(UPDATED_PROVINCE_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .addedDate(UPDATED_ADDED_DATE);

        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmersFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmersFarmer))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmersFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmersFarmerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandFarmersFarmer, cultivatedLandFarmersFarmer),
            getPersistedCultivatedLandFarmersFarmer(cultivatedLandFarmersFarmer)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandFarmersFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmersFarmer using partial update
        CultivatedLandFarmersFarmer partialUpdatedCultivatedLandFarmersFarmer = new CultivatedLandFarmersFarmer();
        partialUpdatedCultivatedLandFarmersFarmer.setId(cultivatedLandFarmersFarmer.getId());

        partialUpdatedCultivatedLandFarmersFarmer
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

        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmersFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmersFarmer))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmersFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmersFarmerUpdatableFieldsEquals(
            partialUpdatedCultivatedLandFarmersFarmer,
            getPersistedCultivatedLandFarmersFarmer(partialUpdatedCultivatedLandFarmersFarmer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmersFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandFarmersFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmersFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandFarmersFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmersFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmersFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmersFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandFarmersFarmer() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerRepository.saveAndFlush(cultivatedLandFarmersFarmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandFarmersFarmer
        restCultivatedLandFarmersFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandFarmersFarmer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandFarmersFarmerRepository.count();
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

    protected CultivatedLandFarmersFarmer getPersistedCultivatedLandFarmersFarmer(CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer) {
        return cultivatedLandFarmersFarmerRepository.findById(cultivatedLandFarmersFarmer.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandFarmersFarmerToMatchAllProperties(
        CultivatedLandFarmersFarmer expectedCultivatedLandFarmersFarmer
    ) {
        assertCultivatedLandFarmersFarmerAllPropertiesEquals(
            expectedCultivatedLandFarmersFarmer,
            getPersistedCultivatedLandFarmersFarmer(expectedCultivatedLandFarmersFarmer)
        );
    }

    protected void assertPersistedCultivatedLandFarmersFarmerToMatchUpdatableProperties(
        CultivatedLandFarmersFarmer expectedCultivatedLandFarmersFarmer
    ) {
        assertCultivatedLandFarmersFarmerAllUpdatablePropertiesEquals(
            expectedCultivatedLandFarmersFarmer,
            getPersistedCultivatedLandFarmersFarmer(expectedCultivatedLandFarmersFarmer)
        );
    }
}
