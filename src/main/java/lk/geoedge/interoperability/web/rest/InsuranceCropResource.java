package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCrop;
import lk.geoedge.interoperability.repository.InsuranceCropRepository;
import lk.geoedge.interoperability.service.InsuranceCropQueryService;
import lk.geoedge.interoperability.service.InsuranceCropService;
import lk.geoedge.interoperability.service.criteria.InsuranceCropCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCrop}.
 */
@RestController
@RequestMapping("/api/insurance-crops")
public class InsuranceCropResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCropResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCrop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCropService insuranceCropService;

    private final InsuranceCropRepository insuranceCropRepository;

    private final InsuranceCropQueryService insuranceCropQueryService;

    public InsuranceCropResource(
        InsuranceCropService insuranceCropService,
        InsuranceCropRepository insuranceCropRepository,
        InsuranceCropQueryService insuranceCropQueryService
    ) {
        this.insuranceCropService = insuranceCropService;
        this.insuranceCropRepository = insuranceCropRepository;
        this.insuranceCropQueryService = insuranceCropQueryService;
    }

    /**
     * {@code POST  /insurance-crops} : Create a new insuranceCrop.
     *
     * @param insuranceCrop the insuranceCrop to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCrop, or with status {@code 400 (Bad Request)} if the insuranceCrop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCrop> createInsuranceCrop(@RequestBody InsuranceCrop insuranceCrop) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCrop : {}", insuranceCrop);
        if (insuranceCrop.getId() != null) {
            throw new BadRequestAlertException("A new insuranceCrop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceCrop = insuranceCropService.save(insuranceCrop);
        return ResponseEntity.created(new URI("/api/insurance-crops/" + insuranceCrop.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insuranceCrop.getId().toString()))
            .body(insuranceCrop);
    }

    /**
     * {@code PUT  /insurance-crops/:id} : Updates an existing insuranceCrop.
     *
     * @param id the id of the insuranceCrop to save.
     * @param insuranceCrop the insuranceCrop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCrop,
     * or with status {@code 400 (Bad Request)} if the insuranceCrop is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCrop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCrop> updateInsuranceCrop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCrop insuranceCrop
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCrop : {}, {}", id, insuranceCrop);
        if (insuranceCrop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCrop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCropRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCrop = insuranceCropService.update(insuranceCrop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCrop.getId().toString()))
            .body(insuranceCrop);
    }

    /**
     * {@code PATCH  /insurance-crops/:id} : Partial updates given fields of an existing insuranceCrop, field will ignore if it is null
     *
     * @param id the id of the insuranceCrop to save.
     * @param insuranceCrop the insuranceCrop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCrop,
     * or with status {@code 400 (Bad Request)} if the insuranceCrop is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCrop is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCrop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCrop> partialUpdateInsuranceCrop(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCrop insuranceCrop
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuranceCrop partially : {}, {}", id, insuranceCrop);
        if (insuranceCrop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCrop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCropRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCrop> result = insuranceCropService.partialUpdate(insuranceCrop);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCrop.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-crops} : get all the insuranceCrops.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCrops in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCrop>> getAllInsuranceCrops(
        InsuranceCropCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCrops by criteria: {}", criteria);

        Page<InsuranceCrop> page = insuranceCropQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-crops/count} : count all the insuranceCrops.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCrops(InsuranceCropCriteria criteria) {
        LOG.debug("REST request to count InsuranceCrops by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCropQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-crops/:id} : get the "id" insuranceCrop.
     *
     * @param id the id of the insuranceCrop to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCrop, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCrop> getInsuranceCrop(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCrop : {}", id);
        Optional<InsuranceCrop> insuranceCrop = insuranceCropService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCrop);
    }

    /**
     * {@code DELETE  /insurance-crops/:id} : delete the "id" insuranceCrop.
     *
     * @param id the id of the insuranceCrop to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCrop(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCrop : {}", id);
        insuranceCropService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
