package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReasonAsserts.*;
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
import lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageCultivatedLandDamageReasonRepository;
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
 * Integration tests for the {@link InsurancePolicyDamageCultivatedLandDamageReasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsurancePolicyDamageCultivatedLandDamageReasonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAMAGE_CATEGORY_ID = 1;
    private static final Integer UPDATED_DAMAGE_CATEGORY_ID = 2;
    private static final Integer SMALLER_DAMAGE_CATEGORY_ID = 1 - 1;

    private static final Integer DEFAULT_DAMAGE_TYPE_ID = 1;
    private static final Integer UPDATED_DAMAGE_TYPE_ID = 2;
    private static final Integer SMALLER_DAMAGE_TYPE_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/insurance-policy-damage-cultivated-land-damage-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc;

    private InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason;

    private InsurancePolicyDamageCultivatedLandDamageReason insertedInsurancePolicyDamageCultivatedLandDamageReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePolicyDamageCultivatedLandDamageReason createEntity() {
        return new InsurancePolicyDamageCultivatedLandDamageReason()
            .name(DEFAULT_NAME)
            .damageCategoryId(DEFAULT_DAMAGE_CATEGORY_ID)
            .damageTypeId(DEFAULT_DAMAGE_TYPE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePolicyDamageCultivatedLandDamageReason createUpdatedEntity() {
        return new InsurancePolicyDamageCultivatedLandDamageReason()
            .name(UPDATED_NAME)
            .damageCategoryId(UPDATED_DAMAGE_CATEGORY_ID)
            .damageTypeId(UPDATED_DAMAGE_TYPE_ID);
    }

    @BeforeEach
    void initTest() {
        insurancePolicyDamageCultivatedLandDamageReason = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsurancePolicyDamageCultivatedLandDamageReason != null) {
            insurancePolicyDamageCultivatedLandDamageReasonRepository.delete(insertedInsurancePolicyDamageCultivatedLandDamageReason);
            insertedInsurancePolicyDamageCultivatedLandDamageReason = null;
        }
    }

    @Test
    @Transactional
    void createInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsurancePolicyDamageCultivatedLandDamageReason
        var returnedInsurancePolicyDamageCultivatedLandDamageReason = om.readValue(
            restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsurancePolicyDamageCultivatedLandDamageReason.class
        );

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsurancePolicyDamageCultivatedLandDamageReasonUpdatableFieldsEquals(
            returnedInsurancePolicyDamageCultivatedLandDamageReason,
            getPersistedInsurancePolicyDamageCultivatedLandDamageReason(returnedInsurancePolicyDamageCultivatedLandDamageReason)
        );

        insertedInsurancePolicyDamageCultivatedLandDamageReason = returnedInsurancePolicyDamageCultivatedLandDamageReason;
    }

    @Test
    @Transactional
    void createInsurancePolicyDamageCultivatedLandDamageReasonWithExistingId() throws Exception {
        // Create the InsurancePolicyDamageCultivatedLandDamageReason with an existing ID
        insurancePolicyDamageCultivatedLandDamageReason.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasons() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePolicyDamageCultivatedLandDamageReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].damageCategoryId").value(hasItem(DEFAULT_DAMAGE_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].damageTypeId").value(hasItem(DEFAULT_DAMAGE_TYPE_ID)));
    }

    @Test
    @Transactional
    void getInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get the insurancePolicyDamageCultivatedLandDamageReason
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, insurancePolicyDamageCultivatedLandDamageReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insurancePolicyDamageCultivatedLandDamageReason.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.damageCategoryId").value(DEFAULT_DAMAGE_CATEGORY_ID))
            .andExpect(jsonPath("$.damageTypeId").value(DEFAULT_DAMAGE_TYPE_ID));
    }

    @Test
    @Transactional
    void getInsurancePolicyDamageCultivatedLandDamageReasonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        Long id = insurancePolicyDamageCultivatedLandDamageReason.getId();

        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where name equals to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where name in
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "name.in=" + DEFAULT_NAME + "," + UPDATED_NAME,
            "name.in=" + UPDATED_NAME
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where name is not null
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where name contains
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where name does not contain
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "name.doesNotContain=" + UPDATED_NAME,
            "name.doesNotContain=" + DEFAULT_NAME
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId equals to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.equals=" + DEFAULT_DAMAGE_CATEGORY_ID,
            "damageCategoryId.equals=" + UPDATED_DAMAGE_CATEGORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId in
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.in=" + DEFAULT_DAMAGE_CATEGORY_ID + "," + UPDATED_DAMAGE_CATEGORY_ID,
            "damageCategoryId.in=" + UPDATED_DAMAGE_CATEGORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId is not null
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.specified=true",
            "damageCategoryId.specified=false"
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId is greater than or equal to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.greaterThanOrEqual=" + DEFAULT_DAMAGE_CATEGORY_ID,
            "damageCategoryId.greaterThanOrEqual=" + UPDATED_DAMAGE_CATEGORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId is less than or equal to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.lessThanOrEqual=" + DEFAULT_DAMAGE_CATEGORY_ID,
            "damageCategoryId.lessThanOrEqual=" + SMALLER_DAMAGE_CATEGORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId is less than
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.lessThan=" + UPDATED_DAMAGE_CATEGORY_ID,
            "damageCategoryId.lessThan=" + DEFAULT_DAMAGE_CATEGORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageCategoryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageCategoryId is greater than
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageCategoryId.greaterThan=" + SMALLER_DAMAGE_CATEGORY_ID,
            "damageCategoryId.greaterThan=" + DEFAULT_DAMAGE_CATEGORY_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId equals to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageTypeId.equals=" + DEFAULT_DAMAGE_TYPE_ID,
            "damageTypeId.equals=" + UPDATED_DAMAGE_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId in
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageTypeId.in=" + DEFAULT_DAMAGE_TYPE_ID + "," + UPDATED_DAMAGE_TYPE_ID,
            "damageTypeId.in=" + UPDATED_DAMAGE_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId is not null
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering("damageTypeId.specified=true", "damageTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId is greater than or equal to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageTypeId.greaterThanOrEqual=" + DEFAULT_DAMAGE_TYPE_ID,
            "damageTypeId.greaterThanOrEqual=" + UPDATED_DAMAGE_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId is less than or equal to
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageTypeId.lessThanOrEqual=" + DEFAULT_DAMAGE_TYPE_ID,
            "damageTypeId.lessThanOrEqual=" + SMALLER_DAMAGE_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId is less than
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageTypeId.lessThan=" + UPDATED_DAMAGE_TYPE_ID,
            "damageTypeId.lessThan=" + DEFAULT_DAMAGE_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllInsurancePolicyDamageCultivatedLandDamageReasonsByDamageTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        // Get all the insurancePolicyDamageCultivatedLandDamageReasonList where damageTypeId is greater than
        defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(
            "damageTypeId.greaterThan=" + SMALLER_DAMAGE_TYPE_ID,
            "damageTypeId.greaterThan=" + DEFAULT_DAMAGE_TYPE_ID
        );
    }

    private void defaultInsurancePolicyDamageCultivatedLandDamageReasonFiltering(String shouldBeFound, String shouldNotBeFound)
        throws Exception {
        defaultInsurancePolicyDamageCultivatedLandDamageReasonShouldBeFound(shouldBeFound);
        defaultInsurancePolicyDamageCultivatedLandDamageReasonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsurancePolicyDamageCultivatedLandDamageReasonShouldBeFound(String filter) throws Exception {
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePolicyDamageCultivatedLandDamageReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].damageCategoryId").value(hasItem(DEFAULT_DAMAGE_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].damageTypeId").value(hasItem(DEFAULT_DAMAGE_TYPE_ID)));

        // Check, that the count call also returns 1
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsurancePolicyDamageCultivatedLandDamageReasonShouldNotBeFound(String filter) throws Exception {
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        // Get the insurancePolicyDamageCultivatedLandDamageReason
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicyDamageCultivatedLandDamageReason
        InsurancePolicyDamageCultivatedLandDamageReason updatedInsurancePolicyDamageCultivatedLandDamageReason =
            insurancePolicyDamageCultivatedLandDamageReasonRepository
                .findById(insurancePolicyDamageCultivatedLandDamageReason.getId())
                .orElseThrow();
        // Disconnect from session so that the updates on updatedInsurancePolicyDamageCultivatedLandDamageReason are not directly saved in db
        em.detach(updatedInsurancePolicyDamageCultivatedLandDamageReason);
        updatedInsurancePolicyDamageCultivatedLandDamageReason
            .name(UPDATED_NAME)
            .damageCategoryId(UPDATED_DAMAGE_CATEGORY_ID)
            .damageTypeId(UPDATED_DAMAGE_TYPE_ID);

        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsurancePolicyDamageCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsurancePolicyDamageCultivatedLandDamageReasonToMatchAllProperties(
            updatedInsurancePolicyDamageCultivatedLandDamageReason
        );
    }

    @Test
    @Transactional
    void putNonExistingInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamageCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancePolicyDamageCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamageCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamageCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsurancePolicyDamageCultivatedLandDamageReasonWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicyDamageCultivatedLandDamageReason using partial update
        InsurancePolicyDamageCultivatedLandDamageReason partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason =
            new InsurancePolicyDamageCultivatedLandDamageReason();
        partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason.setId(insurancePolicyDamageCultivatedLandDamageReason.getId());

        partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason
            .name(UPDATED_NAME)
            .damageCategoryId(UPDATED_DAMAGE_CATEGORY_ID)
            .damageTypeId(UPDATED_DAMAGE_TYPE_ID);

        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePolicyDamageCultivatedLandDamageReasonUpdatableFieldsEquals(
            createUpdateProxyForBean(
                partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason,
                insurancePolicyDamageCultivatedLandDamageReason
            ),
            getPersistedInsurancePolicyDamageCultivatedLandDamageReason(insurancePolicyDamageCultivatedLandDamageReason)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsurancePolicyDamageCultivatedLandDamageReasonWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePolicyDamageCultivatedLandDamageReason using partial update
        InsurancePolicyDamageCultivatedLandDamageReason partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason =
            new InsurancePolicyDamageCultivatedLandDamageReason();
        partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason.setId(insurancePolicyDamageCultivatedLandDamageReason.getId());

        partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason
            .name(UPDATED_NAME)
            .damageCategoryId(UPDATED_DAMAGE_CATEGORY_ID)
            .damageTypeId(UPDATED_DAMAGE_TYPE_ID);

        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePolicyDamageCultivatedLandDamageReasonUpdatableFieldsEquals(
            partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason,
            getPersistedInsurancePolicyDamageCultivatedLandDamageReason(partialUpdatedInsurancePolicyDamageCultivatedLandDamageReason)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamageCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insurancePolicyDamageCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamageCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePolicyDamageCultivatedLandDamageReason.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePolicyDamageCultivatedLandDamageReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePolicyDamageCultivatedLandDamageReason in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsurancePolicyDamageCultivatedLandDamageReason() throws Exception {
        // Initialize the database
        insertedInsurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonRepository.saveAndFlush(
            insurancePolicyDamageCultivatedLandDamageReason
        );

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insurancePolicyDamageCultivatedLandDamageReason
        restInsurancePolicyDamageCultivatedLandDamageReasonMockMvc
            .perform(
                delete(ENTITY_API_URL_ID, insurancePolicyDamageCultivatedLandDamageReason.getId())
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insurancePolicyDamageCultivatedLandDamageReasonRepository.count();
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

    protected InsurancePolicyDamageCultivatedLandDamageReason getPersistedInsurancePolicyDamageCultivatedLandDamageReason(
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) {
        return insurancePolicyDamageCultivatedLandDamageReasonRepository
            .findById(insurancePolicyDamageCultivatedLandDamageReason.getId())
            .orElseThrow();
    }

    protected void assertPersistedInsurancePolicyDamageCultivatedLandDamageReasonToMatchAllProperties(
        InsurancePolicyDamageCultivatedLandDamageReason expectedInsurancePolicyDamageCultivatedLandDamageReason
    ) {
        assertInsurancePolicyDamageCultivatedLandDamageReasonAllPropertiesEquals(
            expectedInsurancePolicyDamageCultivatedLandDamageReason,
            getPersistedInsurancePolicyDamageCultivatedLandDamageReason(expectedInsurancePolicyDamageCultivatedLandDamageReason)
        );
    }

    protected void assertPersistedInsurancePolicyDamageCultivatedLandDamageReasonToMatchUpdatableProperties(
        InsurancePolicyDamageCultivatedLandDamageReason expectedInsurancePolicyDamageCultivatedLandDamageReason
    ) {
        assertInsurancePolicyDamageCultivatedLandDamageReasonAllUpdatablePropertiesEquals(
            expectedInsurancePolicyDamageCultivatedLandDamageReason,
            getPersistedInsurancePolicyDamageCultivatedLandDamageReason(expectedInsurancePolicyDamageCultivatedLandDamageReason)
        );
    }
}
