package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportAsserts.*;
import static lk.geoedge.interoperability.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import lk.geoedge.interoperability.IntegrationTest;
import lk.geoedge.interoperability.domain.CropType;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReport;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportRepository;
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
 * Integration tests for the {@link CultivatedLandDamageReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CultivatedLandDamageReportResourceIT {

    private static final String DEFAULT_DAMAGE_REASON_ID = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_REASON_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DAMAGE_SERVERITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_SERVERITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DAMAGE_DATE_MONITOR = "AAAAAAAAAA";
    private static final String UPDATED_DAMAGE_DATE_MONITOR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FARMER_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_FARMER_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ESTIMATED_YIELD = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATED_YIELD = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cultivated-land-damage-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCultivatedLandDamageReportMockMvc;

    private CultivatedLandDamageReport cultivatedLandDamageReport;

    private CultivatedLandDamageReport insertedCultivatedLandDamageReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReport createEntity() {
        return new CultivatedLandDamageReport()
            .damageReasonId(DEFAULT_DAMAGE_REASON_ID)
            .damageServerityId(DEFAULT_DAMAGE_SERVERITY_ID)
            .damageDateMonitor(DEFAULT_DAMAGE_DATE_MONITOR)
            .description(DEFAULT_DESCRIPTION)
            .farmerComment(DEFAULT_FARMER_COMMENT)
            .estimatedYield(DEFAULT_ESTIMATED_YIELD)
            .createdAt(DEFAULT_CREATED_AT)
            .addedBy(DEFAULT_ADDED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CultivatedLandDamageReport createUpdatedEntity() {
        return new CultivatedLandDamageReport()
            .damageReasonId(UPDATED_DAMAGE_REASON_ID)
            .damageServerityId(UPDATED_DAMAGE_SERVERITY_ID)
            .damageDateMonitor(UPDATED_DAMAGE_DATE_MONITOR)
            .description(UPDATED_DESCRIPTION)
            .farmerComment(UPDATED_FARMER_COMMENT)
            .estimatedYield(UPDATED_ESTIMATED_YIELD)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);
    }

    @BeforeEach
    void initTest() {
        cultivatedLandDamageReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCultivatedLandDamageReport != null) {
            cultivatedLandDamageReportRepository.delete(insertedCultivatedLandDamageReport);
            insertedCultivatedLandDamageReport = null;
        }
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CultivatedLandDamageReport
        var returnedCultivatedLandDamageReport = om.readValue(
            restCultivatedLandDamageReportMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(cultivatedLandDamageReport))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CultivatedLandDamageReport.class
        );

        // Validate the CultivatedLandDamageReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCultivatedLandDamageReportUpdatableFieldsEquals(
            returnedCultivatedLandDamageReport,
            getPersistedCultivatedLandDamageReport(returnedCultivatedLandDamageReport)
        );

        insertedCultivatedLandDamageReport = returnedCultivatedLandDamageReport;
    }

    @Test
    @Transactional
    void createCultivatedLandDamageReportWithExistingId() throws Exception {
        // Create the CultivatedLandDamageReport with an existing ID
        cultivatedLandDamageReport.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCultivatedLandDamageReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReports() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList
        restCultivatedLandDamageReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageReasonId").value(hasItem(DEFAULT_DAMAGE_REASON_ID)))
            .andExpect(jsonPath("$.[*].damageServerityId").value(hasItem(DEFAULT_DAMAGE_SERVERITY_ID)))
            .andExpect(jsonPath("$.[*].damageDateMonitor").value(hasItem(DEFAULT_DAMAGE_DATE_MONITOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].farmerComment").value(hasItem(DEFAULT_FARMER_COMMENT)))
            .andExpect(jsonPath("$.[*].estimatedYield").value(hasItem(DEFAULT_ESTIMATED_YIELD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReport() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get the cultivatedLandDamageReport
        restCultivatedLandDamageReportMockMvc
            .perform(get(ENTITY_API_URL_ID, cultivatedLandDamageReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cultivatedLandDamageReport.getId().intValue()))
            .andExpect(jsonPath("$.damageReasonId").value(DEFAULT_DAMAGE_REASON_ID))
            .andExpect(jsonPath("$.damageServerityId").value(DEFAULT_DAMAGE_SERVERITY_ID))
            .andExpect(jsonPath("$.damageDateMonitor").value(DEFAULT_DAMAGE_DATE_MONITOR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.farmerComment").value(DEFAULT_FARMER_COMMENT))
            .andExpect(jsonPath("$.estimatedYield").value(DEFAULT_ESTIMATED_YIELD))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY));
    }

    @Test
    @Transactional
    void getCultivatedLandDamageReportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        Long id = cultivatedLandDamageReport.getId();

        defaultCultivatedLandDamageReportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCultivatedLandDamageReportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCultivatedLandDamageReportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageReasonIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageReasonId equals to
        defaultCultivatedLandDamageReportFiltering(
            "damageReasonId.equals=" + DEFAULT_DAMAGE_REASON_ID,
            "damageReasonId.equals=" + UPDATED_DAMAGE_REASON_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageReasonIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageReasonId in
        defaultCultivatedLandDamageReportFiltering(
            "damageReasonId.in=" + DEFAULT_DAMAGE_REASON_ID + "," + UPDATED_DAMAGE_REASON_ID,
            "damageReasonId.in=" + UPDATED_DAMAGE_REASON_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageReasonIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageReasonId is not null
        defaultCultivatedLandDamageReportFiltering("damageReasonId.specified=true", "damageReasonId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageReasonIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageReasonId contains
        defaultCultivatedLandDamageReportFiltering(
            "damageReasonId.contains=" + DEFAULT_DAMAGE_REASON_ID,
            "damageReasonId.contains=" + UPDATED_DAMAGE_REASON_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageReasonIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageReasonId does not contain
        defaultCultivatedLandDamageReportFiltering(
            "damageReasonId.doesNotContain=" + UPDATED_DAMAGE_REASON_ID,
            "damageReasonId.doesNotContain=" + DEFAULT_DAMAGE_REASON_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageServerityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageServerityId equals to
        defaultCultivatedLandDamageReportFiltering(
            "damageServerityId.equals=" + DEFAULT_DAMAGE_SERVERITY_ID,
            "damageServerityId.equals=" + UPDATED_DAMAGE_SERVERITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageServerityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageServerityId in
        defaultCultivatedLandDamageReportFiltering(
            "damageServerityId.in=" + DEFAULT_DAMAGE_SERVERITY_ID + "," + UPDATED_DAMAGE_SERVERITY_ID,
            "damageServerityId.in=" + UPDATED_DAMAGE_SERVERITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageServerityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageServerityId is not null
        defaultCultivatedLandDamageReportFiltering("damageServerityId.specified=true", "damageServerityId.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageServerityIdContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageServerityId contains
        defaultCultivatedLandDamageReportFiltering(
            "damageServerityId.contains=" + DEFAULT_DAMAGE_SERVERITY_ID,
            "damageServerityId.contains=" + UPDATED_DAMAGE_SERVERITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageServerityIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageServerityId does not contain
        defaultCultivatedLandDamageReportFiltering(
            "damageServerityId.doesNotContain=" + UPDATED_DAMAGE_SERVERITY_ID,
            "damageServerityId.doesNotContain=" + DEFAULT_DAMAGE_SERVERITY_ID
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageDateMonitorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageDateMonitor equals to
        defaultCultivatedLandDamageReportFiltering(
            "damageDateMonitor.equals=" + DEFAULT_DAMAGE_DATE_MONITOR,
            "damageDateMonitor.equals=" + UPDATED_DAMAGE_DATE_MONITOR
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageDateMonitorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageDateMonitor in
        defaultCultivatedLandDamageReportFiltering(
            "damageDateMonitor.in=" + DEFAULT_DAMAGE_DATE_MONITOR + "," + UPDATED_DAMAGE_DATE_MONITOR,
            "damageDateMonitor.in=" + UPDATED_DAMAGE_DATE_MONITOR
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageDateMonitorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageDateMonitor is not null
        defaultCultivatedLandDamageReportFiltering("damageDateMonitor.specified=true", "damageDateMonitor.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageDateMonitorContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageDateMonitor contains
        defaultCultivatedLandDamageReportFiltering(
            "damageDateMonitor.contains=" + DEFAULT_DAMAGE_DATE_MONITOR,
            "damageDateMonitor.contains=" + UPDATED_DAMAGE_DATE_MONITOR
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageDateMonitorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where damageDateMonitor does not contain
        defaultCultivatedLandDamageReportFiltering(
            "damageDateMonitor.doesNotContain=" + UPDATED_DAMAGE_DATE_MONITOR,
            "damageDateMonitor.doesNotContain=" + DEFAULT_DAMAGE_DATE_MONITOR
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where description equals to
        defaultCultivatedLandDamageReportFiltering(
            "description.equals=" + DEFAULT_DESCRIPTION,
            "description.equals=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where description in
        defaultCultivatedLandDamageReportFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where description is not null
        defaultCultivatedLandDamageReportFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where description contains
        defaultCultivatedLandDamageReportFiltering(
            "description.contains=" + DEFAULT_DESCRIPTION,
            "description.contains=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where description does not contain
        defaultCultivatedLandDamageReportFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByFarmerCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where farmerComment equals to
        defaultCultivatedLandDamageReportFiltering(
            "farmerComment.equals=" + DEFAULT_FARMER_COMMENT,
            "farmerComment.equals=" + UPDATED_FARMER_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByFarmerCommentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where farmerComment in
        defaultCultivatedLandDamageReportFiltering(
            "farmerComment.in=" + DEFAULT_FARMER_COMMENT + "," + UPDATED_FARMER_COMMENT,
            "farmerComment.in=" + UPDATED_FARMER_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByFarmerCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where farmerComment is not null
        defaultCultivatedLandDamageReportFiltering("farmerComment.specified=true", "farmerComment.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByFarmerCommentContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where farmerComment contains
        defaultCultivatedLandDamageReportFiltering(
            "farmerComment.contains=" + DEFAULT_FARMER_COMMENT,
            "farmerComment.contains=" + UPDATED_FARMER_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByFarmerCommentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where farmerComment does not contain
        defaultCultivatedLandDamageReportFiltering(
            "farmerComment.doesNotContain=" + UPDATED_FARMER_COMMENT,
            "farmerComment.doesNotContain=" + DEFAULT_FARMER_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByEstimatedYieldIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where estimatedYield equals to
        defaultCultivatedLandDamageReportFiltering(
            "estimatedYield.equals=" + DEFAULT_ESTIMATED_YIELD,
            "estimatedYield.equals=" + UPDATED_ESTIMATED_YIELD
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByEstimatedYieldIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where estimatedYield in
        defaultCultivatedLandDamageReportFiltering(
            "estimatedYield.in=" + DEFAULT_ESTIMATED_YIELD + "," + UPDATED_ESTIMATED_YIELD,
            "estimatedYield.in=" + UPDATED_ESTIMATED_YIELD
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByEstimatedYieldIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where estimatedYield is not null
        defaultCultivatedLandDamageReportFiltering("estimatedYield.specified=true", "estimatedYield.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByEstimatedYieldContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where estimatedYield contains
        defaultCultivatedLandDamageReportFiltering(
            "estimatedYield.contains=" + DEFAULT_ESTIMATED_YIELD,
            "estimatedYield.contains=" + UPDATED_ESTIMATED_YIELD
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByEstimatedYieldNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where estimatedYield does not contain
        defaultCultivatedLandDamageReportFiltering(
            "estimatedYield.doesNotContain=" + UPDATED_ESTIMATED_YIELD,
            "estimatedYield.doesNotContain=" + DEFAULT_ESTIMATED_YIELD
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where createdAt equals to
        defaultCultivatedLandDamageReportFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where createdAt in
        defaultCultivatedLandDamageReportFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where createdAt is not null
        defaultCultivatedLandDamageReportFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where addedBy equals to
        defaultCultivatedLandDamageReportFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where addedBy in
        defaultCultivatedLandDamageReportFiltering(
            "addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY,
            "addedBy.in=" + UPDATED_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where addedBy is not null
        defaultCultivatedLandDamageReportFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByAddedByContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where addedBy contains
        defaultCultivatedLandDamageReportFiltering("addedBy.contains=" + DEFAULT_ADDED_BY, "addedBy.contains=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByAddedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        // Get all the cultivatedLandDamageReportList where addedBy does not contain
        defaultCultivatedLandDamageReportFiltering(
            "addedBy.doesNotContain=" + UPDATED_ADDED_BY,
            "addedBy.doesNotContain=" + DEFAULT_ADDED_BY
        );
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByCropIsEqualToSomething() throws Exception {
        CropType crop;
        if (TestUtil.findAll(em, CropType.class).isEmpty()) {
            cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);
            crop = CropTypeResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, CropType.class).get(0);
        }
        em.persist(crop);
        em.flush();
        cultivatedLandDamageReport.setCrop(crop);
        cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);
        Long cropId = crop.getId();
        // Get all the cultivatedLandDamageReportList where crop equals to cropId
        defaultCultivatedLandDamageReportShouldBeFound("cropId.equals=" + cropId);

        // Get all the cultivatedLandDamageReportList where crop equals to (cropId + 1)
        defaultCultivatedLandDamageReportShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageCategoryIsEqualToSomething() throws Exception {
        CultivatedLandDamageReportDamageCategory damageCategory;
        if (TestUtil.findAll(em, CultivatedLandDamageReportDamageCategory.class).isEmpty()) {
            cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);
            damageCategory = CultivatedLandDamageReportDamageCategoryResourceIT.createEntity();
        } else {
            damageCategory = TestUtil.findAll(em, CultivatedLandDamageReportDamageCategory.class).get(0);
        }
        em.persist(damageCategory);
        em.flush();
        cultivatedLandDamageReport.setDamageCategory(damageCategory);
        cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);
        Long damageCategoryId = damageCategory.getId();
        // Get all the cultivatedLandDamageReportList where damageCategory equals to damageCategoryId
        defaultCultivatedLandDamageReportShouldBeFound("damageCategoryId.equals=" + damageCategoryId);

        // Get all the cultivatedLandDamageReportList where damageCategory equals to (damageCategoryId + 1)
        defaultCultivatedLandDamageReportShouldNotBeFound("damageCategoryId.equals=" + (damageCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCultivatedLandDamageReportsByDamageTypeIsEqualToSomething() throws Exception {
        CultivatedLandDamageReportDamageType damageType;
        if (TestUtil.findAll(em, CultivatedLandDamageReportDamageType.class).isEmpty()) {
            cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);
            damageType = CultivatedLandDamageReportDamageTypeResourceIT.createEntity();
        } else {
            damageType = TestUtil.findAll(em, CultivatedLandDamageReportDamageType.class).get(0);
        }
        em.persist(damageType);
        em.flush();
        cultivatedLandDamageReport.setDamageType(damageType);
        cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);
        Long damageTypeId = damageType.getId();
        // Get all the cultivatedLandDamageReportList where damageType equals to damageTypeId
        defaultCultivatedLandDamageReportShouldBeFound("damageTypeId.equals=" + damageTypeId);

        // Get all the cultivatedLandDamageReportList where damageType equals to (damageTypeId + 1)
        defaultCultivatedLandDamageReportShouldNotBeFound("damageTypeId.equals=" + (damageTypeId + 1));
    }

    private void defaultCultivatedLandDamageReportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCultivatedLandDamageReportShouldBeFound(shouldBeFound);
        defaultCultivatedLandDamageReportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCultivatedLandDamageReportShouldBeFound(String filter) throws Exception {
        restCultivatedLandDamageReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cultivatedLandDamageReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].damageReasonId").value(hasItem(DEFAULT_DAMAGE_REASON_ID)))
            .andExpect(jsonPath("$.[*].damageServerityId").value(hasItem(DEFAULT_DAMAGE_SERVERITY_ID)))
            .andExpect(jsonPath("$.[*].damageDateMonitor").value(hasItem(DEFAULT_DAMAGE_DATE_MONITOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].farmerComment").value(hasItem(DEFAULT_FARMER_COMMENT)))
            .andExpect(jsonPath("$.[*].estimatedYield").value(hasItem(DEFAULT_ESTIMATED_YIELD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)));

        // Check, that the count call also returns 1
        restCultivatedLandDamageReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCultivatedLandDamageReportShouldNotBeFound(String filter) throws Exception {
        restCultivatedLandDamageReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCultivatedLandDamageReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCultivatedLandDamageReport() throws Exception {
        // Get the cultivatedLandDamageReport
        restCultivatedLandDamageReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCultivatedLandDamageReport() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReport
        CultivatedLandDamageReport updatedCultivatedLandDamageReport = cultivatedLandDamageReportRepository
            .findById(cultivatedLandDamageReport.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCultivatedLandDamageReport are not directly saved in db
        em.detach(updatedCultivatedLandDamageReport);
        updatedCultivatedLandDamageReport
            .damageReasonId(UPDATED_DAMAGE_REASON_ID)
            .damageServerityId(UPDATED_DAMAGE_SERVERITY_ID)
            .damageDateMonitor(UPDATED_DAMAGE_DATE_MONITOR)
            .description(UPDATED_DESCRIPTION)
            .farmerComment(UPDATED_FARMER_COMMENT)
            .estimatedYield(UPDATED_ESTIMATED_YIELD)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandDamageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCultivatedLandDamageReport.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCultivatedLandDamageReport))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCultivatedLandDamageReportToMatchAllProperties(updatedCultivatedLandDamageReport);
    }

    @Test
    @Transactional
    void putNonExistingCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cultivatedLandDamageReport.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCultivatedLandDamageReportWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReport using partial update
        CultivatedLandDamageReport partialUpdatedCultivatedLandDamageReport = new CultivatedLandDamageReport();
        partialUpdatedCultivatedLandDamageReport.setId(cultivatedLandDamageReport.getId());

        partialUpdatedCultivatedLandDamageReport
            .damageServerityId(UPDATED_DAMAGE_SERVERITY_ID)
            .farmerComment(UPDATED_FARMER_COMMENT)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandDamageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReport))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCultivatedLandDamageReport, cultivatedLandDamageReport),
            getPersistedCultivatedLandDamageReport(cultivatedLandDamageReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateCultivatedLandDamageReportWithPatch() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cultivatedLandDamageReport using partial update
        CultivatedLandDamageReport partialUpdatedCultivatedLandDamageReport = new CultivatedLandDamageReport();
        partialUpdatedCultivatedLandDamageReport.setId(cultivatedLandDamageReport.getId());

        partialUpdatedCultivatedLandDamageReport
            .damageReasonId(UPDATED_DAMAGE_REASON_ID)
            .damageServerityId(UPDATED_DAMAGE_SERVERITY_ID)
            .damageDateMonitor(UPDATED_DAMAGE_DATE_MONITOR)
            .description(UPDATED_DESCRIPTION)
            .farmerComment(UPDATED_FARMER_COMMENT)
            .estimatedYield(UPDATED_ESTIMATED_YIELD)
            .createdAt(UPDATED_CREATED_AT)
            .addedBy(UPDATED_ADDED_BY);

        restCultivatedLandDamageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCultivatedLandDamageReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCultivatedLandDamageReport))
            )
            .andExpect(status().isOk());

        // Validate the CultivatedLandDamageReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCultivatedLandDamageReportUpdatableFieldsEquals(
            partialUpdatedCultivatedLandDamageReport,
            getPersistedCultivatedLandDamageReport(partialUpdatedCultivatedLandDamageReport)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cultivatedLandDamageReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCultivatedLandDamageReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cultivatedLandDamageReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCultivatedLandDamageReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cultivatedLandDamageReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CultivatedLandDamageReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCultivatedLandDamageReport() throws Exception {
        // Initialize the database
        insertedCultivatedLandDamageReport = cultivatedLandDamageReportRepository.saveAndFlush(cultivatedLandDamageReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cultivatedLandDamageReport
        restCultivatedLandDamageReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, cultivatedLandDamageReport.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cultivatedLandDamageReportRepository.count();
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

    protected CultivatedLandDamageReport getPersistedCultivatedLandDamageReport(CultivatedLandDamageReport cultivatedLandDamageReport) {
        return cultivatedLandDamageReportRepository.findById(cultivatedLandDamageReport.getId()).orElseThrow();
    }

    protected void assertPersistedCultivatedLandDamageReportToMatchAllProperties(
        CultivatedLandDamageReport expectedCultivatedLandDamageReport
    ) {
        assertCultivatedLandDamageReportAllPropertiesEquals(
            expectedCultivatedLandDamageReport,
            getPersistedCultivatedLandDamageReport(expectedCultivatedLandDamageReport)
        );
    }

    protected void assertPersistedCultivatedLandDamageReportToMatchUpdatableProperties(
        CultivatedLandDamageReport expectedCultivatedLandDamageReport
    ) {
        assertCultivatedLandDamageReportAllUpdatablePropertiesEquals(
            expectedCultivatedLandDamageReport,
            getPersistedCultivatedLandDamageReport(expectedCultivatedLandDamageReport)
        );
    }
}
