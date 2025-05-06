package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandFarmersAsserts.*;
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
import lk.geoedge.interoperability.domain.CultivatedLandFarmers;
import lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersRepository;
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
 * Integration tests for the {@link CultivatedLandFarmersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandFarmersResourceIT {

    private static final Integer DEFAULT_RELATION_ID = 1;
    private static final Integer UPDATED_RELATION_ID = 2;
    private static final Integer SMALLER_RELATION_ID = 1 - 1;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-farmers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandFarmersRepository cultivatedLandFarmersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandFarmersMockMvc;

    private CultivatedLandFarmers cultivatedLandFarmers;

    private CultivatedLandFarmers insertedCultivatedLandFarmers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandFarmers createEntity() {
        return new CultivatedLandFarmers().relationId(DEFAULT_RELATION_ID).createdAt(DEFAULT_CREATED_AT).addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandFarmers createUpdatedEntity() {
        return new CultivatedLandFarmers().relationId(UPDATED_RELATION_ID).createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandFarmers = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandFarmers != null) {
            cultivatedLandFarmersRepository.delete(insertedCultivatedLandFarmers);
            insertedCultivatedLandFarmers = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandFarmers
        var returnedCultivatedLandFarmers = om.readValue(
            restCultivatedLandFarmersMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandFarmers))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandFarmers.class
        );

        // Validate the CultivatedLandFarmers in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandFarmersUpdatableFieldsEquals(
            returnedCultivatedLandFarmers,
            getPersistedCultivatedLandFarmers(returnedCultivatedLandFarmers)
        );

        insertedCultivatedLandFarmers = returnedCultivatedLandFarmers;
    }

    @Test
    @Transactional
    void createCultivatedLandFarmersWithExistingId() throws Exception {
        // Create the CultivatedLandFarmers with an existing ID
        cultivatedLandFarmers.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandFarmersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmers() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList
        restCultivatedLandFarmersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmers.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationId").value(hasItem(DEFAULT_RELATION_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedLandFarmers() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get the cultivatedLandFarmers
        restCultivatedLandFarmersMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandFarmers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandFarmers.getId().intValue()))
            .andExpect(jsonPath("$.relationId").value(DEFAULT_RELATION_ID))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedLandFarmersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        Long id = cultivatedLandFarmers.getId();

        defaultCultivatedLandFarmersFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandFarmersFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandFarmersFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId equals to
        defaultCultivatedLandFarmersFiltering("relationId.equals=" + DEFAULT_RELATION_ID, "relationId.equals=" + UPDATED_RELATION_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId in
        defaultCultivatedLandFarmersFiltering(
            "relationId.in=" + DEFAULT_RELATION_ID + "," + UPDATED_RELATION_ID,
            "relationId.in=" + UPDATED_RELATION_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId is not null
        defaultCultivatedLandFarmersFiltering("relationId.specified=true", "relationId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId is greater than or equal to
        defaultCultivatedLandFarmersFiltering(
            "relationId.greaterThanOrEqual=" + DEFAULT_RELATION_ID,
            "relationId.greaterThanOrEqual=" + UPDATED_RELATION_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId is less than or equal to
        defaultCultivatedLandFarmersFiltering(
            "relationId.lessThanOrEqual=" + DEFAULT_RELATION_ID,
            "relationId.lessThanOrEqual=" + SMALLER_RELATION_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId is less than
        defaultCultivatedLandFarmersFiltering("relationId.lessThan=" + UPDATED_RELATION_ID, "relationId.lessThan=" + DEFAULT_RELATION_ID);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByRelationIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where relationId is greater than
        defaultCultivatedLandFarmersFiltering(
            "relationId.greaterThan=" + SMALLER_RELATION_ID,
            "relationId.greaterThan=" + DEFAULT_RELATION_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt equals to
        defaultCultivatedLandFarmersFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt in
        defaultCultivatedLandFarmersFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt is not null
        defaultCultivatedLandFarmersFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt is greater than or equal to
        defaultCultivatedLandFarmersFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt is less than or equal to
        defaultCultivatedLandFarmersFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt is less than
        defaultCultivatedLandFarmersFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where createdAt is greater than
        defaultCultivatedLandFarmersFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where addedBy equals to
        defaultCultivatedLandFarmersFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where addedBy in
        defaultCultivatedLandFarmersFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where addedBy is not null
        defaultCultivatedLandFarmersFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where addedBy contains
        defaultCultivatedLandFarmersFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        // Get all the cultivatedLandFarmersList where addedBy does not contain
        defaultCultivatedLandFarmersFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByFarmerIsEqualToSomething() throws Exception {
        CultivatedLandFarmersFarmer farmer;
        if (TestUtil.findAll(em, CultivatedLandFarmersFarmer.class).isEmpty()) {
            cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);
            farmer = CultivatedLandFarmersFarmerResourceIT.createEntity();
        } else {
            farmer = TestUtil.findAll(em, CultivatedLandFarmersFarmer.class).get(0);
        }
        em.persist(farmer);
        em.flush();
        cultivatedLandFarmers.setFarmer(farmer);
        cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);
        Long farmerId = farmer.getId();
        // Get all the cultivatedLandFarmersList where farmer equals to farmerId
        defaultCultivatedLandFarmersShouldBeFound("farmerId.equals=" + farmerId);

        // Get all the cultivatedLandFarmersList where farmer equals to (farmerId + 1)
        defaultCultivatedLandFarmersShouldNotBeFound("farmerId.equals=" + (farmerId + 1));
    }

    @Test
    @Transactional
    void getAllCultivatedLandFarmersByCultivatedLandIsEqualToSomething() throws Exception {
        CultivatedLandFarmerLand cultivatedLand;
        if (TestUtil.findAll(em, CultivatedLandFarmerLand.class).isEmpty()) {
            cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);
            cultivatedLand = CultivatedLandFarmerLandResourceIT.createEntity();
        } else {
            cultivatedLand = TestUtil.findAll(em, CultivatedLandFarmerLand.class).get(0);
        }
        em.persist(cultivatedLand);
        em.flush();
        cultivatedLandFarmers.setCultivatedLand(cultivatedLand);
        cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);
        Long cultivatedLandId = cultivatedLand.getId();
        // Get all the cultivatedLandFarmersList where cultivatedLand equals to cultivatedLandId
        defaultCultivatedLandFarmersShouldBeFound("cultivatedLandId.equals=" + cultivatedLandId);

        // Get all the cultivatedLandFarmersList where cultivatedLand equals to (cultivatedLandId + 1)
        defaultCultivatedLandFarmersShouldNotBeFound("cultivatedLandId.equals=" + (cultivatedLandId + 1));
    }

    private void defaultCultivatedLandFarmersFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandFarmersShouldBeFound(shouldBeFound);
        defaultCultivatedLandFarmersShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandFarmersShouldBeFound(String filter) throws Exception {
        restCultivatedLandFarmersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandFarmers.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationId").value(hasItem(DEFAULT_RELATION_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedLandFarmersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandFarmersShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandFarmersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandFarmersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandFarmers() throws Exception {
        // Get the cultivatedLandFarmers
        restCultivatedLandFarmersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandFarmers() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmers
        CultivatedLandFarmers updatedCultivatedLandFarmers = cultivatedLandFarmersRepository
            .findById(cultivatedLandFarmers.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandFarmers are not directly saved in db
        em.detach(updatedCultivatedLandFarmers);
        updatedCultivatedLandFarmers.relationId(UPDATED_RELATION_ID).createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);

        restCultivatedLandFarmersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandFarmers.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandFarmers))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandFarmersToMatchAllProperties(updatedCultivatedLandFarmers);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandFarmers.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandFarmersWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmers using partial update
        CultivatedLandFarmers partialUpdatedCultivatedLandFarmers = new CultivatedLandFarmers();
        partialUpdatedCultivatedLandFarmers.setId(cultivatedLandFarmers.getId());

        partialUpdatedCultivatedLandFarmers.relationId(UPDATED_RELATION_ID).addedBy(UPDATED_ADDED_BY);

        restCultivatedLandFarmersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmers))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmersUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandFarmers, cultivatedLandFarmers),
            getPersistedCultivatedLandFarmers(cultivatedLandFarmers)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandFarmersWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandFarmers using partial update
        CultivatedLandFarmers partialUpdatedCultivatedLandFarmers = new CultivatedLandFarmers();
        partialUpdatedCultivatedLandFarmers.setId(cultivatedLandFarmers.getId());

        partialUpdatedCultivatedLandFarmers.relationId(UPDATED_RELATION_ID).createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);

        restCultivatedLandFarmersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandFarmers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandFarmers))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandFarmers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandFarmersUpdatableFieldsEquals(
            partialUpdatedCultivatedLandFarmers,
            getPersistedCultivatedLandFarmers(partialUpdatedCultivatedLandFarmers)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandFarmers.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandFarmers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandFarmers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandFarmersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandFarmers))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandFarmers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandFarmers() throws Exception {
        // Initialize the database
        insertedCultivatedLandFarmers = cultivatedLandFarmersRepository.saveAndFlush(cultivatedLandFarmers);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandFarmers
        restCultivatedLandFarmersMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandFarmers.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandFarmersRepository.count();
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

    protected CultivatedLandFarmers getPersistedCultivatedLandFarmers(CultivatedLandFarmers cultivatedLandFarmers) {
        return cultivatedLandFarmersRepository.findById(cultivatedLandFarmers.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandFarmersToMatchAllProperties(CultivatedLandFarmers expectedCultivatedLandFarmers) {
        assertCultivatedLandFarmersAllPropertiesEquals(
            expectedCultivatedLandFarmers,
            getPersistedCultivatedLandFarmers(expectedCultivatedLandFarmers)
        );
    }

    protected void assertPersistedCultivatedLandFarmersToMatchUpdatableProperties(CultivatedLandFarmers expectedCultivatedLandFarmers) {
        assertCultivatedLandFarmersAllUpdatablePropertiesEquals(
            expectedCultivatedLandFarmers,
            getPersistedCultivatedLandFarmers(expectedCultivatedLandFarmers)
        );
    }
}
