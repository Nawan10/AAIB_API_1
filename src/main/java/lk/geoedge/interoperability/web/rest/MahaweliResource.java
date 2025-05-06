package lk.geoedge.interoperability.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lk.geoedge.interoperability.domain.Mahaweli;
import lk.geoedge.interoperability.repository.MahaweliRepository;
import lk.geoedge.interoperability.service.MahaweliQueryService;
import lk.geoedge.interoperability.service.MahaweliService;
import lk.geoedge.interoperability.service.criteria.MahaweliCriteria;
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
 * REST controller for managing {@link lk.geoedge.interoperability.domain.Mahaweli}.
 */
@RestController
@RequestMapping("/api/mahawelis")
public class MahaweliResource {

    private static final Logger LOG = LoggerFactory.getLogger(MahaweliResource.class);

    private static final String ENTITY_NAME = "aaibapi1Mahaweli";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MahaweliService mahaweliService;

    private final MahaweliRepository mahaweliRepository;

    private final MahaweliQueryService mahaweliQueryService;

    public MahaweliResource(
        MahaweliService mahaweliService,
        MahaweliRepository mahaweliRepository,
        MahaweliQueryService mahaweliQueryService
    ) {
        this.mahaweliService = mahaweliService;
        this.mahaweliRepository = mahaweliRepository;
        this.mahaweliQueryService = mahaweliQueryService;
    }

    /**
     * {@code POST  /mahawelis} : Create a new mahaweli.
     *
     * @param mahaweli the mahaweli to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mahaweli, or with status {@code 400 (Bad Request)} if the mahaweli has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Mahaweli> createMahaweli(@RequestBody Mahaweli mahaweli) throws URISyntaxException {
        LOG.debug("REST request to save Mahaweli : {}", mahaweli);
        if (mahaweli.getId() != null) {
            throw new BadRequestAlertException("A new mahaweli cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mahaweli = mahaweliService.save(mahaweli);
        return ResponseEntity.created(new URI("/api/mahawelis/" + mahaweli.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, mahaweli.getId().toString()))
            .body(mahaweli);
    }

    /**
     * {@code PUT  /mahawelis/:id} : Updates an existing mahaweli.
     *
     * @param id the id of the mahaweli to save.
     * @param mahaweli the mahaweli to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mahaweli,
     * or with status {@code 400 (Bad Request)} if the mahaweli is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mahaweli couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mahaweli> updateMahaweli(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mahaweli mahaweli
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mahaweli : {}, {}", id, mahaweli);
        if (mahaweli.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mahaweli.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mahaweliRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mahaweli = mahaweliService.update(mahaweli);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mahaweli.getId().toString()))
            .body(mahaweli);
    }

    /**
     * {@code PATCH  /mahawelis/:id} : Partial updates given fields of an existing mahaweli, field will ignore if it is null
     *
     * @param id the id of the mahaweli to save.
     * @param mahaweli the mahaweli to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mahaweli,
     * or with status {@code 400 (Bad Request)} if the mahaweli is not valid,
     * or with status {@code 404 (Not Found)} if the mahaweli is not found,
     * or with status {@code 500 (Internal Server Error)} if the mahaweli couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mahaweli> partialUpdateMahaweli(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mahaweli mahaweli
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mahaweli partially : {}, {}", id, mahaweli);
        if (mahaweli.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mahaweli.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mahaweliRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mahaweli> result = mahaweliService.partialUpdate(mahaweli);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mahaweli.getId().toString())
        );
    }

    /**
     * {@code GET  /mahawelis} : get all the mahawelis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mahawelis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Mahaweli>> getAllMahawelis(
        MahaweliCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Mahawelis by criteria: {}", criteria);

        Page<Mahaweli> page = mahaweliQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mahawelis/count} : count all the mahawelis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMahawelis(MahaweliCriteria criteria) {
        LOG.debug("REST request to count Mahawelis by criteria: {}", criteria);
        return ResponseEntity.ok().body(mahaweliQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mahawelis/:id} : get the "id" mahaweli.
     *
     * @param id the id of the mahaweli to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mahaweli, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mahaweli> getMahaweli(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Mahaweli : {}", id);
        Optional<Mahaweli> mahaweli = mahaweliService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mahaweli);
    }

    /**
     * {@code DELETE  /mahawelis/:id} : delete the "id" mahaweli.
     *
     * @param id the id of the mahaweli to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMahaweli(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Mahaweli : {}", id);
        mahaweliService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
