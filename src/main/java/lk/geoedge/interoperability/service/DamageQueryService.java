package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.Damage;
import lk.geoedge.interoperability.repository.DamageRepository;
import lk.geoedge.interoperability.service.criteria.DamageCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Damage} entities in the database.
 * The main input is a {@link DamageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Damage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DamageQueryService extends QueryService<Damage> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageQueryService.class);

    private final DamageRepository damageRepository;

    public DamageQueryService(DamageRepository damageRepository) {
        this.damageRepository = damageRepository;
    }

    /**
     * Return a {@link Page} of {@link Damage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Damage> findByCriteria(DamageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Damage> specification = createSpecification(criteria);
        return damageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DamageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Damage> specification = createSpecification(criteria);
        return damageRepository.count(specification);
    }

    /**
     * Function to convert {@link DamageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Damage> createSpecification(DamageCriteria criteria) {
        Specification<Damage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Damage_.id),
                buildStringSpecification(criteria.getDamageName(), Damage_.damageName),
                buildStringSpecification(criteria.getDamageCode(), Damage_.damageCode),
                buildStringSpecification(criteria.getDamageFamily(), Damage_.damageFamily),
                buildStringSpecification(criteria.getDamageGenus(), Damage_.damageGenus),
                buildStringSpecification(criteria.getDamageSpecies(), Damage_.damageSpecies),
                buildRangeSpecification(criteria.getCreatedAt(), Damage_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), Damage_.addedBy),
                buildSpecification(criteria.getDamageCategoryId(), root ->
                    root.join(Damage_.damageCategory, JoinType.LEFT).get(DamageCategory_.id)
                ),
                buildSpecification(criteria.getDamageTypeId(), root -> root.join(Damage_.damageType, JoinType.LEFT).get(DamageType_.id))
            );
        }
        return specification;
    }
}
