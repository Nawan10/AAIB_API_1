package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.DamageEntityAsserts.*;
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
import lk.geoedge.interoperability.domain.DamageCategory;
import lk.geoedge.interoperability.domain.DamageEntity;
import lk.geoedge.interoperability.domain.DamageType;
import lk.geoedge.interoperability.repository.DamageEntityRepository;
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
 * Integration tests for the {@link DamageEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DamageEntityResourceIT {

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

    private static final String ENTITY_API_URL = "/api/damage-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DamageEntityRepository damageEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDamageEntityMockMvc;

    private DamageEntity damageEntity;

    private DamageEntity insertedDamageEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DamageEntity createEntity() {
        return new DamageEntity()
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
    public static DamageEntity createUpdatedEntity() {
        return new DamageEntity()
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
        damageEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDamageEntity != null) {
            damageEntityRepository.delete(insertedDamageEntity);
            insertedDamageEntity = null;
        }
    }

    @Test
    @Transactional
    void createDamageEntity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DamageEntity
        var returnedDamageEntity = om.readValue(
            restDamageEntityMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageEntity))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DamageEntity.class
        );

        // Validate the DamageEntity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDamageEntityUpdatableFieldsEquals(returnedDamageEntity, getPersistedDamageEntity(returnedDamageEntity));

        insertedDamageEntity = returnedDamageEntity;
    }

    @Test
    @Transactional
    void createDamageEntityWithExistingId() throws Exception {
        // Create the DamageEntity with an existing ID
        damageEntity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDamageEntityMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageEntity)))
            .andExpect(status().isBadRequest());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDamageEntities() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList
        restDamageEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damageEntity.getId().intValue())))
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
    void getDamageEntity() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get the damageEntity
        restDamageEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, damageEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(damageEntity.getId().intValue()))
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
    void getDamageEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        Long id = damageEntity.getId();

        defaultDamageEntityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDamageEntityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDamageEntityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageName equals to
        defaultDamageEntityFiltering("damageName.equals=" + DEFAULT_DAMAGE_NAME, "damageName.equals=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageName in
        defaultDamageEntityFiltering(
            "damageName.in=" + DEFAULT_DAMAGE_NAME + "," + UPDATED_DAMAGE_NAME,
            "damageName.in=" + UPDATED_DAMAGE_NAME
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageName is not null
        defaultDamageEntityFiltering("damageName.specified=true", "damageName.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageName contains
        defaultDamageEntityFiltering("damageName.contains=" + DEFAULT_DAMAGE_NAME, "damageName.contains=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageName does not contain
        defaultDamageEntityFiltering(
            "damageName.doesNotContain=" + UPDATED_DAMAGE_NAME,
            "damageName.doesNotContain=" + DEFAULT_DAMAGE_NAME
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageCode equals to
        defaultDamageEntityFiltering("damageCode.equals=" + DEFAULT_DAMAGE_CODE, "damageCode.equals=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageCode in
        defaultDamageEntityFiltering(
            "damageCode.in=" + DEFAULT_DAMAGE_CODE + "," + UPDATED_DAMAGE_CODE,
            "damageCode.in=" + UPDATED_DAMAGE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageCode is not null
        defaultDamageEntityFiltering("damageCode.specified=true", "damageCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageCode contains
        defaultDamageEntityFiltering("damageCode.contains=" + DEFAULT_DAMAGE_CODE, "damageCode.contains=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageCode does not contain
        defaultDamageEntityFiltering(
            "damageCode.doesNotContain=" + UPDATED_DAMAGE_CODE,
            "damageCode.doesNotContain=" + DEFAULT_DAMAGE_CODE
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageFamily equals to
        defaultDamageEntityFiltering("damageFamily.equals=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.equals=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageFamilyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageFamily in
        defaultDamageEntityFiltering(
            "damageFamily.in=" + DEFAULT_DAMAGE_FAMILY + "," + UPDATED_DAMAGE_FAMILY,
            "damageFamily.in=" + UPDATED_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageFamilyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageFamily is not null
        defaultDamageEntityFiltering("damageFamily.specified=true", "damageFamily.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageFamilyContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageFamily contains
        defaultDamageEntityFiltering("damageFamily.contains=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.contains=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageFamilyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageFamily does not contain
        defaultDamageEntityFiltering(
            "damageFamily.doesNotContain=" + UPDATED_DAMAGE_FAMILY,
            "damageFamily.doesNotContain=" + DEFAULT_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageGenusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageGenus equals to
        defaultDamageEntityFiltering("damageGenus.equals=" + DEFAULT_DAMAGE_GENUS, "damageGenus.equals=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageGenusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageGenus in
        defaultDamageEntityFiltering(
            "damageGenus.in=" + DEFAULT_DAMAGE_GENUS + "," + UPDATED_DAMAGE_GENUS,
            "damageGenus.in=" + UPDATED_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageGenusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageGenus is not null
        defaultDamageEntityFiltering("damageGenus.specified=true", "damageGenus.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageGenusContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageGenus contains
        defaultDamageEntityFiltering("damageGenus.contains=" + DEFAULT_DAMAGE_GENUS, "damageGenus.contains=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageGenusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageGenus does not contain
        defaultDamageEntityFiltering(
            "damageGenus.doesNotContain=" + UPDATED_DAMAGE_GENUS,
            "damageGenus.doesNotContain=" + DEFAULT_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageSpeciesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageSpecies equals to
        defaultDamageEntityFiltering("damageSpecies.equals=" + DEFAULT_DAMAGE_SPECIES, "damageSpecies.equals=" + UPDATED_DAMAGE_SPECIES);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageSpeciesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageSpecies in
        defaultDamageEntityFiltering(
            "damageSpecies.in=" + DEFAULT_DAMAGE_SPECIES + "," + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.in=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageSpeciesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageSpecies is not null
        defaultDamageEntityFiltering("damageSpecies.specified=true", "damageSpecies.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageSpeciesContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageSpecies contains
        defaultDamageEntityFiltering(
            "damageSpecies.contains=" + DEFAULT_DAMAGE_SPECIES,
            "damageSpecies.contains=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageSpeciesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where damageSpecies does not contain
        defaultDamageEntityFiltering(
            "damageSpecies.doesNotContain=" + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.doesNotContain=" + DEFAULT_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt equals to
        defaultDamageEntityFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt in
        defaultDamageEntityFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt is not null
        defaultDamageEntityFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt is greater than or equal to
        defaultDamageEntityFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt is less than or equal to
        defaultDamageEntityFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt is less than
        defaultDamageEntityFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where createdAt is greater than
        defaultDamageEntityFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where addedBy equals to
        defaultDamageEntityFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where addedBy in
        defaultDamageEntityFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where addedBy is not null
        defaultDamageEntityFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where addedBy contains
        defaultDamageEntityFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        // Get all the damageEntityList where addedBy does not contain
        defaultDamageEntityFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageCategoryIsEqualToSomething() throws Exception {
        DamageCategory damageCategory;
        if (TestUtil.findAll(em, DamageCategory.class).isEmpty()) {
            damageEntityRepository.saveAndFlush(damageEntity);
            damageCategory = DamageCategoryResourceIT.createEntity();
        } else {
            damageCategory = TestUtil.findAll(em, DamageCategory.class).get(0);
        }
        em.persist(damageCategory);
        em.flush();
        damageEntity.setDamageCategory(damageCategory);
        damageEntityRepository.saveAndFlush(damageEntity);
        Long damageCategoryId = damageCategory.getId();
        // Get all the damageEntityList where damageCategory equals to damageCategoryId
        defaultDamageEntityShouldBeFound("damageCategoryId.equals=" + damageCategoryId);

        // Get all the damageEntityList where damageCategory equals to (damageCategoryId + 1)
        defaultDamageEntityShouldNotBeFound("damageCategoryId.equals=" + (damageCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllDamageEntitiesByDamageTypeIsEqualToSomething() throws Exception {
        DamageType damageType;
        if (TestUtil.findAll(em, DamageType.class).isEmpty()) {
            damageEntityRepository.saveAndFlush(damageEntity);
            damageType = DamageTypeResourceIT.createEntity();
        } else {
            damageType = TestUtil.findAll(em, DamageType.class).get(0);
        }
        em.persist(damageType);
        em.flush();
        damageEntity.setDamageType(damageType);
        damageEntityRepository.saveAndFlush(damageEntity);
        Long damageTypeId = damageType.getId();
        // Get all the damageEntityList where damageType equals to damageTypeId
        defaultDamageEntityShouldBeFound("damageTypeId.equals=" + damageTypeId);

        // Get all the damageEntityList where damageType equals to (damageTypeId + 1)
        defaultDamageEntityShouldNotBeFound("damageTypeId.equals=" + (damageTypeId + 1));
    }

    private void defaultDamageEntityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDamageEntityShouldBeFound(shouldBeFound);
        defaultDamageEntityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDamageEntityShouldBeFound(String filter) throws Exception {
        restDamageEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(damageEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageName").value(hasItem(DEFAULT_DAMAGE_NAME)))
            .andExpect(jsonPath("$.[*].damageCode").value(hasItem(DEFAULT_DAMAGE_CODE)))
            .andExpect(jsonPath("$.[*].damageFamily").value(hasItem(DEFAULT_DAMAGE_FAMILY)))
            .andExpect(jsonPath("$.[*].damageGenus").value(hasItem(DEFAULT_DAMAGE_GENUS)))
            .andExpect(jsonPath("$.[*].damageSpecies").value(hasItem(DEFAULT_DAMAGE_SPECIES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restDamageEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDamageEntityShouldNotBeFound(String filter) throws Exception {
        restDamageEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDamageEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDamageEntity() throws Exception {
        // Get the damageEntity
        restDamageEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDamageEntity() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageEntity
        DamageEntity updatedDamageEntity = damageEntityRepository.findById(damageEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDamageEntity are not directly saved in db
        em.detach(updatedDamageEntity);
        updatedDamageEntity
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restDamageEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDamageEntity.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDamageEntity))
            )
            .andExpect(status().isOk());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDamageEntityToMatchAllProperties(updatedDamageEntity);
    }

    @Test
    @Transactional
    void putNonExistingDamageEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, damageEntity.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damageEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDamageEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(damageEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDamageEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageEntityMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(damageEntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDamageEntityWithPatch() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageEntity using partial update
        DamageEntity partialUpdatedDamageEntity = new DamageEntity();
        partialUpdatedDamageEntity.setId(damageEntity.getId());

        partialUpdatedDamageEntity.damageCode(UPDATED_DAMAGE_CODE);

        restDamageEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamageEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamageEntity))
            )
            .andExpect(status().isOk());

        // Validate the DamageEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDamageEntity, damageEntity),
            getPersistedDamageEntity(damageEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateDamageEntityWithPatch() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the damageEntity using partial update
        DamageEntity partialUpdatedDamageEntity = new DamageEntity();
        partialUpdatedDamageEntity.setId(damageEntity.getId());

        partialUpdatedDamageEntity
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restDamageEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDamageEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDamageEntity))
            )
            .andExpect(status().isOk());

        // Validate the DamageEntity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDamageEntityUpdatableFieldsEquals(partialUpdatedDamageEntity, getPersistedDamageEntity(partialUpdatedDamageEntity));
    }

    @Test
    @Transactional
    void patchNonExistingDamageEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageEntity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDamageEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, damageEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damageEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDamageEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(damageEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDamageEntity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        damageEntity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDamageEntityMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(damageEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DamageEntity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDamageEntity() throws Exception {
        // Initialize the database
        insertedDamageEntity = damageEntityRepository.saveAndFlush(damageEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the damageEntity
        restDamageEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, damageEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return damageEntityRepository.count();
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

    protected DamageEntity getPersistedDamageEntity(DamageEntity damageEntity) {
        return damageEntityRepository.findById(damageEntity.getId()).orElseThrow();
    }

    protected void assertPersistedDamageEntityToMatchAllProperties(DamageEntity expectedDamageEntity) {
        assertDamageEntityAllPropertiesEquals(expectedDamageEntity, getPersistedDamageEntity(expectedDamageEntity));
    }

    protected void assertPersistedDamageEntityToMatchUpdatableProperties(DamageEntity expectedDamageEntity) {
        assertDamageEntityAllUpdatablePropertiesEquals(expectedDamageEntity, getPersistedDamageEntity(expectedDamageEntity));
    }
}
