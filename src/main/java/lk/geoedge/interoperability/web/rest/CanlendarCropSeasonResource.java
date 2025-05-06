package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CanlendarCropSeason;
import lk.geoedge.interoperability.repository.CanlendarCropSeasonRepository;
import lk.geoedge.interoperability.service.CanlendarCropSeasonQueryService;
import lk.geoedge.interoperability.service.CanlendarCropSeasonService;
import lk.geoedge.interoperability.service.criteria.CanlendarCropSeasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CanlendarCropSeason}.
 */
@RestController
@RequestMapping("/api/canlendar-crop-seasons")
public class CanlendarCropSeasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropSeasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1CanlendarCropSeason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CanlendarCropSeasonService canlendarCropSeasonService;

    private final CanlendarCropSeasonRepository canlendarCropSeasonRepository;

    private final CanlendarCropSeasonQueryService canlendarCropSeasonQueryService;

    public CanlendarCropSeasonResource(
        CanlendarCropSeasonService canlendarCropSeasonService,
        CanlendarCropSeasonRepository canlendarCropSeasonRepository,
        CanlendarCropSeasonQueryService canlendarCropSeasonQueryService
    ) {
        this.canlendarCropSeasonService = canlendarCropSeasonService;
        this.canlendarCropSeasonRepository = canlendarCropSeasonRepository;
        this.canlendarCropSeasonQueryService = canlendarCropSeasonQueryService;
    }

    /**
     * {@code POST  /canlendar-crop-seasons} : Create a new canlendarCropSeason.
     *
     * @param canlendarCropSeason the canlendarCropSeason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new canlendarCropSeason, or with status {@code 400 (Bad Request)} if the canlendarCropSeason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CanlendarCropSeason> createCanlendarCropSeason(@RequestBody CanlendarCropSeason canlendarCropSeason)
        throws URISyntaxException {
        LOG.debug("REST request to save CanlendarCropSeason : {}", canlendarCropSeason);
        if (canlendarCropSeason.getId() != null) {
            throw new BadRequestAlertException("A new canlendarCropSeason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        canlendarCropSeason = canlendarCropSeasonService.save(canlendarCropSeason);
        return ResponseEntity.created(new URI("/api/canlendar-crop-seasons/" + canlendarCropSeason.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, canlendarCropSeason.getId().toString()))
            .body(canlendarCropSeason);
    }

    /**
     * {@code PUT  /canlendar-crop-seasons/:id} : Updates an existing canlendarCropSeason.
     *
     * @param id the id of the canlendarCropSeason to save.
     * @param canlendarCropSeason the canlendarCropSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canlendarCropSeason,
     * or with status {@code 400 (Bad Request)} if the canlendarCropSeason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the canlendarCropSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CanlendarCropSeason> updateCanlendarCropSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CanlendarCropSeason canlendarCropSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to update CanlendarCropSeason : {}, {}", id, canlendarCropSeason);
        if (canlendarCropSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canlendarCropSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canlendarCropSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        canlendarCropSeason = canlendarCropSeasonService.update(canlendarCropSeason);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canlendarCropSeason.getId().toString()))
            .body(canlendarCropSeason);
    }

    /**
     * {@code PATCH  /canlendar-crop-seasons/:id} : Partial updates given fields of an existing canlendarCropSeason, field will ignore if it is null
     *
     * @param id the id of the canlendarCropSeason to save.
     * @param canlendarCropSeason the canlendarCropSeason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated canlendarCropSeason,
     * or with status {@code 400 (Bad Request)} if the canlendarCropSeason is not valid,
     * or with status {@code 404 (Not Found)} if the canlendarCropSeason is not found,
     * or with status {@code 500 (Internal Server Error)} if the canlendarCropSeason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CanlendarCropSeason> partialUpdateCanlendarCropSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CanlendarCropSeason canlendarCropSeason
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CanlendarCropSeason partially : {}, {}", id, canlendarCropSeason);
        if (canlendarCropSeason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, canlendarCropSeason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!canlendarCropSeasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CanlendarCropSeason> result = canlendarCropSeasonService.partialUpdate(canlendarCropSeason);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, canlendarCropSeason.getId().toString())
        );
    }

    /**
     * {@code GET  /canlendar-crop-seasons} : get all the canlendarCropSeasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of canlendarCropSeasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CanlendarCropSeason>> getAllCanlendarCropSeasons(
        CanlendarCropSeasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CanlendarCropSeasons by criteria: {}", criteria);

        Page<CanlendarCropSeason> page = canlendarCropSeasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /canlendar-crop-seasons/count} : count all the canlendarCropSeasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCanlendarCropSeasons(CanlendarCropSeasonCriteria criteria) {
        LOG.debug("REST request to count CanlendarCropSeasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(canlendarCropSeasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /canlendar-crop-seasons/:id} : get the "id" canlendarCropSeason.
     *
     * @param id the id of the canlendarCropSeason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the canlendarCropSeason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CanlendarCropSeason> getCanlendarCropSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CanlendarCropSeason : {}", id);
        Optional<CanlendarCropSeason> canlendarCropSeason = canlendarCropSeasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(canlendarCropSeason);
    }

    /**
     * {@code DELETE  /canlendar-crop-seasons/:id} : delete the "id" canlendarCropSeason.
     *
     * @param id the id of the canlendarCropSeason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCanlendarCropSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CanlendarCropSeason : {}", id);
        canlendarCropSeasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
