package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsurancePolicyDamage;
import lk.geoedge.interoperability.repository.InsurancePolicyDamageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsurancePolicyDamage}.
 */
@Service
@Transactional
public class InsurancePolicyDamageService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyDamageService.class);

    private final InsurancePolicyDamageRepository insurancePolicyDamageRepository;

    public InsurancePolicyDamageService(InsurancePolicyDamageRepository insurancePolicyDamageRepository) {
        this.insurancePolicyDamageRepository = insurancePolicyDamageRepository;
    }

    /**
     * Save a insurancePolicyDamage.
     *
     * @param insurancePolicyDamage the entity to save.
     * @return the persisted entity.
     */
    public InsurancePolicyDamage save(InsurancePolicyDamage insurancePolicyDamage) {
        LOG.debug("Request to save InsurancePolicyDamage : {}", insurancePolicyDamage);
        return insurancePolicyDamageRepository.save(insurancePolicyDamage);
    }

    /**
     * Update a insurancePolicyDamage.
     *
     * @param insurancePolicyDamage the entity to save.
     * @return the persisted entity.
     */
    public InsurancePolicyDamage update(InsurancePolicyDamage insurancePolicyDamage) {
        LOG.debug("Request to update InsurancePolicyDamage : {}", insurancePolicyDamage);
        return insurancePolicyDamageRepository.save(insurancePolicyDamage);
    }

    /**
     * Partially update a insurancePolicyDamage.
     *
     * @param insurancePolicyDamage the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsurancePolicyDamage> partialUpdate(InsurancePolicyDamage insurancePolicyDamage) {
        LOG.debug("Request to partially update InsurancePolicyDamage : {}", insurancePolicyDamage);

        return insurancePolicyDamageRepository
            .findById(insurancePolicyDamage.getId())
            .map(existingInsurancePolicyDamage -> {
                if (insurancePolicyDamage.getPercentage() != null) {
                    existingInsurancePolicyDamage.setPercentage(insurancePolicyDamage.getPercentage());
                }
                if (insurancePolicyDamage.getIsFree() != null) {
                    existingInsurancePolicyDamage.setIsFree(insurancePolicyDamage.getIsFree());
                }
                if (insurancePolicyDamage.getIsPaid() != null) {
                    existingInsurancePolicyDamage.setIsPaid(insurancePolicyDamage.getIsPaid());
                }

                return existingInsurancePolicyDamage;
            })
            .map(insurancePolicyDamageRepository::save);
    }

    /**
     * Get one insurancePolicyDamage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsurancePolicyDamage> findOne(Long id) {
        LOG.debug("Request to get InsurancePolicyDamage : {}", id);
        return insurancePolicyDamageRepository.findById(id);
    }

    /**
     * Delete the insurancePolicyDamage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsurancePolicyDamage : {}", id);
        insurancePolicyDamageRepository.deleteById(id);
    }
}
