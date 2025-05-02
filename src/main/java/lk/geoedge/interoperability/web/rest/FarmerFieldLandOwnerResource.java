package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldLandOwner;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerRepository;
import lk.geoedge.interoperability.service.FarmerFieldLandOwnerQueryService;
import lk.geoedge.interoperability.service.FarmerFieldLandOwnerService;
import lk.geoedge.interoperability.service.criteria.FarmerFieldLandOwnerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.FarmerFieldLandOwner}.
 */
@RestController
@RequestMapping("/api/farmer-field-land-owners")
public class FarmerFieldLandOwnerResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldLandOwnerResource.class);

    private static final String ENTITY_NAME = "aaibapi1FarmerFieldLandOwner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmerFieldLandOwnerService farmerFieldLandOwnerService;

    private final FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository;

    private final FarmerFieldLandOwnerQueryService farmerFieldLandOwnerQueryService;

    public FarmerFieldLandOwnerResource(
        FarmerFieldLandOwnerService farmerFieldLandOwnerService,
        FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository,
        FarmerFieldLandOwnerQueryService farmerFieldLandOwnerQueryService
    ) {
        this.farmerFieldLandOwnerService = farmerFieldLandOwnerService;
        this.farmerFieldLandOwnerRepository = farmerFieldLandOwnerRepository;
        this.farmerFieldLandOwnerQueryService = farmerFieldLandOwnerQueryService;
    }

    /**
     * {@code POST  /farmer-field-land-owners} : Create a new farmerFieldLandOwner.
     *
     * @param farmerFieldLandOwner the farmerFieldLandOwner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmerFieldLandOwner, or with status {@code 400 (Bad Request)} if the farmerFieldLandOwner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmerFieldLandOwner> createFarmerFieldLandOwner(@RequestBody FarmerFieldLandOwner farmerFieldLandOwner)
        throws URISyntaxException {
        LOG.debug("REST request to save FarmerFieldLandOwner : {}", farmerFieldLandOwner);
        if (farmerFieldLandOwner.getId() != null) {
            throw new BadRequestAlertException("A new farmerFieldLandOwner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmerFieldLandOwner = farmerFieldLandOwnerService.save(farmerFieldLandOwner);
        return ResponseEntity.created(new URI("/api/farmer-field-land-owners/" + farmerFieldLandOwner.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmerFieldLandOwner.getId().toString()))
            .body(farmerFieldLandOwner);
    }

    /**
     * {@code PUT  /farmer-field-land-owners/:id} : Updates an existing farmerFieldLandOwner.
     *
     * @param id the id of the farmerFieldLandOwner to save.
     * @param farmerFieldLandOwner the farmerFieldLandOwner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldLandOwner,
     * or with status {@code 400 (Bad Request)} if the farmerFieldLandOwner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldLandOwner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmerFieldLandOwner> updateFarmerFieldLandOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldLandOwner farmerFieldLandOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to update FarmerFieldLandOwner : {}, {}", id, farmerFieldLandOwner);
        if (farmerFieldLandOwner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldLandOwner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldLandOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmerFieldLandOwner = farmerFieldLandOwnerService.update(farmerFieldLandOwner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldLandOwner.getId().toString()))
            .body(farmerFieldLandOwner);
    }

    /**
     * {@code PATCH  /farmer-field-land-owners/:id} : Partial updates given fields of an existing farmerFieldLandOwner, field will ignore if it is null
     *
     * @param id the id of the farmerFieldLandOwner to save.
     * @param farmerFieldLandOwner the farmerFieldLandOwner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldLandOwner,
     * or with status {@code 400 (Bad Request)} if the farmerFieldLandOwner is not valid,
     * or with status {@code 404 (Not Found)} if the farmerFieldLandOwner is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldLandOwner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmerFieldLandOwner> partialUpdateFarmerFieldLandOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldLandOwner farmerFieldLandOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FarmerFieldLandOwner partially : {}, {}", id, farmerFieldLandOwner);
        if (farmerFieldLandOwner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldLandOwner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldLandOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmerFieldLandOwner> result = farmerFieldLandOwnerService.partialUpdate(farmerFieldLandOwner);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldLandOwner.getId().toString())
        );
    }

    /**
     * {@code GET  /farmer-field-land-owners} : get all the farmerFieldLandOwners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmerFieldLandOwners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmerFieldLandOwner>> getAllFarmerFieldLandOwners(
        FarmerFieldLandOwnerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FarmerFieldLandOwners by criteria: {}", criteria);

        Page<FarmerFieldLandOwner> page = farmerFieldLandOwnerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmer-field-land-owners/count} : count all the farmerFieldLandOwners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFarmerFieldLandOwners(FarmerFieldLandOwnerCriteria criteria) {
        LOG.debug("REST request to count FarmerFieldLandOwners by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmerFieldLandOwnerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmer-field-land-owners/:id} : get the "id" farmerFieldLandOwner.
     *
     * @param id the id of the farmerFieldLandOwner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmerFieldLandOwner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmerFieldLandOwner> getFarmerFieldLandOwner(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FarmerFieldLandOwner : {}", id);
        Optional<FarmerFieldLandOwner> farmerFieldLandOwner = farmerFieldLandOwnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmerFieldLandOwner);
    }

    /**
     * {@code DELETE  /farmer-field-land-owners/:id} : delete the "id" farmerFieldLandOwner.
     *
     * @param id the id of the farmerFieldLandOwner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmerFieldLandOwner(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FarmerFieldLandOwner : {}", id);
        farmerFieldLandOwnerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
