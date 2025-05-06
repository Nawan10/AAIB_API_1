package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexCoverages;
import lk.geoedge.interoperability.repository.IndexCoveragesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexCoverages}.
 */
@Service
@Transactional
public class IndexCoveragesService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexCoveragesService.class);

    private final IndexCoveragesRepository indexCoveragesRepository;

    public IndexCoveragesService(IndexCoveragesRepository indexCoveragesRepository) {
        this.indexCoveragesRepository = indexCoveragesRepository;
    }

    /**
     * Save a indexCoverages.
     *
     * @param indexCoverages the entity to save.
     * @return the persisted entity.
     */
    public IndexCoverages save(IndexCoverages indexCoverages) {
        LOG.debug("Request to save IndexCoverages : {}", indexCoverages);
        return indexCoveragesRepository.save(indexCoverages);
    }

    /**
     * Update a indexCoverages.
     *
     * @param indexCoverages the entity to save.
     * @return the persisted entity.
     */
    public IndexCoverages update(IndexCoverages indexCoverages) {
        LOG.debug("Request to update IndexCoverages : {}", indexCoverages);
        return indexCoveragesRepository.save(indexCoverages);
    }

    /**
     * Partially update a indexCoverages.
     *
     * @param indexCoverages the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexCoverages> partialUpdate(IndexCoverages indexCoverages) {
        LOG.debug("Request to partially update IndexCoverages : {}", indexCoverages);

        return indexCoveragesRepository
            .findById(indexCoverages.getId())
            .map(existingIndexCoverages -> {
                if (indexCoverages.getIndexProductId() != null) {
                    existingIndexCoverages.setIndexProductId(indexCoverages.getIndexProductId());
                }
                if (indexCoverages.getPremiumRate() != null) {
                    existingIndexCoverages.setPremiumRate(indexCoverages.getPremiumRate());
                }
                if (indexCoverages.getIsFree() != null) {
                    existingIndexCoverages.setIsFree(indexCoverages.getIsFree());
                }
                if (indexCoverages.getIsPaid() != null) {
                    existingIndexCoverages.setIsPaid(indexCoverages.getIsPaid());
                }

                return existingIndexCoverages;
            })
            .map(indexCoveragesRepository::save);
    }

    /**
     * Get one indexCoverages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexCoverages> findOne(Long id) {
        LOG.debug("Request to get IndexCoverages : {}", id);
        return indexCoveragesRepository.findById(id);
    }

    /**
     * Delete the indexCoverages by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexCoverages : {}", id);
        indexCoveragesRepository.deleteById(id);
    }
}
