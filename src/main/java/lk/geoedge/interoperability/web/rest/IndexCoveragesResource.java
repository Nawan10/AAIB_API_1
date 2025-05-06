package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexCoverages;
import lk.geoedge.interoperability.repository.IndexCoveragesRepository;
import lk.geoedge.interoperability.service.IndexCoveragesQueryService;
import lk.geoedge.interoperability.service.IndexCoveragesService;
import lk.geoedge.interoperability.service.criteria.IndexCoveragesCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexCoverages}.
 */
@RestController
@RequestMapping("/api/index-coverages")
public class IndexCoveragesResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCoveragesResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexCoverages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexCoveragesService indexCoveragesService;

    private final IndexCoveragesRepository indexCoveragesRepository;

    private final IndexCoveragesQueryService indexCoveragesQueryService;

    public IndexCoveragesResource(
        IndexCoveragesService indexCoveragesService,
        IndexCoveragesRepository indexCoveragesRepository,
        IndexCoveragesQueryService indexCoveragesQueryService
    ) {
        this.indexCoveragesService = indexCoveragesService;
        this.indexCoveragesRepository = indexCoveragesRepository;
        this.indexCoveragesQueryService = indexCoveragesQueryService;
    }

    /**
     * {@code POST  /index-coverages} : Create a new indexCoverages.
     *
     * @param indexCoverages the indexCoverages to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexCoverages, or with status {@code 400 (Bad Request)} if the indexCoverages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexCoverages> createIndexCoverages(@RequestBody IndexCoverages indexCoverages) throws URISyntaxException {
        LOG.debug("REST request to save IndexCoverages : {}", indexCoverages);
        if (indexCoverages.getId() != null) {
            throw new BadRequestAlertException("A new indexCoverages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexCoverages = indexCoveragesService.save(indexCoverages);
        return ResponseEntity.created(new URI("/api/index-coverages/" + indexCoverages.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexCoverages.getId().toString()))
            .body(indexCoverages);
    }

    /**
     * {@code PUT  /index-coverages/:id} : Updates an existing indexCoverages.
     *
     * @param id the id of the indexCoverages to save.
     * @param indexCoverages the indexCoverages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexCoverages,
     * or with status {@code 400 (Bad Request)} if the indexCoverages is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexCoverages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexCoverages> updateIndexCoverages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexCoverages indexCoverages
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexCoverages : {}, {}", id, indexCoverages);
        if (indexCoverages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexCoverages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexCoveragesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexCoverages = indexCoveragesService.update(indexCoverages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexCoverages.getId().toString()))
            .body(indexCoverages);
    }

    /**
     * {@code PATCH  /index-coverages/:id} : Partial updates given fields of an existing indexCoverages, field will ignore if it is null
     *
     * @param id the id of the indexCoverages to save.
     * @param indexCoverages the indexCoverages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexCoverages,
     * or with status {@code 400 (Bad Request)} if the indexCoverages is not valid,
     * or with status {@code 404 (Not Found)} if the indexCoverages is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexCoverages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexCoverages> partialUpdateIndexCoverages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexCoverages indexCoverages
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexCoverages partially : {}, {}", id, indexCoverages);
        if (indexCoverages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexCoverages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexCoveragesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexCoverages> result = indexCoveragesService.partialUpdate(indexCoverages);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexCoverages.getId().toString())
        );
    }

    /**
     * {@code GET  /index-coverages} : get all the indexCoverages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexCoverages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexCoverages>> getAllIndexCoverages(
        IndexCoveragesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexCoverages by criteria: {}", criteria);

        Page<IndexCoverages> page = indexCoveragesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-coverages/count} : count all the indexCoverages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexCoverages(IndexCoveragesCriteria criteria) {
        LOG.debug("REST request to count IndexCoverages by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexCoveragesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-coverages/:id} : get the "id" indexCoverages.
     *
     * @param id the id of the indexCoverages to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexCoverages, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexCoverages> getIndexCoverages(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexCoverages : {}", id);
        Optional<IndexCoverages> indexCoverages = indexCoveragesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexCoverages);
    }

    /**
     * {@code DELETE  /index-coverages/:id} : delete the "id" indexCoverages.
     *
     * @param id the id of the indexCoverages to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexCoverages(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexCoverages : {}", id);
        indexCoveragesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
