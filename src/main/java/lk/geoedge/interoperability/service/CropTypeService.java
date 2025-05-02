package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropType;
import lk.geoedge.interoperability.repository.CropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropType}.
 */
@Service
@Transactional
public class CropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CropTypeService.class);

    private final CropTypeRepository cropTypeRepository;

    public CropTypeService(CropTypeRepository cropTypeRepository) {
        this.cropTypeRepository = cropTypeRepository;
    }

    /**
     * Save a cropType.
     *
     * @param cropType the entity to save.
     * @return the persisted entity.
     */
    public CropType save(CropType cropType) {
        LOG.debug("Request to save CropType : {}", cropType);
        return cropTypeRepository.save(cropType);
    }

    /**
     * Update a cropType.
     *
     * @param cropType the entity to save.
     * @return the persisted entity.
     */
    public CropType update(CropType cropType) {
        LOG.debug("Request to update CropType : {}", cropType);
        return cropTypeRepository.save(cropType);
    }

    /**
     * Partially update a cropType.
     *
     * @param cropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropType> partialUpdate(CropType cropType) {
        LOG.debug("Request to partially update CropType : {}", cropType);

        return cropTypeRepository
            .findById(cropType.getId())
            .map(existingCropType -> {
                if (cropType.getCrop() != null) {
                    existingCropType.setCrop(cropType.getCrop());
                }
                if (cropType.getImage() != null) {
                    existingCropType.setImage(cropType.getImage());
                }
                if (cropType.getMainCrop() != null) {
                    existingCropType.setMainCrop(cropType.getMainCrop());
                }
                if (cropType.getCropCode() != null) {
                    existingCropType.setCropCode(cropType.getCropCode());
                }
                if (cropType.getNoOfStages() != null) {
                    existingCropType.setNoOfStages(cropType.getNoOfStages());
                }
                if (cropType.getDescription() != null) {
                    existingCropType.setDescription(cropType.getDescription());
                }
                if (cropType.getCropTypesId() != null) {
                    existingCropType.setCropTypesId(cropType.getCropTypesId());
                }
                if (cropType.getUnitsId() != null) {
                    existingCropType.setUnitsId(cropType.getUnitsId());
                }
                if (cropType.getArea() != null) {
                    existingCropType.setArea(cropType.getArea());
                }
                if (cropType.getSumInsured() != null) {
                    existingCropType.setSumInsured(cropType.getSumInsured());
                }
                if (cropType.getMinSumInsured() != null) {
                    existingCropType.setMinSumInsured(cropType.getMinSumInsured());
                }
                if (cropType.getMaxSumInsured() != null) {
                    existingCropType.setMaxSumInsured(cropType.getMaxSumInsured());
                }
                if (cropType.getSubsidisedPremiumRate() != null) {
                    existingCropType.setSubsidisedPremiumRate(cropType.getSubsidisedPremiumRate());
                }

                return existingCropType;
            })
            .map(cropTypeRepository::save);
    }

    /**
     * Get one cropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropType> findOne(Long id) {
        LOG.debug("Request to get CropType : {}", id);
        return cropTypeRepository.findById(id);
    }

    /**
     * Delete the cropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropType : {}", id);
        cropTypeRepository.deleteById(id);
    }
}
