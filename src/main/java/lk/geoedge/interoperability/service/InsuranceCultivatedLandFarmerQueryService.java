package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandFarmerRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandFarmerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCultivatedLandFarmer} entities in the database.
 * The main input is a {@link InsuranceCultivatedLandFarmerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCultivatedLandFarmer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCultivatedLandFarmerQueryService extends QueryService<InsuranceCultivatedLandFarmer> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandFarmerQueryService.class);

    private final InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository;

    public InsuranceCultivatedLandFarmerQueryService(InsuranceCultivatedLandFarmerRepository insuranceCultivatedLandFarmerRepository) {
        this.insuranceCultivatedLandFarmerRepository = insuranceCultivatedLandFarmerRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCultivatedLandFarmer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCultivatedLandFarmer> findByCriteria(InsuranceCultivatedLandFarmerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCultivatedLandFarmer> specification = createSpecification(criteria);
        return insuranceCultivatedLandFarmerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCultivatedLandFarmerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCultivatedLandFarmer> specification = createSpecification(criteria);
        return insuranceCultivatedLandFarmerRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCultivatedLandFarmerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCultivatedLandFarmer> createSpecification(InsuranceCultivatedLandFarmerCriteria criteria) {
        Specification<InsuranceCultivatedLandFarmer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCultivatedLandFarmer_.id),
                buildStringSpecification(criteria.getFarmerId(), InsuranceCultivatedLandFarmer_.farmerId),
                buildStringSpecification(criteria.getFarmerName(), InsuranceCultivatedLandFarmer_.farmerName),
                buildStringSpecification(criteria.getNicNo(), InsuranceCultivatedLandFarmer_.nicNo),
                buildStringSpecification(criteria.getAddressFirstLine(), InsuranceCultivatedLandFarmer_.addressFirstLine),
                buildStringSpecification(criteria.getContactNoEmail(), InsuranceCultivatedLandFarmer_.contactNoEmail),
                buildRangeSpecification(criteria.getProvinceId(), InsuranceCultivatedLandFarmer_.provinceId),
                buildRangeSpecification(criteria.getDistrictId(), InsuranceCultivatedLandFarmer_.districtId),
                buildRangeSpecification(criteria.getDsId(), InsuranceCultivatedLandFarmer_.dsId),
                buildRangeSpecification(criteria.getGnId(), InsuranceCultivatedLandFarmer_.gnId),
                buildStringSpecification(criteria.getCity(), InsuranceCultivatedLandFarmer_.city),
                buildRangeSpecification(criteria.getAddedDate(), InsuranceCultivatedLandFarmer_.addedDate)
            );
        }
        return specification;
    }
}
