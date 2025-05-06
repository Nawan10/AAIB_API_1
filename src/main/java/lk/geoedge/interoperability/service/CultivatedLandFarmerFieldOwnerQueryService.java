package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerFieldOwnerRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmerFieldOwnerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandFarmerFieldOwner} entities in the database.
 * The main input is a {@link CultivatedLandFarmerFieldOwnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandFarmerFieldOwner} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandFarmerFieldOwnerQueryService extends QueryService<CultivatedLandFarmerFieldOwner> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmerFieldOwnerQueryService.class);

    private final CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository;

    public CultivatedLandFarmerFieldOwnerQueryService(CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository) {
        this.cultivatedLandFarmerFieldOwnerRepository = cultivatedLandFarmerFieldOwnerRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandFarmerFieldOwner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandFarmerFieldOwner> findByCriteria(CultivatedLandFarmerFieldOwnerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandFarmerFieldOwner> specification = createSpecification(criteria);
        return cultivatedLandFarmerFieldOwnerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandFarmerFieldOwnerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandFarmerFieldOwner> specification = createSpecification(criteria);
        return cultivatedLandFarmerFieldOwnerRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandFarmerFieldOwnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandFarmerFieldOwner> createSpecification(CultivatedLandFarmerFieldOwnerCriteria criteria) {
        Specification<CultivatedLandFarmerFieldOwner> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandFarmerFieldOwner_.id),
                buildStringSpecification(criteria.getLandPlotName(), CultivatedLandFarmerFieldOwner_.landPlotName),
                buildStringSpecification(criteria.getLandRegistryNo(), CultivatedLandFarmerFieldOwner_.landRegistryNo),
                buildRangeSpecification(criteria.getTotalLandExtent(), CultivatedLandFarmerFieldOwner_.totalLandExtent),
                buildRangeSpecification(criteria.getCalculatedArea(), CultivatedLandFarmerFieldOwner_.calculatedArea),
                buildStringSpecification(criteria.getProvinceId(), CultivatedLandFarmerFieldOwner_.provinceId),
                buildStringSpecification(criteria.getDistrictId(), CultivatedLandFarmerFieldOwner_.districtId),
                buildStringSpecification(criteria.getDsId(), CultivatedLandFarmerFieldOwner_.dsId),
                buildStringSpecification(criteria.getGnId(), CultivatedLandFarmerFieldOwner_.gnId),
                buildRangeSpecification(criteria.getCenterLat(), CultivatedLandFarmerFieldOwner_.centerLat),
                buildRangeSpecification(criteria.getCenterLng(), CultivatedLandFarmerFieldOwner_.centerLng)
            );
        }
        return specification;
    }
}
