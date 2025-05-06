package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDamageCropType;
import lk.geoedge.interoperability.repository.CropDamageCropTypeRepository;
import lk.geoedge.interoperability.service.CropDamageCropTypeQueryService;
import lk.geoedge.interoperability.service.CropDamageCropTypeService;
import lk.geoedge.interoperability.service.criteria.CropDamageCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropDamageCropType}.
 */
@RestController
@RequestMapping("/api/crop-damage-crop-types")
public class CropDamageCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropDamageCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropDamageCropTypeService cropDamageCropTypeService;

    private final CropDamageCropTypeRepository cropDamageCropTypeRepository;

    private final CropDamageCropTypeQueryService cropDamageCropTypeQueryService;

    public CropDamageCropTypeResource(
        CropDamageCropTypeService cropDamageCropTypeService,
        CropDamageCropTypeRepository cropDamageCropTypeRepository,
        CropDamageCropTypeQueryService cropDamageCropTypeQueryService
    ) {
        this.cropDamageCropTypeService = cropDamageCropTypeService;
        this.cropDamageCropTypeRepository = cropDamageCropTypeRepository;
        this.cropDamageCropTypeQueryService = cropDamageCropTypeQueryService;
    }

    /**
     * {@code POST  /crop-damage-crop-types} : Create a new cropDamageCropType.
     *
     * @param cropDamageCropType the cropDamageCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropDamageCropType, or with status {@code 400 (Bad Request)} if the cropDamageCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropDamageCropType> createCropDamageCropType(@RequestBody CropDamageCropType cropDamageCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save CropDamageCropType : {}", cropDamageCropType);
        if (cropDamageCropType.getId() != null) {
            throw new BadRequestAlertException("A new cropDamageCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropDamageCropType = cropDamageCropTypeService.save(cropDamageCropType);
        return ResponseEntity.created(new URI("/api/crop-damage-crop-types/" + cropDamageCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropDamageCropType.getId().toString()))
            .body(cropDamageCropType);
    }

    /**
     * {@code PUT  /crop-damage-crop-types/:id} : Updates an existing cropDamageCropType.
     *
     * @param id the id of the cropDamageCropType to save.
     * @param cropDamageCropType the cropDamageCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDamageCropType,
     * or with status {@code 400 (Bad Request)} if the cropDamageCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropDamageCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropDamageCropType> updateCropDamageCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDamageCropType cropDamageCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropDamageCropType : {}, {}", id, cropDamageCropType);
        if (cropDamageCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDamageCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDamageCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropDamageCropType = cropDamageCropTypeService.update(cropDamageCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDamageCropType.getId().toString()))
            .body(cropDamageCropType);
    }

    /**
     * {@code PATCH  /crop-damage-crop-types/:id} : Partial updates given fields of an existing cropDamageCropType, field will ignore if it is null
     *
     * @param id the id of the cropDamageCropType to save.
     * @param cropDamageCropType the cropDamageCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDamageCropType,
     * or with status {@code 400 (Bad Request)} if the cropDamageCropType is not valid,
     * or with status {@code 404 (Not Found)} if the cropDamageCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropDamageCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropDamageCropType> partialUpdateCropDamageCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDamageCropType cropDamageCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropDamageCropType partially : {}, {}", id, cropDamageCropType);
        if (cropDamageCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDamageCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDamageCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropDamageCropType> result = cropDamageCropTypeService.partialUpdate(cropDamageCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDamageCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-damage-crop-types} : get all the cropDamageCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropDamageCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropDamageCropType>> getAllCropDamageCropTypes(
        CropDamageCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropDamageCropTypes by criteria: {}", criteria);

        Page<CropDamageCropType> page = cropDamageCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-damage-crop-types/count} : count all the cropDamageCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropDamageCropTypes(CropDamageCropTypeCriteria criteria) {
        LOG.debug("REST request to count CropDamageCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropDamageCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-damage-crop-types/:id} : get the "id" cropDamageCropType.
     *
     * @param id the id of the cropDamageCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropDamageCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropDamageCropType> getCropDamageCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropDamageCropType : {}", id);
        Optional<CropDamageCropType> cropDamageCropType = cropDamageCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropDamageCropType);
    }

    /**
     * {@code DELETE  /crop-damage-crop-types/:id} : delete the "id" cropDamageCropType.
     *
     * @param id the id of the cropDamageCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropDamageCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropDamageCropType : {}", id);
        cropDamageCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
