package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropVarietyCropType;
import lk.geoedge.interoperability.repository.CropVarietyCropTypeRepository;
import lk.geoedge.interoperability.service.CropVarietyCropTypeQueryService;
import lk.geoedge.interoperability.service.CropVarietyCropTypeService;
import lk.geoedge.interoperability.service.criteria.CropVarietyCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropVarietyCropType}.
 */
@RestController
@RequestMapping("/api/crop-variety-crop-types")
public class CropVarietyCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropVarietyCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropVarietyCropTypeService cropVarietyCropTypeService;

    private final CropVarietyCropTypeRepository cropVarietyCropTypeRepository;

    private final CropVarietyCropTypeQueryService cropVarietyCropTypeQueryService;

    public CropVarietyCropTypeResource(
        CropVarietyCropTypeService cropVarietyCropTypeService,
        CropVarietyCropTypeRepository cropVarietyCropTypeRepository,
        CropVarietyCropTypeQueryService cropVarietyCropTypeQueryService
    ) {
        this.cropVarietyCropTypeService = cropVarietyCropTypeService;
        this.cropVarietyCropTypeRepository = cropVarietyCropTypeRepository;
        this.cropVarietyCropTypeQueryService = cropVarietyCropTypeQueryService;
    }

    /**
     * {@code POST  /crop-variety-crop-types} : Create a new cropVarietyCropType.
     *
     * @param cropVarietyCropType the cropVarietyCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropVarietyCropType, or with status {@code 400 (Bad Request)} if the cropVarietyCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropVarietyCropType> createCropVarietyCropType(@RequestBody CropVarietyCropType cropVarietyCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save CropVarietyCropType : {}", cropVarietyCropType);
        if (cropVarietyCropType.getId() != null) {
            throw new BadRequestAlertException("A new cropVarietyCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropVarietyCropType = cropVarietyCropTypeService.save(cropVarietyCropType);
        return ResponseEntity.created(new URI("/api/crop-variety-crop-types/" + cropVarietyCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropVarietyCropType.getId().toString()))
            .body(cropVarietyCropType);
    }

    /**
     * {@code PUT  /crop-variety-crop-types/:id} : Updates an existing cropVarietyCropType.
     *
     * @param id the id of the cropVarietyCropType to save.
     * @param cropVarietyCropType the cropVarietyCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVarietyCropType,
     * or with status {@code 400 (Bad Request)} if the cropVarietyCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropVarietyCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropVarietyCropType> updateCropVarietyCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropVarietyCropType cropVarietyCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropVarietyCropType : {}, {}", id, cropVarietyCropType);
        if (cropVarietyCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVarietyCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropVarietyCropType = cropVarietyCropTypeService.update(cropVarietyCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropVarietyCropType.getId().toString()))
            .body(cropVarietyCropType);
    }

    /**
     * {@code PATCH  /crop-variety-crop-types/:id} : Partial updates given fields of an existing cropVarietyCropType, field will ignore if it is null
     *
     * @param id the id of the cropVarietyCropType to save.
     * @param cropVarietyCropType the cropVarietyCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVarietyCropType,
     * or with status {@code 400 (Bad Request)} if the cropVarietyCropType is not valid,
     * or with status {@code 404 (Not Found)} if the cropVarietyCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropVarietyCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropVarietyCropType> partialUpdateCropVarietyCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropVarietyCropType cropVarietyCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropVarietyCropType partially : {}, {}", id, cropVarietyCropType);
        if (cropVarietyCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVarietyCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropVarietyCropType> result = cropVarietyCropTypeService.partialUpdate(cropVarietyCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropVarietyCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-variety-crop-types} : get all the cropVarietyCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropVarietyCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropVarietyCropType>> getAllCropVarietyCropTypes(
        CropVarietyCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropVarietyCropTypes by criteria: {}", criteria);

        Page<CropVarietyCropType> page = cropVarietyCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-variety-crop-types/count} : count all the cropVarietyCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropVarietyCropTypes(CropVarietyCropTypeCriteria criteria) {
        LOG.debug("REST request to count CropVarietyCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropVarietyCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-variety-crop-types/:id} : get the "id" cropVarietyCropType.
     *
     * @param id the id of the cropVarietyCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropVarietyCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropVarietyCropType> getCropVarietyCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropVarietyCropType : {}", id);
        Optional<CropVarietyCropType> cropVarietyCropType = cropVarietyCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropVarietyCropType);
    }

    /**
     * {@code DELETE  /crop-variety-crop-types/:id} : delete the "id" cropVarietyCropType.
     *
     * @param id the id of the cropVarietyCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropVarietyCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropVarietyCropType : {}", id);
        cropVarietyCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
