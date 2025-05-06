package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicyAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy;
import lk.geoedge.interoperability.repository.IndexPolicyInsurancePolicyRepository;
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
 * Integration tests for the {@link IndexPolicyInsurancePolicyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPolicyInsurancePolicyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POLICY_NO = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVATE = 1;
    private static final Integer UPDATED_IS_ACTIVATE = 2;
    private static final Integer SMALLER_IS_ACTIVATE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/index-policy-insurance-policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPolicyInsurancePolicyMockMvc;

    private IndexPolicyInsurancePolicy indexPolicyInsurancePolicy;

    private IndexPolicyInsurancePolicy insertedIndexPolicyInsurancePolicy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyInsurancePolicy createEntity() {
        return new IndexPolicyInsurancePolicy().name(DEFAULT_NAME).policyNo(DEFAULT_POLICY_NO).isActivate(DEFAULT_IS_ACTIVATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyInsurancePolicy createUpdatedEntity() {
        return new IndexPolicyInsurancePolicy().name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);
    }

    @BeforeEach
    void initTest() {
        indexPolicyInsurancePolicy = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPolicyInsurancePolicy != null) {
            indexPolicyInsurancePolicyRepository.delete(insertedIndexPolicyInsurancePolicy);
            insertedIndexPolicyInsurancePolicy = null;
        }
    }

    @Test
    @Transactional
    void createIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPolicyInsurancePolicy
        var returnedIndexPolicyInsurancePolicy = om.readValue(
            restIndexPolicyInsurancePolicyMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPolicyInsurancePolicy.class
        );

        // Validate the IndexPolicyInsurancePolicy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPolicyInsurancePolicyUpdatableFieldsEquals(
            returnedIndexPolicyInsurancePolicy,
            getPersistedIndexPolicyInsurancePolicy(returnedIndexPolicyInsurancePolicy)
        );

        insertedIndexPolicyInsurancePolicy = returnedIndexPolicyInsurancePolicy;
    }

    @Test
    @Transactional
    void createIndexPolicyInsurancePolicyWithExistingId() throws Exception {
        // Create the IndexPolicyInsurancePolicy with an existing ID
        indexPolicyInsurancePolicy.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePolicies() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList
        restIndexPolicyInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyInsurancePolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].policyNo").value(hasItem(DEFAULT_POLICY_NO)))
            .andExpect(jsonPath("$.[*].isActivate").value(hasItem(DEFAULT_IS_ACTIVATE)));
    }

    @Test
    @Transactional
    void getIndexPolicyInsurancePolicy() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get the indexPolicyInsurancePolicy
        restIndexPolicyInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPolicyInsurancePolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPolicyInsurancePolicy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.policyNo").value(DEFAULT_POLICY_NO))
            .andExpect(jsonPath("$.isActivate").value(DEFAULT_IS_ACTIVATE));
    }

    @Test
    @Transactional
    void getIndexPolicyInsurancePoliciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        Long id = indexPolicyInsurancePolicy.getId();

        defaultIndexPolicyInsurancePolicyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPolicyInsurancePolicyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPolicyInsurancePolicyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where name equals to
        defaultIndexPolicyInsurancePolicyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where name in
        defaultIndexPolicyInsurancePolicyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where name is not null
        defaultIndexPolicyInsurancePolicyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where name contains
        defaultIndexPolicyInsurancePolicyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where name does not contain
        defaultIndexPolicyInsurancePolicyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByPolicyNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where policyNo equals to
        defaultIndexPolicyInsurancePolicyFiltering("policyNo.equals=" + DEFAULT_POLICY_NO, "policyNo.equals=" + UPDATED_POLICY_NO);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByPolicyNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where policyNo in
        defaultIndexPolicyInsurancePolicyFiltering(
            "policyNo.in=" + DEFAULT_POLICY_NO + "," + UPDATED_POLICY_NO,
            "policyNo.in=" + UPDATED_POLICY_NO
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByPolicyNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where policyNo is not null
        defaultIndexPolicyInsurancePolicyFiltering("policyNo.specified=true", "policyNo.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByPolicyNoContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where policyNo contains
        defaultIndexPolicyInsurancePolicyFiltering("policyNo.contains=" + DEFAULT_POLICY_NO, "policyNo.contains=" + UPDATED_POLICY_NO);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByPolicyNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where policyNo does not contain
        defaultIndexPolicyInsurancePolicyFiltering(
            "policyNo.doesNotContain=" + UPDATED_POLICY_NO,
            "policyNo.doesNotContain=" + DEFAULT_POLICY_NO
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate equals to
        defaultIndexPolicyInsurancePolicyFiltering("isActivate.equals=" + DEFAULT_IS_ACTIVATE, "isActivate.equals=" + UPDATED_IS_ACTIVATE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate in
        defaultIndexPolicyInsurancePolicyFiltering(
            "isActivate.in=" + DEFAULT_IS_ACTIVATE + "," + UPDATED_IS_ACTIVATE,
            "isActivate.in=" + UPDATED_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate is not null
        defaultIndexPolicyInsurancePolicyFiltering("isActivate.specified=true", "isActivate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate is greater than or equal to
        defaultIndexPolicyInsurancePolicyFiltering(
            "isActivate.greaterThanOrEqual=" + DEFAULT_IS_ACTIVATE,
            "isActivate.greaterThanOrEqual=" + UPDATED_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate is less than or equal to
        defaultIndexPolicyInsurancePolicyFiltering(
            "isActivate.lessThanOrEqual=" + DEFAULT_IS_ACTIVATE,
            "isActivate.lessThanOrEqual=" + SMALLER_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate is less than
        defaultIndexPolicyInsurancePolicyFiltering(
            "isActivate.lessThan=" + UPDATED_IS_ACTIVATE,
            "isActivate.lessThan=" + DEFAULT_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyInsurancePoliciesByIsActivateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        // Get all the indexPolicyInsurancePolicyList where isActivate is greater than
        defaultIndexPolicyInsurancePolicyFiltering(
            "isActivate.greaterThan=" + SMALLER_IS_ACTIVATE,
            "isActivate.greaterThan=" + DEFAULT_IS_ACTIVATE
        );
    }

    private void defaultIndexPolicyInsurancePolicyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPolicyInsurancePolicyShouldBeFound(shouldBeFound);
        defaultIndexPolicyInsurancePolicyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPolicyInsurancePolicyShouldBeFound(String filter) throws Exception {
        restIndexPolicyInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyInsurancePolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].policyNo").value(hasItem(DEFAULT_POLICY_NO)))
            .andExpect(jsonPath("$.[*].isActivate").value(hasItem(DEFAULT_IS_ACTIVATE)));

        // Check, that the count call also returns 1
        restIndexPolicyInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPolicyInsurancePolicyShouldNotBeFound(String filter) throws Exception {
        restIndexPolicyInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPolicyInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPolicyInsurancePolicy() throws Exception {
        // Get the indexPolicyInsurancePolicy
        restIndexPolicyInsurancePolicyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPolicyInsurancePolicy() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyInsurancePolicy
        IndexPolicyInsurancePolicy updatedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository
            .findById(indexPolicyInsurancePolicy.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPolicyInsurancePolicy are not directly saved in db
        em.detach(updatedIndexPolicyInsurancePolicy);
        updatedIndexPolicyInsurancePolicy.name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);

        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPolicyInsurancePolicy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPolicyInsurancePolicy))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPolicyInsurancePolicyToMatchAllProperties(updatedIndexPolicyInsurancePolicy);
    }

    @Test
    @Transactional
    void putNonExistingIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyInsurancePolicy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPolicyInsurancePolicy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyInsurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyInsurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPolicyInsurancePolicyWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyInsurancePolicy using partial update
        IndexPolicyInsurancePolicy partialUpdatedIndexPolicyInsurancePolicy = new IndexPolicyInsurancePolicy();
        partialUpdatedIndexPolicyInsurancePolicy.setId(indexPolicyInsurancePolicy.getId());

        partialUpdatedIndexPolicyInsurancePolicy.name(UPDATED_NAME);

        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyInsurancePolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyInsurancePolicy))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyInsurancePolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyInsurancePolicyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPolicyInsurancePolicy, indexPolicyInsurancePolicy),
            getPersistedIndexPolicyInsurancePolicy(indexPolicyInsurancePolicy)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPolicyInsurancePolicyWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyInsurancePolicy using partial update
        IndexPolicyInsurancePolicy partialUpdatedIndexPolicyInsurancePolicy = new IndexPolicyInsurancePolicy();
        partialUpdatedIndexPolicyInsurancePolicy.setId(indexPolicyInsurancePolicy.getId());

        partialUpdatedIndexPolicyInsurancePolicy.name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);

        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyInsurancePolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyInsurancePolicy))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyInsurancePolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyInsurancePolicyUpdatableFieldsEquals(
            partialUpdatedIndexPolicyInsurancePolicy,
            getPersistedIndexPolicyInsurancePolicy(partialUpdatedIndexPolicyInsurancePolicy)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyInsurancePolicy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPolicyInsurancePolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyInsurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPolicyInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyInsurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyInsurancePolicy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyInsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPolicyInsurancePolicy() throws Exception {
        // Initialize the database
        insertedIndexPolicyInsurancePolicy = indexPolicyInsurancePolicyRepository.saveAndFlush(indexPolicyInsurancePolicy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPolicyInsurancePolicy
        restIndexPolicyInsurancePolicyMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPolicyInsurancePolicy.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPolicyInsurancePolicyRepository.count();
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

    protected IndexPolicyInsurancePolicy getPersistedIndexPolicyInsurancePolicy(IndexPolicyInsurancePolicy indexPolicyInsurancePolicy) {
        return indexPolicyInsurancePolicyRepository.findById(indexPolicyInsurancePolicy.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPolicyInsurancePolicyToMatchAllProperties(
        IndexPolicyInsurancePolicy expectedIndexPolicyInsurancePolicy
    ) {
        assertIndexPolicyInsurancePolicyAllPropertiesEquals(
            expectedIndexPolicyInsurancePolicy,
            getPersistedIndexPolicyInsurancePolicy(expectedIndexPolicyInsurancePolicy)
        );
    }

    protected void assertPersistedIndexPolicyInsurancePolicyToMatchUpdatableProperties(
        IndexPolicyInsurancePolicy expectedIndexPolicyInsurancePolicy
    ) {
        assertIndexPolicyInsurancePolicyAllUpdatablePropertiesEquals(
            expectedIndexPolicyInsurancePolicy,
            getPersistedIndexPolicyInsurancePolicy(expectedIndexPolicyInsurancePolicy)
        );
    }
}
