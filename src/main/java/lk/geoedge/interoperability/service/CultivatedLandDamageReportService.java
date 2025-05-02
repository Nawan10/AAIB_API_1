package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandDamageReport;
import lk.geoedge.interoperability.repository.CultivatedLandDamageReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReport}.
 */
@Service
@Transactional
public class CultivatedLandDamageReportService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandDamageReportService.class);

    private final CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository;

    public CultivatedLandDamageReportService(CultivatedLandDamageReportRepository cultivatedLandDamageReportRepository) {
        this.cultivatedLandDamageReportRepository = cultivatedLandDamageReportRepository;
    }

    /**
     * Save a cultivatedLandDamageReport.
     *
     * @param cultivatedLandDamageReport the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReport save(CultivatedLandDamageReport cultivatedLandDamageReport) {
        LOG.debug("Request to save CultivatedLandDamageReport : {}", cultivatedLandDamageReport);
        return cultivatedLandDamageReportRepository.save(cultivatedLandDamageReport);
    }

    /**
     * Update a cultivatedLandDamageReport.
     *
     * @param cultivatedLandDamageReport the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandDamageReport update(CultivatedLandDamageReport cultivatedLandDamageReport) {
        LOG.debug("Request to update CultivatedLandDamageReport : {}", cultivatedLandDamageReport);
        return cultivatedLandDamageReportRepository.save(cultivatedLandDamageReport);
    }

    /**
     * Partially update a cultivatedLandDamageReport.
     *
     * @param cultivatedLandDamageReport the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandDamageReport> partialUpdate(CultivatedLandDamageReport cultivatedLandDamageReport) {
        LOG.debug("Request to partially update CultivatedLandDamageReport : {}", cultivatedLandDamageReport);

        return cultivatedLandDamageReportRepository
            .findById(cultivatedLandDamageReport.getId())
            .map(existingCultivatedLandDamageReport -> {
                if (cultivatedLandDamageReport.getDamageReasonId() != null) {
                    existingCultivatedLandDamageReport.setDamageReasonId(cultivatedLandDamageReport.getDamageReasonId());
                }
                if (cultivatedLandDamageReport.getDamageServerityId() != null) {
                    existingCultivatedLandDamageReport.setDamageServerityId(cultivatedLandDamageReport.getDamageServerityId());
                }
                if (cultivatedLandDamageReport.getDamageDateMonitor() != null) {
                    existingCultivatedLandDamageReport.setDamageDateMonitor(cultivatedLandDamageReport.getDamageDateMonitor());
                }
                if (cultivatedLandDamageReport.getDescription() != null) {
                    existingCultivatedLandDamageReport.setDescription(cultivatedLandDamageReport.getDescription());
                }
                if (cultivatedLandDamageReport.getFarmerComment() != null) {
                    existingCultivatedLandDamageReport.setFarmerComment(cultivatedLandDamageReport.getFarmerComment());
                }
                if (cultivatedLandDamageReport.getEstimatedYield() != null) {
                    existingCultivatedLandDamageReport.setEstimatedYield(cultivatedLandDamageReport.getEstimatedYield());
                }
                if (cultivatedLandDamageReport.getCreatedAt() != null) {
                    existingCultivatedLandDamageReport.setCreatedAt(cultivatedLandDamageReport.getCreatedAt());
                }
                if (cultivatedLandDamageReport.getAddedBy() != null) {
                    existingCultivatedLandDamageReport.setAddedBy(cultivatedLandDamageReport.getAddedBy());
                }

                return existingCultivatedLandDamageReport;
            })
            .map(cultivatedLandDamageReportRepository::save);
    }

    /**
     * Get one cultivatedLandDamageReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandDamageReport> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandDamageReport : {}", id);
        return cultivatedLandDamageReportRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandDamageReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandDamageReport : {}", id);
        cultivatedLandDamageReportRepository.deleteById(id);
    }
}
