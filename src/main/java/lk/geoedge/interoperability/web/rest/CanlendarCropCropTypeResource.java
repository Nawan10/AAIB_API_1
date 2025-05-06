package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CanlendarCropCropType;
import lk.geoedge.interoperability.repository.CanlendarCropCropTypeRepository;
import lk.geoedge.interoperability.service.CanlendarCropCropTypeQueryService;
import lk.geoedge.interoperability.service.CanlendarCropCropTypeService;
import lk.geoedge.interoperability.service.criteria.CanlendarCropCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CanlendarCropCropType}.
 */
@RestController
@RequestMapping("/api/canlendar-crop-crop-types")
public class CanlendarCropCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1CanlendarCropCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CanlendarCropCropTypeService canlendarCropCropTypeService;

    private final CanlendarCropCropTypeRepository canlendarCropCropTypeRepository;

    private final CanlendarCropCropTypeQueryService canlendarCropCropTypeQueryService;

    public CanlendarCropCropTypeResource(
        CanlendarCropCropTypeService canlendarCropCropTypeService,
        CanlendarCropCropTypeRepository canlendarCropCropTypeRepository,
        CanlendarCropCropTypeQueryService canlendarCropCropTypeQueryService
    ) {
        this.canlendarCropCropTypeService = canlendarCropCropTypeService;
        this.canlendarCropCropTypeRepository = canlendarCropCropTypeRepository;
        this.canlendarCropCropTypeQueryService = canlendarCropCropTypeQueryService;
    }

    /**
     * {@code POST  /canlendar-crop-crop-types} : Create a new canlendarCropCropType.
     *
     * @param canlendarCropCropType the canlendarCropCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new canlendarCropCropType, or with status {@code 400 (Bad Request)} if the canlendarCropCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CanlendarCropCropType> createCanlendarCropCropType(@RequestBody CanlendarCropCropType canlendarCropCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save CanlendarCropCropType : {}", canlendarCropCropType);
        if (canlendarCropCropType.getId() != null) {
            throw new BadRequestAlertException("A new canlendarCropCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        canlendarCropCropType = canlendarCropCropTypeService.save(canlendarCropCropType);
        return ResponseEntity.created(new URI("/api/canlendar-crop-crop-types/" + canlendarCropCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, canlendarCropCropType.getId().toString()))
            .body(canlendarCropCropType);
    }

    /**
     * {@code PUT  /canlendar-crop-crop-types/:id} : Updates an existing canlendarCropCropType.
     *
     * @param id the id of the canlendarCropCropType to save.
     * @param canlendarCropCropType the canlendarCropCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canlendarCropCropType,
     * or with status {@code 400 (Bad Request)} if the canlendarCropCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the canlendarCropCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CanlendarCropCropType> updateCanlendarCropCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CanlendarCropCropType canlendarCropCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update CanlendarCropCropType : {}, {}", id, canlendarCropCropType);
        if (canlendarCropCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canlendarCropCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canlendarCropCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        canlendarCropCropType = canlendarCropCropTypeService.update(canlendarCropCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canlendarCropCropType.getId().toString()))
            .body(canlendarCropCropType);
    }

    /**
     * {@code PATCH  /canlendar-crop-crop-types/:id} : Partial updates given fields of an existing canlendarCropCropType, field will ignore if it is null
     *
     * @param id the id of the canlendarCropCropType to save.
     * @param canlendarCropCropType the canlendarCropCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canlendarCropCropType,
     * or with status {@code 400 (Bad Request)} if the canlendarCropCropType is not valid,
     * or with status {@code 404 (Not Found)} if the canlendarCropCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the canlendarCropCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CanlendarCropCropType> partialUpdateCanlendarCropCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CanlendarCropCropType canlendarCropCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CanlendarCropCropType partially : {}, {}", id, canlendarCropCropType);
        if (canlendarCropCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canlendarCropCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canlendarCropCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CanlendarCropCropType> result = canlendarCropCropTypeService.partialUpdate(canlendarCropCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canlendarCropCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /canlendar-crop-crop-types} : get all the canlendarCropCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of canlendarCropCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CanlendarCropCropType>> getAllCanlendarCropCropTypes(
        CanlendarCropCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CanlendarCropCropTypes by criteria: {}", criteria);

        Page<CanlendarCropCropType> page = canlendarCropCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /canlendar-crop-crop-types/count} : count all the canlendarCropCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCanlendarCropCropTypes(CanlendarCropCropTypeCriteria criteria) {
        LOG.debug("REST request to count CanlendarCropCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(canlendarCropCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /canlendar-crop-crop-types/:id} : get the "id" canlendarCropCropType.
     *
     * @param id the id of the canlendarCropCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the canlendarCropCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CanlendarCropCropType> getCanlendarCropCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CanlendarCropCropType : {}", id);
        Optional<CanlendarCropCropType> canlendarCropCropType = canlendarCropCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(canlendarCropCropType);
    }

    /**
     * {@code DELETE  /canlendar-crop-crop-types/:id} : delete the "id" canlendarCropCropType.
     *
     * @param id the id of the canlendarCropCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCanlendarCropCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CanlendarCropCropType : {}", id);
        canlendarCropCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
