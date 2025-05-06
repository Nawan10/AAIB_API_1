package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages}.
 */
@Service
@Transactional
public class InsuranceCultivatedLandCoveragesService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCoveragesService.class);

    private final InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository;

    public InsuranceCultivatedLandCoveragesService(InsuranceCultivatedLandCoveragesRepository insuranceCultivatedLandCoveragesRepository) {
        this.insuranceCultivatedLandCoveragesRepository = insuranceCultivatedLandCoveragesRepository;
    }

    /**
     * Save a insuranceCultivatedLandCoverages.
     *
     * @param insuranceCultivatedLandCoverages the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCoverages save(InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages) {
        LOG.debug("Request to save InsuranceCultivatedLandCoverages : {}", insuranceCultivatedLandCoverages);
        return insuranceCultivatedLandCoveragesRepository.save(insuranceCultivatedLandCoverages);
    }

    /**
     * Update a insuranceCultivatedLandCoverages.
     *
     * @param insuranceCultivatedLandCoverages the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCoverages update(InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages) {
        LOG.debug("Request to update InsuranceCultivatedLandCoverages : {}", insuranceCultivatedLandCoverages);
        return insuranceCultivatedLandCoveragesRepository.save(insuranceCultivatedLandCoverages);
    }

    /**
     * Partially update a insuranceCultivatedLandCoverages.
     *
     * @param insuranceCultivatedLandCoverages the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCultivatedLandCoverages> partialUpdate(InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages) {
        LOG.debug("Request to partially update InsuranceCultivatedLandCoverages : {}", insuranceCultivatedLandCoverages);

        return insuranceCultivatedLandCoveragesRepository
            .findById(insuranceCultivatedLandCoverages.getId())
            .map(existingInsuranceCultivatedLandCoverages -> {
                if (insuranceCultivatedLandCoverages.getConverageAmount() != null) {
                    existingInsuranceCultivatedLandCoverages.setConverageAmount(insuranceCultivatedLandCoverages.getConverageAmount());
                }
                if (insuranceCultivatedLandCoverages.getIsSelect() != null) {
                    existingInsuranceCultivatedLandCoverages.setIsSelect(insuranceCultivatedLandCoverages.getIsSelect());
                }
                if (insuranceCultivatedLandCoverages.getCreatedAt() != null) {
                    existingInsuranceCultivatedLandCoverages.setCreatedAt(insuranceCultivatedLandCoverages.getCreatedAt());
                }
                if (insuranceCultivatedLandCoverages.getAddedBy() != null) {
                    existingInsuranceCultivatedLandCoverages.setAddedBy(insuranceCultivatedLandCoverages.getAddedBy());
                }

                return existingInsuranceCultivatedLandCoverages;
            })
            .map(insuranceCultivatedLandCoveragesRepository::save);
    }

    /**
     * Get one insuranceCultivatedLandCoverages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCultivatedLandCoverages> findOne(Long id) {
        LOG.debug("Request to get InsuranceCultivatedLandCoverages : {}", id);
        return insuranceCultivatedLandCoveragesRepository.findById(id);
    }

    /**
     * Delete the insuranceCultivatedLandCoverages by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCultivatedLandCoverages : {}", id);
        insuranceCultivatedLandCoveragesRepository.deleteById(id);
    }
}
