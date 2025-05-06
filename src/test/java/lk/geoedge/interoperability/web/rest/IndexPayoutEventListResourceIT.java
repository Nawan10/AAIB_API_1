package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListAsserts.*;
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
import lk.geoedge.interoperability.domain.IndexPayoutEventList;
import lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand;
import lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer;
import lk.geoedge.interoperability.domain.IndexPayoutEventListSeason;
import lk.geoedge.interoperability.repository.IndexPayoutEventListRepository;
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
 * Integration tests for the {@link IndexPayoutEventListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndexPayoutEventListResourceIT {

    private static final Integer DEFAULT_INDEX_PAYOUT_EVENT_ID = 1;
    private static final Integer UPDATED_INDEX_PAYOUT_EVENT_ID = 2;
    private static final Integer SMALLER_INDEX_PAYOUT_EVENT_ID = 1 - 1;

    private static final Integer DEFAULT_ASC_ID = 1;
    private static final Integer UPDATED_ASC_ID = 2;
    private static final Integer SMALLER_ASC_ID = 1 - 1;

    private static final Integer DEFAULT_CONFIRMED_BY = 1;
    private static final Integer UPDATED_CONFIRMED_BY = 2;
    private static final Integer SMALLER_CONFIRMED_BY = 1 - 1;

    private static final Double DEFAULT_CULTIVATED_EXTENT = 1D;
    private static final Double UPDATED_CULTIVATED_EXTENT = 2D;
    private static final Double SMALLER_CULTIVATED_EXTENT = 1D - 1D;

    private static final Double DEFAULT_PAYOUT = 1D;
    private static final Double UPDATED_PAYOUT = 2D;
    private static final Double SMALLER_PAYOUT = 1D - 1D;

    private static final String DEFAULT_CONFIRMED_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIRMED_DATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_REJECTED_BY = 1;
    private static final Integer UPDATED_REJECTED_BY = 2;
    private static final Integer SMALLER_REJECTED_BY = 1 - 1;

    private static final String DEFAULT_REJECTED_DATE = "AAAAAAAAAA";
    private static final String UPDATED_REJECTED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Double DEFAULT_FINAL_PAYOUT = 1D;
    private static final Double UPDATED_FINAL_PAYOUT = 2D;
    private static final Double SMALLER_FINAL_PAYOUT = 1D - 1D;

    private static final Integer DEFAULT_INDEX_PAYOUT_EVENT_STATUS = 1;
    private static final Integer UPDATED_INDEX_PAYOUT_EVENT_STATUS = 2;
    private static final Integer SMALLER_INDEX_PAYOUT_EVENT_STATUS = 1 - 1;

    private static final Integer DEFAULT_IS_APPROVED = 1;
    private static final Integer UPDATED_IS_APPROVED = 2;
    private static final Integer SMALLER_IS_APPROVED = 1 - 1;

    private static final Double DEFAULT_MONITORING_RANGE = 1D;
    private static final Double UPDATED_MONITORING_RANGE = 2D;
    private static final Double SMALLER_MONITORING_RANGE = 1D - 1D;

    private static final Integer DEFAULT_IS_INSURANCE = 1;
    private static final Integer UPDATED_IS_INSURANCE = 2;
    private static final Integer SMALLER_IS_INSURANCE = 1 - 1;

    private static final Integer DEFAULT_INSURANCE_CULTIVATED_LAND = 1;
    private static final Integer UPDATED_INSURANCE_CULTIVATED_LAND = 2;
    private static final Integer SMALLER_INSURANCE_CULTIVATED_LAND = 1 - 1;

    private static final Integer DEFAULT_INDEX_CHEQUE_ID = 1;
    private static final Integer UPDATED_INDEX_CHEQUE_ID = 2;
    private static final Integer SMALLER_INDEX_CHEQUE_ID = 1 - 1;

    private static final Integer DEFAULT_INDEX_PRODUCT_ID = 1;
    private static final Integer UPDATED_INDEX_PRODUCT_ID = 2;
    private static final Integer SMALLER_INDEX_PRODUCT_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/index-payout-event-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndexPayoutEventListRepository indexPayoutEventListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndexPayoutEventListMockMvc;

    private IndexPayoutEventList indexPayoutEventList;

    private IndexPayoutEventList insertedIndexPayoutEventList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPayoutEventList createEntity() {
        return new IndexPayoutEventList()
            .indexPayoutEventId(DEFAULT_INDEX_PAYOUT_EVENT_ID)
            .ascId(DEFAULT_ASC_ID)
            .confirmedBy(DEFAULT_CONFIRMED_BY)
            .cultivatedExtent(DEFAULT_CULTIVATED_EXTENT)
            .payout(DEFAULT_PAYOUT)
            .confirmedDate(DEFAULT_CONFIRMED_DATE)
            .rejectedBy(DEFAULT_REJECTED_BY)
            .rejectedDate(DEFAULT_REJECTED_DATE)
            .reason(DEFAULT_REASON)
            .finalPayout(DEFAULT_FINAL_PAYOUT)
            .indexPayoutEventStatus(DEFAULT_INDEX_PAYOUT_EVENT_STATUS)
            .isApproved(DEFAULT_IS_APPROVED)
            .monitoringRange(DEFAULT_MONITORING_RANGE)
            .isInsurance(DEFAULT_IS_INSURANCE)
            .insuranceCultivatedLand(DEFAULT_INSURANCE_CULTIVATED_LAND)
            .indexChequeId(DEFAULT_INDEX_CHEQUE_ID)
            .indexProductId(DEFAULT_INDEX_PRODUCT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexPayoutEventList createUpdatedEntity() {
        return new IndexPayoutEventList()
            .indexPayoutEventId(UPDATED_INDEX_PAYOUT_EVENT_ID)
            .ascId(UPDATED_ASC_ID)
            .confirmedBy(UPDATED_CONFIRMED_BY)
            .cultivatedExtent(UPDATED_CULTIVATED_EXTENT)
            .payout(UPDATED_PAYOUT)
            .confirmedDate(UPDATED_CONFIRMED_DATE)
            .rejectedBy(UPDATED_REJECTED_BY)
            .rejectedDate(UPDATED_REJECTED_DATE)
            .reason(UPDATED_REASON)
            .finalPayout(UPDATED_FINAL_PAYOUT)
            .indexPayoutEventStatus(UPDATED_INDEX_PAYOUT_EVENT_STATUS)
            .isApproved(UPDATED_IS_APPROVED)
            .monitoringRange(UPDATED_MONITORING_RANGE)
            .isInsurance(UPDATED_IS_INSURANCE)
            .insuranceCultivatedLand(UPDATED_INSURANCE_CULTIVATED_LAND)
            .indexChequeId(UPDATED_INDEX_CHEQUE_ID)
            .indexProductId(UPDATED_INDEX_PRODUCT_ID);
    }

    @BeforeEach
    void initTest() {
        indexPayoutEventList = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndexPayoutEventList != null) {
            indexPayoutEventListRepository.delete(insertedIndexPayoutEventList);
            insertedIndexPayoutEventList = null;
        }
    }

    @Test
    @Transactional
    void createIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndexPayoutEventList
        var returnedIndexPayoutEventList = om.readValue(
            restIndexPayoutEventListMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(indexPayoutEventList))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndexPayoutEventList.class
        );

        // Validate the IndexPayoutEventList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIndexPayoutEventListUpdatableFieldsEquals(
            returnedIndexPayoutEventList,
            getPersistedIndexPayoutEventList(returnedIndexPayoutEventList)
        );

        insertedIndexPayoutEventList = returnedIndexPayoutEventList;
    }

    @Test
    @Transactional
    void createIndexPayoutEventListWithExistingId() throws Exception {
        // Create the IndexPayoutEventList with an existing ID
        indexPayoutEventList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexPayoutEventListMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventLists() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList
        restIndexPayoutEventListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventList.getId().intValue())))
            .andExpect(jsonPath("$.[*].indexPayoutEventId").value(hasItem(DEFAULT_INDEX_PAYOUT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].ascId").value(hasItem(DEFAULT_ASC_ID)))
            .andExpect(jsonPath("$.[*].confirmedBy").value(hasItem(DEFAULT_CONFIRMED_BY)))
            .andExpect(jsonPath("$.[*].cultivatedExtent").value(hasItem(DEFAULT_CULTIVATED_EXTENT)))
            .andExpect(jsonPath("$.[*].payout").value(hasItem(DEFAULT_PAYOUT)))
            .andExpect(jsonPath("$.[*].confirmedDate").value(hasItem(DEFAULT_CONFIRMED_DATE)))
            .andExpect(jsonPath("$.[*].rejectedBy").value(hasItem(DEFAULT_REJECTED_BY)))
            .andExpect(jsonPath("$.[*].rejectedDate").value(hasItem(DEFAULT_REJECTED_DATE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].finalPayout").value(hasItem(DEFAULT_FINAL_PAYOUT)))
            .andExpect(jsonPath("$.[*].indexPayoutEventStatus").value(hasItem(DEFAULT_INDEX_PAYOUT_EVENT_STATUS)))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED)))
            .andExpect(jsonPath("$.[*].monitoringRange").value(hasItem(DEFAULT_MONITORING_RANGE)))
            .andExpect(jsonPath("$.[*].isInsurance").value(hasItem(DEFAULT_IS_INSURANCE)))
            .andExpect(jsonPath("$.[*].insuranceCultivatedLand").value(hasItem(DEFAULT_INSURANCE_CULTIVATED_LAND)))
            .andExpect(jsonPath("$.[*].indexChequeId").value(hasItem(DEFAULT_INDEX_CHEQUE_ID)))
            .andExpect(jsonPath("$.[*].indexProductId").value(hasItem(DEFAULT_INDEX_PRODUCT_ID)));
    }

    @Test
    @Transactional
    void getIndexPayoutEventList() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get the indexPayoutEventList
        restIndexPayoutEventListMockMvc
            .perform(get(ENTITY_API_URL_ID, indexPayoutEventList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(indexPayoutEventList.getId().intValue()))
            .andExpect(jsonPath("$.indexPayoutEventId").value(DEFAULT_INDEX_PAYOUT_EVENT_ID))
            .andExpect(jsonPath("$.ascId").value(DEFAULT_ASC_ID))
            .andExpect(jsonPath("$.confirmedBy").value(DEFAULT_CONFIRMED_BY))
            .andExpect(jsonPath("$.cultivatedExtent").value(DEFAULT_CULTIVATED_EXTENT))
            .andExpect(jsonPath("$.payout").value(DEFAULT_PAYOUT))
            .andExpect(jsonPath("$.confirmedDate").value(DEFAULT_CONFIRMED_DATE))
            .andExpect(jsonPath("$.rejectedBy").value(DEFAULT_REJECTED_BY))
            .andExpect(jsonPath("$.rejectedDate").value(DEFAULT_REJECTED_DATE))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.finalPayout").value(DEFAULT_FINAL_PAYOUT))
            .andExpect(jsonPath("$.indexPayoutEventStatus").value(DEFAULT_INDEX_PAYOUT_EVENT_STATUS))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED))
            .andExpect(jsonPath("$.monitoringRange").value(DEFAULT_MONITORING_RANGE))
            .andExpect(jsonPath("$.isInsurance").value(DEFAULT_IS_INSURANCE))
            .andExpect(jsonPath("$.insuranceCultivatedLand").value(DEFAULT_INSURANCE_CULTIVATED_LAND))
            .andExpect(jsonPath("$.indexChequeId").value(DEFAULT_INDEX_CHEQUE_ID))
            .andExpect(jsonPath("$.indexProductId").value(DEFAULT_INDEX_PRODUCT_ID));
    }

    @Test
    @Transactional
    void getIndexPayoutEventListsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        Long id = indexPayoutEventList.getId();

        defaultIndexPayoutEventListFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndexPayoutEventListFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndexPayoutEventListFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId equals to
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventId.equals=" + DEFAULT_INDEX_PAYOUT_EVENT_ID,
            "indexPayoutEventId.equals=" + UPDATED_INDEX_PAYOUT_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId in
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventId.in=" + DEFAULT_INDEX_PAYOUT_EVENT_ID + "," + UPDATED_INDEX_PAYOUT_EVENT_ID,
            "indexPayoutEventId.in=" + UPDATED_INDEX_PAYOUT_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId is not null
        defaultIndexPayoutEventListFiltering("indexPayoutEventId.specified=true", "indexPayoutEventId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventId.greaterThanOrEqual=" + DEFAULT_INDEX_PAYOUT_EVENT_ID,
            "indexPayoutEventId.greaterThanOrEqual=" + UPDATED_INDEX_PAYOUT_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventId.lessThanOrEqual=" + DEFAULT_INDEX_PAYOUT_EVENT_ID,
            "indexPayoutEventId.lessThanOrEqual=" + SMALLER_INDEX_PAYOUT_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId is less than
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventId.lessThan=" + UPDATED_INDEX_PAYOUT_EVENT_ID,
            "indexPayoutEventId.lessThan=" + DEFAULT_INDEX_PAYOUT_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventId is greater than
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventId.greaterThan=" + SMALLER_INDEX_PAYOUT_EVENT_ID,
            "indexPayoutEventId.greaterThan=" + DEFAULT_INDEX_PAYOUT_EVENT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId equals to
        defaultIndexPayoutEventListFiltering("ascId.equals=" + DEFAULT_ASC_ID, "ascId.equals=" + UPDATED_ASC_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId in
        defaultIndexPayoutEventListFiltering("ascId.in=" + DEFAULT_ASC_ID + "," + UPDATED_ASC_ID, "ascId.in=" + UPDATED_ASC_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId is not null
        defaultIndexPayoutEventListFiltering("ascId.specified=true", "ascId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId is greater than or equal to
        defaultIndexPayoutEventListFiltering("ascId.greaterThanOrEqual=" + DEFAULT_ASC_ID, "ascId.greaterThanOrEqual=" + UPDATED_ASC_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId is less than or equal to
        defaultIndexPayoutEventListFiltering("ascId.lessThanOrEqual=" + DEFAULT_ASC_ID, "ascId.lessThanOrEqual=" + SMALLER_ASC_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId is less than
        defaultIndexPayoutEventListFiltering("ascId.lessThan=" + UPDATED_ASC_ID, "ascId.lessThan=" + DEFAULT_ASC_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByAscIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where ascId is greater than
        defaultIndexPayoutEventListFiltering("ascId.greaterThan=" + SMALLER_ASC_ID, "ascId.greaterThan=" + DEFAULT_ASC_ID);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy equals to
        defaultIndexPayoutEventListFiltering("confirmedBy.equals=" + DEFAULT_CONFIRMED_BY, "confirmedBy.equals=" + UPDATED_CONFIRMED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy in
        defaultIndexPayoutEventListFiltering(
            "confirmedBy.in=" + DEFAULT_CONFIRMED_BY + "," + UPDATED_CONFIRMED_BY,
            "confirmedBy.in=" + UPDATED_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy is not null
        defaultIndexPayoutEventListFiltering("confirmedBy.specified=true", "confirmedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "confirmedBy.greaterThanOrEqual=" + DEFAULT_CONFIRMED_BY,
            "confirmedBy.greaterThanOrEqual=" + UPDATED_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "confirmedBy.lessThanOrEqual=" + DEFAULT_CONFIRMED_BY,
            "confirmedBy.lessThanOrEqual=" + SMALLER_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy is less than
        defaultIndexPayoutEventListFiltering(
            "confirmedBy.lessThan=" + UPDATED_CONFIRMED_BY,
            "confirmedBy.lessThan=" + DEFAULT_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedBy is greater than
        defaultIndexPayoutEventListFiltering(
            "confirmedBy.greaterThan=" + SMALLER_CONFIRMED_BY,
            "confirmedBy.greaterThan=" + DEFAULT_CONFIRMED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent equals to
        defaultIndexPayoutEventListFiltering(
            "cultivatedExtent.equals=" + DEFAULT_CULTIVATED_EXTENT,
            "cultivatedExtent.equals=" + UPDATED_CULTIVATED_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent in
        defaultIndexPayoutEventListFiltering(
            "cultivatedExtent.in=" + DEFAULT_CULTIVATED_EXTENT + "," + UPDATED_CULTIVATED_EXTENT,
            "cultivatedExtent.in=" + UPDATED_CULTIVATED_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent is not null
        defaultIndexPayoutEventListFiltering("cultivatedExtent.specified=true", "cultivatedExtent.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "cultivatedExtent.greaterThanOrEqual=" + DEFAULT_CULTIVATED_EXTENT,
            "cultivatedExtent.greaterThanOrEqual=" + UPDATED_CULTIVATED_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "cultivatedExtent.lessThanOrEqual=" + DEFAULT_CULTIVATED_EXTENT,
            "cultivatedExtent.lessThanOrEqual=" + SMALLER_CULTIVATED_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent is less than
        defaultIndexPayoutEventListFiltering(
            "cultivatedExtent.lessThan=" + UPDATED_CULTIVATED_EXTENT,
            "cultivatedExtent.lessThan=" + DEFAULT_CULTIVATED_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedExtentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where cultivatedExtent is greater than
        defaultIndexPayoutEventListFiltering(
            "cultivatedExtent.greaterThan=" + SMALLER_CULTIVATED_EXTENT,
            "cultivatedExtent.greaterThan=" + DEFAULT_CULTIVATED_EXTENT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout equals to
        defaultIndexPayoutEventListFiltering("payout.equals=" + DEFAULT_PAYOUT, "payout.equals=" + UPDATED_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout in
        defaultIndexPayoutEventListFiltering("payout.in=" + DEFAULT_PAYOUT + "," + UPDATED_PAYOUT, "payout.in=" + UPDATED_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout is not null
        defaultIndexPayoutEventListFiltering("payout.specified=true", "payout.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout is greater than or equal to
        defaultIndexPayoutEventListFiltering("payout.greaterThanOrEqual=" + DEFAULT_PAYOUT, "payout.greaterThanOrEqual=" + UPDATED_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout is less than or equal to
        defaultIndexPayoutEventListFiltering("payout.lessThanOrEqual=" + DEFAULT_PAYOUT, "payout.lessThanOrEqual=" + SMALLER_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout is less than
        defaultIndexPayoutEventListFiltering("payout.lessThan=" + UPDATED_PAYOUT, "payout.lessThan=" + DEFAULT_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByPayoutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where payout is greater than
        defaultIndexPayoutEventListFiltering("payout.greaterThan=" + SMALLER_PAYOUT, "payout.greaterThan=" + DEFAULT_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedDate equals to
        defaultIndexPayoutEventListFiltering(
            "confirmedDate.equals=" + DEFAULT_CONFIRMED_DATE,
            "confirmedDate.equals=" + UPDATED_CONFIRMED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedDate in
        defaultIndexPayoutEventListFiltering(
            "confirmedDate.in=" + DEFAULT_CONFIRMED_DATE + "," + UPDATED_CONFIRMED_DATE,
            "confirmedDate.in=" + UPDATED_CONFIRMED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedDate is not null
        defaultIndexPayoutEventListFiltering("confirmedDate.specified=true", "confirmedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedDateContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedDate contains
        defaultIndexPayoutEventListFiltering(
            "confirmedDate.contains=" + DEFAULT_CONFIRMED_DATE,
            "confirmedDate.contains=" + UPDATED_CONFIRMED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByConfirmedDateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where confirmedDate does not contain
        defaultIndexPayoutEventListFiltering(
            "confirmedDate.doesNotContain=" + UPDATED_CONFIRMED_DATE,
            "confirmedDate.doesNotContain=" + DEFAULT_CONFIRMED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy equals to
        defaultIndexPayoutEventListFiltering("rejectedBy.equals=" + DEFAULT_REJECTED_BY, "rejectedBy.equals=" + UPDATED_REJECTED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy in
        defaultIndexPayoutEventListFiltering(
            "rejectedBy.in=" + DEFAULT_REJECTED_BY + "," + UPDATED_REJECTED_BY,
            "rejectedBy.in=" + UPDATED_REJECTED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy is not null
        defaultIndexPayoutEventListFiltering("rejectedBy.specified=true", "rejectedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "rejectedBy.greaterThanOrEqual=" + DEFAULT_REJECTED_BY,
            "rejectedBy.greaterThanOrEqual=" + UPDATED_REJECTED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "rejectedBy.lessThanOrEqual=" + DEFAULT_REJECTED_BY,
            "rejectedBy.lessThanOrEqual=" + SMALLER_REJECTED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy is less than
        defaultIndexPayoutEventListFiltering("rejectedBy.lessThan=" + UPDATED_REJECTED_BY, "rejectedBy.lessThan=" + DEFAULT_REJECTED_BY);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedBy is greater than
        defaultIndexPayoutEventListFiltering(
            "rejectedBy.greaterThan=" + SMALLER_REJECTED_BY,
            "rejectedBy.greaterThan=" + DEFAULT_REJECTED_BY
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedDate equals to
        defaultIndexPayoutEventListFiltering(
            "rejectedDate.equals=" + DEFAULT_REJECTED_DATE,
            "rejectedDate.equals=" + UPDATED_REJECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedDate in
        defaultIndexPayoutEventListFiltering(
            "rejectedDate.in=" + DEFAULT_REJECTED_DATE + "," + UPDATED_REJECTED_DATE,
            "rejectedDate.in=" + UPDATED_REJECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedDate is not null
        defaultIndexPayoutEventListFiltering("rejectedDate.specified=true", "rejectedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedDateContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedDate contains
        defaultIndexPayoutEventListFiltering(
            "rejectedDate.contains=" + DEFAULT_REJECTED_DATE,
            "rejectedDate.contains=" + UPDATED_REJECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByRejectedDateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where rejectedDate does not contain
        defaultIndexPayoutEventListFiltering(
            "rejectedDate.doesNotContain=" + UPDATED_REJECTED_DATE,
            "rejectedDate.doesNotContain=" + DEFAULT_REJECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where reason equals to
        defaultIndexPayoutEventListFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where reason in
        defaultIndexPayoutEventListFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where reason is not null
        defaultIndexPayoutEventListFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where reason contains
        defaultIndexPayoutEventListFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where reason does not contain
        defaultIndexPayoutEventListFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout equals to
        defaultIndexPayoutEventListFiltering("finalPayout.equals=" + DEFAULT_FINAL_PAYOUT, "finalPayout.equals=" + UPDATED_FINAL_PAYOUT);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout in
        defaultIndexPayoutEventListFiltering(
            "finalPayout.in=" + DEFAULT_FINAL_PAYOUT + "," + UPDATED_FINAL_PAYOUT,
            "finalPayout.in=" + UPDATED_FINAL_PAYOUT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout is not null
        defaultIndexPayoutEventListFiltering("finalPayout.specified=true", "finalPayout.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "finalPayout.greaterThanOrEqual=" + DEFAULT_FINAL_PAYOUT,
            "finalPayout.greaterThanOrEqual=" + UPDATED_FINAL_PAYOUT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "finalPayout.lessThanOrEqual=" + DEFAULT_FINAL_PAYOUT,
            "finalPayout.lessThanOrEqual=" + SMALLER_FINAL_PAYOUT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout is less than
        defaultIndexPayoutEventListFiltering(
            "finalPayout.lessThan=" + UPDATED_FINAL_PAYOUT,
            "finalPayout.lessThan=" + DEFAULT_FINAL_PAYOUT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByFinalPayoutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where finalPayout is greater than
        defaultIndexPayoutEventListFiltering(
            "finalPayout.greaterThan=" + SMALLER_FINAL_PAYOUT,
            "finalPayout.greaterThan=" + DEFAULT_FINAL_PAYOUT
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus equals to
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventStatus.equals=" + DEFAULT_INDEX_PAYOUT_EVENT_STATUS,
            "indexPayoutEventStatus.equals=" + UPDATED_INDEX_PAYOUT_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus in
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventStatus.in=" + DEFAULT_INDEX_PAYOUT_EVENT_STATUS + "," + UPDATED_INDEX_PAYOUT_EVENT_STATUS,
            "indexPayoutEventStatus.in=" + UPDATED_INDEX_PAYOUT_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus is not null
        defaultIndexPayoutEventListFiltering("indexPayoutEventStatus.specified=true", "indexPayoutEventStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventStatus.greaterThanOrEqual=" + DEFAULT_INDEX_PAYOUT_EVENT_STATUS,
            "indexPayoutEventStatus.greaterThanOrEqual=" + UPDATED_INDEX_PAYOUT_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventStatus.lessThanOrEqual=" + DEFAULT_INDEX_PAYOUT_EVENT_STATUS,
            "indexPayoutEventStatus.lessThanOrEqual=" + SMALLER_INDEX_PAYOUT_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus is less than
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventStatus.lessThan=" + UPDATED_INDEX_PAYOUT_EVENT_STATUS,
            "indexPayoutEventStatus.lessThan=" + DEFAULT_INDEX_PAYOUT_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexPayoutEventStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexPayoutEventStatus is greater than
        defaultIndexPayoutEventListFiltering(
            "indexPayoutEventStatus.greaterThan=" + SMALLER_INDEX_PAYOUT_EVENT_STATUS,
            "indexPayoutEventStatus.greaterThan=" + DEFAULT_INDEX_PAYOUT_EVENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved equals to
        defaultIndexPayoutEventListFiltering("isApproved.equals=" + DEFAULT_IS_APPROVED, "isApproved.equals=" + UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved in
        defaultIndexPayoutEventListFiltering(
            "isApproved.in=" + DEFAULT_IS_APPROVED + "," + UPDATED_IS_APPROVED,
            "isApproved.in=" + UPDATED_IS_APPROVED
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved is not null
        defaultIndexPayoutEventListFiltering("isApproved.specified=true", "isApproved.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "isApproved.greaterThanOrEqual=" + DEFAULT_IS_APPROVED,
            "isApproved.greaterThanOrEqual=" + UPDATED_IS_APPROVED
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "isApproved.lessThanOrEqual=" + DEFAULT_IS_APPROVED,
            "isApproved.lessThanOrEqual=" + SMALLER_IS_APPROVED
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved is less than
        defaultIndexPayoutEventListFiltering("isApproved.lessThan=" + UPDATED_IS_APPROVED, "isApproved.lessThan=" + DEFAULT_IS_APPROVED);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsApprovedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isApproved is greater than
        defaultIndexPayoutEventListFiltering(
            "isApproved.greaterThan=" + SMALLER_IS_APPROVED,
            "isApproved.greaterThan=" + DEFAULT_IS_APPROVED
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange equals to
        defaultIndexPayoutEventListFiltering(
            "monitoringRange.equals=" + DEFAULT_MONITORING_RANGE,
            "monitoringRange.equals=" + UPDATED_MONITORING_RANGE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange in
        defaultIndexPayoutEventListFiltering(
            "monitoringRange.in=" + DEFAULT_MONITORING_RANGE + "," + UPDATED_MONITORING_RANGE,
            "monitoringRange.in=" + UPDATED_MONITORING_RANGE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange is not null
        defaultIndexPayoutEventListFiltering("monitoringRange.specified=true", "monitoringRange.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "monitoringRange.greaterThanOrEqual=" + DEFAULT_MONITORING_RANGE,
            "monitoringRange.greaterThanOrEqual=" + UPDATED_MONITORING_RANGE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "monitoringRange.lessThanOrEqual=" + DEFAULT_MONITORING_RANGE,
            "monitoringRange.lessThanOrEqual=" + SMALLER_MONITORING_RANGE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange is less than
        defaultIndexPayoutEventListFiltering(
            "monitoringRange.lessThan=" + UPDATED_MONITORING_RANGE,
            "monitoringRange.lessThan=" + DEFAULT_MONITORING_RANGE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByMonitoringRangeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where monitoringRange is greater than
        defaultIndexPayoutEventListFiltering(
            "monitoringRange.greaterThan=" + SMALLER_MONITORING_RANGE,
            "monitoringRange.greaterThan=" + DEFAULT_MONITORING_RANGE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance equals to
        defaultIndexPayoutEventListFiltering("isInsurance.equals=" + DEFAULT_IS_INSURANCE, "isInsurance.equals=" + UPDATED_IS_INSURANCE);
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance in
        defaultIndexPayoutEventListFiltering(
            "isInsurance.in=" + DEFAULT_IS_INSURANCE + "," + UPDATED_IS_INSURANCE,
            "isInsurance.in=" + UPDATED_IS_INSURANCE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance is not null
        defaultIndexPayoutEventListFiltering("isInsurance.specified=true", "isInsurance.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "isInsurance.greaterThanOrEqual=" + DEFAULT_IS_INSURANCE,
            "isInsurance.greaterThanOrEqual=" + UPDATED_IS_INSURANCE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "isInsurance.lessThanOrEqual=" + DEFAULT_IS_INSURANCE,
            "isInsurance.lessThanOrEqual=" + SMALLER_IS_INSURANCE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance is less than
        defaultIndexPayoutEventListFiltering(
            "isInsurance.lessThan=" + UPDATED_IS_INSURANCE,
            "isInsurance.lessThan=" + DEFAULT_IS_INSURANCE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIsInsuranceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where isInsurance is greater than
        defaultIndexPayoutEventListFiltering(
            "isInsurance.greaterThan=" + SMALLER_IS_INSURANCE,
            "isInsurance.greaterThan=" + DEFAULT_IS_INSURANCE
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand equals to
        defaultIndexPayoutEventListFiltering(
            "insuranceCultivatedLand.equals=" + DEFAULT_INSURANCE_CULTIVATED_LAND,
            "insuranceCultivatedLand.equals=" + UPDATED_INSURANCE_CULTIVATED_LAND
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand in
        defaultIndexPayoutEventListFiltering(
            "insuranceCultivatedLand.in=" + DEFAULT_INSURANCE_CULTIVATED_LAND + "," + UPDATED_INSURANCE_CULTIVATED_LAND,
            "insuranceCultivatedLand.in=" + UPDATED_INSURANCE_CULTIVATED_LAND
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand is not null
        defaultIndexPayoutEventListFiltering("insuranceCultivatedLand.specified=true", "insuranceCultivatedLand.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "insuranceCultivatedLand.greaterThanOrEqual=" + DEFAULT_INSURANCE_CULTIVATED_LAND,
            "insuranceCultivatedLand.greaterThanOrEqual=" + UPDATED_INSURANCE_CULTIVATED_LAND
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "insuranceCultivatedLand.lessThanOrEqual=" + DEFAULT_INSURANCE_CULTIVATED_LAND,
            "insuranceCultivatedLand.lessThanOrEqual=" + SMALLER_INSURANCE_CULTIVATED_LAND
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand is less than
        defaultIndexPayoutEventListFiltering(
            "insuranceCultivatedLand.lessThan=" + UPDATED_INSURANCE_CULTIVATED_LAND,
            "insuranceCultivatedLand.lessThan=" + DEFAULT_INSURANCE_CULTIVATED_LAND
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByInsuranceCultivatedLandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where insuranceCultivatedLand is greater than
        defaultIndexPayoutEventListFiltering(
            "insuranceCultivatedLand.greaterThan=" + SMALLER_INSURANCE_CULTIVATED_LAND,
            "insuranceCultivatedLand.greaterThan=" + DEFAULT_INSURANCE_CULTIVATED_LAND
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId equals to
        defaultIndexPayoutEventListFiltering(
            "indexChequeId.equals=" + DEFAULT_INDEX_CHEQUE_ID,
            "indexChequeId.equals=" + UPDATED_INDEX_CHEQUE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId in
        defaultIndexPayoutEventListFiltering(
            "indexChequeId.in=" + DEFAULT_INDEX_CHEQUE_ID + "," + UPDATED_INDEX_CHEQUE_ID,
            "indexChequeId.in=" + UPDATED_INDEX_CHEQUE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId is not null
        defaultIndexPayoutEventListFiltering("indexChequeId.specified=true", "indexChequeId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexChequeId.greaterThanOrEqual=" + DEFAULT_INDEX_CHEQUE_ID,
            "indexChequeId.greaterThanOrEqual=" + UPDATED_INDEX_CHEQUE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexChequeId.lessThanOrEqual=" + DEFAULT_INDEX_CHEQUE_ID,
            "indexChequeId.lessThanOrEqual=" + SMALLER_INDEX_CHEQUE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId is less than
        defaultIndexPayoutEventListFiltering(
            "indexChequeId.lessThan=" + UPDATED_INDEX_CHEQUE_ID,
            "indexChequeId.lessThan=" + DEFAULT_INDEX_CHEQUE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexChequeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexChequeId is greater than
        defaultIndexPayoutEventListFiltering(
            "indexChequeId.greaterThan=" + SMALLER_INDEX_CHEQUE_ID,
            "indexChequeId.greaterThan=" + DEFAULT_INDEX_CHEQUE_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId equals to
        defaultIndexPayoutEventListFiltering(
            "indexProductId.equals=" + DEFAULT_INDEX_PRODUCT_ID,
            "indexProductId.equals=" + UPDATED_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId in
        defaultIndexPayoutEventListFiltering(
            "indexProductId.in=" + DEFAULT_INDEX_PRODUCT_ID + "," + UPDATED_INDEX_PRODUCT_ID,
            "indexProductId.in=" + UPDATED_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId is not null
        defaultIndexPayoutEventListFiltering("indexProductId.specified=true", "indexProductId.specified=false");
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId is greater than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexProductId.greaterThanOrEqual=" + DEFAULT_INDEX_PRODUCT_ID,
            "indexProductId.greaterThanOrEqual=" + UPDATED_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId is less than or equal to
        defaultIndexPayoutEventListFiltering(
            "indexProductId.lessThanOrEqual=" + DEFAULT_INDEX_PRODUCT_ID,
            "indexProductId.lessThanOrEqual=" + SMALLER_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId is less than
        defaultIndexPayoutEventListFiltering(
            "indexProductId.lessThan=" + UPDATED_INDEX_PRODUCT_ID,
            "indexProductId.lessThan=" + DEFAULT_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByIndexProductIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        // Get all the indexPayoutEventListList where indexProductId is greater than
        defaultIndexPayoutEventListFiltering(
            "indexProductId.greaterThan=" + SMALLER_INDEX_PRODUCT_ID,
            "indexProductId.greaterThan=" + DEFAULT_INDEX_PRODUCT_ID
        );
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedFarmerIsEqualToSomething() throws Exception {
        IndexPayoutEventListFarmer cultivatedFarmer;
        if (TestUtil.findAll(em, IndexPayoutEventListFarmer.class).isEmpty()) {
            indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);
            cultivatedFarmer = IndexPayoutEventListFarmerResourceIT.createEntity();
        } else {
            cultivatedFarmer = TestUtil.findAll(em, IndexPayoutEventListFarmer.class).get(0);
        }
        em.persist(cultivatedFarmer);
        em.flush();
        indexPayoutEventList.setCultivatedFarmer(cultivatedFarmer);
        indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);
        Long cultivatedFarmerId = cultivatedFarmer.getId();
        // Get all the indexPayoutEventListList where cultivatedFarmer equals to cultivatedFarmerId
        defaultIndexPayoutEventListShouldBeFound("cultivatedFarmerId.equals=" + cultivatedFarmerId);

        // Get all the indexPayoutEventListList where cultivatedFarmer equals to (cultivatedFarmerId + 1)
        defaultIndexPayoutEventListShouldNotBeFound("cultivatedFarmerId.equals=" + (cultivatedFarmerId + 1));
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsByCultivatedLandIsEqualToSomething() throws Exception {
        IndexPayoutEventListCultivatedLand cultivatedLand;
        if (TestUtil.findAll(em, IndexPayoutEventListCultivatedLand.class).isEmpty()) {
            indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);
            cultivatedLand = IndexPayoutEventListCultivatedLandResourceIT.createEntity();
        } else {
            cultivatedLand = TestUtil.findAll(em, IndexPayoutEventListCultivatedLand.class).get(0);
        }
        em.persist(cultivatedLand);
        em.flush();
        indexPayoutEventList.setCultivatedLand(cultivatedLand);
        indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);
        Long cultivatedLandId = cultivatedLand.getId();
        // Get all the indexPayoutEventListList where cultivatedLand equals to cultivatedLandId
        defaultIndexPayoutEventListShouldBeFound("cultivatedLandId.equals=" + cultivatedLandId);

        // Get all the indexPayoutEventListList where cultivatedLand equals to (cultivatedLandId + 1)
        defaultIndexPayoutEventListShouldNotBeFound("cultivatedLandId.equals=" + (cultivatedLandId + 1));
    }

    @Test
    @Transactional
    void getAllIndexPayoutEventListsBySeasonIsEqualToSomething() throws Exception {
        IndexPayoutEventListSeason season;
        if (TestUtil.findAll(em, IndexPayoutEventListSeason.class).isEmpty()) {
            indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);
            season = IndexPayoutEventListSeasonResourceIT.createEntity();
        } else {
            season = TestUtil.findAll(em, IndexPayoutEventListSeason.class).get(0);
        }
        em.persist(season);
        em.flush();
        indexPayoutEventList.setSeason(season);
        indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);
        Long seasonId = season.getId();
        // Get all the indexPayoutEventListList where season equals to seasonId
        defaultIndexPayoutEventListShouldBeFound("seasonId.equals=" + seasonId);

        // Get all the indexPayoutEventListList where season equals to (seasonId + 1)
        defaultIndexPayoutEventListShouldNotBeFound("seasonId.equals=" + (seasonId + 1));
    }

    private void defaultIndexPayoutEventListFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndexPayoutEventListShouldBeFound(shouldBeFound);
        defaultIndexPayoutEventListShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndexPayoutEventListShouldBeFound(String filter) throws Exception {
        restIndexPayoutEventListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexPayoutEventList.getId().intValue())))
            .andExpect(jsonPath("$.[*].indexPayoutEventId").value(hasItem(DEFAULT_INDEX_PAYOUT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].ascId").value(hasItem(DEFAULT_ASC_ID)))
            .andExpect(jsonPath("$.[*].confirmedBy").value(hasItem(DEFAULT_CONFIRMED_BY)))
            .andExpect(jsonPath("$.[*].cultivatedExtent").value(hasItem(DEFAULT_CULTIVATED_EXTENT)))
            .andExpect(jsonPath("$.[*].payout").value(hasItem(DEFAULT_PAYOUT)))
            .andExpect(jsonPath("$.[*].confirmedDate").value(hasItem(DEFAULT_CONFIRMED_DATE)))
            .andExpect(jsonPath("$.[*].rejectedBy").value(hasItem(DEFAULT_REJECTED_BY)))
            .andExpect(jsonPath("$.[*].rejectedDate").value(hasItem(DEFAULT_REJECTED_DATE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].finalPayout").value(hasItem(DEFAULT_FINAL_PAYOUT)))
            .andExpect(jsonPath("$.[*].indexPayoutEventStatus").value(hasItem(DEFAULT_INDEX_PAYOUT_EVENT_STATUS)))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED)))
            .andExpect(jsonPath("$.[*].monitoringRange").value(hasItem(DEFAULT_MONITORING_RANGE)))
            .andExpect(jsonPath("$.[*].isInsurance").value(hasItem(DEFAULT_IS_INSURANCE)))
            .andExpect(jsonPath("$.[*].insuranceCultivatedLand").value(hasItem(DEFAULT_INSURANCE_CULTIVATED_LAND)))
            .andExpect(jsonPath("$.[*].indexChequeId").value(hasItem(DEFAULT_INDEX_CHEQUE_ID)))
            .andExpect(jsonPath("$.[*].indexProductId").value(hasItem(DEFAULT_INDEX_PRODUCT_ID)));

        // Check, that the count call also returns 1
        restIndexPayoutEventListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndexPayoutEventListShouldNotBeFound(String filter) throws Exception {
        restIndexPayoutEventListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndexPayoutEventListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndexPayoutEventList() throws Exception {
        // Get the indexPayoutEventList
        restIndexPayoutEventListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndexPayoutEventList() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventList
        IndexPayoutEventList updatedIndexPayoutEventList = indexPayoutEventListRepository
            .findById(indexPayoutEventList.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedIndexPayoutEventList are not directly saved in db
        em.detach(updatedIndexPayoutEventList);
        updatedIndexPayoutEventList
            .indexPayoutEventId(UPDATED_INDEX_PAYOUT_EVENT_ID)
            .ascId(UPDATED_ASC_ID)
            .confirmedBy(UPDATED_CONFIRMED_BY)
            .cultivatedExtent(UPDATED_CULTIVATED_EXTENT)
            .payout(UPDATED_PAYOUT)
            .confirmedDate(UPDATED_CONFIRMED_DATE)
            .rejectedBy(UPDATED_REJECTED_BY)
            .rejectedDate(UPDATED_REJECTED_DATE)
            .reason(UPDATED_REASON)
            .finalPayout(UPDATED_FINAL_PAYOUT)
            .indexPayoutEventStatus(UPDATED_INDEX_PAYOUT_EVENT_STATUS)
            .isApproved(UPDATED_IS_APPROVED)
            .monitoringRange(UPDATED_MONITORING_RANGE)
            .isInsurance(UPDATED_IS_INSURANCE)
            .insuranceCultivatedLand(UPDATED_INSURANCE_CULTIVATED_LAND)
            .indexChequeId(UPDATED_INDEX_CHEQUE_ID)
            .indexProductId(UPDATED_INDEX_PRODUCT_ID);

        restIndexPayoutEventListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIndexPayoutEventList.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIndexPayoutEventList))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndexPayoutEventListToMatchAllProperties(updatedIndexPayoutEventList);
    }

    @Test
    @Transactional
    void putNonExistingIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, indexPayoutEventList.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndexPayoutEventListWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventList using partial update
        IndexPayoutEventList partialUpdatedIndexPayoutEventList = new IndexPayoutEventList();
        partialUpdatedIndexPayoutEventList.setId(indexPayoutEventList.getId());

        partialUpdatedIndexPayoutEventList
            .payout(UPDATED_PAYOUT)
            .rejectedBy(UPDATED_REJECTED_BY)
            .reason(UPDATED_REASON)
            .isApproved(UPDATED_IS_APPROVED)
            .monitoringRange(UPDATED_MONITORING_RANGE)
            .isInsurance(UPDATED_IS_INSURANCE);

        restIndexPayoutEventListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventList.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventList))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndexPayoutEventList, indexPayoutEventList),
            getPersistedIndexPayoutEventList(indexPayoutEventList)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndexPayoutEventListWithPatch() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the indexPayoutEventList using partial update
        IndexPayoutEventList partialUpdatedIndexPayoutEventList = new IndexPayoutEventList();
        partialUpdatedIndexPayoutEventList.setId(indexPayoutEventList.getId());

        partialUpdatedIndexPayoutEventList
            .indexPayoutEventId(UPDATED_INDEX_PAYOUT_EVENT_ID)
            .ascId(UPDATED_ASC_ID)
            .confirmedBy(UPDATED_CONFIRMED_BY)
            .cultivatedExtent(UPDATED_CULTIVATED_EXTENT)
            .payout(UPDATED_PAYOUT)
            .confirmedDate(UPDATED_CONFIRMED_DATE)
            .rejectedBy(UPDATED_REJECTED_BY)
            .rejectedDate(UPDATED_REJECTED_DATE)
            .reason(UPDATED_REASON)
            .finalPayout(UPDATED_FINAL_PAYOUT)
            .indexPayoutEventStatus(UPDATED_INDEX_PAYOUT_EVENT_STATUS)
            .isApproved(UPDATED_IS_APPROVED)
            .monitoringRange(UPDATED_MONITORING_RANGE)
            .isInsurance(UPDATED_IS_INSURANCE)
            .insuranceCultivatedLand(UPDATED_INSURANCE_CULTIVATED_LAND)
            .indexChequeId(UPDATED_INDEX_CHEQUE_ID)
            .indexProductId(UPDATED_INDEX_PRODUCT_ID);

        restIndexPayoutEventListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndexPayoutEventList.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndexPayoutEventList))
            )
            .andExpect(status().isOk());

        // Validate the IndexPayoutEventList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndexPayoutEventListUpdatableFieldsEquals(
            partialUpdatedIndexPayoutEventList,
            getPersistedIndexPayoutEventList(partialUpdatedIndexPayoutEventList)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexPayoutEventListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, indexPayoutEventList.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndexPayoutEventList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        indexPayoutEventList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndexPayoutEventListMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(indexPayoutEventList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndexPayoutEventList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndexPayoutEventList() throws Exception {
        // Initialize the database
        insertedIndexPayoutEventList = indexPayoutEventListRepository.saveAndFlush(indexPayoutEventList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the indexPayoutEventList
        restIndexPayoutEventListMockMvc
            .perform(delete(ENTITY_API_URL_ID, indexPayoutEventList.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return indexPayoutEventListRepository.count();
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

    protected IndexPayoutEventList getPersistedIndexPayoutEventList(IndexPayoutEventList indexPayoutEventList) {
        return indexPayoutEventListRepository.findById(indexPayoutEventList.getId()).orElseThrow();
    }

    protected void assertPersistedIndexPayoutEventListToMatchAllProperties(IndexPayoutEventList expectedIndexPayoutEventList) {
        assertIndexPayoutEventListAllPropertiesEquals(
            expectedIndexPayoutEventList,
            getPersistedIndexPayoutEventList(expectedIndexPayoutEventList)
        );
    }

    protected void assertPersistedIndexPayoutEventListToMatchUpdatableProperties(IndexPayoutEventList expectedIndexPayoutEventList) {
        assertIndexPayoutEventListAllUpdatablePropertiesEquals(
            expectedIndexPayoutEventList,
            getPersistedIndexPayoutEventList(expectedIndexPayoutEventList)
        );
    }
}
