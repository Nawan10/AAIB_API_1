package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropVariety;
import lk.geoedge.interoperability.repository.CropVarietyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropVariety}.
 */
@Service
@Transactional
public class CropVarietyService {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyService.class);

    private final CropVarietyRepository cropVarietyRepository;

    public CropVarietyService(CropVarietyRepository cropVarietyRepository) {
        this.cropVarietyRepository = cropVarietyRepository;
    }

    /**
     * Save a cropVariety.
     *
     * @param cropVariety the entity to save.
     * @return the persisted entity.
     */
    public CropVariety save(CropVariety cropVariety) {
        LOG.debug("Request to save CropVariety : {}", cropVariety);
        return cropVarietyRepository.save(cropVariety);
    }

    /**
     * Update a cropVariety.
     *
     * @param cropVariety the entity to save.
     * @return the persisted entity.
     */
    public CropVariety update(CropVariety cropVariety) {
        LOG.debug("Request to update CropVariety : {}", cropVariety);
        return cropVarietyRepository.save(cropVariety);
    }

    /**
     * Partially update a cropVariety.
     *
     * @param cropVariety the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropVariety> partialUpdate(CropVariety cropVariety) {
        LOG.debug("Request to partially update CropVariety : {}", cropVariety);

        return cropVarietyRepository
            .findById(cropVariety.getId())
            .map(existingCropVariety -> {
                if (cropVariety.getName() != null) {
                    existingCropVariety.setName(cropVariety.getName());
                }
                if (cropVariety.getNoOfStages() != null) {
                    existingCropVariety.setNoOfStages(cropVariety.getNoOfStages());
                }
                if (cropVariety.getImage() != null) {
                    existingCropVariety.setImage(cropVariety.getImage());
                }
                if (cropVariety.getDescription() != null) {
                    existingCropVariety.setDescription(cropVariety.getDescription());
                }
                if (cropVariety.getAddedBy() != null) {
                    existingCropVariety.setAddedBy(cropVariety.getAddedBy());
                }
                if (cropVariety.getCreatedAt() != null) {
                    existingCropVariety.setCreatedAt(cropVariety.getCreatedAt());
                }

                return existingCropVariety;
            })
            .map(cropVarietyRepository::save);
    }

    /**
     * Get one cropVariety by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropVariety> findOne(Long id) {
        LOG.debug("Request to get CropVariety : {}", id);
        return cropVarietyRepository.findById(id);
    }

    /**
     * Delete the cropVariety by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropVariety : {}", id);
        cropVarietyRepository.deleteById(id);
    }
}
