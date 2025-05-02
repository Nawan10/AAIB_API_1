package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldOwner;
import lk.geoedge.interoperability.repository.FarmerFieldOwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.FarmerFieldOwner}.
 */
@Service
@Transactional
public class FarmerFieldOwnerService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldOwnerService.class);

    private final FarmerFieldOwnerRepository farmerFieldOwnerRepository;

    public FarmerFieldOwnerService(FarmerFieldOwnerRepository farmerFieldOwnerRepository) {
        this.farmerFieldOwnerRepository = farmerFieldOwnerRepository;
    }

    /**
     * Save a farmerFieldOwner.
     *
     * @param farmerFieldOwner the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldOwner save(FarmerFieldOwner farmerFieldOwner) {
        LOG.debug("Request to save FarmerFieldOwner : {}", farmerFieldOwner);
        return farmerFieldOwnerRepository.save(farmerFieldOwner);
    }

    /**
     * Update a farmerFieldOwner.
     *
     * @param farmerFieldOwner the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldOwner update(FarmerFieldOwner farmerFieldOwner) {
        LOG.debug("Request to update FarmerFieldOwner : {}", farmerFieldOwner);
        return farmerFieldOwnerRepository.save(farmerFieldOwner);
    }

    /**
     * Partially update a farmerFieldOwner.
     *
     * @param farmerFieldOwner the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FarmerFieldOwner> partialUpdate(FarmerFieldOwner farmerFieldOwner) {
        LOG.debug("Request to partially update FarmerFieldOwner : {}", farmerFieldOwner);

        return farmerFieldOwnerRepository
            .findById(farmerFieldOwner.getId())
            .map(existingFarmerFieldOwner -> {
                if (farmerFieldOwner.getLandPlotName() != null) {
                    existingFarmerFieldOwner.setLandPlotName(farmerFieldOwner.getLandPlotName());
                }
                if (farmerFieldOwner.getLandRegistryNo() != null) {
                    existingFarmerFieldOwner.setLandRegistryNo(farmerFieldOwner.getLandRegistryNo());
                }
                if (farmerFieldOwner.getTotalLandExtent() != null) {
                    existingFarmerFieldOwner.setTotalLandExtent(farmerFieldOwner.getTotalLandExtent());
                }
                if (farmerFieldOwner.getCalculatedArea() != null) {
                    existingFarmerFieldOwner.setCalculatedArea(farmerFieldOwner.getCalculatedArea());
                }
                if (farmerFieldOwner.getProvinceId() != null) {
                    existingFarmerFieldOwner.setProvinceId(farmerFieldOwner.getProvinceId());
                }
                if (farmerFieldOwner.getDistrictId() != null) {
                    existingFarmerFieldOwner.setDistrictId(farmerFieldOwner.getDistrictId());
                }
                if (farmerFieldOwner.getDsId() != null) {
                    existingFarmerFieldOwner.setDsId(farmerFieldOwner.getDsId());
                }
                if (farmerFieldOwner.getGnId() != null) {
                    existingFarmerFieldOwner.setGnId(farmerFieldOwner.getGnId());
                }
                if (farmerFieldOwner.getCenterLat() != null) {
                    existingFarmerFieldOwner.setCenterLat(farmerFieldOwner.getCenterLat());
                }
                if (farmerFieldOwner.getCenterLng() != null) {
                    existingFarmerFieldOwner.setCenterLng(farmerFieldOwner.getCenterLng());
                }

                return existingFarmerFieldOwner;
            })
            .map(farmerFieldOwnerRepository::save);
    }

    /**
     * Get one farmerFieldOwner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FarmerFieldOwner> findOne(Long id) {
        LOG.debug("Request to get FarmerFieldOwner : {}", id);
        return farmerFieldOwnerRepository.findById(id);
    }

    /**
     * Delete the farmerFieldOwner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FarmerFieldOwner : {}", id);
        farmerFieldOwnerRepository.deleteById(id);
    }
}
