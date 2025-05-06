package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropVarietyCropDuration;
import lk.geoedge.interoperability.repository.CropVarietyCropDurationRepository;
import lk.geoedge.interoperability.service.CropVarietyCropDurationQueryService;
import lk.geoedge.interoperability.service.CropVarietyCropDurationService;
import lk.geoedge.interoperability.service.criteria.CropVarietyCropDurationCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropVarietyCropDuration}.
 */
@RestController
@RequestMapping("/api/crop-variety-crop-durations")
public class CropVarietyCropDurationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyCropDurationResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropVarietyCropDuration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropVarietyCropDurationService cropVarietyCropDurationService;

    private final CropVarietyCropDurationRepository cropVarietyCropDurationRepository;

    private final CropVarietyCropDurationQueryService cropVarietyCropDurationQueryService;

    public CropVarietyCropDurationResource(
        CropVarietyCropDurationService cropVarietyCropDurationService,
        CropVarietyCropDurationRepository cropVarietyCropDurationRepository,
        CropVarietyCropDurationQueryService cropVarietyCropDurationQueryService
    ) {
        this.cropVarietyCropDurationService = cropVarietyCropDurationService;
        this.cropVarietyCropDurationRepository = cropVarietyCropDurationRepository;
        this.cropVarietyCropDurationQueryService = cropVarietyCropDurationQueryService;
    }

    /**
     * {@code POST  /crop-variety-crop-durations} : Create a new cropVarietyCropDuration.
     *
     * @param cropVarietyCropDuration the cropVarietyCropDuration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropVarietyCropDuration, or with status {@code 400 (Bad Request)} if the cropVarietyCropDuration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropVarietyCropDuration> createCropVarietyCropDuration(
        @RequestBody CropVarietyCropDuration cropVarietyCropDuration
    ) throws URISyntaxException {
        LOG.debug("REST request to save CropVarietyCropDuration : {}", cropVarietyCropDuration);
        if (cropVarietyCropDuration.getId() != null) {
            throw new BadRequestAlertException("A new cropVarietyCropDuration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropVarietyCropDuration = cropVarietyCropDurationService.save(cropVarietyCropDuration);
        return ResponseEntity.created(new URI("/api/crop-variety-crop-durations/" + cropVarietyCropDuration.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropVarietyCropDuration.getId().toString()))
            .body(cropVarietyCropDuration);
    }

    /**
     * {@code PUT  /crop-variety-crop-durations/:id} : Updates an existing cropVarietyCropDuration.
     *
     * @param id the id of the cropVarietyCropDuration to save.
     * @param cropVarietyCropDuration the cropVarietyCropDuration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVarietyCropDuration,
     * or with status {@code 400 (Bad Request)} if the cropVarietyCropDuration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropVarietyCropDuration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropVarietyCropDuration> updateCropVarietyCropDuration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropVarietyCropDuration cropVarietyCropDuration
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropVarietyCropDuration : {}, {}", id, cropVarietyCropDuration);
        if (cropVarietyCropDuration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVarietyCropDuration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyCropDurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropVarietyCropDuration = cropVarietyCropDurationService.update(cropVarietyCropDuration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropVarietyCropDuration.getId().toString()))
            .body(cropVarietyCropDuration);
    }

    /**
     * {@code PATCH  /crop-variety-crop-durations/:id} : Partial updates given fields of an existing cropVarietyCropDuration, field will ignore if it is null
     *
     * @param id the id of the cropVarietyCropDuration to save.
     * @param cropVarietyCropDuration the cropVarietyCropDuration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVarietyCropDuration,
     * or with status {@code 400 (Bad Request)} if the cropVarietyCropDuration is not valid,
     * or with status {@code 404 (Not Found)} if the cropVarietyCropDuration is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropVarietyCropDuration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropVarietyCropDuration> partialUpdateCropVarietyCropDuration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropVarietyCropDuration cropVarietyCropDuration
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropVarietyCropDuration partially : {}, {}", id, cropVarietyCropDuration);
        if (cropVarietyCropDuration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVarietyCropDuration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyCropDurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropVarietyCropDuration> result = cropVarietyCropDurationService.partialUpdate(cropVarietyCropDuration);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropVarietyCropDuration.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-variety-crop-durations} : get all the cropVarietyCropDurations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropVarietyCropDurations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropVarietyCropDuration>> getAllCropVarietyCropDurations(
        CropVarietyCropDurationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropVarietyCropDurations by criteria: {}", criteria);

        Page<CropVarietyCropDuration> page = cropVarietyCropDurationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-variety-crop-durations/count} : count all the cropVarietyCropDurations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropVarietyCropDurations(CropVarietyCropDurationCriteria criteria) {
        LOG.debug("REST request to count CropVarietyCropDurations by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropVarietyCropDurationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-variety-crop-durations/:id} : get the "id" cropVarietyCropDuration.
     *
     * @param id the id of the cropVarietyCropDuration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropVarietyCropDuration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropVarietyCropDuration> getCropVarietyCropDuration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropVarietyCropDuration : {}", id);
        Optional<CropVarietyCropDuration> cropVarietyCropDuration = cropVarietyCropDurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropVarietyCropDuration);
    }

    /**
     * {@code DELETE  /crop-variety-crop-durations/:id} : delete the "id" cropVarietyCropDuration.
     *
     * @param id the id of the cropVarietyCropDuration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropVarietyCropDuration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropVarietyCropDuration : {}", id);
        cropVarietyCropDurationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
