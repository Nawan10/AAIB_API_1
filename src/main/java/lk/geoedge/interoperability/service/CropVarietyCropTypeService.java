package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropVarietyCropType;
import lk.geoedge.interoperability.repository.CropVarietyCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropVarietyCropType}.
 */
@Service
@Transactional
public class CropVarietyCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyCropTypeService.class);

    private final CropVarietyCropTypeRepository cropVarietyCropTypeRepository;

    public CropVarietyCropTypeService(CropVarietyCropTypeRepository cropVarietyCropTypeRepository) {
        this.cropVarietyCropTypeRepository = cropVarietyCropTypeRepository;
    }

    /**
     * Save a cropVarietyCropType.
     *
     * @param cropVarietyCropType the entity to save.
     * @return the persisted entity.
     */
    public CropVarietyCropType save(CropVarietyCropType cropVarietyCropType) {
        LOG.debug("Request to save CropVarietyCropType : {}", cropVarietyCropType);
        return cropVarietyCropTypeRepository.save(cropVarietyCropType);
    }

    /**
     * Update a cropVarietyCropType.
     *
     * @param cropVarietyCropType the entity to save.
     * @return the persisted entity.
     */
    public CropVarietyCropType update(CropVarietyCropType cropVarietyCropType) {
        LOG.debug("Request to update CropVarietyCropType : {}", cropVarietyCropType);
        return cropVarietyCropTypeRepository.save(cropVarietyCropType);
    }

    /**
     * Partially update a cropVarietyCropType.
     *
     * @param cropVarietyCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropVarietyCropType> partialUpdate(CropVarietyCropType cropVarietyCropType) {
        LOG.debug("Request to partially update CropVarietyCropType : {}", cropVarietyCropType);

        return cropVarietyCropTypeRepository
            .findById(cropVarietyCropType.getId())
            .map(existingCropVarietyCropType -> {
                if (cropVarietyCropType.getCrop() != null) {
                    existingCropVarietyCropType.setCrop(cropVarietyCropType.getCrop());
                }
                if (cropVarietyCropType.getImage() != null) {
                    existingCropVarietyCropType.setImage(cropVarietyCropType.getImage());
                }
                if (cropVarietyCropType.getMainCrop() != null) {
                    existingCropVarietyCropType.setMainCrop(cropVarietyCropType.getMainCrop());
                }
                if (cropVarietyCropType.getCropCode() != null) {
                    existingCropVarietyCropType.setCropCode(cropVarietyCropType.getCropCode());
                }
                if (cropVarietyCropType.getNoOfStages() != null) {
                    existingCropVarietyCropType.setNoOfStages(cropVarietyCropType.getNoOfStages());
                }
                if (cropVarietyCropType.getDescription() != null) {
                    existingCropVarietyCropType.setDescription(cropVarietyCropType.getDescription());
                }
                if (cropVarietyCropType.getCropTypesId() != null) {
                    existingCropVarietyCropType.setCropTypesId(cropVarietyCropType.getCropTypesId());
                }
                if (cropVarietyCropType.getUnitsId() != null) {
                    existingCropVarietyCropType.setUnitsId(cropVarietyCropType.getUnitsId());
                }
                if (cropVarietyCropType.getArea() != null) {
                    existingCropVarietyCropType.setArea(cropVarietyCropType.getArea());
                }
                if (cropVarietyCropType.getSumInsured() != null) {
                    existingCropVarietyCropType.setSumInsured(cropVarietyCropType.getSumInsured());
                }
                if (cropVarietyCropType.getMinSumInsured() != null) {
                    existingCropVarietyCropType.setMinSumInsured(cropVarietyCropType.getMinSumInsured());
                }
                if (cropVarietyCropType.getMaxSumInsured() != null) {
                    existingCropVarietyCropType.setMaxSumInsured(cropVarietyCropType.getMaxSumInsured());
                }
                if (cropVarietyCropType.getSubsidisedPremiumRate() != null) {
                    existingCropVarietyCropType.setSubsidisedPremiumRate(cropVarietyCropType.getSubsidisedPremiumRate());
                }

                return existingCropVarietyCropType;
            })
            .map(cropVarietyCropTypeRepository::save);
    }

    /**
     * Get one cropVarietyCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropVarietyCropType> findOne(Long id) {
        LOG.debug("Request to get CropVarietyCropType : {}", id);
        return cropVarietyCropTypeRepository.findById(id);
    }

    /**
     * Delete the cropVarietyCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropVarietyCropType : {}", id);
        cropVarietyCropTypeRepository.deleteById(id);
    }
}
