package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand;
import lk.geoedge.interoperability.repository.IndexPayoutEventListCultivatedLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand}.
 */
@Service
@Transactional
public class IndexPayoutEventListCultivatedLandService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPayoutEventListCultivatedLandService.class);

    private final IndexPayoutEventListCultivatedLandRepository indexPayoutEventListCultivatedLandRepository;

    public IndexPayoutEventListCultivatedLandService(
        IndexPayoutEventListCultivatedLandRepository indexPayoutEventListCultivatedLandRepository
    ) {
        this.indexPayoutEventListCultivatedLandRepository = indexPayoutEventListCultivatedLandRepository;
    }

    /**
     * Save a indexPayoutEventListCultivatedLand.
     *
     * @param indexPayoutEventListCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventListCultivatedLand save(IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand) {
        LOG.debug("Request to save IndexPayoutEventListCultivatedLand : {}", indexPayoutEventListCultivatedLand);
        return indexPayoutEventListCultivatedLandRepository.save(indexPayoutEventListCultivatedLand);
    }

    /**
     * Update a indexPayoutEventListCultivatedLand.
     *
     * @param indexPayoutEventListCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public IndexPayoutEventListCultivatedLand update(IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand) {
        LOG.debug("Request to update IndexPayoutEventListCultivatedLand : {}", indexPayoutEventListCultivatedLand);
        return indexPayoutEventListCultivatedLandRepository.save(indexPayoutEventListCultivatedLand);
    }

    /**
     * Partially update a indexPayoutEventListCultivatedLand.
     *
     * @param indexPayoutEventListCultivatedLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPayoutEventListCultivatedLand> partialUpdate(
        IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand
    ) {
        LOG.debug("Request to partially update IndexPayoutEventListCultivatedLand : {}", indexPayoutEventListCultivatedLand);

        return indexPayoutEventListCultivatedLandRepository
            .findById(indexPayoutEventListCultivatedLand.getId())
            .map(existingIndexPayoutEventListCultivatedLand -> {
                if (indexPayoutEventListCultivatedLand.getLandStatus() != null) {
                    existingIndexPayoutEventListCultivatedLand.setLandStatus(indexPayoutEventListCultivatedLand.getLandStatus());
                }
                if (indexPayoutEventListCultivatedLand.getUrea() != null) {
                    existingIndexPayoutEventListCultivatedLand.setUrea(indexPayoutEventListCultivatedLand.getUrea());
                }
                if (indexPayoutEventListCultivatedLand.getMop() != null) {
                    existingIndexPayoutEventListCultivatedLand.setMop(indexPayoutEventListCultivatedLand.getMop());
                }
                if (indexPayoutEventListCultivatedLand.getTsp() != null) {
                    existingIndexPayoutEventListCultivatedLand.setTsp(indexPayoutEventListCultivatedLand.getTsp());
                }
                if (indexPayoutEventListCultivatedLand.getCreatedAt() != null) {
                    existingIndexPayoutEventListCultivatedLand.setCreatedAt(indexPayoutEventListCultivatedLand.getCreatedAt());
                }
                if (indexPayoutEventListCultivatedLand.getAddedBy() != null) {
                    existingIndexPayoutEventListCultivatedLand.setAddedBy(indexPayoutEventListCultivatedLand.getAddedBy());
                }

                return existingIndexPayoutEventListCultivatedLand;
            })
            .map(indexPayoutEventListCultivatedLandRepository::save);
    }

    /**
     * Get one indexPayoutEventListCultivatedLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPayoutEventListCultivatedLand> findOne(Long id) {
        LOG.debug("Request to get IndexPayoutEventListCultivatedLand : {}", id);
        return indexPayoutEventListCultivatedLandRepository.findById(id);
    }

    /**
     * Delete the indexPayoutEventListCultivatedLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPayoutEventListCultivatedLand : {}", id);
        indexPayoutEventListCultivatedLandRepository.deleteById(id);
    }
}
