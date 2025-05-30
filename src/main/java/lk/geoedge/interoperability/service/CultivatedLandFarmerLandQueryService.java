package lk.geoedge.interoperability.service;

import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandFarmerLand;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerLandRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmerLandCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandFarmerLand} entities in the database.
 * The main input is a {@link CultivatedLandFarmerLandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandFarmerLand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandFarmerLandQueryService extends QueryService<CultivatedLandFarmerLand> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmerLandQueryService.class);

    private final CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository;

    public CultivatedLandFarmerLandQueryService(CultivatedLandFarmerLandRepository cultivatedLandFarmerLandRepository) {
        this.cultivatedLandFarmerLandRepository = cultivatedLandFarmerLandRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandFarmerLand} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandFarmerLand> findByCriteria(CultivatedLandFarmerLandCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandFarmerLand> specification = createSpecification(criteria);
        return cultivatedLandFarmerLandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandFarmerLandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandFarmerLand> specification = createSpecification(criteria);
        return cultivatedLandFarmerLandRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandFarmerLandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandFarmerLand> createSpecification(CultivatedLandFarmerLandCriteria criteria) {
        Specification<CultivatedLandFarmerLand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandFarmerLand_.id),
                buildStringSpecification(criteria.getLandStatus(), CultivatedLandFarmerLand_.landStatus),
                buildRangeSpecification(criteria.getUrea(), CultivatedLandFarmerLand_.urea),
                buildRangeSpecification(criteria.getMop(), CultivatedLandFarmerLand_.mop),
                buildRangeSpecification(criteria.getTsp(), CultivatedLandFarmerLand_.tsp),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedLandFarmerLand_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedLandFarmerLand_.addedBy)
            );
        }
        return specification;
    }
}
