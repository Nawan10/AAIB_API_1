package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCropCropType;
import lk.geoedge.interoperability.repository.InsuranceCropCropTypeRepository;
import lk.geoedge.interoperability.service.InsuranceCropCropTypeQueryService;
import lk.geoedge.interoperability.service.InsuranceCropCropTypeService;
import lk.geoedge.interoperability.service.criteria.InsuranceCropCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCropCropType}.
 */
@RestController
@RequestMapping("/api/insurance-crop-crop-types")
public class InsuranceCropCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCropCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCropCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCropCropTypeService insuranceCropCropTypeService;

    private final InsuranceCropCropTypeRepository insuranceCropCropTypeRepository;

    private final InsuranceCropCropTypeQueryService insuranceCropCropTypeQueryService;

    public InsuranceCropCropTypeResource(
        InsuranceCropCropTypeService insuranceCropCropTypeService,
        InsuranceCropCropTypeRepository insuranceCropCropTypeRepository,
        InsuranceCropCropTypeQueryService insuranceCropCropTypeQueryService
    ) {
        this.insuranceCropCropTypeService = insuranceCropCropTypeService;
        this.insuranceCropCropTypeRepository = insuranceCropCropTypeRepository;
        this.insuranceCropCropTypeQueryService = insuranceCropCropTypeQueryService;
    }

    /**
     * {@code POST  /insurance-crop-crop-types} : Create a new insuranceCropCropType.
     *
     * @param insuranceCropCropType the insuranceCropCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCropCropType, or with status {@code 400 (Bad Request)} if the insuranceCropCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCropCropType> createInsuranceCropCropType(@RequestBody InsuranceCropCropType insuranceCropCropType)
        throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCropCropType : {}", insuranceCropCropType);
        if (insuranceCropCropType.getId() != null) {
            throw new BadRequestAlertException("A new insuranceCropCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceCropCropType = insuranceCropCropTypeService.save(insuranceCropCropType);
        return ResponseEntity.created(new URI("/api/insurance-crop-crop-types/" + insuranceCropCropType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, insuranceCropCropType.getId().toString()))
            .body(insuranceCropCropType);
    }

    /**
     * {@code PUT  /insurance-crop-crop-types/:id} : Updates an existing insuranceCropCropType.
     *
     * @param id the id of the insuranceCropCropType to save.
     * @param insuranceCropCropType the insuranceCropCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCropCropType,
     * or with status {@code 400 (Bad Request)} if the insuranceCropCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCropCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCropCropType> updateInsuranceCropCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCropCropType insuranceCropCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCropCropType : {}, {}", id, insuranceCropCropType);
        if (insuranceCropCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCropCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCropCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCropCropType = insuranceCropCropTypeService.update(insuranceCropCropType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCropCropType.getId().toString()))
            .body(insuranceCropCropType);
    }

    /**
     * {@code PATCH  /insurance-crop-crop-types/:id} : Partial updates given fields of an existing insuranceCropCropType, field will ignore if it is null
     *
     * @param id the id of the insuranceCropCropType to save.
     * @param insuranceCropCropType the insuranceCropCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCropCropType,
     * or with status {@code 400 (Bad Request)} if the insuranceCropCropType is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCropCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCropCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCropCropType> partialUpdateInsuranceCropCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCropCropType insuranceCropCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuranceCropCropType partially : {}, {}", id, insuranceCropCropType);
        if (insuranceCropCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCropCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCropCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCropCropType> result = insuranceCropCropTypeService.partialUpdate(insuranceCropCropType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCropCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-crop-crop-types} : get all the insuranceCropCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCropCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCropCropType>> getAllInsuranceCropCropTypes(
        InsuranceCropCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCropCropTypes by criteria: {}", criteria);

        Page<InsuranceCropCropType> page = insuranceCropCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-crop-crop-types/count} : count all the insuranceCropCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCropCropTypes(InsuranceCropCropTypeCriteria criteria) {
        LOG.debug("REST request to count InsuranceCropCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCropCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-crop-crop-types/:id} : get the "id" insuranceCropCropType.
     *
     * @param id the id of the insuranceCropCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCropCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCropCropType> getInsuranceCropCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCropCropType : {}", id);
        Optional<InsuranceCropCropType> insuranceCropCropType = insuranceCropCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCropCropType);
    }

    /**
     * {@code DELETE  /insurance-crop-crop-types/:id} : delete the "id" insuranceCropCropType.
     *
     * @param id the id of the insuranceCropCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCropCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCropCropType : {}", id);
        insuranceCropCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
