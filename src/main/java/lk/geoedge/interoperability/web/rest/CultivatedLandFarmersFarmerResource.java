package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersFarmerRepository;
import lk.geoedge.interoperability.service.CultivatedLandFarmersFarmerQueryService;
import lk.geoedge.interoperability.service.CultivatedLandFarmersFarmerService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmersFarmerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer}.
 */
@RestController
@RequestMapping("/api/cultivated-land-farmers-farmers")
public class CultivatedLandFarmersFarmerResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmersFarmerResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandFarmersFarmer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandFarmersFarmerService cultivatedLandFarmersFarmerService;

    private final CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository;

    private final CultivatedLandFarmersFarmerQueryService cultivatedLandFarmersFarmerQueryService;

    public CultivatedLandFarmersFarmerResource(
        CultivatedLandFarmersFarmerService cultivatedLandFarmersFarmerService,
        CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository,
        CultivatedLandFarmersFarmerQueryService cultivatedLandFarmersFarmerQueryService
    ) {
        this.cultivatedLandFarmersFarmerService = cultivatedLandFarmersFarmerService;
        this.cultivatedLandFarmersFarmerRepository = cultivatedLandFarmersFarmerRepository;
        this.cultivatedLandFarmersFarmerQueryService = cultivatedLandFarmersFarmerQueryService;
    }

    /**
     * {@code POST  /cultivated-land-farmers-farmers} : Create a new cultivatedLandFarmersFarmer.
     *
     * @param cultivatedLandFarmersFarmer the cultivatedLandFarmersFarmer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandFarmersFarmer, or with status {@code 400 (Bad Request)} if the cultivatedLandFarmersFarmer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandFarmersFarmer> createCultivatedLandFarmersFarmer(
        @RequestBody CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandFarmersFarmer : {}", cultivatedLandFarmersFarmer);
        if (cultivatedLandFarmersFarmer.getId() != null) {
            throw new BadRequestAlertException("A new cultivatedLandFarmersFarmer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerService.save(cultivatedLandFarmersFarmer);
        return ResponseEntity.created(new URI("/api/cultivated-land-farmers-farmers/" + cultivatedLandFarmersFarmer.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmersFarmer.getId().toString())
            )
            .body(cultivatedLandFarmersFarmer);
    }

    /**
     * {@code PUT  /cultivated-land-farmers-farmers/:id} : Updates an existing cultivatedLandFarmersFarmer.
     *
     * @param id the id of the cultivatedLandFarmersFarmer to save.
     * @param cultivatedLandFarmersFarmer the cultivatedLandFarmersFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmersFarmer,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmersFarmer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmersFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmersFarmer> updateCultivatedLandFarmersFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandFarmersFarmer : {}, {}", id, cultivatedLandFarmersFarmer);
        if (cultivatedLandFarmersFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmersFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmersFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerService.update(cultivatedLandFarmersFarmer);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmersFarmer.getId().toString())
            )
            .body(cultivatedLandFarmersFarmer);
    }

    /**
     * {@code PATCH  /cultivated-land-farmers-farmers/:id} : Partial updates given fields of an existing cultivatedLandFarmersFarmer, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandFarmersFarmer to save.
     * @param cultivatedLandFarmersFarmer the cultivatedLandFarmersFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandFarmersFarmer,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandFarmersFarmer is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandFarmersFarmer is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandFarmersFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandFarmersFarmer> partialUpdateCultivatedLandFarmersFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CultivatedLandFarmersFarmer partially : {}, {}", id, cultivatedLandFarmersFarmer);
        if (cultivatedLandFarmersFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandFarmersFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandFarmersFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandFarmersFarmer> result = cultivatedLandFarmersFarmerService.partialUpdate(cultivatedLandFarmersFarmer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cultivatedLandFarmersFarmer.getId().toString())
        );
    }

    /**
     * {@code GET  /cultivated-land-farmers-farmers} : get all the cultivatedLandFarmersFarmers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandFarmersFarmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandFarmersFarmer>> getAllCultivatedLandFarmersFarmers(
        CultivatedLandFarmersFarmerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandFarmersFarmers by criteria: {}", criteria);

        Page<CultivatedLandFarmersFarmer> page = cultivatedLandFarmersFarmerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-farmers-farmers/count} : count all the cultivatedLandFarmersFarmers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandFarmersFarmers(CultivatedLandFarmersFarmerCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandFarmersFarmers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandFarmersFarmerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-farmers-farmers/:id} : get the "id" cultivatedLandFarmersFarmer.
     *
     * @param id the id of the cultivatedLandFarmersFarmer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandFarmersFarmer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandFarmersFarmer> getCultivatedLandFarmersFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CultivatedLandFarmersFarmer : {}", id);
        Optional<CultivatedLandFarmersFarmer> cultivatedLandFarmersFarmer = cultivatedLandFarmersFarmerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandFarmersFarmer);
    }

    /**
     * {@code DELETE  /cultivated-land-farmers-farmers/:id} : delete the "id" cultivatedLandFarmersFarmer.
     *
     * @param id the id of the cultivatedLandFarmersFarmer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandFarmersFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandFarmersFarmer : {}", id);
        cultivatedLandFarmersFarmerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
