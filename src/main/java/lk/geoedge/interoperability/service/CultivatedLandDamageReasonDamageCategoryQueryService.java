package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonDamageCategoryRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReasonDamageCategoryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandDamageReasonDamageCategory} entities in the database.
 * The main input is a {@link CultivatedLandDamageReasonDamageCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandDamageReasonDamageCategory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandDamageReasonDamageCategoryQueryService extends QueryService<CultivatedLandDamageReasonDamageCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonDamageCategoryQueryService.class);

    private final CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository;

    public CultivatedLandDamageReasonDamageCategoryQueryService(
        CultivatedLandDamageReasonDamageCategoryRepository cultivatedLandDamageReasonDamageCategoryRepository
    ) {
        this.cultivatedLandDamageReasonDamageCategoryRepository = cultivatedLandDamageReasonDamageCategoryRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandDamageReasonDamageCategory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandDamageReasonDamageCategory> findByCriteria(
        CultivatedLandDamageReasonDamageCategoryCriteria criteria,
        Pageable page
    ) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandDamageReasonDamageCategory> specification = createSpecification(criteria);
        return cultivatedLandDamageReasonDamageCategoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandDamageReasonDamageCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandDamageReasonDamageCategory> specification = createSpecification(criteria);
        return cultivatedLandDamageReasonDamageCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandDamageReasonDamageCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandDamageReasonDamageCategory> createSpecification(
        CultivatedLandDamageReasonDamageCategoryCriteria criteria
    ) {
        Specification<CultivatedLandDamageReasonDamageCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandDamageReasonDamageCategory_.id),
                buildStringSpecification(criteria.getCategoryName(), CultivatedLandDamageReasonDamageCategory_.categoryName)
            );
        }
        return specification;
    }
}
