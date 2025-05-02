package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCropCropType;
import lk.geoedge.interoperability.repository.CultivatedCropCropTypeRepository;
import lk.geoedge.interoperability.service.CultivatedCropCropTypeQueryService;
import lk.geoedge.interoperability.service.CultivatedCropCropTypeService;
import lk.geoedge.interoperability.service.criteria.CultivatedCropCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedCropCropType}.
 */
@RestController
@RequestMapping("/api/cultivated-crop-crop-types")
public class CultivatedCropCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedCropCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedCropCropTypeService cultivatedCropCropTypeService;

    private final CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository;

    private final CultivatedCropCropTypeQueryService cultivatedCropCropTypeQueryService;

    public CultivatedCropCropTypeResource(
        CultivatedCropCropTypeService cultivatedCropCropTypeService,
        CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository,
        CultivatedCropCropTypeQueryService cultivatedCropCropTypeQueryService
    ) {
        this.cultivatedCropCropTypeService = cultivatedCropCropTypeService;
        this.cultivatedCropCropTypeRepository = cultivatedCropCropTypeRepository;
        this.cultivatedCropCropTypeQueryService = cultivatedCropCropTypeQueryService;
    }

    /**
     * {@code POST  /cultivated-crop-crop-types} : Create a new cultivatedCropCropType.
     *
     * @param cultivatedCropCropType the cultivatedCropCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedCropCropType, or with status {@code 400 (Bad Request)} if the cultivatedCropCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedCropCropType> createCultivatedCropCropType(@RequestBody CultivatedCropCropType cultivatedCropCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save CultivatedCropCropType : {}", cultivatedCropCropType);
        if (cultivatedCropCropType.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedCropCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedCropCropType = cultivatedCropCropTypeService.save(cultivatedCropCropType);
        return ResponseEntity.created(new URI("/api/cultivated-crop-crop-types/" + cultivatedCropCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedCropCropType.getId().toString()))
            .body(cultivatedCropCropType);
    }

    /**
     * {@code PUT  /cultivated-crop-crop-types/:id} : Updates an existing cultivatedCropCropType.
     *
     * @param id the id of the cultivatedCropCropType to save.
     * @param cultivatedCropCropType the cultivatedCropCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCropCropType,
     * or with status {@code 400 (Bad Request)} if the cultivatedCropCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCropCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedCropCropType> updateCultivatedCropCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCropCropType cultivatedCropCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedCropCropType : {}, {}", id, cultivatedCropCropType);
        if (cultivatedCropCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCropCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedCropCropType = cultivatedCropCropTypeService.update(cultivatedCropCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCropCropType.getId().toString()))
            .body(cultivatedCropCropType);
    }

    /**
     * {@code PATCH  /cultivated-crop-crop-types/:id} : Partial updates given fields of an existing cultivatedCropCropType, field will ignore if it is null
     *
     * @param id the id of the cultivatedCropCropType to save.
     * @param cultivatedCropCropType the cultivatedCropCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCropCropType,
     * or with status {@code 400 (Bad Request)} if the cultivatedCropCropType is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedCropCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCropCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedCropCropType> partialUpdateCultivatedCropCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCropCropType cultivatedCropCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedCropCropType partially : {}, {}", id, cultivatedCropCropType);
        if (cultivatedCropCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCropCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedCropCropType> result = cultivatedCropCropTypeService.partialUpdate(cultivatedCropCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCropCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-crop-crop-types} : get all the cultivatedCropCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedCropCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedCropCropType>> getAllCultivatedCropCropTypes(
        CultivatedCropCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedCropCropTypes by criteria: {}", criteria);

        Page<CultivatedCropCropType> page = cultivatedCropCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-crop-crop-types/count} : count all the cultivatedCropCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedCropCropTypes(CultivatedCropCropTypeCriteria criteria) {
        LOG.debug("REST request to count CultivatedCropCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedCropCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-crop-crop-types/:id} : get the "id" cultivatedCropCropType.
     *
     * @param id the id of the cultivatedCropCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedCropCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedCropCropType> getCultivatedCropCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedCropCropType : {}", id);
        Optional<CultivatedCropCropType> cultivatedCropCropType = cultivatedCropCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedCropCropType);
    }

    /**
     * {@code DELETE  /cultivated-crop-crop-types/:id} : delete the "id" cultivatedCropCropType.
     *
     * @param id the id of the cultivatedCropCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedCropCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedCropCropType : {}", id);
        cultivatedCropCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
