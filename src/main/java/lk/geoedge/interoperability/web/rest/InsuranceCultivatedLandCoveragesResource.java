package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesRepository;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCoveragesQueryService;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCoveragesService;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCoveragesCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages}.
 */
@RestController
@RequestMapping("/api/insurance-cultivated-land-coverages")
public class InsuranceCultivatedLandCoveragesResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCoveragesResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCultivatedLandCoverages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCultivatedLandCoveragesService insuranceCultivatedLandCoveragesService;

    private final InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository;

    private final InsuranceCultivatedLandCoveragesQueryService insuranceCultivatedLandCoveragesQueryService;

    public InsuranceCultivatedLandCoveragesResource(
        InsuranceCultivatedLandCoveragesService insuranceCultivatedLandCoveragesService,
        InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository,
        InsuranceCultivatedLandCoveragesQueryService insuranceCultivatedLandCoveragesQueryService
    ) {
        this.insuranceCultivatedLandCoveragesService = insuranceCultivatedLandCoveragesService;
        this.insuranceCultivatedLandCoveragesRepository = insuranceCultivatedLandCoveragesRepository;
        this.insuranceCultivatedLandCoveragesQueryService = insuranceCultivatedLandCoveragesQueryService;
    }

    /**
     * {@code POST  /insurance-cultivated-land-coverages} : Create a new insuranceCultivatedLandCoverages.
     *
     * @param insuranceCultivatedLandCoverages the insuranceCultivatedLandCoverages to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCultivatedLandCoverages, or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCoverages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCultivatedLandCoverages> createInsuranceCultivatedLandCoverages(
        @RequestBody InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages
    ) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCultivatedLandCoverages : {}", insuranceCultivatedLandCoverages);
        if (insuranceCultivatedLandCoverages.getId() != null) {
            throw new BadRequestAlertException("A new insuranceCultivatedLandCoverages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesService.save(insuranceCultivatedLandCoverages);
        return ResponseEntity.created(new URI("/api/insurance-cultivated-land-coverages/" + insuranceCultivatedLandCoverages.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insuranceCultivatedLandCoverages.getId().toString()
                )
            )
            .body(insuranceCultivatedLandCoverages);
    }

    /**
     * {@code PUT  /insurance-cultivated-land-coverages/:id} : Updates an existing insuranceCultivatedLandCoverages.
     *
     * @param id the id of the insuranceCultivatedLandCoverages to save.
     * @param insuranceCultivatedLandCoverages the insuranceCultivatedLandCoverages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCoverages,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCoverages is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCoverages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandCoverages> updateInsuranceCultivatedLandCoverages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCultivatedLandCoverages : {}, {}", id, insuranceCultivatedLandCoverages);
        if (insuranceCultivatedLandCoverages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCoverages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCoveragesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesService.update(insuranceCultivatedLandCoverages);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandCoverages.getId().toString())
            )
            .body(insuranceCultivatedLandCoverages);
    }

    /**
     * {@code PATCH  /insurance-cultivated-land-coverages/:id} : Partial updates given fields of an existing insuranceCultivatedLandCoverages, field will ignore if it is null
     *
     * @param id the id of the insuranceCultivatedLandCoverages to save.
     * @param insuranceCultivatedLandCoverages the insuranceCultivatedLandCoverages to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCoverages,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCoverages is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCultivatedLandCoverages is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCoverages couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCultivatedLandCoverages> partialUpdateInsuranceCultivatedLandCoverages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update InsuranceCultivatedLandCoverages partially : {}, {}",
            id,
            insuranceCultivatedLandCoverages
        );
        if (insuranceCultivatedLandCoverages.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCoverages.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCoveragesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCultivatedLandCoverages> result = insuranceCultivatedLandCoveragesService.partialUpdate(
            insuranceCultivatedLandCoverages
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandCoverages.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-cultivated-land-coverages} : get all the insuranceCultivatedLandCoverages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCultivatedLandCoverages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCultivatedLandCoverages>> getAllInsuranceCultivatedLandCoverages(
        InsuranceCultivatedLandCoveragesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCultivatedLandCoverages by criteria: {}", criteria);

        Page<InsuranceCultivatedLandCoverages> page = insuranceCultivatedLandCoveragesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-cultivated-land-coverages/count} : count all the insuranceCultivatedLandCoverages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCultivatedLandCoverages(InsuranceCultivatedLandCoveragesCriteria criteria) {
        LOG.debug("REST request to count InsuranceCultivatedLandCoverages by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCultivatedLandCoveragesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-cultivated-land-coverages/:id} : get the "id" insuranceCultivatedLandCoverages.
     *
     * @param id the id of the insuranceCultivatedLandCoverages to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCultivatedLandCoverages, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandCoverages> getInsuranceCultivatedLandCoverages(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCultivatedLandCoverages : {}", id);
        Optional<InsuranceCultivatedLandCoverages> insuranceCultivatedLandCoverages = insuranceCultivatedLandCoveragesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCultivatedLandCoverages);
    }

    /**
     * {@code DELETE  /insurance-cultivated-land-coverages/:id} : delete the "id" insuranceCultivatedLandCoverages.
     *
     * @param id the id of the insuranceCultivatedLandCoverages to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCultivatedLandCoverages(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCultivatedLandCoverages : {}", id);
        insuranceCultivatedLandCoveragesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
