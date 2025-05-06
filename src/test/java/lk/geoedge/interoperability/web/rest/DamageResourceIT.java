package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamageAsserts.*;
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
import lk.geoedge.interoperability.domain.Damage;
import lk.geoedge.interoperability.domain.DamageCategory;
import lk.geoedge.interoperability.domain.DamageType;
import lk.geoedge.interoperability.repository.DamageRepository;
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
 * Integration tests for the {@link DamageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamageResourceIT {

    private static final String DEFAULT_DAMAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DAMAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DAMAGE_FAMILY = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_FAMILY = "BBBBBBBBBB";

    private static final String DEFAULT_DAMAGE_GENUS = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_GENUS = "BBBBBBBBBB";

    private static final String DEFAULT_DAMAGE_SPECIES = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_SPECIES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/damages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamageRepository damageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamageMockMvc;

    private Damage damage;

    private Damage insertedDamage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Damage createEntity() {
        return new Damage()
            .damageName(DEFAULT_DAMAGE_NAME)
            .damageCode(DEFAULT_DAMAGE_CODE)
            .damageFamily(DEFAULT_DAMAGE_FAMILY)
            .damageGenus(DEFAULT_DAMAGE_GENUS)
            .damageSpecies(DEFAULT_DAMAGE_SPECIES)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Damage createUpdatedEntity() {
        return new Damage()
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        damage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamage != null) {
            damageRepository.delete(insertedDamage);
            insertedDamage = null;
        }
    }

    @Test
    @Transactional
    void createDamage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Damage
        var returnedDamage = om.readValue(
            restDamageMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damage)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Damage.class
        );

        // Validate the Damage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamageUpdatableFieldsEquals(returnedDamage, getPersistedDamage(returnedDamage));

        insertedDamage = returnedDamage;
    }

    @Test
    @Transactional
    void createDamageWithExistingId() throws Exception {
        // Create the Damage with an existing ID
        damage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamageMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damage)))
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamages() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList
        restDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damage.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageName").value(hasItem(DEFAULT_DAMAGE_NAME)))
            .andExpect(jsonPath("$.[*].damageCode").value(hasItem(DEFAULT_DAMAGE_CODE)))
            .andExpect(jsonPath("$.[*].damageFamily").value(hasItem(DEFAULT_DAMAGE_FAMILY)))
            .andExpect(jsonPath("$.[*].damageGenus").value(hasItem(DEFAULT_DAMAGE_GENUS)))
            .andExpect(jsonPath("$.[*].damageSpecies").value(hasItem(DEFAULT_DAMAGE_SPECIES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getDamage() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get the damage
        restDamageMockMvc
            .perform(get(ENTITY_API_URL_ID, damage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damage.getId().intValue()))
            .andExpect(jsonPath("$.damageName").value(DEFAULT_DAMAGE_NAME))
            .andExpect(jsonPath("$.damageCode").value(DEFAULT_DAMAGE_CODE))
            .andExpect(jsonPath("$.damageFamily").value(DEFAULT_DAMAGE_FAMILY))
            .andExpect(jsonPath("$.damageGenus").value(DEFAULT_DAMAGE_GENUS))
            .andExpect(jsonPath("$.damageSpecies").value(DEFAULT_DAMAGE_SPECIES))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getDamagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        Long id = damage.getId();

        defaultDamageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageName equals to
        defaultDamageFiltering("damageName.equals=" + DEFAULT_DAMAGE_NAME, "damageName.equals=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageName in
        defaultDamageFiltering("damageName.in=" + DEFAULT_DAMAGE_NAME + "," + UPDATED_DAMAGE_NAME, "damageName.in=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageName is not null
        defaultDamageFiltering("damageName.specified=true", "damageName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByDamageNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageName contains
        defaultDamageFiltering("damageName.contains=" + DEFAULT_DAMAGE_NAME, "damageName.contains=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageName does not contain
        defaultDamageFiltering("damageName.doesNotContain=" + UPDATED_DAMAGE_NAME, "damageName.doesNotContain=" + DEFAULT_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageCode equals to
        defaultDamageFiltering("damageCode.equals=" + DEFAULT_DAMAGE_CODE, "damageCode.equals=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageCode in
        defaultDamageFiltering("damageCode.in=" + DEFAULT_DAMAGE_CODE + "," + UPDATED_DAMAGE_CODE, "damageCode.in=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageCode is not null
        defaultDamageFiltering("damageCode.specified=true", "damageCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByDamageCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageCode contains
        defaultDamageFiltering("damageCode.contains=" + DEFAULT_DAMAGE_CODE, "damageCode.contains=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageCode does not contain
        defaultDamageFiltering("damageCode.doesNotContain=" + UPDATED_DAMAGE_CODE, "damageCode.doesNotContain=" + DEFAULT_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageFamily equals to
        defaultDamageFiltering("damageFamily.equals=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.equals=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageFamilyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageFamily in
        defaultDamageFiltering(
            "damageFamily.in=" + DEFAULT_DAMAGE_FAMILY + "," + UPDATED_DAMAGE_FAMILY,
            "damageFamily.in=" + UPDATED_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllDamagesByDamageFamilyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageFamily is not null
        defaultDamageFiltering("damageFamily.specified=true", "damageFamily.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByDamageFamilyContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageFamily contains
        defaultDamageFiltering("damageFamily.contains=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.contains=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageFamilyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageFamily does not contain
        defaultDamageFiltering(
            "damageFamily.doesNotContain=" + UPDATED_DAMAGE_FAMILY,
            "damageFamily.doesNotContain=" + DEFAULT_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllDamagesByDamageGenusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageGenus equals to
        defaultDamageFiltering("damageGenus.equals=" + DEFAULT_DAMAGE_GENUS, "damageGenus.equals=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageGenusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageGenus in
        defaultDamageFiltering(
            "damageGenus.in=" + DEFAULT_DAMAGE_GENUS + "," + UPDATED_DAMAGE_GENUS,
            "damageGenus.in=" + UPDATED_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllDamagesByDamageGenusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageGenus is not null
        defaultDamageFiltering("damageGenus.specified=true", "damageGenus.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByDamageGenusContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageGenus contains
        defaultDamageFiltering("damageGenus.contains=" + DEFAULT_DAMAGE_GENUS, "damageGenus.contains=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageGenusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageGenus does not contain
        defaultDamageFiltering("damageGenus.doesNotContain=" + UPDATED_DAMAGE_GENUS, "damageGenus.doesNotContain=" + DEFAULT_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageSpeciesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageSpecies equals to
        defaultDamageFiltering("damageSpecies.equals=" + DEFAULT_DAMAGE_SPECIES, "damageSpecies.equals=" + UPDATED_DAMAGE_SPECIES);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageSpeciesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageSpecies in
        defaultDamageFiltering(
            "damageSpecies.in=" + DEFAULT_DAMAGE_SPECIES + "," + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.in=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamagesByDamageSpeciesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageSpecies is not null
        defaultDamageFiltering("damageSpecies.specified=true", "damageSpecies.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByDamageSpeciesContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageSpecies contains
        defaultDamageFiltering("damageSpecies.contains=" + DEFAULT_DAMAGE_SPECIES, "damageSpecies.contains=" + UPDATED_DAMAGE_SPECIES);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageSpeciesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where damageSpecies does not contain
        defaultDamageFiltering(
            "damageSpecies.doesNotContain=" + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.doesNotContain=" + DEFAULT_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt equals to
        defaultDamageFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt in
        defaultDamageFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt is not null
        defaultDamageFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt is greater than or equal to
        defaultDamageFiltering("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt is less than or equal to
        defaultDamageFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt is less than
        defaultDamageFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where createdAt is greater than
        defaultDamageFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where addedBy equals to
        defaultDamageFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where addedBy in
        defaultDamageFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where addedBy is not null
        defaultDamageFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where addedBy contains
        defaultDamageFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        // Get all the damageList where addedBy does not contain
        defaultDamageFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesByDamageCategoryIsEqualToSomething() throws Exception {
        DamageCategory damageCategory;
        if (TestUtil.findAll(em, DamageCategory.class).isEmpty()) {
            damageRepository.saveAndFlush(damage);
            damageCategory = DamageCategoryResourceIT.createEntity();
        } else {
            damageCategory = TestUtil.findAll(em, DamageCategory.class).get(0);
        }
        em.persist(damageCategory);
        em.flush();
        damage.setDamageCategory(damageCategory);
        damageRepository.saveAndFlush(damage);
        Long damageCategoryId = damageCategory.getId();
        // Get all the damageList where damageCategory equals to damageCategoryId
        defaultDamageShouldBeFound("damageCategoryId.equals=" + damageCategoryId);

        // Get all the damageList where damageCategory equals to (damageCategoryId + 1)
        defaultDamageShouldNotBeFound("damageCategoryId.equals=" + (damageCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllDamagesByDamageTypeIsEqualToSomething() throws Exception {
        DamageType damageType;
        if (TestUtil.findAll(em, DamageType.class).isEmpty()) {
            damageRepository.saveAndFlush(damage);
            damageType = DamageTypeResourceIT.createEntity();
        } else {
            damageType = TestUtil.findAll(em, DamageType.class).get(0);
        }
        em.persist(damageType);
        em.flush();
        damage.setDamageType(damageType);
        damageRepository.saveAndFlush(damage);
        Long damageTypeId = damageType.getId();
        // Get all the damageList where damageType equals to damageTypeId
        defaultDamageShouldBeFound("damageTypeId.equals=" + damageTypeId);

        // Get all the damageList where damageType equals to (damageTypeId + 1)
        defaultDamageShouldNotBeFound("damageTypeId.equals=" + (damageTypeId + 1));
    }

    private void defaultDamageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamageShouldBeFound(shouldBeFound);
        defaultDamageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamageShouldBeFound(String filter) throws Exception {
        restDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damage.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageName").value(hasItem(DEFAULT_DAMAGE_NAME)))
            .andExpect(jsonPath("$.[*].damageCode").value(hasItem(DEFAULT_DAMAGE_CODE)))
            .andExpect(jsonPath("$.[*].damageFamily").value(hasItem(DEFAULT_DAMAGE_FAMILY)))
            .andExpect(jsonPath("$.[*].damageGenus").value(hasItem(DEFAULT_DAMAGE_GENUS)))
            .andExpect(jsonPath("$.[*].damageSpecies").value(hasItem(DEFAULT_DAMAGE_SPECIES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamageShouldNotBeFound(String filter) throws Exception {
        restDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamage() throws Exception {
        // Get the damage
        restDamageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamage() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damage
        Damage updatedDamage = damageRepository.findById(damage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamage are not directly saved in db
        em.detach(updatedDamage);
        updatedDamage
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamage))
            )
            .andExpect(status().isOk());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamageToMatchAllProperties(updatedDamage);
    }

    @Test
    @Transactional
    void putNonExistingDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamageWithPatch() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damage using partial update
        Damage partialUpdatedDamage = new Damage();
        partialUpdatedDamage.setId(damage.getId());

        partialUpdatedDamage
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .addedBy(UPDATED_ADDED_BY);

        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamage))
            )
            .andExpect(status().isOk());

        // Validate the Damage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDamage, damage), getPersistedDamage(damage));
    }

    @Test
    @Transactional
    void fullUpdateDamageWithPatch() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damage using partial update
        Damage partialUpdatedDamage = new Damage();
        partialUpdatedDamage.setId(damage.getId());

        partialUpdatedDamage
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamage))
            )
            .andExpect(status().isOk());

        // Validate the Damage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageUpdatableFieldsEquals(partialUpdatedDamage, getPersistedDamage(partialUpdatedDamage));
    }

    @Test
    @Transactional
    void patchNonExistingDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(damage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Damage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamage() throws Exception {
        // Initialize the database
        insertedDamage = damageRepository.saveAndFlush(damage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damage
        restDamageMockMvc
            .perform(delete(ENTITY_API_URL_ID, damage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damageRepository.count();
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

    protected Damage getPersistedDamage(Damage damage) {
        return damageRepository.findById(damage.getId()).orElseThrow();
    }

    protected void assertPersistedDamageToMatchAllProperties(Damage expectedDamage) {
        assertDamageAllPropertiesEquals(expectedDamage, getPersistedDamage(expectedDamage));
    }

    protected void assertPersistedDamageToMatchUpdatableProperties(Damage expectedDamage) {
        assertDamageAllUpdatablePropertiesEquals(expectedDamage, getPersistedDamage(expectedDamage));
    }
}
