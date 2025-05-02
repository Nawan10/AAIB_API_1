package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.FarmerFieldOwner;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerRepository;
import lk.geoedge.interoperability.service.criteria.FarmerFieldOwnerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FarmerFieldOwner} entities in the database.
 * The main input is a {@link FarmerFieldOwnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FarmerFieldOwner} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FarmerFieldOwnerQueryService extends QueryService<FarmerFieldOwner> {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldOwnerQueryService.class);

    private final FarmerFieldOwnerRepository farmerFieldOwnerRepository;

    public FarmerFieldOwnerQueryService(FarmerFieldOwnerRepository farmerFieldOwnerRepository) {
        this.farmerFieldOwnerRepository = farmerFieldOwnerRepository;
    }

    /**
     * Return a {@link Page} of {@link FarmerFieldOwner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FarmerFieldOwner> findByCriteria(FarmerFieldOwnerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FarmerFieldOwner> specification = createSpecification(criteria);
        return farmerFieldOwnerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FarmerFieldOwnerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FarmerFieldOwner> specification = createSpecification(criteria);
        return farmerFieldOwnerRepository.count(specification);
    }

    /**
     * Function to convert {@link FarmerFieldOwnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FarmerFieldOwner> createSpecification(FarmerFieldOwnerCriteria criteria) {
        Specification<FarmerFieldOwner> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FarmerFieldOwner_.id),
                buildStringSpecification(criteria.getLandPlotName(), FarmerFieldOwner_.landPlotName),
                buildStringSpecification(criteria.getLandRegistryNo(), FarmerFieldOwner_.landRegistryNo),
                buildRangeSpecification(criteria.getTotalLandExtent(), FarmerFieldOwner_.totalLandExtent),
                buildRangeSpecification(criteria.getCalculatedArea(), FarmerFieldOwner_.calculatedArea),
                buildStringSpecification(criteria.getProvinceId(), FarmerFieldOwner_.provinceId),
                buildStringSpecification(criteria.getDistrictId(), FarmerFieldOwner_.districtId),
                buildStringSpecification(criteria.getDsId(), FarmerFieldOwner_.dsId),
                buildStringSpecification(criteria.getGnId(), FarmerFieldOwner_.gnId),
                buildRangeSpecification(criteria.getCenterLat(), FarmerFieldOwner_.centerLat),
                buildRangeSpecification(criteria.getCenterLng(), FarmerFieldOwner_.centerLng),
                buildSpecification(criteria.getCropId(), root ->
                    root.join(FarmerFieldOwner_.crop, JoinType.LEFT).get(FarmerFieldOwnerCropType_.id)
                )
            );
        }
        return specification;
    }
}
