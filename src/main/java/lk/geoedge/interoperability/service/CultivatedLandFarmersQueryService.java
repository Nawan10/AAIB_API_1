package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandFarmers;
import lk.geoedge.interoperability.repository.CultivatedLandFarmersRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandFarmersCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandFarmers} entities in the database.
 * The main input is a {@link CultivatedLandFarmersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandFarmers} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandFarmersQueryService extends QueryService<CultivatedLandFarmers> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmersQueryService.class);

    private final CultivatedLandFarmersRepository cultivatedLandFarmersRepository;

    public CultivatedLandFarmersQueryService(CultivatedLandFarmersRepository cultivatedLandFarmersRepository) {
        this.cultivatedLandFarmersRepository = cultivatedLandFarmersRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandFarmers} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandFarmers> findByCriteria(CultivatedLandFarmersCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandFarmers> specification = createSpecification(criteria);
        return cultivatedLandFarmersRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandFarmersCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandFarmers> specification = createSpecification(criteria);
        return cultivatedLandFarmersRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandFarmersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandFarmers> createSpecification(CultivatedLandFarmersCriteria criteria) {
        Specification<CultivatedLandFarmers> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandFarmers_.id),
                buildRangeSpecification(criteria.getRelationId(), CultivatedLandFarmers_.relationId),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedLandFarmers_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedLandFarmers_.addedBy),
                buildSpecification(criteria.getFarmerId(), root ->
                    root.join(CultivatedLandFarmers_.farmer, JoinType.LEFT).get(CultivatedLandFarmersFarmer_.id)
                ),
                buildSpecification(criteria.getCultivatedLandId(), root ->
                    root.join(CultivatedLandFarmers_.cultivatedLand, JoinType.LEFT).get(CultivatedLandFarmerLand_.id)
                )
            );
        }
        return specification;
    }
}
