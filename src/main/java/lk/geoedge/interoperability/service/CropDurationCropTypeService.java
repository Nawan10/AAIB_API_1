package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDurationCropType;
import lk.geoedge.interoperability.repository.CropDurationCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropDurationCropType}.
 */
@Service
@Transactional
public class CropDurationCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CropDurationCropTypeService.class);

    private final CropDurationCropTypeRepository cropDurationCropTypeRepository;

    public CropDurationCropTypeService(CropDurationCropTypeRepository cropDurationCropTypeRepository) {
        this.cropDurationCropTypeRepository = cropDurationCropTypeRepository;
    }

    /**
     * Save a cropDurationCropType.
     *
     * @param cropDurationCropType the entity to save.
     * @return the persisted entity.
     */
    public CropDurationCropType save(CropDurationCropType cropDurationCropType) {
        LOG.debug("Request to save CropDurationCropType : {}", cropDurationCropType);
        return cropDurationCropTypeRepository.save(cropDurationCropType);
    }

    /**
     * Update a cropDurationCropType.
     *
     * @param cropDurationCropType the entity to save.
     * @return the persisted entity.
     */
    public CropDurationCropType update(CropDurationCropType cropDurationCropType) {
        LOG.debug("Request to update CropDurationCropType : {}", cropDurationCropType);
        return cropDurationCropTypeRepository.save(cropDurationCropType);
    }

    /**
     * Partially update a cropDurationCropType.
     *
     * @param cropDurationCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropDurationCropType> partialUpdate(CropDurationCropType cropDurationCropType) {
        LOG.debug("Request to partially update CropDurationCropType : {}", cropDurationCropType);

        return cropDurationCropTypeRepository
            .findById(cropDurationCropType.getId())
            .map(existingCropDurationCropType -> {
                if (cropDurationCropType.getCrop() != null) {
                    existingCropDurationCropType.setCrop(cropDurationCropType.getCrop());
                }
                if (cropDurationCropType.getImage() != null) {
                    existingCropDurationCropType.setImage(cropDurationCropType.getImage());
                }
                if (cropDurationCropType.getMainCrop() != null) {
                    existingCropDurationCropType.setMainCrop(cropDurationCropType.getMainCrop());
                }
                if (cropDurationCropType.getCropCode() != null) {
                    existingCropDurationCropType.setCropCode(cropDurationCropType.getCropCode());
                }
                if (cropDurationCropType.getNoOfStages() != null) {
                    existingCropDurationCropType.setNoOfStages(cropDurationCropType.getNoOfStages());
                }
                if (cropDurationCropType.getDescription() != null) {
                    existingCropDurationCropType.setDescription(cropDurationCropType.getDescription());
                }
                if (cropDurationCropType.getCropTypesId() != null) {
                    existingCropDurationCropType.setCropTypesId(cropDurationCropType.getCropTypesId());
                }
                if (cropDurationCropType.getUnitsId() != null) {
                    existingCropDurationCropType.setUnitsId(cropDurationCropType.getUnitsId());
                }
                if (cropDurationCropType.getArea() != null) {
                    existingCropDurationCropType.setArea(cropDurationCropType.getArea());
                }
                if (cropDurationCropType.getSumInsured() != null) {
                    existingCropDurationCropType.setSumInsured(cropDurationCropType.getSumInsured());
                }
                if (cropDurationCropType.getMinSumInsured() != null) {
                    existingCropDurationCropType.setMinSumInsured(cropDurationCropType.getMinSumInsured());
                }
                if (cropDurationCropType.getMaxSumInsured() != null) {
                    existingCropDurationCropType.setMaxSumInsured(cropDurationCropType.getMaxSumInsured());
                }
                if (cropDurationCropType.getSubsidisedPremiumRate() != null) {
                    existingCropDurationCropType.setSubsidisedPremiumRate(cropDurationCropType.getSubsidisedPremiumRate());
                }

                return existingCropDurationCropType;
            })
            .map(cropDurationCropTypeRepository::save);
    }

    /**
     * Get one cropDurationCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropDurationCropType> findOne(Long id) {
        LOG.debug("Request to get CropDurationCropType : {}", id);
        return cropDurationCropTypeRepository.findById(id);
    }

    /**
     * Delete the cropDurationCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropDurationCropType : {}", id);
        cropDurationCropTypeRepository.deleteById(id);
    }
}
