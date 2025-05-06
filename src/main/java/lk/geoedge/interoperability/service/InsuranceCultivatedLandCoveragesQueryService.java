package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCultivatedLandCoveragesCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCultivatedLandCoverages} entities in the database.
 * The main input is a {@link InsuranceCultivatedLandCoveragesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCultivatedLandCoverages} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCultivatedLandCoveragesQueryService extends QueryService<InsuranceCultivatedLandCoverages> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCoveragesQueryService.class);

    private final InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository;

    public InsuranceCultivatedLandCoveragesQueryService(
        InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository
    ) {
        this.insuranceCultivatedLandCoveragesRepository = insuranceCultivatedLandCoveragesRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCultivatedLandCoverages} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCultivatedLandCoverages> findByCriteria(InsuranceCultivatedLandCoveragesCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCultivatedLandCoverages> specification = createSpecification(criteria);
        return insuranceCultivatedLandCoveragesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCultivatedLandCoveragesCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCultivatedLandCoverages> specification = createSpecification(criteria);
        return insuranceCultivatedLandCoveragesRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCultivatedLandCoveragesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCultivatedLandCoverages> createSpecification(InsuranceCultivatedLandCoveragesCriteria criteria) {
        Specification<InsuranceCultivatedLandCoverages> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCultivatedLandCoverages_.id),
                buildRangeSpecification(criteria.getConverageAmount(), InsuranceCultivatedLandCoverages_.converageAmount),
                buildSpecification(criteria.getIsSelect(), InsuranceCultivatedLandCoverages_.isSelect),
                buildRangeSpecification(criteria.getCreatedAt(), InsuranceCultivatedLandCoverages_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), InsuranceCultivatedLandCoverages_.addedBy),
                buildSpecification(criteria.getInsuranceCultivatedLandId(), root ->
                    root
                        .join(InsuranceCultivatedLandCoverages_.insuranceCultivatedLand, JoinType.LEFT)
                        .get(InsuranceCultivatedLandCoveragesInsuranceCultivatedLand_.id)
                ),
                buildSpecification(criteria.getIndexCoverageId(), root ->
                    root.join(InsuranceCultivatedLandCoverages_.indexCoverage, JoinType.LEFT).get(IndexCoverages_.id)
                )
            );
        }
        return specification;
    }
}
