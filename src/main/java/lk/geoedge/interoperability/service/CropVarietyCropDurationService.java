package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropVarietyCropDuration;
import lk.geoedge.interoperability.repository.CropVarietyCropDurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropVarietyCropDuration}.
 */
@Service
@Transactional
public class CropVarietyCropDurationService {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyCropDurationService.class);

    private final CropVarietyCropDurationRepository cropVarietyCropDurationRepository;

    public CropVarietyCropDurationService(CropVarietyCropDurationRepository cropVarietyCropDurationRepository) {
        this.cropVarietyCropDurationRepository = cropVarietyCropDurationRepository;
    }

    /**
     * Save a cropVarietyCropDuration.
     *
     * @param cropVarietyCropDuration the entity to save.
     * @return the persisted entity.
     */
    public CropVarietyCropDuration save(CropVarietyCropDuration cropVarietyCropDuration) {
        LOG.debug("Request to save CropVarietyCropDuration : {}", cropVarietyCropDuration);
        return cropVarietyCropDurationRepository.save(cropVarietyCropDuration);
    }

    /**
     * Update a cropVarietyCropDuration.
     *
     * @param cropVarietyCropDuration the entity to save.
     * @return the persisted entity.
     */
    public CropVarietyCropDuration update(CropVarietyCropDuration cropVarietyCropDuration) {
        LOG.debug("Request to update CropVarietyCropDuration : {}", cropVarietyCropDuration);
        return cropVarietyCropDurationRepository.save(cropVarietyCropDuration);
    }

    /**
     * Partially update a cropVarietyCropDuration.
     *
     * @param cropVarietyCropDuration the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropVarietyCropDuration> partialUpdate(CropVarietyCropDuration cropVarietyCropDuration) {
        LOG.debug("Request to partially update CropVarietyCropDuration : {}", cropVarietyCropDuration);

        return cropVarietyCropDurationRepository
            .findById(cropVarietyCropDuration.getId())
            .map(existingCropVarietyCropDuration -> {
                if (cropVarietyCropDuration.getDuration() != null) {
                    existingCropVarietyCropDuration.setDuration(cropVarietyCropDuration.getDuration());
                }
                if (cropVarietyCropDuration.getName() != null) {
                    existingCropVarietyCropDuration.setName(cropVarietyCropDuration.getName());
                }
                if (cropVarietyCropDuration.getStages() != null) {
                    existingCropVarietyCropDuration.setStages(cropVarietyCropDuration.getStages());
                }
                if (cropVarietyCropDuration.getAddedBy() != null) {
                    existingCropVarietyCropDuration.setAddedBy(cropVarietyCropDuration.getAddedBy());
                }
                if (cropVarietyCropDuration.getAddedDate() != null) {
                    existingCropVarietyCropDuration.setAddedDate(cropVarietyCropDuration.getAddedDate());
                }

                return existingCropVarietyCropDuration;
            })
            .map(cropVarietyCropDurationRepository::save);
    }

    /**
     * Get one cropVarietyCropDuration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropVarietyCropDuration> findOne(Long id) {
        LOG.debug("Request to get CropVarietyCropDuration : {}", id);
        return cropVarietyCropDurationRepository.findById(id);
    }

    /**
     * Delete the cropVarietyCropDuration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropVarietyCropDuration : {}", id);
        cropVarietyCropDurationRepository.deleteById(id);
    }
}
