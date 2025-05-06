package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmerLand;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerLandRepository;
import lk.geoedge.interoperability.service.CultivatedLandFarmerLandQueryService;
import lk.geoedge.interoperability.service.CultivatedLandFarmerLandService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmerLandCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmerLand}.
 */
@RestController
@RequestMapping("/api/cultivated-land-farmer-lands")
public class CultivatedLandFarmerLandResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmerLandResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandFarmerLand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandFarmerLandService cultivatedLandFarmerLandService;

    private final CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository;

    private final CultivatedLandFarmerLandQueryService cultivatedLandFarmerLandQueryService;

    public CultivatedLandFarmerLandResource(
        CultivatedLandFarmerLandService cultivatedLandFarmerLandService,
        CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository,
        CultivatedLandFarmerLandQueryService cultivatedLandFarmerLandQueryService
    ) {
        this.cultivatedLandFarmerLandService = cultivatedLandFarmerLandService;
        this.cultivatedLandFarmerLandRepository = cultivatedLandFarmerLandRepository;
        this.cultivatedLandFarmerLandQueryService = cultivatedLandFarmerLandQueryService;
    }

    /**
     * {@code POST  /cultivated-land-farmer-lands} : Create a new cultivatedLandFarmerLand.
     *
     * @param cultivatedLandFarmerLand the cultivatedLandFarmerLand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandFarmerLand, or with status {@code 400 (Bad Request)} if the cultivatedLandFarmerLand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandFarmerLand> createCultivatedLandFarmerLand(
        @RequestBody CultivatedLandFarmerLand cultivatedLandFarmerLand
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandFarmerLand : {}", cultivatedLandFarmerLand);
        if (cultivatedLandFarmerLand.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandFarmerLand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandFarmerLand = cultivatedLandFarmerLandService.save(cultivatedLandFarmerLand);
        return ResponseEntity.created(new URI("/api/cultivated-land-farmer-lands/" + cultivatedLandFarmerLand.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmerLand.getId().toString()))
            .body(cultivatedLandFarmerLand);
    }

    /**
     * {@code PUT  /cultivated-land-farmer-lands/:id} : Updates an existing cultivatedLandFarmerLand.
     *
     * @param id the id of the cultivatedLandFarmerLand to save.
     * @param cultivatedLandFarmerLand the cultivatedLandFarmerLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmerLand,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmerLand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmerLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmerLand> updateCultivatedLandFarmerLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmerLand cultivatedLandFarmerLand
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandFarmerLand : {}, {}", id, cultivatedLandFarmerLand);
        if (cultivatedLandFarmerLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmerLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmerLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandFarmerLand = cultivatedLandFarmerLandService.update(cultivatedLandFarmerLand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmerLand.getId().toString()))
            .body(cultivatedLandFarmerLand);
    }

    /**
     * {@code PATCH  /cultivated-land-farmer-lands/:id} : Partial updates given fields of an existing cultivatedLandFarmerLand, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandFarmerLand to save.
     * @param cultivatedLandFarmerLand the cultivatedLandFarmerLand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmerLand,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmerLand is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandFarmerLand is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmerLand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandFarmerLand> partialUpdateCultivatedLandFarmerLand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmerLand cultivatedLandFarmerLand
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandFarmerLand partially : {}, {}", id, cultivatedLandFarmerLand);
        if (cultivatedLandFarmerLand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmerLand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmerLandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandFarmerLand> result = cultivatedLandFarmerLandService.partialUpdate(cultivatedLandFarmerLand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmerLand.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-farmer-lands} : get all the cultivatedLandFarmerLands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandFarmerLands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandFarmerLand>> getAllCultivatedLandFarmerLands(
        CultivatedLandFarmerLandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandFarmerLands by criteria: {}", criteria);

        Page<CultivatedLandFarmerLand> page = cultivatedLandFarmerLandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-farmer-lands/count} : count all the cultivatedLandFarmerLands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandFarmerLands(CultivatedLandFarmerLandCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandFarmerLands by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandFarmerLandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-farmer-lands/:id} : get the "id" cultivatedLandFarmerLand.
     *
     * @param id the id of the cultivatedLandFarmerLand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandFarmerLand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmerLand> getCultivatedLandFarmerLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandFarmerLand : {}", id);
        Optional<CultivatedLandFarmerLand> cultivatedLandFarmerLand = cultivatedLandFarmerLandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandFarmerLand);
    }

    /**
     * {@code DELETE  /cultivated-land-farmer-lands/:id} : delete the "id" cultivatedLandFarmerLand.
     *
     * @param id the id of the cultivatedLandFarmerLand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandFarmerLand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandFarmerLand : {}", id);
        cultivatedLandFarmerLandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
