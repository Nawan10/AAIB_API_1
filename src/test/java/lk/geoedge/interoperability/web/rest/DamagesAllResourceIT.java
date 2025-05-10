package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamagesAllAsserts.*;
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
import lk.geoedge.interoperability.domain.DamagesAll;
import lk.geoedge.interoperability.domain.DamagesCategory;
import lk.geoedge.interoperability.domain.DamagesType;
import lk.geoedge.interoperability.repository.DamagesAllRepository;
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
 * Integration tests for the {@link DamagesAllResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamagesAllResourceIT {

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

    private static final String ENTITY_API_URL = "/api/damages-alls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamagesAllRepository damagesAllRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamagesAllMockMvc;

    private DamagesAll damagesAll;

    private DamagesAll insertedDamagesAll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamagesAll createEntity() {
        return new DamagesAll()
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
    public static DamagesAll createUpdatedEntity() {
        return new DamagesAll()
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
        damagesAll = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamagesAll != null) {
            damagesAllRepository.delete(insertedDamagesAll);
            insertedDamagesAll = null;
        }
    }

    @Test
    @Transactional
    void createDamagesAll() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DamagesAll
        var returnedDamagesAll = om.readValue(
            restDamagesAllMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesAll))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DamagesAll.class
        );

        // Validate the DamagesAll in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamagesAllUpdatableFieldsEquals(returnedDamagesAll, getPersistedDamagesAll(returnedDamagesAll));

        insertedDamagesAll = returnedDamagesAll;
    }

    @Test
    @Transactional
    void createDamagesAllWithExistingId() throws Exception {
        // Create the DamagesAll with an existing ID
        damagesAll.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamagesAllMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesAll)))
            .andExpect(status().isBadRequest());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamagesAlls() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList
        restDamagesAllMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damagesAll.getId().intValue())))
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
    void getDamagesAll() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get the damagesAll
        restDamagesAllMockMvc
            .perform(get(ENTITY_API_URL_ID, damagesAll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damagesAll.getId().intValue()))
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
    void getDamagesAllsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        Long id = damagesAll.getId();

        defaultDamagesAllFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamagesAllFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamagesAllFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageName equals to
        defaultDamagesAllFiltering("damageName.equals=" + DEFAULT_DAMAGE_NAME, "damageName.equals=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageName in
        defaultDamagesAllFiltering(
            "damageName.in=" + DEFAULT_DAMAGE_NAME + "," + UPDATED_DAMAGE_NAME,
            "damageName.in=" + UPDATED_DAMAGE_NAME
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageName is not null
        defaultDamagesAllFiltering("damageName.specified=true", "damageName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageName contains
        defaultDamagesAllFiltering("damageName.contains=" + DEFAULT_DAMAGE_NAME, "damageName.contains=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageName does not contain
        defaultDamagesAllFiltering("damageName.doesNotContain=" + UPDATED_DAMAGE_NAME, "damageName.doesNotContain=" + DEFAULT_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageCode equals to
        defaultDamagesAllFiltering("damageCode.equals=" + DEFAULT_DAMAGE_CODE, "damageCode.equals=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageCode in
        defaultDamagesAllFiltering(
            "damageCode.in=" + DEFAULT_DAMAGE_CODE + "," + UPDATED_DAMAGE_CODE,
            "damageCode.in=" + UPDATED_DAMAGE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageCode is not null
        defaultDamagesAllFiltering("damageCode.specified=true", "damageCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageCode contains
        defaultDamagesAllFiltering("damageCode.contains=" + DEFAULT_DAMAGE_CODE, "damageCode.contains=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageCode does not contain
        defaultDamagesAllFiltering("damageCode.doesNotContain=" + UPDATED_DAMAGE_CODE, "damageCode.doesNotContain=" + DEFAULT_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageFamily equals to
        defaultDamagesAllFiltering("damageFamily.equals=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.equals=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageFamilyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageFamily in
        defaultDamagesAllFiltering(
            "damageFamily.in=" + DEFAULT_DAMAGE_FAMILY + "," + UPDATED_DAMAGE_FAMILY,
            "damageFamily.in=" + UPDATED_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageFamilyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageFamily is not null
        defaultDamagesAllFiltering("damageFamily.specified=true", "damageFamily.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageFamilyContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageFamily contains
        defaultDamagesAllFiltering("damageFamily.contains=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.contains=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageFamilyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageFamily does not contain
        defaultDamagesAllFiltering(
            "damageFamily.doesNotContain=" + UPDATED_DAMAGE_FAMILY,
            "damageFamily.doesNotContain=" + DEFAULT_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageGenusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageGenus equals to
        defaultDamagesAllFiltering("damageGenus.equals=" + DEFAULT_DAMAGE_GENUS, "damageGenus.equals=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageGenusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageGenus in
        defaultDamagesAllFiltering(
            "damageGenus.in=" + DEFAULT_DAMAGE_GENUS + "," + UPDATED_DAMAGE_GENUS,
            "damageGenus.in=" + UPDATED_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageGenusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageGenus is not null
        defaultDamagesAllFiltering("damageGenus.specified=true", "damageGenus.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageGenusContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageGenus contains
        defaultDamagesAllFiltering("damageGenus.contains=" + DEFAULT_DAMAGE_GENUS, "damageGenus.contains=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageGenusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageGenus does not contain
        defaultDamagesAllFiltering(
            "damageGenus.doesNotContain=" + UPDATED_DAMAGE_GENUS,
            "damageGenus.doesNotContain=" + DEFAULT_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageSpeciesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageSpecies equals to
        defaultDamagesAllFiltering("damageSpecies.equals=" + DEFAULT_DAMAGE_SPECIES, "damageSpecies.equals=" + UPDATED_DAMAGE_SPECIES);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageSpeciesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageSpecies in
        defaultDamagesAllFiltering(
            "damageSpecies.in=" + DEFAULT_DAMAGE_SPECIES + "," + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.in=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageSpeciesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageSpecies is not null
        defaultDamagesAllFiltering("damageSpecies.specified=true", "damageSpecies.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageSpeciesContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageSpecies contains
        defaultDamagesAllFiltering("damageSpecies.contains=" + DEFAULT_DAMAGE_SPECIES, "damageSpecies.contains=" + UPDATED_DAMAGE_SPECIES);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageSpeciesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where damageSpecies does not contain
        defaultDamagesAllFiltering(
            "damageSpecies.doesNotContain=" + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.doesNotContain=" + DEFAULT_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt equals to
        defaultDamagesAllFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt in
        defaultDamagesAllFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt is not null
        defaultDamagesAllFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt is greater than or equal to
        defaultDamagesAllFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt is less than or equal to
        defaultDamagesAllFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt is less than
        defaultDamagesAllFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where createdAt is greater than
        defaultDamagesAllFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where addedBy equals to
        defaultDamagesAllFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where addedBy in
        defaultDamagesAllFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where addedBy is not null
        defaultDamagesAllFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDamagesAllsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where addedBy contains
        defaultDamagesAllFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        // Get all the damagesAllList where addedBy does not contain
        defaultDamagesAllFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageCategoryIsEqualToSomething() throws Exception {
        DamagesCategory damageCategory;
        if (TestUtil.findAll(em, DamagesCategory.class).isEmpty()) {
            damagesAllRepository.saveAndFlush(damagesAll);
            damageCategory = DamagesCategoryResourceIT.createEntity();
        } else {
            damageCategory = TestUtil.findAll(em, DamagesCategory.class).get(0);
        }
        em.persist(damageCategory);
        em.flush();
        damagesAll.setDamageCategory(damageCategory);
        damagesAllRepository.saveAndFlush(damagesAll);
        Long damageCategoryId = damageCategory.getId();
        // Get all the damagesAllList where damageCategory equals to damageCategoryId
        defaultDamagesAllShouldBeFound("damageCategoryId.equals=" + damageCategoryId);

        // Get all the damagesAllList where damageCategory equals to (damageCategoryId + 1)
        defaultDamagesAllShouldNotBeFound("damageCategoryId.equals=" + (damageCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllDamagesAllsByDamageTypeIsEqualToSomething() throws Exception {
        DamagesType damageType;
        if (TestUtil.findAll(em, DamagesType.class).isEmpty()) {
            damagesAllRepository.saveAndFlush(damagesAll);
            damageType = DamagesTypeResourceIT.createEntity();
        } else {
            damageType = TestUtil.findAll(em, DamagesType.class).get(0);
        }
        em.persist(damageType);
        em.flush();
        damagesAll.setDamageType(damageType);
        damagesAllRepository.saveAndFlush(damagesAll);
        Long damageTypeId = damageType.getId();
        // Get all the damagesAllList where damageType equals to damageTypeId
        defaultDamagesAllShouldBeFound("damageTypeId.equals=" + damageTypeId);

        // Get all the damagesAllList where damageType equals to (damageTypeId + 1)
        defaultDamagesAllShouldNotBeFound("damageTypeId.equals=" + (damageTypeId + 1));
    }

    private void defaultDamagesAllFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamagesAllShouldBeFound(shouldBeFound);
        defaultDamagesAllShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamagesAllShouldBeFound(String filter) throws Exception {
        restDamagesAllMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damagesAll.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageName").value(hasItem(DEFAULT_DAMAGE_NAME)))
            .andExpect(jsonPath("$.[*].damageCode").value(hasItem(DEFAULT_DAMAGE_CODE)))
            .andExpect(jsonPath("$.[*].damageFamily").value(hasItem(DEFAULT_DAMAGE_FAMILY)))
            .andExpect(jsonPath("$.[*].damageGenus").value(hasItem(DEFAULT_DAMAGE_GENUS)))
            .andExpect(jsonPath("$.[*].damageSpecies").value(hasItem(DEFAULT_DAMAGE_SPECIES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restDamagesAllMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamagesAllShouldNotBeFound(String filter) throws Exception {
        restDamagesAllMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamagesAllMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamagesAll() throws Exception {
        // Get the damagesAll
        restDamagesAllMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamagesAll() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesAll
        DamagesAll updatedDamagesAll = damagesAllRepository.findById(damagesAll.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamagesAll are not directly saved in db
        em.detach(updatedDamagesAll);
        updatedDamagesAll
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restDamagesAllMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamagesAll.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamagesAll))
            )
            .andExpect(status().isOk());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamagesAllToMatchAllProperties(updatedDamagesAll);
    }

    @Test
    @Transactional
    void putNonExistingDamagesAll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesAll.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamagesAllMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damagesAll.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damagesAll))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamagesAll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesAll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesAllMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damagesAll))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamagesAll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesAll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesAllMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damagesAll)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamagesAllWithPatch() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesAll using partial update
        DamagesAll partialUpdatedDamagesAll = new DamagesAll();
        partialUpdatedDamagesAll.setId(damagesAll.getId());

        partialUpdatedDamagesAll
            .damageName(UPDATED_DAMAGE_NAME)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .addedBy(UPDATED_ADDED_BY);

        restDamagesAllMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamagesAll.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamagesAll))
            )
            .andExpect(status().isOk());

        // Validate the DamagesAll in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamagesAllUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDamagesAll, damagesAll),
            getPersistedDamagesAll(damagesAll)
        );
    }

    @Test
    @Transactional
    void fullUpdateDamagesAllWithPatch() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damagesAll using partial update
        DamagesAll partialUpdatedDamagesAll = new DamagesAll();
        partialUpdatedDamagesAll.setId(damagesAll.getId());

        partialUpdatedDamagesAll
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restDamagesAllMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamagesAll.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamagesAll))
            )
            .andExpect(status().isOk());

        // Validate the DamagesAll in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamagesAllUpdatableFieldsEquals(partialUpdatedDamagesAll, getPersistedDamagesAll(partialUpdatedDamagesAll));
    }

    @Test
    @Transactional
    void patchNonExistingDamagesAll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesAll.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamagesAllMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damagesAll.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesAll))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamagesAll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesAll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesAllMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damagesAll))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamagesAll() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damagesAll.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamagesAllMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(damagesAll))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamagesAll in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamagesAll() throws Exception {
        // Initialize the database
        insertedDamagesAll = damagesAllRepository.saveAndFlush(damagesAll);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damagesAll
        restDamagesAllMockMvc
            .perform(delete(ENTITY_API_URL_ID, damagesAll.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damagesAllRepository.count();
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

    protected DamagesAll getPersistedDamagesAll(DamagesAll damagesAll) {
        return damagesAllRepository.findById(damagesAll.getId()).orElseThrow();
    }

    protected void assertPersistedDamagesAllToMatchAllProperties(DamagesAll expectedDamagesAll) {
        assertDamagesAllAllPropertiesEquals(expectedDamagesAll, getPersistedDamagesAll(expectedDamagesAll));
    }

    protected void assertPersistedDamagesAllToMatchUpdatableProperties(DamagesAll expectedDamagesAll) {
        assertDamagesAllAllUpdatablePropertiesEquals(expectedDamagesAll, getPersistedDamagesAll(expectedDamagesAll));
    }
}
