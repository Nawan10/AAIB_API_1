package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandFarmerRepository;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandFarmerQueryService;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandFarmerService;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandFarmerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer}.
 */
@RestController
@RequestMapping("/api/insurance-cultivated-land-farmers")
public class InsuranceCultivatedLandFarmerResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandFarmerResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCultivatedLandFarmer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCultivatedLandFarmerService insuranceCultivatedLandFarmerService;

    private final InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository;

    private final InsuranceCultivatedLandFarmerQueryService insuranceCultivatedLandFarmerQueryService;

    public InsuranceCultivatedLandFarmerResource(
        InsuranceCultivatedLandFarmerService insuranceCultivatedLandFarmerService,
        InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository,
        InsuranceCultivatedLandFarmerQueryService insuranceCultivatedLandFarmerQueryService
    ) {
        this.insuranceCultivatedLandFarmerService = insuranceCultivatedLandFarmerService;
        this.insuranceCultivatedLandFarmerRepository = insuranceCultivatedLandFarmerRepository;
        this.insuranceCultivatedLandFarmerQueryService = insuranceCultivatedLandFarmerQueryService;
    }

    /**
     * {@code POST  /insurance-cultivated-land-farmers} : Create a new insuranceCultivatedLandFarmer.
     *
     * @param insuranceCultivatedLandFarmer the insuranceCultivatedLandFarmer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCultivatedLandFarmer, or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandFarmer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCultivatedLandFarmer> createInsuranceCultivatedLandFarmer(
        @RequestBody InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCultivatedLandFarmer : {}", insuranceCultivatedLandFarmer);
        if (insuranceCultivatedLandFarmer.getId() != null) {
            throw new BadRequestAlertException("A new insuranceCultivatedLandFarmer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerService.save(insuranceCultivatedLandFarmer);
        return ResponseEntity.created(new URI("/api/insurance-cultivated-land-farmers/" + insuranceCultivatedLandFarmer.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandFarmer.getId().toString())
            )
            .body(insuranceCultivatedLandFarmer);
    }

    /**
     * {@code PUT  /insurance-cultivated-land-farmers/:id} : Updates an existing insuranceCultivatedLandFarmer.
     *
     * @param id the id of the insuranceCultivatedLandFarmer to save.
     * @param insuranceCultivatedLandFarmer the insuranceCultivatedLandFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandFarmer,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandFarmer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandFarmer> updateInsuranceCultivatedLandFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCultivatedLandFarmer : {}, {}", id, insuranceCultivatedLandFarmer);
        if (insuranceCultivatedLandFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerService.update(insuranceCultivatedLandFarmer);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandFarmer.getId().toString())
            )
            .body(insuranceCultivatedLandFarmer);
    }

    /**
     * {@code PATCH  /insurance-cultivated-land-farmers/:id} : Partial updates given fields of an existing insuranceCultivatedLandFarmer, field will ignore if it is null
     *
     * @param id the id of the insuranceCultivatedLandFarmer to save.
     * @param insuranceCultivatedLandFarmer the insuranceCultivatedLandFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandFarmer,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandFarmer is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCultivatedLandFarmer is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCultivatedLandFarmer> partialUpdateInsuranceCultivatedLandFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuranceCultivatedLandFarmer partially : {}, {}", id, insuranceCultivatedLandFarmer);
        if (insuranceCultivatedLandFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCultivatedLandFarmer> result = insuranceCultivatedLandFarmerService.partialUpdate(insuranceCultivatedLandFarmer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandFarmer.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-cultivated-land-farmers} : get all the insuranceCultivatedLandFarmers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCultivatedLandFarmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCultivatedLandFarmer>> getAllInsuranceCultivatedLandFarmers(
        InsuranceCultivatedLandFarmerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCultivatedLandFarmers by criteria: {}", criteria);

        Page<InsuranceCultivatedLandFarmer> page = insuranceCultivatedLandFarmerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-cultivated-land-farmers/count} : count all the insuranceCultivatedLandFarmers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCultivatedLandFarmers(InsuranceCultivatedLandFarmerCriteria criteria) {
        LOG.debug("REST request to count InsuranceCultivatedLandFarmers by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCultivatedLandFarmerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-cultivated-land-farmers/:id} : get the "id" insuranceCultivatedLandFarmer.
     *
     * @param id the id of the insuranceCultivatedLandFarmer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCultivatedLandFarmer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandFarmer> getInsuranceCultivatedLandFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCultivatedLandFarmer : {}", id);
        Optional<InsuranceCultivatedLandFarmer> insuranceCultivatedLandFarmer = insuranceCultivatedLandFarmerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCultivatedLandFarmer);
    }

    /**
     * {@code DELETE  /insurance-cultivated-land-farmers/:id} : delete the "id" insuranceCultivatedLandFarmer.
     *
     * @param id the id of the insuranceCultivatedLandFarmer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCultivatedLandFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCultivatedLandFarmer : {}", id);
        insuranceCultivatedLandFarmerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
