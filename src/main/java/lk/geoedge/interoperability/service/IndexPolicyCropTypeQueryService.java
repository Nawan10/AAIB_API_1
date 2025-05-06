package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPolicyCropType;
import lk.geoedge.interoperability.repository.IndexPolicyCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.IndexPolicyCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPolicyCropType} entities in the database.
 * The main input is a {@link IndexPolicyCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPolicyCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPolicyCropTypeQueryService extends QueryService<IndexPolicyCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyCropTypeQueryService.class);

    private final IndexPolicyCropTypeRepository indexPolicyCropTypeRepository;

    public IndexPolicyCropTypeQueryService(IndexPolicyCropTypeRepository indexPolicyCropTypeRepository) {
        this.indexPolicyCropTypeRepository = indexPolicyCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPolicyCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPolicyCropType> findByCriteria(IndexPolicyCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPolicyCropType> specification = createSpecification(criteria);
        return indexPolicyCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPolicyCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPolicyCropType> specification = createSpecification(criteria);
        return indexPolicyCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPolicyCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPolicyCropType> createSpecification(IndexPolicyCropTypeCriteria criteria) {
        Specification<IndexPolicyCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPolicyCropType_.id),
                buildStringSpecification(criteria.getCrop(), IndexPolicyCropType_.crop),
                buildStringSpecification(criteria.getImage(), IndexPolicyCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), IndexPolicyCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), IndexPolicyCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), IndexPolicyCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), IndexPolicyCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), IndexPolicyCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), IndexPolicyCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), IndexPolicyCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), IndexPolicyCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), IndexPolicyCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), IndexPolicyCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), IndexPolicyCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
