package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexCoverages;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesRepository;
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
 * Integration tests for the {@link InsuranceCultivatedLandCoveragesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCultivatedLandCoveragesResourceIT {

    private static final Double DEFAULT_CONVERAGE_AMOUNT = 1D;
    private static final Double UPDATED_CONVERAGE_AMOUNT = 2D;
    private static final Double SMALLER_CONVERAGE_AMOUNT = 1D - 1D;

    private static final Boolean DEFAULT_IS_SELECT = false;
    private static final Boolean UPDATED_IS_SELECT = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurance-cultivated-land-coverages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCultivatedLandCoveragesMockMvc;

    private InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages;

    private InsuranceCultivatedLandCoverages insertedInsuranceCultivatedLandCoverages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandCoverages createEntity() {
        return new InsuranceCultivatedLandCoverages()
            .converageAmount(DEFAULT_CONVERAGE_AMOUNT)
            .isSelect(DEFAULT_IS_SELECT)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCultivatedLandCoverages createUpdatedEntity() {
        return new InsuranceCultivatedLandCoverages()
            .converageAmount(UPDATED_CONVERAGE_AMOUNT)
            .isSelect(UPDATED_IS_SELECT)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        insuranceCultivatedLandCoverages = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCultivatedLandCoverages != null) {
            insuranceCultivatedLandCoveragesRepository.delete(insertedInsuranceCultivatedLandCoverages);
            insertedInsuranceCultivatedLandCoverages = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCultivatedLandCoverages
        var returnedInsuranceCultivatedLandCoverages = om.readValue(
            restInsuranceCultivatedLandCoveragesMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCultivatedLandCoverages.class
        );

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCultivatedLandCoveragesUpdatableFieldsEquals(
            returnedInsuranceCultivatedLandCoverages,
            getPersistedInsuranceCultivatedLandCoverages(returnedInsuranceCultivatedLandCoverages)
        );

        insertedInsuranceCultivatedLandCoverages = returnedInsuranceCultivatedLandCoverages;
    }

    @Test
    @Transactional
    void createInsuranceCultivatedLandCoveragesWithExistingId() throws Exception {
        // Create the InsuranceCultivatedLandCoverages with an existing ID
        insuranceCultivatedLandCoverages.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoverages() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCoverages.getId().intValue())))
            .andExpect(jsonPath("$.[*].converageAmount").value(hasItem(DEFAULT_CONVERAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].isSelect").value(hasItem(DEFAULT_IS_SELECT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandCoverages() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get the insuranceCultivatedLandCoverages
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCultivatedLandCoverages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCultivatedLandCoverages.getId().intValue()))
            .andExpect(jsonPath("$.converageAmount").value(DEFAULT_CONVERAGE_AMOUNT))
            .andExpect(jsonPath("$.isSelect").value(DEFAULT_IS_SELECT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getInsuranceCultivatedLandCoveragesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        Long id = insuranceCultivatedLandCoverages.getId();

        defaultInsuranceCultivatedLandCoveragesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCultivatedLandCoveragesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCultivatedLandCoveragesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount equals to
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "converageAmount.equals=" + DEFAULT_CONVERAGE_AMOUNT,
            "converageAmount.equals=" + UPDATED_CONVERAGE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount in
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "converageAmount.in=" + DEFAULT_CONVERAGE_AMOUNT + "," + UPDATED_CONVERAGE_AMOUNT,
            "converageAmount.in=" + UPDATED_CONVERAGE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount is not null
        defaultInsuranceCultivatedLandCoveragesFiltering("converageAmount.specified=true", "converageAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount is greater than or equal to
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "converageAmount.greaterThanOrEqual=" + DEFAULT_CONVERAGE_AMOUNT,
            "converageAmount.greaterThanOrEqual=" + UPDATED_CONVERAGE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount is less than or equal to
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "converageAmount.lessThanOrEqual=" + DEFAULT_CONVERAGE_AMOUNT,
            "converageAmount.lessThanOrEqual=" + SMALLER_CONVERAGE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount is less than
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "converageAmount.lessThan=" + UPDATED_CONVERAGE_AMOUNT,
            "converageAmount.lessThan=" + DEFAULT_CONVERAGE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByConverageAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where converageAmount is greater than
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "converageAmount.greaterThan=" + SMALLER_CONVERAGE_AMOUNT,
            "converageAmount.greaterThan=" + DEFAULT_CONVERAGE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByIsSelectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where isSelect equals to
        defaultInsuranceCultivatedLandCoveragesFiltering("isSelect.equals=" + DEFAULT_IS_SELECT, "isSelect.equals=" + UPDATED_IS_SELECT);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByIsSelectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where isSelect in
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "isSelect.in=" + DEFAULT_IS_SELECT + "," + UPDATED_IS_SELECT,
            "isSelect.in=" + UPDATED_IS_SELECT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByIsSelectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where isSelect is not null
        defaultInsuranceCultivatedLandCoveragesFiltering("isSelect.specified=true", "isSelect.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where createdAt equals to
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "createdAt.equals=" + DEFAULT_CREATED_AT,
            "createdAt.equals=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where createdAt in
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where createdAt is not null
        defaultInsuranceCultivatedLandCoveragesFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where addedBy equals to
        defaultInsuranceCultivatedLandCoveragesFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where addedBy in
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where addedBy is not null
        defaultInsuranceCultivatedLandCoveragesFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where addedBy contains
        defaultInsuranceCultivatedLandCoveragesFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        // Get all the insuranceCultivatedLandCoveragesList where addedBy does not contain
        defaultInsuranceCultivatedLandCoveragesFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByInsuranceCultivatedLandIsEqualToSomething() throws Exception {
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLand;
        if (TestUtil.findAll(em, InsuranceCultivatedLandCoveragesInsuranceCultivatedLand.class).isEmpty()) {
            insuranceCultivatedLandCoveragesRepository.saveAndFlush(insuranceCultivatedLandCoverages);
            insuranceCultivatedLand = InsuranceCultivatedLandCoveragesInsuranceCultivatedLandResourceIT.createEntity();
        } else {
            insuranceCultivatedLand = TestUtil.findAll(em, InsuranceCultivatedLandCoveragesInsuranceCultivatedLand.class).get(0);
        }
        em.persist(insuranceCultivatedLand);
        em.flush();
        insuranceCultivatedLandCoverages.setInsuranceCultivatedLand(insuranceCultivatedLand);
        insuranceCultivatedLandCoveragesRepository.saveAndFlush(insuranceCultivatedLandCoverages);
        Long insuranceCultivatedLandId = insuranceCultivatedLand.getId();
        // Get all the insuranceCultivatedLandCoveragesList where insuranceCultivatedLand equals to insuranceCultivatedLandId
        defaultInsuranceCultivatedLandCoveragesShouldBeFound("insuranceCultivatedLandId.equals=" + insuranceCultivatedLandId);

        // Get all the insuranceCultivatedLandCoveragesList where insuranceCultivatedLand equals to (insuranceCultivatedLandId + 1)
        defaultInsuranceCultivatedLandCoveragesShouldNotBeFound("insuranceCultivatedLandId.equals=" + (insuranceCultivatedLandId + 1));
    }

    @Test
    @Transactional
    void getAllInsuranceCultivatedLandCoveragesByIndexCoverageIsEqualToSomething() throws Exception {
        IndexCoverages indexCoverage;
        if (TestUtil.findAll(em, IndexCoverages.class).isEmpty()) {
            insuranceCultivatedLandCoveragesRepository.saveAndFlush(insuranceCultivatedLandCoverages);
            indexCoverage = IndexCoveragesResourceIT.createEntity();
        } else {
            indexCoverage = TestUtil.findAll(em, IndexCoverages.class).get(0);
        }
        em.persist(indexCoverage);
        em.flush();
        insuranceCultivatedLandCoverages.setIndexCoverage(indexCoverage);
        insuranceCultivatedLandCoveragesRepository.saveAndFlush(insuranceCultivatedLandCoverages);
        Long indexCoverageId = indexCoverage.getId();
        // Get all the insuranceCultivatedLandCoveragesList where indexCoverage equals to indexCoverageId
        defaultInsuranceCultivatedLandCoveragesShouldBeFound("indexCoverageId.equals=" + indexCoverageId);

        // Get all the insuranceCultivatedLandCoveragesList where indexCoverage equals to (indexCoverageId + 1)
        defaultInsuranceCultivatedLandCoveragesShouldNotBeFound("indexCoverageId.equals=" + (indexCoverageId + 1));
    }

    private void defaultInsuranceCultivatedLandCoveragesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCultivatedLandCoveragesShouldBeFound(shouldBeFound);
        defaultInsuranceCultivatedLandCoveragesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCultivatedLandCoveragesShouldBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCultivatedLandCoverages.getId().intValue())))
            .andExpect(jsonPath("$.[*].converageAmount").value(hasItem(DEFAULT_CONVERAGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].isSelect").value(hasItem(DEFAULT_IS_SELECT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCultivatedLandCoveragesShouldNotBeFound(String filter) throws Exception {
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCultivatedLandCoverages() throws Exception {
        // Get the insuranceCultivatedLandCoverages
        restInsuranceCultivatedLandCoveragesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCultivatedLandCoverages() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCoverages
        InsuranceCultivatedLandCoverages updatedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository
            .findById(insuranceCultivatedLandCoverages.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCultivatedLandCoverages are not directly saved in db
        em.detach(updatedInsuranceCultivatedLandCoverages);
        updatedInsuranceCultivatedLandCoverages
            .converageAmount(UPDATED_CONVERAGE_AMOUNT)
            .isSelect(UPDATED_IS_SELECT)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCultivatedLandCoverages.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCultivatedLandCoverages))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCultivatedLandCoveragesToMatchAllProperties(updatedInsuranceCultivatedLandCoverages);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoverages.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCultivatedLandCoverages.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCultivatedLandCoveragesWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCoverages using partial update
        InsuranceCultivatedLandCoverages partialUpdatedInsuranceCultivatedLandCoverages = new InsuranceCultivatedLandCoverages();
        partialUpdatedInsuranceCultivatedLandCoverages.setId(insuranceCultivatedLandCoverages.getId());

        partialUpdatedInsuranceCultivatedLandCoverages.converageAmount(UPDATED_CONVERAGE_AMOUNT).addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCoverages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCoverages))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCoverages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCoveragesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCultivatedLandCoverages, insuranceCultivatedLandCoverages),
            getPersistedInsuranceCultivatedLandCoverages(insuranceCultivatedLandCoverages)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCultivatedLandCoveragesWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCultivatedLandCoverages using partial update
        InsuranceCultivatedLandCoverages partialUpdatedInsuranceCultivatedLandCoverages = new InsuranceCultivatedLandCoverages();
        partialUpdatedInsuranceCultivatedLandCoverages.setId(insuranceCultivatedLandCoverages.getId());

        partialUpdatedInsuranceCultivatedLandCoverages
            .converageAmount(UPDATED_CONVERAGE_AMOUNT)
            .isSelect(UPDATED_IS_SELECT)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCultivatedLandCoverages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCultivatedLandCoverages))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCultivatedLandCoverages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCultivatedLandCoveragesUpdatableFieldsEquals(
            partialUpdatedInsuranceCultivatedLandCoverages,
            getPersistedInsuranceCultivatedLandCoverages(partialUpdatedInsuranceCultivatedLandCoverages)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoverages.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCultivatedLandCoverages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCultivatedLandCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCultivatedLandCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCultivatedLandCoverages))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCultivatedLandCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCultivatedLandCoverages() throws Exception {
        // Initialize the database
        insertedInsuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesRepository.saveAndFlush(
            insuranceCultivatedLandCoverages
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCultivatedLandCoverages
        restInsuranceCultivatedLandCoveragesMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceCultivatedLandCoverages.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCultivatedLandCoveragesRepository.count();
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

    protected InsuranceCultivatedLandCoverages getPersistedInsuranceCultivatedLandCoverages(
        InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages
    ) {
        return insuranceCultivatedLandCoveragesRepository.findById(insuranceCultivatedLandCoverages.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCultivatedLandCoveragesToMatchAllProperties(
        InsuranceCultivatedLandCoverages expectedInsuranceCultivatedLandCoverages
    ) {
        assertInsuranceCultivatedLandCoveragesAllPropertiesEquals(
            expectedInsuranceCultivatedLandCoverages,
            getPersistedInsuranceCultivatedLandCoverages(expectedInsuranceCultivatedLandCoverages)
        );
    }

    protected void assertPersistedInsuranceCultivatedLandCoveragesToMatchUpdatableProperties(
        InsuranceCultivatedLandCoverages expectedInsuranceCultivatedLandCoverages
    ) {
        assertInsuranceCultivatedLandCoveragesAllUpdatablePropertiesEquals(
            expectedInsuranceCultivatedLandCoverages,
            getPersistedInsuranceCultivatedLandCoverages(expectedInsuranceCultivatedLandCoverages)
        );
    }
}
