package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldOwner;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerRepository;
import lk.geoedge.interoperability.service.FarmerFieldOwnerQueryService;
import lk.geoedge.interoperability.service.FarmerFieldOwnerService;
import lk.geoedge.interoperability.service.criteria.FarmerFieldOwnerCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.FarmerFieldOwner}.
 */
@RestController
@RequestMapping("/api/farmer-field-owners")
public class FarmerFieldOwnerResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldOwnerResource.class);

    private static final String ENTITY_NAME = "aaibapi1FarmerFieldOwner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmerFieldOwnerService farmerFieldOwnerService;

    private final FarmerFieldOwnerRepository farmerFieldOwnerRepository;

    private final FarmerFieldOwnerQueryService farmerFieldOwnerQueryService;

    public FarmerFieldOwnerResource(
        FarmerFieldOwnerService farmerFieldOwnerService,
        FarmerFieldOwnerRepository farmerFieldOwnerRepository,
        FarmerFieldOwnerQueryService farmerFieldOwnerQueryService
    ) {
        this.farmerFieldOwnerService = farmerFieldOwnerService;
        this.farmerFieldOwnerRepository = farmerFieldOwnerRepository;
        this.farmerFieldOwnerQueryService = farmerFieldOwnerQueryService;
    }

    /**
     * {@code POST  /farmer-field-owners} : Create a new farmerFieldOwner.
     *
     * @param farmerFieldOwner the farmerFieldOwner to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmerFieldOwner, or with status {@code 400 (Bad Request)} if the farmerFieldOwner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmerFieldOwner> createFarmerFieldOwner(@RequestBody FarmerFieldOwner farmerFieldOwner)
        throws URISyntaxException {
        LOG.debug("REST request to save FarmerFieldOwner : {}", farmerFieldOwner);
        if (farmerFieldOwner.getId() != null) {
            throw new BadRequestAlertException("A new farmerFieldOwner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmerFieldOwner = farmerFieldOwnerService.save(farmerFieldOwner);
        return ResponseEntity.created(new URI("/api/farmer-field-owners/" + farmerFieldOwner.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, farmerFieldOwner.getId().toString()))
            .body(farmerFieldOwner);
    }

    /**
     * {@code PUT  /farmer-field-owners/:id} : Updates an existing farmerFieldOwner.
     *
     * @param id the id of the farmerFieldOwner to save.
     * @param farmerFieldOwner the farmerFieldOwner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldOwner,
     * or with status {@code 400 (Bad Request)} if the farmerFieldOwner is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldOwner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmerFieldOwner> updateFarmerFieldOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldOwner farmerFieldOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to update FarmerFieldOwner : {}, {}", id, farmerFieldOwner);
        if (farmerFieldOwner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldOwner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmerFieldOwner = farmerFieldOwnerService.update(farmerFieldOwner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldOwner.getId().toString()))
            .body(farmerFieldOwner);
    }

    /**
     * {@code PATCH  /farmer-field-owners/:id} : Partial updates given fields of an existing farmerFieldOwner, field will ignore if it is null
     *
     * @param id the id of the farmerFieldOwner to save.
     * @param farmerFieldOwner the farmerFieldOwner to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmerFieldOwner,
     * or with status {@code 400 (Bad Request)} if the farmerFieldOwner is not valid,
     * or with status {@code 404 (Not Found)} if the farmerFieldOwner is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmerFieldOwner couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmerFieldOwner> partialUpdateFarmerFieldOwner(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FarmerFieldOwner farmerFieldOwner
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FarmerFieldOwner partially : {}, {}", id, farmerFieldOwner);
        if (farmerFieldOwner.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmerFieldOwner.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmerFieldOwnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmerFieldOwner> result = farmerFieldOwnerService.partialUpdate(farmerFieldOwner);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, farmerFieldOwner.getId().toString())
        );
    }

    /**
     * {@code GET  /farmer-field-owners} : get all the farmerFieldOwners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmerFieldOwners in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmerFieldOwner>> getAllFarmerFieldOwners(
        FarmerFieldOwnerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FarmerFieldOwners by criteria: {}", criteria);

        Page<FarmerFieldOwner> page = farmerFieldOwnerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmer-field-owners/count} : count all the farmerFieldOwners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFarmerFieldOwners(FarmerFieldOwnerCriteria criteria) {
        LOG.debug("REST request to count FarmerFieldOwners by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmerFieldOwnerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmer-field-owners/:id} : get the "id" farmerFieldOwner.
     *
     * @param id the id of the farmerFieldOwner to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmerFieldOwner, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmerFieldOwner> getFarmerFieldOwner(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FarmerFieldOwner : {}", id);
        Optional<FarmerFieldOwner> farmerFieldOwner = farmerFieldOwnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmerFieldOwner);
    }

    /**
     * {@code DELETE  /farmer-field-owners/:id} : delete the "id" farmerFieldOwner.
     *
     * @param id the id of the farmerFieldOwner to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmerFieldOwner(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FarmerFieldOwner : {}", id);
        farmerFieldOwnerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
