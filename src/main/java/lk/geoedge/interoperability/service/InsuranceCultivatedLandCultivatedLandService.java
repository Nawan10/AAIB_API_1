package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand;
import lk.geoedge.interoperability.repository.InsuranceCultivatedLandCultivatedLandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand}.
 */
@Service
@Transactional
public class InsuranceCultivatedLandCultivatedLandService {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCultivatedLandCultivatedLandService.class);

    private final InsuranceCultivatedLandCultivatedLandRepository insuranceCultivatedLandCultivatedLandRepository;

    public InsuranceCultivatedLandCultivatedLandService(
        InsuranceCultivatedLandCultivatedLandRepository insuranceCultivatedLandCultivatedLandRepository
    ) {
        this.insuranceCultivatedLandCultivatedLandRepository = insuranceCultivatedLandCultivatedLandRepository;
    }

    /**
     * Save a insuranceCultivatedLandCultivatedLand.
     *
     * @param insuranceCultivatedLandCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCultivatedLand save(InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand) {
        LOG.debug("Request to save InsuranceCultivatedLandCultivatedLand : {}", insuranceCultivatedLandCultivatedLand);
        return insuranceCultivatedLandCultivatedLandRepository.save(insuranceCultivatedLandCultivatedLand);
    }

    /**
     * Update a insuranceCultivatedLandCultivatedLand.
     *
     * @param insuranceCultivatedLandCultivatedLand the entity to save.
     * @return the persisted entity.
     */
    public InsuranceCultivatedLandCultivatedLand update(InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand) {
        LOG.debug("Request to update InsuranceCultivatedLandCultivatedLand : {}", insuranceCultivatedLandCultivatedLand);
        return insuranceCultivatedLandCultivatedLandRepository.save(insuranceCultivatedLandCultivatedLand);
    }

    /**
     * Partially update a insuranceCultivatedLandCultivatedLand.
     *
     * @param insuranceCultivatedLandCultivatedLand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InsuranceCultivatedLandCultivatedLand> partialUpdate(
        InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand
    ) {
        LOG.debug("Request to partially update InsuranceCultivatedLandCultivatedLand : {}", insuranceCultivatedLandCultivatedLand);

        return insuranceCultivatedLandCultivatedLandRepository
            .findById(insuranceCultivatedLandCultivatedLand.getId())
            .map(existingInsuranceCultivatedLandCultivatedLand -> {
                if (insuranceCultivatedLandCultivatedLand.getLandStatus() != null) {
                    existingInsuranceCultivatedLandCultivatedLand.setLandStatus(insuranceCultivatedLandCultivatedLand.getLandStatus());
                }
                if (insuranceCultivatedLandCultivatedLand.getUrea() != null) {
                    existingInsuranceCultivatedLandCultivatedLand.setUrea(insuranceCultivatedLandCultivatedLand.getUrea());
                }
                if (insuranceCultivatedLandCultivatedLand.getMop() != null) {
                    existingInsuranceCultivatedLandCultivatedLand.setMop(insuranceCultivatedLandCultivatedLand.getMop());
                }
                if (insuranceCultivatedLandCultivatedLand.getTsp() != null) {
                    existingInsuranceCultivatedLandCultivatedLand.setTsp(insuranceCultivatedLandCultivatedLand.getTsp());
                }
                if (insuranceCultivatedLandCultivatedLand.getCreatedAt() != null) {
                    existingInsuranceCultivatedLandCultivatedLand.setCreatedAt(insuranceCultivatedLandCultivatedLand.getCreatedAt());
                }
                if (insuranceCultivatedLandCultivatedLand.getAddedBy() != null) {
                    existingInsuranceCultivatedLandCultivatedLand.setAddedBy(insuranceCultivatedLandCultivatedLand.getAddedBy());
                }

                return existingInsuranceCultivatedLandCultivatedLand;
            })
            .map(insuranceCultivatedLandCultivatedLandRepository::save);
    }

    /**
     * Get one insuranceCultivatedLandCultivatedLand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InsuranceCultivatedLandCultivatedLand> findOne(Long id) {
        LOG.debug("Request to get InsuranceCultivatedLandCultivatedLand : {}", id);
        return insuranceCultivatedLandCultivatedLandRepository.findById(id);
    }

    /**
     * Delete the insuranceCultivatedLandCultivatedLand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InsuranceCultivatedLandCultivatedLand : {}", id);
        insuranceCultivatedLandCultivatedLandRepository.deleteById(id);
    }
}
