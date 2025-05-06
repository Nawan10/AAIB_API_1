package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand;
import lk.geoedge.interoperability.repository.IndexPayoutEventListCultivatedLandRepository;
import lk.geoedge.interoperability.service.IndexPayoutEventListCultivatedLandQueryService;
import lk.geoedge.interoperability.service.IndexPayoutEventListCultivatedLandService;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListCultivatedLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand}.
 */
@RestController
@RequestMapping("/api/index-payout-event-list-cultivated-lands")
public class IndexPayoutEventListCultivatedLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListCultivatedLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPayoutEventListCultivatedLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPayoutEventListCultivatedLandService indexPayoutEventListCultivatedLandService;

    private final IndexPayoutEventListCultivatedLandRepository indexPayoutEventListCultivatedLandRepository;

    private final IndexPayoutEventListCultivatedLandQueryService indexPayoutEventListCultivatedLandQueryService;

    public IndexPayoutEventListCultivatedLandResource(
        IndexPayoutEventListCultivatedLandService indexPayoutEventListCultivatedLandService,
        IndexPayoutEventListCultivatedLandRepository indexPayoutEventListCultivatedLandRepository,
        IndexPayoutEventListCultivatedLandQueryService indexPayoutEventListCultivatedLandQueryService
    ) {
        this.indexPayoutEventListCultivatedLandService = indexPayoutEventListCultivatedLandService;
        this.indexPayoutEventListCultivatedLandRepository = indexPayoutEventListCultivatedLandRepository;
        this.indexPayoutEventListCultivatedLandQueryService = indexPayoutEventListCultivatedLandQueryService;
    }

    /**
     * {@code POST  /index-payout-event-list-cultivated-lands} : Create a new indexPayoutEventListCultivatedLand.
     *
     * @param indexPayoutEventListCultivatedLand the indexPayoutEventListCultivatedLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPayoutEventListCultivatedLand, or with status {@code 400 (Bad Request)} if the indexPayoutEventListCultivatedLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPayoutEventListCultivatedLand> createIndexPayoutEventListCultivatedLand(
        @RequestBody IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to save IndexPayoutEventListCultivatedLand : {}", indexPayoutEventListCultivatedLand);
        if (indexPayoutEventListCultivatedLand.getId() != null) {
            throw new BadRequestAlertException(
                "A new indexPayoutEventListCultivatedLand cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        indexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandService.save(indexPayoutEventListCultivatedLand);
        return ResponseEntity.created(
            new URI("/api/index-payout-event-list-cultivated-lands/" + indexPayoutEventListCultivatedLand.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    indexPayoutEventListCultivatedLand.getId().toString()
                )
            )
            .body(indexPayoutEventListCultivatedLand);
    }

    /**
     * {@code PUT  /index-payout-event-list-cultivated-lands/:id} : Updates an existing indexPayoutEventListCultivatedLand.
     *
     * @param id the id of the indexPayoutEventListCultivatedLand to save.
     * @param indexPayoutEventListCultivatedLand the indexPayoutEventListCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventListCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventListCultivatedLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventListCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPayoutEventListCultivatedLand> updateIndexPayoutEventListCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPayoutEventListCultivatedLand : {}, {}", id, indexPayoutEventListCultivatedLand);
        if (indexPayoutEventListCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventListCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandService.update(indexPayoutEventListCultivatedLand);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    indexPayoutEventListCultivatedLand.getId().toString()
                )
            )
            .body(indexPayoutEventListCultivatedLand);
    }

    /**
     * {@code PATCH  /index-payout-event-list-cultivated-lands/:id} : Partial updates given fields of an existing indexPayoutEventListCultivatedLand, field will ignore if it is null
     *
     * @param id the id of the indexPayoutEventListCultivatedLand to save.
     * @param indexPayoutEventListCultivatedLand the indexPayoutEventListCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventListCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventListCultivatedLand is not valid,
     * or with status {@code 404 (Not Found)} if the indexPayoutEventListCultivatedLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventListCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPayoutEventListCultivatedLand> partialUpdateIndexPayoutEventListCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update IndexPayoutEventListCultivatedLand partially : {}, {}",
            id,
            indexPayoutEventListCultivatedLand
        );
        if (indexPayoutEventListCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventListCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPayoutEventListCultivatedLand> result = indexPayoutEventListCultivatedLandService.partialUpdate(
            indexPayoutEventListCultivatedLand
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListCultivatedLand.getId().toString())
        );
    }

    /**
     * {@code GET  /index-payout-event-list-cultivated-lands} : get all the indexPayoutEventListCultivatedLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPayoutEventListCultivatedLands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPayoutEventListCultivatedLand>> getAllIndexPayoutEventListCultivatedLands(
        IndexPayoutEventListCultivatedLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPayoutEventListCultivatedLands by criteria: {}", criteria);

        Page<IndexPayoutEventListCultivatedLand> page = indexPayoutEventListCultivatedLandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-payout-event-list-cultivated-lands/count} : count all the indexPayoutEventListCultivatedLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPayoutEventListCultivatedLands(IndexPayoutEventListCultivatedLandCriteria criteria) {
        LOG.debug("REST request to count IndexPayoutEventListCultivatedLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPayoutEventListCultivatedLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-payout-event-list-cultivated-lands/:id} : get the "id" indexPayoutEventListCultivatedLand.
     *
     * @param id the id of the indexPayoutEventListCultivatedLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPayoutEventListCultivatedLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPayoutEventListCultivatedLand> getIndexPayoutEventListCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPayoutEventListCultivatedLand : {}", id);
        Optional<IndexPayoutEventListCultivatedLand> indexPayoutEventListCultivatedLand = indexPayoutEventListCultivatedLandService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(indexPayoutEventListCultivatedLand);
    }

    /**
     * {@code DELETE  /index-payout-event-list-cultivated-lands/:id} : delete the "id" indexPayoutEventListCultivatedLand.
     *
     * @param id the id of the indexPayoutEventListCultivatedLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPayoutEventListCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPayoutEventListCultivatedLand : {}", id);
        indexPayoutEventListCultivatedLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
