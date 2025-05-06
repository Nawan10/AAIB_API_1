package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsurancePolicyDamage;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageRepository;
import lk.geoedge.interoperability.service.InsurancePolicyDamageQueryService;
import lk.geoedge.interoperability.service.InsurancePolicyDamageService;
import lk.geoedge.interoperability.service.criteria.InsurancePolicyDamageCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsurancePolicyDamage}.
 */
@RestController
@RequestMapping("/api/insurance-policy-damages")
public class InsurancePolicyDamageResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyDamageResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsurancePolicyDamage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsurancePolicyDamageService insurancePolicyDamageService;

    private final InsurancePolicyDamageRepository insurancePolicyDamageRepository;

    private final InsurancePolicyDamageQueryService insurancePolicyDamageQueryService;

    public InsurancePolicyDamageResource(
        InsurancePolicyDamageService insurancePolicyDamageService,
        InsurancePolicyDamageRepository insurancePolicyDamageRepository,
        InsurancePolicyDamageQueryService insurancePolicyDamageQueryService
    ) {
        this.insurancePolicyDamageService = insurancePolicyDamageService;
        this.insurancePolicyDamageRepository = insurancePolicyDamageRepository;
        this.insurancePolicyDamageQueryService = insurancePolicyDamageQueryService;
    }

    /**
     * {@code POST  /insurance-policy-damages} : Create a new insurancePolicyDamage.
     *
     * @param insurancePolicyDamage the insurancePolicyDamage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insurancePolicyDamage, or with status {@code 400 (Bad Request)} if the insurancePolicyDamage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsurancePolicyDamage> createInsurancePolicyDamage(@RequestBody InsurancePolicyDamage insurancePolicyDamage)
        throws URISyntaxException {
        LOG.debug("REST request to save InsurancePolicyDamage : {}", insurancePolicyDamage);
        if (insurancePolicyDamage.getId() != null) {
            throw new BadRequestAlertException("A new insurancePolicyDamage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insurancePolicyDamage = insurancePolicyDamageService.save(insurancePolicyDamage);
        return ResponseEntity.created(new URI("/api/insurance-policy-damages/" + insurancePolicyDamage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insurancePolicyDamage.getId().toString()))
            .body(insurancePolicyDamage);
    }

    /**
     * {@code PUT  /insurance-policy-damages/:id} : Updates an existing insurancePolicyDamage.
     *
     * @param id the id of the insurancePolicyDamage to save.
     * @param insurancePolicyDamage the insurancePolicyDamage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePolicyDamage,
     * or with status {@code 400 (Bad Request)} if the insurancePolicyDamage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insurancePolicyDamage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyDamage> updateInsurancePolicyDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancePolicyDamage insurancePolicyDamage
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsurancePolicyDamage : {}, {}", id, insurancePolicyDamage);
        if (insurancePolicyDamage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePolicyDamage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePolicyDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insurancePolicyDamage = insurancePolicyDamageService.update(insurancePolicyDamage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insurancePolicyDamage.getId().toString()))
            .body(insurancePolicyDamage);
    }

    /**
     * {@code PATCH  /insurance-policy-damages/:id} : Partial updates given fields of an existing insurancePolicyDamage, field will ignore if it is null
     *
     * @param id the id of the insurancePolicyDamage to save.
     * @param insurancePolicyDamage the insurancePolicyDamage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePolicyDamage,
     * or with status {@code 400 (Bad Request)} if the insurancePolicyDamage is not valid,
     * or with status {@code 404 (Not Found)} if the insurancePolicyDamage is not found,
     * or with status {@code 500 (Internal Server Error)} if the insurancePolicyDamage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsurancePolicyDamage> partialUpdateInsurancePolicyDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancePolicyDamage insurancePolicyDamage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsurancePolicyDamage partially : {}, {}", id, insurancePolicyDamage);
        if (insurancePolicyDamage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePolicyDamage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePolicyDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsurancePolicyDamage> result = insurancePolicyDamageService.partialUpdate(insurancePolicyDamage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insurancePolicyDamage.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-policy-damages} : get all the insurancePolicyDamages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insurancePolicyDamages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsurancePolicyDamage>> getAllInsurancePolicyDamages(
        InsurancePolicyDamageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsurancePolicyDamages by criteria: {}", criteria);

        Page<InsurancePolicyDamage> page = insurancePolicyDamageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-policy-damages/count} : count all the insurancePolicyDamages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsurancePolicyDamages(InsurancePolicyDamageCriteria criteria) {
        LOG.debug("REST request to count InsurancePolicyDamages by criteria: {}", criteria);
        return ResponseEntity.ok().body(insurancePolicyDamageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-policy-damages/:id} : get the "id" insurancePolicyDamage.
     *
     * @param id the id of the insurancePolicyDamage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insurancePolicyDamage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyDamage> getInsurancePolicyDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsurancePolicyDamage : {}", id);
        Optional<InsurancePolicyDamage> insurancePolicyDamage = insurancePolicyDamageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insurancePolicyDamage);
    }

    /**
     * {@code DELETE  /insurance-policy-damages/:id} : delete the "id" insurancePolicyDamage.
     *
     * @param id the id of the insurancePolicyDamage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurancePolicyDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsurancePolicyDamage : {}", id);
        insurancePolicyDamageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
