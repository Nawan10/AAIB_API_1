package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropVarietyCropType;
import lk.geoedge.interoperability.repository.CropVarietyCropTypeRepository;
import lk.geoedge.interoperability.service.criteria.CropVarietyCropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropVarietyCropType} entities in the database.
 * The main input is a {@link CropVarietyCropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropVarietyCropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropVarietyCropTypeQueryService extends QueryService<CropVarietyCropType> {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyCropTypeQueryService.class);

    private final CropVarietyCropTypeRepository cropVarietyCropTypeRepository;

    public CropVarietyCropTypeQueryService(CropVarietyCropTypeRepository cropVarietyCropTypeRepository) {
        this.cropVarietyCropTypeRepository = cropVarietyCropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link CropVarietyCropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropVarietyCropType> findByCriteria(CropVarietyCropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropVarietyCropType> specification = createSpecification(criteria);
        return cropVarietyCropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropVarietyCropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropVarietyCropType> specification = createSpecification(criteria);
        return cropVarietyCropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CropVarietyCropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropVarietyCropType> createSpecification(CropVarietyCropTypeCriteria criteria) {
        Specification<CropVarietyCropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropVarietyCropType_.id),
                buildStringSpecification(criteria.getCrop(), CropVarietyCropType_.crop),
                buildStringSpecification(criteria.getImage(), CropVarietyCropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), CropVarietyCropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), CropVarietyCropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), CropVarietyCropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), CropVarietyCropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), CropVarietyCropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), CropVarietyCropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), CropVarietyCropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), CropVarietyCropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), CropVarietyCropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), CropVarietyCropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), CropVarietyCropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
