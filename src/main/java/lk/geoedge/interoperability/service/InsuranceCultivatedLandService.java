package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLand}.
 */
@Service
@Transactional
public class InsuranceCultivatedLandService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandService.class);

    private final InsuranceCultivatedLandRepository insuranceCultivatedLandRepository;

    public InsuranceCultivatedLandService(InsuranceCultivatedLandRepository insuranceCultivatedLandRepository) {
        this.insuranceCultivatedLandRepository = insuranceCultivatedLandRepository;
    }

    /**
     * Save a insuranceCultivatedLand.
     *
     * @param insuranceCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLand save(InsuranceCultivatedLand insuranceCultivatedLand) {
        LOG.debug("Request to save InsuranceCultivatedLand : {}", insuranceCultivatedLand);
        return insuranceCultivatedLandRepository.save(insuranceCultivatedLand);
    }

    /**
     * Update a insuranceCultivatedLand.
     *
     * @param insuranceCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLand update(InsuranceCultivatedLand insuranceCultivatedLand) {
        LOG.debug("Request to update InsuranceCultivatedLand : {}", insuranceCultivatedLand);
        return insuranceCultivatedLandRepository.save(insuranceCultivatedLand);
    }

    /**
     * Partially update a insuranceCultivatedLand.
     *
     * @param insuranceCultivatedLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCultivatedLand> partialUpdate(InsuranceCultivatedLand insuranceCultivatedLand) {
        LOG.debug("Request to partially update InsuranceCultivatedLand : {}", insuranceCultivatedLand);

        return insuranceCultivatedLandRepository
            .findById(insuranceCultivatedLand.getId())
            .map(existingInsuranceCultivatedLand -> {
                if (insuranceCultivatedLand.getCropDurationId() != null) {
                    existingInsuranceCultivatedLand.setCropDurationId(insuranceCultivatedLand.getCropDurationId());
                }
                if (insuranceCultivatedLand.getInsurancePoliceId() != null) {
                    existingInsuranceCultivatedLand.setInsurancePoliceId(insuranceCultivatedLand.getInsurancePoliceId());
                }
                if (insuranceCultivatedLand.getSumInsuredPerAcre() != null) {
                    existingInsuranceCultivatedLand.setSumInsuredPerAcre(insuranceCultivatedLand.getSumInsuredPerAcre());
                }
                if (insuranceCultivatedLand.getInsuranceExtent() != null) {
                    existingInsuranceCultivatedLand.setInsuranceExtent(insuranceCultivatedLand.getInsuranceExtent());
                }
                if (insuranceCultivatedLand.getSumAmount() != null) {
                    existingInsuranceCultivatedLand.setSumAmount(insuranceCultivatedLand.getSumAmount());
                }
                if (insuranceCultivatedLand.getInsuranceStatus() != null) {
                    existingInsuranceCultivatedLand.setInsuranceStatus(insuranceCultivatedLand.getInsuranceStatus());
                }
                if (insuranceCultivatedLand.getCreatedAt() != null) {
                    existingInsuranceCultivatedLand.setCreatedAt(insuranceCultivatedLand.getCreatedAt());
                }
                if (insuranceCultivatedLand.getAddedBy() != null) {
                    existingInsuranceCultivatedLand.setAddedBy(insuranceCultivatedLand.getAddedBy());
                }

                return existingInsuranceCultivatedLand;
            })
            .map(insuranceCultivatedLandRepository::save);
    }

    /**
     * Get one insuranceCultivatedLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCultivatedLand> findOne(Long id) {
        LOG.debug("Request to get InsuranceCultivatedLand : {}", id);
        return insuranceCultivatedLandRepository.findById(id);
    }

    /**
     * Delete the insuranceCultivatedLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCultivatedLand : {}", id);
        insuranceCultivatedLandRepository.deleteById(id);
    }
}
