package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCropCropType;
import lk.geoedge.interoperability.repository.InsuranceCropCropTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCropCropType}.
 */
@Service
@Transactional
public class InsuranceCropCropTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCropCropTypeService.class);

    private final InsuranceCropCropTypeRepository insuranceCropCropTypeRepository;

    public InsuranceCropCropTypeService(InsuranceCropCropTypeRepository insuranceCropCropTypeRepository) {
        this.insuranceCropCropTypeRepository = insuranceCropCropTypeRepository;
    }

    /**
     * Save a insuranceCropCropType.
     *
     * @param insuranceCropCropType the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCropCropType save(InsuranceCropCropType insuranceCropCropType) {
        LOG.debug("Request to save InsuranceCropCropType : {}", insuranceCropCropType);
        return insuranceCropCropTypeRepository.save(insuranceCropCropType);
    }

    /**
     * Update a insuranceCropCropType.
     *
     * @param insuranceCropCropType the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCropCropType update(InsuranceCropCropType insuranceCropCropType) {
        LOG.debug("Request to update InsuranceCropCropType : {}", insuranceCropCropType);
        return insuranceCropCropTypeRepository.save(insuranceCropCropType);
    }

    /**
     * Partially update a insuranceCropCropType.
     *
     * @param insuranceCropCropType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCropCropType> partialUpdate(InsuranceCropCropType insuranceCropCropType) {
        LOG.debug("Request to partially update InsuranceCropCropType : {}", insuranceCropCropType);

        return insuranceCropCropTypeRepository
            .findById(insuranceCropCropType.getId())
            .map(existingInsuranceCropCropType -> {
                if (insuranceCropCropType.getCrop() != null) {
                    existingInsuranceCropCropType.setCrop(insuranceCropCropType.getCrop());
                }
                if (insuranceCropCropType.getImage() != null) {
                    existingInsuranceCropCropType.setImage(insuranceCropCropType.getImage());
                }
                if (insuranceCropCropType.getMainCrop() != null) {
                    existingInsuranceCropCropType.setMainCrop(insuranceCropCropType.getMainCrop());
                }
                if (insuranceCropCropType.getCropCode() != null) {
                    existingInsuranceCropCropType.setCropCode(insuranceCropCropType.getCropCode());
                }
                if (insuranceCropCropType.getNoOfStages() != null) {
                    existingInsuranceCropCropType.setNoOfStages(insuranceCropCropType.getNoOfStages());
                }
                if (insuranceCropCropType.getDescription() != null) {
                    existingInsuranceCropCropType.setDescription(insuranceCropCropType.getDescription());
                }
                if (insuranceCropCropType.getCropTypesId() != null) {
                    existingInsuranceCropCropType.setCropTypesId(insuranceCropCropType.getCropTypesId());
                }
                if (insuranceCropCropType.getUnitsId() != null) {
                    existingInsuranceCropCropType.setUnitsId(insuranceCropCropType.getUnitsId());
                }
                if (insuranceCropCropType.getArea() != null) {
                    existingInsuranceCropCropType.setArea(insuranceCropCropType.getArea());
                }
                if (insuranceCropCropType.getSumInsured() != null) {
                    existingInsuranceCropCropType.setSumInsured(insuranceCropCropType.getSumInsured());
                }
                if (insuranceCropCropType.getMinSumInsured() != null) {
                    existingInsuranceCropCropType.setMinSumInsured(insuranceCropCropType.getMinSumInsured());
                }
                if (insuranceCropCropType.getMaxSumInsured() != null) {
                    existingInsuranceCropCropType.setMaxSumInsured(insuranceCropCropType.getMaxSumInsured());
                }
                if (insuranceCropCropType.getSubsidisedPremiumRate() != null) {
                    existingInsuranceCropCropType.setSubsidisedPremiumRate(insuranceCropCropType.getSubsidisedPremiumRate());
                }

                return existingInsuranceCropCropType;
            })
            .map(insuranceCropCropTypeRepository::save);
    }

    /**
     * Get one insuranceCropCropType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCropCropType> findOne(Long id) {
        LOG.debug("Request to get InsuranceCropCropType : {}", id);
        return insuranceCropCropTypeRepository.findById(id);
    }

    /**
     * Delete the insuranceCropCropType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCropCropType : {}", id);
        insuranceCropCropTypeRepository.deleteById(id);
    }
}
