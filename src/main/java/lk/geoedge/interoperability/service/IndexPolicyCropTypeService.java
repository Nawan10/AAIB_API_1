package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyCropType;
import lk.geoedge.interoperability.repository.IndexPolicyCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPolicyCropType}.
 */
@Service
@Transactional
public class IndexPolicyCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyCropTypeService.class);

    private final IndexPolicyCropTypeRepository indexPolicyCropTypeRepository;

    public IndexPolicyCropTypeService(IndexPolicyCropTypeRepository indexPolicyCropTypeRepository) {
        this.indexPolicyCropTypeRepository = indexPolicyCropTypeRepository;
    }

    /**
     * Save a indexPolicyCropType.
     *
     * @param indexPolicyCropType the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyCropType save(IndexPolicyCropType indexPolicyCropType) {
        LOG.debug("Request to save IndexPolicyCropType : {}", indexPolicyCropType);
        return indexPolicyCropTypeRepository.save(indexPolicyCropType);
    }

    /**
     * Update a indexPolicyCropType.
     *
     * @param indexPolicyCropType the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyCropType update(IndexPolicyCropType indexPolicyCropType) {
        LOG.debug("Request to update IndexPolicyCropType : {}", indexPolicyCropType);
        return indexPolicyCropTypeRepository.save(indexPolicyCropType);
    }

    /**
     * Partially update a indexPolicyCropType.
     *
     * @param indexPolicyCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPolicyCropType> partialUpdate(IndexPolicyCropType indexPolicyCropType) {
        LOG.debug("Request to partially update IndexPolicyCropType : {}", indexPolicyCropType);

        return indexPolicyCropTypeRepository
            .findById(indexPolicyCropType.getId())
            .map(existingIndexPolicyCropType -> {
                if (indexPolicyCropType.getCrop() != null) {
                    existingIndexPolicyCropType.setCrop(indexPolicyCropType.getCrop());
                }
                if (indexPolicyCropType.getImage() != null) {
                    existingIndexPolicyCropType.setImage(indexPolicyCropType.getImage());
                }
                if (indexPolicyCropType.getMainCrop() != null) {
                    existingIndexPolicyCropType.setMainCrop(indexPolicyCropType.getMainCrop());
                }
                if (indexPolicyCropType.getCropCode() != null) {
                    existingIndexPolicyCropType.setCropCode(indexPolicyCropType.getCropCode());
                }
                if (indexPolicyCropType.getNoOfStages() != null) {
                    existingIndexPolicyCropType.setNoOfStages(indexPolicyCropType.getNoOfStages());
                }
                if (indexPolicyCropType.getDescription() != null) {
                    existingIndexPolicyCropType.setDescription(indexPolicyCropType.getDescription());
                }
                if (indexPolicyCropType.getCropTypesId() != null) {
                    existingIndexPolicyCropType.setCropTypesId(indexPolicyCropType.getCropTypesId());
                }
                if (indexPolicyCropType.getUnitsId() != null) {
                    existingIndexPolicyCropType.setUnitsId(indexPolicyCropType.getUnitsId());
                }
                if (indexPolicyCropType.getArea() != null) {
                    existingIndexPolicyCropType.setArea(indexPolicyCropType.getArea());
                }
                if (indexPolicyCropType.getSumInsured() != null) {
                    existingIndexPolicyCropType.setSumInsured(indexPolicyCropType.getSumInsured());
                }
                if (indexPolicyCropType.getMinSumInsured() != null) {
                    existingIndexPolicyCropType.setMinSumInsured(indexPolicyCropType.getMinSumInsured());
                }
                if (indexPolicyCropType.getMaxSumInsured() != null) {
                    existingIndexPolicyCropType.setMaxSumInsured(indexPolicyCropType.getMaxSumInsured());
                }
                if (indexPolicyCropType.getSubsidisedPremiumRate() != null) {
                    existingIndexPolicyCropType.setSubsidisedPremiumRate(indexPolicyCropType.getSubsidisedPremiumRate());
                }

                return existingIndexPolicyCropType;
            })
            .map(indexPolicyCropTypeRepository::save);
    }

    /**
     * Get one indexPolicyCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPolicyCropType> findOne(Long id) {
        LOG.debug("Request to get IndexPolicyCropType : {}", id);
        return indexPolicyCropTypeRepository.findById(id);
    }

    /**
     * Delete the indexPolicyCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPolicyCropType : {}", id);
        indexPolicyCropTypeRepository.deleteById(id);
    }
}
