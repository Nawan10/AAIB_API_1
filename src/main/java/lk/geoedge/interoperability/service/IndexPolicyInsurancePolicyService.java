package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy;
import lk.geoedge.interoperability.repository.IndexPolicyInsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy}.
 */
@Service
@Transactional
public class IndexPolicyInsurancePolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(IndexPolicyInsurancePolicyService.class);

    private final IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository;

    public IndexPolicyInsurancePolicyService(IndexPolicyInsurancePolicyRepository indexPolicyInsurancePolicyRepository) {
        this.indexPolicyInsurancePolicyRepository = indexPolicyInsurancePolicyRepository;
    }

    /**
     * Save a indexPolicyInsurancePolicy.
     *
     * @param indexPolicyInsurancePolicy the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyInsurancePolicy save(IndexPolicyInsurancePolicy indexPolicyInsurancePolicy) {
        LOG.debug("Request to save IndexPolicyInsurancePolicy : {}", indexPolicyInsurancePolicy);
        return indexPolicyInsurancePolicyRepository.save(indexPolicyInsurancePolicy);
    }

    /**
     * Update a indexPolicyInsurancePolicy.
     *
     * @param indexPolicyInsurancePolicy the entity to save.
     * @return the persisted entity.
     */
    public IndexPolicyInsurancePolicy update(IndexPolicyInsurancePolicy indexPolicyInsurancePolicy) {
        LOG.debug("Request to update IndexPolicyInsurancePolicy : {}", indexPolicyInsurancePolicy);
        return indexPolicyInsurancePolicyRepository.save(indexPolicyInsurancePolicy);
    }

    /**
     * Partially update a indexPolicyInsurancePolicy.
     *
     * @param indexPolicyInsurancePolicy the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndexPolicyInsurancePolicy> partialUpdate(IndexPolicyInsurancePolicy indexPolicyInsurancePolicy) {
        LOG.debug("Request to partially update IndexPolicyInsurancePolicy : {}", indexPolicyInsurancePolicy);

        return indexPolicyInsurancePolicyRepository
            .findById(indexPolicyInsurancePolicy.getId())
            .map(existingIndexPolicyInsurancePolicy -> {
                if (indexPolicyInsurancePolicy.getName() != null) {
                    existingIndexPolicyInsurancePolicy.setName(indexPolicyInsurancePolicy.getName());
                }
                if (indexPolicyInsurancePolicy.getPolicyNo() != null) {
                    existingIndexPolicyInsurancePolicy.setPolicyNo(indexPolicyInsurancePolicy.getPolicyNo());
                }
                if (indexPolicyInsurancePolicy.getIsActivate() != null) {
                    existingIndexPolicyInsurancePolicy.setIsActivate(indexPolicyInsurancePolicy.getIsActivate());
                }

                return existingIndexPolicyInsurancePolicy;
            })
            .map(indexPolicyInsurancePolicyRepository::save);
    }

    /**
     * Get one indexPolicyInsurancePolicy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexPolicyInsurancePolicy> findOne(Long id) {
        LOG.debug("Request to get IndexPolicyInsurancePolicy : {}", id);
        return indexPolicyInsurancePolicyRepository.findById(id);
    }

    /**
     * Delete the indexPolicyInsurancePolicy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndexPolicyInsurancePolicy : {}", id);
        indexPolicyInsurancePolicyRepository.deleteById(id);
    }
}
