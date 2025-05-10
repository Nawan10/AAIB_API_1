package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandEntityAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandEntity;
import lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner;
import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import lk.geoedge.interoperability.repository.CultivatedLandEntityRepository;
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
 * Integration tests for the {@link CultivatedLandEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandEntityResourceIT {

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

    private static final String ENTITY_API_URL = "/api/cultivated-land-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandEntityRepository cultivatedLandEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandEntityMockMvc;

    private CultivatedLandEntity cultivatedLandEntity;

    private CultivatedLandEntity insertedCultivatedLandEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandEntity createEntity() {
        return new CultivatedLandEntity()
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
    public static CultivatedLandEntity createUpdatedEntity() {
        return new CultivatedLandEntity()
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandEntity != null) {
            cultivatedLandEntityRepository.delete(insertedCultivatedLandEntity);
            insertedCultivatedLandEntity = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandEntity
        var returnedCultivatedLandEntity = om.readValue(
            restCultivatedLandEntityMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandEntity))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandEntity.class
        );

        // Validate the CultivatedLandEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandEntityUpdatableFieldsEquals(
            returnedCultivatedLandEntity,
            getPersistedCultivatedLandEntity(returnedCultivatedLandEntity)
        );

        insertedCultivatedLandEntity = returnedCultivatedLandEntity;
    }

    @Test
    @Transactional
    void createCultivatedLandEntityWithExistingId() throws Exception {
        // Create the CultivatedLandEntity with an existing ID
        cultivatedLandEntity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntities() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList
        restCultivatedLandEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedLandEntity() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get the cultivatedLandEntity
        restCultivatedLandEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandEntity.getId().intValue()))
            .andExpect(jsonPath("$.landStatus").value(DEFAULT_LAND_STATUS))
            .andExpect(jsonPath("$.urea").value(DEFAULT_UREA))
            .andExpect(jsonPath("$.mop").value(DEFAULT_MOP))
            .andExpect(jsonPath("$.tsp").value(DEFAULT_TSP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedLandEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        Long id = cultivatedLandEntity.getId();

        defaultCultivatedLandEntityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandEntityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandEntityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByLandStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where landStatus equals to
        defaultCultivatedLandEntityFiltering("landStatus.equals=" + DEFAULT_LAND_STATUS, "landStatus.equals=" + UPDATED_LAND_STATUS);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByLandStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where landStatus in
        defaultCultivatedLandEntityFiltering(
            "landStatus.in=" + DEFAULT_LAND_STATUS + "," + UPDATED_LAND_STATUS,
            "landStatus.in=" + UPDATED_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByLandStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where landStatus is not null
        defaultCultivatedLandEntityFiltering("landStatus.specified=true", "landStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByLandStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where landStatus contains
        defaultCultivatedLandEntityFiltering("landStatus.contains=" + DEFAULT_LAND_STATUS, "landStatus.contains=" + UPDATED_LAND_STATUS);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByLandStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where landStatus does not contain
        defaultCultivatedLandEntityFiltering(
            "landStatus.doesNotContain=" + UPDATED_LAND_STATUS,
            "landStatus.doesNotContain=" + DEFAULT_LAND_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea equals to
        defaultCultivatedLandEntityFiltering("urea.equals=" + DEFAULT_UREA, "urea.equals=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea in
        defaultCultivatedLandEntityFiltering("urea.in=" + DEFAULT_UREA + "," + UPDATED_UREA, "urea.in=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea is not null
        defaultCultivatedLandEntityFiltering("urea.specified=true", "urea.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea is greater than or equal to
        defaultCultivatedLandEntityFiltering("urea.greaterThanOrEqual=" + DEFAULT_UREA, "urea.greaterThanOrEqual=" + UPDATED_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea is less than or equal to
        defaultCultivatedLandEntityFiltering("urea.lessThanOrEqual=" + DEFAULT_UREA, "urea.lessThanOrEqual=" + SMALLER_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea is less than
        defaultCultivatedLandEntityFiltering("urea.lessThan=" + UPDATED_UREA, "urea.lessThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByUreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where urea is greater than
        defaultCultivatedLandEntityFiltering("urea.greaterThan=" + SMALLER_UREA, "urea.greaterThan=" + DEFAULT_UREA);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop equals to
        defaultCultivatedLandEntityFiltering("mop.equals=" + DEFAULT_MOP, "mop.equals=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop in
        defaultCultivatedLandEntityFiltering("mop.in=" + DEFAULT_MOP + "," + UPDATED_MOP, "mop.in=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop is not null
        defaultCultivatedLandEntityFiltering("mop.specified=true", "mop.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop is greater than or equal to
        defaultCultivatedLandEntityFiltering("mop.greaterThanOrEqual=" + DEFAULT_MOP, "mop.greaterThanOrEqual=" + UPDATED_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop is less than or equal to
        defaultCultivatedLandEntityFiltering("mop.lessThanOrEqual=" + DEFAULT_MOP, "mop.lessThanOrEqual=" + SMALLER_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop is less than
        defaultCultivatedLandEntityFiltering("mop.lessThan=" + UPDATED_MOP, "mop.lessThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByMopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where mop is greater than
        defaultCultivatedLandEntityFiltering("mop.greaterThan=" + SMALLER_MOP, "mop.greaterThan=" + DEFAULT_MOP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp equals to
        defaultCultivatedLandEntityFiltering("tsp.equals=" + DEFAULT_TSP, "tsp.equals=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp in
        defaultCultivatedLandEntityFiltering("tsp.in=" + DEFAULT_TSP + "," + UPDATED_TSP, "tsp.in=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp is not null
        defaultCultivatedLandEntityFiltering("tsp.specified=true", "tsp.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp is greater than or equal to
        defaultCultivatedLandEntityFiltering("tsp.greaterThanOrEqual=" + DEFAULT_TSP, "tsp.greaterThanOrEqual=" + UPDATED_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp is less than or equal to
        defaultCultivatedLandEntityFiltering("tsp.lessThanOrEqual=" + DEFAULT_TSP, "tsp.lessThanOrEqual=" + SMALLER_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp is less than
        defaultCultivatedLandEntityFiltering("tsp.lessThan=" + UPDATED_TSP, "tsp.lessThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByTspIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where tsp is greater than
        defaultCultivatedLandEntityFiltering("tsp.greaterThan=" + SMALLER_TSP, "tsp.greaterThan=" + DEFAULT_TSP);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt equals to
        defaultCultivatedLandEntityFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt in
        defaultCultivatedLandEntityFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt is not null
        defaultCultivatedLandEntityFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt is greater than or equal to
        defaultCultivatedLandEntityFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt is less than or equal to
        defaultCultivatedLandEntityFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt is less than
        defaultCultivatedLandEntityFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where createdAt is greater than
        defaultCultivatedLandEntityFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where addedBy equals to
        defaultCultivatedLandEntityFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where addedBy in
        defaultCultivatedLandEntityFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where addedBy is not null
        defaultCultivatedLandEntityFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where addedBy contains
        defaultCultivatedLandEntityFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        // Get all the cultivatedLandEntityList where addedBy does not contain
        defaultCultivatedLandEntityFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesByFarmFieldIsEqualToSomething() throws Exception {
        CultivatedLandFarmerFieldOwner farmField;
        if (TestUtil.findAll(em, CultivatedLandFarmerFieldOwner.class).isEmpty()) {
            cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);
            farmField = CultivatedLandFarmerFieldOwnerResourceIT.createEntity();
        } else {
            farmField = TestUtil.findAll(em, CultivatedLandFarmerFieldOwner.class).get(0);
        }
        em.persist(farmField);
        em.flush();
        cultivatedLandEntity.setFarmField(farmField);
        cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);
        Long farmFieldId = farmField.getId();
        // Get all the cultivatedLandEntityList where farmField equals to farmFieldId
        defaultCultivatedLandEntityShouldBeFound("farmFieldId.equals=" + farmFieldId);

        // Get all the cultivatedLandEntityList where farmField equals to (farmFieldId + 1)
        defaultCultivatedLandEntityShouldNotBeFound("farmFieldId.equals=" + (farmFieldId + 1));
    }

    @Test
    @Transactional
    void getAllCultivatedLandEntitiesBySeasonIsEqualToSomething() throws Exception {
        CultivatedLandSeason season;
        if (TestUtil.findAll(em, CultivatedLandSeason.class).isEmpty()) {
            cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);
            season = CultivatedLandSeasonResourceIT.createEntity();
        } else {
            season = TestUtil.findAll(em, CultivatedLandSeason.class).get(0);
        }
        em.persist(season);
        em.flush();
        cultivatedLandEntity.setSeason(season);
        cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);
        Long seasonId = season.getId();
        // Get all the cultivatedLandEntityList where season equals to seasonId
        defaultCultivatedLandEntityShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the cultivatedLandEntityList where season equals to (seasonId + 1)
        defaultCultivatedLandEntityShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    private void defaultCultivatedLandEntityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandEntityShouldBeFound(shouldBeFound);
        defaultCultivatedLandEntityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandEntityShouldBeFound(String filter) throws Exception {
        restCultivatedLandEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].landStatus").value(hasItem(DEFAULT_LAND_STATUS)))
            .andExpect(jsonPath("$.[*].urea").value(hasItem(DEFAULT_UREA)))
            .andExpect(jsonPath("$.[*].mop").value(hasItem(DEFAULT_MOP)))
            .andExpect(jsonPath("$.[*].tsp").value(hasItem(DEFAULT_TSP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedLandEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandEntityShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandEntity() throws Exception {
        // Get the cultivatedLandEntity
        restCultivatedLandEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandEntity() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandEntity
        CultivatedLandEntity updatedCultivatedLandEntity = cultivatedLandEntityRepository
            .findById(cultivatedLandEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandEntity are not directly saved in db
        em.detach(updatedCultivatedLandEntity);
        updatedCultivatedLandEntity
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandEntity.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandEntity))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandEntityToMatchAllProperties(updatedCultivatedLandEntity);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandEntity.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandEntityMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandEntityWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandEntity using partial update
        CultivatedLandEntity partialUpdatedCultivatedLandEntity = new CultivatedLandEntity();
        partialUpdatedCultivatedLandEntity.setId(cultivatedLandEntity.getId());

        partialUpdatedCultivatedLandEntity.urea(UPDATED_UREA).mop(UPDATED_MOP).tsp(UPDATED_TSP).addedBy(UPDATED_ADDED_BY);

        restCultivatedLandEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandEntity))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandEntity, cultivatedLandEntity),
            getPersistedCultivatedLandEntity(cultivatedLandEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandEntityWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandEntity using partial update
        CultivatedLandEntity partialUpdatedCultivatedLandEntity = new CultivatedLandEntity();
        partialUpdatedCultivatedLandEntity.setId(cultivatedLandEntity.getId());

        partialUpdatedCultivatedLandEntity
            .landStatus(UPDATED_LAND_STATUS)
            .urea(UPDATED_UREA)
            .mop(UPDATED_MOP)
            .tsp(UPDATED_TSP)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandEntity))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandEntityUpdatableFieldsEquals(
            partialUpdatedCultivatedLandEntity,
            getPersistedCultivatedLandEntity(partialUpdatedCultivatedLandEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandEntity() throws Exception {
        // Initialize the database
        insertedCultivatedLandEntity = cultivatedLandEntityRepository.saveAndFlush(cultivatedLandEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandEntity
        restCultivatedLandEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandEntityRepository.count();
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

    protected CultivatedLandEntity getPersistedCultivatedLandEntity(CultivatedLandEntity cultivatedLandEntity) {
        return cultivatedLandEntityRepository.findById(cultivatedLandEntity.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandEntityToMatchAllProperties(CultivatedLandEntity expectedCultivatedLandEntity) {
        assertCultivatedLandEntityAllPropertiesEquals(
            expectedCultivatedLandEntity,
            getPersistedCultivatedLandEntity(expectedCultivatedLandEntity)
        );
    }

    protected void assertPersistedCultivatedLandEntityToMatchUpdatableProperties(CultivatedLandEntity expectedCultivatedLandEntity) {
        assertCultivatedLandEntityAllUpdatablePropertiesEquals(
            expectedCultivatedLandEntity,
            getPersistedCultivatedLandEntity(expectedCultivatedLandEntity)
        );
    }
}
