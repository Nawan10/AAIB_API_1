package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandService;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand}.
 */
@RestController
@RequestMapping("/api/insurance-cultivated-land-coverages-insurance-cultivated-lands")
public class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCoveragesInsuranceCultivatedLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCultivatedLandCoveragesInsuranceCultivatedLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCultivatedLandCoveragesInsuranceCultivatedLandService insuranceCultivatedLandCoveragesInsuranceCultivatedLandService;

    private final InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;

    private final InsuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService insuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService;

    public InsuranceCultivatedLandCoveragesInsuranceCultivatedLandResource(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandService insuranceCultivatedLandCoveragesInsuranceCultivatedLandService,
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository,
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService insuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService
    ) {
        this.insuranceCultivatedLandCoveragesInsuranceCultivatedLandService =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandService;
        this.insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
        this.insuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService;
    }

    /**
     * {@code POST  /insurance-cultivated-land-coverages-insurance-cultivated-lands} : Create a new insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param insuranceCultivatedLandCoveragesInsuranceCultivatedLand the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCultivatedLandCoveragesInsuranceCultivatedLand, or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCoveragesInsuranceCultivatedLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand
    > createInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
        @RequestBody InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to save InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}",
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId() != null) {
            throw new BadRequestAlertException(
                "A new insuranceCultivatedLandCoveragesInsuranceCultivatedLand cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        insuranceCultivatedLandCoveragesInsuranceCultivatedLand = insuranceCultivatedLandCoveragesInsuranceCultivatedLandService.save(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        return ResponseEntity.created(
            new URI(
                "/api/insurance-cultivated-land-coverages-insurance-cultivated-lands/" +
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId()
            )
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId().toString()
                )
            )
            .body(insuranceCultivatedLandCoveragesInsuranceCultivatedLand);
    }

    /**
     * {@code PUT  /insurance-cultivated-land-coverages-insurance-cultivated-lands/:id} : Updates an existing insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to save.
     * @param insuranceCultivatedLandCoveragesInsuranceCultivatedLand the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCoveragesInsuranceCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCoveragesInsuranceCultivatedLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCoveragesInsuranceCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand
    > updateInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to update InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}, {}",
            id,
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCultivatedLandCoveragesInsuranceCultivatedLand = insuranceCultivatedLandCoveragesInsuranceCultivatedLandService.update(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId().toString()
                )
            )
            .body(insuranceCultivatedLandCoveragesInsuranceCultivatedLand);
    }

    /**
     * {@code PATCH  /insurance-cultivated-land-coverages-insurance-cultivated-lands/:id} : Partial updates given fields of an existing insuranceCultivatedLandCoveragesInsuranceCultivatedLand, field will ignore if it is null
     *
     * @param id the id of the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to save.
     * @param insuranceCultivatedLandCoveragesInsuranceCultivatedLand the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCoveragesInsuranceCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCoveragesInsuranceCultivatedLand is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCultivatedLandCoveragesInsuranceCultivatedLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCoveragesInsuranceCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand
    > partialUpdateInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update InsuranceCultivatedLandCoveragesInsuranceCultivatedLand partially : {}, {}",
            id,
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> result =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandService.partialUpdate(
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                false,
                ENTITY_NAME,
                insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /insurance-cultivated-land-coverages-insurance-cultivated-lands} : get all the insuranceCultivatedLandCoveragesInsuranceCultivatedLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCultivatedLandCoveragesInsuranceCultivatedLands in body.
     */
    @GetMapping("")
    public ResponseEntity<
        List<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand>
    > getAllInsuranceCultivatedLandCoveragesInsuranceCultivatedLands(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCultivatedLandCoveragesInsuranceCultivatedLands by criteria: {}", criteria);

        Page<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> page =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-cultivated-land-coverages-insurance-cultivated-lands/count} : count all the insuranceCultivatedLandCoveragesInsuranceCultivatedLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCultivatedLandCoveragesInsuranceCultivatedLands(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandCriteria criteria
    ) {
        LOG.debug("REST request to count InsuranceCultivatedLandCoveragesInsuranceCultivatedLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCultivatedLandCoveragesInsuranceCultivatedLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-cultivated-land-coverages-insurance-cultivated-lands/:id} : get the "id" insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCultivatedLandCoveragesInsuranceCultivatedLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand
    > getInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}", id);
        Optional<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> insuranceCultivatedLandCoveragesInsuranceCultivatedLand =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCultivatedLandCoveragesInsuranceCultivatedLand);
    }

    /**
     * {@code DELETE  /insurance-cultivated-land-coverages-insurance-cultivated-lands/:id} : delete the "id" insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLandCoveragesInsuranceCultivatedLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCultivatedLandCoveragesInsuranceCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}", id);
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
