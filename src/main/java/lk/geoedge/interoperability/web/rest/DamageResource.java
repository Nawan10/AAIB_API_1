package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.Damage;
import lk.geoedge.interoperability.repository.DamageRepository;
import lk.geoedge.interoperability.service.DamageQueryService;
import lk.geoedge.interoperability.service.DamageService;
import lk.geoedge.interoperability.service.criteria.DamageCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.Damage}.
 */
@RestController
@RequestMapping("/api/damages")
public class DamageResource {

    private static final Logger LOG = LoggerFactory.getLogger(DamageResource.class);

    private static final String ENTITY_NAME = "aaibapi1Damage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DamageService damageService;

    private final DamageRepository damageRepository;

    private final DamageQueryService damageQueryService;

    public DamageResource(DamageService damageService, DamageRepository damageRepository, DamageQueryService damageQueryService) {
        this.damageService = damageService;
        this.damageRepository = damageRepository;
        this.damageQueryService = damageQueryService;
    }

    /**
     * {@code POST  /damages} : Create a new damage.
     *
     * @param damage the damage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new damage, or with status {@code 400 (Bad Request)} if the damage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Damage> createDamage(@RequestBody Damage damage) throws URISyntaxException {
        LOG.debug("REST request to save Damage : {}", damage);
        if (damage.getId() != null) {
            throw new BadRequestAlertException("A new damage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        damage = damageService.save(damage);
        return ResponseEntity.created(new URI("/api/damages/" + damage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, damage.getId().toString()))
            .body(damage);
    }

    /**
     * {@code PUT  /damages/:id} : Updates an existing damage.
     *
     * @param id the id of the damage to save.
     * @param damage the damage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damage,
     * or with status {@code 400 (Bad Request)} if the damage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the damage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Damage> updateDamage(@PathVariable(value = "id", required = false) final Long id, @RequestBody Damage damage)
        throws URISyntaxException {
        LOG.debug("REST request to update Damage : {}, {}", id, damage);
        if (damage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        damage = damageService.update(damage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damage.getId().toString()))
            .body(damage);
    }

    /**
     * {@code PATCH  /damages/:id} : Partial updates given fields of an existing damage, field will ignore if it is null
     *
     * @param id the id of the damage to save.
     * @param damage the damage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated damage,
     * or with status {@code 400 (Bad Request)} if the damage is not valid,
     * or with status {@code 404 (Not Found)} if the damage is not found,
     * or with status {@code 500 (Internal Server Error)} if the damage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Damage> partialUpdateDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Damage damage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Damage partially : {}, {}", id, damage);
        if (damage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, damage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!damageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Damage> result = damageService.partialUpdate(damage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, damage.getId().toString())
        );
    }

    /**
     * {@code GET  /damages} : get all the damages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of damages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Damage>> getAllDamages(
        DamageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Damages by criteria: {}", criteria);

        Page<Damage> page = damageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /damages/count} : count all the damages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDamages(DamageCriteria criteria) {
        LOG.debug("REST request to count Damages by criteria: {}", criteria);
        return ResponseEntity.ok().body(damageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /damages/:id} : get the "id" damage.
     *
     * @param id the id of the damage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the damage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Damage> getDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Damage : {}", id);
        Optional<Damage> damage = damageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(damage);
    }

    /**
     * {@code DELETE  /damages/:id} : delete the "id" damage.
     *
     * @param id the id of the damage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDamage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Damage : {}", id);
        damageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
