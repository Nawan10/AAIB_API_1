package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageTypeRepository;
import lk.geoedge.interoperability.service.CultivatedLandDamageReasonDamageTypeQueryService;
import lk.geoedge.interoperability.service.CultivatedLandDamageReasonDamageTypeService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReasonDamageTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType}.
 */
@RestController
@RequestMapping("/api/cultivated-land-damage-reason-damage-types")
public class CultivatedLandDamageReasonDamageTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonDamageTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandDamageReasonDamageType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandDamageReasonDamageTypeService cultivatedLandDamageReasonDamageTypeService;

    private final CultivatedLandDamageReasonDamageTypeRepository cultivatedLandDamageReasonDamageTypeRepository;

    private final CultivatedLandDamageReasonDamageTypeQueryService cultivatedLandDamageReasonDamageTypeQueryService;

    public CultivatedLandDamageReasonDamageTypeResource(
        CultivatedLandDamageReasonDamageTypeService cultivatedLandDamageReasonDamageTypeService,
        CultivatedLandDamageReasonDamageTypeRepository cultivatedLandDamageReasonDamageTypeRepository,
        CultivatedLandDamageReasonDamageTypeQueryService cultivatedLandDamageReasonDamageTypeQueryService
    ) {
        this.cultivatedLandDamageReasonDamageTypeService = cultivatedLandDamageReasonDamageTypeService;
        this.cultivatedLandDamageReasonDamageTypeRepository = cultivatedLandDamageReasonDamageTypeRepository;
        this.cultivatedLandDamageReasonDamageTypeQueryService = cultivatedLandDamageReasonDamageTypeQueryService;
    }

    /**
     * {@code POST  /cultivated-land-damage-reason-damage-types} : Create a new cultivatedLandDamageReasonDamageType.
     *
     * @param cultivatedLandDamageReasonDamageType the cultivatedLandDamageReasonDamageType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandDamageReasonDamageType, or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReasonDamageType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandDamageReasonDamageType> createCultivatedLandDamageReasonDamageType(
        @RequestBody CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandDamageReasonDamageType : {}", cultivatedLandDamageReasonDamageType);
        if (cultivatedLandDamageReasonDamageType.getId() != null) {
            throw new BadRequestAlertException(
                "A new cultivatedLandDamageReasonDamageType cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        cultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeService.save(cultivatedLandDamageReasonDamageType);
        return ResponseEntity.created(
            new URI("/api/cultivated-land-damage-reason-damage-types/" + cultivatedLandDamageReasonDamageType.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReasonDamageType.getId().toString()
                )
            )
            .body(cultivatedLandDamageReasonDamageType);
    }

    /**
     * {@code PUT  /cultivated-land-damage-reason-damage-types/:id} : Updates an existing cultivatedLandDamageReasonDamageType.
     *
     * @param id the id of the cultivatedLandDamageReasonDamageType to save.
     * @param cultivatedLandDamageReasonDamageType the cultivatedLandDamageReasonDamageType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReasonDamageType,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReasonDamageType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReasonDamageType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReasonDamageType> updateCultivatedLandDamageReasonDamageType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandDamageReasonDamageType : {}, {}", id, cultivatedLandDamageReasonDamageType);
        if (cultivatedLandDamageReasonDamageType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReasonDamageType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReasonDamageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandDamageReasonDamageType = cultivatedLandDamageReasonDamageTypeService.update(cultivatedLandDamageReasonDamageType);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReasonDamageType.getId().toString()
                )
            )
            .body(cultivatedLandDamageReasonDamageType);
    }

    /**
     * {@code PATCH  /cultivated-land-damage-reason-damage-types/:id} : Partial updates given fields of an existing cultivatedLandDamageReasonDamageType, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandDamageReasonDamageType to save.
     * @param cultivatedLandDamageReasonDamageType the cultivatedLandDamageReasonDamageType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReasonDamageType,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReasonDamageType is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandDamageReasonDamageType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReasonDamageType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandDamageReasonDamageType> partialUpdateCultivatedLandDamageReasonDamageType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update CultivatedLandDamageReasonDamageType partially : {}, {}",
            id,
            cultivatedLandDamageReasonDamageType
        );
        if (cultivatedLandDamageReasonDamageType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReasonDamageType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReasonDamageTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandDamageReasonDamageType> result = cultivatedLandDamageReasonDamageTypeService.partialUpdate(
            cultivatedLandDamageReasonDamageType
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandDamageReasonDamageType.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-damage-reason-damage-types} : get all the cultivatedLandDamageReasonDamageTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandDamageReasonDamageTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandDamageReasonDamageType>> getAllCultivatedLandDamageReasonDamageTypes(
        CultivatedLandDamageReasonDamageTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReasonDamageTypes by criteria: {}", criteria);

        Page<CultivatedLandDamageReasonDamageType> page = cultivatedLandDamageReasonDamageTypeQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-damage-reason-damage-types/count} : count all the cultivatedLandDamageReasonDamageTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandDamageReasonDamageTypes(CultivatedLandDamageReasonDamageTypeCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandDamageReasonDamageTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandDamageReasonDamageTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-damage-reason-damage-types/:id} : get the "id" cultivatedLandDamageReasonDamageType.
     *
     * @param id the id of the cultivatedLandDamageReasonDamageType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandDamageReasonDamageType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReasonDamageType> getCultivatedLandDamageReasonDamageType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandDamageReasonDamageType : {}", id);
        Optional<CultivatedLandDamageReasonDamageType> cultivatedLandDamageReasonDamageType =
            cultivatedLandDamageReasonDamageTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandDamageReasonDamageType);
    }

    /**
     * {@code DELETE  /cultivated-land-damage-reason-damage-types/:id} : delete the "id" cultivatedLandDamageReasonDamageType.
     *
     * @param id the id of the cultivatedLandDamageReasonDamageType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandDamageReasonDamageType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandDamageReasonDamageType : {}", id);
        cultivatedLandDamageReasonDamageTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
