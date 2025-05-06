package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsurancePolicy;
import lk.geoedge.interoperability.repository.InsurancePolicyRepository;
import lk.geoedge.interoperability.service.InsurancePolicyQueryService;
import lk.geoedge.interoperability.service.InsurancePolicyService;
import lk.geoedge.interoperability.service.criteria.InsurancePolicyCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsurancePolicy}.
 */
@RestController
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsurancePolicy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsurancePolicyService insurancePolicyService;

    private final InsurancePolicyRepository insurancePolicyRepository;

    private final InsurancePolicyQueryService insurancePolicyQueryService;

    public InsurancePolicyResource(
        InsurancePolicyService insurancePolicyService,
        InsurancePolicyRepository insurancePolicyRepository,
        InsurancePolicyQueryService insurancePolicyQueryService
    ) {
        this.insurancePolicyService = insurancePolicyService;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.insurancePolicyQueryService = insurancePolicyQueryService;
    }

    /**
     * {@code POST  /insurance-policies} : Create a new insurancePolicy.
     *
     * @param insurancePolicy the insurancePolicy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insurancePolicy, or with status {@code 400 (Bad Request)} if the insurancePolicy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsurancePolicy> createInsurancePolicy(@RequestBody InsurancePolicy insurancePolicy) throws URISyntaxException {
        LOG.debug("REST request to save InsurancePolicy : {}", insurancePolicy);
        if (insurancePolicy.getId() != null) {
            throw new BadRequestAlertException("A new insurancePolicy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insurancePolicy = insurancePolicyService.save(insurancePolicy);
        return ResponseEntity.created(new URI("/api/insurance-policies/" + insurancePolicy.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insurancePolicy.getId().toString()))
            .body(insurancePolicy);
    }

    /**
     * {@code PUT  /insurance-policies/:id} : Updates an existing insurancePolicy.
     *
     * @param id the id of the insurancePolicy to save.
     * @param insurancePolicy the insurancePolicy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePolicy,
     * or with status {@code 400 (Bad Request)} if the insurancePolicy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insurancePolicy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicy> updateInsurancePolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancePolicy insurancePolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsurancePolicy : {}, {}", id, insurancePolicy);
        if (insurancePolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePolicy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insurancePolicy = insurancePolicyService.update(insurancePolicy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insurancePolicy.getId().toString()))
            .body(insurancePolicy);
    }

    /**
     * {@code PATCH  /insurance-policies/:id} : Partial updates given fields of an existing insurancePolicy, field will ignore if it is null
     *
     * @param id the id of the insurancePolicy to save.
     * @param insurancePolicy the insurancePolicy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePolicy,
     * or with status {@code 400 (Bad Request)} if the insurancePolicy is not valid,
     * or with status {@code 404 (Not Found)} if the insurancePolicy is not found,
     * or with status {@code 500 (Internal Server Error)} if the insurancePolicy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsurancePolicy> partialUpdateInsurancePolicy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancePolicy insurancePolicy
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsurancePolicy partially : {}, {}", id, insurancePolicy);
        if (insurancePolicy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePolicy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePolicyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsurancePolicy> result = insurancePolicyService.partialUpdate(insurancePolicy);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insurancePolicy.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-policies} : get all the insurancePolicies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insurancePolicies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsurancePolicy>> getAllInsurancePolicies(
        InsurancePolicyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsurancePolicies by criteria: {}", criteria);

        Page<InsurancePolicy> page = insurancePolicyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-policies/count} : count all the insurancePolicies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsurancePolicies(InsurancePolicyCriteria criteria) {
        LOG.debug("REST request to count InsurancePolicies by criteria: {}", criteria);
        return ResponseEntity.ok().body(insurancePolicyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-policies/:id} : get the "id" insurancePolicy.
     *
     * @param id the id of the insurancePolicy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insurancePolicy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicy> getInsurancePolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsurancePolicy : {}", id);
        Optional<InsurancePolicy> insurancePolicy = insurancePolicyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insurancePolicy);
    }

    /**
     * {@code DELETE  /insurance-policies/:id} : delete the "id" insurancePolicy.
     *
     * @param id the id of the insurancePolicy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurancePolicy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsurancePolicy : {}", id);
        insurancePolicyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
