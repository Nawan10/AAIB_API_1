package lk.geoedge.interoperability.web.rest;

import static lk.geoedge.interoperability.domain.MahaweliAsserts.*;
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
import lk.geoedge.interoperability.domain.Mahaweli;
import lk.geoedge.interoperability.repository.MahaweliRepository;
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
 * Integration tests for the {@link MahaweliResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MahaweliResourceIT {

    private static final String DEFAULT_MAHAWELI = "AAAAAAAAAA";
    private static final String UPDATED_MAHAWELI = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADDED_BY = 1;
    private static final Integer UPDATED_ADDED_BY = 2;
    private static final Integer SMALLER_ADDED_BY = 1 - 1;

    private static final LocalDate DEFAULT_ADDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADDED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ADDED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/mahawelis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MahaweliRepository mahaweliRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMahaweliMockMvc;

    private Mahaweli mahaweli;

    private Mahaweli insertedMahaweli;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mahaweli createEntity() {
        return new Mahaweli().mahaweli(DEFAULT_MAHAWELI).code(DEFAULT_CODE).addedBy(DEFAULT_ADDED_BY).addedDate(DEFAULT_ADDED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mahaweli createUpdatedEntity() {
        return new Mahaweli().mahaweli(UPDATED_MAHAWELI).code(UPDATED_CODE).addedBy(UPDATED_ADDED_BY).addedDate(UPDATED_ADDED_DATE);
    }

    @BeforeEach
    void initTest() {
        mahaweli = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMahaweli != null) {
            mahaweliRepository.delete(insertedMahaweli);
            insertedMahaweli = null;
        }
    }

    @Test
    @Transactional
    void createMahaweli() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mahaweli
        var returnedMahaweli = om.readValue(
            restMahaweliMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mahaweli)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Mahaweli.class
        );

        // Validate the Mahaweli in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMahaweliUpdatableFieldsEquals(returnedMahaweli, getPersistedMahaweli(returnedMahaweli));

        insertedMahaweli = returnedMahaweli;
    }

    @Test
    @Transactional
    void createMahaweliWithExistingId() throws Exception {
        // Create the Mahaweli with an existing ID
        mahaweli.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMahaweliMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mahaweli)))
            .andExpect(status().isBadRequest());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMahawelis() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList
        restMahaweliMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mahaweli.getId().intValue())))
            .andExpect(jsonPath("$.[*].mahaweli").value(hasItem(DEFAULT_MAHAWELI)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMahaweli() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get the mahaweli
        restMahaweliMockMvc
            .perform(get(ENTITY_API_URL_ID, mahaweli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mahaweli.getId().intValue()))
            .andExpect(jsonPath("$.mahaweli").value(DEFAULT_MAHAWELI))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.addedBy").value(DEFAULT_ADDED_BY))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMahawelisByIdFiltering() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        Long id = mahaweli.getId();

        defaultMahaweliFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMahaweliFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMahaweliFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMahawelisByMahaweliIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where mahaweli equals to
        defaultMahaweliFiltering("mahaweli.equals=" + DEFAULT_MAHAWELI, "mahaweli.equals=" + UPDATED_MAHAWELI);
    }

    @Test
    @Transactional
    void getAllMahawelisByMahaweliIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where mahaweli in
        defaultMahaweliFiltering("mahaweli.in=" + DEFAULT_MAHAWELI + "," + UPDATED_MAHAWELI, "mahaweli.in=" + UPDATED_MAHAWELI);
    }

    @Test
    @Transactional
    void getAllMahawelisByMahaweliIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where mahaweli is not null
        defaultMahaweliFiltering("mahaweli.specified=true", "mahaweli.specified=false");
    }

    @Test
    @Transactional
    void getAllMahawelisByMahaweliContainsSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where mahaweli contains
        defaultMahaweliFiltering("mahaweli.contains=" + DEFAULT_MAHAWELI, "mahaweli.contains=" + UPDATED_MAHAWELI);
    }

    @Test
    @Transactional
    void getAllMahawelisByMahaweliNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where mahaweli does not contain
        defaultMahaweliFiltering("mahaweli.doesNotContain=" + UPDATED_MAHAWELI, "mahaweli.doesNotContain=" + DEFAULT_MAHAWELI);
    }

    @Test
    @Transactional
    void getAllMahawelisByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where code equals to
        defaultMahaweliFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMahawelisByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where code in
        defaultMahaweliFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMahawelisByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where code is not null
        defaultMahaweliFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllMahawelisByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where code contains
        defaultMahaweliFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllMahawelisByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where code does not contain
        defaultMahaweliFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy equals to
        defaultMahaweliFiltering("addedBy.equals=" + DEFAULT_ADDED_BY, "addedBy.equals=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy in
        defaultMahaweliFiltering("addedBy.in=" + DEFAULT_ADDED_BY + "," + UPDATED_ADDED_BY, "addedBy.in=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy is not null
        defaultMahaweliFiltering("addedBy.specified=true", "addedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy is greater than or equal to
        defaultMahaweliFiltering("addedBy.greaterThanOrEqual=" + DEFAULT_ADDED_BY, "addedBy.greaterThanOrEqual=" + UPDATED_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy is less than or equal to
        defaultMahaweliFiltering("addedBy.lessThanOrEqual=" + DEFAULT_ADDED_BY, "addedBy.lessThanOrEqual=" + SMALLER_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy is less than
        defaultMahaweliFiltering("addedBy.lessThan=" + UPDATED_ADDED_BY, "addedBy.lessThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedBy is greater than
        defaultMahaweliFiltering("addedBy.greaterThan=" + SMALLER_ADDED_BY, "addedBy.greaterThan=" + DEFAULT_ADDED_BY);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate equals to
        defaultMahaweliFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate in
        defaultMahaweliFiltering("addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE, "addedDate.in=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate is not null
        defaultMahaweliFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate is greater than or equal to
        defaultMahaweliFiltering(
            "addedDate.greaterThanOrEqual=" + DEFAULT_ADDED_DATE,
            "addedDate.greaterThanOrEqual=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate is less than or equal to
        defaultMahaweliFiltering("addedDate.lessThanOrEqual=" + DEFAULT_ADDED_DATE, "addedDate.lessThanOrEqual=" + SMALLER_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate is less than
        defaultMahaweliFiltering("addedDate.lessThan=" + UPDATED_ADDED_DATE, "addedDate.lessThan=" + DEFAULT_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllMahawelisByAddedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        // Get all the mahaweliList where addedDate is greater than
        defaultMahaweliFiltering("addedDate.greaterThan=" + SMALLER_ADDED_DATE, "addedDate.greaterThan=" + DEFAULT_ADDED_DATE);
    }

    private void defaultMahaweliFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMahaweliShouldBeFound(shouldBeFound);
        defaultMahaweliShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMahaweliShouldBeFound(String filter) throws Exception {
        restMahaweliMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mahaweli.getId().intValue())))
            .andExpect(jsonPath("$.[*].mahaweli").value(hasItem(DEFAULT_MAHAWELI)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].addedBy").value(hasItem(DEFAULT_ADDED_BY)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));

        // Check, that the count call also returns 1
        restMahaweliMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMahaweliShouldNotBeFound(String filter) throws Exception {
        restMahaweliMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMahaweliMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMahaweli() throws Exception {
        // Get the mahaweli
        restMahaweliMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMahaweli() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mahaweli
        Mahaweli updatedMahaweli = mahaweliRepository.findById(mahaweli.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMahaweli are not directly saved in db
        em.detach(updatedMahaweli);
        updatedMahaweli.mahaweli(UPDATED_MAHAWELI).code(UPDATED_CODE).addedBy(UPDATED_ADDED_BY).addedDate(UPDATED_ADDED_DATE);

        restMahaweliMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMahaweli.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMahaweli))
            )
            .andExpect(status().isOk());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMahaweliToMatchAllProperties(updatedMahaweli);
    }

    @Test
    @Transactional
    void putNonExistingMahaweli() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mahaweli.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMahaweliMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mahaweli.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mahaweli))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMahaweli() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mahaweli.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMahaweliMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mahaweli))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMahaweli() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mahaweli.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMahaweliMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mahaweli)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMahaweliWithPatch() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mahaweli using partial update
        Mahaweli partialUpdatedMahaweli = new Mahaweli();
        partialUpdatedMahaweli.setId(mahaweli.getId());

        partialUpdatedMahaweli.code(UPDATED_CODE);

        restMahaweliMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMahaweli.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMahaweli))
            )
            .andExpect(status().isOk());

        // Validate the Mahaweli in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMahaweliUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMahaweli, mahaweli), getPersistedMahaweli(mahaweli));
    }

    @Test
    @Transactional
    void fullUpdateMahaweliWithPatch() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mahaweli using partial update
        Mahaweli partialUpdatedMahaweli = new Mahaweli();
        partialUpdatedMahaweli.setId(mahaweli.getId());

        partialUpdatedMahaweli.mahaweli(UPDATED_MAHAWELI).code(UPDATED_CODE).addedBy(UPDATED_ADDED_BY).addedDate(UPDATED_ADDED_DATE);

        restMahaweliMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMahaweli.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMahaweli))
            )
            .andExpect(status().isOk());

        // Validate the Mahaweli in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMahaweliUpdatableFieldsEquals(partialUpdatedMahaweli, getPersistedMahaweli(partialUpdatedMahaweli));
    }

    @Test
    @Transactional
    void patchNonExistingMahaweli() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mahaweli.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMahaweliMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mahaweli.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mahaweli))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMahaweli() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mahaweli.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMahaweliMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mahaweli))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMahaweli() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mahaweli.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMahaweliMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mahaweli)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mahaweli in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMahaweli() throws Exception {
        // Initialize the database
        insertedMahaweli = mahaweliRepository.saveAndFlush(mahaweli);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mahaweli
        restMahaweliMockMvc
            .perform(delete(ENTITY_API_URL_ID, mahaweli.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mahaweliRepository.count();
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

    protected Mahaweli getPersistedMahaweli(Mahaweli mahaweli) {
        return mahaweliRepository.findById(mahaweli.getId()).orElseThrow();
    }

    protected void assertPersistedMahaweliToMatchAllProperties(Mahaweli expectedMahaweli) {
        assertMahaweliAllPropertiesEquals(expectedMahaweli, getPersistedMahaweli(expectedMahaweli));
    }

    protected void assertPersistedMahaweliToMatchUpdatableProperties(Mahaweli expectedMahaweli) {
        assertMahaweliAllUpdatablePropertiesEquals(expectedMahaweli, getPersistedMahaweli(expectedMahaweli));
    }
}
