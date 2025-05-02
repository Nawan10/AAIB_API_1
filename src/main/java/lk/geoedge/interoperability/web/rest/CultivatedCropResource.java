package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCrop;
import lk.geoedge.interoperability.repository.CultivatedCropRepository;
import lk.geoedge.interoperability.service.CultivatedCropQueryService;
import lk.geoedge.interoperability.service.CultivatedCropService;
import lk.geoedge.interoperability.service.criteria.CultivatedCropCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedCrop}.
 */
@RestController
@RequestMapping("/api/cultivated-crops")
public class CultivatedCropResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedCrop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedCropService cultivatedCropService;

    private final CultivatedCropRepository cultivatedCropRepository;

    private final CultivatedCropQueryService cultivatedCropQueryService;

    public CultivatedCropResource(
        CultivatedCropService cultivatedCropService,
        CultivatedCropRepository cultivatedCropRepository,
        CultivatedCropQueryService cultivatedCropQueryService
    ) {
        this.cultivatedCropService = cultivatedCropService;
        this.cultivatedCropRepository = cultivatedCropRepository;
        this.cultivatedCropQueryService = cultivatedCropQueryService;
    }

    /**
     * {@code POST  /cultivated-crops} : Create a new cultivatedCrop.
     *
     * @param cultivatedCrop the cultivatedCrop to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedCrop, or with status {@code 400 (Bad Request)} if the cultivatedCrop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedCrop> createCultivatedCrop(@RequestBody CultivatedCrop cultivatedCrop) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedCrop : {}", cultivatedCrop);
        if (cultivatedCrop.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedCrop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedCrop = cultivatedCropService.save(cultivatedCrop);
        return ResponseEntity.created(new URI("/api/cultivated-crops/" + cultivatedCrop.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedCrop.getId().toString()))
            .body(cultivatedCrop);
    }

    /**
     * {@code PUT  /cultivated-crops/:id} : Updates an existing cultivatedCrop.
     *
     * @param id the id of the cultivatedCrop to save.
     * @param cultivatedCrop the cultivatedCrop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCrop,
     * or with status {@code 400 (Bad Request)} if the cultivatedCrop is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCrop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedCrop> updateCultivatedCrop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCrop cultivatedCrop
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedCrop : {}, {}", id, cultivatedCrop);
        if (cultivatedCrop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCrop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedCrop = cultivatedCropService.update(cultivatedCrop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCrop.getId().toString()))
            .body(cultivatedCrop);
    }

    /**
     * {@code PATCH  /cultivated-crops/:id} : Partial updates given fields of an existing cultivatedCrop, field will ignore if it is null
     *
     * @param id the id of the cultivatedCrop to save.
     * @param cultivatedCrop the cultivatedCrop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCrop,
     * or with status {@code 400 (Bad Request)} if the cultivatedCrop is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedCrop is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCrop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedCrop> partialUpdateCultivatedCrop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCrop cultivatedCrop
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedCrop partially : {}, {}", id, cultivatedCrop);
        if (cultivatedCrop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCrop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedCrop> result = cultivatedCropService.partialUpdate(cultivatedCrop);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCrop.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-crops} : get all the cultivatedCrops.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedCrops in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedCrop>> getAllCultivatedCrops(
        CultivatedCropCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedCrops by criteria: {}", criteria);

        Page<CultivatedCrop> page = cultivatedCropQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-crops/count} : count all the cultivatedCrops.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedCrops(CultivatedCropCriteria criteria) {
        LOG.debug("REST request to count CultivatedCrops by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedCropQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-crops/:id} : get the "id" cultivatedCrop.
     *
     * @param id the id of the cultivatedCrop to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedCrop, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedCrop> getCultivatedCrop(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedCrop : {}", id);
        Optional<CultivatedCrop> cultivatedCrop = cultivatedCropService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedCrop);
    }

    /**
     * {@code DELETE  /cultivated-crops/:id} : delete the "id" cultivatedCrop.
     *
     * @param id the id of the cultivatedCrop to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedCrop(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedCrop : {}", id);
        cultivatedCropService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
