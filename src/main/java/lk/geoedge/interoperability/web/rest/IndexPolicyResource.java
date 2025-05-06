package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicy;
import lk.geoedge.interoperability.repository.IndexPolicyRepository;
import lk.geoedge.interoperability.service.IndexPolicyQueryService;
import lk.geoedge.interoperability.service.IndexPolicyService;
import lk.geoedge.interoperability.service.criteria.IndexPolicyCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPolicy}.
 */
@RestController
@RequestMapping("/api/index-policies")
public class IndexPolicyResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPolicy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPolicyService indexPolicyService;

    private final IndexPolicyRepository indexPolicyRepository;

    private final IndexPolicyQueryService indexPolicyQueryService;

    public IndexPolicyResource(
        IndexPolicyService indexPolicyService,
        IndexPolicyRepository indexPolicyRepository,
        IndexPolicyQueryService indexPolicyQueryService
    ) {
        this.indexPolicyService = indexPolicyService;
        this.indexPolicyRepository = indexPolicyRepository;
        this.indexPolicyQueryService = indexPolicyQueryService;
    }

    /**
     * {@code POST  /index-policies} : Create a new indexPolicy.
     *
     * @param indexPolicy the indexPolicy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPolicy, or with status {@code 400 (Bad Request)} if the indexPolicy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPolicy> createIndexPolicy(@RequestBody IndexPolicy indexPolicy) throws URISyntaxException {
        LOG.debug("REST request to save IndexPolicy : {}", indexPolicy);
        if (indexPolicy.getId() != null) {
            throw new BadRequestAlertException("A new indexPolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPolicy = indexPolicyService.save(indexPolicy);
        return ResponseEntity.created(new URI("/api/index-policies/" + indexPolicy.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPolicy.getId().toString()))
            .body(indexPolicy);
    }

    /**
     * {@code PUT  /index-policies/:id} : Updates an existing indexPolicy.
     *
     * @param id the id of the indexPolicy to save.
     * @param indexPolicy the indexPolicy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicy,
     * or with status {@code 400 (Bad Request)} if the indexPolicy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPolicy> updateIndexPolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicy indexPolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPolicy : {}, {}", id, indexPolicy);
        if (indexPolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPolicy = indexPolicyService.update(indexPolicy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicy.getId().toString()))
            .body(indexPolicy);
    }

    /**
     * {@code PATCH  /index-policies/:id} : Partial updates given fields of an existing indexPolicy, field will ignore if it is null
     *
     * @param id the id of the indexPolicy to save.
     * @param indexPolicy the indexPolicy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicy,
     * or with status {@code 400 (Bad Request)} if the indexPolicy is not valid,
     * or with status {@code 404 (Not Found)} if the indexPolicy is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPolicy> partialUpdateIndexPolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicy indexPolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPolicy partially : {}, {}", id, indexPolicy);
        if (indexPolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPolicy> result = indexPolicyService.partialUpdate(indexPolicy);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicy.getId().toString())
        );
    }

    /**
     * {@code GET  /index-policies} : get all the indexPolicies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPolicies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPolicy>> getAllIndexPolicies(
        IndexPolicyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPolicies by criteria: {}", criteria);

        Page<IndexPolicy> page = indexPolicyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-policies/count} : count all the indexPolicies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPolicies(IndexPolicyCriteria criteria) {
        LOG.debug("REST request to count IndexPolicies by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPolicyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-policies/:id} : get the "id" indexPolicy.
     *
     * @param id the id of the indexPolicy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPolicy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPolicy> getIndexPolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPolicy : {}", id);
        Optional<IndexPolicy> indexPolicy = indexPolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPolicy);
    }

    /**
     * {@code DELETE  /index-policies/:id} : delete the "id" indexPolicy.
     *
     * @param id the id of the indexPolicy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPolicy : {}", id);
        indexPolicyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
