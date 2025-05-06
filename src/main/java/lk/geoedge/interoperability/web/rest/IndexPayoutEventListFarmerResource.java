package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer;
import lk.geoedge.interoperability.repository.IndexPayoutEventListFarmerRepository;
import lk.geoedge.interoperability.service.IndexPayoutEventListFarmerQueryService;
import lk.geoedge.interoperability.service.IndexPayoutEventListFarmerService;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListFarmerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer}.
 */
@RestController
@RequestMapping("/api/index-payout-event-list-farmers")
public class IndexPayoutEventListFarmerResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListFarmerResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPayoutEventListFarmer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPayoutEventListFarmerService indexPayoutEventListFarmerService;

    private final IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository;

    private final IndexPayoutEventListFarmerQueryService indexPayoutEventListFarmerQueryService;

    public IndexPayoutEventListFarmerResource(
        IndexPayoutEventListFarmerService indexPayoutEventListFarmerService,
        IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository,
        IndexPayoutEventListFarmerQueryService indexPayoutEventListFarmerQueryService
    ) {
        this.indexPayoutEventListFarmerService = indexPayoutEventListFarmerService;
        this.indexPayoutEventListFarmerRepository = indexPayoutEventListFarmerRepository;
        this.indexPayoutEventListFarmerQueryService = indexPayoutEventListFarmerQueryService;
    }

    /**
     * {@code POST  /index-payout-event-list-farmers} : Create a new indexPayoutEventListFarmer.
     *
     * @param indexPayoutEventListFarmer the indexPayoutEventListFarmer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPayoutEventListFarmer, or with status {@code 400 (Bad Request)} if the indexPayoutEventListFarmer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPayoutEventListFarmer> createIndexPayoutEventListFarmer(
        @RequestBody IndexPayoutEventListFarmer indexPayoutEventListFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to save IndexPayoutEventListFarmer : {}", indexPayoutEventListFarmer);
        if (indexPayoutEventListFarmer.getId() != null) {
            throw new BadRequestAlertException("A new indexPayoutEventListFarmer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPayoutEventListFarmer = indexPayoutEventListFarmerService.save(indexPayoutEventListFarmer);
        return ResponseEntity.created(new URI("/api/index-payout-event-list-farmers/" + indexPayoutEventListFarmer.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListFarmer.getId().toString())
            )
            .body(indexPayoutEventListFarmer);
    }

    /**
     * {@code PUT  /index-payout-event-list-farmers/:id} : Updates an existing indexPayoutEventListFarmer.
     *
     * @param id the id of the indexPayoutEventListFarmer to save.
     * @param indexPayoutEventListFarmer the indexPayoutEventListFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventListFarmer,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventListFarmer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventListFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPayoutEventListFarmer> updateIndexPayoutEventListFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventListFarmer indexPayoutEventListFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPayoutEventListFarmer : {}, {}", id, indexPayoutEventListFarmer);
        if (indexPayoutEventListFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventListFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPayoutEventListFarmer = indexPayoutEventListFarmerService.update(indexPayoutEventListFarmer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListFarmer.getId().toString()))
            .body(indexPayoutEventListFarmer);
    }

    /**
     * {@code PATCH  /index-payout-event-list-farmers/:id} : Partial updates given fields of an existing indexPayoutEventListFarmer, field will ignore if it is null
     *
     * @param id the id of the indexPayoutEventListFarmer to save.
     * @param indexPayoutEventListFarmer the indexPayoutEventListFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPayoutEventListFarmer,
     * or with status {@code 400 (Bad Request)} if the indexPayoutEventListFarmer is not valid,
     * or with status {@code 404 (Not Found)} if the indexPayoutEventListFarmer is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPayoutEventListFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPayoutEventListFarmer> partialUpdateIndexPayoutEventListFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPayoutEventListFarmer indexPayoutEventListFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPayoutEventListFarmer partially : {}, {}", id, indexPayoutEventListFarmer);
        if (indexPayoutEventListFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPayoutEventListFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPayoutEventListFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPayoutEventListFarmer> result = indexPayoutEventListFarmerService.partialUpdate(indexPayoutEventListFarmer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPayoutEventListFarmer.getId().toString())
        );
    }

    /**
     * {@code GET  /index-payout-event-list-farmers} : get all the indexPayoutEventListFarmers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPayoutEventListFarmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPayoutEventListFarmer>> getAllIndexPayoutEventListFarmers(
        IndexPayoutEventListFarmerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPayoutEventListFarmers by criteria: {}", criteria);

        Page<IndexPayoutEventListFarmer> page = indexPayoutEventListFarmerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-payout-event-list-farmers/count} : count all the indexPayoutEventListFarmers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPayoutEventListFarmers(IndexPayoutEventListFarmerCriteria criteria) {
        LOG.debug("REST request to count IndexPayoutEventListFarmers by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPayoutEventListFarmerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-payout-event-list-farmers/:id} : get the "id" indexPayoutEventListFarmer.
     *
     * @param id the id of the indexPayoutEventListFarmer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPayoutEventListFarmer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPayoutEventListFarmer> getIndexPayoutEventListFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPayoutEventListFarmer : {}", id);
        Optional<IndexPayoutEventListFarmer> indexPayoutEventListFarmer = indexPayoutEventListFarmerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPayoutEventListFarmer);
    }

    /**
     * {@code DELETE  /index-payout-event-list-farmers/:id} : delete the "id" indexPayoutEventListFarmer.
     *
     * @param id the id of the indexPayoutEventListFarmer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPayoutEventListFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPayoutEventListFarmer : {}", id);
        indexPayoutEventListFarmerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
