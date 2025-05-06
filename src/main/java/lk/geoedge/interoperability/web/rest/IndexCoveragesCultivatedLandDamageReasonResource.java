package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.IndexCoveragesCultivatedLandDamageReasonRepository;
import lk.geoedge.interoperability.service.IndexCoveragesCultivatedLandDamageReasonQueryService;
import lk.geoedge.interoperability.service.IndexCoveragesCultivatedLandDamageReasonService;
import lk.geoedge.interoperability.service.criteria.IndexCoveragesCultivatedLandDamageReasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason}.
 */
@RestController
@RequestMapping("/api/index-coverages-cultivated-land-damage-reasons")
public class IndexCoveragesCultivatedLandDamageReasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCoveragesCultivatedLandDamageReasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexCoveragesCultivatedLandDamageReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexCoveragesCultivatedLandDamageReasonService indexCoveragesCultivatedLandDamageReasonService;

    private final IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository;

    private final IndexCoveragesCultivatedLandDamageReasonQueryService indexCoveragesCultivatedLandDamageReasonQueryService;

    public IndexCoveragesCultivatedLandDamageReasonResource(
        IndexCoveragesCultivatedLandDamageReasonService indexCoveragesCultivatedLandDamageReasonService,
        IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository,
        IndexCoveragesCultivatedLandDamageReasonQueryService indexCoveragesCultivatedLandDamageReasonQueryService
    ) {
        this.indexCoveragesCultivatedLandDamageReasonService = indexCoveragesCultivatedLandDamageReasonService;
        this.indexCoveragesCultivatedLandDamageReasonRepository = indexCoveragesCultivatedLandDamageReasonRepository;
        this.indexCoveragesCultivatedLandDamageReasonQueryService = indexCoveragesCultivatedLandDamageReasonQueryService;
    }

    /**
     * {@code POST  /index-coverages-cultivated-land-damage-reasons} : Create a new indexCoveragesCultivatedLandDamageReason.
     *
     * @param indexCoveragesCultivatedLandDamageReason the indexCoveragesCultivatedLandDamageReason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexCoveragesCultivatedLandDamageReason, or with status {@code 400 (Bad Request)} if the indexCoveragesCultivatedLandDamageReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexCoveragesCultivatedLandDamageReason> createIndexCoveragesCultivatedLandDamageReason(
        @RequestBody IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug("REST request to save IndexCoveragesCultivatedLandDamageReason : {}", indexCoveragesCultivatedLandDamageReason);
        if (indexCoveragesCultivatedLandDamageReason.getId() != null) {
            throw new BadRequestAlertException(
                "A new indexCoveragesCultivatedLandDamageReason cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        indexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonService.save(
            indexCoveragesCultivatedLandDamageReason
        );
        return ResponseEntity.created(
            new URI("/api/index-coverages-cultivated-land-damage-reasons/" + indexCoveragesCultivatedLandDamageReason.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    indexCoveragesCultivatedLandDamageReason.getId().toString()
                )
            )
            .body(indexCoveragesCultivatedLandDamageReason);
    }

    /**
     * {@code PUT  /index-coverages-cultivated-land-damage-reasons/:id} : Updates an existing indexCoveragesCultivatedLandDamageReason.
     *
     * @param id the id of the indexCoveragesCultivatedLandDamageReason to save.
     * @param indexCoveragesCultivatedLandDamageReason the indexCoveragesCultivatedLandDamageReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexCoveragesCultivatedLandDamageReason,
     * or with status {@code 400 (Bad Request)} if the indexCoveragesCultivatedLandDamageReason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexCoveragesCultivatedLandDamageReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexCoveragesCultivatedLandDamageReason> updateIndexCoveragesCultivatedLandDamageReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexCoveragesCultivatedLandDamageReason : {}, {}", id, indexCoveragesCultivatedLandDamageReason);
        if (indexCoveragesCultivatedLandDamageReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexCoveragesCultivatedLandDamageReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexCoveragesCultivatedLandDamageReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexCoveragesCultivatedLandDamageReason = indexCoveragesCultivatedLandDamageReasonService.update(
            indexCoveragesCultivatedLandDamageReason
        );
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    indexCoveragesCultivatedLandDamageReason.getId().toString()
                )
            )
            .body(indexCoveragesCultivatedLandDamageReason);
    }

    /**
     * {@code PATCH  /index-coverages-cultivated-land-damage-reasons/:id} : Partial updates given fields of an existing indexCoveragesCultivatedLandDamageReason, field will ignore if it is null
     *
     * @param id the id of the indexCoveragesCultivatedLandDamageReason to save.
     * @param indexCoveragesCultivatedLandDamageReason the indexCoveragesCultivatedLandDamageReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexCoveragesCultivatedLandDamageReason,
     * or with status {@code 400 (Bad Request)} if the indexCoveragesCultivatedLandDamageReason is not valid,
     * or with status {@code 404 (Not Found)} if the indexCoveragesCultivatedLandDamageReason is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexCoveragesCultivatedLandDamageReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexCoveragesCultivatedLandDamageReason> partialUpdateIndexCoveragesCultivatedLandDamageReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update IndexCoveragesCultivatedLandDamageReason partially : {}, {}",
            id,
            indexCoveragesCultivatedLandDamageReason
        );
        if (indexCoveragesCultivatedLandDamageReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexCoveragesCultivatedLandDamageReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexCoveragesCultivatedLandDamageReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexCoveragesCultivatedLandDamageReason> result = indexCoveragesCultivatedLandDamageReasonService.partialUpdate(
            indexCoveragesCultivatedLandDamageReason
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                false,
                ENTITY_NAME,
                indexCoveragesCultivatedLandDamageReason.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /index-coverages-cultivated-land-damage-reasons} : get all the indexCoveragesCultivatedLandDamageReasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexCoveragesCultivatedLandDamageReasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexCoveragesCultivatedLandDamageReason>> getAllIndexCoveragesCultivatedLandDamageReasons(
        IndexCoveragesCultivatedLandDamageReasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexCoveragesCultivatedLandDamageReasons by criteria: {}", criteria);

        Page<IndexCoveragesCultivatedLandDamageReason> page = indexCoveragesCultivatedLandDamageReasonQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-coverages-cultivated-land-damage-reasons/count} : count all the indexCoveragesCultivatedLandDamageReasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexCoveragesCultivatedLandDamageReasons(IndexCoveragesCultivatedLandDamageReasonCriteria criteria) {
        LOG.debug("REST request to count IndexCoveragesCultivatedLandDamageReasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexCoveragesCultivatedLandDamageReasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-coverages-cultivated-land-damage-reasons/:id} : get the "id" indexCoveragesCultivatedLandDamageReason.
     *
     * @param id the id of the indexCoveragesCultivatedLandDamageReason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexCoveragesCultivatedLandDamageReason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexCoveragesCultivatedLandDamageReason> getIndexCoveragesCultivatedLandDamageReason(
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get IndexCoveragesCultivatedLandDamageReason : {}", id);
        Optional<IndexCoveragesCultivatedLandDamageReason> indexCoveragesCultivatedLandDamageReason =
            indexCoveragesCultivatedLandDamageReasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexCoveragesCultivatedLandDamageReason);
    }

    /**
     * {@code DELETE  /index-coverages-cultivated-land-damage-reasons/:id} : delete the "id" indexCoveragesCultivatedLandDamageReason.
     *
     * @param id the id of the indexCoveragesCultivatedLandDamageReason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexCoveragesCultivatedLandDamageReason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexCoveragesCultivatedLandDamageReason : {}", id);
        indexCoveragesCultivatedLandDamageReasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
