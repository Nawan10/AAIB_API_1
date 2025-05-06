package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.IndexCoveragesCultivatedLandDamageReasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason}.
 */
@Service
@Transactional
public class IndexCoveragesCultivatedLandDamageReasonService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCoveragesCultivatedLandDamageReasonService.class);

    private final IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository;

    public IndexCoveragesCultivatedLandDamageReasonService(
        IndexCoveragesCultivatedLandDamageReasonRepository indexCoveragesCultivatedLandDamageReasonRepository
    ) {
        this.indexCoveragesCultivatedLandDamageReasonRepository = indexCoveragesCultivatedLandDamageReasonRepository;
    }

    /**
     * Save a indexCoveragesCultivatedLandDamageReason.
     *
     * @param indexCoveragesCultivatedLandDamageReason the entity to save.
     * @return the persisted entity.
     */
    public IndexCoveragesCultivatedLandDamageReason save(
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) {
        LOG.debug("Request to save IndexCoveragesCultivatedLandDamageReason : {}", indexCoveragesCultivatedLandDamageReason);
        return indexCoveragesCultivatedLandDamageReasonRepository.save(indexCoveragesCultivatedLandDamageReason);
    }

    /**
     * Update a indexCoveragesCultivatedLandDamageReason.
     *
     * @param indexCoveragesCultivatedLandDamageReason the entity to save.
     * @return the persisted entity.
     */
    public IndexCoveragesCultivatedLandDamageReason update(
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) {
        LOG.debug("Request to update IndexCoveragesCultivatedLandDamageReason : {}", indexCoveragesCultivatedLandDamageReason);
        return indexCoveragesCultivatedLandDamageReasonRepository.save(indexCoveragesCultivatedLandDamageReason);
    }

    /**
     * Partially update a indexCoveragesCultivatedLandDamageReason.
     *
     * @param indexCoveragesCultivatedLandDamageReason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexCoveragesCultivatedLandDamageReason> partialUpdate(
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason
    ) {
        LOG.debug("Request to partially update IndexCoveragesCultivatedLandDamageReason : {}", indexCoveragesCultivatedLandDamageReason);

        return indexCoveragesCultivatedLandDamageReasonRepository
            .findById(indexCoveragesCultivatedLandDamageReason.getId())
            .map(existingIndexCoveragesCultivatedLandDamageReason -> {
                if (indexCoveragesCultivatedLandDamageReason.getName() != null) {
                    existingIndexCoveragesCultivatedLandDamageReason.setName(indexCoveragesCultivatedLandDamageReason.getName());
                }

                return existingIndexCoveragesCultivatedLandDamageReason;
            })
            .map(indexCoveragesCultivatedLandDamageReasonRepository::save);
    }

    /**
     * Get one indexCoveragesCultivatedLandDamageReason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexCoveragesCultivatedLandDamageReason> findOne(Long id) {
        LOG.debug("Request to get IndexCoveragesCultivatedLandDamageReason : {}", id);
        return indexCoveragesCultivatedLandDamageReasonRepository.findById(id);
    }

    /**
     * Delete the indexCoveragesCultivatedLandDamageReason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexCoveragesCultivatedLandDamageReason : {}", id);
        indexCoveragesCultivatedLandDamageReasonRepository.deleteById(id);
    }
}
