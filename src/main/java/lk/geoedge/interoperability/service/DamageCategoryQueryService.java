package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.DamageCategory;
import lk.geoedge.interoperability.repository.DamageCategoryRepository;
import lk.geoedge.interoperability.service.criteria.DamageCategoryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DamageCategory} entities in the database.
 * The main input is a {@link DamageCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DamageCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DamageCategoryQueryService extends QueryService<DamageCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(DamageCategoryQueryService.class);

    private final DamageCategoryRepository damageCategoryRepository;

    public DamageCategoryQueryService(DamageCategoryRepository damageCategoryRepository) {
        this.damageCategoryRepository = damageCategoryRepository;
    }

    /**
     * Return a {@link Page} of {@link DamageCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DamageCategory> findByCriteria(DamageCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DamageCategory> specification = createSpecification(criteria);
        return damageCategoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DamageCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DamageCategory> specification = createSpecification(criteria);
        return damageCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link DamageCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DamageCategory> createSpecification(DamageCategoryCriteria criteria) {
        Specification<DamageCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DamageCategory_.id),
                buildStringSpecification(criteria.getCategoryName(), DamageCategory_.categoryName)
            );
        }
        return specification;
    }
}
