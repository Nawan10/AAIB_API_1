package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand}.
 */
@Service
@Transactional
public class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCoveragesInsuranceCultivatedLandService.class);

    private final InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;

    public InsuranceCultivatedLandCoveragesInsuranceCultivatedLandService(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository
    ) {
        this.insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository =
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository;
    }

    /**
     * Save a insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param insuranceCultivatedLandCoveragesInsuranceCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCoveragesInsuranceCultivatedLand save(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        LOG.debug(
            "Request to save InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}",
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.save(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
    }

    /**
     * Update a insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param insuranceCultivatedLandCoveragesInsuranceCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCoveragesInsuranceCultivatedLand update(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        LOG.debug(
            "Request to update InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}",
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.save(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );
    }

    /**
     * Partially update a insuranceCultivatedLandCoveragesInsuranceCultivatedLand.
     *
     * @param insuranceCultivatedLandCoveragesInsuranceCultivatedLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> partialUpdate(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        LOG.debug(
            "Request to partially update InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}",
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand
        );

        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository
            .findById(insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getId())
            .map(existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand -> {
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getCropDurationId() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setCropDurationId(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getCropDurationId()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getInsurancePoliceId() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setInsurancePoliceId(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getInsurancePoliceId()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getSumInsuredPerAcre() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setSumInsuredPerAcre(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getSumInsuredPerAcre()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getInsuranceExtent() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setInsuranceExtent(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getInsuranceExtent()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getSumAmount() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setSumAmount(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getSumAmount()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getInsuranceStatus() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setInsuranceStatus(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getInsuranceStatus()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getCreatedAt() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setCreatedAt(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getCreatedAt()
                    );
                }
                if (insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getAddedBy() != null) {
                    existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand.setAddedBy(
                        insuranceCultivatedLandCoveragesInsuranceCultivatedLand.getAddedBy()
                    );
                }

                return existingInsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
            })
            .map(insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository::save);
    }

    /**
     * Get one insuranceCultivatedLandCoveragesInsuranceCultivatedLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> findOne(Long id) {
        LOG.debug("Request to get InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}", id);
        return insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.findById(id);
    }

    /**
     * Delete the insuranceCultivatedLandCoveragesInsuranceCultivatedLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCultivatedLandCoveragesInsuranceCultivatedLand : {}", id);
        insuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository.deleteById(id);
    }
}
