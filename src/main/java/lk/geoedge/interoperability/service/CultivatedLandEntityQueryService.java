package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.CultivatedLandEntity;
import lk.geoedge.interoperability.repository.CultivatedLandEntityRepository;
import lk.geoedge.interoperability.service.criteria.CultivatedLandEntityCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CultivatedLandEntity} entities in the database.
 * The main input is a {@link CultivatedLandEntityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CultivatedLandEntity} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CultivatedLandEntityQueryService extends QueryService<CultivatedLandEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandEntityQueryService.class);

    private final CultivatedLandEntityRepository cultivatedLandEntityRepository;

    public CultivatedLandEntityQueryService(CultivatedLandEntityRepository cultivatedLandEntityRepository) {
        this.cultivatedLandEntityRepository = cultivatedLandEntityRepository;
    }

    /**
     * Return a {@link Page} of {@link CultivatedLandEntity} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CultivatedLandEntity> findByCriteria(CultivatedLandEntityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CultivatedLandEntity> specification = createSpecification(criteria);
        return cultivatedLandEntityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CultivatedLandEntityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CultivatedLandEntity> specification = createSpecification(criteria);
        return cultivatedLandEntityRepository.count(specification);
    }

    /**
     * Function to convert {@link CultivatedLandEntityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CultivatedLandEntity> createSpecification(CultivatedLandEntityCriteria criteria) {
        Specification<CultivatedLandEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), CultivatedLandEntity_.id),
                buildStringSpecification(criteria.getLandStatus(), CultivatedLandEntity_.landStatus),
                buildRangeSpecification(criteria.getUrea(), CultivatedLandEntity_.urea),
                buildRangeSpecification(criteria.getMop(), CultivatedLandEntity_.mop),
                buildRangeSpecification(criteria.getTsp(), CultivatedLandEntity_.tsp),
                buildRangeSpecification(criteria.getCreatedAt(), CultivatedLandEntity_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), CultivatedLandEntity_.addedBy),
                buildSpecification(criteria.getFarmFieldId(), root ->
                    root.join(CultivatedLandEntity_.farmField, JoinType.LEFT).get(CultivatedLandFarmerFieldOwner_.id)
                ),
                buildSpecification(criteria.getSeasonId(), root ->
                    root.join(CultivatedLandEntity_.season, JoinType.LEFT).get(CultivatedLandSeason_.id)
                )
            );
        }
        return specification;
    }
}
