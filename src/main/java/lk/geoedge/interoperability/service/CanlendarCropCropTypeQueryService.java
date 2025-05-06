package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CanlendarCropCropType;
import lk.geoedge.interoperability.repository.CanlendarCropCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.CanlendarCropCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CanlendarCropCropType} entities in the database.
 * The main input is a {@link CanlendarCropCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CanlendarCropCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CanlendarCropCropTypeQueryService extends QueryService<CanlendarCropCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropCropTypeQueryService.class);

    private final CanlendarCropCropTypeRepository canlendarCropCropTypeRepository;

    public CanlendarCropCropTypeQueryService(CanlendarCropCropTypeRepository canlendarCropCropTypeRepository) {
        this.canlendarCropCropTypeRepository = canlendarCropCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link CanlendarCropCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CanlendarCropCropType> findByCriteria(CanlendarCropCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CanlendarCropCropType> specification = createSpecification(criteria);
        return canlendarCropCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CanlendarCropCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CanlendarCropCropType> specification = createSpecification(criteria);
        return canlendarCropCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CanlendarCropCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CanlendarCropCropType> createSpecification(CanlendarCropCropTypeCriteria criteria) {
        Specification<CanlendarCropCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CanlendarCropCropType_.id),
                buildStringSpecification(criteria.getCrop(), CanlendarCropCropType_.crop),
                buildStringSpecification(criteria.getImage(), CanlendarCropCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), CanlendarCropCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), CanlendarCropCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), CanlendarCropCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), CanlendarCropCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), CanlendarCropCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), CanlendarCropCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), CanlendarCropCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), CanlendarCropCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), CanlendarCropCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), CanlendarCropCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), CanlendarCropCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
