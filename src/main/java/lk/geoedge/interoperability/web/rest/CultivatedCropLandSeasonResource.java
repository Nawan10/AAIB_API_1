package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedCropLandSeason;
import lk.geoedge.interoperability.repository.CultivatedCropLandSeasonRepository;
import lk.geoedge.interoperability.service.CultivatedCropLandSeasonQueryService;
import lk.geoedge.interoperability.service.CultivatedCropLandSeasonService;
import lk.geoedge.interoperability.service.criteria.CultivatedCropLandSeasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedCropLandSeason}.
 */
@RestController
@RequestMapping("/api/cultivated-crop-land-seasons")
public class CultivatedCropLandSeasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropLandSeasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedCropLandSeason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedCropLandSeasonService cultivatedCropLandSeasonService;

    private final CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository;

    private final CultivatedCropLandSeasonQueryService cultivatedCropLandSeasonQueryService;

    public CultivatedCropLandSeasonResource(
        CultivatedCropLandSeasonService cultivatedCropLandSeasonService,
        CultivatedCropLandSeasonRepository cultivatedCropLandSeasonRepository,
        CultivatedCropLandSeasonQueryService cultivatedCropLandSeasonQueryService
    ) {
        this.cultivatedCropLandSeasonService = cultivatedCropLandSeasonService;
        this.cultivatedCropLandSeasonRepository = cultivatedCropLandSeasonRepository;
        this.cultivatedCropLandSeasonQueryService = cultivatedCropLandSeasonQueryService;
    }

    /**
     * {@code POST  /cultivated-crop-land-seasons} : Create a new cultivatedCropLandSeason.
     *
     * @param cultivatedCropLandSeason the cultivatedCropLandSeason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedCropLandSeason, or with status {@code 400 (Bad Request)} if the cultivatedCropLandSeason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedCropLandSeason> createCultivatedCropLandSeason(
        @RequestBody CultivatedCropLandSeason cultivatedCropLandSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedCropLandSeason : {}", cultivatedCropLandSeason);
        if (cultivatedCropLandSeason.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedCropLandSeason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedCropLandSeason = cultivatedCropLandSeasonService.save(cultivatedCropLandSeason);
        return ResponseEntity.created(new URI("/api/cultivated-crop-land-seasons/" + cultivatedCropLandSeason.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedCropLandSeason.getId().toString()))
            .body(cultivatedCropLandSeason);
    }

    /**
     * {@code PUT  /cultivated-crop-land-seasons/:id} : Updates an existing cultivatedCropLandSeason.
     *
     * @param id the id of the cultivatedCropLandSeason to save.
     * @param cultivatedCropLandSeason the cultivatedCropLandSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCropLandSeason,
     * or with status {@code 400 (Bad Request)} if the cultivatedCropLandSeason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCropLandSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedCropLandSeason> updateCultivatedCropLandSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCropLandSeason cultivatedCropLandSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedCropLandSeason : {}, {}", id, cultivatedCropLandSeason);
        if (cultivatedCropLandSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCropLandSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropLandSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedCropLandSeason = cultivatedCropLandSeasonService.update(cultivatedCropLandSeason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCropLandSeason.getId().toString()))
            .body(cultivatedCropLandSeason);
    }

    /**
     * {@code PATCH  /cultivated-crop-land-seasons/:id} : Partial updates given fields of an existing cultivatedCropLandSeason, field will ignore if it is null
     *
     * @param id the id of the cultivatedCropLandSeason to save.
     * @param cultivatedCropLandSeason the cultivatedCropLandSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedCropLandSeason,
     * or with status {@code 400 (Bad Request)} if the cultivatedCropLandSeason is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedCropLandSeason is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedCropLandSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedCropLandSeason> partialUpdateCultivatedCropLandSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedCropLandSeason cultivatedCropLandSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedCropLandSeason partially : {}, {}", id, cultivatedCropLandSeason);
        if (cultivatedCropLandSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedCropLandSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedCropLandSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedCropLandSeason> result = cultivatedCropLandSeasonService.partialUpdate(cultivatedCropLandSeason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedCropLandSeason.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-crop-land-seasons} : get all the cultivatedCropLandSeasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedCropLandSeasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedCropLandSeason>> getAllCultivatedCropLandSeasons(
        CultivatedCropLandSeasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedCropLandSeasons by criteria: {}", criteria);

        Page<CultivatedCropLandSeason> page = cultivatedCropLandSeasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-crop-land-seasons/count} : count all the cultivatedCropLandSeasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedCropLandSeasons(CultivatedCropLandSeasonCriteria criteria) {
        LOG.debug("REST request to count CultivatedCropLandSeasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedCropLandSeasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-crop-land-seasons/:id} : get the "id" cultivatedCropLandSeason.
     *
     * @param id the id of the cultivatedCropLandSeason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedCropLandSeason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedCropLandSeason> getCultivatedCropLandSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedCropLandSeason : {}", id);
        Optional<CultivatedCropLandSeason> cultivatedCropLandSeason = cultivatedCropLandSeasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedCropLandSeason);
    }

    /**
     * {@code DELETE  /cultivated-crop-land-seasons/:id} : delete the "id" cultivatedCropLandSeason.
     *
     * @param id the id of the cultivatedCropLandSeason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedCropLandSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedCropLandSeason : {}", id);
        cultivatedCropLandSeasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
