package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.FarmerFieldOwnerCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FarmerFieldOwnerCropType} entities in the database.
 * The main input is a {@link FarmerFieldOwnerCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FarmerFieldOwnerCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FarmerFieldOwnerCropTypeQueryService extends QueryService<FarmerFieldOwnerCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldOwnerCropTypeQueryService.class);

    private final FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository;

    public FarmerFieldOwnerCropTypeQueryService(FarmerFieldOwnerCropTypeRepository farmerFieldOwnerCropTypeRepository) {
        this.farmerFieldOwnerCropTypeRepository = farmerFieldOwnerCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link FarmerFieldOwnerCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FarmerFieldOwnerCropType> findByCriteria(FarmerFieldOwnerCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FarmerFieldOwnerCropType> specification = createSpecification(criteria);
        return farmerFieldOwnerCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FarmerFieldOwnerCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FarmerFieldOwnerCropType> specification = createSpecification(criteria);
        return farmerFieldOwnerCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FarmerFieldOwnerCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FarmerFieldOwnerCropType> createSpecification(FarmerFieldOwnerCropTypeCriteria criteria) {
        Specification<FarmerFieldOwnerCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FarmerFieldOwnerCropType_.id),
                buildStringSpecification(criteria.getCrop(), FarmerFieldOwnerCropType_.crop),
                buildStringSpecification(criteria.getImage(), FarmerFieldOwnerCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), FarmerFieldOwnerCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), FarmerFieldOwnerCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), FarmerFieldOwnerCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), FarmerFieldOwnerCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), FarmerFieldOwnerCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), FarmerFieldOwnerCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), FarmerFieldOwnerCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), FarmerFieldOwnerCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), FarmerFieldOwnerCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), FarmerFieldOwnerCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), FarmerFieldOwnerCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
