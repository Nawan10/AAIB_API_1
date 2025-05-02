package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandSeason;
import lk.geoedge.interoperability.repository.CultivatedLandSeasonRepository;
import lk.geoedge.interoperability.service.CultivatedLandSeasonQueryService;
import lk.geoedge.interoperability.service.CultivatedLandSeasonService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandSeasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandSeason}.
 */
@RestController
@RequestMapping("/api/cultivated-land-seasons")
public class CultivatedLandSeasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandSeasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandSeason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandSeasonService cultivatedLandSeasonService;

    private final CultivatedLandSeasonRepository cultivatedLandSeasonRepository;

    private final CultivatedLandSeasonQueryService cultivatedLandSeasonQueryService;

    public CultivatedLandSeasonResource(
        CultivatedLandSeasonService cultivatedLandSeasonService,
        CultivatedLandSeasonRepository cultivatedLandSeasonRepository,
        CultivatedLandSeasonQueryService cultivatedLandSeasonQueryService
    ) {
        this.cultivatedLandSeasonService = cultivatedLandSeasonService;
        this.cultivatedLandSeasonRepository = cultivatedLandSeasonRepository;
        this.cultivatedLandSeasonQueryService = cultivatedLandSeasonQueryService;
    }

    /**
     * {@code POST  /cultivated-land-seasons} : Create a new cultivatedLandSeason.
     *
     * @param cultivatedLandSeason the cultivatedLandSeason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandSeason, or with status {@code 400 (Bad Request)} if the cultivatedLandSeason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandSeason> createCultivatedLandSeason(@RequestBody CultivatedLandSeason cultivatedLandSeason)
        throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandSeason : {}", cultivatedLandSeason);
        if (cultivatedLandSeason.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandSeason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandSeason = cultivatedLandSeasonService.save(cultivatedLandSeason);
        return ResponseEntity.created(new URI("/api/cultivated-land-seasons/" + cultivatedLandSeason.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandSeason.getId().toString()))
            .body(cultivatedLandSeason);
    }

    /**
     * {@code PUT  /cultivated-land-seasons/:id} : Updates an existing cultivatedLandSeason.
     *
     * @param id the id of the cultivatedLandSeason to save.
     * @param cultivatedLandSeason the cultivatedLandSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandSeason,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandSeason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandSeason> updateCultivatedLandSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandSeason cultivatedLandSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandSeason : {}, {}", id, cultivatedLandSeason);
        if (cultivatedLandSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandSeason = cultivatedLandSeasonService.update(cultivatedLandSeason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandSeason.getId().toString()))
            .body(cultivatedLandSeason);
    }

    /**
     * {@code PATCH  /cultivated-land-seasons/:id} : Partial updates given fields of an existing cultivatedLandSeason, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandSeason to save.
     * @param cultivatedLandSeason the cultivatedLandSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandSeason,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandSeason is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandSeason is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandSeason> partialUpdateCultivatedLandSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandSeason cultivatedLandSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandSeason partially : {}, {}", id, cultivatedLandSeason);
        if (cultivatedLandSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandSeason> result = cultivatedLandSeasonService.partialUpdate(cultivatedLandSeason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandSeason.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-seasons} : get all the cultivatedLandSeasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandSeasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandSeason>> getAllCultivatedLandSeasons(
        CultivatedLandSeasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandSeasons by criteria: {}", criteria);

        Page<CultivatedLandSeason> page = cultivatedLandSeasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-seasons/count} : count all the cultivatedLandSeasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandSeasons(CultivatedLandSeasonCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandSeasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandSeasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-seasons/:id} : get the "id" cultivatedLandSeason.
     *
     * @param id the id of the cultivatedLandSeason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandSeason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandSeason> getCultivatedLandSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandSeason : {}", id);
        Optional<CultivatedLandSeason> cultivatedLandSeason = cultivatedLandSeasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandSeason);
    }

    /**
     * {@code DELETE  /cultivated-land-seasons/:id} : delete the "id" cultivatedLandSeason.
     *
     * @param id the id of the cultivatedLandSeason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandSeason : {}", id);
        cultivatedLandSeasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
