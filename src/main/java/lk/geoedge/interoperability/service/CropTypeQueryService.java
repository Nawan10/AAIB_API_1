package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CropType;
import lk.geoedge.interoperability.repository.CropTypeRepository;
import lk.geoedge.interoperability.service.criteria.CropTypeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropType} entities in the database.
 * The main input is a {@link CropTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CropType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropTypeQueryService extends QueryService<CropType> {

    private static final Logger LOG = LoggerFactory.getLogger(CropTypeQueryService.class);

    private final CropTypeRepository cropTypeRepository;

    public CropTypeQueryService(CropTypeRepository cropTypeRepository) {
        this.cropTypeRepository = cropTypeRepository;
    }

    /**
     * Return a {@link Page} of {@link CropType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CropType> findByCriteria(CropTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CropType> specification = createSpecification(criteria);
        return cropTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropType> specification = createSpecification(criteria);
        return cropTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CropTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropType> createSpecification(CropTypeCriteria criteria) {
        Specification<CropType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CropType_.id),
                buildStringSpecification(criteria.getCrop(), CropType_.crop),
                buildStringSpecification(criteria.getImage(), CropType_.image),
                buildRangeSpecification(criteria.getMainCrop(), CropType_.mainCrop),
                buildStringSpecification(criteria.getCropCode(), CropType_.cropCode),
                buildStringSpecification(criteria.getNoOfStages(), CropType_.noOfStages),
                buildStringSpecification(criteria.getDescription(), CropType_.description),
                buildRangeSpecification(criteria.getCropTypesId(), CropType_.cropTypesId),
                buildRangeSpecification(criteria.getUnitsId(), CropType_.unitsId),
                buildRangeSpecification(criteria.getArea(), CropType_.area),
                buildRangeSpecification(criteria.getSumInsured(), CropType_.sumInsured),
                buildRangeSpecification(criteria.getMinSumInsured(), CropType_.minSumInsured),
                buildRangeSpecification(criteria.getMaxSumInsured(), CropType_.maxSumInsured),
                buildRangeSpecification(criteria.getSubsidisedPremiumRate(), CropType_.subsidisedPremiumRate)
            );
        }
        return specification;
    }
}
