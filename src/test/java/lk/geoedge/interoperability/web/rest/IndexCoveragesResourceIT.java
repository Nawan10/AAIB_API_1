package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexCoveragesAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexCoverages;
import lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.IndexCoveragesRepository;
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
 * Integration tests for the {@link IndexCoveragesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexCoveragesResourceIT {

    private static final Integer DEFAULT_INDEX_PRODUCT_ID = 1;
    private static final Integer UPDATED_INDEX_PRODUCT_ID = 2;
    private static final Integer SMALLER_INDEX_PRODUCT_ID = 1 - 1;

    private static final Integer DEFAULT_PREMIUM_RATE = 1;
    private static final Integer UPDATED_PREMIUM_RATE = 2;
    private static final Integer SMALLER_PREMIUM_RATE = 1 - 1;

    private static final Integer DEFAULT_IS_FREE = 1;
    private static final Integer UPDATED_IS_FREE = 2;
    private static final Integer SMALLER_IS_FREE = 1 - 1;

    private static final Integer DEFAULT_IS_PAID = 1;
    private static final Integer UPDATED_IS_PAID = 2;
    private static final Integer SMALLER_IS_PAID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/index-coverages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexCoveragesRepository indexCoveragesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexCoveragesMockMvc;

    private IndexCoverages indexCoverages;

    private IndexCoverages insertedIndexCoverages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexCoverages createEntity() {
        return new IndexCoverages()
            .indexProductId(DEFAULT_INDEX_PRODUCT_ID)
            .premiumRate(DEFAULT_PREMIUM_RATE)
            .isFree(DEFAULT_IS_FREE)
            .isPaid(DEFAULT_IS_PAID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexCoverages createUpdatedEntity() {
        return new IndexCoverages()
            .indexProductId(UPDATED_INDEX_PRODUCT_ID)
            .premiumRate(UPDATED_PREMIUM_RATE)
            .isFree(UPDATED_IS_FREE)
            .isPaid(UPDATED_IS_PAID);
    }

    @BeforeEach
    void initTest() {
        indexCoverages = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexCoverages != null) {
            indexCoveragesRepository.delete(insertedIndexCoverages);
            insertedIndexCoverages = null;
        }
    }

    @Test
    @Transactional
    void createIndexCoverages() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexCoverages
        var returnedIndexCoverages = om.readValue(
            restIndexCoveragesMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexCoverages))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexCoverages.class
        );

        // Validate the IndexCoverages in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexCoveragesUpdatableFieldsEquals(returnedIndexCoverages, getPersistedIndexCoverages(returnedIndexCoverages));

        insertedIndexCoverages = returnedIndexCoverages;
    }

    @Test
    @Transactional
    void createIndexCoveragesWithExistingId() throws Exception {
        // Create the IndexCoverages with an existing ID
        indexCoverages.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexCoveragesMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexCoverages() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList
        restIndexCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexCoverages.getId().intValue())))
            .andExpect(jsonPath("$.[*].indexProductId").value(hasItem(DEFAULT_INDEX_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].premiumRate").value(hasItem(DEFAULT_PREMIUM_RATE)))
            .andExpect(jsonPath("$.[*].isFree").value(hasItem(DEFAULT_IS_FREE)))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID)));
    }

    @Test
    @Transactional
    void getIndexCoverages() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get the indexCoverages
        restIndexCoveragesMockMvc
            .perform(get(ENTITY_API_URL_ID, indexCoverages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexCoverages.getId().intValue()))
            .andExpect(jsonPath("$.indexProductId").value(DEFAULT_INDEX_PRODUCT_ID))
            .andExpect(jsonPath("$.premiumRate").value(DEFAULT_PREMIUM_RATE))
            .andExpect(jsonPath("$.isFree").value(DEFAULT_IS_FREE))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID));
    }

    @Test
    @Transactional
    void getIndexCoveragesByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        Long id = indexCoverages.getId();

        defaultIndexCoveragesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexCoveragesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexCoveragesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId equals to
        defaultIndexCoveragesFiltering(
            "indexProductId.equals=" + DEFAULT_INDEX_PRODUCT_ID,
            "indexProductId.equals=" + UPDATED_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId in
        defaultIndexCoveragesFiltering(
            "indexProductId.in=" + DEFAULT_INDEX_PRODUCT_ID + "," + UPDATED_INDEX_PRODUCT_ID,
            "indexProductId.in=" + UPDATED_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId is not null
        defaultIndexCoveragesFiltering("indexProductId.specified=true", "indexProductId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId is greater than or equal to
        defaultIndexCoveragesFiltering(
            "indexProductId.greaterThanOrEqual=" + DEFAULT_INDEX_PRODUCT_ID,
            "indexProductId.greaterThanOrEqual=" + UPDATED_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId is less than or equal to
        defaultIndexCoveragesFiltering(
            "indexProductId.lessThanOrEqual=" + DEFAULT_INDEX_PRODUCT_ID,
            "indexProductId.lessThanOrEqual=" + SMALLER_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId is less than
        defaultIndexCoveragesFiltering(
            "indexProductId.lessThan=" + UPDATED_INDEX_PRODUCT_ID,
            "indexProductId.lessThan=" + DEFAULT_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIndexProductIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where indexProductId is greater than
        defaultIndexCoveragesFiltering(
            "indexProductId.greaterThan=" + SMALLER_INDEX_PRODUCT_ID,
            "indexProductId.greaterThan=" + DEFAULT_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate equals to
        defaultIndexCoveragesFiltering("premiumRate.equals=" + DEFAULT_PREMIUM_RATE, "premiumRate.equals=" + UPDATED_PREMIUM_RATE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate in
        defaultIndexCoveragesFiltering(
            "premiumRate.in=" + DEFAULT_PREMIUM_RATE + "," + UPDATED_PREMIUM_RATE,
            "premiumRate.in=" + UPDATED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate is not null
        defaultIndexCoveragesFiltering("premiumRate.specified=true", "premiumRate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate is greater than or equal to
        defaultIndexCoveragesFiltering(
            "premiumRate.greaterThanOrEqual=" + DEFAULT_PREMIUM_RATE,
            "premiumRate.greaterThanOrEqual=" + UPDATED_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate is less than or equal to
        defaultIndexCoveragesFiltering(
            "premiumRate.lessThanOrEqual=" + DEFAULT_PREMIUM_RATE,
            "premiumRate.lessThanOrEqual=" + SMALLER_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate is less than
        defaultIndexCoveragesFiltering("premiumRate.lessThan=" + UPDATED_PREMIUM_RATE, "premiumRate.lessThan=" + DEFAULT_PREMIUM_RATE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByPremiumRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where premiumRate is greater than
        defaultIndexCoveragesFiltering(
            "premiumRate.greaterThan=" + SMALLER_PREMIUM_RATE,
            "premiumRate.greaterThan=" + DEFAULT_PREMIUM_RATE
        );
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree equals to
        defaultIndexCoveragesFiltering("isFree.equals=" + DEFAULT_IS_FREE, "isFree.equals=" + UPDATED_IS_FREE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree in
        defaultIndexCoveragesFiltering("isFree.in=" + DEFAULT_IS_FREE + "," + UPDATED_IS_FREE, "isFree.in=" + UPDATED_IS_FREE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree is not null
        defaultIndexCoveragesFiltering("isFree.specified=true", "isFree.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree is greater than or equal to
        defaultIndexCoveragesFiltering("isFree.greaterThanOrEqual=" + DEFAULT_IS_FREE, "isFree.greaterThanOrEqual=" + UPDATED_IS_FREE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree is less than or equal to
        defaultIndexCoveragesFiltering("isFree.lessThanOrEqual=" + DEFAULT_IS_FREE, "isFree.lessThanOrEqual=" + SMALLER_IS_FREE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree is less than
        defaultIndexCoveragesFiltering("isFree.lessThan=" + UPDATED_IS_FREE, "isFree.lessThan=" + DEFAULT_IS_FREE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsFreeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isFree is greater than
        defaultIndexCoveragesFiltering("isFree.greaterThan=" + SMALLER_IS_FREE, "isFree.greaterThan=" + DEFAULT_IS_FREE);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid equals to
        defaultIndexCoveragesFiltering("isPaid.equals=" + DEFAULT_IS_PAID, "isPaid.equals=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid in
        defaultIndexCoveragesFiltering("isPaid.in=" + DEFAULT_IS_PAID + "," + UPDATED_IS_PAID, "isPaid.in=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid is not null
        defaultIndexCoveragesFiltering("isPaid.specified=true", "isPaid.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid is greater than or equal to
        defaultIndexCoveragesFiltering("isPaid.greaterThanOrEqual=" + DEFAULT_IS_PAID, "isPaid.greaterThanOrEqual=" + UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid is less than or equal to
        defaultIndexCoveragesFiltering("isPaid.lessThanOrEqual=" + DEFAULT_IS_PAID, "isPaid.lessThanOrEqual=" + SMALLER_IS_PAID);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid is less than
        defaultIndexCoveragesFiltering("isPaid.lessThan=" + UPDATED_IS_PAID, "isPaid.lessThan=" + DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByIsPaidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        // Get all the indexCoveragesList where isPaid is greater than
        defaultIndexCoveragesFiltering("isPaid.greaterThan=" + SMALLER_IS_PAID, "isPaid.greaterThan=" + DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    void getAllIndexCoveragesByDamageReasonIsEqualToSomething() throws Exception {
        IndexCoveragesCultivatedLandDamageReason damageReason;
        if (TestUtil.findAll(em, IndexCoveragesCultivatedLandDamageReason.class).isEmpty()) {
            indexCoveragesRepository.saveAndFlush(indexCoverages);
            damageReason = IndexCoveragesCultivatedLandDamageReasonResourceIT.createEntity();
        } else {
            damageReason = TestUtil.findAll(em, IndexCoveragesCultivatedLandDamageReason.class).get(0);
        }
        em.persist(damageReason);
        em.flush();
        indexCoverages.setDamageReason(damageReason);
        indexCoveragesRepository.saveAndFlush(indexCoverages);
        Long damageReasonId = damageReason.getId();
        // Get all the indexCoveragesList where damageReason equals to damageReasonId
        defaultIndexCoveragesShouldBeFound("damageReasonId.equals=" + damageReasonId);

        // Get all the indexCoveragesList where damageReason equals to (damageReasonId + 1)
        defaultIndexCoveragesShouldNotBeFound("damageReasonId.equals=" + (damageReasonId + 1));
    }

    private void defaultIndexCoveragesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexCoveragesShouldBeFound(shouldBeFound);
        defaultIndexCoveragesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexCoveragesShouldBeFound(String filter) throws Exception {
        restIndexCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexCoverages.getId().intValue())))
            .andExpect(jsonPath("$.[*].indexProductId").value(hasItem(DEFAULT_INDEX_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].premiumRate").value(hasItem(DEFAULT_PREMIUM_RATE)))
            .andExpect(jsonPath("$.[*].isFree").value(hasItem(DEFAULT_IS_FREE)))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID)));

        // Check, that the count call also returns 1
        restIndexCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexCoveragesShouldNotBeFound(String filter) throws Exception {
        restIndexCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexCoveragesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexCoverages() throws Exception {
        // Get the indexCoverages
        restIndexCoveragesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexCoverages() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexCoverages
        IndexCoverages updatedIndexCoverages = indexCoveragesRepository.findById(indexCoverages.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndexCoverages are not directly saved in db
        em.detach(updatedIndexCoverages);
        updatedIndexCoverages
            .indexProductId(UPDATED_INDEX_PRODUCT_ID)
            .premiumRate(UPDATED_PREMIUM_RATE)
            .isFree(UPDATED_IS_FREE)
            .isPaid(UPDATED_IS_PAID);

        restIndexCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexCoverages.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexCoverages))
            )
            .andExpect(status().isOk());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexCoveragesToMatchAllProperties(updatedIndexCoverages);
    }

    @Test
    @Transactional
    void putNonExistingIndexCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoverages.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexCoverages.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexCoverages)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexCoveragesWithPatch() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexCoverages using partial update
        IndexCoverages partialUpdatedIndexCoverages = new IndexCoverages();
        partialUpdatedIndexCoverages.setId(indexCoverages.getId());

        partialUpdatedIndexCoverages.indexProductId(UPDATED_INDEX_PRODUCT_ID).isPaid(UPDATED_IS_PAID);

        restIndexCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexCoverages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexCoverages))
            )
            .andExpect(status().isOk());

        // Validate the IndexCoverages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexCoveragesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexCoverages, indexCoverages),
            getPersistedIndexCoverages(indexCoverages)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexCoveragesWithPatch() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexCoverages using partial update
        IndexCoverages partialUpdatedIndexCoverages = new IndexCoverages();
        partialUpdatedIndexCoverages.setId(indexCoverages.getId());

        partialUpdatedIndexCoverages
            .indexProductId(UPDATED_INDEX_PRODUCT_ID)
            .premiumRate(UPDATED_PREMIUM_RATE)
            .isFree(UPDATED_IS_FREE)
            .isPaid(UPDATED_IS_PAID);

        restIndexCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexCoverages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexCoverages))
            )
            .andExpect(status().isOk());

        // Validate the IndexCoverages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexCoveragesUpdatableFieldsEquals(partialUpdatedIndexCoverages, getPersistedIndexCoverages(partialUpdatedIndexCoverages));
    }

    @Test
    @Transactional
    void patchNonExistingIndexCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoverages.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexCoverages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexCoverages))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexCoverages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexCoverages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexCoveragesMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(indexCoverages))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexCoverages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexCoverages() throws Exception {
        // Initialize the database
        insertedIndexCoverages = indexCoveragesRepository.saveAndFlush(indexCoverages);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexCoverages
        restIndexCoveragesMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexCoverages.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexCoveragesRepository.count();
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

    protected IndexCoverages getPersistedIndexCoverages(IndexCoverages indexCoverages) {
        return indexCoveragesRepository.findById(indexCoverages.getId()).orElseThrow();
    }

    protected void assertPersistedIndexCoveragesToMatchAllProperties(IndexCoverages expectedIndexCoverages) {
        assertIndexCoveragesAllPropertiesEquals(expectedIndexCoverages, getPersistedIndexCoverages(expectedIndexCoverages));
    }

    protected void assertPersistedIndexCoveragesToMatchUpdatableProperties(IndexCoverages expectedIndexCoverages) {
        assertIndexCoveragesAllUpdatablePropertiesEquals(expectedIndexCoverages, getPersistedIndexCoverages(expectedIndexCoverages));
    }
}
