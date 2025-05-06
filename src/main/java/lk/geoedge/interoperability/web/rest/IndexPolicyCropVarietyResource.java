package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyCropVariety;
import lk.geoedge.interoperability.repository.IndexPolicyCropVarietyRepository;
import lk.geoedge.interoperability.service.IndexPolicyCropVarietyQueryService;
import lk.geoedge.interoperability.service.IndexPolicyCropVarietyService;
import lk.geoedge.interoperability.service.criteria.IndexPolicyCropVarietyCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPolicyCropVariety}.
 */
@RestController
@RequestMapping("/api/index-policy-crop-varieties")
public class IndexPolicyCropVarietyResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyCropVarietyResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPolicyCropVariety";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPolicyCropVarietyService indexPolicyCropVarietyService;

    private final IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository;

    private final IndexPolicyCropVarietyQueryService indexPolicyCropVarietyQueryService;

    public IndexPolicyCropVarietyResource(
        IndexPolicyCropVarietyService indexPolicyCropVarietyService,
        IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository,
        IndexPolicyCropVarietyQueryService indexPolicyCropVarietyQueryService
    ) {
        this.indexPolicyCropVarietyService = indexPolicyCropVarietyService;
        this.indexPolicyCropVarietyRepository = indexPolicyCropVarietyRepository;
        this.indexPolicyCropVarietyQueryService = indexPolicyCropVarietyQueryService;
    }

    /**
     * {@code POST  /index-policy-crop-varieties} : Create a new indexPolicyCropVariety.
     *
     * @param indexPolicyCropVariety the indexPolicyCropVariety to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPolicyCropVariety, or with status {@code 400 (Bad Request)} if the indexPolicyCropVariety has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPolicyCropVariety> createIndexPolicyCropVariety(@RequestBody IndexPolicyCropVariety indexPolicyCropVariety)
        throws URISyntaxException {
        LOG.debug("REST request to save IndexPolicyCropVariety : {}", indexPolicyCropVariety);
        if (indexPolicyCropVariety.getId() != null) {
            throw new BadRequestAlertException("A new indexPolicyCropVariety cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPolicyCropVariety = indexPolicyCropVarietyService.save(indexPolicyCropVariety);
        return ResponseEntity.created(new URI("/api/index-policy-crop-varieties/" + indexPolicyCropVariety.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPolicyCropVariety.getId().toString()))
            .body(indexPolicyCropVariety);
    }

    /**
     * {@code PUT  /index-policy-crop-varieties/:id} : Updates an existing indexPolicyCropVariety.
     *
     * @param id the id of the indexPolicyCropVariety to save.
     * @param indexPolicyCropVariety the indexPolicyCropVariety to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyCropVariety,
     * or with status {@code 400 (Bad Request)} if the indexPolicyCropVariety is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyCropVariety couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPolicyCropVariety> updateIndexPolicyCropVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyCropVariety indexPolicyCropVariety
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPolicyCropVariety : {}, {}", id, indexPolicyCropVariety);
        if (indexPolicyCropVariety.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyCropVariety.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyCropVarietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPolicyCropVariety = indexPolicyCropVarietyService.update(indexPolicyCropVariety);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyCropVariety.getId().toString()))
            .body(indexPolicyCropVariety);
    }

    /**
     * {@code PATCH  /index-policy-crop-varieties/:id} : Partial updates given fields of an existing indexPolicyCropVariety, field will ignore if it is null
     *
     * @param id the id of the indexPolicyCropVariety to save.
     * @param indexPolicyCropVariety the indexPolicyCropVariety to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyCropVariety,
     * or with status {@code 400 (Bad Request)} if the indexPolicyCropVariety is not valid,
     * or with status {@code 404 (Not Found)} if the indexPolicyCropVariety is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyCropVariety couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPolicyCropVariety> partialUpdateIndexPolicyCropVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyCropVariety indexPolicyCropVariety
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPolicyCropVariety partially : {}, {}", id, indexPolicyCropVariety);
        if (indexPolicyCropVariety.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyCropVariety.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyCropVarietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPolicyCropVariety> result = indexPolicyCropVarietyService.partialUpdate(indexPolicyCropVariety);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyCropVariety.getId().toString())
        );
    }

    /**
     * {@code GET  /index-policy-crop-varieties} : get all the indexPolicyCropVarieties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPolicyCropVarieties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPolicyCropVariety>> getAllIndexPolicyCropVarieties(
        IndexPolicyCropVarietyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPolicyCropVarieties by criteria: {}", criteria);

        Page<IndexPolicyCropVariety> page = indexPolicyCropVarietyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-policy-crop-varieties/count} : count all the indexPolicyCropVarieties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPolicyCropVarieties(IndexPolicyCropVarietyCriteria criteria) {
        LOG.debug("REST request to count IndexPolicyCropVarieties by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPolicyCropVarietyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-policy-crop-varieties/:id} : get the "id" indexPolicyCropVariety.
     *
     * @param id the id of the indexPolicyCropVariety to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPolicyCropVariety, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPolicyCropVariety> getIndexPolicyCropVariety(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPolicyCropVariety : {}", id);
        Optional<IndexPolicyCropVariety> indexPolicyCropVariety = indexPolicyCropVarietyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPolicyCropVariety);
    }

    /**
     * {@code DELETE  /index-policy-crop-varieties/:id} : delete the "id" indexPolicyCropVariety.
     *
     * @param id the id of the indexPolicyCropVariety to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPolicyCropVariety(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPolicyCropVariety : {}", id);
        indexPolicyCropVarietyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
