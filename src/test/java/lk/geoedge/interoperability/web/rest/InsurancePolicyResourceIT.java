package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsurancePolicyAsserts.*;
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
import lk.geoedge.interoperability.domain.InsurancePolicy;
import lk.geoedge.interoperability.repository.InsurancePolicyRepository;
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
 * Integration tests for the {@link InsurancePolicyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsurancePolicyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POLICY_NO = "AAAAAAAAAA";
    private static final String UPDATED_POLICY_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_ACTIVATE = 1;
    private static final Integer UPDATED_IS_ACTIVATE = 2;
    private static final Integer SMALLER_IS_ACTIVATE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/insurance-policies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsurancePolicyMockMvc;

    private InsurancePolicy insurancePolicy;

    private InsurancePolicy insertedInsurancePolicy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePolicy createEntity() {
        return new InsurancePolicy().name(DEFAULT_NAME).policyNo(DEFAULT_POLICY_NO).isActivate(DEFAULT_IS_ACTIVATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePolicy createUpdatedEntity() {
        return new InsurancePolicy().name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);
    }

    @BeforeEach
    void initTest() {
        insurancePolicy = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsurancePolicy != null) {
            insurancePolicyRepository.delete(insertedInsurancePolicy);
            insertedInsurancePolicy = null;
        }
    }

    @Test
    @Transactional
    void createInsurancePolicy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsurancePolicy
        var returnedInsurancePolicy = om.readValue(
            restInsurancePolicyMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insurancePolicy))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsurancePolicy.class
        );

        // Validate the InsurancePolicy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsurancePolicyUpdatableFieldsEquals(returnedInsurancePolicy, getPersistedInsurancePolicy(returnedInsurancePolicy));

        insertedInsurancePolicy = returnedInsurancePolicy;
    }

    @Test
    @Transactional
    void createInsurancePolicyWithExistingId() throws Exception {
        // Create the InsurancePolicy with an existing ID
        insurancePolicy.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsurancePolicyMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsurancePolicies() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList
        restInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].policyNo").value(hasItem(DEFAULT_POLICY_NO)))
            .andExpect(jsonPath("$.[*].isActivate").value(hasItem(DEFAULT_IS_ACTIVATE)));
    }

    @Test
    @Transactional
    void getInsurancePolicy() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get the insurancePolicy
        restInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL_ID, insurancePolicy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insurancePolicy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.policyNo").value(DEFAULT_POLICY_NO))
            .andExpect(jsonPath("$.isActivate").value(DEFAULT_IS_ACTIVATE));
    }

    @Test
    @Transactional
    void getInsurancePoliciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        Long id = insurancePolicy.getId();

        defaultInsurancePolicyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsurancePolicyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsurancePolicyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where name equals to
        defaultInsurancePolicyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where name in
        defaultInsurancePolicyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where name is not null
        defaultInsurancePolicyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where name contains
        defaultInsurancePolicyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where name does not contain
        defaultInsurancePolicyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByPolicyNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where policyNo equals to
        defaultInsurancePolicyFiltering("policyNo.equals=" + DEFAULT_POLICY_NO, "policyNo.equals=" + UPDATED_POLICY_NO);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByPolicyNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where policyNo in
        defaultInsurancePolicyFiltering("policyNo.in=" + DEFAULT_POLICY_NO + "," + UPDATED_POLICY_NO, "policyNo.in=" + UPDATED_POLICY_NO);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByPolicyNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where policyNo is not null
        defaultInsurancePolicyFiltering("policyNo.specified=true", "policyNo.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByPolicyNoContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where policyNo contains
        defaultInsurancePolicyFiltering("policyNo.contains=" + DEFAULT_POLICY_NO, "policyNo.contains=" + UPDATED_POLICY_NO);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByPolicyNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where policyNo does not contain
        defaultInsurancePolicyFiltering("policyNo.doesNotContain=" + UPDATED_POLICY_NO, "policyNo.doesNotContain=" + DEFAULT_POLICY_NO);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate equals to
        defaultInsurancePolicyFiltering("isActivate.equals=" + DEFAULT_IS_ACTIVATE, "isActivate.equals=" + UPDATED_IS_ACTIVATE);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate in
        defaultInsurancePolicyFiltering(
            "isActivate.in=" + DEFAULT_IS_ACTIVATE + "," + UPDATED_IS_ACTIVATE,
            "isActivate.in=" + UPDATED_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate is not null
        defaultInsurancePolicyFiltering("isActivate.specified=true", "isActivate.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate is greater than or equal to
        defaultInsurancePolicyFiltering(
            "isActivate.greaterThanOrEqual=" + DEFAULT_IS_ACTIVATE,
            "isActivate.greaterThanOrEqual=" + UPDATED_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate is less than or equal to
        defaultInsurancePolicyFiltering(
            "isActivate.lessThanOrEqual=" + DEFAULT_IS_ACTIVATE,
            "isActivate.lessThanOrEqual=" + SMALLER_IS_ACTIVATE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate is less than
        defaultInsurancePolicyFiltering("isActivate.lessThan=" + UPDATED_IS_ACTIVATE, "isActivate.lessThan=" + DEFAULT_IS_ACTIVATE);
    }

    @Test
    @Transactional
    void getAllInsurancePoliciesByIsActivateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        // Get all the insurancePolicyList where isActivate is greater than
        defaultInsurancePolicyFiltering("isActivate.greaterThan=" + SMALLER_IS_ACTIVATE, "isActivate.greaterThan=" + DEFAULT_IS_ACTIVATE);
    }

    private void defaultInsurancePolicyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsurancePolicyShouldBeFound(shouldBeFound);
        defaultInsurancePolicyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsurancePolicyShouldBeFound(String filter) throws Exception {
        restInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePolicy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].policyNo").value(hasItem(DEFAULT_POLICY_NO)))
            .andExpect(jsonPath("$.[*].isActivate").value(hasItem(DEFAULT_IS_ACTIVATE)));

        // Check, that the count call also returns 1
        restInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsurancePolicyShouldNotBeFound(String filter) throws Exception {
        restInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsurancePolicyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsurancePolicy() throws Exception {
        // Get the insurancePolicy
        restInsurancePolicyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsurancePolicy() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicy
        InsurancePolicy updatedInsurancePolicy = insurancePolicyRepository.findById(insurancePolicy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsurancePolicy are not directly saved in db
        em.detach(updatedInsurancePolicy);
        updatedInsurancePolicy.name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);

        restInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsurancePolicy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsurancePolicy))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsurancePolicyToMatchAllProperties(updatedInsurancePolicy);
    }

    @Test
    @Transactional
    void putNonExistingInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancePolicy.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsurancePolicyWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicy using partial update
        InsurancePolicy partialUpdatedInsurancePolicy = new InsurancePolicy();
        partialUpdatedInsurancePolicy.setId(insurancePolicy.getId());

        partialUpdatedInsurancePolicy.name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);

        restInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePolicy))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePolicyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsurancePolicy, insurancePolicy),
            getPersistedInsurancePolicy(insurancePolicy)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsurancePolicyWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicy using partial update
        InsurancePolicy partialUpdatedInsurancePolicy = new InsurancePolicy();
        partialUpdatedInsurancePolicy.setId(insurancePolicy.getId());

        partialUpdatedInsurancePolicy.name(UPDATED_NAME).policyNo(UPDATED_POLICY_NO).isActivate(UPDATED_IS_ACTIVATE);

        restInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePolicy))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePolicyUpdatableFieldsEquals(
            partialUpdatedInsurancePolicy,
            getPersistedInsurancePolicy(partialUpdatedInsurancePolicy)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicy.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insurancePolicy.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsurancePolicy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicy.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePolicy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsurancePolicy() throws Exception {
        // Initialize the database
        insertedInsurancePolicy = insurancePolicyRepository.saveAndFlush(insurancePolicy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insurancePolicy
        restInsurancePolicyMockMvc
            .perform(delete(ENTITY_API_URL_ID, insurancePolicy.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insurancePolicyRepository.count();
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

    protected InsurancePolicy getPersistedInsurancePolicy(InsurancePolicy insurancePolicy) {
        return insurancePolicyRepository.findById(insurancePolicy.getId()).orElseThrow();
    }

    protected void assertPersistedInsurancePolicyToMatchAllProperties(InsurancePolicy expectedInsurancePolicy) {
        assertInsurancePolicyAllPropertiesEquals(expectedInsurancePolicy, getPersistedInsurancePolicy(expectedInsurancePolicy));
    }

    protected void assertPersistedInsurancePolicyToMatchUpdatableProperties(InsurancePolicy expectedInsurancePolicy) {
        assertInsurancePolicyAllUpdatablePropertiesEquals(expectedInsurancePolicy, getPersistedInsurancePolicy(expectedInsurancePolicy));
    }
}
