package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.DamageEntity;
import lk.geoedge.interoperability.repository.DamageEntityRepository;
import lk.geoedge.interoperability.service.criteria.DamageEntityCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DamageEntity} entities in the database.
 * The main input is a {@link DamageEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DamageEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DamageEntityQueryService extends QueryService<DamageEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageEntityQueryService.class);

    private final DamageEntityRepository damageEntityRepository;

    public DamageEntityQueryService(DamageEntityRepository damageEntityRepository) {
        this.damageEntityRepository = damageEntityRepository;
    }

    /**
     * Return a {@link Page} of {@link DamageEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DamageEntity> findByCriteria(DamageEntityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DamageEntity> specification = createSpecification(criteria);
        return damageEntityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DamageEntityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DamageEntity> specification = createSpecification(criteria);
        return damageEntityRepository.count(specification);
    }

    /**
     * Function to convert {@link DamageEntityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DamageEntity> createSpecification(DamageEntityCriteria criteria) {
        Specification<DamageEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DamageEntity_.id),
                buildStringSpecification(criteria.getDamageName(), DamageEntity_.damageName),
                buildStringSpecification(criteria.getDamageCode(), DamageEntity_.damageCode),
                buildStringSpecification(criteria.getDamageFamily(), DamageEntity_.damageFamily),
                buildStringSpecification(criteria.getDamageGenus(), DamageEntity_.damageGenus),
                buildStringSpecification(criteria.getDamageSpecies(), DamageEntity_.damageSpecies),
                buildRangeSpecification(criteria.getCreatedAt(), DamageEntity_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), DamageEntity_.addedBy),
                buildSpecification(criteria.getDamageCategoryId(), root ->
                    root.join(DamageEntity_.damageCategory, JoinType.LEFT).get(DamageCategory_.id)
                ),
                buildSpecification(criteria.getDamageTypeId(), root ->
                    root.join(DamageEntity_.damageType, JoinType.LEFT).get(DamageType_.id)
                )
            );
        }
        return specification;
    }
}
