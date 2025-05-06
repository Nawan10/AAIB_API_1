package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDurationCropType;
import lk.geoedge.interoperability.repository.CropDurationCropTypeRepository;
import lk.geoedge.interoperability.service.CropDurationCropTypeQueryService;
import lk.geoedge.interoperability.service.CropDurationCropTypeService;
import lk.geoedge.interoperability.service.criteria.CropDurationCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropDurationCropType}.
 */
@RestController
@RequestMapping("/api/crop-duration-crop-types")
public class CropDurationCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropDurationCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropDurationCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropDurationCropTypeService cropDurationCropTypeService;

    private final CropDurationCropTypeRepository cropDurationCropTypeRepository;

    private final CropDurationCropTypeQueryService cropDurationCropTypeQueryService;

    public CropDurationCropTypeResource(
        CropDurationCropTypeService cropDurationCropTypeService,
        CropDurationCropTypeRepository cropDurationCropTypeRepository,
        CropDurationCropTypeQueryService cropDurationCropTypeQueryService
    ) {
        this.cropDurationCropTypeService = cropDurationCropTypeService;
        this.cropDurationCropTypeRepository = cropDurationCropTypeRepository;
        this.cropDurationCropTypeQueryService = cropDurationCropTypeQueryService;
    }

    /**
     * {@code POST  /crop-duration-crop-types} : Create a new cropDurationCropType.
     *
     * @param cropDurationCropType the cropDurationCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropDurationCropType, or with status {@code 400 (Bad Request)} if the cropDurationCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropDurationCropType> createCropDurationCropType(@RequestBody CropDurationCropType cropDurationCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save CropDurationCropType : {}", cropDurationCropType);
        if (cropDurationCropType.getId() != null) {
            throw new BadRequestAlertException("A new cropDurationCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropDurationCropType = cropDurationCropTypeService.save(cropDurationCropType);
        return ResponseEntity.created(new URI("/api/crop-duration-crop-types/" + cropDurationCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropDurationCropType.getId().toString()))
            .body(cropDurationCropType);
    }

    /**
     * {@code PUT  /crop-duration-crop-types/:id} : Updates an existing cropDurationCropType.
     *
     * @param id the id of the cropDurationCropType to save.
     * @param cropDurationCropType the cropDurationCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDurationCropType,
     * or with status {@code 400 (Bad Request)} if the cropDurationCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropDurationCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropDurationCropType> updateCropDurationCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDurationCropType cropDurationCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropDurationCropType : {}, {}", id, cropDurationCropType);
        if (cropDurationCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDurationCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDurationCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropDurationCropType = cropDurationCropTypeService.update(cropDurationCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDurationCropType.getId().toString()))
            .body(cropDurationCropType);
    }

    /**
     * {@code PATCH  /crop-duration-crop-types/:id} : Partial updates given fields of an existing cropDurationCropType, field will ignore if it is null
     *
     * @param id the id of the cropDurationCropType to save.
     * @param cropDurationCropType the cropDurationCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDurationCropType,
     * or with status {@code 400 (Bad Request)} if the cropDurationCropType is not valid,
     * or with status {@code 404 (Not Found)} if the cropDurationCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropDurationCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropDurationCropType> partialUpdateCropDurationCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDurationCropType cropDurationCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropDurationCropType partially : {}, {}", id, cropDurationCropType);
        if (cropDurationCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDurationCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDurationCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropDurationCropType> result = cropDurationCropTypeService.partialUpdate(cropDurationCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDurationCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-duration-crop-types} : get all the cropDurationCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropDurationCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropDurationCropType>> getAllCropDurationCropTypes(
        CropDurationCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropDurationCropTypes by criteria: {}", criteria);

        Page<CropDurationCropType> page = cropDurationCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-duration-crop-types/count} : count all the cropDurationCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropDurationCropTypes(CropDurationCropTypeCriteria criteria) {
        LOG.debug("REST request to count CropDurationCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropDurationCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-duration-crop-types/:id} : get the "id" cropDurationCropType.
     *
     * @param id the id of the cropDurationCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropDurationCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropDurationCropType> getCropDurationCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropDurationCropType : {}", id);
        Optional<CropDurationCropType> cropDurationCropType = cropDurationCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropDurationCropType);
    }

    /**
     * {@code DELETE  /crop-duration-crop-types/:id} : delete the "id" cropDurationCropType.
     *
     * @param id the id of the cropDurationCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropDurationCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropDurationCropType : {}", id);
        cropDurationCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
