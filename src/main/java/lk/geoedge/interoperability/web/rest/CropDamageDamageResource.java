package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDamageDamage;
import lk.geoedge.interoperability.repository.CropDamageDamageRepository;
import lk.geoedge.interoperability.service.CropDamageDamageQueryService;
import lk.geoedge.interoperability.service.CropDamageDamageService;
import lk.geoedge.interoperability.service.criteria.CropDamageDamageCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CropDamageDamage}.
 */
@RestController
@RequestMapping("/api/crop-damage-damages")
public class CropDamageDamageResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageDamageResource.class);

    private static final String ENTITY_NAME = "aaibapi1CropDamageDamage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CropDamageDamageService cropDamageDamageService;

    private final CropDamageDamageRepository cropDamageDamageRepository;

    private final CropDamageDamageQueryService cropDamageDamageQueryService;

    public CropDamageDamageResource(
        CropDamageDamageService cropDamageDamageService,
        CropDamageDamageRepository cropDamageDamageRepository,
        CropDamageDamageQueryService cropDamageDamageQueryService
    ) {
        this.cropDamageDamageService = cropDamageDamageService;
        this.cropDamageDamageRepository = cropDamageDamageRepository;
        this.cropDamageDamageQueryService = cropDamageDamageQueryService;
    }

    /**
     * {@code POST  /crop-damage-damages} : Create a new cropDamageDamage.
     *
     * @param cropDamageDamage the cropDamageDamage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropDamageDamage, or with status {@code 400 (Bad Request)} if the cropDamageDamage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropDamageDamage> createCropDamageDamage(@RequestBody CropDamageDamage cropDamageDamage)
        throws URISyntaxException {
        LOG.debug("REST request to save CropDamageDamage : {}", cropDamageDamage);
        if (cropDamageDamage.getId() != null) {
            throw new BadRequestAlertException("A new cropDamageDamage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropDamageDamage = cropDamageDamageService.save(cropDamageDamage);
        return ResponseEntity.created(new URI("/api/crop-damage-damages/" + cropDamageDamage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cropDamageDamage.getId().toString()))
            .body(cropDamageDamage);
    }

    /**
     * {@code PUT  /crop-damage-damages/:id} : Updates an existing cropDamageDamage.
     *
     * @param id the id of the cropDamageDamage to save.
     * @param cropDamageDamage the cropDamageDamage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDamageDamage,
     * or with status {@code 400 (Bad Request)} if the cropDamageDamage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropDamageDamage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropDamageDamage> updateCropDamageDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDamageDamage cropDamageDamage
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropDamageDamage : {}, {}", id, cropDamageDamage);
        if (cropDamageDamage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDamageDamage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDamageDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropDamageDamage = cropDamageDamageService.update(cropDamageDamage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDamageDamage.getId().toString()))
            .body(cropDamageDamage);
    }

    /**
     * {@code PATCH  /crop-damage-damages/:id} : Partial updates given fields of an existing cropDamageDamage, field will ignore if it is null
     *
     * @param id the id of the cropDamageDamage to save.
     * @param cropDamageDamage the cropDamageDamage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropDamageDamage,
     * or with status {@code 400 (Bad Request)} if the cropDamageDamage is not valid,
     * or with status {@code 404 (Not Found)} if the cropDamageDamage is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropDamageDamage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropDamageDamage> partialUpdateCropDamageDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CropDamageDamage cropDamageDamage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropDamageDamage partially : {}, {}", id, cropDamageDamage);
        if (cropDamageDamage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropDamageDamage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropDamageDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropDamageDamage> result = cropDamageDamageService.partialUpdate(cropDamageDamage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cropDamageDamage.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-damage-damages} : get all the cropDamageDamages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cropDamageDamages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropDamageDamage>> getAllCropDamageDamages(
        CropDamageDamageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CropDamageDamages by criteria: {}", criteria);

        Page<CropDamageDamage> page = cropDamageDamageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crop-damage-damages/count} : count all the cropDamageDamages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropDamageDamages(CropDamageDamageCriteria criteria) {
        LOG.debug("REST request to count CropDamageDamages by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropDamageDamageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-damage-damages/:id} : get the "id" cropDamageDamage.
     *
     * @param id the id of the cropDamageDamage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropDamageDamage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropDamageDamage> getCropDamageDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropDamageDamage : {}", id);
        Optional<CropDamageDamage> cropDamageDamage = cropDamageDamageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropDamageDamage);
    }

    /**
     * {@code DELETE  /crop-damage-damages/:id} : delete the "id" cropDamageDamage.
     *
     * @param id the id of the cropDamageDamage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropDamageDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropDamageDamage : {}", id);
        cropDamageDamageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
