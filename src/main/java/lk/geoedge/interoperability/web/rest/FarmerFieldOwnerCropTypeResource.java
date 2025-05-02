package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerCropTypeRepository;
import lk.geoedge.interoperability.service.FarmerFieldOwnerCropTypeQueryService;
import lk.geoedge.interoperability.service.FarmerFieldOwnerCropTypeService;
import lk.geoedge.interoperability.service.criteria.FarmerFieldOwnerCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType}.
 */
@RestController
@RequestMapping("/api/farmer-field-owner-crop-types")
public class FarmerFieldOwnerCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldOwnerCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1FarmerFieldOwnerCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmerFieldOwnerCropTypeService farmerFieldOwnerCropTypeService;

    private final FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository;

    private final FarmerFieldOwnerCropTypeQueryService farmerFieldOwnerCropTypeQueryService;

    public FarmerFieldOwnerCropTypeResource(
        FarmerFieldOwnerCropTypeService farmerFieldOwnerCropTypeService,
        FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository,
        FarmerFieldOwnerCropTypeQueryService farmerFieldOwnerCropTypeQueryService
    ) {
        this.farmerFieldOwnerCropTypeService = farmerFieldOwnerCropTypeService;
        this.farmerFieldOwnerCropTypeRepository = farmerFieldOwnerCropTypeRepository;
        this.farmerFieldOwnerCropTypeQueryService = farmerFieldOwnerCropTypeQueryService;
    }

    /**
     * {@code POST  /farmer-field-owner-crop-types} : Create a new farmerFieldOwnerCropType.
     *
     * @param farmerFieldOwnerCropType the farmerFieldOwnerCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmerFieldOwnerCropType, or with status {@code 400 (Bad Request)} if the farmerFieldOwnerCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmerFieldOwnerCropType> createFarmerFieldOwnerCropType(
        @RequestBody FarmerFieldOwnerCropType farmerFieldOwnerCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to save FarmerFieldOwnerCropType : {}", farmerFieldOwnerCropType);
        if (farmerFieldOwnerCropType.getId() != null) {
            throw new BadRequestAlertException("A new farmerFieldOwnerCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmerFieldOwnerCropType = farmerFieldOwnerCropTypeService.save(farmerFieldOwnerCropType);
        return ResponseEntity.created(new URI("/api/farmer-field-owner-crop-types/" + farmerFieldOwnerCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmerFieldOwnerCropType.getId().toString()))
            .body(farmerFieldOwnerCropType);
    }

    /**
     * {@code PUT  /farmer-field-owner-crop-types/:id} : Updates an existing farmerFieldOwnerCropType.
     *
     * @param id the id of the farmerFieldOwnerCropType to save.
     * @param farmerFieldOwnerCropType the farmerFieldOwnerCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldOwnerCropType,
     * or with status {@code 400 (Bad Request)} if the farmerFieldOwnerCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldOwnerCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmerFieldOwnerCropType> updateFarmerFieldOwnerCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldOwnerCropType farmerFieldOwnerCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update FarmerFieldOwnerCropType : {}, {}", id, farmerFieldOwnerCropType);
        if (farmerFieldOwnerCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldOwnerCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldOwnerCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmerFieldOwnerCropType = farmerFieldOwnerCropTypeService.update(farmerFieldOwnerCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldOwnerCropType.getId().toString()))
            .body(farmerFieldOwnerCropType);
    }

    /**
     * {@code PATCH  /farmer-field-owner-crop-types/:id} : Partial updates given fields of an existing farmerFieldOwnerCropType, field will ignore if it is null
     *
     * @param id the id of the farmerFieldOwnerCropType to save.
     * @param farmerFieldOwnerCropType the farmerFieldOwnerCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldOwnerCropType,
     * or with status {@code 400 (Bad Request)} if the farmerFieldOwnerCropType is not valid,
     * or with status {@code 404 (Not Found)} if the farmerFieldOwnerCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldOwnerCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmerFieldOwnerCropType> partialUpdateFarmerFieldOwnerCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldOwnerCropType farmerFieldOwnerCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FarmerFieldOwnerCropType partially : {}, {}", id, farmerFieldOwnerCropType);
        if (farmerFieldOwnerCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldOwnerCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldOwnerCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmerFieldOwnerCropType> result = farmerFieldOwnerCropTypeService.partialUpdate(farmerFieldOwnerCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldOwnerCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /farmer-field-owner-crop-types} : get all the farmerFieldOwnerCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmerFieldOwnerCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmerFieldOwnerCropType>> getAllFarmerFieldOwnerCropTypes(
        FarmerFieldOwnerCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FarmerFieldOwnerCropTypes by criteria: {}", criteria);

        Page<FarmerFieldOwnerCropType> page = farmerFieldOwnerCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmer-field-owner-crop-types/count} : count all the farmerFieldOwnerCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFarmerFieldOwnerCropTypes(FarmerFieldOwnerCropTypeCriteria criteria) {
        LOG.debug("REST request to count FarmerFieldOwnerCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmerFieldOwnerCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmer-field-owner-crop-types/:id} : get the "id" farmerFieldOwnerCropType.
     *
     * @param id the id of the farmerFieldOwnerCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmerFieldOwnerCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmerFieldOwnerCropType> getFarmerFieldOwnerCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FarmerFieldOwnerCropType : {}", id);
        Optional<FarmerFieldOwnerCropType> farmerFieldOwnerCropType = farmerFieldOwnerCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmerFieldOwnerCropType);
    }

    /**
     * {@code DELETE  /farmer-field-owner-crop-types/:id} : delete the "id" farmerFieldOwnerCropType.
     *
     * @param id the id of the farmerFieldOwnerCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmerFieldOwnerCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FarmerFieldOwnerCropType : {}", id);
        farmerFieldOwnerCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
