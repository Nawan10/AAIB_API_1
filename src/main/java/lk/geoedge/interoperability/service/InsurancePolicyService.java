package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsurancePolicy;
import lk.geoedge.interoperability.repository.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsurancePolicy}.
 */
@Service
@Transactional
public class InsurancePolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePolicyService.class);

    private final InsurancePolicyRepository insurancePolicyRepository;

    public InsurancePolicyService(InsurancePolicyRepository insurancePolicyRepository) {
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    /**
     * Save a insurancePolicy.
     *
     * @param insurancePolicy the entity to save.
     * @return the persisted entity.
     */
    public InsurancePolicy save(InsurancePolicy insurancePolicy) {
        LOG.debug("Request to save InsurancePolicy : {}", insurancePolicy);
        return insurancePolicyRepository.save(insurancePolicy);
    }

    /**
     * Update a insurancePolicy.
     *
     * @param insurancePolicy the entity to save.
     * @return the persisted entity.
     */
    public InsurancePolicy update(InsurancePolicy insurancePolicy) {
        LOG.debug("Request to update InsurancePolicy : {}", insurancePolicy);
        return insurancePolicyRepository.save(insurancePolicy);
    }

    /**
     * Partially update a insurancePolicy.
     *
     * @param insurancePolicy the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsurancePolicy> partialUpdate(InsurancePolicy insurancePolicy) {
        LOG.debug("Request to partially update InsurancePolicy : {}", insurancePolicy);

        return insurancePolicyRepository
            .findById(insurancePolicy.getId())
            .map(existingInsurancePolicy -> {
                if (insurancePolicy.getName() != null) {
                    existingInsurancePolicy.setName(insurancePolicy.getName());
                }
                if (insurancePolicy.getPolicyNo() != null) {
                    existingInsurancePolicy.setPolicyNo(insurancePolicy.getPolicyNo());
                }
                if (insurancePolicy.getIsActivate() != null) {
                    existingInsurancePolicy.setIsActivate(insurancePolicy.getIsActivate());
                }

                return existingInsurancePolicy;
            })
            .map(insurancePolicyRepository::save);
    }

    /**
     * Get one insurancePolicy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsurancePolicy> findOne(Long id) {
        LOG.debug("Request to get InsurancePolicy : {}", id);
        return insurancePolicyRepository.findById(id);
    }

    /**
     * Delete the insurancePolicy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsurancePolicy : {}", id);
        insurancePolicyRepository.deleteById(id);
    }
}
