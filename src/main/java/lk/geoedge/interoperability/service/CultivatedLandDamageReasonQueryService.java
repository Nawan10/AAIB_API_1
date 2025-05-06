package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReasonRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReasonCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandDamageReason} entities in the database.
 * The main input is a {@link CultivatedLandDamageReasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandDamageReason} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandDamageReasonQueryService extends QueryService<CultivatedLandDamageReason> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReasonQueryService.class);

    private final CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository;

    public CultivatedLandDamageReasonQueryService(CultivatedLandDamageReasonRepository cultivatedLandDamageReasonRepository) {
        this.cultivatedLandDamageReasonRepository = cultivatedLandDamageReasonRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandDamageReason} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandDamageReason> findByCriteria(CultivatedLandDamageReasonCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandDamageReason> specification = createSpecification(criteria);
        return cultivatedLandDamageReasonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandDamageReasonCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandDamageReason> specification = createSpecification(criteria);
        return cultivatedLandDamageReasonRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandDamageReasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandDamageReason> createSpecification(CultivatedLandDamageReasonCriteria criteria) {
        Specification<CultivatedLandDamageReason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandDamageReason_.id),
                buildStringSpecification(criteria.getName(), CultivatedLandDamageReason_.name),
                buildSpecification(criteria.getDamageCategoryId(), root ->
                    root.join(CultivatedLandDamageReason_.damageCategory, JoinType.LEFT).get(CultivatedLandDamageReasonDamageCategory_.id)
                ),
                buildSpecification(criteria.getDamageTypeId(), root ->
                    root.join(CultivatedLandDamageReason_.damageType, JoinType.LEFT).get(CultivatedLandDamageReasonDamageType_.id)
                )
            );
        }
        return specification;
    }
}
