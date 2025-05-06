package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyWeatherStation;
import lk.geoedge.interoperability.repository.IndexPolicyWeatherStationRepository;
import lk.geoedge.interoperability.service.IndexPolicyWeatherStationQueryService;
import lk.geoedge.interoperability.service.IndexPolicyWeatherStationService;
import lk.geoedge.interoperability.service.criteria.IndexPolicyWeatherStationCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.IndexPolicyWeatherStation}.
 */
@RestController
@RequestMapping("/api/index-policy-weather-stations")
public class IndexPolicyWeatherStationResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyWeatherStationResource.class);

    private static final String ENTITY_NAME = "aaibapi1IndexPolicyWeatherStation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexPolicyWeatherStationService indexPolicyWeatherStationService;

    private final IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository;

    private final IndexPolicyWeatherStationQueryService indexPolicyWeatherStationQueryService;

    public IndexPolicyWeatherStationResource(
        IndexPolicyWeatherStationService indexPolicyWeatherStationService,
        IndexPolicyWeatherStationRepository indexPolicyWeatherStationRepository,
        IndexPolicyWeatherStationQueryService indexPolicyWeatherStationQueryService
    ) {
        this.indexPolicyWeatherStationService = indexPolicyWeatherStationService;
        this.indexPolicyWeatherStationRepository = indexPolicyWeatherStationRepository;
        this.indexPolicyWeatherStationQueryService = indexPolicyWeatherStationQueryService;
    }

    /**
     * {@code POST  /index-policy-weather-stations} : Create a new indexPolicyWeatherStation.
     *
     * @param indexPolicyWeatherStation the indexPolicyWeatherStation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexPolicyWeatherStation, or with status {@code 400 (Bad Request)} if the indexPolicyWeatherStation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndexPolicyWeatherStation> createIndexPolicyWeatherStation(
        @RequestBody IndexPolicyWeatherStation indexPolicyWeatherStation
    ) throws URISyntaxException {
        LOG.debug("REST request to save IndexPolicyWeatherStation : {}", indexPolicyWeatherStation);
        if (indexPolicyWeatherStation.getId() != null) {
            throw new BadRequestAlertException("A new indexPolicyWeatherStation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        indexPolicyWeatherStation = indexPolicyWeatherStationService.save(indexPolicyWeatherStation);
        return ResponseEntity.created(new URI("/api/index-policy-weather-stations/" + indexPolicyWeatherStation.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, indexPolicyWeatherStation.getId().toString())
            )
            .body(indexPolicyWeatherStation);
    }

    /**
     * {@code PUT  /index-policy-weather-stations/:id} : Updates an existing indexPolicyWeatherStation.
     *
     * @param id the id of the indexPolicyWeatherStation to save.
     * @param indexPolicyWeatherStation the indexPolicyWeatherStation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyWeatherStation,
     * or with status {@code 400 (Bad Request)} if the indexPolicyWeatherStation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyWeatherStation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndexPolicyWeatherStation> updateIndexPolicyWeatherStation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyWeatherStation indexPolicyWeatherStation
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndexPolicyWeatherStation : {}, {}", id, indexPolicyWeatherStation);
        if (indexPolicyWeatherStation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyWeatherStation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyWeatherStationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        indexPolicyWeatherStation = indexPolicyWeatherStationService.update(indexPolicyWeatherStation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyWeatherStation.getId().toString()))
            .body(indexPolicyWeatherStation);
    }

    /**
     * {@code PATCH  /index-policy-weather-stations/:id} : Partial updates given fields of an existing indexPolicyWeatherStation, field will ignore if it is null
     *
     * @param id the id of the indexPolicyWeatherStation to save.
     * @param indexPolicyWeatherStation the indexPolicyWeatherStation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexPolicyWeatherStation,
     * or with status {@code 400 (Bad Request)} if the indexPolicyWeatherStation is not valid,
     * or with status {@code 404 (Not Found)} if the indexPolicyWeatherStation is not found,
     * or with status {@code 500 (Internal Server Error)} if the indexPolicyWeatherStation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndexPolicyWeatherStation> partialUpdateIndexPolicyWeatherStation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IndexPolicyWeatherStation indexPolicyWeatherStation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndexPolicyWeatherStation partially : {}, {}", id, indexPolicyWeatherStation);
        if (indexPolicyWeatherStation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, indexPolicyWeatherStation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!indexPolicyWeatherStationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndexPolicyWeatherStation> result = indexPolicyWeatherStationService.partialUpdate(indexPolicyWeatherStation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indexPolicyWeatherStation.getId().toString())
        );
    }

    /**
     * {@code GET  /index-policy-weather-stations} : get all the indexPolicyWeatherStations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexPolicyWeatherStations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndexPolicyWeatherStation>> getAllIndexPolicyWeatherStations(
        IndexPolicyWeatherStationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get IndexPolicyWeatherStations by criteria: {}", criteria);

        Page<IndexPolicyWeatherStation> page = indexPolicyWeatherStationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-policy-weather-stations/count} : count all the indexPolicyWeatherStations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndexPolicyWeatherStations(IndexPolicyWeatherStationCriteria criteria) {
        LOG.debug("REST request to count IndexPolicyWeatherStations by criteria: {}", criteria);
        return ResponseEntity.ok().body(indexPolicyWeatherStationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /index-policy-weather-stations/:id} : get the "id" indexPolicyWeatherStation.
     *
     * @param id the id of the indexPolicyWeatherStation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexPolicyWeatherStation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndexPolicyWeatherStation> getIndexPolicyWeatherStation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndexPolicyWeatherStation : {}", id);
        Optional<IndexPolicyWeatherStation> indexPolicyWeatherStation = indexPolicyWeatherStationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexPolicyWeatherStation);
    }

    /**
     * {@code DELETE  /index-policy-weather-stations/:id} : delete the "id" indexPolicyWeatherStation.
     *
     * @param id the id of the indexPolicyWeatherStation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexPolicyWeatherStation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndexPolicyWeatherStation : {}", id);
        indexPolicyWeatherStationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
