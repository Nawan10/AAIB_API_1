package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDamage;
import lk.geoedge.interoperability.repository.CropDamageRepository;
import lk.geoedge.interoperability.service.CropDamageQueryService;
import lk.geoedge.interoperability.service.CropDamageService;
import lk.geoedge.interoperability.service.criteria.CropDamageCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropDamage}.
 */
@RestController
@RequestMapping("/api/crop-damages")
public class CropDamageResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropDamage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropDamageService cropDamageService;

    private final CropDamageRepository cropDamageRepository;

    private final CropDamageQueryService cropDamageQueryService;

    public CropDamageResource(
        CropDamageService cropDamageService,
        CropDamageRepository cropDamageRepository,
        CropDamageQueryService cropDamageQueryService
    ) {
        this.cropDamageService = cropDamageService;
        this.cropDamageRepository = cropDamageRepository;
        this.cropDamageQueryService = cropDamageQueryService;
    }

    /**
     * {@code POST  /crop-damages} : Create a new cropDamage.
     *
     * @param cropDamage the cropDamage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropDamage, or with status {@code 400 (Bad Request)} if the cropDamage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropDamage> createCropDamage(@RequestBody CropDamage cropDamage) throws URISyntaxException {
        LOG.debug("REST request to save CropDamage : {}", cropDamage);
        if (cropDamage.getId() != null) {
            throw new BadRequestAlertException("A new cropDamage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropDamage = cropDamageService.save(cropDamage);
        return ResponseEntity.created(new URI("/api/crop-damages/" + cropDamage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropDamage.getId().toString()))
            .body(cropDamage);
    }

    /**
     * {@code PUT  /crop-damages/:id} : Updates an existing cropDamage.
     *
     * @param id the id of the cropDamage to save.
     * @param cropDamage the cropDamage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDamage,
     * or with status {@code 400 (Bad Request)} if the cropDamage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropDamage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropDamage> updateCropDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDamage cropDamage
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropDamage : {}, {}", id, cropDamage);
        if (cropDamage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDamage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropDamage = cropDamageService.update(cropDamage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDamage.getId().toString()))
            .body(cropDamage);
    }

    /**
     * {@code PATCH  /crop-damages/:id} : Partial updates given fields of an existing cropDamage, field will ignore if it is null
     *
     * @param id the id of the cropDamage to save.
     * @param cropDamage the cropDamage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDamage,
     * or with status {@code 400 (Bad Request)} if the cropDamage is not valid,
     * or with status {@code 404 (Not Found)} if the cropDamage is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropDamage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropDamage> partialUpdateCropDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDamage cropDamage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropDamage partially : {}, {}", id, cropDamage);
        if (cropDamage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDamage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropDamage> result = cropDamageService.partialUpdate(cropDamage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDamage.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-damages} : get all the cropDamages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropDamages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropDamage>> getAllCropDamages(
        CropDamageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropDamages by criteria: {}", criteria);

        Page<CropDamage> page = cropDamageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-damages/count} : count all the cropDamages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropDamages(CropDamageCriteria criteria) {
        LOG.debug("REST request to count CropDamages by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropDamageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-damages/:id} : get the "id" cropDamage.
     *
     * @param id the id of the cropDamage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropDamage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropDamage> getCropDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropDamage : {}", id);
        Optional<CropDamage> cropDamage = cropDamageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropDamage);
    }

    /**
     * {@code DELETE  /crop-damages/:id} : delete the "id" cropDamage.
     *
     * @param id the id of the cropDamage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropDamage : {}", id);
        cropDamageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
