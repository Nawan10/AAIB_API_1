package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.FarmerFieldLandOwnerAsserts.*;
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
import lk.geoedge.interoperability.domain.FarmerFieldLandOwner;
import lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer;
import lk.geoedge.interoperability.domain.FarmerFieldOwner;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerRepository;
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
 * Integration tests for the {@link FarmerFieldLandOwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmerFieldLandOwnerResourceIT {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/farmer-field-land-owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmerFieldLandOwnerMockMvc;

    private FarmerFieldLandOwner farmerFieldLandOwner;

    private FarmerFieldLandOwner insertedFarmerFieldLandOwner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FarmerFieldLandOwner createEntity() {
        return new FarmerFieldLandOwner().createdAt(DEFAULT_CREATED_AT).addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FarmerFieldLandOwner createUpdatedEntity() {
        return new FarmerFieldLandOwner().createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        farmerFieldLandOwner = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFarmerFieldLandOwner != null) {
            farmerFieldLandOwnerRepository.delete(insertedFarmerFieldLandOwner);
            insertedFarmerFieldLandOwner = null;
        }
    }

    @Test
    @Transactional
    void createFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FarmerFieldLandOwner
        var returnedFarmerFieldLandOwner = om.readValue(
            restFarmerFieldLandOwnerMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(farmerFieldLandOwner))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FarmerFieldLandOwner.class
        );

        // Validate the FarmerFieldLandOwner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFarmerFieldLandOwnerUpdatableFieldsEquals(
            returnedFarmerFieldLandOwner,
            getPersistedFarmerFieldLandOwner(returnedFarmerFieldLandOwner)
        );

        insertedFarmerFieldLandOwner = returnedFarmerFieldLandOwner;
    }

    @Test
    @Transactional
    void createFarmerFieldLandOwnerWithExistingId() throws Exception {
        // Create the FarmerFieldLandOwner with an existing ID
        farmerFieldLandOwner.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmerFieldLandOwnerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwners() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList
        restFarmerFieldLandOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldLandOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getFarmerFieldLandOwner() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get the farmerFieldLandOwner
        restFarmerFieldLandOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, farmerFieldLandOwner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmerFieldLandOwner.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getFarmerFieldLandOwnersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        Long id = farmerFieldLandOwner.getId();

        defaultFarmerFieldLandOwnerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFarmerFieldLandOwnerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFarmerFieldLandOwnerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt equals to
        defaultFarmerFieldLandOwnerFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt in
        defaultFarmerFieldLandOwnerFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt is not null
        defaultFarmerFieldLandOwnerFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt is greater than or equal to
        defaultFarmerFieldLandOwnerFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt is less than or equal to
        defaultFarmerFieldLandOwnerFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt is less than
        defaultFarmerFieldLandOwnerFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where createdAt is greater than
        defaultFarmerFieldLandOwnerFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where addedBy equals to
        defaultFarmerFieldLandOwnerFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where addedBy in
        defaultFarmerFieldLandOwnerFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where addedBy is not null
        defaultFarmerFieldLandOwnerFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where addedBy contains
        defaultFarmerFieldLandOwnerFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        // Get all the farmerFieldLandOwnerList where addedBy does not contain
        defaultFarmerFieldLandOwnerFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByFarmerFieldOwnerIsEqualToSomething() throws Exception {
        FarmerFieldOwner farmerFieldOwner;
        if (TestUtil.findAll(em, FarmerFieldOwner.class).isEmpty()) {
            farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);
            farmerFieldOwner = FarmerFieldOwnerResourceIT.createEntity();
        } else {
            farmerFieldOwner = TestUtil.findAll(em, FarmerFieldOwner.class).get(0);
        }
        em.persist(farmerFieldOwner);
        em.flush();
        farmerFieldLandOwner.setFarmerFieldOwner(farmerFieldOwner);
        farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);
        Long farmerFieldOwnerId = farmerFieldOwner.getId();
        // Get all the farmerFieldLandOwnerList where farmerFieldOwner equals to farmerFieldOwnerId
        defaultFarmerFieldLandOwnerShouldBeFound("farmerFieldOwnerId.equals=" + farmerFieldOwnerId);

        // Get all the farmerFieldLandOwnerList where farmerFieldOwner equals to (farmerFieldOwnerId + 1)
        defaultFarmerFieldLandOwnerShouldNotBeFound("farmerFieldOwnerId.equals=" + (farmerFieldOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllFarmerFieldLandOwnersByFarmerIsEqualToSomething() throws Exception {
        FarmerFieldLandOwnerFarmer farmer;
        if (TestUtil.findAll(em, FarmerFieldLandOwnerFarmer.class).isEmpty()) {
            farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);
            farmer = FarmerFieldLandOwnerFarmerResourceIT.createEntity();
        } else {
            farmer = TestUtil.findAll(em, FarmerFieldLandOwnerFarmer.class).get(0);
        }
        em.persist(farmer);
        em.flush();
        farmerFieldLandOwner.setFarmer(farmer);
        farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);
        Long farmerId = farmer.getId();
        // Get all the farmerFieldLandOwnerList where farmer equals to farmerId
        defaultFarmerFieldLandOwnerShouldBeFound("farmerId.equals=" + farmerId);

        // Get all the farmerFieldLandOwnerList where farmer equals to (farmerId + 1)
        defaultFarmerFieldLandOwnerShouldNotBeFound("farmerId.equals=" + (farmerId + 1));
    }

    private void defaultFarmerFieldLandOwnerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFarmerFieldLandOwnerShouldBeFound(shouldBeFound);
        defaultFarmerFieldLandOwnerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmerFieldLandOwnerShouldBeFound(String filter) throws Exception {
        restFarmerFieldLandOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmerFieldLandOwner.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restFarmerFieldLandOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmerFieldLandOwnerShouldNotBeFound(String filter) throws Exception {
        restFarmerFieldLandOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmerFieldLandOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmerFieldLandOwner() throws Exception {
        // Get the farmerFieldLandOwner
        restFarmerFieldLandOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmerFieldLandOwner() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldLandOwner
        FarmerFieldLandOwner updatedFarmerFieldLandOwner = farmerFieldLandOwnerRepository
            .findById(farmerFieldLandOwner.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedFarmerFieldLandOwner are not directly saved in db
        em.detach(updatedFarmerFieldLandOwner);
        updatedFarmerFieldLandOwner.createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);

        restFarmerFieldLandOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFarmerFieldLandOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFarmerFieldLandOwner))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmerFieldLandOwnerToMatchAllProperties(updatedFarmerFieldLandOwner);
    }

    @Test
    @Transactional
    void putNonExistingFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmerFieldLandOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmerFieldLandOwnerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldLandOwner using partial update
        FarmerFieldLandOwner partialUpdatedFarmerFieldLandOwner = new FarmerFieldLandOwner();
        partialUpdatedFarmerFieldLandOwner.setId(farmerFieldLandOwner.getId());

        restFarmerFieldLandOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldLandOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldLandOwner))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldLandOwner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldLandOwnerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFarmerFieldLandOwner, farmerFieldLandOwner),
            getPersistedFarmerFieldLandOwner(farmerFieldLandOwner)
        );
    }

    @Test
    @Transactional
    void fullUpdateFarmerFieldLandOwnerWithPatch() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmerFieldLandOwner using partial update
        FarmerFieldLandOwner partialUpdatedFarmerFieldLandOwner = new FarmerFieldLandOwner();
        partialUpdatedFarmerFieldLandOwner.setId(farmerFieldLandOwner.getId());

        partialUpdatedFarmerFieldLandOwner.createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);

        restFarmerFieldLandOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmerFieldLandOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmerFieldLandOwner))
            )
            .andExpect(status().isOk());

        // Validate the FarmerFieldLandOwner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmerFieldLandOwnerUpdatableFieldsEquals(
            partialUpdatedFarmerFieldLandOwner,
            getPersistedFarmerFieldLandOwner(partialUpdatedFarmerFieldLandOwner)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmerFieldLandOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isBadRequest());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmerFieldLandOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmerFieldLandOwner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmerFieldLandOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmerFieldLandOwner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FarmerFieldLandOwner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmerFieldLandOwner() throws Exception {
        // Initialize the database
        insertedFarmerFieldLandOwner = farmerFieldLandOwnerRepository.saveAndFlush(farmerFieldLandOwner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmerFieldLandOwner
        restFarmerFieldLandOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmerFieldLandOwner.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmerFieldLandOwnerRepository.count();
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

    protected FarmerFieldLandOwner getPersistedFarmerFieldLandOwner(FarmerFieldLandOwner farmerFieldLandOwner) {
        return farmerFieldLandOwnerRepository.findById(farmerFieldLandOwner.getId()).orElseThrow();
    }

    protected void assertPersistedFarmerFieldLandOwnerToMatchAllProperties(FarmerFieldLandOwner expectedFarmerFieldLandOwner) {
        assertFarmerFieldLandOwnerAllPropertiesEquals(
            expectedFarmerFieldLandOwner,
            getPersistedFarmerFieldLandOwner(expectedFarmerFieldLandOwner)
        );
    }

    protected void assertPersistedFarmerFieldLandOwnerToMatchUpdatableProperties(FarmerFieldLandOwner expectedFarmerFieldLandOwner) {
        assertFarmerFieldLandOwnerAllUpdatablePropertiesEquals(
            expectedFarmerFieldLandOwner,
            getPersistedFarmerFieldLandOwner(expectedFarmerFieldLandOwner)
        );
    }
}
