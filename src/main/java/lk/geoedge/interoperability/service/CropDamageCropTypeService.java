package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDamageCropType;
import lk.geoedge.interoperability.repository.CropDamageCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropDamageCropType}.
 */
@Service
@Transactional
public class CropDamageCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CropDamageCropTypeService.class);

    private final CropDamageCropTypeRepository cropDamageCropTypeRepository;

    public CropDamageCropTypeService(CropDamageCropTypeRepository cropDamageCropTypeRepository) {
        this.cropDamageCropTypeRepository = cropDamageCropTypeRepository;
    }

    /**
     * Save a cropDamageCropType.
     *
     * @param cropDamageCropType the entity to save.
     * @return the persisted entity.
     */
    public CropDamageCropType save(CropDamageCropType cropDamageCropType) {
        LOG.debug("Request to save CropDamageCropType : {}", cropDamageCropType);
        return cropDamageCropTypeRepository.save(cropDamageCropType);
    }

    /**
     * Update a cropDamageCropType.
     *
     * @param cropDamageCropType the entity to save.
     * @return the persisted entity.
     */
    public CropDamageCropType update(CropDamageCropType cropDamageCropType) {
        LOG.debug("Request to update CropDamageCropType : {}", cropDamageCropType);
        return cropDamageCropTypeRepository.save(cropDamageCropType);
    }

    /**
     * Partially update a cropDamageCropType.
     *
     * @param cropDamageCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropDamageCropType> partialUpdate(CropDamageCropType cropDamageCropType) {
        LOG.debug("Request to partially update CropDamageCropType : {}", cropDamageCropType);

        return cropDamageCropTypeRepository
            .findById(cropDamageCropType.getId())
            .map(existingCropDamageCropType -> {
                if (cropDamageCropType.getCrop() != null) {
                    existingCropDamageCropType.setCrop(cropDamageCropType.getCrop());
                }
                if (cropDamageCropType.getImage() != null) {
                    existingCropDamageCropType.setImage(cropDamageCropType.getImage());
                }
                if (cropDamageCropType.getMainCrop() != null) {
                    existingCropDamageCropType.setMainCrop(cropDamageCropType.getMainCrop());
                }
                if (cropDamageCropType.getCropCode() != null) {
                    existingCropDamageCropType.setCropCode(cropDamageCropType.getCropCode());
                }
                if (cropDamageCropType.getNoOfStages() != null) {
                    existingCropDamageCropType.setNoOfStages(cropDamageCropType.getNoOfStages());
                }
                if (cropDamageCropType.getDescription() != null) {
                    existingCropDamageCropType.setDescription(cropDamageCropType.getDescription());
                }
                if (cropDamageCropType.getCropTypesId() != null) {
                    existingCropDamageCropType.setCropTypesId(cropDamageCropType.getCropTypesId());
                }
                if (cropDamageCropType.getUnitsId() != null) {
                    existingCropDamageCropType.setUnitsId(cropDamageCropType.getUnitsId());
                }
                if (cropDamageCropType.getArea() != null) {
                    existingCropDamageCropType.setArea(cropDamageCropType.getArea());
                }
                if (cropDamageCropType.getSumInsured() != null) {
                    existingCropDamageCropType.setSumInsured(cropDamageCropType.getSumInsured());
                }
                if (cropDamageCropType.getMinSumInsured() != null) {
                    existingCropDamageCropType.setMinSumInsured(cropDamageCropType.getMinSumInsured());
                }
                if (cropDamageCropType.getMaxSumInsured() != null) {
                    existingCropDamageCropType.setMaxSumInsured(cropDamageCropType.getMaxSumInsured());
                }
                if (cropDamageCropType.getSubsidisedPremiumRate() != null) {
                    existingCropDamageCropType.setSubsidisedPremiumRate(cropDamageCropType.getSubsidisedPremiumRate());
                }

                return existingCropDamageCropType;
            })
            .map(cropDamageCropTypeRepository::save);
    }

    /**
     * Get one cropDamageCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropDamageCropType> findOne(Long id) {
        LOG.debug("Request to get CropDamageCropType : {}", id);
        return cropDamageCropTypeRepository.findById(id);
    }

    /**
     * Delete the cropDamageCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropDamageCropType : {}", id);
        cropDamageCropTypeRepository.deleteById(id);
    }
}
