package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwnerAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerFieldOwnerRepository;
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
 * Integration tests for the {@link CultivatedLandFarmerFieldOwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandFarmerFieldOwnerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-land-farmer-field-owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandFarmerFieldOwnerMockMvc;

    private CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner;

    private CultivatedLandFarmerFieldOwner insertedCultivatedLandFarmerFieldOwner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandFarmerFieldOwner createEntity() {
        return new CultivatedLandFarmerFieldOwner()
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
    public static CultivatedLandFarmerFieldOwner createUpdatedEntity() {
        return new CultivatedLandFarmerFieldOwner()
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
        cultivatedLandFarmerFieldOwner = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandFarmerFieldOwner != null) {
            cultivatedLandFarmerFieldOwnerRepository.delete(insertedCultivatedLandFarmerFieldOwner);
            insertedCultivatedLandFarmerFieldOwner = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandFarmerFieldOwner
        var returnedCultivatedLandFarmerFieldOwner = om.readValue(
            restCultivatedLandFarmerFieldOwnerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandFarmerFieldOwner.class
        );

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandFarmerFieldOwnerUpdatableFieldsEquals(
            returnedCultivatedLandFarmerFieldOwner,
            getPersistedCultivatedLandFarmerFieldOwner(returnedCultivatedLandFarmerFieldOwner)
        );

        insertedCultivatedLandFarmerFieldOwner = returnedCultivatedLandFarmerFieldOwner;
    }

    @Test
    @Transactional
    void createCultivatedLandFarmerFieldOwnerWithExistingId() throws Exception {
        // Create the CultivatedLandFarmerFieldOwner with an existing ID
        cultivatedLandFarmerFieldOwner.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwners() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmerFieldOwner.getId().intValue())))
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
    void getCultivatedLandFarmerFieldOwner() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get the cultivatedLandFarmerFieldOwner
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandFarmerFieldOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandFarmerFieldOwner.getId().intValue()))
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
    void getCultivatedLandFarmerFieldOwnersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        Long id = cultivatedLandFarmerFieldOwner.getId();

        defaultCultivatedLandFarmerFieldOwnerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandFarmerFieldOwnerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandFarmerFieldOwnerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandPlotNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landPlotName equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landPlotName.equals=" + DEFAULT_LAND_PLOT_NAME,
            "landPlotName.equals=" + UPDATED_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandPlotNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landPlotName in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landPlotName.in=" + DEFAULT_LAND_PLOT_NAME + "," + UPDATED_LAND_PLOT_NAME,
            "landPlotName.in=" + UPDATED_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandPlotNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landPlotName is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("landPlotName.specified=true", "landPlotName.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandPlotNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landPlotName contains
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landPlotName.contains=" + DEFAULT_LAND_PLOT_NAME,
            "landPlotName.contains=" + UPDATED_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandPlotNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landPlotName does not contain
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landPlotName.doesNotContain=" + UPDATED_LAND_PLOT_NAME,
            "landPlotName.doesNotContain=" + DEFAULT_LAND_PLOT_NAME
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandRegistryNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landRegistryNo equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landRegistryNo.equals=" + DEFAULT_LAND_REGISTRY_NO,
            "landRegistryNo.equals=" + UPDATED_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandRegistryNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landRegistryNo in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landRegistryNo.in=" + DEFAULT_LAND_REGISTRY_NO + "," + UPDATED_LAND_REGISTRY_NO,
            "landRegistryNo.in=" + UPDATED_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandRegistryNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landRegistryNo is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("landRegistryNo.specified=true", "landRegistryNo.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandRegistryNoContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landRegistryNo contains
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landRegistryNo.contains=" + DEFAULT_LAND_REGISTRY_NO,
            "landRegistryNo.contains=" + UPDATED_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByLandRegistryNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where landRegistryNo does not contain
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "landRegistryNo.doesNotContain=" + UPDATED_LAND_REGISTRY_NO,
            "landRegistryNo.doesNotContain=" + DEFAULT_LAND_REGISTRY_NO
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "totalLandExtent.equals=" + DEFAULT_TOTAL_LAND_EXTENT,
            "totalLandExtent.equals=" + UPDATED_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "totalLandExtent.in=" + DEFAULT_TOTAL_LAND_EXTENT + "," + UPDATED_TOTAL_LAND_EXTENT,
            "totalLandExtent.in=" + UPDATED_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("totalLandExtent.specified=true", "totalLandExtent.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent is greater than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "totalLandExtent.greaterThanOrEqual=" + DEFAULT_TOTAL_LAND_EXTENT,
            "totalLandExtent.greaterThanOrEqual=" + UPDATED_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent is less than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "totalLandExtent.lessThanOrEqual=" + DEFAULT_TOTAL_LAND_EXTENT,
            "totalLandExtent.lessThanOrEqual=" + SMALLER_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent is less than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "totalLandExtent.lessThan=" + UPDATED_TOTAL_LAND_EXTENT,
            "totalLandExtent.lessThan=" + DEFAULT_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByTotalLandExtentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where totalLandExtent is greater than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "totalLandExtent.greaterThan=" + SMALLER_TOTAL_LAND_EXTENT,
            "totalLandExtent.greaterThan=" + DEFAULT_TOTAL_LAND_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "calculatedArea.equals=" + DEFAULT_CALCULATED_AREA,
            "calculatedArea.equals=" + UPDATED_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "calculatedArea.in=" + DEFAULT_CALCULATED_AREA + "," + UPDATED_CALCULATED_AREA,
            "calculatedArea.in=" + UPDATED_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("calculatedArea.specified=true", "calculatedArea.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea is greater than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "calculatedArea.greaterThanOrEqual=" + DEFAULT_CALCULATED_AREA,
            "calculatedArea.greaterThanOrEqual=" + UPDATED_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea is less than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "calculatedArea.lessThanOrEqual=" + DEFAULT_CALCULATED_AREA,
            "calculatedArea.lessThanOrEqual=" + SMALLER_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea is less than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "calculatedArea.lessThan=" + UPDATED_CALCULATED_AREA,
            "calculatedArea.lessThan=" + DEFAULT_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCalculatedAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where calculatedArea is greater than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "calculatedArea.greaterThan=" + SMALLER_CALCULATED_AREA,
            "calculatedArea.greaterThan=" + DEFAULT_CALCULATED_AREA
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByProvinceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where provinceId equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "provinceId.equals=" + DEFAULT_PROVINCE_ID,
            "provinceId.equals=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByProvinceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where provinceId in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "provinceId.in=" + DEFAULT_PROVINCE_ID + "," + UPDATED_PROVINCE_ID,
            "provinceId.in=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByProvinceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where provinceId is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("provinceId.specified=true", "provinceId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByProvinceIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where provinceId contains
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "provinceId.contains=" + DEFAULT_PROVINCE_ID,
            "provinceId.contains=" + UPDATED_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByProvinceIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where provinceId does not contain
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "provinceId.doesNotContain=" + UPDATED_PROVINCE_ID,
            "provinceId.doesNotContain=" + DEFAULT_PROVINCE_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDistrictIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where districtId equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "districtId.equals=" + DEFAULT_DISTRICT_ID,
            "districtId.equals=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDistrictIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where districtId in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "districtId.in=" + DEFAULT_DISTRICT_ID + "," + UPDATED_DISTRICT_ID,
            "districtId.in=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDistrictIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where districtId is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("districtId.specified=true", "districtId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDistrictIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where districtId contains
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "districtId.contains=" + DEFAULT_DISTRICT_ID,
            "districtId.contains=" + UPDATED_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDistrictIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where districtId does not contain
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "districtId.doesNotContain=" + UPDATED_DISTRICT_ID,
            "districtId.doesNotContain=" + DEFAULT_DISTRICT_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDsIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where dsId equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering("dsId.equals=" + DEFAULT_DS_ID, "dsId.equals=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDsIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where dsId in
        defaultCultivatedLandFarmerFieldOwnerFiltering("dsId.in=" + DEFAULT_DS_ID + "," + UPDATED_DS_ID, "dsId.in=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDsIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where dsId is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("dsId.specified=true", "dsId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDsIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where dsId contains
        defaultCultivatedLandFarmerFieldOwnerFiltering("dsId.contains=" + DEFAULT_DS_ID, "dsId.contains=" + UPDATED_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByDsIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where dsId does not contain
        defaultCultivatedLandFarmerFieldOwnerFiltering("dsId.doesNotContain=" + UPDATED_DS_ID, "dsId.doesNotContain=" + DEFAULT_DS_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByGnIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where gnId equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering("gnId.equals=" + DEFAULT_GN_ID, "gnId.equals=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByGnIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where gnId in
        defaultCultivatedLandFarmerFieldOwnerFiltering("gnId.in=" + DEFAULT_GN_ID + "," + UPDATED_GN_ID, "gnId.in=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByGnIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where gnId is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("gnId.specified=true", "gnId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByGnIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where gnId contains
        defaultCultivatedLandFarmerFieldOwnerFiltering("gnId.contains=" + DEFAULT_GN_ID, "gnId.contains=" + UPDATED_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByGnIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where gnId does not contain
        defaultCultivatedLandFarmerFieldOwnerFiltering("gnId.doesNotContain=" + UPDATED_GN_ID, "gnId.doesNotContain=" + DEFAULT_GN_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering("centerLat.equals=" + DEFAULT_CENTER_LAT, "centerLat.equals=" + UPDATED_CENTER_LAT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLat.in=" + DEFAULT_CENTER_LAT + "," + UPDATED_CENTER_LAT,
            "centerLat.in=" + UPDATED_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("centerLat.specified=true", "centerLat.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat is greater than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLat.greaterThanOrEqual=" + DEFAULT_CENTER_LAT,
            "centerLat.greaterThanOrEqual=" + UPDATED_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat is less than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLat.lessThanOrEqual=" + DEFAULT_CENTER_LAT,
            "centerLat.lessThanOrEqual=" + SMALLER_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat is less than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLat.lessThan=" + UPDATED_CENTER_LAT,
            "centerLat.lessThan=" + DEFAULT_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLat is greater than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLat.greaterThan=" + SMALLER_CENTER_LAT,
            "centerLat.greaterThan=" + DEFAULT_CENTER_LAT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng equals to
        defaultCultivatedLandFarmerFieldOwnerFiltering("centerLng.equals=" + DEFAULT_CENTER_LNG, "centerLng.equals=" + UPDATED_CENTER_LNG);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng in
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLng.in=" + DEFAULT_CENTER_LNG + "," + UPDATED_CENTER_LNG,
            "centerLng.in=" + UPDATED_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng is not null
        defaultCultivatedLandFarmerFieldOwnerFiltering("centerLng.specified=true", "centerLng.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng is greater than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLng.greaterThanOrEqual=" + DEFAULT_CENTER_LNG,
            "centerLng.greaterThanOrEqual=" + UPDATED_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng is less than or equal to
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLng.lessThanOrEqual=" + DEFAULT_CENTER_LNG,
            "centerLng.lessThanOrEqual=" + SMALLER_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng is less than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLng.lessThan=" + UPDATED_CENTER_LNG,
            "centerLng.lessThan=" + DEFAULT_CENTER_LNG
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerFieldOwnersByCenterLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        // Get all the cultivatedLandFarmerFieldOwnerList where centerLng is greater than
        defaultCultivatedLandFarmerFieldOwnerFiltering(
            "centerLng.greaterThan=" + SMALLER_CENTER_LNG,
            "centerLng.greaterThan=" + DEFAULT_CENTER_LNG
        );
    }

    private void defaultCultivatedLandFarmerFieldOwnerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandFarmerFieldOwnerShouldBeFound(shouldBeFound);
        defaultCultivatedLandFarmerFieldOwnerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandFarmerFieldOwnerShouldBeFound(String filter) throws Exception {
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmerFieldOwner.getId().intValue())))
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
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandFarmerFieldOwnerShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandFarmerFieldOwner() throws Exception {
        // Get the cultivatedLandFarmerFieldOwner
        restCultivatedLandFarmerFieldOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandFarmerFieldOwner() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmerFieldOwner
        CultivatedLandFarmerFieldOwner updatedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository
            .findById(cultivatedLandFarmerFieldOwner.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandFarmerFieldOwner are not directly saved in db
        em.detach(updatedCultivatedLandFarmerFieldOwner);
        updatedCultivatedLandFarmerFieldOwner
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

        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandFarmerFieldOwnerToMatchAllProperties(updatedCultivatedLandFarmerFieldOwner);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerFieldOwner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandFarmerFieldOwnerWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmerFieldOwner using partial update
        CultivatedLandFarmerFieldOwner partialUpdatedCultivatedLandFarmerFieldOwner = new CultivatedLandFarmerFieldOwner();
        partialUpdatedCultivatedLandFarmerFieldOwner.setId(cultivatedLandFarmerFieldOwner.getId());

        partialUpdatedCultivatedLandFarmerFieldOwner
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

        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmerFieldOwner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmerFieldOwnerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandFarmerFieldOwner, cultivatedLandFarmerFieldOwner),
            getPersistedCultivatedLandFarmerFieldOwner(cultivatedLandFarmerFieldOwner)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandFarmerFieldOwnerWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmerFieldOwner using partial update
        CultivatedLandFarmerFieldOwner partialUpdatedCultivatedLandFarmerFieldOwner = new CultivatedLandFarmerFieldOwner();
        partialUpdatedCultivatedLandFarmerFieldOwner.setId(cultivatedLandFarmerFieldOwner.getId());

        partialUpdatedCultivatedLandFarmerFieldOwner
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

        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmerFieldOwner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmerFieldOwnerUpdatableFieldsEquals(
            partialUpdatedCultivatedLandFarmerFieldOwner,
            getPersistedCultivatedLandFarmerFieldOwner(partialUpdatedCultivatedLandFarmerFieldOwner)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerFieldOwner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandFarmerFieldOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandFarmerFieldOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerFieldOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmerFieldOwner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmerFieldOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandFarmerFieldOwner() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerFieldOwner = cultivatedLandFarmerFieldOwnerRepository.saveAndFlush(cultivatedLandFarmerFieldOwner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandFarmerFieldOwner
        restCultivatedLandFarmerFieldOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandFarmerFieldOwner.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandFarmerFieldOwnerRepository.count();
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

    protected CultivatedLandFarmerFieldOwner getPersistedCultivatedLandFarmerFieldOwner(
        CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner
    ) {
        return cultivatedLandFarmerFieldOwnerRepository.findById(cultivatedLandFarmerFieldOwner.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandFarmerFieldOwnerToMatchAllProperties(
        CultivatedLandFarmerFieldOwner expectedCultivatedLandFarmerFieldOwner
    ) {
        assertCultivatedLandFarmerFieldOwnerAllPropertiesEquals(
            expectedCultivatedLandFarmerFieldOwner,
            getPersistedCultivatedLandFarmerFieldOwner(expectedCultivatedLandFarmerFieldOwner)
        );
    }

    protected void assertPersistedCultivatedLandFarmerFieldOwnerToMatchUpdatableProperties(
        CultivatedLandFarmerFieldOwner expectedCultivatedLandFarmerFieldOwner
    ) {
        assertCultivatedLandFarmerFieldOwnerAllUpdatablePropertiesEquals(
            expectedCultivatedLandFarmerFieldOwner,
            getPersistedCultivatedLandFarmerFieldOwner(expectedCultivatedLandFarmerFieldOwner)
        );
    }
}
