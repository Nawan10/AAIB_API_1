package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicySeason;
import lk.geoedge.interoperability.repository.IndexPolicySeasonRepository;
import lk.geoedge.interoperability.service.IndexPolicySeasonQueryService;
import lk.geoedge.interoperability.service.IndexPolicySeasonService;
import lk.geoedge.interoperability.service.criteria.IndexPolicySeasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPolicySeason}.
 */
@RestController
@RequestMapping("/api/index-policy-seasons")
public class IndexPolicySeasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicySeasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPolicySeason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPolicySeasonService indexPolicySeasonService;

    private final IndexPolicySeasonRepository indexPolicySeasonRepository;

    private final IndexPolicySeasonQueryService indexPolicySeasonQueryService;

    public IndexPolicySeasonResource(
        IndexPolicySeasonService indexPolicySeasonService,
        IndexPolicySeasonRepository indexPolicySeasonRepository,
        IndexPolicySeasonQueryService indexPolicySeasonQueryService
    ) {
        this.indexPolicySeasonService = indexPolicySeasonService;
        this.indexPolicySeasonRepository = indexPolicySeasonRepository;
        this.indexPolicySeasonQueryService = indexPolicySeasonQueryService;
    }

    /**
     * {@code POST  /index-policy-seasons} : Create a new indexPolicySeason.
     *
     * @param indexPolicySeason the indexPolicySeason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPolicySeason, or with status {@code 400 (Bad Request)} if the indexPolicySeason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPolicySeason> createIndexPolicySeason(@RequestBody IndexPolicySeason indexPolicySeason)
        throws URISyntaxException {
        LOG.debug("REST request to save IndexPolicySeason : {}", indexPolicySeason);
        if (indexPolicySeason.getId() != null) {
            throw new BadRequestAlertException("A new indexPolicySeason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPolicySeason = indexPolicySeasonService.save(indexPolicySeason);
        return ResponseEntity.created(new URI("/api/index-policy-seasons/" + indexPolicySeason.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPolicySeason.getId().toString()))
            .body(indexPolicySeason);
    }

    /**
     * {@code PUT  /index-policy-seasons/:id} : Updates an existing indexPolicySeason.
     *
     * @param id the id of the indexPolicySeason to save.
     * @param indexPolicySeason the indexPolicySeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicySeason,
     * or with status {@code 400 (Bad Request)} if the indexPolicySeason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicySeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPolicySeason> updateIndexPolicySeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicySeason indexPolicySeason
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPolicySeason : {}, {}", id, indexPolicySeason);
        if (indexPolicySeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicySeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicySeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPolicySeason = indexPolicySeasonService.update(indexPolicySeason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicySeason.getId().toString()))
            .body(indexPolicySeason);
    }

    /**
     * {@code PATCH  /index-policy-seasons/:id} : Partial updates given fields of an existing indexPolicySeason, field will ignore if it is null
     *
     * @param id the id of the indexPolicySeason to save.
     * @param indexPolicySeason the indexPolicySeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicySeason,
     * or with status {@code 400 (Bad Request)} if the indexPolicySeason is not valid,
     * or with status {@code 404 (Not Found)} if the indexPolicySeason is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicySeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPolicySeason> partialUpdateIndexPolicySeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicySeason indexPolicySeason
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPolicySeason partially : {}, {}", id, indexPolicySeason);
        if (indexPolicySeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicySeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicySeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPolicySeason> result = indexPolicySeasonService.partialUpdate(indexPolicySeason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicySeason.getId().toString())
        );
    }

    /**
     * {@code GET  /index-policy-seasons} : get all the indexPolicySeasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPolicySeasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPolicySeason>> getAllIndexPolicySeasons(
        IndexPolicySeasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPolicySeasons by criteria: {}", criteria);

        Page<IndexPolicySeason> page = indexPolicySeasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-policy-seasons/count} : count all the indexPolicySeasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPolicySeasons(IndexPolicySeasonCriteria criteria) {
        LOG.debug("REST request to count IndexPolicySeasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPolicySeasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-policy-seasons/:id} : get the "id" indexPolicySeason.
     *
     * @param id the id of the indexPolicySeason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPolicySeason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPolicySeason> getIndexPolicySeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPolicySeason : {}", id);
        Optional<IndexPolicySeason> indexPolicySeason = indexPolicySeasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPolicySeason);
    }

    /**
     * {@code DELETE  /index-policy-seasons/:id} : delete the "id" indexPolicySeason.
     *
     * @param id the id of the indexPolicySeason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPolicySeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPolicySeason : {}", id);
        indexPolicySeasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
