package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventListSeason;
import lk.geoedge.interoperability.repository.IndexPayoutEventListSeasonRepository;
import lk.geoedge.interoperability.service.IndexPayoutEventListSeasonQueryService;
import lk.geoedge.interoperability.service.IndexPayoutEventListSeasonService;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListSeasonCriteria;
import lk.geoedge.interoperability.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventListSeason}.
 */
@RestController
@RequestMapping("/api/index-payout-event-list-seasons")
public class IndexPayoutEventListSeasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListSeasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPayoutEventListSeason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPayoutEventListSeasonService indexPayoutEventListSeasonService;

    private final IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository;

    private final IndexPayoutEventListSeasonQueryService indexPayoutEventListSeasonQueryService;

    public IndexPayoutEventListSeasonResource(
        IndexPayoutEventListSeasonService indexPayoutEventListSeasonService,
        IndexPayoutEventListSeasonRepository indexPayoutEventListSeasonRepository,
        IndexPayoutEventListSeasonQueryService indexPayoutEventListSeasonQueryService
    ) {
        this.indexPayoutEventListSeasonService = indexPayoutEventListSeasonService;
        this.indexPayoutEventListSeasonRepository = indexPayoutEventListSeasonRepository;
        this.indexPayoutEventListSeasonQueryService = indexPayoutEventListSeasonQueryService;
    }

    /**
     * {@code POST  /index-payout-event-list-seasons} : Create a new indexPayoutEventListSeason.
     *
     * @param indexPayoutEventListSeason the indexPayoutEventListSeason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPayoutEventListSeason, or with status {@code 400 (Bad Request)} if the indexPayoutEventListSeason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPayoutEventListSeason> createIndexPayoutEventListSeason(
        @RequestBody IndexPayoutEventListSeason indexPayoutEventListSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to save IndexPayoutEventListSeason : {}", indexPayoutEventListSeason);
        if (indexPayoutEventListSeason.getId() != null) {
            throw new BadRequestAlertException("A new indexPayoutEventListSeason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPayoutEventListSeason = indexPayoutEventListSeasonService.save(indexPayoutEventListSeason);
        return ResponseEntity.created(new URI("/api/index-payout-event-list-seasons/" + indexPayoutEventListSeason.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListSeason.getId().toString())
            )
            .body(indexPayoutEventListSeason);
    }

    /**
     * {@code PUT  /index-payout-event-list-seasons/:id} : Updates an existing indexPayoutEventListSeason.
     *
     * @param id the id of the indexPayoutEventListSeason to save.
     * @param indexPayoutEventListSeason the indexPayoutEventListSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventListSeason,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventListSeason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventListSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPayoutEventListSeason> updateIndexPayoutEventListSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventListSeason indexPayoutEventListSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPayoutEventListSeason : {}, {}", id, indexPayoutEventListSeason);
        if (indexPayoutEventListSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventListSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPayoutEventListSeason = indexPayoutEventListSeasonService.update(indexPayoutEventListSeason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListSeason.getId().toString()))
            .body(indexPayoutEventListSeason);
    }

    /**
     * {@code PATCH  /index-payout-event-list-seasons/:id} : Partial updates given fields of an existing indexPayoutEventListSeason, field will ignore if it is null
     *
     * @param id the id of the indexPayoutEventListSeason to save.
     * @param indexPayoutEventListSeason the indexPayoutEventListSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventListSeason,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventListSeason is not valid,
     * or with status {@code 404 (Not Found)} if the indexPayoutEventListSeason is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventListSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPayoutEventListSeason> partialUpdateIndexPayoutEventListSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventListSeason indexPayoutEventListSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPayoutEventListSeason partially : {}, {}", id, indexPayoutEventListSeason);
        if (indexPayoutEventListSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventListSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPayoutEventListSeason> result = indexPayoutEventListSeasonService.partialUpdate(indexPayoutEventListSeason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListSeason.getId().toString())
        );
    }

    /**
     * {@code GET  /index-payout-event-list-seasons} : get all the indexPayoutEventListSeasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPayoutEventListSeasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPayoutEventListSeason>> getAllIndexPayoutEventListSeasons(
        IndexPayoutEventListSeasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPayoutEventListSeasons by criteria: {}", criteria);

        Page<IndexPayoutEventListSeason> page = indexPayoutEventListSeasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-payout-event-list-seasons/count} : count all the indexPayoutEventListSeasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPayoutEventListSeasons(IndexPayoutEventListSeasonCriteria criteria) {
        LOG.debug("REST request to count IndexPayoutEventListSeasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPayoutEventListSeasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-payout-event-list-seasons/:id} : get the "id" indexPayoutEventListSeason.
     *
     * @param id the id of the indexPayoutEventListSeason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPayoutEventListSeason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPayoutEventListSeason> getIndexPayoutEventListSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPayoutEventListSeason : {}", id);
        Optional<IndexPayoutEventListSeason> indexPayoutEventListSeason = indexPayoutEventListSeasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPayoutEventListSeason);
    }

    /**
     * {@code DELETE  /index-payout-event-list-seasons/:id} : delete the "id" indexPayoutEventListSeason.
     *
     * @param id the id of the indexPayoutEventListSeason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPayoutEventListSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPayoutEventListSeason : {}", id);
        indexPayoutEventListSeasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
