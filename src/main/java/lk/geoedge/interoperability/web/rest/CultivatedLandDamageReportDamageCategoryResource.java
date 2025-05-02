package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportDamageCategoryRepository;
import lk.geoedge.interoperability.service.CultivatedLandDamageReportDamageCategoryQueryService;
import lk.geoedge.interoperability.service.CultivatedLandDamageReportDamageCategoryService;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReportDamageCategoryCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory}.
 */
@RestController
@RequestMapping("/api/cultivated-land-damage-report-damage-categories")
public class CultivatedLandDamageReportDamageCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportDamageCategoryResource.class);

    private static final String ENTITY_NAME = "aaibapi1CultivatedLandDamageReportDamageCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CultivatedLandDamageReportDamageCategoryService cultivatedLandDamageReportDamageCategoryService;

    private final CultivatedLandDamageReportDamageCategoryRepository cultivatedLandDamageReportDamageCategoryRepository;

    private final CultivatedLandDamageReportDamageCategoryQueryService cultivatedLandDamageReportDamageCategoryQueryService;

    public CultivatedLandDamageReportDamageCategoryResource(
        CultivatedLandDamageReportDamageCategoryService cultivatedLandDamageReportDamageCategoryService,
        CultivatedLandDamageReportDamageCategoryRepository cultivatedLandDamageReportDamageCategoryRepository,
        CultivatedLandDamageReportDamageCategoryQueryService cultivatedLandDamageReportDamageCategoryQueryService
    ) {
        this.cultivatedLandDamageReportDamageCategoryService = cultivatedLandDamageReportDamageCategoryService;
        this.cultivatedLandDamageReportDamageCategoryRepository = cultivatedLandDamageReportDamageCategoryRepository;
        this.cultivatedLandDamageReportDamageCategoryQueryService = cultivatedLandDamageReportDamageCategoryQueryService;
    }

    /**
     * {@code POST  /cultivated-land-damage-report-damage-categories} : Create a new cultivatedLandDamageReportDamageCategory.
     *
     * @param cultivatedLandDamageReportDamageCategory the cultivatedLandDamageReportDamageCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cultivatedLandDamageReportDamageCategory, or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReportDamageCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CultivatedLandDamageReportDamageCategory> createCultivatedLandDamageReportDamageCategory(
        @RequestBody CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to save CultivatedLandDamageReportDamageCategory : {}", cultivatedLandDamageReportDamageCategory);
        if (cultivatedLandDamageReportDamageCategory.getId() != null) {
            throw new BadRequestAlertException(
                "A new cultivatedLandDamageReportDamageCategory cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        cultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryService.save(
            cultivatedLandDamageReportDamageCategory
        );
        return ResponseEntity.created(
            new URI("/api/cultivated-land-damage-report-damage-categories/" + cultivatedLandDamageReportDamageCategory.getId())
        )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReportDamageCategory.getId().toString()
                )
            )
            .body(cultivatedLandDamageReportDamageCategory);
    }

    /**
     * {@code PUT  /cultivated-land-damage-report-damage-categories/:id} : Updates an existing cultivatedLandDamageReportDamageCategory.
     *
     * @param id the id of the cultivatedLandDamageReportDamageCategory to save.
     * @param cultivatedLandDamageReportDamageCategory the cultivatedLandDamageReportDamageCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReportDamageCategory,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReportDamageCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReportDamageCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReportDamageCategory> updateCultivatedLandDamageReportDamageCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update CultivatedLandDamageReportDamageCategory : {}, {}", id, cultivatedLandDamageReportDamageCategory);
        if (cultivatedLandDamageReportDamageCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReportDamageCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReportDamageCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cultivatedLandDamageReportDamageCategory = cultivatedLandDamageReportDamageCategoryService.update(
            cultivatedLandDamageReportDamageCategory
        );
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    false,
                    ENTITY_NAME,
                    cultivatedLandDamageReportDamageCategory.getId().toString()
                )
            )
            .body(cultivatedLandDamageReportDamageCategory);
    }

    /**
     * {@code PATCH  /cultivated-land-damage-report-damage-categories/:id} : Partial updates given fields of an existing cultivatedLandDamageReportDamageCategory, field will ignore if it is null
     *
     * @param id the id of the cultivatedLandDamageReportDamageCategory to save.
     * @param cultivatedLandDamageReportDamageCategory the cultivatedLandDamageReportDamageCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cultivatedLandDamageReportDamageCategory,
     * or with status {@code 400 (Bad Request)} if the cultivatedLandDamageReportDamageCategory is not valid,
     * or with status {@code 404 (Not Found)} if the cultivatedLandDamageReportDamageCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the cultivatedLandDamageReportDamageCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CultivatedLandDamageReportDamageCategory> partialUpdateCultivatedLandDamageReportDamageCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update CultivatedLandDamageReportDamageCategory partially : {}, {}",
            id,
            cultivatedLandDamageReportDamageCategory
        );
        if (cultivatedLandDamageReportDamageCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cultivatedLandDamageReportDamageCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cultivatedLandDamageReportDamageCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CultivatedLandDamageReportDamageCategory> result = cultivatedLandDamageReportDamageCategoryService.partialUpdate(
            cultivatedLandDamageReportDamageCategory
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                false,
                ENTITY_NAME,
                cultivatedLandDamageReportDamageCategory.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /cultivated-land-damage-report-damage-categories} : get all the cultivatedLandDamageReportDamageCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cultivatedLandDamageReportDamageCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CultivatedLandDamageReportDamageCategory>> getAllCultivatedLandDamageReportDamageCategories(
        CultivatedLandDamageReportDamageCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReportDamageCategories by criteria: {}", criteria);

        Page<CultivatedLandDamageReportDamageCategory> page = cultivatedLandDamageReportDamageCategoryQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cultivated-land-damage-report-damage-categories/count} : count all the cultivatedLandDamageReportDamageCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCultivatedLandDamageReportDamageCategories(CultivatedLandDamageReportDamageCategoryCriteria criteria) {
        LOG.debug("REST request to count CultivatedLandDamageReportDamageCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(cultivatedLandDamageReportDamageCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cultivated-land-damage-report-damage-categories/:id} : get the "id" cultivatedLandDamageReportDamageCategory.
     *
     * @param id the id of the cultivatedLandDamageReportDamageCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cultivatedLandDamageReportDamageCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CultivatedLandDamageReportDamageCategory> getCultivatedLandDamageReportDamageCategory(
        @PathVariable("id") Long id
    ) {
        LOG.debug("REST request to get CultivatedLandDamageReportDamageCategory : {}", id);
        Optional<CultivatedLandDamageReportDamageCategory> cultivatedLandDamageReportDamageCategory =
            cultivatedLandDamageReportDamageCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cultivatedLandDamageReportDamageCategory);
    }

    /**
     * {@code DELETE  /cultivated-land-damage-report-damage-categories/:id} : delete the "id" cultivatedLandDamageReportDamageCategory.
     *
     * @param id the id of the cultivatedLandDamageReportDamageCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultivatedLandDamageReportDamageCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CultivatedLandDamageReportDamageCategory : {}", id);
        cultivatedLandDamageReportDamageCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
