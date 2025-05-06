package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropVariety;
import lk.geoedge.interoperability.repository.CropVarietyRepository;
import lk.geoedge.interoperability.service.CropVarietyQueryService;
import lk.geoedge.interoperability.service.CropVarietyService;
import lk.geoedge.interoperability.service.criteria.CropVarietyCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropVariety}.
 */
@RestController
@RequestMapping("/api/crop-varieties")
public class CropVarietyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropVariety";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropVarietyService cropVarietyService;

    private final CropVarietyRepository cropVarietyRepository;

    private final CropVarietyQueryService cropVarietyQueryService;

    public CropVarietyResource(
        CropVarietyService cropVarietyService,
        CropVarietyRepository cropVarietyRepository,
        CropVarietyQueryService cropVarietyQueryService
    ) {
        this.cropVarietyService = cropVarietyService;
        this.cropVarietyRepository = cropVarietyRepository;
        this.cropVarietyQueryService = cropVarietyQueryService;
    }

    /**
     * {@code POST  /crop-varieties} : Create a new cropVariety.
     *
     * @param cropVariety the cropVariety to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropVariety, or with status {@code 400 (Bad Request)} if the cropVariety has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropVariety> createCropVariety(@RequestBody CropVariety cropVariety) throws URISyntaxException {
        LOG.debug("REST request to save CropVariety : {}", cropVariety);
        if (cropVariety.getId() != null) {
            throw new BadRequestAlertException("A new cropVariety cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropVariety = cropVarietyService.save(cropVariety);
        return ResponseEntity.created(new URI("/api/crop-varieties/" + cropVariety.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropVariety.getId().toString()))
            .body(cropVariety);
    }

    /**
     * {@code PUT  /crop-varieties/:id} : Updates an existing cropVariety.
     *
     * @param id the id of the cropVariety to save.
     * @param cropVariety the cropVariety to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVariety,
     * or with status {@code 400 (Bad Request)} if the cropVariety is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropVariety couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropVariety> updateCropVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropVariety cropVariety
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropVariety : {}, {}", id, cropVariety);
        if (cropVariety.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVariety.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropVariety = cropVarietyService.update(cropVariety);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropVariety.getId().toString()))
            .body(cropVariety);
    }

    /**
     * {@code PATCH  /crop-varieties/:id} : Partial updates given fields of an existing cropVariety, field will ignore if it is null
     *
     * @param id the id of the cropVariety to save.
     * @param cropVariety the cropVariety to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVariety,
     * or with status {@code 400 (Bad Request)} if the cropVariety is not valid,
     * or with status {@code 404 (Not Found)} if the cropVariety is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropVariety couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropVariety> partialUpdateCropVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropVariety cropVariety
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropVariety partially : {}, {}", id, cropVariety);
        if (cropVariety.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVariety.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropVariety> result = cropVarietyService.partialUpdate(cropVariety);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropVariety.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-varieties} : get all the cropVarieties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropVarieties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropVariety>> getAllCropVarieties(
        CropVarietyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropVarieties by criteria: {}", criteria);

        Page<CropVariety> page = cropVarietyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-varieties/count} : count all the cropVarieties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropVarieties(CropVarietyCriteria criteria) {
        LOG.debug("REST request to count CropVarieties by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropVarietyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-varieties/:id} : get the "id" cropVariety.
     *
     * @param id the id of the cropVariety to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropVariety, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropVariety> getCropVariety(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropVariety : {}", id);
        Optional<CropVariety> cropVariety = cropVarietyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropVariety);
    }

    /**
     * {@code DELETE  /crop-varieties/:id} : delete the "id" cropVariety.
     *
     * @param id the id of the cropVariety to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropVariety(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropVariety : {}", id);
        cropVarietyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
