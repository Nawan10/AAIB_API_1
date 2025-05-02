package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedCropCropType;
import lk.geoedge.interoperability.repository.CultivatedCropCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedCropCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedCropCropType} entities in the database.
 * The main input is a {@link CultivatedCropCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedCropCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedCropCropTypeQueryService extends QueryService<CultivatedCropCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedCropCropTypeQueryService.class);

    private final CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository;

    public CultivatedCropCropTypeQueryService(CultivatedCropCropTypeRepository cultivatedCropCropTypeRepository) {
        this.cultivatedCropCropTypeRepository = cultivatedCropCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedCropCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedCropCropType> findByCriteria(CultivatedCropCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedCropCropType> specification = createSpecification(criteria);
        return cultivatedCropCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedCropCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedCropCropType> specification = createSpecification(criteria);
        return cultivatedCropCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedCropCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedCropCropType> createSpecification(CultivatedCropCropTypeCriteria criteria) {
        Specification<CultivatedCropCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedCropCropType_.id),
                buildStringSpecification(criteria.getCrop(), CultivatedCropCropType_.crop),
                buildStringSpecification(criteria.getImage(), CultivatedCropCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), CultivatedCropCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), CultivatedCropCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), CultivatedCropCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), CultivatedCropCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), CultivatedCropCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), CultivatedCropCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), CultivatedCropCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), CultivatedCropCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), CultivatedCropCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), CultivatedCropCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), CultivatedCropCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
