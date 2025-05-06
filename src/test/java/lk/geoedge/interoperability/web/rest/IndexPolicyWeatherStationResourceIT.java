package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPolicyWeatherStationAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPolicyWeatherStation;
import lk.geoedge.interoperability.repository.IndexPolicyWeatherStationRepository;
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
 * Integration tests for the {@link IndexPolicyWeatherStationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPolicyWeatherStationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;
    private static final Double SMALLER_LATITUDE = 1D - 1D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;
    private static final Double SMALLER_LONGITUDE = 1D - 1D;

    private static final Integer DEFAULT_GN_ID = 1;
    private static final Integer UPDATED_GN_ID = 2;
    private static final Integer SMALLER_GN_ID = 1 - 1;

    private static final Integer DEFAULT_DISTRICT_ID = 1;
    private static final Integer UPDATED_DISTRICT_ID = 2;
    private static final Integer SMALLER_DISTRICT_ID = 1 - 1;

    private static final Integer DEFAULT_PROVINCE_ID = 1;
    private static final Integer UPDATED_PROVINCE_ID = 2;
    private static final Integer SMALLER_PROVINCE_ID = 1 - 1;

    private static final Integer DEFAULT_DS_ID = 1;
    private static final Integer UPDATED_DS_ID = 2;
    private static final Integer SMALLER_DS_ID = 1 - 1;

    private static final Integer DEFAULT_ADDED_BY = 1;
    private static final Integer UPDATED_ADDED_BY = 2;
    private static final Integer SMALLER_ADDED_BY = 1 - 1;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/index-policy-weather-stations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPolicyWeatherStationMockMvc;

    private IndexPolicyWeatherStation indexPolicyWeatherStation;

    private IndexPolicyWeatherStation insertedIndexPolicyWeatherStation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyWeatherStation createEntity() {
        return new IndexPolicyWeatherStation()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .gnId(DEFAULT_GN_ID)
            .districtId(DEFAULT_DISTRICT_ID)
            .provinceId(DEFAULT_PROVINCE_ID)
            .dsId(DEFAULT_DS_ID)
            .addedBy(DEFAULT_ADDED_BY)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyWeatherStation createUpdatedEntity() {
        return new IndexPolicyWeatherStation()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .gnId(UPDATED_GN_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .provinceId(UPDATED_PROVINCE_ID)
            .dsId(UPDATED_DS_ID)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        indexPolicyWeatherStation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPolicyWeatherStation != null) {
            indexPolicyWeatherStationRepository.delete(insertedIndexPolicyWeatherStation);
            insertedIndexPolicyWeatherStation = null;
        }
    }

    @Test
    @Transactional
    void createIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPolicyWeatherStation
        var returnedIndexPolicyWeatherStation = om.readValue(
            restIndexPolicyWeatherStationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPolicyWeatherStation))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPolicyWeatherStation.class
        );

        // Validate the IndexPolicyWeatherStation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPolicyWeatherStationUpdatableFieldsEquals(
            returnedIndexPolicyWeatherStation,
            getPersistedIndexPolicyWeatherStation(returnedIndexPolicyWeatherStation)
        );

        insertedIndexPolicyWeatherStation = returnedIndexPolicyWeatherStation;
    }

    @Test
    @Transactional
    void createIndexPolicyWeatherStationWithExistingId() throws Exception {
        // Create the IndexPolicyWeatherStation with an existing ID
        indexPolicyWeatherStation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPolicyWeatherStationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStations() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList
        restIndexPolicyWeatherStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyWeatherStation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].gnId").value(hasItem(DEFAULT_GN_ID)))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].dsId").value(hasItem(DEFAULT_DS_ID)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getIndexPolicyWeatherStation() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get the indexPolicyWeatherStation
        restIndexPolicyWeatherStationMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPolicyWeatherStation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPolicyWeatherStation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.gnId").value(DEFAULT_GN_ID))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID))
            .andExpect(jsonPath("$.provinceId").value(DEFAULT_PROVINCE_ID))
            .andExpect(jsonPath("$.dsId").value(DEFAULT_DS_ID))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getIndexPolicyWeatherStationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        Long id = indexPolicyWeatherStation.getId();

        defaultIndexPolicyWeatherStationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPolicyWeatherStationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPolicyWeatherStationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where name equals to
        defaultIndexPolicyWeatherStationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where name in
        defaultIndexPolicyWeatherStationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where name is not null
        defaultIndexPolicyWeatherStationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where name contains
        defaultIndexPolicyWeatherStationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where name does not contain
        defaultIndexPolicyWeatherStationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where code equals to
        defaultIndexPolicyWeatherStationFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where code in
        defaultIndexPolicyWeatherStationFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where code is not null
        defaultIndexPolicyWeatherStationFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where code contains
        defaultIndexPolicyWeatherStationFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where code does not contain
        defaultIndexPolicyWeatherStationFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude equals to
        defaultIndexPolicyWeatherStationFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude in
        defaultIndexPolicyWeatherStationFiltering(
            "latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE,
            "latitude.in=" + UPDATED_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude is not null
        defaultIndexPolicyWeatherStationFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE,
            "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude is less than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "latitude.lessThanOrEqual=" + DEFAULT_LATITUDE,
            "latitude.lessThanOrEqual=" + SMALLER_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude is less than
        defaultIndexPolicyWeatherStationFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where latitude is greater than
        defaultIndexPolicyWeatherStationFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude equals to
        defaultIndexPolicyWeatherStationFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude in
        defaultIndexPolicyWeatherStationFiltering(
            "longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE,
            "longitude.in=" + UPDATED_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude is not null
        defaultIndexPolicyWeatherStationFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE,
            "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude is less than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE,
            "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude is less than
        defaultIndexPolicyWeatherStationFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where longitude is greater than
        defaultIndexPolicyWeatherStationFiltering(
            "longitude.greaterThan=" + SMALLER_LONGITUDE,
            "longitude.greaterThan=" + DEFAULT_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId equals to
        defaultIndexPolicyWeatherStationFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId in
        defaultIndexPolicyWeatherStationFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId is not null
        defaultIndexPolicyWeatherStationFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering("gnId.greaterThanOrEqual=" + DEFAULT_GN_ID, "gnId.greaterThanOrEqual=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId is less than or equal to
        defaultIndexPolicyWeatherStationFiltering("gnId.lessThanOrEqual=" + DEFAULT_GN_ID, "gnId.lessThanOrEqual=" + SMALLER_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId is less than
        defaultIndexPolicyWeatherStationFiltering("gnId.lessThan=" + UPDATED_GN_ID, "gnId.lessThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByGnIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where gnId is greater than
        defaultIndexPolicyWeatherStationFiltering("gnId.greaterThan=" + SMALLER_GN_ID, "gnId.greaterThan=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId equals to
        defaultIndexPolicyWeatherStationFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId in
        defaultIndexPolicyWeatherStationFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId is not null
        defaultIndexPolicyWeatherStationFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "districtId.greaterThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.greaterThanOrEqual=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId is less than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "districtId.lessThanOrEqual=" + DEFAULT_DISTRICT_ID,
            "districtId.lessThanOrEqual=" + SMALLER_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId is less than
        defaultIndexPolicyWeatherStationFiltering(
            "districtId.lessThan=" + UPDATED_DISTRICT_ID,
            "districtId.lessThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDistrictIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where districtId is greater than
        defaultIndexPolicyWeatherStationFiltering(
            "districtId.greaterThan=" + SMALLER_DISTRICT_ID,
            "districtId.greaterThan=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId equals to
        defaultIndexPolicyWeatherStationFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId in
        defaultIndexPolicyWeatherStationFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId is not null
        defaultIndexPolicyWeatherStationFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "provinceId.greaterThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.greaterThanOrEqual=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId is less than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "provinceId.lessThanOrEqual=" + DEFAULT_PROVINCE_ID,
            "provinceId.lessThanOrEqual=" + SMALLER_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId is less than
        defaultIndexPolicyWeatherStationFiltering(
            "provinceId.lessThan=" + UPDATED_PROVINCE_ID,
            "provinceId.lessThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByProvinceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where provinceId is greater than
        defaultIndexPolicyWeatherStationFiltering(
            "provinceId.greaterThan=" + SMALLER_PROVINCE_ID,
            "provinceId.greaterThan=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId equals to
        defaultIndexPolicyWeatherStationFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId in
        defaultIndexPolicyWeatherStationFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId is not null
        defaultIndexPolicyWeatherStationFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering("dsId.greaterThanOrEqual=" + DEFAULT_DS_ID, "dsId.greaterThanOrEqual=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId is less than or equal to
        defaultIndexPolicyWeatherStationFiltering("dsId.lessThanOrEqual=" + DEFAULT_DS_ID, "dsId.lessThanOrEqual=" + SMALLER_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId is less than
        defaultIndexPolicyWeatherStationFiltering("dsId.lessThan=" + UPDATED_DS_ID, "dsId.lessThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByDsIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where dsId is greater than
        defaultIndexPolicyWeatherStationFiltering("dsId.greaterThan=" + SMALLER_DS_ID, "dsId.greaterThan=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy equals to
        defaultIndexPolicyWeatherStationFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy in
        defaultIndexPolicyWeatherStationFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy is not null
        defaultIndexPolicyWeatherStationFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "addedBy.greaterThanOrEqual=" + DEFAULT_ADDED_BY,
            "addedBy.greaterThanOrEqual=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy is less than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "addedBy.lessThanOrEqual=" + DEFAULT_ADDED_BY,
            "addedBy.lessThanOrEqual=" + SMALLER_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy is less than
        defaultIndexPolicyWeatherStationFiltering("addedBy.lessThan=" + UPDATED_ADDED_BY, "addedBy.lessThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByAddedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where addedBy is greater than
        defaultIndexPolicyWeatherStationFiltering("addedBy.greaterThan=" + SMALLER_ADDED_BY, "addedBy.greaterThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt equals to
        defaultIndexPolicyWeatherStationFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt in
        defaultIndexPolicyWeatherStationFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt is not null
        defaultIndexPolicyWeatherStationFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt is greater than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt is less than or equal to
        defaultIndexPolicyWeatherStationFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt is less than
        defaultIndexPolicyWeatherStationFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllIndexPolicyWeatherStationsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        // Get all the indexPolicyWeatherStationList where createdAt is greater than
        defaultIndexPolicyWeatherStationFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    private void defaultIndexPolicyWeatherStationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPolicyWeatherStationShouldBeFound(shouldBeFound);
        defaultIndexPolicyWeatherStationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPolicyWeatherStationShouldBeFound(String filter) throws Exception {
        restIndexPolicyWeatherStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyWeatherStation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].gnId").value(hasItem(DEFAULT_GN_ID)))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].dsId").value(hasItem(DEFAULT_DS_ID)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restIndexPolicyWeatherStationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPolicyWeatherStationShouldNotBeFound(String filter) throws Exception {
        restIndexPolicyWeatherStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPolicyWeatherStationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPolicyWeatherStation() throws Exception {
        // Get the indexPolicyWeatherStation
        restIndexPolicyWeatherStationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPolicyWeatherStation() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyWeatherStation
        IndexPolicyWeatherStation updatedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository
            .findById(indexPolicyWeatherStation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPolicyWeatherStation are not directly saved in db
        em.detach(updatedIndexPolicyWeatherStation);
        updatedIndexPolicyWeatherStation
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .gnId(UPDATED_GN_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .provinceId(UPDATED_PROVINCE_ID)
            .dsId(UPDATED_DS_ID)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restIndexPolicyWeatherStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPolicyWeatherStation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPolicyWeatherStation))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPolicyWeatherStationToMatchAllProperties(updatedIndexPolicyWeatherStation);
    }

    @Test
    @Transactional
    void putNonExistingIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyWeatherStation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyWeatherStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPolicyWeatherStation.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyWeatherStation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyWeatherStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyWeatherStation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyWeatherStationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPolicyWeatherStationWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyWeatherStation using partial update
        IndexPolicyWeatherStation partialUpdatedIndexPolicyWeatherStation = new IndexPolicyWeatherStation();
        partialUpdatedIndexPolicyWeatherStation.setId(indexPolicyWeatherStation.getId());

        partialUpdatedIndexPolicyWeatherStation
            .code(UPDATED_CODE)
            .longitude(UPDATED_LONGITUDE)
            .gnId(UPDATED_GN_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restIndexPolicyWeatherStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyWeatherStation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyWeatherStation))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyWeatherStation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyWeatherStationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPolicyWeatherStation, indexPolicyWeatherStation),
            getPersistedIndexPolicyWeatherStation(indexPolicyWeatherStation)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPolicyWeatherStationWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyWeatherStation using partial update
        IndexPolicyWeatherStation partialUpdatedIndexPolicyWeatherStation = new IndexPolicyWeatherStation();
        partialUpdatedIndexPolicyWeatherStation.setId(indexPolicyWeatherStation.getId());

        partialUpdatedIndexPolicyWeatherStation
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .gnId(UPDATED_GN_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .provinceId(UPDATED_PROVINCE_ID)
            .dsId(UPDATED_DS_ID)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restIndexPolicyWeatherStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyWeatherStation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyWeatherStation))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyWeatherStation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyWeatherStationUpdatableFieldsEquals(
            partialUpdatedIndexPolicyWeatherStation,
            getPersistedIndexPolicyWeatherStation(partialUpdatedIndexPolicyWeatherStation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyWeatherStation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyWeatherStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPolicyWeatherStation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyWeatherStation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyWeatherStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPolicyWeatherStation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyWeatherStation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyWeatherStationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyWeatherStation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyWeatherStation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPolicyWeatherStation() throws Exception {
        // Initialize the database
        insertedIndexPolicyWeatherStation = indexPolicyWeatherStationRepository.saveAndFlush(indexPolicyWeatherStation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPolicyWeatherStation
        restIndexPolicyWeatherStationMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPolicyWeatherStation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPolicyWeatherStationRepository.count();
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

    protected IndexPolicyWeatherStation getPersistedIndexPolicyWeatherStation(IndexPolicyWeatherStation indexPolicyWeatherStation) {
        return indexPolicyWeatherStationRepository.findById(indexPolicyWeatherStation.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPolicyWeatherStationToMatchAllProperties(
        IndexPolicyWeatherStation expectedIndexPolicyWeatherStation
    ) {
        assertIndexPolicyWeatherStationAllPropertiesEquals(
            expectedIndexPolicyWeatherStation,
            getPersistedIndexPolicyWeatherStation(expectedIndexPolicyWeatherStation)
        );
    }

    protected void assertPersistedIndexPolicyWeatherStationToMatchUpdatableProperties(
        IndexPolicyWeatherStation expectedIndexPolicyWeatherStation
    ) {
        assertIndexPolicyWeatherStationAllUpdatablePropertiesEquals(
            expectedIndexPolicyWeatherStation,
            getPersistedIndexPolicyWeatherStation(expectedIndexPolicyWeatherStation)
        );
    }
}
