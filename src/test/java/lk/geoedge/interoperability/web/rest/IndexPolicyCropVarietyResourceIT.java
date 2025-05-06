package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPolicyCropVarietyAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPolicyCropVariety;
import lk.geoedge.interoperability.repository.IndexPolicyCropVarietyRepository;
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
 * Integration tests for the {@link IndexPolicyCropVarietyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPolicyCropVarietyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NO_OF_STAGES = 1;
    private static final Integer UPDATED_NO_OF_STAGES = 2;
    private static final Integer SMALLER_NO_OF_STAGES = 1 - 1;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADDED_BY = 1;
    private static final Integer UPDATED_ADDED_BY = 2;
    private static final Integer SMALLER_ADDED_BY = 1 - 1;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/index-policy-crop-varieties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPolicyCropVarietyMockMvc;

    private IndexPolicyCropVariety indexPolicyCropVariety;

    private IndexPolicyCropVariety insertedIndexPolicyCropVariety;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyCropVariety createEntity() {
        return new IndexPolicyCropVariety()
            .name(DEFAULT_NAME)
            .noOfStages(DEFAULT_NO_OF_STAGES)
            .image(DEFAULT_IMAGE)
            .description(DEFAULT_DESCRIPTION)
            .addedBy(DEFAULT_ADDED_BY)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPolicyCropVariety createUpdatedEntity() {
        return new IndexPolicyCropVariety()
            .name(UPDATED_NAME)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        indexPolicyCropVariety = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPolicyCropVariety != null) {
            indexPolicyCropVarietyRepository.delete(insertedIndexPolicyCropVariety);
            insertedIndexPolicyCropVariety = null;
        }
    }

    @Test
    @Transactional
    void createIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPolicyCropVariety
        var returnedIndexPolicyCropVariety = om.readValue(
            restIndexPolicyCropVarietyMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPolicyCropVariety))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPolicyCropVariety.class
        );

        // Validate the IndexPolicyCropVariety in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPolicyCropVarietyUpdatableFieldsEquals(
            returnedIndexPolicyCropVariety,
            getPersistedIndexPolicyCropVariety(returnedIndexPolicyCropVariety)
        );

        insertedIndexPolicyCropVariety = returnedIndexPolicyCropVariety;
    }

    @Test
    @Transactional
    void createIndexPolicyCropVarietyWithExistingId() throws Exception {
        // Create the IndexPolicyCropVariety with an existing ID
        indexPolicyCropVariety.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPolicyCropVarietyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarieties() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList
        restIndexPolicyCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyCropVariety.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].noOfStages").value(hasItem(DEFAULT_NO_OF_STAGES)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getIndexPolicyCropVariety() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get the indexPolicyCropVariety
        restIndexPolicyCropVarietyMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPolicyCropVariety.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPolicyCropVariety.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.noOfStages").value(DEFAULT_NO_OF_STAGES))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getIndexPolicyCropVarietiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        Long id = indexPolicyCropVariety.getId();

        defaultIndexPolicyCropVarietyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPolicyCropVarietyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPolicyCropVarietyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where name equals to
        defaultIndexPolicyCropVarietyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where name in
        defaultIndexPolicyCropVarietyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where name is not null
        defaultIndexPolicyCropVarietyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where name contains
        defaultIndexPolicyCropVarietyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where name does not contain
        defaultIndexPolicyCropVarietyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages equals to
        defaultIndexPolicyCropVarietyFiltering("noOfStages.equals=" + DEFAULT_NO_OF_STAGES, "noOfStages.equals=" + UPDATED_NO_OF_STAGES);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages in
        defaultIndexPolicyCropVarietyFiltering(
            "noOfStages.in=" + DEFAULT_NO_OF_STAGES + "," + UPDATED_NO_OF_STAGES,
            "noOfStages.in=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages is not null
        defaultIndexPolicyCropVarietyFiltering("noOfStages.specified=true", "noOfStages.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages is greater than or equal to
        defaultIndexPolicyCropVarietyFiltering(
            "noOfStages.greaterThanOrEqual=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.greaterThanOrEqual=" + UPDATED_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages is less than or equal to
        defaultIndexPolicyCropVarietyFiltering(
            "noOfStages.lessThanOrEqual=" + DEFAULT_NO_OF_STAGES,
            "noOfStages.lessThanOrEqual=" + SMALLER_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages is less than
        defaultIndexPolicyCropVarietyFiltering(
            "noOfStages.lessThan=" + UPDATED_NO_OF_STAGES,
            "noOfStages.lessThan=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByNoOfStagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where noOfStages is greater than
        defaultIndexPolicyCropVarietyFiltering(
            "noOfStages.greaterThan=" + SMALLER_NO_OF_STAGES,
            "noOfStages.greaterThan=" + DEFAULT_NO_OF_STAGES
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where image equals to
        defaultIndexPolicyCropVarietyFiltering("image.equals=" + DEFAULT_IMAGE, "image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where image in
        defaultIndexPolicyCropVarietyFiltering("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE, "image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where image is not null
        defaultIndexPolicyCropVarietyFiltering("image.specified=true", "image.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByImageContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where image contains
        defaultIndexPolicyCropVarietyFiltering("image.contains=" + DEFAULT_IMAGE, "image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where image does not contain
        defaultIndexPolicyCropVarietyFiltering("image.doesNotContain=" + UPDATED_IMAGE, "image.doesNotContain=" + DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where description equals to
        defaultIndexPolicyCropVarietyFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where description in
        defaultIndexPolicyCropVarietyFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where description is not null
        defaultIndexPolicyCropVarietyFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where description contains
        defaultIndexPolicyCropVarietyFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where description does not contain
        defaultIndexPolicyCropVarietyFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy equals to
        defaultIndexPolicyCropVarietyFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy in
        defaultIndexPolicyCropVarietyFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy is not null
        defaultIndexPolicyCropVarietyFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy is greater than or equal to
        defaultIndexPolicyCropVarietyFiltering(
            "addedBy.greaterThanOrEqual=" + DEFAULT_ADDED_BY,
            "addedBy.greaterThanOrEqual=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy is less than or equal to
        defaultIndexPolicyCropVarietyFiltering(
            "addedBy.lessThanOrEqual=" + DEFAULT_ADDED_BY,
            "addedBy.lessThanOrEqual=" + SMALLER_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy is less than
        defaultIndexPolicyCropVarietyFiltering("addedBy.lessThan=" + UPDATED_ADDED_BY, "addedBy.lessThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByAddedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where addedBy is greater than
        defaultIndexPolicyCropVarietyFiltering("addedBy.greaterThan=" + SMALLER_ADDED_BY, "addedBy.greaterThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt equals to
        defaultIndexPolicyCropVarietyFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt in
        defaultIndexPolicyCropVarietyFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt is not null
        defaultIndexPolicyCropVarietyFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt is greater than or equal to
        defaultIndexPolicyCropVarietyFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt is less than or equal to
        defaultIndexPolicyCropVarietyFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt is less than
        defaultIndexPolicyCropVarietyFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllIndexPolicyCropVarietiesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        // Get all the indexPolicyCropVarietyList where createdAt is greater than
        defaultIndexPolicyCropVarietyFiltering(
            "createdAt.greaterThan=" + SMALLER_CREATED_AT,
            "createdAt.greaterThan=" + DEFAULT_CREATED_AT
        );
    }

    private void defaultIndexPolicyCropVarietyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPolicyCropVarietyShouldBeFound(shouldBeFound);
        defaultIndexPolicyCropVarietyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPolicyCropVarietyShouldBeFound(String filter) throws Exception {
        restIndexPolicyCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPolicyCropVariety.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].noOfStages").value(hasItem(DEFAULT_NO_OF_STAGES)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restIndexPolicyCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPolicyCropVarietyShouldNotBeFound(String filter) throws Exception {
        restIndexPolicyCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPolicyCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPolicyCropVariety() throws Exception {
        // Get the indexPolicyCropVariety
        restIndexPolicyCropVarietyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPolicyCropVariety() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyCropVariety
        IndexPolicyCropVariety updatedIndexPolicyCropVariety = indexPolicyCropVarietyRepository
            .findById(indexPolicyCropVariety.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPolicyCropVariety are not directly saved in db
        em.detach(updatedIndexPolicyCropVariety);
        updatedIndexPolicyCropVariety
            .name(UPDATED_NAME)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restIndexPolicyCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPolicyCropVariety.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPolicyCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPolicyCropVarietyToMatchAllProperties(updatedIndexPolicyCropVariety);
    }

    @Test
    @Transactional
    void putNonExistingIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropVariety.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPolicyCropVariety.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPolicyCropVarietyWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyCropVariety using partial update
        IndexPolicyCropVariety partialUpdatedIndexPolicyCropVariety = new IndexPolicyCropVariety();
        partialUpdatedIndexPolicyCropVariety.setId(indexPolicyCropVariety.getId());

        partialUpdatedIndexPolicyCropVariety
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restIndexPolicyCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyCropVariety.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyCropVariety in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyCropVarietyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPolicyCropVariety, indexPolicyCropVariety),
            getPersistedIndexPolicyCropVariety(indexPolicyCropVariety)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPolicyCropVarietyWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPolicyCropVariety using partial update
        IndexPolicyCropVariety partialUpdatedIndexPolicyCropVariety = new IndexPolicyCropVariety();
        partialUpdatedIndexPolicyCropVariety.setId(indexPolicyCropVariety.getId());

        partialUpdatedIndexPolicyCropVariety
            .name(UPDATED_NAME)
            .noOfStages(UPDATED_NO_OF_STAGES)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .addedBy(UPDATED_ADDED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restIndexPolicyCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPolicyCropVariety.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPolicyCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the IndexPolicyCropVariety in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPolicyCropVarietyUpdatableFieldsEquals(
            partialUpdatedIndexPolicyCropVariety,
            getPersistedIndexPolicyCropVariety(partialUpdatedIndexPolicyCropVariety)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropVariety.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPolicyCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPolicyCropVariety.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPolicyCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPolicyCropVariety.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPolicyCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPolicyCropVariety))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPolicyCropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPolicyCropVariety() throws Exception {
        // Initialize the database
        insertedIndexPolicyCropVariety = indexPolicyCropVarietyRepository.saveAndFlush(indexPolicyCropVariety);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPolicyCropVariety
        restIndexPolicyCropVarietyMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPolicyCropVariety.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPolicyCropVarietyRepository.count();
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

    protected IndexPolicyCropVariety getPersistedIndexPolicyCropVariety(IndexPolicyCropVariety indexPolicyCropVariety) {
        return indexPolicyCropVarietyRepository.findById(indexPolicyCropVariety.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPolicyCropVarietyToMatchAllProperties(IndexPolicyCropVariety expectedIndexPolicyCropVariety) {
        assertIndexPolicyCropVarietyAllPropertiesEquals(
            expectedIndexPolicyCropVariety,
            getPersistedIndexPolicyCropVariety(expectedIndexPolicyCropVariety)
        );
    }

    protected void assertPersistedIndexPolicyCropVarietyToMatchUpdatableProperties(IndexPolicyCropVariety expectedIndexPolicyCropVariety) {
        assertIndexPolicyCropVarietyAllUpdatablePropertiesEquals(
            expectedIndexPolicyCropVariety,
            getPersistedIndexPolicyCropVariety(expectedIndexPolicyCropVariety)
        );
    }
}
