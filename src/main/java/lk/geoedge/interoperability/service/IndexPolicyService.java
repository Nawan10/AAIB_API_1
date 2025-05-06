package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicy;
import lk.geoedge.interoperability.repository.IndexPolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPolicy}.
 */
@Service
@Transactional
public class IndexPolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyService.class);

    private final IndexPolicyRepository indexPolicyRepository;

    public IndexPolicyService(IndexPolicyRepository indexPolicyRepository) {
        this.indexPolicyRepository = indexPolicyRepository;
    }

    /**
     * Save a indexPolicy.
     *
     * @param indexPolicy the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicy save(IndexPolicy indexPolicy) {
        LOG.debug("Request to save IndexPolicy : {}", indexPolicy);
        return indexPolicyRepository.save(indexPolicy);
    }

    /**
     * Update a indexPolicy.
     *
     * @param indexPolicy the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicy update(IndexPolicy indexPolicy) {
        LOG.debug("Request to update IndexPolicy : {}", indexPolicy);
        return indexPolicyRepository.save(indexPolicy);
    }

    /**
     * Partially update a indexPolicy.
     *
     * @param indexPolicy the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPolicy> partialUpdate(IndexPolicy indexPolicy) {
        LOG.debug("Request to partially update IndexPolicy : {}", indexPolicy);

        return indexPolicyRepository
            .findById(indexPolicy.getId())
            .map(existingIndexPolicy -> {
                if (indexPolicy.getStartDate() != null) {
                    existingIndexPolicy.setStartDate(indexPolicy.getStartDate());
                }
                if (indexPolicy.getEndDate() != null) {
                    existingIndexPolicy.setEndDate(indexPolicy.getEndDate());
                }
                if (indexPolicy.getStageNo() != null) {
                    existingIndexPolicy.setStageNo(indexPolicy.getStageNo());
                }
                if (indexPolicy.getIndexStatus() != null) {
                    existingIndexPolicy.setIndexStatus(indexPolicy.getIndexStatus());
                }

                return existingIndexPolicy;
            })
            .map(indexPolicyRepository::save);
    }

    /**
     * Get one indexPolicy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPolicy> findOne(Long id) {
        LOG.debug("Request to get IndexPolicy : {}", id);
        return indexPolicyRepository.findById(id);
    }

    /**
     * Delete the indexPolicy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPolicy : {}", id);
        indexPolicyRepository.deleteById(id);
    }
}
