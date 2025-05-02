package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCropTypeRepository;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCropTypeQueryService;
import lk.geoedge.interoperability.service.InsuranceCultivatedLandCropTypeService;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCropTypeCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropType}.
 */
@RestController
@RequestMapping("/api/insurance-cultivated-land-crop-types")
public class InsuranceCultivatedLandCropTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCropTypeResource.class);

    private static final String ENTITY_NAME = "aaibapi1InsuranceCultivatedLandCropType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceCultivatedLandCropTypeService insuranceCultivatedLandCropTypeService;

    private final InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository;

    private final InsuranceCultivatedLandCropTypeQueryService insuranceCultivatedLandCropTypeQueryService;

    public InsuranceCultivatedLandCropTypeResource(
        InsuranceCultivatedLandCropTypeService insuranceCultivatedLandCropTypeService,
        InsuranceCultivatedLandCropTypeRepository insuranceCultivatedLandCropTypeRepository,
        InsuranceCultivatedLandCropTypeQueryService insuranceCultivatedLandCropTypeQueryService
    ) {
        this.insuranceCultivatedLandCropTypeService = insuranceCultivatedLandCropTypeService;
        this.insuranceCultivatedLandCropTypeRepository = insuranceCultivatedLandCropTypeRepository;
        this.insuranceCultivatedLandCropTypeQueryService = insuranceCultivatedLandCropTypeQueryService;
    }

    /**
     * {@code POST  /insurance-cultivated-land-crop-types} : Create a new insuranceCultivatedLandCropType.
     *
     * @param insuranceCultivatedLandCropType the insuranceCultivatedLandCropType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insuranceCultivatedLandCropType, or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCropType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsuranceCultivatedLandCropType> createInsuranceCultivatedLandCropType(
        @RequestBody InsuranceCultivatedLandCropType insuranceCultivatedLandCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to save InsuranceCultivatedLandCropType : {}", insuranceCultivatedLandCropType);
        if (insuranceCultivatedLandCropType.getId() != null) {
            throw new BadRequestAlertException("A new insuranceCultivatedLandCropType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeService.save(insuranceCultivatedLandCropType);
        return ResponseEntity.created(new URI("/api/insurance-cultivated-land-crop-types/" + insuranceCultivatedLandCropType.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    insuranceCultivatedLandCropType.getId().toString()
                )
            )
            .body(insuranceCultivatedLandCropType);
    }

    /**
     * {@code PUT  /insurance-cultivated-land-crop-types/:id} : Updates an existing insuranceCultivatedLandCropType.
     *
     * @param id the id of the insuranceCultivatedLandCropType to save.
     * @param insuranceCultivatedLandCropType the insuranceCultivatedLandCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCropType,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCropType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandCropType> updateInsuranceCultivatedLandCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCropType insuranceCultivatedLandCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsuranceCultivatedLandCropType : {}, {}", id, insuranceCultivatedLandCropType);
        if (insuranceCultivatedLandCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeService.update(insuranceCultivatedLandCropType);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandCropType.getId().toString())
            )
            .body(insuranceCultivatedLandCropType);
    }

    /**
     * {@code PATCH  /insurance-cultivated-land-crop-types/:id} : Partial updates given fields of an existing insuranceCultivatedLandCropType, field will ignore if it is null
     *
     * @param id the id of the insuranceCultivatedLandCropType to save.
     * @param insuranceCultivatedLandCropType the insuranceCultivatedLandCropType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insuranceCultivatedLandCropType,
     * or with status {@code 400 (Bad Request)} if the insuranceCultivatedLandCropType is not valid,
     * or with status {@code 404 (Not Found)} if the insuranceCultivatedLandCropType is not found,
     * or with status {@code 500 (Internal Server Error)} if the insuranceCultivatedLandCropType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsuranceCultivatedLandCropType> partialUpdateInsuranceCultivatedLandCropType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InsuranceCultivatedLandCropType insuranceCultivatedLandCropType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsuranceCultivatedLandCropType partially : {}, {}", id, insuranceCultivatedLandCropType);
        if (insuranceCultivatedLandCropType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insuranceCultivatedLandCropType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insuranceCultivatedLandCropTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsuranceCultivatedLandCropType> result = insuranceCultivatedLandCropTypeService.partialUpdate(
            insuranceCultivatedLandCropType
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, insuranceCultivatedLandCropType.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-cultivated-land-crop-types} : get all the insuranceCultivatedLandCropTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insuranceCultivatedLandCropTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsuranceCultivatedLandCropType>> getAllInsuranceCultivatedLandCropTypes(
        InsuranceCultivatedLandCropTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsuranceCultivatedLandCropTypes by criteria: {}", criteria);

        Page<InsuranceCultivatedLandCropType> page = insuranceCultivatedLandCropTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-cultivated-land-crop-types/count} : count all the insuranceCultivatedLandCropTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsuranceCultivatedLandCropTypes(InsuranceCultivatedLandCropTypeCriteria criteria) {
        LOG.debug("REST request to count InsuranceCultivatedLandCropTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(insuranceCultivatedLandCropTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-cultivated-land-crop-types/:id} : get the "id" insuranceCultivatedLandCropType.
     *
     * @param id the id of the insuranceCultivatedLandCropType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insuranceCultivatedLandCropType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCultivatedLandCropType> getInsuranceCultivatedLandCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InsuranceCultivatedLandCropType : {}", id);
        Optional<InsuranceCultivatedLandCropType> insuranceCultivatedLandCropType = insuranceCultivatedLandCropTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insuranceCultivatedLandCropType);
    }

    /**
     * {@code DELETE  /insurance-cultivated-land-crop-types/:id} : delete the "id" insuranceCultivatedLandCropType.
     *
     * @param id the id of the insuranceCultivatedLandCropType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsuranceCultivatedLandCropType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InsuranceCultivatedLandCropType : {}", id);
        insuranceCultivatedLandCropTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
