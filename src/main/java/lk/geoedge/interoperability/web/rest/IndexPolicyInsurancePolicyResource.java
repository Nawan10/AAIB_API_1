package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy;
import lk.geoedge.interoperability.repository.IndexPolicyInsurancePolicyRepository;
import lk.geoedge.interoperability.service.IndexPolicyInsurancePolicyQueryService;
import lk.geoedge.interoperability.service.IndexPolicyInsurancePolicyService;
import lk.geoedge.interoperability.service.criteria.IndexPolicyInsurancePolicyCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy}.
 */
@RestController
@RequestMapping("/api/index-policy-insurance-policies")
public class IndexPolicyInsurancePolicyResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyInsurancePolicyResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPolicyInsurancePolicy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPolicyInsurancePolicyService indexPolicyInsurancePolicyService;

    private final IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository;

    private final IndexPolicyInsurancePolicyQueryService indexPolicyInsurancePolicyQueryService;

    public IndexPolicyInsurancePolicyResource(
        IndexPolicyInsurancePolicyService indexPolicyInsurancePolicyService,
        IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository,
        IndexPolicyInsurancePolicyQueryService indexPolicyInsurancePolicyQueryService
    ) {
        this.indexPolicyInsurancePolicyService = indexPolicyInsurancePolicyService;
        this.indexPolicyInsurancePolicyRepository = indexPolicyInsurancePolicyRepository;
        this.indexPolicyInsurancePolicyQueryService = indexPolicyInsurancePolicyQueryService;
    }

    /**
     * {@code POST  /index-policy-insurance-policies} : Create a new indexPolicyInsurancePolicy.
     *
     * @param indexPolicyInsurancePolicy the indexPolicyInsurancePolicy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPolicyInsurancePolicy, or with status {@code 400 (Bad Request)} if the indexPolicyInsurancePolicy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPolicyInsurancePolicy> createIndexPolicyInsurancePolicy(
        @RequestBody IndexPolicyInsurancePolicy indexPolicyInsurancePolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to save IndexPolicyInsurancePolicy : {}", indexPolicyInsurancePolicy);
        if (indexPolicyInsurancePolicy.getId() != null) {
            throw new BadRequestAlertException("A new indexPolicyInsurancePolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPolicyInsurancePolicy = indexPolicyInsurancePolicyService.save(indexPolicyInsurancePolicy);
        return ResponseEntity.created(new URI("/api/index-policy-insurance-policies/" + indexPolicyInsurancePolicy.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPolicyInsurancePolicy.getId().toString())
            )
            .body(indexPolicyInsurancePolicy);
    }

    /**
     * {@code PUT  /index-policy-insurance-policies/:id} : Updates an existing indexPolicyInsurancePolicy.
     *
     * @param id the id of the indexPolicyInsurancePolicy to save.
     * @param indexPolicyInsurancePolicy the indexPolicyInsurancePolicy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyInsurancePolicy,
     * or with status {@code 400 (Bad Request)} if the indexPolicyInsurancePolicy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyInsurancePolicy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPolicyInsurancePolicy> updateIndexPolicyInsurancePolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyInsurancePolicy indexPolicyInsurancePolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPolicyInsurancePolicy : {}, {}", id, indexPolicyInsurancePolicy);
        if (indexPolicyInsurancePolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyInsurancePolicy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyInsurancePolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPolicyInsurancePolicy = indexPolicyInsurancePolicyService.update(indexPolicyInsurancePolicy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyInsurancePolicy.getId().toString()))
            .body(indexPolicyInsurancePolicy);
    }

    /**
     * {@code PATCH  /index-policy-insurance-policies/:id} : Partial updates given fields of an existing indexPolicyInsurancePolicy, field will ignore if it is null
     *
     * @param id the id of the indexPolicyInsurancePolicy to save.
     * @param indexPolicyInsurancePolicy the indexPolicyInsurancePolicy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyInsurancePolicy,
     * or with status {@code 400 (Bad Request)} if the indexPolicyInsurancePolicy is not valid,
     * or with status {@code 404 (Not Found)} if the indexPolicyInsurancePolicy is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyInsurancePolicy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPolicyInsurancePolicy> partialUpdateIndexPolicyInsurancePolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyInsurancePolicy indexPolicyInsurancePolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPolicyInsurancePolicy partially : {}, {}", id, indexPolicyInsurancePolicy);
        if (indexPolicyInsurancePolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyInsurancePolicy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyInsurancePolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPolicyInsurancePolicy> result = indexPolicyInsurancePolicyService.partialUpdate(indexPolicyInsurancePolicy);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyInsurancePolicy.getId().toString())
        );
    }

    /**
     * {@code GET  /index-policy-insurance-policies} : get all the indexPolicyInsurancePolicies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPolicyInsurancePolicies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPolicyInsurancePolicy>> getAllIndexPolicyInsurancePolicies(
        IndexPolicyInsurancePolicyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPolicyInsurancePolicies by criteria: {}", criteria);

        Page<IndexPolicyInsurancePolicy> page = indexPolicyInsurancePolicyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-policy-insurance-policies/count} : count all the indexPolicyInsurancePolicies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPolicyInsurancePolicies(IndexPolicyInsurancePolicyCriteria criteria) {
        LOG.debug("REST request to count IndexPolicyInsurancePolicies by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPolicyInsurancePolicyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-policy-insurance-policies/:id} : get the "id" indexPolicyInsurancePolicy.
     *
     * @param id the id of the indexPolicyInsurancePolicy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPolicyInsurancePolicy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPolicyInsurancePolicy> getIndexPolicyInsurancePolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPolicyInsurancePolicy : {}", id);
        Optional<IndexPolicyInsurancePolicy> indexPolicyInsurancePolicy = indexPolicyInsurancePolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPolicyInsurancePolicy);
    }

    /**
     * {@code DELETE  /index-policy-insurance-policies/:id} : delete the "id" indexPolicyInsurancePolicy.
     *
     * @param id the id of the indexPolicyInsurancePolicy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPolicyInsurancePolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPolicyInsurancePolicy : {}", id);
        indexPolicyInsurancePolicyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
