package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsuranceCropAsserts.*;
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
import lk.geoedge.interoperability.domain.InsuranceCrop;
import lk.geoedge.interoperability.domain.InsuranceCropCropType;
import lk.geoedge.interoperability.repository.InsuranceCropRepository;
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
 * Integration tests for the {@link InsuranceCropResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsuranceCropResourceIT {

    private static final String DEFAULT_POLICY_ID = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_YIELD = 1D;
    private static final Double UPDATED_YIELD = 2D;
    private static final Double SMALLER_YIELD = 1D - 1D;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insurance-crops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsuranceCropRepository insuranceCropRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceCropMockMvc;

    private InsuranceCrop insuranceCrop;

    private InsuranceCrop insertedInsuranceCrop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCrop createEntity() {
        return new InsuranceCrop().policyId(DEFAULT_POLICY_ID).yield(DEFAULT_YIELD).createdAt(DEFAULT_CREATED_AT).addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceCrop createUpdatedEntity() {
        return new InsuranceCrop().policyId(UPDATED_POLICY_ID).yield(UPDATED_YIELD).createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        insuranceCrop = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsuranceCrop != null) {
            insuranceCropRepository.delete(insertedInsuranceCrop);
            insertedInsuranceCrop = null;
        }
    }

    @Test
    @Transactional
    void createInsuranceCrop() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsuranceCrop
        var returnedInsuranceCrop = om.readValue(
            restInsuranceCropMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceCrop))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsuranceCrop.class
        );

        // Validate the InsuranceCrop in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsuranceCropUpdatableFieldsEquals(returnedInsuranceCrop, getPersistedInsuranceCrop(returnedInsuranceCrop));

        insertedInsuranceCrop = returnedInsuranceCrop;
    }

    @Test
    @Transactional
    void createInsuranceCropWithExistingId() throws Exception {
        // Create the InsuranceCrop with an existing ID
        insuranceCrop.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceCropMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceCrop)))
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsuranceCrops() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList
        restInsuranceCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCrop.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyId").value(hasItem(DEFAULT_POLICY_ID)))
            .andExpect(jsonPath("$.[*].yield").value(hasItem(DEFAULT_YIELD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getInsuranceCrop() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get the insuranceCrop
        restInsuranceCropMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceCrop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceCrop.getId().intValue()))
            .andExpect(jsonPath("$.policyId").value(DEFAULT_POLICY_ID))
            .andExpect(jsonPath("$.yield").value(DEFAULT_YIELD))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getInsuranceCropsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        Long id = insuranceCrop.getId();

        defaultInsuranceCropFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsuranceCropFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsuranceCropFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByPolicyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where policyId equals to
        defaultInsuranceCropFiltering("policyId.equals=" + DEFAULT_POLICY_ID, "policyId.equals=" + UPDATED_POLICY_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByPolicyIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where policyId in
        defaultInsuranceCropFiltering("policyId.in=" + DEFAULT_POLICY_ID + "," + UPDATED_POLICY_ID, "policyId.in=" + UPDATED_POLICY_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByPolicyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where policyId is not null
        defaultInsuranceCropFiltering("policyId.specified=true", "policyId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByPolicyIdContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where policyId contains
        defaultInsuranceCropFiltering("policyId.contains=" + DEFAULT_POLICY_ID, "policyId.contains=" + UPDATED_POLICY_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByPolicyIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where policyId does not contain
        defaultInsuranceCropFiltering("policyId.doesNotContain=" + UPDATED_POLICY_ID, "policyId.doesNotContain=" + DEFAULT_POLICY_ID);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield equals to
        defaultInsuranceCropFiltering("yield.equals=" + DEFAULT_YIELD, "yield.equals=" + UPDATED_YIELD);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield in
        defaultInsuranceCropFiltering("yield.in=" + DEFAULT_YIELD + "," + UPDATED_YIELD, "yield.in=" + UPDATED_YIELD);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield is not null
        defaultInsuranceCropFiltering("yield.specified=true", "yield.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield is greater than or equal to
        defaultInsuranceCropFiltering("yield.greaterThanOrEqual=" + DEFAULT_YIELD, "yield.greaterThanOrEqual=" + UPDATED_YIELD);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield is less than or equal to
        defaultInsuranceCropFiltering("yield.lessThanOrEqual=" + DEFAULT_YIELD, "yield.lessThanOrEqual=" + SMALLER_YIELD);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield is less than
        defaultInsuranceCropFiltering("yield.lessThan=" + UPDATED_YIELD, "yield.lessThan=" + DEFAULT_YIELD);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByYieldIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where yield is greater than
        defaultInsuranceCropFiltering("yield.greaterThan=" + SMALLER_YIELD, "yield.greaterThan=" + DEFAULT_YIELD);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt equals to
        defaultInsuranceCropFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt in
        defaultInsuranceCropFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt is not null
        defaultInsuranceCropFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt is greater than or equal to
        defaultInsuranceCropFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt is less than or equal to
        defaultInsuranceCropFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt is less than
        defaultInsuranceCropFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where createdAt is greater than
        defaultInsuranceCropFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where addedBy equals to
        defaultInsuranceCropFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where addedBy in
        defaultInsuranceCropFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where addedBy is not null
        defaultInsuranceCropFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where addedBy contains
        defaultInsuranceCropFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        // Get all the insuranceCropList where addedBy does not contain
        defaultInsuranceCropFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllInsuranceCropsByCropIsEqualToSomething() throws Exception {
        InsuranceCropCropType crop;
        if (TestUtil.findAll(em, InsuranceCropCropType.class).isEmpty()) {
            insuranceCropRepository.saveAndFlush(insuranceCrop);
            crop = InsuranceCropCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, InsuranceCropCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        insuranceCrop.setCrop(crop);
        insuranceCropRepository.saveAndFlush(insuranceCrop);
        Long cropId = crop.getId();
        // Get all the insuranceCropList where crop equals to cropId
        defaultInsuranceCropShouldBeFound("cropId.equals=" + cropId);

        // Get all the insuranceCropList where crop equals to (cropId + 1)
        defaultInsuranceCropShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultInsuranceCropFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsuranceCropShouldBeFound(shouldBeFound);
        defaultInsuranceCropShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsuranceCropShouldBeFound(String filter) throws Exception {
        restInsuranceCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceCrop.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyId").value(hasItem(DEFAULT_POLICY_ID)))
            .andExpect(jsonPath("$.[*].yield").value(hasItem(DEFAULT_YIELD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restInsuranceCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsuranceCropShouldNotBeFound(String filter) throws Exception {
        restInsuranceCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsuranceCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceCrop() throws Exception {
        // Get the insuranceCrop
        restInsuranceCropMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceCrop() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCrop
        InsuranceCrop updatedInsuranceCrop = insuranceCropRepository.findById(insuranceCrop.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsuranceCrop are not directly saved in db
        em.detach(updatedInsuranceCrop);
        updatedInsuranceCrop.policyId(UPDATED_POLICY_ID).yield(UPDATED_YIELD).createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);

        restInsuranceCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsuranceCrop.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsuranceCrop))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsuranceCropToMatchAllProperties(updatedInsuranceCrop);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCrop.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceCrop.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insuranceCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insuranceCrop)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceCropWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCrop using partial update
        InsuranceCrop partialUpdatedInsuranceCrop = new InsuranceCrop();
        partialUpdatedInsuranceCrop.setId(insuranceCrop.getId());

        partialUpdatedInsuranceCrop.policyId(UPDATED_POLICY_ID).createdAt(UPDATED_CREATED_AT).addedBy(UPDATED_ADDED_BY);

        restInsuranceCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCrop))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCrop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCropUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsuranceCrop, insuranceCrop),
            getPersistedInsuranceCrop(insuranceCrop)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsuranceCropWithPatch() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insuranceCrop using partial update
        InsuranceCrop partialUpdatedInsuranceCrop = new InsuranceCrop();
        partialUpdatedInsuranceCrop.setId(insuranceCrop.getId());

        partialUpdatedInsuranceCrop
            .policyId(UPDATED_POLICY_ID)
            .yield(UPDATED_YIELD)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restInsuranceCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsuranceCrop))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceCrop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsuranceCropUpdatableFieldsEquals(partialUpdatedInsuranceCrop, getPersistedInsuranceCrop(partialUpdatedInsuranceCrop));
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCrop.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insuranceCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insuranceCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceCropMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insuranceCrop))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceCrop() throws Exception {
        // Initialize the database
        insertedInsuranceCrop = insuranceCropRepository.saveAndFlush(insuranceCrop);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insuranceCrop
        restInsuranceCropMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceCrop.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insuranceCropRepository.count();
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

    protected InsuranceCrop getPersistedInsuranceCrop(InsuranceCrop insuranceCrop) {
        return insuranceCropRepository.findById(insuranceCrop.getId()).orElseThrow();
    }

    protected void assertPersistedInsuranceCropToMatchAllProperties(InsuranceCrop expectedInsuranceCrop) {
        assertInsuranceCropAllPropertiesEquals(expectedInsuranceCrop, getPersistedInsuranceCrop(expectedInsuranceCrop));
    }

    protected void assertPersistedInsuranceCropToMatchUpdatableProperties(InsuranceCrop expectedInsuranceCrop) {
        assertInsuranceCropAllUpdatablePropertiesEquals(expectedInsuranceCrop, getPersistedInsuranceCrop(expectedInsuranceCrop));
    }
}
