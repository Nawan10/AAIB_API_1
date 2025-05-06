package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyCropVariety;
import lk.geoedge.interoperability.repository.IndexPolicyCropVarietyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPolicyCropVariety}.
 */
@Service
@Transactional
public class IndexPolicyCropVarietyService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyCropVarietyService.class);

    private final IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository;

    public IndexPolicyCropVarietyService(IndexPolicyCropVarietyRepository indexPolicyCropVarietyRepository) {
        this.indexPolicyCropVarietyRepository = indexPolicyCropVarietyRepository;
    }

    /**
     * Save a indexPolicyCropVariety.
     *
     * @param indexPolicyCropVariety the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyCropVariety save(IndexPolicyCropVariety indexPolicyCropVariety) {
        LOG.debug("Request to save IndexPolicyCropVariety : {}", indexPolicyCropVariety);
        return indexPolicyCropVarietyRepository.save(indexPolicyCropVariety);
    }

    /**
     * Update a indexPolicyCropVariety.
     *
     * @param indexPolicyCropVariety the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyCropVariety update(IndexPolicyCropVariety indexPolicyCropVariety) {
        LOG.debug("Request to update IndexPolicyCropVariety : {}", indexPolicyCropVariety);
        return indexPolicyCropVarietyRepository.save(indexPolicyCropVariety);
    }

    /**
     * Partially update a indexPolicyCropVariety.
     *
     * @param indexPolicyCropVariety the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPolicyCropVariety> partialUpdate(IndexPolicyCropVariety indexPolicyCropVariety) {
        LOG.debug("Request to partially update IndexPolicyCropVariety : {}", indexPolicyCropVariety);

        return indexPolicyCropVarietyRepository
            .findById(indexPolicyCropVariety.getId())
            .map(existingIndexPolicyCropVariety -> {
                if (indexPolicyCropVariety.getName() != null) {
                    existingIndexPolicyCropVariety.setName(indexPolicyCropVariety.getName());
                }
                if (indexPolicyCropVariety.getNoOfStages() != null) {
                    existingIndexPolicyCropVariety.setNoOfStages(indexPolicyCropVariety.getNoOfStages());
                }
                if (indexPolicyCropVariety.getImage() != null) {
                    existingIndexPolicyCropVariety.setImage(indexPolicyCropVariety.getImage());
                }
                if (indexPolicyCropVariety.getDescription() != null) {
                    existingIndexPolicyCropVariety.setDescription(indexPolicyCropVariety.getDescription());
                }
                if (indexPolicyCropVariety.getAddedBy() != null) {
                    existingIndexPolicyCropVariety.setAddedBy(indexPolicyCropVariety.getAddedBy());
                }
                if (indexPolicyCropVariety.getCreatedAt() != null) {
                    existingIndexPolicyCropVariety.setCreatedAt(indexPolicyCropVariety.getCreatedAt());
                }

                return existingIndexPolicyCropVariety;
            })
            .map(indexPolicyCropVarietyRepository::save);
    }

    /**
     * Get one indexPolicyCropVariety by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPolicyCropVariety> findOne(Long id) {
        LOG.debug("Request to get IndexPolicyCropVariety : {}", id);
        return indexPolicyCropVarietyRepository.findById(id);
    }

    /**
     * Delete the indexPolicyCropVariety by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPolicyCropVariety : {}", id);
        indexPolicyCropVarietyRepository.deleteById(id);
    }
}
