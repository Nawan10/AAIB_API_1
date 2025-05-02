package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedCropCultivatedLandRepository;
import lk.geoedge.interoperability.service.CultivatedCropCultivatedLandQueryService;
import lk.geoedge.interoperability.service.CultivatedCropCultivatedLandService;
import lk.geoedge.interoperability.service.criteria.CultivatedCropCultivatedLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand}.
 */
@RestController
@RequestMapping("/api/cultivated-crop-cultivated-lands")
public class CultivatedCropCultivatedLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropCultivatedLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedCropCultivatedLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedCropCultivatedLandService cultivatedCropCultivatedLandService;

    private final CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository;

    private final CultivatedCropCultivatedLandQueryService cultivatedCropCultivatedLandQueryService;

    public CultivatedCropCultivatedLandResource(
        CultivatedCropCultivatedLandService cultivatedCropCultivatedLandService,
        CultivatedCropCultivatedLandRepository cultivatedCropCultivatedLandRepository,
        CultivatedCropCultivatedLandQueryService cultivatedCropCultivatedLandQueryService
    ) {
        this.cultivatedCropCultivatedLandService = cultivatedCropCultivatedLandService;
        this.cultivatedCropCultivatedLandRepository = cultivatedCropCultivatedLandRepository;
        this.cultivatedCropCultivatedLandQueryService = cultivatedCropCultivatedLandQueryService;
    }

    /**
     * {@code POST  /cultivated-crop-cultivated-lands} : Create a new cultivatedCropCultivatedLand.
     *
     * @param cultivatedCropCultivatedLand the cultivatedCropCultivatedLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedCropCultivatedLand, or with status {@code 400 (Bad Request)} if the cultivatedCropCultivatedLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedCropCultivatedLand> createCultivatedCropCultivatedLand(
        @RequestBody CultivatedCropCultivatedLand cultivatedCropCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedCropCultivatedLand : {}", cultivatedCropCultivatedLand);
        if (cultivatedCropCultivatedLand.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedCropCultivatedLand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedCropCultivatedLand = cultivatedCropCultivatedLandService.save(cultivatedCropCultivatedLand);
        return ResponseEntity.created(new URI("/api/cultivated-crop-cultivated-lands/" + cultivatedCropCultivatedLand.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedCropCultivatedLand.getId().toString())
            )
            .body(cultivatedCropCultivatedLand);
    }

    /**
     * {@code PUT  /cultivated-crop-cultivated-lands/:id} : Updates an existing cultivatedCropCultivatedLand.
     *
     * @param id the id of the cultivatedCropCultivatedLand to save.
     * @param cultivatedCropCultivatedLand the cultivatedCropCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCropCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the cultivatedCropCultivatedLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCropCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedCropCultivatedLand> updateCultivatedCropCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCropCultivatedLand cultivatedCropCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedCropCultivatedLand : {}, {}", id, cultivatedCropCultivatedLand);
        if (cultivatedCropCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCropCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedCropCultivatedLand = cultivatedCropCultivatedLandService.update(cultivatedCropCultivatedLand);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCropCultivatedLand.getId().toString())
            )
            .body(cultivatedCropCultivatedLand);
    }

    /**
     * {@code PATCH  /cultivated-crop-cultivated-lands/:id} : Partial updates given fields of an existing cultivatedCropCultivatedLand, field will ignore if it is null
     *
     * @param id the id of the cultivatedCropCultivatedLand to save.
     * @param cultivatedCropCultivatedLand the cultivatedCropCultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCropCultivatedLand,
     * or with status {@code 400 (Bad Request)} if the cultivatedCropCultivatedLand is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedCropCultivatedLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCropCultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedCropCultivatedLand> partialUpdateCultivatedCropCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCropCultivatedLand cultivatedCropCultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedCropCultivatedLand partially : {}, {}", id, cultivatedCropCultivatedLand);
        if (cultivatedCropCultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCropCultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropCultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedCropCultivatedLand> result = cultivatedCropCultivatedLandService.partialUpdate(cultivatedCropCultivatedLand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCropCultivatedLand.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-crop-cultivated-lands} : get all the cultivatedCropCultivatedLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedCropCultivatedLands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedCropCultivatedLand>> getAllCultivatedCropCultivatedLands(
        CultivatedCropCultivatedLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedCropCultivatedLands by criteria: {}", criteria);

        Page<CultivatedCropCultivatedLand> page = cultivatedCropCultivatedLandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-crop-cultivated-lands/count} : count all the cultivatedCropCultivatedLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedCropCultivatedLands(CultivatedCropCultivatedLandCriteria criteria) {
        LOG.debug("REST request to count CultivatedCropCultivatedLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedCropCultivatedLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-crop-cultivated-lands/:id} : get the "id" cultivatedCropCultivatedLand.
     *
     * @param id the id of the cultivatedCropCultivatedLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedCropCultivatedLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedCropCultivatedLand> getCultivatedCropCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedCropCultivatedLand : {}", id);
        Optional<CultivatedCropCultivatedLand> cultivatedCropCultivatedLand = cultivatedCropCultivatedLandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedCropCultivatedLand);
    }

    /**
     * {@code DELETE  /cultivated-crop-cultivated-lands/:id} : delete the "id" cultivatedCropCultivatedLand.
     *
     * @param id the id of the cultivatedCropCultivatedLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedCropCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedCropCultivatedLand : {}", id);
        cultivatedCropCultivatedLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
