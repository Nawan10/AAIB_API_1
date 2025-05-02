package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerFarmerRepository;
import lk.geoedge.interoperability.service.FarmerFieldLandOwnerFarmerQueryService;
import lk.geoedge.interoperability.service.FarmerFieldLandOwnerFarmerService;
import lk.geoedge.interoperability.service.criteria.FarmerFieldLandOwnerFarmerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer}.
 */
@RestController
@RequestMapping("/api/farmer-field-land-owner-farmers")
public class FarmerFieldLandOwnerFarmerResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldLandOwnerFarmerResource.class);

    private static final String ENTITY_NAME = "aaibapi1FarmerFieldLandOwnerFarmer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmerFieldLandOwnerFarmerService farmerFieldLandOwnerFarmerService;

    private final FarmerFieldLandOwnerFarmerRepository farmerFieldLandOwnerFarmerRepository;

    private final FarmerFieldLandOwnerFarmerQueryService farmerFieldLandOwnerFarmerQueryService;

    public FarmerFieldLandOwnerFarmerResource(
        FarmerFieldLandOwnerFarmerService farmerFieldLandOwnerFarmerService,
        FarmerFieldLandOwnerFarmerRepository farmerFieldLandOwnerFarmerRepository,
        FarmerFieldLandOwnerFarmerQueryService farmerFieldLandOwnerFarmerQueryService
    ) {
        this.farmerFieldLandOwnerFarmerService = farmerFieldLandOwnerFarmerService;
        this.farmerFieldLandOwnerFarmerRepository = farmerFieldLandOwnerFarmerRepository;
        this.farmerFieldLandOwnerFarmerQueryService = farmerFieldLandOwnerFarmerQueryService;
    }

    /**
     * {@code POST  /farmer-field-land-owner-farmers} : Create a new farmerFieldLandOwnerFarmer.
     *
     * @param farmerFieldLandOwnerFarmer the farmerFieldLandOwnerFarmer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmerFieldLandOwnerFarmer, or with status {@code 400 (Bad Request)} if the farmerFieldLandOwnerFarmer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmerFieldLandOwnerFarmer> createFarmerFieldLandOwnerFarmer(
        @RequestBody FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to save FarmerFieldLandOwnerFarmer : {}", farmerFieldLandOwnerFarmer);
        if (farmerFieldLandOwnerFarmer.getId() != null) {
            throw new BadRequestAlertException("A new farmerFieldLandOwnerFarmer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerService.save(farmerFieldLandOwnerFarmer);
        return ResponseEntity.created(new URI("/api/farmer-field-land-owner-farmers/" + farmerFieldLandOwnerFarmer.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmerFieldLandOwnerFarmer.getId().toString())
            )
            .body(farmerFieldLandOwnerFarmer);
    }

    /**
     * {@code PUT  /farmer-field-land-owner-farmers/:id} : Updates an existing farmerFieldLandOwnerFarmer.
     *
     * @param id the id of the farmerFieldLandOwnerFarmer to save.
     * @param farmerFieldLandOwnerFarmer the farmerFieldLandOwnerFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldLandOwnerFarmer,
     * or with status {@code 400 (Bad Request)} if the farmerFieldLandOwnerFarmer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldLandOwnerFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmerFieldLandOwnerFarmer> updateFarmerFieldLandOwnerFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to update FarmerFieldLandOwnerFarmer : {}, {}", id, farmerFieldLandOwnerFarmer);
        if (farmerFieldLandOwnerFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldLandOwnerFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldLandOwnerFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerService.update(farmerFieldLandOwnerFarmer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldLandOwnerFarmer.getId().toString()))
            .body(farmerFieldLandOwnerFarmer);
    }

    /**
     * {@code PATCH  /farmer-field-land-owner-farmers/:id} : Partial updates given fields of an existing farmerFieldLandOwnerFarmer, field will ignore if it is null
     *
     * @param id the id of the farmerFieldLandOwnerFarmer to save.
     * @param farmerFieldLandOwnerFarmer the farmerFieldLandOwnerFarmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldLandOwnerFarmer,
     * or with status {@code 400 (Bad Request)} if the farmerFieldLandOwnerFarmer is not valid,
     * or with status {@code 404 (Not Found)} if the farmerFieldLandOwnerFarmer is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldLandOwnerFarmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmerFieldLandOwnerFarmer> partialUpdateFarmerFieldLandOwnerFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FarmerFieldLandOwnerFarmer partially : {}, {}", id, farmerFieldLandOwnerFarmer);
        if (farmerFieldLandOwnerFarmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldLandOwnerFarmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldLandOwnerFarmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmerFieldLandOwnerFarmer> result = farmerFieldLandOwnerFarmerService.partialUpdate(farmerFieldLandOwnerFarmer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldLandOwnerFarmer.getId().toString())
        );
    }

    /**
     * {@code GET  /farmer-field-land-owner-farmers} : get all the farmerFieldLandOwnerFarmers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmerFieldLandOwnerFarmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmerFieldLandOwnerFarmer>> getAllFarmerFieldLandOwnerFarmers(
        FarmerFieldLandOwnerFarmerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FarmerFieldLandOwnerFarmers by criteria: {}", criteria);

        Page<FarmerFieldLandOwnerFarmer> page = farmerFieldLandOwnerFarmerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmer-field-land-owner-farmers/count} : count all the farmerFieldLandOwnerFarmers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFarmerFieldLandOwnerFarmers(FarmerFieldLandOwnerFarmerCriteria criteria) {
        LOG.debug("REST request to count FarmerFieldLandOwnerFarmers by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmerFieldLandOwnerFarmerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmer-field-land-owner-farmers/:id} : get the "id" farmerFieldLandOwnerFarmer.
     *
     * @param id the id of the farmerFieldLandOwnerFarmer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmerFieldLandOwnerFarmer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmerFieldLandOwnerFarmer> getFarmerFieldLandOwnerFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FarmerFieldLandOwnerFarmer : {}", id);
        Optional<FarmerFieldLandOwnerFarmer> farmerFieldLandOwnerFarmer = farmerFieldLandOwnerFarmerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmerFieldLandOwnerFarmer);
    }

    /**
     * {@code DELETE  /farmer-field-land-owner-farmers/:id} : delete the "id" farmerFieldLandOwnerFarmer.
     *
     * @param id the id of the farmerFieldLandOwnerFarmer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmerFieldLandOwnerFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FarmerFieldLandOwnerFarmer : {}", id);
        farmerFieldLandOwnerFarmerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
