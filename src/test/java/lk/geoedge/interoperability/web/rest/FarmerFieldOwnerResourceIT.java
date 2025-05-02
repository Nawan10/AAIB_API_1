package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.FarmerFieldOwnerAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.FarmerFieldOwner;
import lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerRepository;
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
 * Integration tests for the {@link FarmerFieldOwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmerFieldOwnerResourceIT {

    private static final String DEFAULT_LAND_PLOT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAND_PLOT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAND_REGISTRY_NO = "AAAAAAAAAA";
    private static final String UPDATED_LAND_REGISTRY_NO = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_LAND_EXTENT = 1D;
    private static final Double UPDATED_TOTAL_LAND_EXTENT = 2D;
    private static final Double SMALLER_TOTAL_LAND_EXTENT = 1D - 1D;

    private static final Double DEFAULT_CALCULATED_AREA = 1D;
    private static final Double UPDATED_CALCULATED_AREA = 2D;
    private static final Double SMALLER_CALCULATED_AREA = 1D - 1D;

    private static final String DEFAULT_PROVINCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DS_ID = "AAAAAAAAAA";
    private static final String UPDATED_DS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GN_ID = "AAAAAAAAAA";
    private static final String UPDATED_GN_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_CENTER_LAT = 1D;
    private static final Double UPDATED_CENTER_LAT = 2D;
    private static final Double SMALLER_CENTER_LAT = 1D - 1D;

    private static final Double DEFAULT_CENTER_LNG = 1D;
    private static final Double UPDATED_CENTER_LNG = 2D;
    private static final Double SMALLER_CENTER_LNG = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/farmer-field-owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmerFieldOwnerRepository farmerFieldOwnerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmerFieldOwnerMockMvc;

    private FarmerFieldOwner farmerFieldOwner;

    private FarmerFieldOwner insertedFarmerFieldOwner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FarmerFieldOwner createEntity() {
        return new FarmerFieldOwner()
            .landPlotName(DEFAULT_LAND_PLOT_NAME)
            .landRegistryNo(DEFAULT_LAND_REGISTRY_NO)
            .totalLandExtent(DEFAULT_TOTAL_LAND_EXTENT)
            .calculatedArea(DEFAULT_CALCULATED_AREA)
            .provinceId(DEFAULT_PROVINCE_ID)
            .districtId(DEFAULT_DISTRICT_ID)
            .dsId(DEFAULT_DS_ID)
            .gnId(DEFAULT_GN_ID)
            .centerLat(DEFAULT_CENTER_LAT)
            .centerLng(DEFAULT_CENTER_LNG);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FarmerFieldOwner createUpdatedEntity() {
        return new FarmerFieldOwner()
            .landPlotName(UPDATED_LAND_PLOT_NAME)
            .landRegistryNo(UPDATED_LAND_REGISTRY_NO)
            .totalLandExtent(UPDATED_TOTAL_LAND_EXTENT)
            .calculatedArea(UPDATED_CALCULATED_AREA)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .centerLat(UPDATED_CENTER_LAT)
            .centerLng(UPDATED_CENTER_LNG);
    }

    @BeforeEach
    void initTest() {
        farmerFieldOwner = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFarmerFieldOwner != null) {
            farmerFieldOwnerRepository.delete(insertedFarmerFieldOwner);
            insertedFarmerFieldOwner = null;
        }
    }

    @Test
    @Transactional
    void createFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FarmerFieldOwner
        var returnedFarmerFieldOwner = om.readValue(
            restFarmerFieldOwnerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(farmerFieldOwner))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FarmerFieldOwner.class
        );

        // Validate the FarmerFieldOwner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFarmerFieldOwnerUpdatableFieldsEquals(returnedFarmerFieldOwner, getPersistedFarmerFieldOwner(returnedFarmerFieldOwner));

        insertedFarmerFieldOwner = returnedFarmerFieldOwner;
    }

    @Test
    @Transactional
    void createFarmerFieldOwnerWithExistingId() throws Exception {
        // Create the FarmerFieldOwner with an existing ID
        farmerFieldOwner.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmerFieldOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwners() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList
        restFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].landPlotName").value(hasItem(DEFAULT_LAND_PLOT_NAME)))
            .andExpect(jsonPath("$.[*].landRegistryNo").value(hasItem(DEFAULT_LAND_REGISTRY_NO)))
            .andExpect(jsonPath("$.[*].totalLandExtent").value(hasItem(DEFAULT_TOTAL_LAND_EXTENT)))
            .andExpect(jsonPath("$.[*].calculatedArea").value(hasItem(DEFAULT_CALCULATED_AREA)))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].dsId").value(hasItem(DEFAULT_DS_ID)))
            .andExpect(jsonPath("$.[*].gnId").value(hasItem(DEFAULT_GN_ID)))
            .andExpect(jsonPath("$.[*].centerLat").value(hasItem(DEFAULT_CENTER_LAT)))
            .andExpect(jsonPath("$.[*].centerLng").value(hasItem(DEFAULT_CENTER_LNG)));
    }

    @Test
    @Transactional
    void getFarmerFieldOwner() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get the farmerFieldOwner
        restFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, farmerFieldOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmerFieldOwner.getId().intValue()))
            .andExpect(jsonPath("$.landPlotName").value(DEFAULT_LAND_PLOT_NAME))
            .andExpect(jsonPath("$.landRegistryNo").value(DEFAULT_LAND_REGISTRY_NO))
            .andExpect(jsonPath("$.totalLandExtent").value(DEFAULT_TOTAL_LAND_EXTENT))
            .andExpect(jsonPath("$.calculatedArea").value(DEFAULT_CALCULATED_AREA))
            .andExpect(jsonPath("$.provinceId").value(DEFAULT_PROVINCE_ID))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID))
            .andExpect(jsonPath("$.dsId").value(DEFAULT_DS_ID))
            .andExpect(jsonPath("$.gnId").value(DEFAULT_GN_ID))
            .andExpect(jsonPath("$.centerLat").value(DEFAULT_CENTER_LAT))
            .andExpect(jsonPath("$.centerLng").value(DEFAULT_CENTER_LNG));
    }

    @Test
    @Transactional
    void getFarmerFieldOwnersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        Long id = farmerFieldOwner.getId();

        defaultFarmerFieldOwnerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFarmerFieldOwnerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFarmerFieldOwnerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandPlotNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landPlotName equals to
        defaultFarmerFieldOwnerFiltering("landPlotName.equals=" + DEFAULT_LAND_PLOT_NAME, "landPlotName.equals=" + UPDATED_LAND_PLOT_NAME);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandPlotNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landPlotName in
        defaultFarmerFieldOwnerFiltering(
            "landPlotName.in=" + DEFAULT_LAND_PLOT_NAME + "," + UPDATED_LAND_PLOT_NAME,
            "landPlotName.in=" + UPDATED_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandPlotNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landPlotName is not null
        defaultFarmerFieldOwnerFiltering("landPlotName.specified=true", "landPlotName.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandPlotNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landPlotName contains
        defaultFarmerFieldOwnerFiltering(
            "landPlotName.contains=" + DEFAULT_LAND_PLOT_NAME,
            "landPlotName.contains=" + UPDATED_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandPlotNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landPlotName does not contain
        defaultFarmerFieldOwnerFiltering(
            "landPlotName.doesNotContain=" + UPDATED_LAND_PLOT_NAME,
            "landPlotName.doesNotContain=" + DEFAULT_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandRegistryNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landRegistryNo equals to
        defaultFarmerFieldOwnerFiltering(
            "landRegistryNo.equals=" + DEFAULT_LAND_REGISTRY_NO,
            "landRegistryNo.equals=" + UPDATED_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandRegistryNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landRegistryNo in
        defaultFarmerFieldOwnerFiltering(
            "landRegistryNo.in=" + DEFAULT_LAND_REGISTRY_NO + "," + UPDATED_LAND_REGISTRY_NO,
            "landRegistryNo.in=" + UPDATED_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandRegistryNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landRegistryNo is not null
        defaultFarmerFieldOwnerFiltering("landRegistryNo.specified=true", "landRegistryNo.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandRegistryNoContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landRegistryNo contains
        defaultFarmerFieldOwnerFiltering(
            "landRegistryNo.contains=" + DEFAULT_LAND_REGISTRY_NO,
            "landRegistryNo.contains=" + UPDATED_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByLandRegistryNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where landRegistryNo does not contain
        defaultFarmerFieldOwnerFiltering(
            "landRegistryNo.doesNotContain=" + UPDATED_LAND_REGISTRY_NO,
            "landRegistryNo.doesNotContain=" + DEFAULT_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent equals to
        defaultFarmerFieldOwnerFiltering(
            "totalLandExtent.equals=" + DEFAULT_TOTAL_LAND_EXTENT,
            "totalLandExtent.equals=" + UPDATED_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent in
        defaultFarmerFieldOwnerFiltering(
            "totalLandExtent.in=" + DEFAULT_TOTAL_LAND_EXTENT + "," + UPDATED_TOTAL_LAND_EXTENT,
            "totalLandExtent.in=" + UPDATED_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent is not null
        defaultFarmerFieldOwnerFiltering("totalLandExtent.specified=true", "totalLandExtent.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent is greater than or equal to
        defaultFarmerFieldOwnerFiltering(
            "totalLandExtent.greaterThanOrEqual=" + DEFAULT_TOTAL_LAND_EXTENT,
            "totalLandExtent.greaterThanOrEqual=" + UPDATED_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent is less than or equal to
        defaultFarmerFieldOwnerFiltering(
            "totalLandExtent.lessThanOrEqual=" + DEFAULT_TOTAL_LAND_EXTENT,
            "totalLandExtent.lessThanOrEqual=" + SMALLER_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent is less than
        defaultFarmerFieldOwnerFiltering(
            "totalLandExtent.lessThan=" + UPDATED_TOTAL_LAND_EXTENT,
            "totalLandExtent.lessThan=" + DEFAULT_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByTotalLandExtentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where totalLandExtent is greater than
        defaultFarmerFieldOwnerFiltering(
            "totalLandExtent.greaterThan=" + SMALLER_TOTAL_LAND_EXTENT,
            "totalLandExtent.greaterThan=" + DEFAULT_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea equals to
        defaultFarmerFieldOwnerFiltering(
            "calculatedArea.equals=" + DEFAULT_CALCULATED_AREA,
            "calculatedArea.equals=" + UPDATED_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea in
        defaultFarmerFieldOwnerFiltering(
            "calculatedArea.in=" + DEFAULT_CALCULATED_AREA + "," + UPDATED_CALCULATED_AREA,
            "calculatedArea.in=" + UPDATED_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea is not null
        defaultFarmerFieldOwnerFiltering("calculatedArea.specified=true", "calculatedArea.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea is greater than or equal to
        defaultFarmerFieldOwnerFiltering(
            "calculatedArea.greaterThanOrEqual=" + DEFAULT_CALCULATED_AREA,
            "calculatedArea.greaterThanOrEqual=" + UPDATED_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea is less than or equal to
        defaultFarmerFieldOwnerFiltering(
            "calculatedArea.lessThanOrEqual=" + DEFAULT_CALCULATED_AREA,
            "calculatedArea.lessThanOrEqual=" + SMALLER_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea is less than
        defaultFarmerFieldOwnerFiltering(
            "calculatedArea.lessThan=" + UPDATED_CALCULATED_AREA,
            "calculatedArea.lessThan=" + DEFAULT_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCalculatedAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where calculatedArea is greater than
        defaultFarmerFieldOwnerFiltering(
            "calculatedArea.greaterThan=" + SMALLER_CALCULATED_AREA,
            "calculatedArea.greaterThan=" + DEFAULT_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where provinceId equals to
        defaultFarmerFieldOwnerFiltering("provinceId.equals=" + DEFAULT_PROVINCE_ID, "provinceId.equals=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where provinceId in
        defaultFarmerFieldOwnerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where provinceId is not null
        defaultFarmerFieldOwnerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByProvinceIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where provinceId contains
        defaultFarmerFieldOwnerFiltering("provinceId.contains=" + DEFAULT_PROVINCE_ID, "provinceId.contains=" + UPDATED_PROVINCE_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByProvinceIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where provinceId does not contain
        defaultFarmerFieldOwnerFiltering(
            "provinceId.doesNotContain=" + UPDATED_PROVINCE_ID,
            "provinceId.doesNotContain=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where districtId equals to
        defaultFarmerFieldOwnerFiltering("districtId.equals=" + DEFAULT_DISTRICT_ID, "districtId.equals=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where districtId in
        defaultFarmerFieldOwnerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where districtId is not null
        defaultFarmerFieldOwnerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDistrictIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where districtId contains
        defaultFarmerFieldOwnerFiltering("districtId.contains=" + DEFAULT_DISTRICT_ID, "districtId.contains=" + UPDATED_DISTRICT_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDistrictIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where districtId does not contain
        defaultFarmerFieldOwnerFiltering(
            "districtId.doesNotContain=" + UPDATED_DISTRICT_ID,
            "districtId.doesNotContain=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where dsId equals to
        defaultFarmerFieldOwnerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where dsId in
        defaultFarmerFieldOwnerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where dsId is not null
        defaultFarmerFieldOwnerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDsIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where dsId contains
        defaultFarmerFieldOwnerFiltering("dsId.contains=" + DEFAULT_DS_ID, "dsId.contains=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByDsIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where dsId does not contain
        defaultFarmerFieldOwnerFiltering("dsId.doesNotContain=" + UPDATED_DS_ID, "dsId.doesNotContain=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where gnId equals to
        defaultFarmerFieldOwnerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where gnId in
        defaultFarmerFieldOwnerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where gnId is not null
        defaultFarmerFieldOwnerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByGnIdContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where gnId contains
        defaultFarmerFieldOwnerFiltering("gnId.contains=" + DEFAULT_GN_ID, "gnId.contains=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByGnIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where gnId does not contain
        defaultFarmerFieldOwnerFiltering("gnId.doesNotContain=" + UPDATED_GN_ID, "gnId.doesNotContain=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat equals to
        defaultFarmerFieldOwnerFiltering("centerLat.equals=" + DEFAULT_CENTER_LAT, "centerLat.equals=" + UPDATED_CENTER_LAT);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat in
        defaultFarmerFieldOwnerFiltering(
            "centerLat.in=" + DEFAULT_CENTER_LAT + "," + UPDATED_CENTER_LAT,
            "centerLat.in=" + UPDATED_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat is not null
        defaultFarmerFieldOwnerFiltering("centerLat.specified=true", "centerLat.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat is greater than or equal to
        defaultFarmerFieldOwnerFiltering(
            "centerLat.greaterThanOrEqual=" + DEFAULT_CENTER_LAT,
            "centerLat.greaterThanOrEqual=" + UPDATED_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat is less than or equal to
        defaultFarmerFieldOwnerFiltering(
            "centerLat.lessThanOrEqual=" + DEFAULT_CENTER_LAT,
            "centerLat.lessThanOrEqual=" + SMALLER_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat is less than
        defaultFarmerFieldOwnerFiltering("centerLat.lessThan=" + UPDATED_CENTER_LAT, "centerLat.lessThan=" + DEFAULT_CENTER_LAT);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLat is greater than
        defaultFarmerFieldOwnerFiltering("centerLat.greaterThan=" + SMALLER_CENTER_LAT, "centerLat.greaterThan=" + DEFAULT_CENTER_LAT);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng equals to
        defaultFarmerFieldOwnerFiltering("centerLng.equals=" + DEFAULT_CENTER_LNG, "centerLng.equals=" + UPDATED_CENTER_LNG);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng in
        defaultFarmerFieldOwnerFiltering(
            "centerLng.in=" + DEFAULT_CENTER_LNG + "," + UPDATED_CENTER_LNG,
            "centerLng.in=" + UPDATED_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng is not null
        defaultFarmerFieldOwnerFiltering("centerLng.specified=true", "centerLng.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng is greater than or equal to
        defaultFarmerFieldOwnerFiltering(
            "centerLng.greaterThanOrEqual=" + DEFAULT_CENTER_LNG,
            "centerLng.greaterThanOrEqual=" + UPDATED_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng is less than or equal to
        defaultFarmerFieldOwnerFiltering(
            "centerLng.lessThanOrEqual=" + DEFAULT_CENTER_LNG,
            "centerLng.lessThanOrEqual=" + SMALLER_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng is less than
        defaultFarmerFieldOwnerFiltering("centerLng.lessThan=" + UPDATED_CENTER_LNG, "centerLng.lessThan=" + DEFAULT_CENTER_LNG);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCenterLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        // Get all the farmerFieldOwnerList where centerLng is greater than
        defaultFarmerFieldOwnerFiltering("centerLng.greaterThan=" + SMALLER_CENTER_LNG, "centerLng.greaterThan=" + DEFAULT_CENTER_LNG);
    }

    @Test
    @Transactional
    void getAllFarmerFieldOwnersByCropIsEqualToSomething() throws Exception {
        FarmerFieldOwnerCropType crop;
        if (TestUtil.findAll(em, FarmerFieldOwnerCropType.class).isEmpty()) {
            farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);
            crop = FarmerFieldOwnerCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, FarmerFieldOwnerCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        farmerFieldOwner.setCrop(crop);
        farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);
        Long cropId = crop.getId();
        // Get all the farmerFieldOwnerList where crop equals to cropId
        defaultFarmerFieldOwnerShouldBeFound("cropId.equals=" + cropId);

        // Get all the farmerFieldOwnerList where crop equals to (cropId + 1)
        defaultFarmerFieldOwnerShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultFarmerFieldOwnerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFarmerFieldOwnerShouldBeFound(shouldBeFound);
        defaultFarmerFieldOwnerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmerFieldOwnerShouldBeFound(String filter) throws Exception {
        restFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].landPlotName").value(hasItem(DEFAULT_LAND_PLOT_NAME)))
            .andExpect(jsonPath("$.[*].landRegistryNo").value(hasItem(DEFAULT_LAND_REGISTRY_NO)))
            .andExpect(jsonPath("$.[*].totalLandExtent").value(hasItem(DEFAULT_TOTAL_LAND_EXTENT)))
            .andExpect(jsonPath("$.[*].calculatedArea").value(hasItem(DEFAULT_CALCULATED_AREA)))
            .andExpect(jsonPath("$.[*].provinceId").value(hasItem(DEFAULT_PROVINCE_ID)))
            .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID)))
            .andExpect(jsonPath("$.[*].dsId").value(hasItem(DEFAULT_DS_ID)))
            .andExpect(jsonPath("$.[*].gnId").value(hasItem(DEFAULT_GN_ID)))
            .andExpect(jsonPath("$.[*].centerLat").value(hasItem(DEFAULT_CENTER_LAT)))
            .andExpect(jsonPath("$.[*].centerLng").value(hasItem(DEFAULT_CENTER_LNG)));

        // Check, that the count call also returns 1
        restFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmerFieldOwnerShouldNotBeFound(String filter) throws Exception {
        restFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmerFieldOwner() throws Exception {
        // Get the farmerFieldOwner
        restFarmerFieldOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmerFieldOwner() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldOwner
        FarmerFieldOwner updatedFarmerFieldOwner = farmerFieldOwnerRepository.findById(farmerFieldOwner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFarmerFieldOwner are not directly saved in db
        em.detach(updatedFarmerFieldOwner);
        updatedFarmerFieldOwner
            .landPlotName(UPDATED_LAND_PLOT_NAME)
            .landRegistryNo(UPDATED_LAND_REGISTRY_NO)
            .totalLandExtent(UPDATED_TOTAL_LAND_EXTENT)
            .calculatedArea(UPDATED_CALCULATED_AREA)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .centerLat(UPDATED_CENTER_LAT)
            .centerLng(UPDATED_CENTER_LNG);

        restFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFarmerFieldOwner))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmerFieldOwnerToMatchAllProperties(updatedFarmerFieldOwner);
    }

    @Test
    @Transactional
    void putNonExistingFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmerFieldOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmerFieldOwnerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldOwner using partial update
        FarmerFieldOwner partialUpdatedFarmerFieldOwner = new FarmerFieldOwner();
        partialUpdatedFarmerFieldOwner.setId(farmerFieldOwner.getId());

        partialUpdatedFarmerFieldOwner
            .landPlotName(UPDATED_LAND_PLOT_NAME)
            .landRegistryNo(UPDATED_LAND_REGISTRY_NO)
            .calculatedArea(UPDATED_CALCULATED_AREA)
            .provinceId(UPDATED_PROVINCE_ID)
            .centerLat(UPDATED_CENTER_LAT)
            .centerLng(UPDATED_CENTER_LNG);

        restFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldOwner))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldOwner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldOwnerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFarmerFieldOwner, farmerFieldOwner),
            getPersistedFarmerFieldOwner(farmerFieldOwner)
        );
    }

    @Test
    @Transactional
    void fullUpdateFarmerFieldOwnerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldOwner using partial update
        FarmerFieldOwner partialUpdatedFarmerFieldOwner = new FarmerFieldOwner();
        partialUpdatedFarmerFieldOwner.setId(farmerFieldOwner.getId());

        partialUpdatedFarmerFieldOwner
            .landPlotName(UPDATED_LAND_PLOT_NAME)
            .landRegistryNo(UPDATED_LAND_REGISTRY_NO)
            .totalLandExtent(UPDATED_TOTAL_LAND_EXTENT)
            .calculatedArea(UPDATED_CALCULATED_AREA)
            .provinceId(UPDATED_PROVINCE_ID)
            .districtId(UPDATED_DISTRICT_ID)
            .dsId(UPDATED_DS_ID)
            .gnId(UPDATED_GN_ID)
            .centerLat(UPDATED_CENTER_LAT)
            .centerLng(UPDATED_CENTER_LNG);

        restFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldOwner))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldOwner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldOwnerUpdatableFieldsEquals(
            partialUpdatedFarmerFieldOwner,
            getPersistedFarmerFieldOwner(partialUpdatedFarmerFieldOwner)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmerFieldOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldOwner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmerFieldOwner() throws Exception {
        // Initialize the database
        insertedFarmerFieldOwner = farmerFieldOwnerRepository.saveAndFlush(farmerFieldOwner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmerFieldOwner
        restFarmerFieldOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmerFieldOwner.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmerFieldOwnerRepository.count();
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

    protected FarmerFieldOwner getPersistedFarmerFieldOwner(FarmerFieldOwner farmerFieldOwner) {
        return farmerFieldOwnerRepository.findById(farmerFieldOwner.getId()).orElseThrow();
    }

    protected void assertPersistedFarmerFieldOwnerToMatchAllProperties(FarmerFieldOwner expectedFarmerFieldOwner) {
        assertFarmerFieldOwnerAllPropertiesEquals(expectedFarmerFieldOwner, getPersistedFarmerFieldOwner(expectedFarmerFieldOwner));
    }

    protected void assertPersistedFarmerFieldOwnerToMatchUpdatableProperties(FarmerFieldOwner expectedFarmerFieldOwner) {
        assertFarmerFieldOwnerAllUpdatablePropertiesEquals(
            expectedFarmerFieldOwner,
            getPersistedFarmerFieldOwner(expectedFarmerFieldOwner)
        );
    }
}
