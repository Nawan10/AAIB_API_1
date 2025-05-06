package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCultivatedLandRepository;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCultivatedLandQueryService;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCultivatedLandService;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCultivatedLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand}.
 */
@RestController
@RequestMapping("/api/insurance-cultivated-land-cultivated-lands")
public class InsuranceCultivatedLandCultivatedLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCultivatedLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCultivatedLandCultivatedLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCultivatedLandCultivatedLandService insuranceCultivatedLandCultivatedLandService;

    private final InsuranceCultivatedLandCultivatedLandRepository insuranceCultivatedLandCultivatedLandRepository;

    private final InsuranceCultivatedLandCultivatedLandQueryService insuranceCultivatedLandCultivatedLandQueryService;

    public InsuranceCultivatedLandCultivatedLandResource(
        InsuranceCultivatedLandCultivatedLandService insuranceCultivatedLandCultivatedLandService,
        InsuranceCultivatedLandCultivatedLandRepository insuranceCultivatedLandCultivatedLandRepository,
        InsuranceCultivatedLandCultivatedLandQueryService insuranceCultivatedLandCultivatedLandQueryService
    ) {
        this.insuranceCultivatedLandCultivatedLandService = insuranceCultivatedLandCultivatedLandService;
        this.insuranceCultivatedLandCultivatedLandRepository = insuranceCultivatedLandCultivatedLandRepository;
        this.insuranceCultivatedLandCultivatedLandQueryService = insuranceCultivatedLandCultivatedLandQueryService;
    }

    /**
     * {@code POST  /insurance-cultivated-land-cultivated-lands} : Create a new insuranceCultivatedLandCultivatedLand.
     *
     * @param insuranceCultivatedLandCultivatedLand the insuranceCultivatedLandCultivatedLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCultivatedLandCultivatedLand, or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCultivatedLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCultivatedLandCultivatedLand> createInsuranceCultivatedLandCultivatedLand(
        @RequestBody InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCultivatedLandCultivatedLand : {}", insuranceCultivatedLandCultivatedLand);
        if (insuranceCultivatedLandCultivatedLand.getId() != null) {
            throw new BadRequestAlertException(
                "A new insuranceCultivatedLandCultivatedLand cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        insuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandService.save(insuranceCultivatedLandCultivatedLand);
        return ResponseEntity.created(
            new URI("/api/insurance-cultivated-land-cultivated-lands/" + insuranceCultivatedLandCultivatedLand.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insuranceCultivatedLandCultivatedLand.getId().toString()
                )
            )
            .body(insuranceCultivatedLandCultivatedLand);
    }

    /**
     * {@code PUT  /insurance-cultivated-land-cultivated-lands/:id} : Updates an existing insuranceCultivatedLandCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLandCultivatedLand to save.
     * @param insuranceCultivatedLandCultivatedLand the insuranceCultivatedLandCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCultivatedLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandCultivatedLand> updateInsuranceCultivatedLandCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCultivatedLandCultivatedLand : {}, {}", id, insuranceCultivatedLandCultivatedLand);
        if (insuranceCultivatedLandCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCultivatedLandCultivatedLand = insuranceCultivatedLandCultivatedLandService.update(insuranceCultivatedLandCultivatedLand);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insuranceCultivatedLandCultivatedLand.getId().toString()
                )
            )
            .body(insuranceCultivatedLandCultivatedLand);
    }

    /**
     * {@code PATCH  /insurance-cultivated-land-cultivated-lands/:id} : Partial updates given fields of an existing insuranceCultivatedLandCultivatedLand, field will ignore if it is null
     *
     * @param id the id of the insuranceCultivatedLandCultivatedLand to save.
     * @param insuranceCultivatedLandCultivatedLand the insuranceCultivatedLandCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCultivatedLand is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCultivatedLandCultivatedLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCultivatedLandCultivatedLand> partialUpdateInsuranceCultivatedLandCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update InsuranceCultivatedLandCultivatedLand partially : {}, {}",
            id,
            insuranceCultivatedLandCultivatedLand
        );
        if (insuranceCultivatedLandCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCultivatedLandCultivatedLand> result = insuranceCultivatedLandCultivatedLandService.partialUpdate(
            insuranceCultivatedLandCultivatedLand
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                false,
                ENTITY_NAME,
                insuranceCultivatedLandCultivatedLand.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /insurance-cultivated-land-cultivated-lands} : get all the insuranceCultivatedLandCultivatedLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCultivatedLandCultivatedLands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCultivatedLandCultivatedLand>> getAllInsuranceCultivatedLandCultivatedLands(
        InsuranceCultivatedLandCultivatedLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCultivatedLandCultivatedLands by criteria: {}", criteria);

        Page<InsuranceCultivatedLandCultivatedLand> page = insuranceCultivatedLandCultivatedLandQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-cultivated-land-cultivated-lands/count} : count all the insuranceCultivatedLandCultivatedLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCultivatedLandCultivatedLands(InsuranceCultivatedLandCultivatedLandCriteria criteria) {
        LOG.debug("REST request to count InsuranceCultivatedLandCultivatedLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCultivatedLandCultivatedLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-cultivated-land-cultivated-lands/:id} : get the "id" insuranceCultivatedLandCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLandCultivatedLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCultivatedLandCultivatedLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandCultivatedLand> getInsuranceCultivatedLandCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCultivatedLandCultivatedLand : {}", id);
        Optional<InsuranceCultivatedLandCultivatedLand> insuranceCultivatedLandCultivatedLand =
            insuranceCultivatedLandCultivatedLandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCultivatedLandCultivatedLand);
    }

    /**
     * {@code DELETE  /insurance-cultivated-land-cultivated-lands/:id} : delete the "id" insuranceCultivatedLandCultivatedLand.
     *
     * @param id the id of the insuranceCultivatedLandCultivatedLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCultivatedLandCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCultivatedLandCultivatedLand : {}", id);
        insuranceCultivatedLandCultivatedLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
