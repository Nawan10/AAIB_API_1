package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLandAsserts.*;
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
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCultivatedLandRepository;
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
 * Integration tests for the {@link InsuranceCultivatedLandCultivatedLandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCultivatedLandCultivatedLandResourceIT {

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

    private static final String ENTITY_API_URL = "/api/insurance-cultivated-land-cultivated-lands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCultivatedLandCultivatedLandRepository insuranceCultivatedLandCultivatedLandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCultivatedLandCultivatedLandMockMvc;

    private InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand;

    private InsuranceCultivatedLandCultivatedLand insertedInsuranceCultivatedLandCultivatedLand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandCultivatedLand createEntity() {
        return new InsuranceCultivatedLandCultivatedLand()
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
    public static InsuranceCultivatedLandCultivatedLand createUpdatedEntity() {
        return new InsuranceCultivatedLandCultivatedLand()
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        insuranceCultivatedLandCultivatedLand = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCultivatedLandCultivatedLand != null) {
            insuranceCultivatedLandCultivatedLandRepository.delete(insertedInsuranceCultivatedLandCultivatedLand);
            insertedInsuranceCultivatedLandCultivatedLand = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCultivatedLandCultivatedLand
        var returnedInsuranceCultivatedLandCultivatedLand = om.readValue(
            restInsuranceCultivatedLandCultivatedLandMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCultivatedLandCultivatedLand.class
        );

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCultivatedLandCultivatedLandUpdatableFieldsEquals(
            returnedInsuranceCultivatedLandCultivatedLand,
            getPersistedInsuranceCultivatedLandCultivatedLand(returnedInsuranceCultivatedLandCultivatedLand)
        );

        insertedInsuranceCultivatedLandCultivatedLand = returnedInsuranceCultivatedLandCultivatedLand;
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCultivatedLandWithExistingId() throws Exception {
        // Create the InsuranceCultivatedLandCultivatedLand with an existing ID
        insuranceCultivatedLandCultivatedLand.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLands() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get the insuranceCultivatedLandCultivatedLand
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCultivatedLandCultivatedLand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCultivatedLandCultivatedLand.getId().intValue()))
            .andExpect(jsonPath("$.landStatus").value(DEFAULT_LAND_STATUS))
            .andExpect(jsonPath("$.urea").value(DEFAULT_UREA))
            .andExpect(jsonPath("$.mop").value(DEFAULT_MOP))
            .andExpect(jsonPath("$.tsp").value(DEFAULT_TSP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandCultivatedLandsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        Long id = insuranceCultivatedLandCultivatedLand.getId();

        defaultInsuranceCultivatedLandCultivatedLandFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCultivatedLandCultivatedLandFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCultivatedLandCultivatedLandFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByLandStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where landStatus equals to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "landStatus.equals=" + DEFAULT_LAND_STATUS,
            "landStatus.equals=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByLandStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where landStatus in
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "landStatus.in=" + DEFAULT_LAND_STATUS + "," + UPDATED_LAND_STATUS,
            "landStatus.in=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByLandStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where landStatus is not null
        defaultInsuranceCultivatedLandCultivatedLandFiltering("landStatus.specified=true", "landStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByLandStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where landStatus contains
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "landStatus.contains=" + DEFAULT_LAND_STATUS,
            "landStatus.contains=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByLandStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where landStatus does not contain
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "landStatus.doesNotContain=" + UPDATED_LAND_STATUS,
            "landStatus.doesNotContain=" + DEFAULT_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea equals to
        defaultInsuranceCultivatedLandCultivatedLandFiltering("urea.equals=" + DEFAULT_UREA, "urea.equals=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea in
        defaultInsuranceCultivatedLandCultivatedLandFiltering("urea.in=" + DEFAULT_UREA + "," + UPDATED_UREA, "urea.in=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea is not null
        defaultInsuranceCultivatedLandCultivatedLandFiltering("urea.specified=true", "urea.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea is greater than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "urea.greaterThanOrEqual=" + DEFAULT_UREA,
            "urea.greaterThanOrEqual=" + UPDATED_UREA
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea is less than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "urea.lessThanOrEqual=" + DEFAULT_UREA,
            "urea.lessThanOrEqual=" + SMALLER_UREA
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea is less than
        defaultInsuranceCultivatedLandCultivatedLandFiltering("urea.lessThan=" + UPDATED_UREA, "urea.lessThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByUreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where urea is greater than
        defaultInsuranceCultivatedLandCultivatedLandFiltering("urea.greaterThan=" + SMALLER_UREA, "urea.greaterThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop equals to
        defaultInsuranceCultivatedLandCultivatedLandFiltering("mop.equals=" + DEFAULT_MOP, "mop.equals=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop in
        defaultInsuranceCultivatedLandCultivatedLandFiltering("mop.in=" + DEFAULT_MOP + "," + UPDATED_MOP, "mop.in=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop is not null
        defaultInsuranceCultivatedLandCultivatedLandFiltering("mop.specified=true", "mop.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop is greater than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "mop.greaterThanOrEqual=" + DEFAULT_MOP,
            "mop.greaterThanOrEqual=" + UPDATED_MOP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop is less than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering("mop.lessThanOrEqual=" + DEFAULT_MOP, "mop.lessThanOrEqual=" + SMALLER_MOP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop is less than
        defaultInsuranceCultivatedLandCultivatedLandFiltering("mop.lessThan=" + UPDATED_MOP, "mop.lessThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByMopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where mop is greater than
        defaultInsuranceCultivatedLandCultivatedLandFiltering("mop.greaterThan=" + SMALLER_MOP, "mop.greaterThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp equals to
        defaultInsuranceCultivatedLandCultivatedLandFiltering("tsp.equals=" + DEFAULT_TSP, "tsp.equals=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp in
        defaultInsuranceCultivatedLandCultivatedLandFiltering("tsp.in=" + DEFAULT_TSP + "," + UPDATED_TSP, "tsp.in=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp is not null
        defaultInsuranceCultivatedLandCultivatedLandFiltering("tsp.specified=true", "tsp.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp is greater than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "tsp.greaterThanOrEqual=" + DEFAULT_TSP,
            "tsp.greaterThanOrEqual=" + UPDATED_TSP
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp is less than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering("tsp.lessThanOrEqual=" + DEFAULT_TSP, "tsp.lessThanOrEqual=" + SMALLER_TSP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp is less than
        defaultInsuranceCultivatedLandCultivatedLandFiltering("tsp.lessThan=" + UPDATED_TSP, "tsp.lessThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByTspIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where tsp is greater than
        defaultInsuranceCultivatedLandCultivatedLandFiltering("tsp.greaterThan=" + SMALLER_TSP, "tsp.greaterThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt equals to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "createdAt.equals=" + DEFAULT_CREATED_AT,
            "createdAt.equals=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt in
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt is not null
        defaultInsuranceCultivatedLandCultivatedLandFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt is greater than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt is less than or equal to
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt is less than
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "createdAt.lessThan=" + UPDATED_CREATED_AT,
            "createdAt.lessThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where createdAt is greater than
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where addedBy equals to
        defaultInsuranceCultivatedLandCultivatedLandFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where addedBy in
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where addedBy is not null
        defaultInsuranceCultivatedLandCultivatedLandFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where addedBy contains
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "addedBy.contains=" + DEFAULT_ADDED_BY,
            "addedBy.contains=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCultivatedLandsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        // Get all the insuranceCultivatedLandCultivatedLandList where addedBy does not contain
        defaultInsuranceCultivatedLandCultivatedLandFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    private void defaultInsuranceCultivatedLandCultivatedLandFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCultivatedLandCultivatedLandShouldBeFound(shouldBeFound);
        defaultInsuranceCultivatedLandCultivatedLandShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCultivatedLandCultivatedLandShouldBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCultivatedLand.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCultivatedLandCultivatedLandShouldNotBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCultivatedLandCultivatedLand() throws Exception {
        // Get the insuranceCultivatedLandCultivatedLand
        restInsuranceCultivatedLandCultivatedLandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCultivatedLandCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCultivatedLand
        InsuranceCultivatedLandCultivatedLand updatedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository
            .findById(insuranceCultivatedLandCultivatedLand.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCultivatedLandCultivatedLand are not directly saved in db
        em.detach(updatedInsuranceCultivatedLandCultivatedLand);
        updatedInsuranceCultivatedLandCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCultivatedLandCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCultivatedLandCultivatedLandToMatchAllProperties(updatedInsuranceCultivatedLandCultivatedLand);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCultivatedLandCultivatedLand.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCultivatedLandCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCultivatedLand using partial update
        InsuranceCultivatedLandCultivatedLand partialUpdatedInsuranceCultivatedLandCultivatedLand =
            new InsuranceCultivatedLandCultivatedLand();
        partialUpdatedInsuranceCultivatedLandCultivatedLand.setId(insuranceCultivatedLandCultivatedLand.getId());

        partialUpdatedInsuranceCultivatedLandCultivatedLand.tsp(UPDATED_TSP);

        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCultivatedLandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCultivatedLandCultivatedLand, insuranceCultivatedLandCultivatedLand),
            getPersistedInsuranceCultivatedLandCultivatedLand(insuranceCultivatedLandCultivatedLand)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCultivatedLandCultivatedLandWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCultivatedLand using partial update
        InsuranceCultivatedLandCultivatedLand partialUpdatedInsuranceCultivatedLandCultivatedLand =
            new InsuranceCultivatedLandCultivatedLand();
        partialUpdatedInsuranceCultivatedLandCultivatedLand.setId(insuranceCultivatedLandCultivatedLand.getId());

        partialUpdatedInsuranceCultivatedLandCultivatedLand
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCultivatedLandUpdatableFieldsEquals(
            partialUpdatedInsuranceCultivatedLandCultivatedLand,
            getPersistedInsuranceCultivatedLandCultivatedLand(partialUpdatedInsuranceCultivatedLandCultivatedLand)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCultivatedLand.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCultivatedLandCultivatedLand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCultivatedLandCultivatedLand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCultivatedLand.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCultivatedLand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCultivatedLand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCultivatedLandCultivatedLand() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandRepository.saveAndFlush(
            insuranceCultivatedLandCultivatedLand
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCultivatedLandCultivatedLand
        restInsuranceCultivatedLandCultivatedLandMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, insuranceCultivatedLandCultivatedLand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCultivatedLandCultivatedLandRepository.count();
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

    protected InsuranceCultivatedLandCultivatedLand getPersistedInsuranceCultivatedLandCultivatedLand(
        InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand
    ) {
        return insuranceCultivatedLandCultivatedLandRepository.findById(insuranceCultivatedLandCultivatedLand.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCultivatedLandCultivatedLandToMatchAllProperties(
        InsuranceCultivatedLandCultivatedLand expectedInsuranceCultivatedLandCultivatedLand
    ) {
        assertInsuranceCultivatedLandCultivatedLandAllPropertiesEquals(
            expectedInsuranceCultivatedLandCultivatedLand,
            getPersistedInsuranceCultivatedLandCultivatedLand(expectedInsuranceCultivatedLandCultivatedLand)
        );
    }

    protected void assertPersistedInsuranceCultivatedLandCultivatedLandToMatchUpdatableProperties(
        InsuranceCultivatedLandCultivatedLand expectedInsuranceCultivatedLandCultivatedLand
    ) {
        assertInsuranceCultivatedLandCultivatedLandAllUpdatablePropertiesEquals(
            expectedInsuranceCultivatedLandCultivatedLand,
            getPersistedInsuranceCultivatedLandCultivatedLand(expectedInsuranceCultivatedLandCultivatedLand)
        );
    }
}
