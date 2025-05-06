package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CropDamageDamageAsserts.*;
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
import lk.geoedge.interoperability.domain.CropDamageDamage;
import lk.geoedge.interoperability.repository.CropDamageDamageRepository;
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
 * Integration tests for the {@link CropDamageDamageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropDamageDamageResourceIT {

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

    private static final String ENTITY_API_URL = "/api/crop-damage-damages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropDamageDamageRepository cropDamageDamageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropDamageDamageMockMvc;

    private CropDamageDamage cropDamageDamage;

    private CropDamageDamage insertedCropDamageDamage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropDamageDamage createEntity() {
        return new CropDamageDamage()
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
    public static CropDamageDamage createUpdatedEntity() {
        return new CropDamageDamage()
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
        cropDamageDamage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropDamageDamage != null) {
            cropDamageDamageRepository.delete(insertedCropDamageDamage);
            insertedCropDamageDamage = null;
        }
    }

    @Test
    @Transactional
    void createCropDamageDamage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropDamageDamage
        var returnedCropDamageDamage = om.readValue(
            restCropDamageDamageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cropDamageDamage))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropDamageDamage.class
        );

        // Validate the CropDamageDamage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCropDamageDamageUpdatableFieldsEquals(returnedCropDamageDamage, getPersistedCropDamageDamage(returnedCropDamageDamage));

        insertedCropDamageDamage = returnedCropDamageDamage;
    }

    @Test
    @Transactional
    void createCropDamageDamageWithExistingId() throws Exception {
        // Create the CropDamageDamage with an existing ID
        cropDamageDamage.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropDamageDamageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCropDamageDamages() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList
        restCropDamageDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDamageDamage.getId().intValue())))
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
    void getCropDamageDamage() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get the cropDamageDamage
        restCropDamageDamageMockMvc
            .perform(get(ENTITY_API_URL_ID, cropDamageDamage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropDamageDamage.getId().intValue()))
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
    void getCropDamageDamagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        Long id = cropDamageDamage.getId();

        defaultCropDamageDamageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropDamageDamageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropDamageDamageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageName equals to
        defaultCropDamageDamageFiltering("damageName.equals=" + DEFAULT_DAMAGE_NAME, "damageName.equals=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageName in
        defaultCropDamageDamageFiltering(
            "damageName.in=" + DEFAULT_DAMAGE_NAME + "," + UPDATED_DAMAGE_NAME,
            "damageName.in=" + UPDATED_DAMAGE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageName is not null
        defaultCropDamageDamageFiltering("damageName.specified=true", "damageName.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageName contains
        defaultCropDamageDamageFiltering("damageName.contains=" + DEFAULT_DAMAGE_NAME, "damageName.contains=" + UPDATED_DAMAGE_NAME);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageName does not contain
        defaultCropDamageDamageFiltering(
            "damageName.doesNotContain=" + UPDATED_DAMAGE_NAME,
            "damageName.doesNotContain=" + DEFAULT_DAMAGE_NAME
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageCode equals to
        defaultCropDamageDamageFiltering("damageCode.equals=" + DEFAULT_DAMAGE_CODE, "damageCode.equals=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageCode in
        defaultCropDamageDamageFiltering(
            "damageCode.in=" + DEFAULT_DAMAGE_CODE + "," + UPDATED_DAMAGE_CODE,
            "damageCode.in=" + UPDATED_DAMAGE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageCode is not null
        defaultCropDamageDamageFiltering("damageCode.specified=true", "damageCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageCode contains
        defaultCropDamageDamageFiltering("damageCode.contains=" + DEFAULT_DAMAGE_CODE, "damageCode.contains=" + UPDATED_DAMAGE_CODE);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageCode does not contain
        defaultCropDamageDamageFiltering(
            "damageCode.doesNotContain=" + UPDATED_DAMAGE_CODE,
            "damageCode.doesNotContain=" + DEFAULT_DAMAGE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageFamily equals to
        defaultCropDamageDamageFiltering("damageFamily.equals=" + DEFAULT_DAMAGE_FAMILY, "damageFamily.equals=" + UPDATED_DAMAGE_FAMILY);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageFamilyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageFamily in
        defaultCropDamageDamageFiltering(
            "damageFamily.in=" + DEFAULT_DAMAGE_FAMILY + "," + UPDATED_DAMAGE_FAMILY,
            "damageFamily.in=" + UPDATED_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageFamilyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageFamily is not null
        defaultCropDamageDamageFiltering("damageFamily.specified=true", "damageFamily.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageFamilyContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageFamily contains
        defaultCropDamageDamageFiltering(
            "damageFamily.contains=" + DEFAULT_DAMAGE_FAMILY,
            "damageFamily.contains=" + UPDATED_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageFamilyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageFamily does not contain
        defaultCropDamageDamageFiltering(
            "damageFamily.doesNotContain=" + UPDATED_DAMAGE_FAMILY,
            "damageFamily.doesNotContain=" + DEFAULT_DAMAGE_FAMILY
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageGenusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageGenus equals to
        defaultCropDamageDamageFiltering("damageGenus.equals=" + DEFAULT_DAMAGE_GENUS, "damageGenus.equals=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageGenusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageGenus in
        defaultCropDamageDamageFiltering(
            "damageGenus.in=" + DEFAULT_DAMAGE_GENUS + "," + UPDATED_DAMAGE_GENUS,
            "damageGenus.in=" + UPDATED_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageGenusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageGenus is not null
        defaultCropDamageDamageFiltering("damageGenus.specified=true", "damageGenus.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageGenusContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageGenus contains
        defaultCropDamageDamageFiltering("damageGenus.contains=" + DEFAULT_DAMAGE_GENUS, "damageGenus.contains=" + UPDATED_DAMAGE_GENUS);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageGenusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageGenus does not contain
        defaultCropDamageDamageFiltering(
            "damageGenus.doesNotContain=" + UPDATED_DAMAGE_GENUS,
            "damageGenus.doesNotContain=" + DEFAULT_DAMAGE_GENUS
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageSpeciesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageSpecies equals to
        defaultCropDamageDamageFiltering(
            "damageSpecies.equals=" + DEFAULT_DAMAGE_SPECIES,
            "damageSpecies.equals=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageSpeciesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageSpecies in
        defaultCropDamageDamageFiltering(
            "damageSpecies.in=" + DEFAULT_DAMAGE_SPECIES + "," + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.in=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageSpeciesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageSpecies is not null
        defaultCropDamageDamageFiltering("damageSpecies.specified=true", "damageSpecies.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageSpeciesContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageSpecies contains
        defaultCropDamageDamageFiltering(
            "damageSpecies.contains=" + DEFAULT_DAMAGE_SPECIES,
            "damageSpecies.contains=" + UPDATED_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByDamageSpeciesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where damageSpecies does not contain
        defaultCropDamageDamageFiltering(
            "damageSpecies.doesNotContain=" + UPDATED_DAMAGE_SPECIES,
            "damageSpecies.doesNotContain=" + DEFAULT_DAMAGE_SPECIES
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt equals to
        defaultCropDamageDamageFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt in
        defaultCropDamageDamageFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt is not null
        defaultCropDamageDamageFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt is greater than or equal to
        defaultCropDamageDamageFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt is less than or equal to
        defaultCropDamageDamageFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt is less than
        defaultCropDamageDamageFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where createdAt is greater than
        defaultCropDamageDamageFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where addedBy equals to
        defaultCropDamageDamageFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where addedBy in
        defaultCropDamageDamageFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where addedBy is not null
        defaultCropDamageDamageFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where addedBy contains
        defaultCropDamageDamageFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCropDamageDamagesByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        // Get all the cropDamageDamageList where addedBy does not contain
        defaultCropDamageDamageFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    private void defaultCropDamageDamageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropDamageDamageShouldBeFound(shouldBeFound);
        defaultCropDamageDamageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropDamageDamageShouldBeFound(String filter) throws Exception {
        restCropDamageDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropDamageDamage.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageName").value(hasItem(DEFAULT_DAMAGE_NAME)))
            .andExpect(jsonPath("$.[*].damageCode").value(hasItem(DEFAULT_DAMAGE_CODE)))
            .andExpect(jsonPath("$.[*].damageFamily").value(hasItem(DEFAULT_DAMAGE_FAMILY)))
            .andExpect(jsonPath("$.[*].damageGenus").value(hasItem(DEFAULT_DAMAGE_GENUS)))
            .andExpect(jsonPath("$.[*].damageSpecies").value(hasItem(DEFAULT_DAMAGE_SPECIES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCropDamageDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropDamageDamageShouldNotBeFound(String filter) throws Exception {
        restCropDamageDamageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropDamageDamageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropDamageDamage() throws Exception {
        // Get the cropDamageDamage
        restCropDamageDamageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropDamageDamage() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamageDamage
        CropDamageDamage updatedCropDamageDamage = cropDamageDamageRepository.findById(cropDamageDamage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropDamageDamage are not directly saved in db
        em.detach(updatedCropDamageDamage);
        updatedCropDamageDamage
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCropDamageDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCropDamageDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCropDamageDamage))
            )
            .andExpect(status().isOk());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropDamageDamageToMatchAllProperties(updatedCropDamageDamage);
    }

    @Test
    @Transactional
    void putNonExistingCropDamageDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageDamage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDamageDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropDamageDamage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropDamageDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageDamageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropDamageDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageDamageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropDamageDamageWithPatch() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamageDamage using partial update
        CropDamageDamage partialUpdatedCropDamageDamage = new CropDamageDamage();
        partialUpdatedCropDamageDamage.setId(cropDamageDamage.getId());

        partialUpdatedCropDamageDamage
            .damageName(UPDATED_DAMAGE_NAME)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .createdAt(UPDATED_CREATED_AT);

        restCropDamageDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDamageDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDamageDamage))
            )
            .andExpect(status().isOk());

        // Validate the CropDamageDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDamageDamageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropDamageDamage, cropDamageDamage),
            getPersistedCropDamageDamage(cropDamageDamage)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropDamageDamageWithPatch() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropDamageDamage using partial update
        CropDamageDamage partialUpdatedCropDamageDamage = new CropDamageDamage();
        partialUpdatedCropDamageDamage.setId(cropDamageDamage.getId());

        partialUpdatedCropDamageDamage
            .damageName(UPDATED_DAMAGE_NAME)
            .damageCode(UPDATED_DAMAGE_CODE)
            .damageFamily(UPDATED_DAMAGE_FAMILY)
            .damageGenus(UPDATED_DAMAGE_GENUS)
            .damageSpecies(UPDATED_DAMAGE_SPECIES)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCropDamageDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropDamageDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropDamageDamage))
            )
            .andExpect(status().isOk());

        // Validate the CropDamageDamage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropDamageDamageUpdatableFieldsEquals(
            partialUpdatedCropDamageDamage,
            getPersistedCropDamageDamage(partialUpdatedCropDamageDamage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCropDamageDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageDamage.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropDamageDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropDamageDamage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropDamageDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageDamageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropDamageDamage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropDamageDamage.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropDamageDamageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDamageDamage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropDamageDamage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropDamageDamage() throws Exception {
        // Initialize the database
        insertedCropDamageDamage = cropDamageDamageRepository.saveAndFlush(cropDamageDamage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropDamageDamage
        restCropDamageDamageMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropDamageDamage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropDamageDamageRepository.count();
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

    protected CropDamageDamage getPersistedCropDamageDamage(CropDamageDamage cropDamageDamage) {
        return cropDamageDamageRepository.findById(cropDamageDamage.getId()).orElseThrow();
    }

    protected void assertPersistedCropDamageDamageToMatchAllProperties(CropDamageDamage expectedCropDamageDamage) {
        assertCropDamageDamageAllPropertiesEquals(expectedCropDamageDamage, getPersistedCropDamageDamage(expectedCropDamageDamage));
    }

    protected void assertPersistedCropDamageDamageToMatchUpdatableProperties(CropDamageDamage expectedCropDamageDamage) {
        assertCropDamageDamageAllUpdatablePropertiesEquals(
            expectedCropDamageDamage,
            getPersistedCropDamageDamage(expectedCropDamageDamage)
        );
    }
}
