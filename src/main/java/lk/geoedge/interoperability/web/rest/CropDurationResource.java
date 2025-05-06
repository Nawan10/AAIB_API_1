package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDuration;
import lk.geoedge.interoperability.repository.CropDurationRepository;
import lk.geoedge.interoperability.service.CropDurationQueryService;
import lk.geoedge.interoperability.service.CropDurationService;
import lk.geoedge.interoperability.service.criteria.CropDurationCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropDuration}.
 */
@RestController
@RequestMapping("/api/crop-durations")
public class CropDurationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropDurationResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropDuration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropDurationService cropDurationService;

    private final CropDurationRepository cropDurationRepository;

    private final CropDurationQueryService cropDurationQueryService;

    public CropDurationResource(
        CropDurationService cropDurationService,
        CropDurationRepository cropDurationRepository,
        CropDurationQueryService cropDurationQueryService
    ) {
        this.cropDurationService = cropDurationService;
        this.cropDurationRepository = cropDurationRepository;
        this.cropDurationQueryService = cropDurationQueryService;
    }

    /**
     * {@code POST  /crop-durations} : Create a new cropDuration.
     *
     * @param cropDuration the cropDuration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropDuration, or with status {@code 400 (Bad Request)} if the cropDuration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropDuration> createCropDuration(@RequestBody CropDuration cropDuration) throws URISyntaxException {
        LOG.debug("REST request to save CropDuration : {}", cropDuration);
        if (cropDuration.getId() != null) {
            throw new BadRequestAlertException("A new cropDuration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropDuration = cropDurationService.save(cropDuration);
        return ResponseEntity.created(new URI("/api/crop-durations/" + cropDuration.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropDuration.getId().toString()))
            .body(cropDuration);
    }

    /**
     * {@code PUT  /crop-durations/:id} : Updates an existing cropDuration.
     *
     * @param id the id of the cropDuration to save.
     * @param cropDuration the cropDuration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDuration,
     * or with status {@code 400 (Bad Request)} if the cropDuration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropDuration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropDuration> updateCropDuration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDuration cropDuration
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropDuration : {}, {}", id, cropDuration);
        if (cropDuration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDuration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropDuration = cropDurationService.update(cropDuration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDuration.getId().toString()))
            .body(cropDuration);
    }

    /**
     * {@code PATCH  /crop-durations/:id} : Partial updates given fields of an existing cropDuration, field will ignore if it is null
     *
     * @param id the id of the cropDuration to save.
     * @param cropDuration the cropDuration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDuration,
     * or with status {@code 400 (Bad Request)} if the cropDuration is not valid,
     * or with status {@code 404 (Not Found)} if the cropDuration is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropDuration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropDuration> partialUpdateCropDuration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDuration cropDuration
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropDuration partially : {}, {}", id, cropDuration);
        if (cropDuration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDuration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropDuration> result = cropDurationService.partialUpdate(cropDuration);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDuration.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-durations} : get all the cropDurations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropDurations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropDuration>> getAllCropDurations(
        CropDurationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropDurations by criteria: {}", criteria);

        Page<CropDuration> page = cropDurationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-durations/count} : count all the cropDurations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropDurations(CropDurationCriteria criteria) {
        LOG.debug("REST request to count CropDurations by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropDurationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-durations/:id} : get the "id" cropDuration.
     *
     * @param id the id of the cropDuration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropDuration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropDuration> getCropDuration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropDuration : {}", id);
        Optional<CropDuration> cropDuration = cropDurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropDuration);
    }

    /**
     * {@code DELETE  /crop-durations/:id} : delete the "id" cropDuration.
     *
     * @param id the id of the cropDuration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropDuration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropDuration : {}", id);
        cropDurationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
