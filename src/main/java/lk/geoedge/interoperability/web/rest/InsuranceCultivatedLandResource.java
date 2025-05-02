package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandRepository;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandQueryService;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandService;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLand}.
 */
@RestController
@RequestMapping("/api/insurance-cultivated-lands")
public class InsuranceCultivatedLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCultivatedLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCultivatedLandService insuranceCultivatedLandService;

    private final InsuranceCultivatedLandRepository insuranceCultivatedLandRepository;

    private final InsuranceCultivatedLandQueryService insuranceCultivatedLandQueryService;

    public InsuranceCultivatedLandResource(
        InsuranceCultivatedLandService insuranceCultivatedLandService,
        InsuranceCultivatedLandRepository insuranceCultivatedLandRepository,
        InsuranceCultivatedLandQueryService insuranceCultivatedLandQueryService
    ) {
        this.insuranceCultivatedLandService = insuranceCultivatedLandService;
        this.insuranceCultivatedLandRepository = insuranceCultivatedLandRepository;
        this.insuranceCultivatedLandQueryService = insuranceCultivatedLandQueryService;
    }

    /**
     * {@code POST  /insurance-cultivated-lands} : Create a new insuranceCultivatedLand.
     *
     * @param insuranceCultivatedLand the insuranceCultivatedLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCultivatedLand, or with status {@code 400 (Bad Request)} if the insuranceCultivatedLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCultivatedLand> createInsuranceCultivatedLand(
        @RequestBody InsuranceCultivatedLand insuranceCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCultivatedLand : {}", insuranceCultivatedLand);
        if (insuranceCultivatedLand.getId() != null) {
            throw new BadRequestAlertException("A new insuranceCultivatedLand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceCultivatedLand = insuranceCultivatedLandService.save(insuranceCultivatedLand);
        return ResponseEntity.created(new URI("/api/insurance-cultivated-lands/" + insuranceCultivatedLand.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLand.getId().toString()))
            .body(insuranceCultivatedLand);
    }

    /**
     * {@code PUT  /insurance-cultivated-lands/:id} : Updates an existing insuranceCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLand to save.
     * @param insuranceCultivatedLand the insuranceCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLand> updateInsuranceCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLand insuranceCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCultivatedLand : {}, {}", id, insuranceCultivatedLand);
        if (insuranceCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCultivatedLand = insuranceCultivatedLandService.update(insuranceCultivatedLand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLand.getId().toString()))
            .body(insuranceCultivatedLand);
    }

    /**
     * {@code PATCH  /insurance-cultivated-lands/:id} : Partial updates given fields of an existing insuranceCultivatedLand, field will ignore if it is null
     *
     * @param id the id of the insuranceCultivatedLand to save.
     * @param insuranceCultivatedLand the insuranceCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLand is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCultivatedLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCultivatedLand> partialUpdateInsuranceCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLand insuranceCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuranceCultivatedLand partially : {}, {}", id, insuranceCultivatedLand);
        if (insuranceCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCultivatedLand> result = insuranceCultivatedLandService.partialUpdate(insuranceCultivatedLand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLand.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-cultivated-lands} : get all the insuranceCultivatedLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCultivatedLands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCultivatedLand>> getAllInsuranceCultivatedLands(
        InsuranceCultivatedLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCultivatedLands by criteria: {}", criteria);

        Page<InsuranceCultivatedLand> page = insuranceCultivatedLandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-cultivated-lands/count} : count all the insuranceCultivatedLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCultivatedLands(InsuranceCultivatedLandCriteria criteria) {
        LOG.debug("REST request to count InsuranceCultivatedLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCultivatedLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-cultivated-lands/:id} : get the "id" insuranceCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCultivatedLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLand> getInsuranceCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCultivatedLand : {}", id);
        Optional<InsuranceCultivatedLand> insuranceCultivatedLand = insuranceCultivatedLandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCultivatedLand);
    }

    /**
     * {@code DELETE  /insurance-cultivated-lands/:id} : delete the "id" insuranceCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCultivatedLand : {}", id);
        insuranceCultivatedLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
