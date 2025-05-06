package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer;
import lk.geoedge.interoperability.repository.IndexPayoutEventListFarmerRepository;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListFarmerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPayoutEventListFarmer} entities in the database.
 * The main input is a {@link IndexPayoutEventListFarmerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPayoutEventListFarmer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPayoutEventListFarmerQueryService extends QueryService<IndexPayoutEventListFarmer> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListFarmerQueryService.class);

    private final IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository;

    public IndexPayoutEventListFarmerQueryService(IndexPayoutEventListFarmerRepository indexPayoutEventListFarmerRepository) {
        this.indexPayoutEventListFarmerRepository = indexPayoutEventListFarmerRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPayoutEventListFarmer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPayoutEventListFarmer> findByCriteria(IndexPayoutEventListFarmerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPayoutEventListFarmer> specification = createSpecification(criteria);
        return indexPayoutEventListFarmerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPayoutEventListFarmerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPayoutEventListFarmer> specification = createSpecification(criteria);
        return indexPayoutEventListFarmerRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPayoutEventListFarmerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPayoutEventListFarmer> createSpecification(IndexPayoutEventListFarmerCriteria criteria) {
        Specification<IndexPayoutEventListFarmer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPayoutEventListFarmer_.id),
                buildStringSpecification(criteria.getFarmerId(), IndexPayoutEventListFarmer_.farmerId),
                buildStringSpecification(criteria.getFarmerName(), IndexPayoutEventListFarmer_.farmerName),
                buildStringSpecification(criteria.getNicNo(), IndexPayoutEventListFarmer_.nicNo),
                buildStringSpecification(criteria.getAddressFirstLine(), IndexPayoutEventListFarmer_.addressFirstLine),
                buildStringSpecification(criteria.getContactNoEmail(), IndexPayoutEventListFarmer_.contactNoEmail),
                buildRangeSpecification(criteria.getProvinceId(), IndexPayoutEventListFarmer_.provinceId),
                buildRangeSpecification(criteria.getDistrictId(), IndexPayoutEventListFarmer_.districtId),
                buildRangeSpecification(criteria.getDsId(), IndexPayoutEventListFarmer_.dsId),
                buildRangeSpecification(criteria.getGnId(), IndexPayoutEventListFarmer_.gnId),
                buildStringSpecification(criteria.getCity(), IndexPayoutEventListFarmer_.city),
                buildRangeSpecification(criteria.getAddedDate(), IndexPayoutEventListFarmer_.addedDate)
            );
        }
        return specification;
    }
}
