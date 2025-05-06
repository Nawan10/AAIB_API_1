package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicySeason;
import lk.geoedge.interoperability.repository.IndexPolicySeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPolicySeason}.
 */
@Service
@Transactional
public class IndexPolicySeasonService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicySeasonService.class);

    private final IndexPolicySeasonRepository indexPolicySeasonRepository;

    public IndexPolicySeasonService(IndexPolicySeasonRepository indexPolicySeasonRepository) {
        this.indexPolicySeasonRepository = indexPolicySeasonRepository;
    }

    /**
     * Save a indexPolicySeason.
     *
     * @param indexPolicySeason the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicySeason save(IndexPolicySeason indexPolicySeason) {
        LOG.debug("Request to save IndexPolicySeason : {}", indexPolicySeason);
        return indexPolicySeasonRepository.save(indexPolicySeason);
    }

    /**
     * Update a indexPolicySeason.
     *
     * @param indexPolicySeason the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicySeason update(IndexPolicySeason indexPolicySeason) {
        LOG.debug("Request to update IndexPolicySeason : {}", indexPolicySeason);
        return indexPolicySeasonRepository.save(indexPolicySeason);
    }

    /**
     * Partially update a indexPolicySeason.
     *
     * @param indexPolicySeason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPolicySeason> partialUpdate(IndexPolicySeason indexPolicySeason) {
        LOG.debug("Request to partially update IndexPolicySeason : {}", indexPolicySeason);

        return indexPolicySeasonRepository
            .findById(indexPolicySeason.getId())
            .map(existingIndexPolicySeason -> {
                if (indexPolicySeason.getName() != null) {
                    existingIndexPolicySeason.setName(indexPolicySeason.getName());
                }
                if (indexPolicySeason.getPeriod() != null) {
                    existingIndexPolicySeason.setPeriod(indexPolicySeason.getPeriod());
                }

                return existingIndexPolicySeason;
            })
            .map(indexPolicySeasonRepository::save);
    }

    /**
     * Get one indexPolicySeason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPolicySeason> findOne(Long id) {
        LOG.debug("Request to get IndexPolicySeason : {}", id);
        return indexPolicySeasonRepository.findById(id);
    }

    /**
     * Delete the indexPolicySeason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPolicySeason : {}", id);
        indexPolicySeasonRepository.deleteById(id);
    }
}
