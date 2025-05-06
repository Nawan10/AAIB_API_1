package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CropDuration;
import lk.geoedge.interoperability.repository.CropDurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CropDuration}.
 */
@Service
@Transactional
public class CropDurationService {

    private static final Logger LOG = LoggerFactory.getLogger(CropDurationService.class);

    private final CropDurationRepository cropDurationRepository;

    public CropDurationService(CropDurationRepository cropDurationRepository) {
        this.cropDurationRepository = cropDurationRepository;
    }

    /**
     * Save a cropDuration.
     *
     * @param cropDuration the entity to save.
     * @return the persisted entity.
     */
    public CropDuration save(CropDuration cropDuration) {
        LOG.debug("Request to save CropDuration : {}", cropDuration);
        return cropDurationRepository.save(cropDuration);
    }

    /**
     * Update a cropDuration.
     *
     * @param cropDuration the entity to save.
     * @return the persisted entity.
     */
    public CropDuration update(CropDuration cropDuration) {
        LOG.debug("Request to update CropDuration : {}", cropDuration);
        return cropDurationRepository.save(cropDuration);
    }

    /**
     * Partially update a cropDuration.
     *
     * @param cropDuration the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropDuration> partialUpdate(CropDuration cropDuration) {
        LOG.debug("Request to partially update CropDuration : {}", cropDuration);

        return cropDurationRepository
            .findById(cropDuration.getId())
            .map(existingCropDuration -> {
                if (cropDuration.getDuration() != null) {
                    existingCropDuration.setDuration(cropDuration.getDuration());
                }
                if (cropDuration.getName() != null) {
                    existingCropDuration.setName(cropDuration.getName());
                }
                if (cropDuration.getStages() != null) {
                    existingCropDuration.setStages(cropDuration.getStages());
                }
                if (cropDuration.getAddedBy() != null) {
                    existingCropDuration.setAddedBy(cropDuration.getAddedBy());
                }
                if (cropDuration.getAddedDate() != null) {
                    existingCropDuration.setAddedDate(cropDuration.getAddedDate());
                }

                return existingCropDuration;
            })
            .map(cropDurationRepository::save);
    }

    /**
     * Get one cropDuration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropDuration> findOne(Long id) {
        LOG.debug("Request to get CropDuration : {}", id);
        return cropDurationRepository.findById(id);
    }

    /**
     * Delete the cropDuration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropDuration : {}", id);
        cropDurationRepository.deleteById(id);
    }
}
