package lk.geoedge.interoperability.service;

import jakarta.persistence.criteria.JoinType;
import lk.geoedge.interoperability.domain.*; // for static metamodels
import lk.geoedge.interoperability.domain.IndexPayoutEventList;
import lk.geoedge.interoperability.repository.IndexPayoutEventListRepository;
import lk.geoedge.interoperability.service.criteria.IndexPayoutEventListCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndexPayoutEventList} entities in the database.
 * The main input is a {@link IndexPayoutEventListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link IndexPayoutEventList} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndexPayoutEventListQueryService extends QueryService<IndexPayoutEventList> {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListQueryService.class);

    private final IndexPayoutEventListRepository indexPayoutEventListRepository;

    public IndexPayoutEventListQueryService(IndexPayoutEventListRepository indexPayoutEventListRepository) {
        this.indexPayoutEventListRepository = indexPayoutEventListRepository;
    }

    /**
     * Return a {@link Page} of {@link IndexPayoutEventList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexPayoutEventList> findByCriteria(IndexPayoutEventListCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IndexPayoutEventList> specification = createSpecification(criteria);
        return indexPayoutEventListRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndexPayoutEventListCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndexPayoutEventList> specification = createSpecification(criteria);
        return indexPayoutEventListRepository.count(specification);
    }

    /**
     * Function to convert {@link IndexPayoutEventListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndexPayoutEventList> createSpecification(IndexPayoutEventListCriteria criteria) {
        Specification<IndexPayoutEventList> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), IndexPayoutEventList_.id),
                buildRangeSpecification(criteria.getIndexPayoutEventId(), IndexPayoutEventList_.indexPayoutEventId),
                buildRangeSpecification(criteria.getAscId(), IndexPayoutEventList_.ascId),
                buildRangeSpecification(criteria.getConfirmedBy(), IndexPayoutEventList_.confirmedBy),
                buildRangeSpecification(criteria.getCultivatedExtent(), IndexPayoutEventList_.cultivatedExtent),
                buildRangeSpecification(criteria.getPayout(), IndexPayoutEventList_.payout),
                buildStringSpecification(criteria.getConfirmedDate(), IndexPayoutEventList_.confirmedDate),
                buildRangeSpecification(criteria.getRejectedBy(), IndexPayoutEventList_.rejectedBy),
                buildStringSpecification(criteria.getRejectedDate(), IndexPayoutEventList_.rejectedDate),
                buildStringSpecification(criteria.getReason(), IndexPayoutEventList_.reason),
                buildRangeSpecification(criteria.getFinalPayout(), IndexPayoutEventList_.finalPayout),
                buildRangeSpecification(criteria.getIndexPayoutEventStatus(), IndexPayoutEventList_.indexPayoutEventStatus),
                buildRangeSpecification(criteria.getIsApproved(), IndexPayoutEventList_.isApproved),
                buildRangeSpecification(criteria.getMonitoringRange(), IndexPayoutEventList_.monitoringRange),
                buildRangeSpecification(criteria.getIsInsurance(), IndexPayoutEventList_.isInsurance),
                buildRangeSpecification(criteria.getInsuranceCultivatedLand(), IndexPayoutEventList_.insuranceCultivatedLand),
                buildRangeSpecification(criteria.getIndexChequeId(), IndexPayoutEventList_.indexChequeId),
                buildRangeSpecification(criteria.getIndexProductId(), IndexPayoutEventList_.indexProductId),
                buildSpecification(criteria.getCultivatedFarmerId(), root ->
                    root.join(IndexPayoutEventList_.cultivatedFarmer, JoinType.LEFT).get(IndexPayoutEventListFarmer_.id)
                ),
                buildSpecification(criteria.getCultivatedLandId(), root ->
                    root.join(IndexPayoutEventList_.cultivatedLand, JoinType.LEFT).get(IndexPayoutEventListCultivatedLand_.id)
                ),
                buildSpecification(criteria.getSeasonId(), root ->
                    root.join(IndexPayoutEventList_.season, JoinType.LEFT).get(IndexPayoutEventListSeason_.id)
                )
            );
        }
        return specification;
    }
}
