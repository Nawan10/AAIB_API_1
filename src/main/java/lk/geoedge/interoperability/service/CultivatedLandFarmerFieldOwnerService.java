package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner;
import lk.geoedge.interoperability.repository.CultivatedLandFarmerFieldOwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner}.
 */
@Service
@Transactional
public class CultivatedLandFarmerFieldOwnerService {

    private static final Logger LOG = LoggerFactory.getLogger(CultivatedLandFarmerFieldOwnerService.class);

    private final CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository;

    public CultivatedLandFarmerFieldOwnerService(CultivatedLandFarmerFieldOwnerRepository cultivatedLandFarmerFieldOwnerRepository) {
        this.cultivatedLandFarmerFieldOwnerRepository = cultivatedLandFarmerFieldOwnerRepository;
    }

    /**
     * Save a cultivatedLandFarmerFieldOwner.
     *
     * @param cultivatedLandFarmerFieldOwner the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmerFieldOwner save(CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner) {
        LOG.debug("Request to save CultivatedLandFarmerFieldOwner : {}", cultivatedLandFarmerFieldOwner);
        return cultivatedLandFarmerFieldOwnerRepository.save(cultivatedLandFarmerFieldOwner);
    }

    /**
     * Update a cultivatedLandFarmerFieldOwner.
     *
     * @param cultivatedLandFarmerFieldOwner the entity to save.
     * @return the persisted entity.
     */
    public CultivatedLandFarmerFieldOwner update(CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner) {
        LOG.debug("Request to update CultivatedLandFarmerFieldOwner : {}", cultivatedLandFarmerFieldOwner);
        return cultivatedLandFarmerFieldOwnerRepository.save(cultivatedLandFarmerFieldOwner);
    }

    /**
     * Partially update a cultivatedLandFarmerFieldOwner.
     *
     * @param cultivatedLandFarmerFieldOwner the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CultivatedLandFarmerFieldOwner> partialUpdate(CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner) {
        LOG.debug("Request to partially update CultivatedLandFarmerFieldOwner : {}", cultivatedLandFarmerFieldOwner);

        return cultivatedLandFarmerFieldOwnerRepository
            .findById(cultivatedLandFarmerFieldOwner.getId())
            .map(existingCultivatedLandFarmerFieldOwner -> {
                if (cultivatedLandFarmerFieldOwner.getLandPlotName() != null) {
                    existingCultivatedLandFarmerFieldOwner.setLandPlotName(cultivatedLandFarmerFieldOwner.getLandPlotName());
                }
                if (cultivatedLandFarmerFieldOwner.getLandRegistryNo() != null) {
                    existingCultivatedLandFarmerFieldOwner.setLandRegistryNo(cultivatedLandFarmerFieldOwner.getLandRegistryNo());
                }
                if (cultivatedLandFarmerFieldOwner.getTotalLandExtent() != null) {
                    existingCultivatedLandFarmerFieldOwner.setTotalLandExtent(cultivatedLandFarmerFieldOwner.getTotalLandExtent());
                }
                if (cultivatedLandFarmerFieldOwner.getCalculatedArea() != null) {
                    existingCultivatedLandFarmerFieldOwner.setCalculatedArea(cultivatedLandFarmerFieldOwner.getCalculatedArea());
                }
                if (cultivatedLandFarmerFieldOwner.getProvinceId() != null) {
                    existingCultivatedLandFarmerFieldOwner.setProvinceId(cultivatedLandFarmerFieldOwner.getProvinceId());
                }
                if (cultivatedLandFarmerFieldOwner.getDistrictId() != null) {
                    existingCultivatedLandFarmerFieldOwner.setDistrictId(cultivatedLandFarmerFieldOwner.getDistrictId());
                }
                if (cultivatedLandFarmerFieldOwner.getDsId() != null) {
                    existingCultivatedLandFarmerFieldOwner.setDsId(cultivatedLandFarmerFieldOwner.getDsId());
                }
                if (cultivatedLandFarmerFieldOwner.getGnId() != null) {
                    existingCultivatedLandFarmerFieldOwner.setGnId(cultivatedLandFarmerFieldOwner.getGnId());
                }
                if (cultivatedLandFarmerFieldOwner.getCenterLat() != null) {
                    existingCultivatedLandFarmerFieldOwner.setCenterLat(cultivatedLandFarmerFieldOwner.getCenterLat());
                }
                if (cultivatedLandFarmerFieldOwner.getCenterLng() != null) {
                    existingCultivatedLandFarmerFieldOwner.setCenterLng(cultivatedLandFarmerFieldOwner.getCenterLng());
                }

                return existingCultivatedLandFarmerFieldOwner;
            })
            .map(cultivatedLandFarmerFieldOwnerRepository::save);
    }

    /**
     * Get one cultivatedLandFarmerFieldOwner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CultivatedLandFarmerFieldOwner> findOne(Long id) {
        LOG.debug("Request to get CultivatedLandFarmerFieldOwner : {}", id);
        return cultivatedLandFarmerFieldOwnerRepository.findById(id);
    }

    /**
     * Delete the cultivatedLandFarmerFieldOwner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CultivatedLandFarmerFieldOwner : {}", id);
        cultivatedLandFarmerFieldOwnerRepository.deleteById(id);
    }
}
