package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventList;
import lk.geoedge.interoperability.repository.IndexPayoutEventListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventList}.
 */
@Service
@Transactional
public class IndexPayoutEventListService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListService.class);

    private final IndexPayoutEventListRepository indexPayoutEventListRepository;

    public IndexPayoutEventListService(IndexPayoutEventListRepository indexPayoutEventListRepository) {
        this.indexPayoutEventListRepository = indexPayoutEventListRepository;
    }

    /**
     * Save a indexPayoutEventList.
     *
     * @param indexPayoutEventList the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventList save(IndexPayoutEventList indexPayoutEventList) {
        LOG.debug("Request to save IndexPayoutEventList : {}", indexPayoutEventList);
        return indexPayoutEventListRepository.save(indexPayoutEventList);
    }

    /**
     * Update a indexPayoutEventList.
     *
     * @param indexPayoutEventList the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventList update(IndexPayoutEventList indexPayoutEventList) {
        LOG.debug("Request to update IndexPayoutEventList : {}", indexPayoutEventList);
        return indexPayoutEventListRepository.save(indexPayoutEventList);
    }

    /**
     * Partially update a indexPayoutEventList.
     *
     * @param indexPayoutEventList the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPayoutEventList> partialUpdate(IndexPayoutEventList indexPayoutEventList) {
        LOG.debug("Request to partially update IndexPayoutEventList : {}", indexPayoutEventList);

        return indexPayoutEventListRepository
            .findById(indexPayoutEventList.getId())
            .map(existingIndexPayoutEventList -> {
                if (indexPayoutEventList.getIndexPayoutEventId() != null) {
                    existingIndexPayoutEventList.setIndexPayoutEventId(indexPayoutEventList.getIndexPayoutEventId());
                }
                if (indexPayoutEventList.getAscId() != null) {
                    existingIndexPayoutEventList.setAscId(indexPayoutEventList.getAscId());
                }
                if (indexPayoutEventList.getConfirmedBy() != null) {
                    existingIndexPayoutEventList.setConfirmedBy(indexPayoutEventList.getConfirmedBy());
                }
                if (indexPayoutEventList.getCultivatedExtent() != null) {
                    existingIndexPayoutEventList.setCultivatedExtent(indexPayoutEventList.getCultivatedExtent());
                }
                if (indexPayoutEventList.getPayout() != null) {
                    existingIndexPayoutEventList.setPayout(indexPayoutEventList.getPayout());
                }
                if (indexPayoutEventList.getConfirmedDate() != null) {
                    existingIndexPayoutEventList.setConfirmedDate(indexPayoutEventList.getConfirmedDate());
                }
                if (indexPayoutEventList.getRejectedBy() != null) {
                    existingIndexPayoutEventList.setRejectedBy(indexPayoutEventList.getRejectedBy());
                }
                if (indexPayoutEventList.getRejectedDate() != null) {
                    existingIndexPayoutEventList.setRejectedDate(indexPayoutEventList.getRejectedDate());
                }
                if (indexPayoutEventList.getReason() != null) {
                    existingIndexPayoutEventList.setReason(indexPayoutEventList.getReason());
                }
                if (indexPayoutEventList.getFinalPayout() != null) {
                    existingIndexPayoutEventList.setFinalPayout(indexPayoutEventList.getFinalPayout());
                }
                if (indexPayoutEventList.getIndexPayoutEventStatus() != null) {
                    existingIndexPayoutEventList.setIndexPayoutEventStatus(indexPayoutEventList.getIndexPayoutEventStatus());
                }
                if (indexPayoutEventList.getIsApproved() != null) {
                    existingIndexPayoutEventList.setIsApproved(indexPayoutEventList.getIsApproved());
                }
                if (indexPayoutEventList.getMonitoringRange() != null) {
                    existingIndexPayoutEventList.setMonitoringRange(indexPayoutEventList.getMonitoringRange());
                }
                if (indexPayoutEventList.getIsInsurance() != null) {
                    existingIndexPayoutEventList.setIsInsurance(indexPayoutEventList.getIsInsurance());
                }
                if (indexPayoutEventList.getInsuranceCultivatedLand() != null) {
                    existingIndexPayoutEventList.setInsuranceCultivatedLand(indexPayoutEventList.getInsuranceCultivatedLand());
                }
                if (indexPayoutEventList.getIndexChequeId() != null) {
                    existingIndexPayoutEventList.setIndexChequeId(indexPayoutEventList.getIndexChequeId());
                }
                if (indexPayoutEventList.getIndexProductId() != null) {
                    existingIndexPayoutEventList.setIndexProductId(indexPayoutEventList.getIndexProductId());
                }

                return existingIndexPayoutEventList;
            })
            .map(indexPayoutEventListRepository::save);
    }

    /**
     * Get one indexPayoutEventList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPayoutEventList> findOne(Long id) {
        LOG.debug("Request to get IndexPayoutEventList : {}", id);
        return indexPayoutEventListRepository.findById(id);
    }

    /**
     * Delete the indexPayoutEventList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPayoutEventList : {}", id);
        indexPayoutEventListRepository.deleteById(id);
    }
}
