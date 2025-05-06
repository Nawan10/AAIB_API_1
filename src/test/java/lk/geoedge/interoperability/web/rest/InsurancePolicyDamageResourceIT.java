package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsurancePolicyDamageAsserts.*;
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
import lk.geoedge.interoperability.domain.InsurancePolicyDamage;
import lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageRepository;
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
 * Integration tests for the {@link InsurancePolicyDamageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsurancePolicyDamageResourceIT {

    private static final Double DEFAULT_PERCENTAGE = 1D;
    private static final Double UPDATED_PERCENTAGE = 2D;
    private static final Double SMALLER_PERCENTAGE = 1D - 1D;

    private static final Integer DEFAULT_IS_FREE = 1;
    private static final Integer UPDATED_IS_FREE = 2;
    private static final Integer SMALLER_IS_FREE = 1 - 1;

    private static final Integer DEFAULT_IS_PAID = 1;
    private static final Integer UPDATED_IS_PAID = 2;
    private static final Integer SMALLER_IS_PAID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/insurance-policy-damages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsurancePolicyDamageRepository insurancePolicyDamageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsurancePolicyDamageMockMvc;

    private InsurancePolicyDamage insurancePolicyDamage;

    private InsurancePolicyDamage insertedInsurancePolicyDamage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePolicyDamage createEntity() {
        return new InsurancePolicyDamage().percentage(DEFAULT_PERCENTAGE).isFree(DEFAULT_IS_FREE).isPaid(DEFAULT_IS_PAID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePolicyDamage createUpdatedEntity() {
        return new InsurancePolicyDamage().percentage(UPDATED_PERCENTAGE).isFree(UPDATED_IS_FREE).isPaid(UPDATED_IS_PAID);
    }

    @BeforeEach
    void initTest() {
        insurancePolicyDamage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsurancePolicyDamage != null) {
            insurancePolicyDamageRepository.delete(insertedInsurancePolicyDamage);
            insertedInsurancePolicyDamage = null;
        }
    }

    @Test
    @Transactional
    void createInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsurancePolicyDamage
        var returnedInsurancePolicyDamage = om.readValue(
            restInsurancePolicyDamageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insurancePolicyDamage))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsurancePolicyDamage.class
        );

        // Validate the InsurancePolicyDamage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsurancePolicyDamageUpdatableFieldsEquals(
            returnedInsurancePolicyDamage,
            getPersistedInsurancePolicyDamage(returnedInsurancePolicyDamage)
        );

        insertedInsurancePolicyDamage = returnedInsurancePolicyDamage;
    }

    @Test
    @Transactional
    void createInsurancePolicyDamageWithExistingId() throws Exception {
        // Create the InsurancePolicyDamage with an existing ID
        insurancePolicyDamage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsurancePolicyDamageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamages() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList
        restInsurancePolicyDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePolicyDamage.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].isFree").value(hasItem(DEFAULT_IS_FREE)))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID)));
    }

    @Test
    @Transactional
    void getInsurancePolicyDamage() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get the insurancePolicyDamage
        restInsurancePolicyDamageMockMvc
            .perform(get(ENTITY_API_URL_ID, insurancePolicyDamage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insurancePolicyDamage.getId().intValue()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE))
            .andExpect(jsonPath("$.isFree").value(DEFAULT_IS_FREE))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID));
    }

    @Test
    @Transactional
    void getInsurancePolicyDamagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        Long id = insurancePolicyDamage.getId();

        defaultInsurancePolicyDamageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsurancePolicyDamageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsurancePolicyDamageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage equals to
        defaultInsurancePolicyDamageFiltering("percentage.equals=" + DEFAULT_PERCENTAGE, "percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage in
        defaultInsurancePolicyDamageFiltering(
            "percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE,
            "percentage.in=" + UPDATED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage is not null
        defaultInsurancePolicyDamageFiltering("percentage.specified=true", "percentage.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage is greater than or equal to
        defaultInsurancePolicyDamageFiltering(
            "percentage.greaterThanOrEqual=" + DEFAULT_PERCENTAGE,
            "percentage.greaterThanOrEqual=" + UPDATED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage is less than or equal to
        defaultInsurancePolicyDamageFiltering(
            "percentage.lessThanOrEqual=" + DEFAULT_PERCENTAGE,
            "percentage.lessThanOrEqual=" + SMALLER_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage is less than
        defaultInsurancePolicyDamageFiltering("percentage.lessThan=" + UPDATED_PERCENTAGE, "percentage.lessThan=" + DEFAULT_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where percentage is greater than
        defaultInsurancePolicyDamageFiltering(
            "percentage.greaterThan=" + SMALLER_PERCENTAGE,
            "percentage.greaterThan=" + DEFAULT_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree equals to
        defaultInsurancePolicyDamageFiltering("isFree.equals=" + DEFAULT_IS_FREE, "isFree.equals=" + UPDATED_IS_FREE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree in
        defaultInsurancePolicyDamageFiltering("isFree.in=" + DEFAULT_IS_FREE + "," + UPDATED_IS_FREE, "isFree.in=" + UPDATED_IS_FREE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree is not null
        defaultInsurancePolicyDamageFiltering("isFree.specified=true", "isFree.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree is greater than or equal to
        defaultInsurancePolicyDamageFiltering(
            "isFree.greaterThanOrEqual=" + DEFAULT_IS_FREE,
            "isFree.greaterThanOrEqual=" + UPDATED_IS_FREE
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree is less than or equal to
        defaultInsurancePolicyDamageFiltering("isFree.lessThanOrEqual=" + DEFAULT_IS_FREE, "isFree.lessThanOrEqual=" + SMALLER_IS_FREE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree is less than
        defaultInsurancePolicyDamageFiltering("isFree.lessThan=" + UPDATED_IS_FREE, "isFree.lessThan=" + DEFAULT_IS_FREE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsFreeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isFree is greater than
        defaultInsurancePolicyDamageFiltering("isFree.greaterThan=" + SMALLER_IS_FREE, "isFree.greaterThan=" + DEFAULT_IS_FREE);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid equals to
        defaultInsurancePolicyDamageFiltering("isPaid.equals=" + DEFAULT_IS_PAID, "isPaid.equals=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid in
        defaultInsurancePolicyDamageFiltering("isPaid.in=" + DEFAULT_IS_PAID + "," + UPDATED_IS_PAID, "isPaid.in=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid is not null
        defaultInsurancePolicyDamageFiltering("isPaid.specified=true", "isPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid is greater than or equal to
        defaultInsurancePolicyDamageFiltering(
            "isPaid.greaterThanOrEqual=" + DEFAULT_IS_PAID,
            "isPaid.greaterThanOrEqual=" + UPDATED_IS_PAID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid is less than or equal to
        defaultInsurancePolicyDamageFiltering("isPaid.lessThanOrEqual=" + DEFAULT_IS_PAID, "isPaid.lessThanOrEqual=" + SMALLER_IS_PAID);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid is less than
        defaultInsurancePolicyDamageFiltering("isPaid.lessThan=" + UPDATED_IS_PAID, "isPaid.lessThan=" + DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByIsPaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        // Get all the insurancePolicyDamageList where isPaid is greater than
        defaultInsurancePolicyDamageFiltering("isPaid.greaterThan=" + SMALLER_IS_PAID, "isPaid.greaterThan=" + DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByInsurancePolicyIsEqualToSomething() throws Exception {
        InsurancePolicy insurancePolicy;
        if (TestUtil.findAll(em, InsurancePolicy.class).isEmpty()) {
            insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);
            insurancePolicy = InsurancePolicyResourceIT.createEntity();
        } else {
            insurancePolicy = TestUtil.findAll(em, InsurancePolicy.class).get(0);
        }
        em.persist(insurancePolicy);
        em.flush();
        insurancePolicyDamage.setInsurancePolicy(insurancePolicy);
        insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);
        Long insurancePolicyId = insurancePolicy.getId();
        // Get all the insurancePolicyDamageList where insurancePolicy equals to insurancePolicyId
        defaultInsurancePolicyDamageShouldBeFound("insurancePolicyId.equals=" + insurancePolicyId);

        // Get all the insurancePolicyDamageList where insurancePolicy equals to (insurancePolicyId + 1)
        defaultInsurancePolicyDamageShouldNotBeFound("insurancePolicyId.equals=" + (insurancePolicyId + 1));
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamagesByDamageReasonIsEqualToSomething() throws Exception {
        InsurancePolicyDamageCultivatedLandDamageReason damageReason;
        if (TestUtil.findAll(em, InsurancePolicyDamageCultivatedLandDamageReason.class).isEmpty()) {
            insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);
            damageReason = InsurancePolicyDamageCultivatedLandDamageReasonResourceIT.createEntity();
        } else {
            damageReason = TestUtil.findAll(em, InsurancePolicyDamageCultivatedLandDamageReason.class).get(0);
        }
        em.persist(damageReason);
        em.flush();
        insurancePolicyDamage.setDamageReason(damageReason);
        insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);
        Long damageReasonId = damageReason.getId();
        // Get all the insurancePolicyDamageList where damageReason equals to damageReasonId
        defaultInsurancePolicyDamageShouldBeFound("damageReasonId.equals=" + damageReasonId);

        // Get all the insurancePolicyDamageList where damageReason equals to (damageReasonId + 1)
        defaultInsurancePolicyDamageShouldNotBeFound("damageReasonId.equals=" + (damageReasonId + 1));
    }

    private void defaultInsurancePolicyDamageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsurancePolicyDamageShouldBeFound(shouldBeFound);
        defaultInsurancePolicyDamageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsurancePolicyDamageShouldBeFound(String filter) throws Exception {
        restInsurancePolicyDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePolicyDamage.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].isFree").value(hasItem(DEFAULT_IS_FREE)))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID)));

        // Check, that the count call also returns 1
        restInsurancePolicyDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsurancePolicyDamageShouldNotBeFound(String filter) throws Exception {
        restInsurancePolicyDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsurancePolicyDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsurancePolicyDamage() throws Exception {
        // Get the insurancePolicyDamage
        restInsurancePolicyDamageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsurancePolicyDamage() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicyDamage
        InsurancePolicyDamage updatedInsurancePolicyDamage = insurancePolicyDamageRepository
            .findById(insurancePolicyDamage.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInsurancePolicyDamage are not directly saved in db
        em.detach(updatedInsurancePolicyDamage);
        updatedInsurancePolicyDamage.percentage(UPDATED_PERCENTAGE).isFree(UPDATED_IS_FREE).isPaid(UPDATED_IS_PAID);

        restInsurancePolicyDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsurancePolicyDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsurancePolicyDamage))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsurancePolicyDamageToMatchAllProperties(updatedInsurancePolicyDamage);
    }

    @Test
    @Transactional
    void putNonExistingInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancePolicyDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsurancePolicyDamageWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicyDamage using partial update
        InsurancePolicyDamage partialUpdatedInsurancePolicyDamage = new InsurancePolicyDamage();
        partialUpdatedInsurancePolicyDamage.setId(insurancePolicyDamage.getId());

        partialUpdatedInsurancePolicyDamage.percentage(UPDATED_PERCENTAGE).isFree(UPDATED_IS_FREE);

        restInsurancePolicyDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePolicyDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePolicyDamage))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicyDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePolicyDamageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsurancePolicyDamage, insurancePolicyDamage),
            getPersistedInsurancePolicyDamage(insurancePolicyDamage)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsurancePolicyDamageWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicyDamage using partial update
        InsurancePolicyDamage partialUpdatedInsurancePolicyDamage = new InsurancePolicyDamage();
        partialUpdatedInsurancePolicyDamage.setId(insurancePolicyDamage.getId());

        partialUpdatedInsurancePolicyDamage.percentage(UPDATED_PERCENTAGE).isFree(UPDATED_IS_FREE).isPaid(UPDATED_IS_PAID);

        restInsurancePolicyDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePolicyDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePolicyDamage))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicyDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePolicyDamageUpdatableFieldsEquals(
            partialUpdatedInsurancePolicyDamage,
            getPersistedInsurancePolicyDamage(partialUpdatedInsurancePolicyDamage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insurancePolicyDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsurancePolicyDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicyDamage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePolicyDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsurancePolicyDamage() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamage = insurancePolicyDamageRepository.saveAndFlush(insurancePolicyDamage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insurancePolicyDamage
        restInsurancePolicyDamageMockMvc
            .perform(delete(ENTITY_API_URL_ID, insurancePolicyDamage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insurancePolicyDamageRepository.count();
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

    protected InsurancePolicyDamage getPersistedInsurancePolicyDamage(InsurancePolicyDamage insurancePolicyDamage) {
        return insurancePolicyDamageRepository.findById(insurancePolicyDamage.getId()).orElseThrow();
    }

    protected void assertPersistedInsurancePolicyDamageToMatchAllProperties(InsurancePolicyDamage expectedInsurancePolicyDamage) {
        assertInsurancePolicyDamageAllPropertiesEquals(
            expectedInsurancePolicyDamage,
            getPersistedInsurancePolicyDamage(expectedInsurancePolicyDamage)
        );
    }

    protected void assertPersistedInsurancePolicyDamageToMatchUpdatableProperties(InsurancePolicyDamage expectedInsurancePolicyDamage) {
        assertInsurancePolicyDamageAllUpdatablePropertiesEquals(
            expectedInsurancePolicyDamage,
            getPersistedInsurancePolicyDamage(expectedInsurancePolicyDamage)
        );
    }
}
