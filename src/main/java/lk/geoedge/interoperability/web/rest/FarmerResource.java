package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.Farmer;
import lk.geoedge.interoperability.repository.FarmerRepository;
import lk.geoedge.interoperability.service.FarmerQueryService;
import lk.geoedge.interoperability.service.FarmerService;
import lk.geoedge.interoperability.service.criteria.FarmerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.Farmer}.
 */
@RestController
@RequestMapping("/api/farmers")
public class FarmerResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerResource.class);

    private static final String ENTITY_NAME = "aaibapi1Farmer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmerService farmerService;

    private final FarmerRepository farmerRepository;

    private final FarmerQueryService farmerQueryService;

    public FarmerResource(FarmerService farmerService, FarmerRepository farmerRepository, FarmerQueryService farmerQueryService) {
        this.farmerService = farmerService;
        this.farmerRepository = farmerRepository;
        this.farmerQueryService = farmerQueryService;
    }

    /**
     * {@code POST  /farmers} : Create a new farmer.
     *
     * @param farmer the farmer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmer, or with status {@code 400 (Bad Request)} if the farmer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Farmer> createFarmer(@RequestBody Farmer farmer) throws URISyntaxException {
        LOG.debug("REST request to save Farmer : {}", farmer);
        if (farmer.getId() != null) {
            throw new BadRequestAlertException("A new farmer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmer = farmerService.save(farmer);
        return ResponseEntity.created(new URI("/api/farmers/" + farmer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmer.getId().toString()))
            .body(farmer);
    }

    /**
     * {@code PUT  /farmers/:id} : Updates an existing farmer.
     *
     * @param id the id of the farmer to save.
     * @param farmer the farmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmer,
     * or with status {@code 400 (Bad Request)} if the farmer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Farmer> updateFarmer(@PathVariable(value = "id", required = false) final Long id, @RequestBody Farmer farmer)
        throws URISyntaxException {
        LOG.debug("REST request to update Farmer : {}, {}", id, farmer);
        if (farmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmer = farmerService.update(farmer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmer.getId().toString()))
            .body(farmer);
    }

    /**
     * {@code PATCH  /farmers/:id} : Partial updates given fields of an existing farmer, field will ignore if it is null
     *
     * @param id the id of the farmer to save.
     * @param farmer the farmer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmer,
     * or with status {@code 400 (Bad Request)} if the farmer is not valid,
     * or with status {@code 404 (Not Found)} if the farmer is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Farmer> partialUpdateFarmer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Farmer farmer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Farmer partially : {}, {}", id, farmer);
        if (farmer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Farmer> result = farmerService.partialUpdate(farmer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmer.getId().toString())
        );
    }

    /**
     * {@code GET  /farmers} : get all the farmers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Farmer>> getAllFarmers(
        FarmerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Farmers by criteria: {}", criteria);

        Page<Farmer> page = farmerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmers/count} : count all the farmers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFarmers(FarmerCriteria criteria) {
        LOG.debug("REST request to count Farmers by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmers/:id} : get the "id" farmer.
     *
     * @param id the id of the farmer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Farmer : {}", id);
        Optional<Farmer> farmer = farmerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmer);
    }

    /**
     * {@code DELETE  /farmers/:id} : delete the "id" farmer.
     *
     * @param id the id of the farmer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Farmer : {}", id);
        farmerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
