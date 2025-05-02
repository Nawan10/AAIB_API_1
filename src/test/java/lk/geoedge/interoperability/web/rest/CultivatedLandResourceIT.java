package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLand;
import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import lk.geoedge.interoperability.repository.CultivatedLandRepository;
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
 * Integration tests for the {@link CultivatedLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandRepository cultivatedLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandMockMvc;

    private CultivatedLand cultivatedLand;

    private CultivatedLand insertedCultivatedLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLand createEntity() {
        return new CultivatedLand()
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
    public static CultivatedLand createUpdatedEntity() {
        return new CultivatedLand()
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLand != null) {
            cultivatedLandRepository.delete(insertedCultivatedLand);
            insertedCultivatedLand = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLand
        var returnedCultivatedLand = om.readValue(
            restCultivatedLandMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLand.class
        );

        // Validate the CultivatedLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandUpdatableFieldsEquals(returnedCultivatedLand, getPersistedCultivatedLand(returnedCultivatedLand));

        insertedCultivatedLand = returnedCultivatedLand;
    }

    @Test
    @Transactional
    void createCultivatedLandWithExistingId() throws Exception {
        // Create the CultivatedLand with an existing ID
        cultivatedLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLands() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList
        restCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedLand() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get the cultivatedLand
        restCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLand.getId().intValue()))
            .andExpect(jsonPath("$.landStatus").value(DEFAULT_LAND_STATUS))
            .andExpect(jsonPath("$.urea").value(DEFAULT_UREA))
            .andExpect(jsonPath("$.mop").value(DEFAULT_MOP))
            .andExpect(jsonPath("$.tsp").value(DEFAULT_TSP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        Long id = cultivatedLand.getId();

        defaultCultivatedLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByLandStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where landStatus equals to
        defaultCultivatedLandFiltering("landStatus.equals=" + DEFAULT_LAND_STATUS, "landStatus.equals=" + UPDATED_LAND_STATUS);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByLandStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where landStatus in
        defaultCultivatedLandFiltering(
            "landStatus.in=" + DEFAULT_LAND_STATUS + "," + UPDATED_LAND_STATUS,
            "landStatus.in=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByLandStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where landStatus is not null
        defaultCultivatedLandFiltering("landStatus.specified=true", "landStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByLandStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where landStatus contains
        defaultCultivatedLandFiltering("landStatus.contains=" + DEFAULT_LAND_STATUS, "landStatus.contains=" + UPDATED_LAND_STATUS);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByLandStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where landStatus does not contain
        defaultCultivatedLandFiltering(
            "landStatus.doesNotContain=" + UPDATED_LAND_STATUS,
            "landStatus.doesNotContain=" + DEFAULT_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea equals to
        defaultCultivatedLandFiltering("urea.equals=" + DEFAULT_UREA, "urea.equals=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea in
        defaultCultivatedLandFiltering("urea.in=" + DEFAULT_UREA + "," + UPDATED_UREA, "urea.in=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea is not null
        defaultCultivatedLandFiltering("urea.specified=true", "urea.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea is greater than or equal to
        defaultCultivatedLandFiltering("urea.greaterThanOrEqual=" + DEFAULT_UREA, "urea.greaterThanOrEqual=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea is less than or equal to
        defaultCultivatedLandFiltering("urea.lessThanOrEqual=" + DEFAULT_UREA, "urea.lessThanOrEqual=" + SMALLER_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea is less than
        defaultCultivatedLandFiltering("urea.lessThan=" + UPDATED_UREA, "urea.lessThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByUreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where urea is greater than
        defaultCultivatedLandFiltering("urea.greaterThan=" + SMALLER_UREA, "urea.greaterThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop equals to
        defaultCultivatedLandFiltering("mop.equals=" + DEFAULT_MOP, "mop.equals=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop in
        defaultCultivatedLandFiltering("mop.in=" + DEFAULT_MOP + "," + UPDATED_MOP, "mop.in=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop is not null
        defaultCultivatedLandFiltering("mop.specified=true", "mop.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop is greater than or equal to
        defaultCultivatedLandFiltering("mop.greaterThanOrEqual=" + DEFAULT_MOP, "mop.greaterThanOrEqual=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop is less than or equal to
        defaultCultivatedLandFiltering("mop.lessThanOrEqual=" + DEFAULT_MOP, "mop.lessThanOrEqual=" + SMALLER_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop is less than
        defaultCultivatedLandFiltering("mop.lessThan=" + UPDATED_MOP, "mop.lessThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByMopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where mop is greater than
        defaultCultivatedLandFiltering("mop.greaterThan=" + SMALLER_MOP, "mop.greaterThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp equals to
        defaultCultivatedLandFiltering("tsp.equals=" + DEFAULT_TSP, "tsp.equals=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp in
        defaultCultivatedLandFiltering("tsp.in=" + DEFAULT_TSP + "," + UPDATED_TSP, "tsp.in=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp is not null
        defaultCultivatedLandFiltering("tsp.specified=true", "tsp.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp is greater than or equal to
        defaultCultivatedLandFiltering("tsp.greaterThanOrEqual=" + DEFAULT_TSP, "tsp.greaterThanOrEqual=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp is less than or equal to
        defaultCultivatedLandFiltering("tsp.lessThanOrEqual=" + DEFAULT_TSP, "tsp.lessThanOrEqual=" + SMALLER_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp is less than
        defaultCultivatedLandFiltering("tsp.lessThan=" + UPDATED_TSP, "tsp.lessThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByTspIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where tsp is greater than
        defaultCultivatedLandFiltering("tsp.greaterThan=" + SMALLER_TSP, "tsp.greaterThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt equals to
        defaultCultivatedLandFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt in
        defaultCultivatedLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt is not null
        defaultCultivatedLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt is greater than or equal to
        defaultCultivatedLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt is less than or equal to
        defaultCultivatedLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt is less than
        defaultCultivatedLandFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where createdAt is greater than
        defaultCultivatedLandFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where addedBy equals to
        defaultCultivatedLandFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where addedBy in
        defaultCultivatedLandFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where addedBy is not null
        defaultCultivatedLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where addedBy contains
        defaultCultivatedLandFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        // Get all the cultivatedLandList where addedBy does not contain
        defaultCultivatedLandFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandsBySeasonIsEqualToSomething() throws Exception {
        CultivatedLandSeason season;
        if (TestUtil.findAll(em, CultivatedLandSeason.class).isEmpty()) {
            cultivatedLandRepository.saveAndFlush(cultivatedLand);
            season = CultivatedLandSeasonResourceIT.createEntity();
        } else {
            season = TestUtil.findAll(em, CultivatedLandSeason.class).get(0);
        }
        em.persist(season);
        em.flush();
        cultivatedLand.setSeason(season);
        cultivatedLandRepository.saveAndFlush(cultivatedLand);
        Long seasonId = season.getId();
        // Get all the cultivatedLandList where season equals to seasonId
        defaultCultivatedLandShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the cultivatedLandList where season equals to (seasonId + 1)
        defaultCultivatedLandShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    private void defaultCultivatedLandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandShouldBeFound(shouldBeFound);
        defaultCultivatedLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandShouldBeFound(String filter) throws Exception {
        restCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLand() throws Exception {
        // Get the cultivatedLand
        restCultivatedLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLand() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLand
        CultivatedLand updatedCultivatedLand = cultivatedLandRepository.findById(cultivatedLand.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLand are not directly saved in db
        em.detach(updatedCultivatedLand);
        updatedCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandToMatchAllProperties(updatedCultivatedLand);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedLand)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLand using partial update
        CultivatedLand partialUpdatedCultivatedLand = new CultivatedLand();
        partialUpdatedCultivatedLand.setId(cultivatedLand.getId());

        partialUpdatedCultivatedLand.landStatus(UPDATED_LAND_STATUS).urea(UPDATED_UREA);

        restCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLand, cultivatedLand),
            getPersistedCultivatedLand(cultivatedLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLand using partial update
        CultivatedLand partialUpdatedCultivatedLand = new CultivatedLand();
        partialUpdatedCultivatedLand.setId(cultivatedLand.getId());

        partialUpdatedCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandUpdatableFieldsEquals(partialUpdatedCultivatedLand, getPersistedCultivatedLand(partialUpdatedCultivatedLand));
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLand() throws Exception {
        // Initialize the database
        insertedCultivatedLand = cultivatedLandRepository.saveAndFlush(cultivatedLand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLand
        restCultivatedLandMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandRepository.count();
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

    protected CultivatedLand getPersistedCultivatedLand(CultivatedLand cultivatedLand) {
        return cultivatedLandRepository.findById(cultivatedLand.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandToMatchAllProperties(CultivatedLand expectedCultivatedLand) {
        assertCultivatedLandAllPropertiesEquals(expectedCultivatedLand, getPersistedCultivatedLand(expectedCultivatedLand));
    }

    protected void assertPersistedCultivatedLandToMatchUpdatableProperties(CultivatedLand expectedCultivatedLand) {
        assertCultivatedLandAllUpdatablePropertiesEquals(expectedCultivatedLand, getPersistedCultivatedLand(expectedCultivatedLand));
    }
}
