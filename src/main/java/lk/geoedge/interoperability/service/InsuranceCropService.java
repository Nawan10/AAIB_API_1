package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCrop;
import lk.geoedge.interoperability.repository.InsuranceCropRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCrop}.
 */
@Service
@Transactional
public class InsuranceCropService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCropService.class);

    private final InsuranceCropRepository insuranceCropRepository;

    public InsuranceCropService(InsuranceCropRepository insuranceCropRepository) {
        this.insuranceCropRepository = insuranceCropRepository;
    }

    /**
     * Save a insuranceCrop.
     *
     * @param insuranceCrop the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCrop save(InsuranceCrop insuranceCrop) {
        LOG.debug("Request to save InsuranceCrop : {}", insuranceCrop);
        return insuranceCropRepository.save(insuranceCrop);
    }

    /**
     * Update a insuranceCrop.
     *
     * @param insuranceCrop the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCrop update(InsuranceCrop insuranceCrop) {
        LOG.debug("Request to update InsuranceCrop : {}", insuranceCrop);
        return insuranceCropRepository.save(insuranceCrop);
    }

    /**
     * Partially update a insuranceCrop.
     *
     * @param insuranceCrop the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCrop> partialUpdate(InsuranceCrop insuranceCrop) {
        LOG.debug("Request to partially update InsuranceCrop : {}", insuranceCrop);

        return insuranceCropRepository
            .findById(insuranceCrop.getId())
            .map(existingInsuranceCrop -> {
                if (insuranceCrop.getPolicyId() != null) {
                    existingInsuranceCrop.setPolicyId(insuranceCrop.getPolicyId());
                }
                if (insuranceCrop.getYield() != null) {
                    existingInsuranceCrop.setYield(insuranceCrop.getYield());
                }
                if (insuranceCrop.getCreatedAt() != null) {
                    existingInsuranceCrop.setCreatedAt(insuranceCrop.getCreatedAt());
                }
                if (insuranceCrop.getAddedBy() != null) {
                    existingInsuranceCrop.setAddedBy(insuranceCrop.getAddedBy());
                }

                return existingInsuranceCrop;
            })
            .map(insuranceCropRepository::save);
    }

    /**
     * Get one insuranceCrop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCrop> findOne(Long id) {
        LOG.debug("Request to get InsuranceCrop : {}", id);
        return insuranceCropRepository.findById(id);
    }

    /**
     * Delete the insuranceCrop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCrop : {}", id);
        insuranceCropRepository.deleteById(id);
    }
}
