package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListFarmerAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer;
import lk.geoedge.interoperability.repository.IndexPayoutEventListFarmerRepository;
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
 * Integration tests for the {@link IndexPayoutEventListFarmerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPayoutEventListFarmerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/index-payout-event-list-farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPayoutEventListFarmerMockMvc;

    private IndexPayoutEventListFarmer indexPayoutEventListFarmer;

    private IndexPayoutEventListFarmer insertedIndexPayoutEventListFarmer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPayoutEventListFarmer createEntity() {
        return new IndexPayoutEventListFarmer()
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
    public static IndexPayoutEventListFarmer createUpdatedEntity() {
        return new IndexPayoutEventListFarmer()
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
        indexPayoutEventListFarmer = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPayoutEventListFarmer != null) {
            indexPayoutEventListFarmerRepository.delete(insertedIndexPayoutEventListFarmer);
            insertedIndexPayoutEventListFarmer = null;
        }
    }

    @Test
    @Transactional
    void createIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPayoutEventListFarmer
        var returnedIndexPayoutEventListFarmer = om.readValue(
            restIndexPayoutEventListFarmerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPayoutEventListFarmer.class
        );

        // Validate the IndexPayoutEventListFarmer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPayoutEventListFarmerUpdatableFieldsEquals(
            returnedIndexPayoutEventListFarmer,
            getPersistedIndexPayoutEventListFarmer(returnedIndexPayoutEventListFarmer)
        );

        insertedIndexPayoutEventListFarmer = returnedIndexPayoutEventListFarmer;
    }

    @Test
    @Transactional
    void createIndexPayoutEventListFarmerWithExistingId() throws Exception {
        // Create the IndexPayoutEventListFarmer with an existing ID
        indexPayoutEventListFarmer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmers() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList
        restIndexPayoutEventListFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventListFarmer.getId().intValue())))
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
    void getIndexPayoutEventListFarmer() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get the indexPayoutEventListFarmer
        restIndexPayoutEventListFarmerMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPayoutEventListFarmer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPayoutEventListFarmer.getId().intValue()))
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
    void getIndexPayoutEventListFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        Long id = indexPayoutEventListFarmer.getId();

        defaultIndexPayoutEventListFarmerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPayoutEventListFarmerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPayoutEventListFarmerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerId equals to
        defaultIndexPayoutEventListFarmerFiltering("farmerId.equals=" + DEFAULT_FARMER_ID, "farmerId.equals=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerId in
        defaultIndexPayoutEventListFarmerFiltering(
            "farmerId.in=" + DEFAULT_FARMER_ID + "," + UPDATED_FARMER_ID,
            "farmerId.in=" + UPDATED_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerId is not null
        defaultIndexPayoutEventListFarmerFiltering("farmerId.specified=true", "farmerId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerIdContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerId contains
        defaultIndexPayoutEventListFarmerFiltering("farmerId.contains=" + DEFAULT_FARMER_ID, "farmerId.contains=" + UPDATED_FARMER_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerId does not contain
        defaultIndexPayoutEventListFarmerFiltering(
            "farmerId.doesNotContain=" + UPDATED_FARMER_ID,
            "farmerId.doesNotContain=" + DEFAULT_FARMER_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerName equals to
        defaultIndexPayoutEventListFarmerFiltering("farmerName.equals=" + DEFAULT_FARMER_NAME, "farmerName.equals=" + UPDATED_FARMER_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerName in
        defaultIndexPayoutEventListFarmerFiltering(
            "farmerName.in=" + DEFAULT_FARMER_NAME + "," + UPDATED_FARMER_NAME,
            "farmerName.in=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerName is not null
        defaultIndexPayoutEventListFarmerFiltering("farmerName.specified=true", "farmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerName contains
        defaultIndexPayoutEventListFarmerFiltering(
            "farmerName.contains=" + DEFAULT_FARMER_NAME,
            "farmerName.contains=" + UPDATED_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByFarmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where farmerName does not contain
        defaultIndexPayoutEventListFarmerFiltering(
            "farmerName.doesNotContain=" + UPDATED_FARMER_NAME,
            "farmerName.doesNotContain=" + DEFAULT_FARMER_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByNicNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where nicNo equals to
        defaultIndexPayoutEventListFarmerFiltering("nicNo.equals=" + DEFAULT_NIC_NO, "nicNo.equals=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByNicNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where nicNo in
        defaultIndexPayoutEventListFarmerFiltering("nicNo.in=" + DEFAULT_NIC_NO + "," + UPDATED_NIC_NO, "nicNo.in=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByNicNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where nicNo is not null
        defaultIndexPayoutEventListFarmerFiltering("nicNo.specified=true", "nicNo.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByNicNoContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where nicNo contains
        defaultIndexPayoutEventListFarmerFiltering("nicNo.contains=" + DEFAULT_NIC_NO, "nicNo.contains=" + UPDATED_NIC_NO);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByNicNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where nicNo does not contain
        defaultIndexPayoutEventListFarmerFiltering("nicNo.doesNotContain=" + UPDATED_NIC_NO, "nicNo.doesNotContain=" + DEFAULT_NIC_NO);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddressFirstLineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addressFirstLine equals to
        defaultIndexPayoutEventListFarmerFiltering(
            "addressFirstLine.equals=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.equals=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddressFirstLineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addressFirstLine in
        defaultIndexPayoutEventListFarmerFiltering(
            "addressFirstLine.in=" + DEFAULT_ADDRESS_FIRST_LINE + "," + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.in=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddressFirstLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addressFirstLine is not null
        defaultIndexPayoutEventListFarmerFiltering("addressFirstLine.specified=true", "addressFirstLine.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddressFirstLineContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addressFirstLine contains
        defaultIndexPayoutEventListFarmerFiltering(
            "addressFirstLine.contains=" + DEFAULT_ADDRESS_FIRST_LINE,
            "addressFirstLine.contains=" + UPDATED_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddressFirstLineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addressFirstLine does not contain
        defaultIndexPayoutEventListFarmerFiltering(
            "addressFirstLine.doesNotContain=" + UPDATED_ADDRESS_FIRST_LINE,
            "addressFirstLine.doesNotContain=" + DEFAULT_ADDRESS_FIRST_LINE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByContactNoEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where contactNoEmail equals to
        defaultIndexPayoutEventListFarmerFiltering(
            "contactNoEmail.equals=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.equals=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByContactNoEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where contactNoEmail in
        defaultIndexPayoutEventListFarmerFiltering(
            "contactNoEmail.in=" + DEFAULT_CONTACT_NO_EMAIL + "," + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.in=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByContactNoEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where contactNoEmail is not null
        defaultIndexPayoutEventListFarmerFiltering("contactNoEmail.specified=true", "contactNoEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByContactNoEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where contactNoEmail contains
        defaultIndexPayoutEventListFarmerFiltering(
            "contactNoEmail.contains=" + DEFAULT_CONTACT_NO_EMAIL,
            "contactNoEmail.contains=" + UPDATED_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByContactNoEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where contactNoEmail does not contain
        defaultIndexPayoutEventListFarmerFiltering(
            "contactNoEmail.doesNotContain=" + UPDATED_CONTACT_NO_EMAIL,
            "contactNoEmail.doesNotContain=" + DEFAULT_CONTACT_NO_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId equals to
        defaultIndexPayoutEventListFarmerFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId in
        defaultIndexPayoutEventListFarmerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId is not null
        defaultIndexPayoutEventListFarmerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId is greater than or equal to
        defaultIndexPayoutEventListFarmerFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId is less than or equal to
        defaultIndexPayoutEventListFarmerFiltering(
            "provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId is less than
        defaultIndexPayoutEventListFarmerFiltering(
            "provinceId.lessThan=" + UPDATED_PROVINCE_ID,
            "provinceId.lessThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where provinceId is greater than
        defaultIndexPayoutEventListFarmerFiltering(
            "provinceId.greaterThan=" + SMALLER_PROVINCE_ID,
            "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId equals to
        defaultIndexPayoutEventListFarmerFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId in
        defaultIndexPayoutEventListFarmerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId is not null
        defaultIndexPayoutEventListFarmerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId is greater than or equal to
        defaultIndexPayoutEventListFarmerFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId is less than or equal to
        defaultIndexPayoutEventListFarmerFiltering(
            "districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId is less than
        defaultIndexPayoutEventListFarmerFiltering(
            "districtId.lessThan=" + UPDATED_DISTRICT_ID,
            "districtId.lessThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where districtId is greater than
        defaultIndexPayoutEventListFarmerFiltering(
            "districtId.greaterThan=" + SMALLER_DISTRICT_ID,
            "districtId.greaterThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId equals to
        defaultIndexPayoutEventListFarmerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId in
        defaultIndexPayoutEventListFarmerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId is not null
        defaultIndexPayoutEventListFarmerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId is greater than or equal to
        defaultIndexPayoutEventListFarmerFiltering("dsId.greaterThanOrEqual=" + DEFAULT_DS_ID, "dsId.greaterThanOrEqual=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId is less than or equal to
        defaultIndexPayoutEventListFarmerFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId is less than
        defaultIndexPayoutEventListFarmerFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where dsId is greater than
        defaultIndexPayoutEventListFarmerFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId equals to
        defaultIndexPayoutEventListFarmerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId in
        defaultIndexPayoutEventListFarmerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId is not null
        defaultIndexPayoutEventListFarmerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId is greater than or equal to
        defaultIndexPayoutEventListFarmerFiltering("gnId.greaterThanOrEqual=" + DEFAULT_GN_ID, "gnId.greaterThanOrEqual=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId is less than or equal to
        defaultIndexPayoutEventListFarmerFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId is less than
        defaultIndexPayoutEventListFarmerFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where gnId is greater than
        defaultIndexPayoutEventListFarmerFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where city equals to
        defaultIndexPayoutEventListFarmerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where city in
        defaultIndexPayoutEventListFarmerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where city is not null
        defaultIndexPayoutEventListFarmerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where city contains
        defaultIndexPayoutEventListFarmerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where city does not contain
        defaultIndexPayoutEventListFarmerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addedDate equals to
        defaultIndexPayoutEventListFarmerFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addedDate in
        defaultIndexPayoutEventListFarmerFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListFarmersByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        // Get all the indexPayoutEventListFarmerList where addedDate is not null
        defaultIndexPayoutEventListFarmerFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    private void defaultIndexPayoutEventListFarmerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPayoutEventListFarmerShouldBeFound(shouldBeFound);
        defaultIndexPayoutEventListFarmerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPayoutEventListFarmerShouldBeFound(String filter) throws Exception {
        restIndexPayoutEventListFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventListFarmer.getId().intValue())))
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
        restIndexPayoutEventListFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPayoutEventListFarmerShouldNotBeFound(String filter) throws Exception {
        restIndexPayoutEventListFarmerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPayoutEventListFarmerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPayoutEventListFarmer() throws Exception {
        // Get the indexPayoutEventListFarmer
        restIndexPayoutEventListFarmerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPayoutEventListFarmer() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListFarmer
        IndexPayoutEventListFarmer updatedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository
            .findById(indexPayoutEventListFarmer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPayoutEventListFarmer are not directly saved in db
        em.detach(updatedIndexPayoutEventListFarmer);
        updatedIndexPayoutEventListFarmer
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

        restIndexPayoutEventListFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPayoutEventListFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPayoutEventListFarmer))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPayoutEventListFarmerToMatchAllProperties(updatedIndexPayoutEventListFarmer);
    }

    @Test
    @Transactional
    void putNonExistingIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPayoutEventListFarmer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPayoutEventListFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListFarmer using partial update
        IndexPayoutEventListFarmer partialUpdatedIndexPayoutEventListFarmer = new IndexPayoutEventListFarmer();
        partialUpdatedIndexPayoutEventListFarmer.setId(indexPayoutEventListFarmer.getId());

        partialUpdatedIndexPayoutEventListFarmer
            .farmerId(UPDATED_FARMER_ID)
            .farmerName(UPDATED_FARMER_NAME)
            .nicNo(UPDATED_NIC_NO)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .city(UPDATED_CITY);

        restIndexPayoutEventListFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventListFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventListFarmer))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListFarmerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPayoutEventListFarmer, indexPayoutEventListFarmer),
            getPersistedIndexPayoutEventListFarmer(indexPayoutEventListFarmer)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPayoutEventListFarmerWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListFarmer using partial update
        IndexPayoutEventListFarmer partialUpdatedIndexPayoutEventListFarmer = new IndexPayoutEventListFarmer();
        partialUpdatedIndexPayoutEventListFarmer.setId(indexPayoutEventListFarmer.getId());

        partialUpdatedIndexPayoutEventListFarmer
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

        restIndexPayoutEventListFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventListFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventListFarmer))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListFarmer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListFarmerUpdatableFieldsEquals(
            partialUpdatedIndexPayoutEventListFarmer,
            getPersistedIndexPayoutEventListFarmer(partialUpdatedIndexPayoutEventListFarmer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListFarmer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPayoutEventListFarmer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPayoutEventListFarmer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListFarmer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListFarmerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListFarmer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventListFarmer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPayoutEventListFarmer() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListFarmer = indexPayoutEventListFarmerRepository.saveAndFlush(indexPayoutEventListFarmer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPayoutEventListFarmer
        restIndexPayoutEventListFarmerMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPayoutEventListFarmer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPayoutEventListFarmerRepository.count();
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

    protected IndexPayoutEventListFarmer getPersistedIndexPayoutEventListFarmer(IndexPayoutEventListFarmer indexPayoutEventListFarmer) {
        return indexPayoutEventListFarmerRepository.findById(indexPayoutEventListFarmer.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPayoutEventListFarmerToMatchAllProperties(
        IndexPayoutEventListFarmer expectedIndexPayoutEventListFarmer
    ) {
        assertIndexPayoutEventListFarmerAllPropertiesEquals(
            expectedIndexPayoutEventListFarmer,
            getPersistedIndexPayoutEventListFarmer(expectedIndexPayoutEventListFarmer)
        );
    }

    protected void assertPersistedIndexPayoutEventListFarmerToMatchUpdatableProperties(
        IndexPayoutEventListFarmer expectedIndexPayoutEventListFarmer
    ) {
        assertIndexPayoutEventListFarmerAllUpdatablePropertiesEquals(
            expectedIndexPayoutEventListFarmer,
            getPersistedIndexPayoutEventListFarmer(expectedIndexPayoutEventListFarmer)
        );
    }
}
