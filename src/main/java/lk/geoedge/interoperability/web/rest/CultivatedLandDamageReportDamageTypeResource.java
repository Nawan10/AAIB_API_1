package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportDamageTypeRepository;
import lk.geoedge.interoperability.service.CultivatedLandDamageReportDamageTypeQueryService;
import lk.geoedge.interoperability.service.CultivatedLandDamageReportDamageTypeService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReportDamageTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType}.
 */
@RestController
@RequestMapping("/api/cultivated-land-damage-report-damage-types")
public class CultivatedLandDamageReportDamageTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportDamageTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandDamageReportDamageType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandDamageReportDamageTypeService cultivatedLandDamageReportDamageTypeService;

    private final CultivatedLandDamageReportDamageTypeRepository cultivatedLandDamageReportDamageTypeRepository;

    private final CultivatedLandDamageReportDamageTypeQueryService cultivatedLandDamageReportDamageTypeQueryService;

    public CultivatedLandDamageReportDamageTypeResource(
        CultivatedLandDamageReportDamageTypeService cultivatedLandDamageReportDamageTypeService,
        CultivatedLandDamageReportDamageTypeRepository cultivatedLandDamageReportDamageTypeRepository,
        CultivatedLandDamageReportDamageTypeQueryService cultivatedLandDamageReportDamageTypeQueryService
    ) {
        this.cultivatedLandDamageReportDamageTypeService = cultivatedLandDamageReportDamageTypeService;
        this.cultivatedLandDamageReportDamageTypeRepository = cultivatedLandDamageReportDamageTypeRepository;
        this.cultivatedLandDamageReportDamageTypeQueryService = cultivatedLandDamageReportDamageTypeQueryService;
    }

    /**
     * {@code POST  /cultivated-land-damage-report-damage-types} : Create a new cultivatedLandDamageReportDamageType.
     *
     * @param cultivatedLandDamageReportDamageType the cultivatedLandDamageReportDamageType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandDamageReportDamageType, or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReportDamageType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandDamageReportDamageType> createCultivatedLandDamageReportDamageType(
        @RequestBody CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandDamageReportDamageType : {}", cultivatedLandDamageReportDamageType);
        if (cultivatedLandDamageReportDamageType.getId() != null) {
            throw new BadRequestAlertException(
                "A new cultivatedLandDamageReportDamageType cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        cultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeService.save(cultivatedLandDamageReportDamageType);
        return ResponseEntity.created(
            new URI("/api/cultivated-land-damage-report-damage-types/" + cultivatedLandDamageReportDamageType.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReportDamageType.getId().toString()
                )
            )
            .body(cultivatedLandDamageReportDamageType);
    }

    /**
     * {@code PUT  /cultivated-land-damage-report-damage-types/:id} : Updates an existing cultivatedLandDamageReportDamageType.
     *
     * @param id the id of the cultivatedLandDamageReportDamageType to save.
     * @param cultivatedLandDamageReportDamageType the cultivatedLandDamageReportDamageType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReportDamageType,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReportDamageType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReportDamageType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReportDamageType> updateCultivatedLandDamageReportDamageType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandDamageReportDamageType : {}, {}", id, cultivatedLandDamageReportDamageType);
        if (cultivatedLandDamageReportDamageType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReportDamageType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReportDamageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandDamageReportDamageType = cultivatedLandDamageReportDamageTypeService.update(cultivatedLandDamageReportDamageType);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReportDamageType.getId().toString()
                )
            )
            .body(cultivatedLandDamageReportDamageType);
    }

    /**
     * {@code PATCH  /cultivated-land-damage-report-damage-types/:id} : Partial updates given fields of an existing cultivatedLandDamageReportDamageType, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandDamageReportDamageType to save.
     * @param cultivatedLandDamageReportDamageType the cultivatedLandDamageReportDamageType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReportDamageType,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReportDamageType is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandDamageReportDamageType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReportDamageType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandDamageReportDamageType> partialUpdateCultivatedLandDamageReportDamageType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update CultivatedLandDamageReportDamageType partially : {}, {}",
            id,
            cultivatedLandDamageReportDamageType
        );
        if (cultivatedLandDamageReportDamageType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReportDamageType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReportDamageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandDamageReportDamageType> result = cultivatedLandDamageReportDamageTypeService.partialUpdate(
            cultivatedLandDamageReportDamageType
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReportDamageType.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-damage-report-damage-types} : get all the cultivatedLandDamageReportDamageTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandDamageReportDamageTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandDamageReportDamageType>> getAllCultivatedLandDamageReportDamageTypes(
        CultivatedLandDamageReportDamageTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReportDamageTypes by criteria: {}", criteria);

        Page<CultivatedLandDamageReportDamageType> page = cultivatedLandDamageReportDamageTypeQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-damage-report-damage-types/count} : count all the cultivatedLandDamageReportDamageTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandDamageReportDamageTypes(CultivatedLandDamageReportDamageTypeCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandDamageReportDamageTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandDamageReportDamageTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-damage-report-damage-types/:id} : get the "id" cultivatedLandDamageReportDamageType.
     *
     * @param id the id of the cultivatedLandDamageReportDamageType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandDamageReportDamageType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReportDamageType> getCultivatedLandDamageReportDamageType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandDamageReportDamageType : {}", id);
        Optional<CultivatedLandDamageReportDamageType> cultivatedLandDamageReportDamageType =
            cultivatedLandDamageReportDamageTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandDamageReportDamageType);
    }

    /**
     * {@code DELETE  /cultivated-land-damage-report-damage-types/:id} : delete the "id" cultivatedLandDamageReportDamageType.
     *
     * @param id the id of the cultivatedLandDamageReportDamageType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandDamageReportDamageType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandDamageReportDamageType : {}", id);
        cultivatedLandDamageReportDamageTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
