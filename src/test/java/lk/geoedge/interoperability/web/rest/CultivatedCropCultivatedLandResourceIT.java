package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedCropCultivatedLandAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand;
import lk.geoedge.interoperability.domain.CultivatedCropLandSeason;
import lk.geoedge.interoperability.repository.CultivatedCropCultivatedLandRepository;
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
 * Integration tests for the {@link CultivatedCropCultivatedLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedCropCultivatedLandResourceIT {

    private static final String DEFAULT_LAND_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_LAND_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_UREA = 1D;
    private static final Double UPDATED_UREA = 2D;
    private static final Double SMALLER_UREA = 1D - 1D;

    private static final Double DEFAULT_MOP = 1D;
    private static final Double UPDATED_MOP = 2D;
    private static final Double SMALLER_MOP = 1D - 1D;

    private static final Double DEFAULT_TSP = 1D;
    private static final Double UPDATED_TSP = 2D;
    private static final Double SMALLER_TSP = 1D - 1D;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-crop-cultivated-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedCropCultivatedLandMockMvc;

    private CultivatedCropCultivatedLand cultivatedCropCultivatedLand;

    private CultivatedCropCultivatedLand insertedCultivatedCropCultivatedLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCropCultivatedLand createEntity() {
        return new CultivatedCropCultivatedLand()
            .landStatus(DEFAULT_LAND_STATUS)
            .urea(DEFAULT_UREA)
            .mop(DEFAULT_MOP)
            .tsp(DEFAULT_TSP)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedCropCultivatedLand createUpdatedEntity() {
        return new CultivatedCropCultivatedLand()
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedCropCultivatedLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedCropCultivatedLand != null) {
            cultivatedCropCultivatedLandRepository.delete(insertedCultivatedCropCultivatedLand);
            insertedCultivatedCropCultivatedLand = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedCropCultivatedLand
        var returnedCultivatedCropCultivatedLand = om.readValue(
            restCultivatedCropCultivatedLandMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedCropCultivatedLand.class
        );

        // Validate the CultivatedCropCultivatedLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedCropCultivatedLandUpdatableFieldsEquals(
            returnedCultivatedCropCultivatedLand,
            getPersistedCultivatedCropCultivatedLand(returnedCultivatedCropCultivatedLand)
        );

        insertedCultivatedCropCultivatedLand = returnedCultivatedCropCultivatedLand;
    }

    @Test
    @Transactional
    void createCultivatedCropCultivatedLandWithExistingId() throws Exception {
        // Create the CultivatedCropCultivatedLand with an existing ID
        cultivatedCropCultivatedLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLands() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList
        restCultivatedCropCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCropCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedCropCultivatedLand() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get the cultivatedCropCultivatedLand
        restCultivatedCropCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedCropCultivatedLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedCropCultivatedLand.getId().intValue()))
            .andExpect(jsonPath("$.landStatus").value(DEFAULT_LAND_STATUS))
            .andExpect(jsonPath("$.urea").value(DEFAULT_UREA))
            .andExpect(jsonPath("$.mop").value(DEFAULT_MOP))
            .andExpect(jsonPath("$.tsp").value(DEFAULT_TSP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedCropCultivatedLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        Long id = cultivatedCropCultivatedLand.getId();

        defaultCultivatedCropCultivatedLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedCropCultivatedLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedCropCultivatedLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByLandStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where landStatus equals to
        defaultCultivatedCropCultivatedLandFiltering(
            "landStatus.equals=" + DEFAULT_LAND_STATUS,
            "landStatus.equals=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByLandStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where landStatus in
        defaultCultivatedCropCultivatedLandFiltering(
            "landStatus.in=" + DEFAULT_LAND_STATUS + "," + UPDATED_LAND_STATUS,
            "landStatus.in=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByLandStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where landStatus is not null
        defaultCultivatedCropCultivatedLandFiltering("landStatus.specified=true", "landStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByLandStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where landStatus contains
        defaultCultivatedCropCultivatedLandFiltering(
            "landStatus.contains=" + DEFAULT_LAND_STATUS,
            "landStatus.contains=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByLandStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where landStatus does not contain
        defaultCultivatedCropCultivatedLandFiltering(
            "landStatus.doesNotContain=" + UPDATED_LAND_STATUS,
            "landStatus.doesNotContain=" + DEFAULT_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea equals to
        defaultCultivatedCropCultivatedLandFiltering("urea.equals=" + DEFAULT_UREA, "urea.equals=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea in
        defaultCultivatedCropCultivatedLandFiltering("urea.in=" + DEFAULT_UREA + "," + UPDATED_UREA, "urea.in=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea is not null
        defaultCultivatedCropCultivatedLandFiltering("urea.specified=true", "urea.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea is greater than or equal to
        defaultCultivatedCropCultivatedLandFiltering("urea.greaterThanOrEqual=" + DEFAULT_UREA, "urea.greaterThanOrEqual=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea is less than or equal to
        defaultCultivatedCropCultivatedLandFiltering("urea.lessThanOrEqual=" + DEFAULT_UREA, "urea.lessThanOrEqual=" + SMALLER_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea is less than
        defaultCultivatedCropCultivatedLandFiltering("urea.lessThan=" + UPDATED_UREA, "urea.lessThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByUreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where urea is greater than
        defaultCultivatedCropCultivatedLandFiltering("urea.greaterThan=" + SMALLER_UREA, "urea.greaterThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop equals to
        defaultCultivatedCropCultivatedLandFiltering("mop.equals=" + DEFAULT_MOP, "mop.equals=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop in
        defaultCultivatedCropCultivatedLandFiltering("mop.in=" + DEFAULT_MOP + "," + UPDATED_MOP, "mop.in=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop is not null
        defaultCultivatedCropCultivatedLandFiltering("mop.specified=true", "mop.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop is greater than or equal to
        defaultCultivatedCropCultivatedLandFiltering("mop.greaterThanOrEqual=" + DEFAULT_MOP, "mop.greaterThanOrEqual=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop is less than or equal to
        defaultCultivatedCropCultivatedLandFiltering("mop.lessThanOrEqual=" + DEFAULT_MOP, "mop.lessThanOrEqual=" + SMALLER_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop is less than
        defaultCultivatedCropCultivatedLandFiltering("mop.lessThan=" + UPDATED_MOP, "mop.lessThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByMopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where mop is greater than
        defaultCultivatedCropCultivatedLandFiltering("mop.greaterThan=" + SMALLER_MOP, "mop.greaterThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp equals to
        defaultCultivatedCropCultivatedLandFiltering("tsp.equals=" + DEFAULT_TSP, "tsp.equals=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp in
        defaultCultivatedCropCultivatedLandFiltering("tsp.in=" + DEFAULT_TSP + "," + UPDATED_TSP, "tsp.in=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp is not null
        defaultCultivatedCropCultivatedLandFiltering("tsp.specified=true", "tsp.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp is greater than or equal to
        defaultCultivatedCropCultivatedLandFiltering("tsp.greaterThanOrEqual=" + DEFAULT_TSP, "tsp.greaterThanOrEqual=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp is less than or equal to
        defaultCultivatedCropCultivatedLandFiltering("tsp.lessThanOrEqual=" + DEFAULT_TSP, "tsp.lessThanOrEqual=" + SMALLER_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp is less than
        defaultCultivatedCropCultivatedLandFiltering("tsp.lessThan=" + UPDATED_TSP, "tsp.lessThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByTspIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where tsp is greater than
        defaultCultivatedCropCultivatedLandFiltering("tsp.greaterThan=" + SMALLER_TSP, "tsp.greaterThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt equals to
        defaultCultivatedCropCultivatedLandFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt in
        defaultCultivatedCropCultivatedLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt is not null
        defaultCultivatedCropCultivatedLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt is greater than or equal to
        defaultCultivatedCropCultivatedLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt is less than or equal to
        defaultCultivatedCropCultivatedLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt is less than
        defaultCultivatedCropCultivatedLandFiltering(
            "createdAt.lessThan=" + UPDATED_CREATED_AT,
            "createdAt.lessThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where createdAt is greater than
        defaultCultivatedCropCultivatedLandFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where addedBy equals to
        defaultCultivatedCropCultivatedLandFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where addedBy in
        defaultCultivatedCropCultivatedLandFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where addedBy is not null
        defaultCultivatedCropCultivatedLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where addedBy contains
        defaultCultivatedCropCultivatedLandFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        // Get all the cultivatedCropCultivatedLandList where addedBy does not contain
        defaultCultivatedCropCultivatedLandFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCultivatedCropCultivatedLandsBySeasonIsEqualToSomething() throws Exception {
        CultivatedCropLandSeason season;
        if (TestUtil.findAll(em, CultivatedCropLandSeason.class).isEmpty()) {
            cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);
            season = CultivatedCropLandSeasonResourceIT.createEntity();
        } else {
            season = TestUtil.findAll(em, CultivatedCropLandSeason.class).get(0);
        }
        em.persist(season);
        em.flush();
        cultivatedCropCultivatedLand.setSeason(season);
        cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);
        Long seasonId = season.getId();
        // Get all the cultivatedCropCultivatedLandList where season equals to seasonId
        defaultCultivatedCropCultivatedLandShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the cultivatedCropCultivatedLandList where season equals to (seasonId + 1)
        defaultCultivatedCropCultivatedLandShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    private void defaultCultivatedCropCultivatedLandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedCropCultivatedLandShouldBeFound(shouldBeFound);
        defaultCultivatedCropCultivatedLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedCropCultivatedLandShouldBeFound(String filter) throws Exception {
        restCultivatedCropCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedCropCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedCropCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedCropCultivatedLandShouldNotBeFound(String filter) throws Exception {
        restCultivatedCropCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedCropCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedCropCultivatedLand() throws Exception {
        // Get the cultivatedCropCultivatedLand
        restCultivatedCropCultivatedLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedCropCultivatedLand() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropCultivatedLand
        CultivatedCropCultivatedLand updatedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository
            .findById(cultivatedCropCultivatedLand.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedCropCultivatedLand are not directly saved in db
        em.detach(updatedCultivatedCropCultivatedLand);
        updatedCultivatedCropCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedCropCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedCropCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedCropCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedCropCultivatedLandToMatchAllProperties(updatedCultivatedCropCultivatedLand);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedCropCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedCropCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropCultivatedLand using partial update
        CultivatedCropCultivatedLand partialUpdatedCultivatedCropCultivatedLand = new CultivatedCropCultivatedLand();
        partialUpdatedCultivatedCropCultivatedLand.setId(cultivatedCropCultivatedLand.getId());

        partialUpdatedCultivatedCropCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedCropCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCropCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCropCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropCultivatedLandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedCropCultivatedLand, cultivatedCropCultivatedLand),
            getPersistedCultivatedCropCultivatedLand(cultivatedCropCultivatedLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedCropCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedCropCultivatedLand using partial update
        CultivatedCropCultivatedLand partialUpdatedCultivatedCropCultivatedLand = new CultivatedCropCultivatedLand();
        partialUpdatedCultivatedCropCultivatedLand.setId(cultivatedCropCultivatedLand.getId());

        partialUpdatedCultivatedCropCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedCropCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedCropCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedCropCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedCropCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedCropCultivatedLandUpdatableFieldsEquals(
            partialUpdatedCultivatedCropCultivatedLand,
            getPersistedCultivatedCropCultivatedLand(partialUpdatedCultivatedCropCultivatedLand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedCropCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedCropCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedCropCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedCropCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedCropCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedCropCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedCropCultivatedLand() throws Exception {
        // Initialize the database
        insertedCultivatedCropCultivatedLand = cultivatedCropCultivatedLandRepository.saveAndFlush(cultivatedCropCultivatedLand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedCropCultivatedLand
        restCultivatedCropCultivatedLandMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedCropCultivatedLand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedCropCultivatedLandRepository.count();
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

    protected CultivatedCropCultivatedLand getPersistedCultivatedCropCultivatedLand(
        CultivatedCropCultivatedLand cultivatedCropCultivatedLand
    ) {
        return cultivatedCropCultivatedLandRepository.findById(cultivatedCropCultivatedLand.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedCropCultivatedLandToMatchAllProperties(
        CultivatedCropCultivatedLand expectedCultivatedCropCultivatedLand
    ) {
        assertCultivatedCropCultivatedLandAllPropertiesEquals(
            expectedCultivatedCropCultivatedLand,
            getPersistedCultivatedCropCultivatedLand(expectedCultivatedCropCultivatedLand)
        );
    }

    protected void assertPersistedCultivatedCropCultivatedLandToMatchUpdatableProperties(
        CultivatedCropCultivatedLand expectedCultivatedCropCultivatedLand
    ) {
        assertCultivatedCropCultivatedLandAllUpdatablePropertiesEquals(
            expectedCultivatedCropCultivatedLand,
            getPersistedCultivatedCropCultivatedLand(expectedCultivatedCropCultivatedLand)
        );
    }
}
