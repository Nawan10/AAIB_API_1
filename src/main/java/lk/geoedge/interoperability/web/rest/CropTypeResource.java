package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropType;
import lk.geoedge.interoperability.repository.CropTypeRepository;
import lk.geoedge.interoperability.service.CropTypeQueryService;
import lk.geoedge.interoperability.service.CropTypeService;
import lk.geoedge.interoperability.service.criteria.CropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropType}.
 */
@RestController
@RequestMapping("/api/crop-types")
public class CropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropTypeService cropTypeService;

    private final CropTypeRepository cropTypeRepository;

    private final CropTypeQueryService cropTypeQueryService;

    public CropTypeResource(
        CropTypeService cropTypeService,
        CropTypeRepository cropTypeRepository,
        CropTypeQueryService cropTypeQueryService
    ) {
        this.cropTypeService = cropTypeService;
        this.cropTypeRepository = cropTypeRepository;
        this.cropTypeQueryService = cropTypeQueryService;
    }

    /**
     * {@code POST  /crop-types} : Create a new cropType.
     *
     * @param cropType the cropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropType, or with status {@code 400 (Bad Request)} if the cropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropType> createCropType(@RequestBody CropType cropType) throws URISyntaxException {
        LOG.debug("REST request to save CropType : {}", cropType);
        if (cropType.getId() != null) {
            throw new BadRequestAlertException("A new cropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropType = cropTypeService.save(cropType);
        return ResponseEntity.created(new URI("/api/crop-types/" + cropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropType.getId().toString()))
            .body(cropType);
    }

    /**
     * {@code PUT  /crop-types/:id} : Updates an existing cropType.
     *
     * @param id the id of the cropType to save.
     * @param cropType the cropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropType,
     * or with status {@code 400 (Bad Request)} if the cropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropType> updateCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropType cropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropType : {}, {}", id, cropType);
        if (cropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropType = cropTypeService.update(cropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropType.getId().toString()))
            .body(cropType);
    }

    /**
     * {@code PATCH  /crop-types/:id} : Partial updates given fields of an existing cropType, field will ignore if it is null
     *
     * @param id the id of the cropType to save.
     * @param cropType the cropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropType,
     * or with status {@code 400 (Bad Request)} if the cropType is not valid,
     * or with status {@code 404 (Not Found)} if the cropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropType> partialUpdateCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropType cropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropType partially : {}, {}", id, cropType);
        if (cropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropType> result = cropTypeService.partialUpdate(cropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropType.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-types} : get all the cropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropType>> getAllCropTypes(
        CropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropTypes by criteria: {}", criteria);

        Page<CropType> page = cropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-types/count} : count all the cropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropTypes(CropTypeCriteria criteria) {
        LOG.debug("REST request to count CropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-types/:id} : get the "id" cropType.
     *
     * @param id the id of the cropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropType> getCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropType : {}", id);
        Optional<CropType> cropType = cropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropType);
    }

    /**
     * {@code DELETE  /crop-types/:id} : delete the "id" cropType.
     *
     * @param id the id of the cropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropType : {}", id);
        cropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
