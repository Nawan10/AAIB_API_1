package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReport;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportRepository;
import lk.geoedge.interoperability.service.CultivatedLandDamageReportQueryService;
import lk.geoedge.interoperability.service.CultivatedLandDamageReportService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReportCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReport}.
 */
@RestController
@RequestMapping("/api/cultivated-land-damage-reports")
public class CultivatedLandDamageReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandDamageReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandDamageReportService cultivatedLandDamageReportService;

    private final CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository;

    private final CultivatedLandDamageReportQueryService cultivatedLandDamageReportQueryService;

    public CultivatedLandDamageReportResource(
        CultivatedLandDamageReportService cultivatedLandDamageReportService,
        CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository,
        CultivatedLandDamageReportQueryService cultivatedLandDamageReportQueryService
    ) {
        this.cultivatedLandDamageReportService = cultivatedLandDamageReportService;
        this.cultivatedLandDamageReportRepository = cultivatedLandDamageReportRepository;
        this.cultivatedLandDamageReportQueryService = cultivatedLandDamageReportQueryService;
    }

    /**
     * {@code POST  /cultivated-land-damage-reports} : Create a new cultivatedLandDamageReport.
     *
     * @param cultivatedLandDamageReport the cultivatedLandDamageReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandDamageReport, or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandDamageReport> createCultivatedLandDamageReport(
        @RequestBody CultivatedLandDamageReport cultivatedLandDamageReport
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandDamageReport : {}", cultivatedLandDamageReport);
        if (cultivatedLandDamageReport.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandDamageReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandDamageReport = cultivatedLandDamageReportService.save(cultivatedLandDamageReport);
        return ResponseEntity.created(new URI("/api/cultivated-land-damage-reports/" + cultivatedLandDamageReport.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReport.getId().toString())
            )
            .body(cultivatedLandDamageReport);
    }

    /**
     * {@code PUT  /cultivated-land-damage-reports/:id} : Updates an existing cultivatedLandDamageReport.
     *
     * @param id the id of the cultivatedLandDamageReport to save.
     * @param cultivatedLandDamageReport the cultivatedLandDamageReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReport,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReport> updateCultivatedLandDamageReport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReport cultivatedLandDamageReport
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandDamageReport : {}, {}", id, cultivatedLandDamageReport);
        if (cultivatedLandDamageReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandDamageReport = cultivatedLandDamageReportService.update(cultivatedLandDamageReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReport.getId().toString()))
            .body(cultivatedLandDamageReport);
    }

    /**
     * {@code PATCH  /cultivated-land-damage-reports/:id} : Partial updates given fields of an existing cultivatedLandDamageReport, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandDamageReport to save.
     * @param cultivatedLandDamageReport the cultivatedLandDamageReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReport,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReport is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandDamageReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandDamageReport> partialUpdateCultivatedLandDamageReport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReport cultivatedLandDamageReport
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandDamageReport partially : {}, {}", id, cultivatedLandDamageReport);
        if (cultivatedLandDamageReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandDamageReport> result = cultivatedLandDamageReportService.partialUpdate(cultivatedLandDamageReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReport.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-damage-reports} : get all the cultivatedLandDamageReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandDamageReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandDamageReport>> getAllCultivatedLandDamageReports(
        CultivatedLandDamageReportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReports by criteria: {}", criteria);

        Page<CultivatedLandDamageReport> page = cultivatedLandDamageReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-damage-reports/count} : count all the cultivatedLandDamageReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandDamageReports(CultivatedLandDamageReportCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandDamageReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandDamageReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-damage-reports/:id} : get the "id" cultivatedLandDamageReport.
     *
     * @param id the id of the cultivatedLandDamageReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandDamageReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReport> getCultivatedLandDamageReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandDamageReport : {}", id);
        Optional<CultivatedLandDamageReport> cultivatedLandDamageReport = cultivatedLandDamageReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandDamageReport);
    }

    /**
     * {@code DELETE  /cultivated-land-damage-reports/:id} : delete the "id" cultivatedLandDamageReport.
     *
     * @param id the id of the cultivatedLandDamageReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandDamageReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandDamageReport : {}", id);
        cultivatedLandDamageReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
