package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.InsuranceCrop;
import lk.geoedge.interoperability.repository.InsuranceCropRepository;
import lk.geoedge.interoperability.service.criteria.InsuranceCropCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InsuranceCrop} entities in the database.
 * The main input is a {@link InsuranceCropCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsuranceCrop} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsuranceCropQueryService extends QueryService<InsuranceCrop> {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCropQueryService.class);

    private final InsuranceCropRepository insuranceCropRepository;

    public InsuranceCropQueryService(InsuranceCropRepository insuranceCropRepository) {
        this.insuranceCropRepository = insuranceCropRepository;
    }

    /**
     * Return a {@link Page} of {@link InsuranceCrop} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsuranceCrop> findByCriteria(InsuranceCropCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsuranceCrop> specification = createSpecification(criteria);
        return insuranceCropRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsuranceCropCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsuranceCrop> specification = createSpecification(criteria);
        return insuranceCropRepository.count(specification);
    }

    /**
     * Function to convert {@link InsuranceCropCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsuranceCrop> createSpecification(InsuranceCropCriteria criteria) {
        Specification<InsuranceCrop> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InsuranceCrop_.id),
                buildStringSpecification(criteria.getPolicyId(), InsuranceCrop_.policyId),
                buildRangeSpecification(criteria.getYield(), InsuranceCrop_.yield),
                buildRangeSpecification(criteria.getCreatedAt(), InsuranceCrop_.createdAt),
                buildStringSpecification(criteria.getAddedBy(), InsuranceCrop_.addedBy),
                buildSpecification(criteria.getCropId(), root ->
                    root.join(InsuranceCrop_.crop, JoinType.LEFT).get(InsuranceCropCropType_.id)
                )
            );
        }
        return specification;
    }
}
