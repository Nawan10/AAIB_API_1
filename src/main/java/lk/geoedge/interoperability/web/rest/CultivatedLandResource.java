package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLand;
import lk.geoedge.interoperability.repository.CultivatedLandRepository;
import lk.geoedge.interoperability.service.CultivatedLandQueryService;
import lk.geoedge.interoperability.service.CultivatedLandService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLand}.
 */
@RestController
@RequestMapping("/api/cultivated-lands")
public class CultivatedLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandService cultivatedLandService;

    private final CultivatedLandRepository cultivatedLandRepository;

    private final CultivatedLandQueryService cultivatedLandQueryService;

    public CultivatedLandResource(
        CultivatedLandService cultivatedLandService,
        CultivatedLandRepository cultivatedLandRepository,
        CultivatedLandQueryService cultivatedLandQueryService
    ) {
        this.cultivatedLandService = cultivatedLandService;
        this.cultivatedLandRepository = cultivatedLandRepository;
        this.cultivatedLandQueryService = cultivatedLandQueryService;
    }

    /**
     * {@code POST  /cultivated-lands} : Create a new cultivatedLand.
     *
     * @param cultivatedLand the cultivatedLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLand, or with status {@code 400 (Bad Request)} if the cultivatedLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLand> createCultivatedLand(@RequestBody CultivatedLand cultivatedLand) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLand : {}", cultivatedLand);
        if (cultivatedLand.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLand = cultivatedLandService.save(cultivatedLand);
        return ResponseEntity.created(new URI("/api/cultivated-lands/" + cultivatedLand.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLand.getId().toString()))
            .body(cultivatedLand);
    }

    /**
     * {@code PUT  /cultivated-lands/:id} : Updates an existing cultivatedLand.
     *
     * @param id the id of the cultivatedLand to save.
     * @param cultivatedLand the cultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLand,
     * or with status {@code 400 (Bad Request)} if the cultivatedLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLand> updateCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLand cultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLand : {}, {}", id, cultivatedLand);
        if (cultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLand = cultivatedLandService.update(cultivatedLand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLand.getId().toString()))
            .body(cultivatedLand);
    }

    /**
     * {@code PATCH  /cultivated-lands/:id} : Partial updates given fields of an existing cultivatedLand, field will ignore if it is null
     *
     * @param id the id of the cultivatedLand to save.
     * @param cultivatedLand the cultivatedLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLand,
     * or with status {@code 400 (Bad Request)} if the cultivatedLand is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLand> partialUpdateCultivatedLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLand cultivatedLand
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLand partially : {}, {}", id, cultivatedLand);
        if (cultivatedLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLand> result = cultivatedLandService.partialUpdate(cultivatedLand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLand.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-lands} : get all the cultivatedLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLand>> getAllCultivatedLands(
        CultivatedLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLands by criteria: {}", criteria);

        Page<CultivatedLand> page = cultivatedLandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-lands/count} : count all the cultivatedLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLands(CultivatedLandCriteria criteria) {
        LOG.debug("REST request to count CultivatedLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-lands/:id} : get the "id" cultivatedLand.
     *
     * @param id the id of the cultivatedLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLand> getCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLand : {}", id);
        Optional<CultivatedLand> cultivatedLand = cultivatedLandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLand);
    }

    /**
     * {@code DELETE  /cultivated-lands/:id} : delete the "id" cultivatedLand.
     *
     * @param id the id of the cultivatedLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLand : {}", id);
        cultivatedLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
