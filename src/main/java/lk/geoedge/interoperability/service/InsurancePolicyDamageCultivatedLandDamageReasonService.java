package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageCultivatedLandDamageReasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason}.
 */
@Service
@Transactional
public class InsurancePolicyDamageCultivatedLandDamageReasonService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyDamageCultivatedLandDamageReasonService.class);

    private final InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository;

    public InsurancePolicyDamageCultivatedLandDamageReasonService(
        InsurancePolicyDamageCultivatedLandDamageReasonRepository insurancePolicyDamageCultivatedLandDamageReasonRepository
    ) {
        this.insurancePolicyDamageCultivatedLandDamageReasonRepository = insurancePolicyDamageCultivatedLandDamageReasonRepository;
    }

    /**
     * Save a insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param insurancePolicyDamageCultivatedLandDamageReason the entity to save.
     * @return the persisted entity.
     */
    public InsurancePolicyDamageCultivatedLandDamageReason save(
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) {
        LOG.debug("Request to save InsurancePolicyDamageCultivatedLandDamageReason : {}", insurancePolicyDamageCultivatedLandDamageReason);
        return insurancePolicyDamageCultivatedLandDamageReasonRepository.save(insurancePolicyDamageCultivatedLandDamageReason);
    }

    /**
     * Update a insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param insurancePolicyDamageCultivatedLandDamageReason the entity to save.
     * @return the persisted entity.
     */
    public InsurancePolicyDamageCultivatedLandDamageReason update(
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) {
        LOG.debug(
            "Request to update InsurancePolicyDamageCultivatedLandDamageReason : {}",
            insurancePolicyDamageCultivatedLandDamageReason
        );
        return insurancePolicyDamageCultivatedLandDamageReasonRepository.save(insurancePolicyDamageCultivatedLandDamageReason);
    }

    /**
     * Partially update a insurancePolicyDamageCultivatedLandDamageReason.
     *
     * @param insurancePolicyDamageCultivatedLandDamageReason the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsurancePolicyDamageCultivatedLandDamageReason> partialUpdate(
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) {
        LOG.debug(
            "Request to partially update InsurancePolicyDamageCultivatedLandDamageReason : {}",
            insurancePolicyDamageCultivatedLandDamageReason
        );

        return insurancePolicyDamageCultivatedLandDamageReasonRepository
            .findById(insurancePolicyDamageCultivatedLandDamageReason.getId())
            .map(existingInsurancePolicyDamageCultivatedLandDamageReason -> {
                if (insurancePolicyDamageCultivatedLandDamageReason.getName() != null) {
                    existingInsurancePolicyDamageCultivatedLandDamageReason.setName(
                        insurancePolicyDamageCultivatedLandDamageReason.getName()
                    );
                }
                if (insurancePolicyDamageCultivatedLandDamageReason.getDamageCategoryId() != null) {
                    existingInsurancePolicyDamageCultivatedLandDamageReason.setDamageCategoryId(
                        insurancePolicyDamageCultivatedLandDamageReason.getDamageCategoryId()
                    );
                }
                if (insurancePolicyDamageCultivatedLandDamageReason.getDamageTypeId() != null) {
                    existingInsurancePolicyDamageCultivatedLandDamageReason.setDamageTypeId(
                        insurancePolicyDamageCultivatedLandDamageReason.getDamageTypeId()
                    );
                }

                return existingInsurancePolicyDamageCultivatedLandDamageReason;
            })
            .map(insurancePolicyDamageCultivatedLandDamageReasonRepository::save);
    }

    /**
     * Get one insurancePolicyDamageCultivatedLandDamageReason by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsurancePolicyDamageCultivatedLandDamageReason> findOne(Long id) {
        LOG.debug("Request to get InsurancePolicyDamageCultivatedLandDamageReason : {}", id);
        return insurancePolicyDamageCultivatedLandDamageReasonRepository.findById(id);
    }

    /**
     * Delete the insurancePolicyDamageCultivatedLandDamageReason by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsurancePolicyDamageCultivatedLandDamageReason : {}", id);
        insurancePolicyDamageCultivatedLandDamageReasonRepository.deleteById(id);
    }
}
