package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmerLandAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandFarmerLand;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerLandRepository;
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
 * Integration tests for the {@link CultivatedLandFarmerLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandFarmerLandResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-land-farmer-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandFarmerLandMockMvc;

    private CultivatedLandFarmerLand cultivatedLandFarmerLand;

    private CultivatedLandFarmerLand insertedCultivatedLandFarmerLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandFarmerLand createEntity() {
        return new CultivatedLandFarmerLand()
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
    public static CultivatedLandFarmerLand createUpdatedEntity() {
        return new CultivatedLandFarmerLand()
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandFarmerLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandFarmerLand != null) {
            cultivatedLandFarmerLandRepository.delete(insertedCultivatedLandFarmerLand);
            insertedCultivatedLandFarmerLand = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandFarmerLand
        var returnedCultivatedLandFarmerLand = om.readValue(
            restCultivatedLandFarmerLandMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandFarmerLand.class
        );

        // Validate the CultivatedLandFarmerLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandFarmerLandUpdatableFieldsEquals(
            returnedCultivatedLandFarmerLand,
            getPersistedCultivatedLandFarmerLand(returnedCultivatedLandFarmerLand)
        );

        insertedCultivatedLandFarmerLand = returnedCultivatedLandFarmerLand;
    }

    @Test
    @Transactional
    void createCultivatedLandFarmerLandWithExistingId() throws Exception {
        // Create the CultivatedLandFarmerLand with an existing ID
        cultivatedLandFarmerLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandFarmerLandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLands() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList
        restCultivatedLandFarmerLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmerLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedLandFarmerLand() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get the cultivatedLandFarmerLand
        restCultivatedLandFarmerLandMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandFarmerLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandFarmerLand.getId().intValue()))
            .andExpect(jsonPath("$.landStatus").value(DEFAULT_LAND_STATUS))
            .andExpect(jsonPath("$.urea").value(DEFAULT_UREA))
            .andExpect(jsonPath("$.mop").value(DEFAULT_MOP))
            .andExpect(jsonPath("$.tsp").value(DEFAULT_TSP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedLandFarmerLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        Long id = cultivatedLandFarmerLand.getId();

        defaultCultivatedLandFarmerLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandFarmerLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandFarmerLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByLandStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where landStatus equals to
        defaultCultivatedLandFarmerLandFiltering("landStatus.equals=" + DEFAULT_LAND_STATUS, "landStatus.equals=" + UPDATED_LAND_STATUS);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByLandStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where landStatus in
        defaultCultivatedLandFarmerLandFiltering(
            "landStatus.in=" + DEFAULT_LAND_STATUS + "," + UPDATED_LAND_STATUS,
            "landStatus.in=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByLandStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where landStatus is not null
        defaultCultivatedLandFarmerLandFiltering("landStatus.specified=true", "landStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByLandStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where landStatus contains
        defaultCultivatedLandFarmerLandFiltering(
            "landStatus.contains=" + DEFAULT_LAND_STATUS,
            "landStatus.contains=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByLandStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where landStatus does not contain
        defaultCultivatedLandFarmerLandFiltering(
            "landStatus.doesNotContain=" + UPDATED_LAND_STATUS,
            "landStatus.doesNotContain=" + DEFAULT_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea equals to
        defaultCultivatedLandFarmerLandFiltering("urea.equals=" + DEFAULT_UREA, "urea.equals=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea in
        defaultCultivatedLandFarmerLandFiltering("urea.in=" + DEFAULT_UREA + "," + UPDATED_UREA, "urea.in=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea is not null
        defaultCultivatedLandFarmerLandFiltering("urea.specified=true", "urea.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea is greater than or equal to
        defaultCultivatedLandFarmerLandFiltering("urea.greaterThanOrEqual=" + DEFAULT_UREA, "urea.greaterThanOrEqual=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea is less than or equal to
        defaultCultivatedLandFarmerLandFiltering("urea.lessThanOrEqual=" + DEFAULT_UREA, "urea.lessThanOrEqual=" + SMALLER_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea is less than
        defaultCultivatedLandFarmerLandFiltering("urea.lessThan=" + UPDATED_UREA, "urea.lessThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByUreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where urea is greater than
        defaultCultivatedLandFarmerLandFiltering("urea.greaterThan=" + SMALLER_UREA, "urea.greaterThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop equals to
        defaultCultivatedLandFarmerLandFiltering("mop.equals=" + DEFAULT_MOP, "mop.equals=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop in
        defaultCultivatedLandFarmerLandFiltering("mop.in=" + DEFAULT_MOP + "," + UPDATED_MOP, "mop.in=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop is not null
        defaultCultivatedLandFarmerLandFiltering("mop.specified=true", "mop.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop is greater than or equal to
        defaultCultivatedLandFarmerLandFiltering("mop.greaterThanOrEqual=" + DEFAULT_MOP, "mop.greaterThanOrEqual=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop is less than or equal to
        defaultCultivatedLandFarmerLandFiltering("mop.lessThanOrEqual=" + DEFAULT_MOP, "mop.lessThanOrEqual=" + SMALLER_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop is less than
        defaultCultivatedLandFarmerLandFiltering("mop.lessThan=" + UPDATED_MOP, "mop.lessThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByMopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where mop is greater than
        defaultCultivatedLandFarmerLandFiltering("mop.greaterThan=" + SMALLER_MOP, "mop.greaterThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp equals to
        defaultCultivatedLandFarmerLandFiltering("tsp.equals=" + DEFAULT_TSP, "tsp.equals=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp in
        defaultCultivatedLandFarmerLandFiltering("tsp.in=" + DEFAULT_TSP + "," + UPDATED_TSP, "tsp.in=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp is not null
        defaultCultivatedLandFarmerLandFiltering("tsp.specified=true", "tsp.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp is greater than or equal to
        defaultCultivatedLandFarmerLandFiltering("tsp.greaterThanOrEqual=" + DEFAULT_TSP, "tsp.greaterThanOrEqual=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp is less than or equal to
        defaultCultivatedLandFarmerLandFiltering("tsp.lessThanOrEqual=" + DEFAULT_TSP, "tsp.lessThanOrEqual=" + SMALLER_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp is less than
        defaultCultivatedLandFarmerLandFiltering("tsp.lessThan=" + UPDATED_TSP, "tsp.lessThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByTspIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where tsp is greater than
        defaultCultivatedLandFarmerLandFiltering("tsp.greaterThan=" + SMALLER_TSP, "tsp.greaterThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt equals to
        defaultCultivatedLandFarmerLandFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt in
        defaultCultivatedLandFarmerLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt is not null
        defaultCultivatedLandFarmerLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt is greater than or equal to
        defaultCultivatedLandFarmerLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt is less than or equal to
        defaultCultivatedLandFarmerLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt is less than
        defaultCultivatedLandFarmerLandFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where createdAt is greater than
        defaultCultivatedLandFarmerLandFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where addedBy equals to
        defaultCultivatedLandFarmerLandFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where addedBy in
        defaultCultivatedLandFarmerLandFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where addedBy is not null
        defaultCultivatedLandFarmerLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where addedBy contains
        defaultCultivatedLandFarmerLandFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmerLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        // Get all the cultivatedLandFarmerLandList where addedBy does not contain
        defaultCultivatedLandFarmerLandFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    private void defaultCultivatedLandFarmerLandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandFarmerLandShouldBeFound(shouldBeFound);
        defaultCultivatedLandFarmerLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandFarmerLandShouldBeFound(String filter) throws Exception {
        restCultivatedLandFarmerLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmerLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedLandFarmerLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandFarmerLandShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandFarmerLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandFarmerLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandFarmerLand() throws Exception {
        // Get the cultivatedLandFarmerLand
        restCultivatedLandFarmerLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandFarmerLand() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmerLand
        CultivatedLandFarmerLand updatedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository
            .findById(cultivatedLandFarmerLand.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandFarmerLand are not directly saved in db
        em.detach(updatedCultivatedLandFarmerLand);
        updatedCultivatedLandFarmerLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandFarmerLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandFarmerLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandFarmerLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandFarmerLandToMatchAllProperties(updatedCultivatedLandFarmerLand);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandFarmerLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerLandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandFarmerLandWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmerLand using partial update
        CultivatedLandFarmerLand partialUpdatedCultivatedLandFarmerLand = new CultivatedLandFarmerLand();
        partialUpdatedCultivatedLandFarmerLand.setId(cultivatedLandFarmerLand.getId());

        partialUpdatedCultivatedLandFarmerLand.mop(UPDATED_MOP).addedBy(UPDATED_ADDED_BY);

        restCultivatedLandFarmerLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmerLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmerLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmerLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmerLandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandFarmerLand, cultivatedLandFarmerLand),
            getPersistedCultivatedLandFarmerLand(cultivatedLandFarmerLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandFarmerLandWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmerLand using partial update
        CultivatedLandFarmerLand partialUpdatedCultivatedLandFarmerLand = new CultivatedLandFarmerLand();
        partialUpdatedCultivatedLandFarmerLand.setId(cultivatedLandFarmerLand.getId());

        partialUpdatedCultivatedLandFarmerLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandFarmerLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmerLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmerLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmerLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmerLandUpdatableFieldsEquals(
            partialUpdatedCultivatedLandFarmerLand,
            getPersistedCultivatedLandFarmerLand(partialUpdatedCultivatedLandFarmerLand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandFarmerLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandFarmerLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmerLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmerLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmerLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmerLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandFarmerLand() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmerLand = cultivatedLandFarmerLandRepository.saveAndFlush(cultivatedLandFarmerLand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandFarmerLand
        restCultivatedLandFarmerLandMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandFarmerLand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandFarmerLandRepository.count();
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

    protected CultivatedLandFarmerLand getPersistedCultivatedLandFarmerLand(CultivatedLandFarmerLand cultivatedLandFarmerLand) {
        return cultivatedLandFarmerLandRepository.findById(cultivatedLandFarmerLand.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandFarmerLandToMatchAllProperties(CultivatedLandFarmerLand expectedCultivatedLandFarmerLand) {
        assertCultivatedLandFarmerLandAllPropertiesEquals(
            expectedCultivatedLandFarmerLand,
            getPersistedCultivatedLandFarmerLand(expectedCultivatedLandFarmerLand)
        );
    }

    protected void assertPersistedCultivatedLandFarmerLandToMatchUpdatableProperties(
        CultivatedLandFarmerLand expectedCultivatedLandFarmerLand
    ) {
        assertCultivatedLandFarmerLandAllUpdatablePropertiesEquals(
            expectedCultivatedLandFarmerLand,
            getPersistedCultivatedLandFarmerLand(expectedCultivatedLandFarmerLand)
        );
    }
}
