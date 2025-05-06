package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLandAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand;
import lk.geoedge.interoperability.repository.IndexPayoutEventListCultivatedLandRepository;
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
 * Integration tests for the {@link IndexPayoutEventListCultivatedLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPayoutEventListCultivatedLandResourceIT {

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

    private static final String ENTITY_API_URL = "/api/index-payout-event-list-cultivated-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPayoutEventListCultivatedLandRepository indexPayoutEventListCultivatedLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPayoutEventListCultivatedLandMockMvc;

    private IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand;

    private IndexPayoutEventListCultivatedLand insertedIndexPayoutEventListCultivatedLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPayoutEventListCultivatedLand createEntity() {
        return new IndexPayoutEventListCultivatedLand()
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
    public static IndexPayoutEventListCultivatedLand createUpdatedEntity() {
        return new IndexPayoutEventListCultivatedLand()
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        indexPayoutEventListCultivatedLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPayoutEventListCultivatedLand != null) {
            indexPayoutEventListCultivatedLandRepository.delete(insertedIndexPayoutEventListCultivatedLand);
            insertedIndexPayoutEventListCultivatedLand = null;
        }
    }

    @Test
    @Transactional
    void createIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPayoutEventListCultivatedLand
        var returnedIndexPayoutEventListCultivatedLand = om.readValue(
            restIndexPayoutEventListCultivatedLandMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPayoutEventListCultivatedLand.class
        );

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPayoutEventListCultivatedLandUpdatableFieldsEquals(
            returnedIndexPayoutEventListCultivatedLand,
            getPersistedIndexPayoutEventListCultivatedLand(returnedIndexPayoutEventListCultivatedLand)
        );

        insertedIndexPayoutEventListCultivatedLand = returnedIndexPayoutEventListCultivatedLand;
    }

    @Test
    @Transactional
    void createIndexPayoutEventListCultivatedLandWithExistingId() throws Exception {
        // Create the IndexPayoutEventListCultivatedLand with an existing ID
        indexPayoutEventListCultivatedLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLands() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventListCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getIndexPayoutEventListCultivatedLand() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get the indexPayoutEventListCultivatedLand
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPayoutEventListCultivatedLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPayoutEventListCultivatedLand.getId().intValue()))
            .andExpect(jsonPath("$.landStatus").value(DEFAULT_LAND_STATUS))
            .andExpect(jsonPath("$.urea").value(DEFAULT_UREA))
            .andExpect(jsonPath("$.mop").value(DEFAULT_MOP))
            .andExpect(jsonPath("$.tsp").value(DEFAULT_TSP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getIndexPayoutEventListCultivatedLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        Long id = indexPayoutEventListCultivatedLand.getId();

        defaultIndexPayoutEventListCultivatedLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPayoutEventListCultivatedLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPayoutEventListCultivatedLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByLandStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where landStatus equals to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "landStatus.equals=" + DEFAULT_LAND_STATUS,
            "landStatus.equals=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByLandStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where landStatus in
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "landStatus.in=" + DEFAULT_LAND_STATUS + "," + UPDATED_LAND_STATUS,
            "landStatus.in=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByLandStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where landStatus is not null
        defaultIndexPayoutEventListCultivatedLandFiltering("landStatus.specified=true", "landStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByLandStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where landStatus contains
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "landStatus.contains=" + DEFAULT_LAND_STATUS,
            "landStatus.contains=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByLandStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where landStatus does not contain
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "landStatus.doesNotContain=" + UPDATED_LAND_STATUS,
            "landStatus.doesNotContain=" + DEFAULT_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea equals to
        defaultIndexPayoutEventListCultivatedLandFiltering("urea.equals=" + DEFAULT_UREA, "urea.equals=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea in
        defaultIndexPayoutEventListCultivatedLandFiltering("urea.in=" + DEFAULT_UREA + "," + UPDATED_UREA, "urea.in=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea is not null
        defaultIndexPayoutEventListCultivatedLandFiltering("urea.specified=true", "urea.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea is greater than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "urea.greaterThanOrEqual=" + DEFAULT_UREA,
            "urea.greaterThanOrEqual=" + UPDATED_UREA
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea is less than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering("urea.lessThanOrEqual=" + DEFAULT_UREA, "urea.lessThanOrEqual=" + SMALLER_UREA);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea is less than
        defaultIndexPayoutEventListCultivatedLandFiltering("urea.lessThan=" + UPDATED_UREA, "urea.lessThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByUreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where urea is greater than
        defaultIndexPayoutEventListCultivatedLandFiltering("urea.greaterThan=" + SMALLER_UREA, "urea.greaterThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop equals to
        defaultIndexPayoutEventListCultivatedLandFiltering("mop.equals=" + DEFAULT_MOP, "mop.equals=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop in
        defaultIndexPayoutEventListCultivatedLandFiltering("mop.in=" + DEFAULT_MOP + "," + UPDATED_MOP, "mop.in=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop is not null
        defaultIndexPayoutEventListCultivatedLandFiltering("mop.specified=true", "mop.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop is greater than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "mop.greaterThanOrEqual=" + DEFAULT_MOP,
            "mop.greaterThanOrEqual=" + UPDATED_MOP
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop is less than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering("mop.lessThanOrEqual=" + DEFAULT_MOP, "mop.lessThanOrEqual=" + SMALLER_MOP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop is less than
        defaultIndexPayoutEventListCultivatedLandFiltering("mop.lessThan=" + UPDATED_MOP, "mop.lessThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByMopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where mop is greater than
        defaultIndexPayoutEventListCultivatedLandFiltering("mop.greaterThan=" + SMALLER_MOP, "mop.greaterThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp equals to
        defaultIndexPayoutEventListCultivatedLandFiltering("tsp.equals=" + DEFAULT_TSP, "tsp.equals=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp in
        defaultIndexPayoutEventListCultivatedLandFiltering("tsp.in=" + DEFAULT_TSP + "," + UPDATED_TSP, "tsp.in=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp is not null
        defaultIndexPayoutEventListCultivatedLandFiltering("tsp.specified=true", "tsp.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp is greater than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "tsp.greaterThanOrEqual=" + DEFAULT_TSP,
            "tsp.greaterThanOrEqual=" + UPDATED_TSP
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp is less than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering("tsp.lessThanOrEqual=" + DEFAULT_TSP, "tsp.lessThanOrEqual=" + SMALLER_TSP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp is less than
        defaultIndexPayoutEventListCultivatedLandFiltering("tsp.lessThan=" + UPDATED_TSP, "tsp.lessThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByTspIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where tsp is greater than
        defaultIndexPayoutEventListCultivatedLandFiltering("tsp.greaterThan=" + SMALLER_TSP, "tsp.greaterThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt equals to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "createdAt.equals=" + DEFAULT_CREATED_AT,
            "createdAt.equals=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt in
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt is not null
        defaultIndexPayoutEventListCultivatedLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt is greater than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt is less than or equal to
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt is less than
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "createdAt.lessThan=" + UPDATED_CREATED_AT,
            "createdAt.lessThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where createdAt is greater than
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where addedBy equals to
        defaultIndexPayoutEventListCultivatedLandFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where addedBy in
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where addedBy is not null
        defaultIndexPayoutEventListCultivatedLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where addedBy contains
        defaultIndexPayoutEventListCultivatedLandFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListCultivatedLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        // Get all the indexPayoutEventListCultivatedLandList where addedBy does not contain
        defaultIndexPayoutEventListCultivatedLandFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    private void defaultIndexPayoutEventListCultivatedLandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPayoutEventListCultivatedLandShouldBeFound(shouldBeFound);
        defaultIndexPayoutEventListCultivatedLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPayoutEventListCultivatedLandShouldBeFound(String filter) throws Exception {
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventListCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPayoutEventListCultivatedLandShouldNotBeFound(String filter) throws Exception {
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPayoutEventListCultivatedLand() throws Exception {
        // Get the indexPayoutEventListCultivatedLand
        restIndexPayoutEventListCultivatedLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPayoutEventListCultivatedLand() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListCultivatedLand
        IndexPayoutEventListCultivatedLand updatedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository
            .findById(indexPayoutEventListCultivatedLand.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPayoutEventListCultivatedLand are not directly saved in db
        em.detach(updatedIndexPayoutEventListCultivatedLand);
        updatedIndexPayoutEventListCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPayoutEventListCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPayoutEventListCultivatedLandToMatchAllProperties(updatedIndexPayoutEventListCultivatedLand);
    }

    @Test
    @Transactional
    void putNonExistingIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPayoutEventListCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPayoutEventListCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListCultivatedLand using partial update
        IndexPayoutEventListCultivatedLand partialUpdatedIndexPayoutEventListCultivatedLand = new IndexPayoutEventListCultivatedLand();
        partialUpdatedIndexPayoutEventListCultivatedLand.setId(indexPayoutEventListCultivatedLand.getId());

        partialUpdatedIndexPayoutEventListCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventListCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListCultivatedLandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPayoutEventListCultivatedLand, indexPayoutEventListCultivatedLand),
            getPersistedIndexPayoutEventListCultivatedLand(indexPayoutEventListCultivatedLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPayoutEventListCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventListCultivatedLand using partial update
        IndexPayoutEventListCultivatedLand partialUpdatedIndexPayoutEventListCultivatedLand = new IndexPayoutEventListCultivatedLand();
        partialUpdatedIndexPayoutEventListCultivatedLand.setId(indexPayoutEventListCultivatedLand.getId());

        partialUpdatedIndexPayoutEventListCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventListCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventListCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListCultivatedLandUpdatableFieldsEquals(
            partialUpdatedIndexPayoutEventListCultivatedLand,
            getPersistedIndexPayoutEventListCultivatedLand(partialUpdatedIndexPayoutEventListCultivatedLand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPayoutEventListCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPayoutEventListCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventListCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventListCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventListCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPayoutEventListCultivatedLand() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandRepository.saveAndFlush(
            indexPayoutEventListCultivatedLand
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPayoutEventListCultivatedLand
        restIndexPayoutEventListCultivatedLandMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPayoutEventListCultivatedLand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPayoutEventListCultivatedLandRepository.count();
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

    protected IndexPayoutEventListCultivatedLand getPersistedIndexPayoutEventListCultivatedLand(
        IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand
    ) {
        return indexPayoutEventListCultivatedLandRepository.findById(indexPayoutEventListCultivatedLand.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPayoutEventListCultivatedLandToMatchAllProperties(
        IndexPayoutEventListCultivatedLand expectedIndexPayoutEventListCultivatedLand
    ) {
        assertIndexPayoutEventListCultivatedLandAllPropertiesEquals(
            expectedIndexPayoutEventListCultivatedLand,
            getPersistedIndexPayoutEventListCultivatedLand(expectedIndexPayoutEventListCultivatedLand)
        );
    }

    protected void assertPersistedIndexPayoutEventListCultivatedLandToMatchUpdatableProperties(
        IndexPayoutEventListCultivatedLand expectedIndexPayoutEventListCultivatedLand
    ) {
        assertIndexPayoutEventListCultivatedLandAllUpdatablePropertiesEquals(
            expectedIndexPayoutEventListCultivatedLand,
            getPersistedIndexPayoutEventListCultivatedLand(expectedIndexPayoutEventListCultivatedLand)
        );
    }
}
