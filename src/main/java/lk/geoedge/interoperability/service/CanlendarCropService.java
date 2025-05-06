package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CanlendarCrop;
import lk.geoedge.interoperability.repository.CanlendarCropRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CanlendarCrop}.
 */
@Service
@Transactional
public class CanlendarCropService {

    private static final Logger LOG = LoggerFactory.getLogger(CanlendarCropService.class);

    private final CanlendarCropRepository canlendarCropRepository;

    public CanlendarCropService(CanlendarCropRepository canlendarCropRepository) {
        this.canlendarCropRepository = canlendarCropRepository;
    }

    /**
     * Save a canlendarCrop.
     *
     * @param canlendarCrop the entity to save.
     * @return the persisted entity.
     */
    public CanlendarCrop save(CanlendarCrop canlendarCrop) {
        LOG.debug("Request to save CanlendarCrop : {}", canlendarCrop);
        return canlendarCropRepository.save(canlendarCrop);
    }

    /**
     * Update a canlendarCrop.
     *
     * @param canlendarCrop the entity to save.
     * @return the persisted entity.
     */
    public CanlendarCrop update(CanlendarCrop canlendarCrop) {
        LOG.debug("Request to update CanlendarCrop : {}", canlendarCrop);
        return canlendarCropRepository.save(canlendarCrop);
    }

    /**
     * Partially update a canlendarCrop.
     *
     * @param canlendarCrop the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CanlendarCrop> partialUpdate(CanlendarCrop canlendarCrop) {
        LOG.debug("Request to partially update CanlendarCrop : {}", canlendarCrop);

        return canlendarCropRepository
            .findById(canlendarCrop.getId())
            .map(existingCanlendarCrop -> {
                if (canlendarCrop.getStartDate() != null) {
                    existingCanlendarCrop.setStartDate(canlendarCrop.getStartDate());
                }
                if (canlendarCrop.getEndDate() != null) {
                    existingCanlendarCrop.setEndDate(canlendarCrop.getEndDate());
                }
                if (canlendarCrop.getPercentage() != null) {
                    existingCanlendarCrop.setPercentage(canlendarCrop.getPercentage());
                }
                if (canlendarCrop.getCanlendarCropStatus() != null) {
                    existingCanlendarCrop.setCanlendarCropStatus(canlendarCrop.getCanlendarCropStatus());
                }
                if (canlendarCrop.getReason() != null) {
                    existingCanlendarCrop.setReason(canlendarCrop.getReason());
                }
                if (canlendarCrop.getCreatedAt() != null) {
                    existingCanlendarCrop.setCreatedAt(canlendarCrop.getCreatedAt());
                }
                if (canlendarCrop.getAddedBy() != null) {
                    existingCanlendarCrop.setAddedBy(canlendarCrop.getAddedBy());
                }

                return existingCanlendarCrop;
            })
            .map(canlendarCropRepository::save);
    }

    /**
     * Get one canlendarCrop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CanlendarCrop> findOne(Long id) {
        LOG.debug("Request to get CanlendarCrop : {}", id);
        return canlendarCropRepository.findById(id);
    }

    /**
     * Delete the canlendarCrop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CanlendarCrop : {}", id);
        canlendarCropRepository.deleteById(id);
    }
}
