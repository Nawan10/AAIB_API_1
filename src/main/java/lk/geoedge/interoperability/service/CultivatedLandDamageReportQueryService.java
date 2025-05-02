package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandDamageReport;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandDamageReportCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandDamageReport} entities in the database.
 * The main input is a {@link CultivatedLandDamageReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandDamageReport} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandDamageReportQueryService extends QueryService<CultivatedLandDamageReport> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportQueryService.class);

    private final CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository;

    public CultivatedLandDamageReportQueryService(CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository) {
        this.cultivatedLandDamageReportRepository = cultivatedLandDamageReportRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandDamageReport} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandDamageReport> findByCriteria(CultivatedLandDamageReportCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandDamageReport> specification = createSpecification(criteria);
        return cultivatedLandDamageReportRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandDamageReportCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandDamageReport> specification = createSpecification(criteria);
        return cultivatedLandDamageReportRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandDamageReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandDamageReport> createSpecification(CultivatedLandDamageReportCriteria criteria) {
        Specification<CultivatedLandDamageReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandDamageReport_.id),
                buildStringSpecification(criteria.getDamageReasonId(), CultivatedLandDamageReport_.damageReasonId),
                buildStringSpecification(criteria.getDamageServerityId(), CultivatedLandDamageReport_.damageServerityId),
                buildStringSpecification(criteria.getDamageDateMonitor(), CultivatedLandDamageReport_.damageDateMonitor),
                buildStringSpecification(criteria.getDescription(), CultivatedLandDamageReport_.description),
                buildStringSpecification(criteria.getFarmerComment(), CultivatedLandDamageReport_.farmerComment),
                buildStringSpecification(criteria.getEstimatedYield(), CultivatedLandDamageReport_.estimatedYield),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedLandDamageReport_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedLandDamageReport_.addedBy),
                buildSpecification(criteria.getCropId(), root ->
                    root.join(CultivatedLandDamageReport_.crop, JoinType.LEFT).get(CropType_.id)
                ),
                buildSpecification(criteria.getDamageCategoryId(), root ->
                    root.join(CultivatedLandDamageReport_.damageCategory, JoinType.LEFT).get(CultivatedLandDamageReportDamageCategory_.id)
                ),
                buildSpecification(criteria.getDamageTypeId(), root ->
                    root.join(CultivatedLandDamageReport_.damageType, JoinType.LEFT).get(CultivatedLandDamageReportDamageType_.id)
                )
            );
        }
        return specification;
    }
}
