package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.Season;
import lk.geoedge.interoperability.repository.SeasonRepository;
import lk.geoedge.interoperability.service.SeasonQueryService;
import lk.geoedge.interoperability.service.SeasonService;
import lk.geoedge.interoperability.service.criteria.SeasonCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.Season}.
 */
@RestController
@RequestMapping("/api/seasons")
public class SeasonResource {

    private static final Logger LOG = LoggerFactory.getLogger(SeasonResource.class);

    private static final String ENTITY_NAME = "aaibapi1Season";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeasonService seasonService;

    private final SeasonRepository seasonRepository;

    private final SeasonQueryService seasonQueryService;

    public SeasonResource(SeasonService seasonService, SeasonRepository seasonRepository, SeasonQueryService seasonQueryService) {
        this.seasonService = seasonService;
        this.seasonRepository = seasonRepository;
        this.seasonQueryService = seasonQueryService;
    }

    /**
     * {@code POST  /seasons} : Create a new season.
     *
     * @param season the season to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new season, or with status {@code 400 (Bad Request)} if the season has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Season> createSeason(@RequestBody Season season) throws URISyntaxException {
        LOG.debug("REST request to save Season : {}", season);
        if (season.getId() != null) {
            throw new BadRequestAlertException("A new season cannot already have an ID", ENTITY_NAME, "idexists");
        }
        season = seasonService.save(season);
        return ResponseEntity.created(new URI("/api/seasons/" + season.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, season.getId().toString()))
            .body(season);
    }

    /**
     * {@code PUT  /seasons/:id} : Updates an existing season.
     *
     * @param id the id of the season to save.
     * @param season the season to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated season,
     * or with status {@code 400 (Bad Request)} if the season is not valid,
     * or with status {@code 500 (Internal Server Error)} if the season couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Season> updateSeason(@PathVariable(value = "id", required = false) final Long id, @RequestBody Season season)
        throws URISyntaxException {
        LOG.debug("REST request to update Season : {}, {}", id, season);
        if (season.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, season.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        season = seasonService.update(season);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, season.getId().toString()))
            .body(season);
    }

    /**
     * {@code PATCH  /seasons/:id} : Partial updates given fields of an existing season, field will ignore if it is null
     *
     * @param id the id of the season to save.
     * @param season the season to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated season,
     * or with status {@code 400 (Bad Request)} if the season is not valid,
     * or with status {@code 404 (Not Found)} if the season is not found,
     * or with status {@code 500 (Internal Server Error)} if the season couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Season> partialUpdateSeason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Season season
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Season partially : {}, {}", id, season);
        if (season.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, season.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Season> result = seasonService.partialUpdate(season);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, season.getId().toString())
        );
    }

    /**
     * {@code GET  /seasons} : get all the seasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seasons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Season>> getAllSeasons(
        SeasonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Seasons by criteria: {}", criteria);

        Page<Season> page = seasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seasons/count} : count all the seasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSeasons(SeasonCriteria criteria) {
        LOG.debug("REST request to count Seasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(seasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seasons/:id} : get the "id" season.
     *
     * @param id the id of the season to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the season, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Season> getSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Season : {}", id);
        Optional<Season> season = seasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(season);
    }

    /**
     * {@code DELETE  /seasons/:id} : delete the "id" season.
     *
     * @param id the id of the season to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Season : {}", id);
        seasonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
