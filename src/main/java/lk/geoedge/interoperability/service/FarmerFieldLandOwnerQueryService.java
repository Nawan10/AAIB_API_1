package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.FarmerFieldLandOwner;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerRepository;
import lk.geoedge.interoperability.service.criteria.FarmerFieldLandOwnerCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FarmerFieldLandOwner} entities in the database.
 * The main input is a {@link FarmerFieldLandOwnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FarmerFieldLandOwner} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FarmerFieldLandOwnerQueryService extends QueryService<FarmerFieldLandOwner> {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldLandOwnerQueryService.class);

    private final FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository;

    public FarmerFieldLandOwnerQueryService(FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository) {
        this.farmerFieldLandOwnerRepository = farmerFieldLandOwnerRepository;
    }

    /**
     * Return a {@link Page} of {@link FarmerFieldLandOwner} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FarmerFieldLandOwner> findByCriteria(FarmerFieldLandOwnerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FarmerFieldLandOwner> specification = createSpecification(criteria);
        return farmerFieldLandOwnerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FarmerFieldLandOwnerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FarmerFieldLandOwner> specification = createSpecification(criteria);
        return farmerFieldLandOwnerRepository.count(specification);
    }

    /**
     * Function to convert {@link FarmerFieldLandOwnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FarmerFieldLandOwner> createSpecification(FarmerFieldLandOwnerCriteria criteria) {
        Specification<FarmerFieldLandOwner> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FarmerFieldLandOwner_.id),
                buildRangeSpecification(criteria.getCreatedAt(), FarmerFieldLandOwner_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), FarmerFieldLandOwner_.addedBy),
                buildSpecification(criteria.getFarmerFieldOwnerId(), root ->
                    root.join(FarmerFieldLandOwner_.farmerFieldOwner, JoinType.LEFT).get(FarmerFieldOwner_.id)
                ),
                buildSpecification(criteria.getFarmerId(), root ->
                    root.join(FarmerFieldLandOwner_.farmer, JoinType.LEFT).get(FarmerFieldLandOwnerFarmer_.id)
                )
            );
        }
        return specification;
    }
}
