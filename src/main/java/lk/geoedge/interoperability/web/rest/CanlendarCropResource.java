package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CanlendarCrop;
import lk.geoedge.interoperability.repository.CanlendarCropRepository;
import lk.geoedge.interoperability.service.CanlendarCropQueryService;
import lk.geoedge.interoperability.service.CanlendarCropService;
import lk.geoedge.interoperability.service.criteria.CanlendarCropCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CanlendarCrop}.
 */
@RestController
@RequestMapping("/api/canlendar-crops")
public class CanlendarCropResource {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropResource.class);

    private static final String ENTITY_NAME = "aaibapi1CanlendarCrop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CanlendarCropService canlendarCropService;

    private final CanlendarCropRepository canlendarCropRepository;

    private final CanlendarCropQueryService canlendarCropQueryService;

    public CanlendarCropResource(
        CanlendarCropService canlendarCropService,
        CanlendarCropRepository canlendarCropRepository,
        CanlendarCropQueryService canlendarCropQueryService
    ) {
        this.canlendarCropService = canlendarCropService;
        this.canlendarCropRepository = canlendarCropRepository;
        this.canlendarCropQueryService = canlendarCropQueryService;
    }

    /**
     * {@code POST  /canlendar-crops} : Create a new canlendarCrop.
     *
     * @param canlendarCrop the canlendarCrop to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new canlendarCrop, or with status {@code 400 (Bad Request)} if the canlendarCrop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CanlendarCrop> createCanlendarCrop(@RequestBody CanlendarCrop canlendarCrop) throws URISyntaxException {
        LOG.debug("REST request to save CanlendarCrop : {}", canlendarCrop);
        if (canlendarCrop.getId() != null) {
            throw new BadRequestAlertException("A new canlendarCrop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        canlendarCrop = canlendarCropService.save(canlendarCrop);
        return ResponseEntity.created(new URI("/api/canlendar-crops/" + canlendarCrop.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, canlendarCrop.getId().toString()))
            .body(canlendarCrop);
    }

    /**
     * {@code PUT  /canlendar-crops/:id} : Updates an existing canlendarCrop.
     *
     * @param id the id of the canlendarCrop to save.
     * @param canlendarCrop the canlendarCrop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canlendarCrop,
     * or with status {@code 400 (Bad Request)} if the canlendarCrop is not valid,
     * or with status {@code 500 (Internal Server Error)} if the canlendarCrop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CanlendarCrop> updateCanlendarCrop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CanlendarCrop canlendarCrop
    ) throws URISyntaxException {
        LOG.debug("REST request to update CanlendarCrop : {}, {}", id, canlendarCrop);
        if (canlendarCrop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canlendarCrop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canlendarCropRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        canlendarCrop = canlendarCropService.update(canlendarCrop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canlendarCrop.getId().toString()))
            .body(canlendarCrop);
    }

    /**
     * {@code PATCH  /canlendar-crops/:id} : Partial updates given fields of an existing canlendarCrop, field will ignore if it is null
     *
     * @param id the id of the canlendarCrop to save.
     * @param canlendarCrop the canlendarCrop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canlendarCrop,
     * or with status {@code 400 (Bad Request)} if the canlendarCrop is not valid,
     * or with status {@code 404 (Not Found)} if the canlendarCrop is not found,
     * or with status {@code 500 (Internal Server Error)} if the canlendarCrop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CanlendarCrop> partialUpdateCanlendarCrop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CanlendarCrop canlendarCrop
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CanlendarCrop partially : {}, {}", id, canlendarCrop);
        if (canlendarCrop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canlendarCrop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canlendarCropRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CanlendarCrop> result = canlendarCropService.partialUpdate(canlendarCrop);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canlendarCrop.getId().toString())
        );
    }

    /**
     * {@code GET  /canlendar-crops} : get all the canlendarCrops.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of canlendarCrops in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CanlendarCrop>> getAllCanlendarCrops(
        CanlendarCropCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CanlendarCrops by criteria: {}", criteria);

        Page<CanlendarCrop> page = canlendarCropQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /canlendar-crops/count} : count all the canlendarCrops.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCanlendarCrops(CanlendarCropCriteria criteria) {
        LOG.debug("REST request to count CanlendarCrops by criteria: {}", criteria);
        return ResponseEntity.ok().body(canlendarCropQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /canlendar-crops/:id} : get the "id" canlendarCrop.
     *
     * @param id the id of the canlendarCrop to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the canlendarCrop, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CanlendarCrop> getCanlendarCrop(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CanlendarCrop : {}", id);
        Optional<CanlendarCrop> canlendarCrop = canlendarCropService.findOne(id);
        return ResponseUtil.wrapOrNotFound(canlendarCrop);
    }

    /**
     * {@code DELETE  /canlendar-crops/:id} : delete the "id" canlendarCrop.
     *
     * @param id the id of the canlendarCrop to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCanlendarCrop(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CanlendarCrop : {}", id);
        canlendarCropService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
