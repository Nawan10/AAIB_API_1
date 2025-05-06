package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CanlendarCropAsserts.*;
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
import lk.geoedge.interoperability.domain.CanlendarCrop;
import lk.geoedge.interoperability.domain.CanlendarCropCropType;
import lk.geoedge.interoperability.domain.CanlendarCropSeason;
import lk.geoedge.interoperability.repository.CanlendarCropRepository;
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
 * Integration tests for the {@link CanlendarCropResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CanlendarCropResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_PERCENTAGE = 1D;
    private static final Double UPDATED_PERCENTAGE = 2D;
    private static final Double SMALLER_PERCENTAGE = 1D - 1D;

    private static final Integer DEFAULT_CANLENDAR_CROP_STATUS = 1;
    private static final Integer UPDATED_CANLENDAR_CROP_STATUS = 2;
    private static final Integer SMALLER_CANLENDAR_CROP_STATUS = 1 - 1;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/canlendar-crops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CanlendarCropRepository canlendarCropRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCanlendarCropMockMvc;

    private CanlendarCrop canlendarCrop;

    private CanlendarCrop insertedCanlendarCrop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanlendarCrop createEntity() {
        return new CanlendarCrop()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .percentage(DEFAULT_PERCENTAGE)
            .canlendarCropStatus(DEFAULT_CANLENDAR_CROP_STATUS)
            .reason(DEFAULT_REASON)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanlendarCrop createUpdatedEntity() {
        return new CanlendarCrop()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .percentage(UPDATED_PERCENTAGE)
            .canlendarCropStatus(UPDATED_CANLENDAR_CROP_STATUS)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        canlendarCrop = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCanlendarCrop != null) {
            canlendarCropRepository.delete(insertedCanlendarCrop);
            insertedCanlendarCrop = null;
        }
    }

    @Test
    @Transactional
    void createCanlendarCrop() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CanlendarCrop
        var returnedCanlendarCrop = om.readValue(
            restCanlendarCropMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canlendarCrop))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CanlendarCrop.class
        );

        // Validate the CanlendarCrop in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCanlendarCropUpdatableFieldsEquals(returnedCanlendarCrop, getPersistedCanlendarCrop(returnedCanlendarCrop));

        insertedCanlendarCrop = returnedCanlendarCrop;
    }

    @Test
    @Transactional
    void createCanlendarCropWithExistingId() throws Exception {
        // Create the CanlendarCrop with an existing ID
        canlendarCrop.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCanlendarCropMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canlendarCrop)))
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCanlendarCrops() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList
        restCanlendarCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canlendarCrop.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].canlendarCropStatus").value(hasItem(DEFAULT_CANLENDAR_CROP_STATUS)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCanlendarCrop() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get the canlendarCrop
        restCanlendarCropMockMvc
            .perform(get(ENTITY_API_URL_ID, canlendarCrop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(canlendarCrop.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE))
            .andExpect(jsonPath("$.canlendarCropStatus").value(DEFAULT_CANLENDAR_CROP_STATUS))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCanlendarCropsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        Long id = canlendarCrop.getId();

        defaultCanlendarCropFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCanlendarCropFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCanlendarCropFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate equals to
        defaultCanlendarCropFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate in
        defaultCanlendarCropFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate is not null
        defaultCanlendarCropFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate is greater than or equal to
        defaultCanlendarCropFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate is less than or equal to
        defaultCanlendarCropFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate is less than
        defaultCanlendarCropFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where startDate is greater than
        defaultCanlendarCropFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate equals to
        defaultCanlendarCropFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate in
        defaultCanlendarCropFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate is not null
        defaultCanlendarCropFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate is greater than or equal to
        defaultCanlendarCropFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate is less than or equal to
        defaultCanlendarCropFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate is less than
        defaultCanlendarCropFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where endDate is greater than
        defaultCanlendarCropFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage equals to
        defaultCanlendarCropFiltering("percentage.equals=" + DEFAULT_PERCENTAGE, "percentage.equals=" + UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage in
        defaultCanlendarCropFiltering(
            "percentage.in=" + DEFAULT_PERCENTAGE + "," + UPDATED_PERCENTAGE,
            "percentage.in=" + UPDATED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage is not null
        defaultCanlendarCropFiltering("percentage.specified=true", "percentage.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage is greater than or equal to
        defaultCanlendarCropFiltering(
            "percentage.greaterThanOrEqual=" + DEFAULT_PERCENTAGE,
            "percentage.greaterThanOrEqual=" + UPDATED_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage is less than or equal to
        defaultCanlendarCropFiltering(
            "percentage.lessThanOrEqual=" + DEFAULT_PERCENTAGE,
            "percentage.lessThanOrEqual=" + SMALLER_PERCENTAGE
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage is less than
        defaultCanlendarCropFiltering("percentage.lessThan=" + UPDATED_PERCENTAGE, "percentage.lessThan=" + DEFAULT_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByPercentageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where percentage is greater than
        defaultCanlendarCropFiltering("percentage.greaterThan=" + SMALLER_PERCENTAGE, "percentage.greaterThan=" + DEFAULT_PERCENTAGE);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus equals to
        defaultCanlendarCropFiltering(
            "canlendarCropStatus.equals=" + DEFAULT_CANLENDAR_CROP_STATUS,
            "canlendarCropStatus.equals=" + UPDATED_CANLENDAR_CROP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus in
        defaultCanlendarCropFiltering(
            "canlendarCropStatus.in=" + DEFAULT_CANLENDAR_CROP_STATUS + "," + UPDATED_CANLENDAR_CROP_STATUS,
            "canlendarCropStatus.in=" + UPDATED_CANLENDAR_CROP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus is not null
        defaultCanlendarCropFiltering("canlendarCropStatus.specified=true", "canlendarCropStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus is greater than or equal to
        defaultCanlendarCropFiltering(
            "canlendarCropStatus.greaterThanOrEqual=" + DEFAULT_CANLENDAR_CROP_STATUS,
            "canlendarCropStatus.greaterThanOrEqual=" + UPDATED_CANLENDAR_CROP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus is less than or equal to
        defaultCanlendarCropFiltering(
            "canlendarCropStatus.lessThanOrEqual=" + DEFAULT_CANLENDAR_CROP_STATUS,
            "canlendarCropStatus.lessThanOrEqual=" + SMALLER_CANLENDAR_CROP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus is less than
        defaultCanlendarCropFiltering(
            "canlendarCropStatus.lessThan=" + UPDATED_CANLENDAR_CROP_STATUS,
            "canlendarCropStatus.lessThan=" + DEFAULT_CANLENDAR_CROP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCanlendarCropStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where canlendarCropStatus is greater than
        defaultCanlendarCropFiltering(
            "canlendarCropStatus.greaterThan=" + SMALLER_CANLENDAR_CROP_STATUS,
            "canlendarCropStatus.greaterThan=" + DEFAULT_CANLENDAR_CROP_STATUS
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where reason equals to
        defaultCanlendarCropFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where reason in
        defaultCanlendarCropFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where reason is not null
        defaultCanlendarCropFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where reason contains
        defaultCanlendarCropFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where reason does not contain
        defaultCanlendarCropFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt equals to
        defaultCanlendarCropFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt in
        defaultCanlendarCropFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt is not null
        defaultCanlendarCropFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt is greater than or equal to
        defaultCanlendarCropFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt is less than or equal to
        defaultCanlendarCropFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt is less than
        defaultCanlendarCropFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where createdAt is greater than
        defaultCanlendarCropFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where addedBy equals to
        defaultCanlendarCropFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where addedBy in
        defaultCanlendarCropFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where addedBy is not null
        defaultCanlendarCropFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where addedBy contains
        defaultCanlendarCropFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        // Get all the canlendarCropList where addedBy does not contain
        defaultCanlendarCropFiltering("addedBy.doesNotContain=" + UPDATED_ADDED_BY, "addedBy.doesNotContain=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCanlendarCropsBySeasonIsEqualToSomething() throws Exception {
        CanlendarCropSeason season;
        if (TestUtil.findAll(em, CanlendarCropSeason.class).isEmpty()) {
            canlendarCropRepository.saveAndFlush(canlendarCrop);
            season = CanlendarCropSeasonResourceIT.createEntity();
        } else {
            season = TestUtil.findAll(em, CanlendarCropSeason.class).get(0);
        }
        em.persist(season);
        em.flush();
        canlendarCrop.setSeason(season);
        canlendarCropRepository.saveAndFlush(canlendarCrop);
        Long seasonId = season.getId();
        // Get all the canlendarCropList where season equals to seasonId
        defaultCanlendarCropShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the canlendarCropList where season equals to (seasonId + 1)
        defaultCanlendarCropShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    @Test
    @Transactional
    void getAllCanlendarCropsByCropIsEqualToSomething() throws Exception {
        CanlendarCropCropType crop;
        if (TestUtil.findAll(em, CanlendarCropCropType.class).isEmpty()) {
            canlendarCropRepository.saveAndFlush(canlendarCrop);
            crop = CanlendarCropCropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, CanlendarCropCropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        canlendarCrop.setCrop(crop);
        canlendarCropRepository.saveAndFlush(canlendarCrop);
        Long cropId = crop.getId();
        // Get all the canlendarCropList where crop equals to cropId
        defaultCanlendarCropShouldBeFound("cropId.equals=" + cropId);

        // Get all the canlendarCropList where crop equals to (cropId + 1)
        defaultCanlendarCropShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultCanlendarCropFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCanlendarCropShouldBeFound(shouldBeFound);
        defaultCanlendarCropShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCanlendarCropShouldBeFound(String filter) throws Exception {
        restCanlendarCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canlendarCrop.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].canlendarCropStatus").value(hasItem(DEFAULT_CANLENDAR_CROP_STATUS)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCanlendarCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCanlendarCropShouldNotBeFound(String filter) throws Exception {
        restCanlendarCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCanlendarCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCanlendarCrop() throws Exception {
        // Get the canlendarCrop
        restCanlendarCropMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCanlendarCrop() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCrop
        CanlendarCrop updatedCanlendarCrop = canlendarCropRepository.findById(canlendarCrop.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCanlendarCrop are not directly saved in db
        em.detach(updatedCanlendarCrop);
        updatedCanlendarCrop
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .percentage(UPDATED_PERCENTAGE)
            .canlendarCropStatus(UPDATED_CANLENDAR_CROP_STATUS)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCanlendarCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCanlendarCrop.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCanlendarCrop))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCanlendarCropToMatchAllProperties(updatedCanlendarCrop);
    }

    @Test
    @Transactional
    void putNonExistingCanlendarCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCrop.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanlendarCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canlendarCrop.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCanlendarCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canlendarCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCanlendarCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canlendarCrop)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCanlendarCropWithPatch() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCrop using partial update
        CanlendarCrop partialUpdatedCanlendarCrop = new CanlendarCrop();
        partialUpdatedCanlendarCrop.setId(canlendarCrop.getId());

        partialUpdatedCanlendarCrop.startDate(UPDATED_START_DATE).addedBy(UPDATED_ADDED_BY);

        restCanlendarCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanlendarCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanlendarCrop))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCrop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanlendarCropUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCanlendarCrop, canlendarCrop),
            getPersistedCanlendarCrop(canlendarCrop)
        );
    }

    @Test
    @Transactional
    void fullUpdateCanlendarCropWithPatch() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canlendarCrop using partial update
        CanlendarCrop partialUpdatedCanlendarCrop = new CanlendarCrop();
        partialUpdatedCanlendarCrop.setId(canlendarCrop.getId());

        partialUpdatedCanlendarCrop
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .percentage(UPDATED_PERCENTAGE)
            .canlendarCropStatus(UPDATED_CANLENDAR_CROP_STATUS)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCanlendarCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanlendarCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanlendarCrop))
            )
            .andExpect(status().isOk());

        // Validate the CanlendarCrop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanlendarCropUpdatableFieldsEquals(partialUpdatedCanlendarCrop, getPersistedCanlendarCrop(partialUpdatedCanlendarCrop));
    }

    @Test
    @Transactional
    void patchNonExistingCanlendarCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCrop.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanlendarCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, canlendarCrop.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCanlendarCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canlendarCrop))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCanlendarCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canlendarCrop.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanlendarCropMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(canlendarCrop))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanlendarCrop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCanlendarCrop() throws Exception {
        // Initialize the database
        insertedCanlendarCrop = canlendarCropRepository.saveAndFlush(canlendarCrop);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the canlendarCrop
        restCanlendarCropMockMvc
            .perform(delete(ENTITY_API_URL_ID, canlendarCrop.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return canlendarCropRepository.count();
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

    protected CanlendarCrop getPersistedCanlendarCrop(CanlendarCrop canlendarCrop) {
        return canlendarCropRepository.findById(canlendarCrop.getId()).orElseThrow();
    }

    protected void assertPersistedCanlendarCropToMatchAllProperties(CanlendarCrop expectedCanlendarCrop) {
        assertCanlendarCropAllPropertiesEquals(expectedCanlendarCrop, getPersistedCanlendarCrop(expectedCanlendarCrop));
    }

    protected void assertPersistedCanlendarCropToMatchUpdatableProperties(CanlendarCrop expectedCanlendarCrop) {
        assertCanlendarCropAllUpdatablePropertiesEquals(expectedCanlendarCrop, getPersistedCanlendarCrop(expectedCanlendarCrop));
    }
}
