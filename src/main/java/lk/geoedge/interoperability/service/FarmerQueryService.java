package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.Farmer;
import lk.geoedge.interoperability.repository.FarmerRepository;
import lk.geoedge.interoperability.service.criteria.FarmerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Farmer} entities in the database.
 * The main input is a {@link FarmerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Farmer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FarmerQueryService extends QueryService<Farmer> {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerQueryService.class);

    private final FarmerRepository farmerRepository;

    public FarmerQueryService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    /**
     * Return a {@link Page} of {@link Farmer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Farmer> findByCriteria(FarmerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Farmer> specification = createSpecification(criteria);
        return farmerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FarmerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Farmer> specification = createSpecification(criteria);
        return farmerRepository.count(specification);
    }

    /**
     * Function to convert {@link FarmerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Farmer> createSpecification(FarmerCriteria criteria) {
        Specification<Farmer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Farmer_.id),
                buildStringSpecification(criteria.getFarmerId(), Farmer_.farmerId),
                buildStringSpecification(criteria.getFarmerName(), Farmer_.farmerName),
                buildStringSpecification(criteria.getNicNo(), Farmer_.nicNo),
                buildStringSpecification(criteria.getAddressFirstLine(), Farmer_.addressFirstLine),
                buildStringSpecification(criteria.getContactNoEmail(), Farmer_.contactNoEmail),
                buildRangeSpecification(criteria.getProvinceId(), Farmer_.provinceId),
                buildRangeSpecification(criteria.getDistrictId(), Farmer_.districtId),
                buildRangeSpecification(criteria.getDsId(), Farmer_.dsId),
                buildRangeSpecification(criteria.getGnId(), Farmer_.gnId),
                buildStringSpecification(criteria.getCity(), Farmer_.city),
                buildRangeSpecification(criteria.getAddedDate(), Farmer_.addedDate)
            );
        }
        return specification;
    }
}
