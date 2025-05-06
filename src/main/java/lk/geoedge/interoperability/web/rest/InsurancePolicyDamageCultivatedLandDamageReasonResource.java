package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageCultivatedLandDamageReasonRepository;
import lk.geoedge.interoperability.service.InsurancePolicyDamageCultivatedLandDamageReasonQueryService;
import lk.geoedge.interoperability.service.InsurancePolicyDamageCultivatedLandDamageReasonService;
import lk.geoedge.interoperability.service.criteria.InsurancePolicyDamageCultivatedLandDamageReasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason}.
 */
@RestController
@RequestMapping("/api/insurance-policy-damage-cultivated-land-damage-reasons")
public class InsurancePolicyDamageCultivatedLandDamageReasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyDamageCultivatedLandDamageReasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsurancePolicyDamageCultivatedLandDamageReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsurancePolicyDamageCultivatedLandDamageReasonService insurancePolicyDamageCultivatedLandDamageReasonService;

    private final InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository;

    private final InsurancePolicyDamageCultivatedLandDamageReasonQueryService insurancePolicyDamageCultivatedLandDamageReasonQueryService;

    public InsurancePolicyDamageCultivatedLandDamageReasonResource(
        InsurancePolicyDamageCultivatedLandDamageReasonService insurancePolicyDamageCultivatedLandDamageReasonService,
        InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository,
        InsurancePolicyDamageCultivatedLandDamageReasonQueryService insurancePolicyDamageCultivatedLandDamageReasonQueryService
    ) {
        this.insurancePolicyDamageCultivatedLandDamageReasonService = insurancePolicyDamageCultivatedLandDamageReasonService;
        this.insurancePolicyDamageCultivatedLandDamageReasonRepository = insurancePolicyDamageCultivatedLandDamageReasonRepository;
        this.insurancePolicyDamageCultivatedLandDamageReasonQueryService = insurancePolicyDamageCultivatedLandDamageReasonQueryService;
    }

    /**
     * {@code POST  /insurance-policy-damage-cultivated-land-damage-reasons} : Create a new insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param insurancePolicyDamageCultivatedLandDamageReason the insurancePolicyDamageCultivatedLandDamageReason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insurancePolicyDamageCultivatedLandDamageReason, or with status {@code 400 (Bad Request)} if the insurancePolicyDamageCultivatedLandDamageReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsurancePolicyDamageCultivatedLandDamageReason> createInsurancePolicyDamageCultivatedLandDamageReason(
        @RequestBody InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to save InsurancePolicyDamageCultivatedLandDamageReason : {}",
            insurancePolicyDamageCultivatedLandDamageReason
        );
        if (insurancePolicyDamageCultivatedLandDamageReason.getId() != null) {
            throw new BadRequestAlertException(
                "A new insurancePolicyDamageCultivatedLandDamageReason cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        insurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonService.save(
            insurancePolicyDamageCultivatedLandDamageReason
        );
        return ResponseEntity.created(
            new URI(
                "/api/insurance-policy-damage-cultivated-land-damage-reasons/" + insurancePolicyDamageCultivatedLandDamageReason.getId()
            )
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insurancePolicyDamageCultivatedLandDamageReason.getId().toString()
                )
            )
            .body(insurancePolicyDamageCultivatedLandDamageReason);
    }

    /**
     * {@code PUT  /insurance-policy-damage-cultivated-land-damage-reasons/:id} : Updates an existing insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param id the id of the insurancePolicyDamageCultivatedLandDamageReason to save.
     * @param insurancePolicyDamageCultivatedLandDamageReason the insurancePolicyDamageCultivatedLandDamageReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePolicyDamageCultivatedLandDamageReason,
     * or with status {@code 400 (Bad Request)} if the insurancePolicyDamageCultivatedLandDamageReason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insurancePolicyDamageCultivatedLandDamageReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsurancePolicyDamageCultivatedLandDamageReason> updateInsurancePolicyDamageCultivatedLandDamageReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to update InsurancePolicyDamageCultivatedLandDamageReason : {}, {}",
            id,
            insurancePolicyDamageCultivatedLandDamageReason
        );
        if (insurancePolicyDamageCultivatedLandDamageReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePolicyDamageCultivatedLandDamageReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePolicyDamageCultivatedLandDamageReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insurancePolicyDamageCultivatedLandDamageReason = insurancePolicyDamageCultivatedLandDamageReasonService.update(
            insurancePolicyDamageCultivatedLandDamageReason
        );
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insurancePolicyDamageCultivatedLandDamageReason.getId().toString()
                )
            )
            .body(insurancePolicyDamageCultivatedLandDamageReason);
    }

    /**
     * {@code PATCH  /insurance-policy-damage-cultivated-land-damage-reasons/:id} : Partial updates given fields of an existing insurancePolicyDamageCultivatedLandDamageReason, field will ignore if it is null
     *
     * @param id the id of the insurancePolicyDamageCultivatedLandDamageReason to save.
     * @param insurancePolicyDamageCultivatedLandDamageReason the insurancePolicyDamageCultivatedLandDamageReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePolicyDamageCultivatedLandDamageReason,
     * or with status {@code 400 (Bad Request)} if the insurancePolicyDamageCultivatedLandDamageReason is not valid,
     * or with status {@code 404 (Not Found)} if the insurancePolicyDamageCultivatedLandDamageReason is not found,
     * or with status {@code 500 (Internal Server Error)} if the insurancePolicyDamageCultivatedLandDamageReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsurancePolicyDamageCultivatedLandDamageReason> partialUpdateInsurancePolicyDamageCultivatedLandDamageReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update InsurancePolicyDamageCultivatedLandDamageReason partially : {}, {}",
            id,
            insurancePolicyDamageCultivatedLandDamageReason
        );
        if (insurancePolicyDamageCultivatedLandDamageReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePolicyDamageCultivatedLandDamageReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePolicyDamageCultivatedLandDamageReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsurancePolicyDamageCultivatedLandDamageReason> result =
            insurancePolicyDamageCultivatedLandDamageReasonService.partialUpdate(insurancePolicyDamageCultivatedLandDamageReason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                false,
                ENTITY_NAME,
                insurancePolicyDamageCultivatedLandDamageReason.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /insurance-policy-damage-cultivated-land-damage-reasons} : get all the insurancePolicyDamageCultivatedLandDamageReasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insurancePolicyDamageCultivatedLandDamageReasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsurancePolicyDamageCultivatedLandDamageReason>> getAllInsurancePolicyDamageCultivatedLandDamageReasons(
        InsurancePolicyDamageCultivatedLandDamageReasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsurancePolicyDamageCultivatedLandDamageReasons by criteria: {}", criteria);

        Page<InsurancePolicyDamageCultivatedLandDamageReason> page =
            insurancePolicyDamageCultivatedLandDamageReasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-policy-damage-cultivated-land-damage-reasons/count} : count all the insurancePolicyDamageCultivatedLandDamageReasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsurancePolicyDamageCultivatedLandDamageReasons(
        InsurancePolicyDamageCultivatedLandDamageReasonCriteria criteria
    ) {
        LOG.debug("REST request to count InsurancePolicyDamageCultivatedLandDamageReasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(insurancePolicyDamageCultivatedLandDamageReasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-policy-damage-cultivated-land-damage-reasons/:id} : get the "id" insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param id the id of the insurancePolicyDamageCultivatedLandDamageReason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insurancePolicyDamageCultivatedLandDamageReason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePolicyDamageCultivatedLandDamageReason> getInsurancePolicyDamageCultivatedLandDamageReason(
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get InsurancePolicyDamageCultivatedLandDamageReason : {}", id);
        Optional<InsurancePolicyDamageCultivatedLandDamageReason> insurancePolicyDamageCultivatedLandDamageReason =
            insurancePolicyDamageCultivatedLandDamageReasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insurancePolicyDamageCultivatedLandDamageReason);
    }

    /**
     * {@code DELETE  /insurance-policy-damage-cultivated-land-damage-reasons/:id} : delete the "id" insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param id the id of the insurancePolicyDamageCultivatedLandDamageReason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurancePolicyDamageCultivatedLandDamageReason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsurancePolicyDamageCultivatedLandDamageReason : {}", id);
        insurancePolicyDamageCultivatedLandDamageReasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
