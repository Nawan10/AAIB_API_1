package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersFarmerRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmersFarmerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandFarmersFarmer} entities in the database.
 * The main input is a {@link CultivatedLandFarmersFarmerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandFarmersFarmer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandFarmersFarmerQueryService extends QueryService<CultivatedLandFarmersFarmer> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmersFarmerQueryService.class);

    private final CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository;

    public CultivatedLandFarmersFarmerQueryService(CultivatedLandFarmersFarmerRepository cultivatedLandFarmersFarmerRepository) {
        this.cultivatedLandFarmersFarmerRepository = cultivatedLandFarmersFarmerRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandFarmersFarmer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandFarmersFarmer> findByCriteria(CultivatedLandFarmersFarmerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandFarmersFarmer> specification = createSpecification(criteria);
        return cultivatedLandFarmersFarmerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandFarmersFarmerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandFarmersFarmer> specification = createSpecification(criteria);
        return cultivatedLandFarmersFarmerRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandFarmersFarmerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandFarmersFarmer> createSpecification(CultivatedLandFarmersFarmerCriteria criteria) {
        Specification<CultivatedLandFarmersFarmer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandFarmersFarmer_.id),
                buildStringSpecification(criteria.getFarmerId(), CultivatedLandFarmersFarmer_.farmerId),
                buildStringSpecification(criteria.getFarmerName(), CultivatedLandFarmersFarmer_.farmerName),
                buildStringSpecification(criteria.getNicNo(), CultivatedLandFarmersFarmer_.nicNo),
                buildStringSpecification(criteria.getAddressFirstLine(), CultivatedLandFarmersFarmer_.addressFirstLine),
                buildStringSpecification(criteria.getContactNoEmail(), CultivatedLandFarmersFarmer_.contactNoEmail),
                buildRangeSpecification(criteria.getProvinceId(), CultivatedLandFarmersFarmer_.provinceId),
                buildRangeSpecification(criteria.getDistrictId(), CultivatedLandFarmersFarmer_.districtId),
                buildRangeSpecification(criteria.getDsId(), CultivatedLandFarmersFarmer_.dsId),
                buildRangeSpecification(criteria.getGnId(), CultivatedLandFarmersFarmer_.gnId),
                buildStringSpecification(criteria.getCity(), CultivatedLandFarmersFarmer_.city),
                buildRangeSpecification(criteria.getAddedDate(), CultivatedLandFarmersFarmer_.addedDate)
            );
        }
        return specification;
    }
}
