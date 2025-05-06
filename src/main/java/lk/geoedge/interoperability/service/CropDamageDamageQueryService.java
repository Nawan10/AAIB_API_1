package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropDamageDamage;
import lk.geoedge.interoperability.repository.CropDamageDamageRepository;
import lk.geoedge.interoperability.service.criteria.CropDamageDamageCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropDamageDamage} entities in the database.
 * The main input is a {@link CropDamageDamageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropDamageDamage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropDamageDamageQueryService extends QueryService<CropDamageDamage> {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageDamageQueryService.class);

    private final CropDamageDamageRepository cropDamageDamageRepository;

    public CropDamageDamageQueryService(CropDamageDamageRepository cropDamageDamageRepository) {
        this.cropDamageDamageRepository = cropDamageDamageRepository;
    }

    /**
     * Return a {@link Page} of {@link CropDamageDamage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropDamageDamage> findByCriteria(CropDamageDamageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropDamageDamage> specification = createSpecification(criteria);
        return cropDamageDamageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropDamageDamageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropDamageDamage> specification = createSpecification(criteria);
        return cropDamageDamageRepository.count(specification);
    }

    /**
     * Function to convert {@link CropDamageDamageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropDamageDamage> createSpecification(CropDamageDamageCriteria criteria) {
        Specification<CropDamageDamage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropDamageDamage_.id),
                buildStringSpecification(criteria.getDamageName(), CropDamageDamage_.damageName),
                buildStringSpecification(criteria.getDamageCode(), CropDamageDamage_.damageCode),
                buildStringSpecification(criteria.getDamageFamily(), CropDamageDamage_.damageFamily),
                buildStringSpecification(criteria.getDamageGenus(), CropDamageDamage_.damageGenus),
                buildStringSpecification(criteria.getDamageSpecies(), CropDamageDamage_.damageSpecies),
                buildRangeSpecification(criteria.getCreatedAt(), CropDamageDamage_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CropDamageDamage_.addedBy)
            );
        }
        return specification;
    }
}
