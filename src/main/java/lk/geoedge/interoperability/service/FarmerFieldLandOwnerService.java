package lk.geoedge.interoperability.service;

import java.util.Optional;
import lk.geoedge.interoperability.domain.FarmerFieldLandOwner;
import lk.geoedge.interoperability.repository.FarmerFieldLandOwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link lk.geoedge.interoperability.domain.FarmerFieldLandOwner}.
 */
@Service
@Transactional
public class FarmerFieldLandOwnerService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmerFieldLandOwnerService.class);

    private final FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository;

    public FarmerFieldLandOwnerService(FarmerFieldLandOwnerRepository farmerFieldLandOwnerRepository) {
        this.farmerFieldLandOwnerRepository = farmerFieldLandOwnerRepository;
    }

    /**
     * Save a farmerFieldLandOwner.
     *
     * @param farmerFieldLandOwner the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldLandOwner save(FarmerFieldLandOwner farmerFieldLandOwner) {
        LOG.debug("Request to save FarmerFieldLandOwner : {}", farmerFieldLandOwner);
        return farmerFieldLandOwnerRepository.save(farmerFieldLandOwner);
    }

    /**
     * Update a farmerFieldLandOwner.
     *
     * @param farmerFieldLandOwner the entity to save.
     * @return the persisted entity.
     */
    public FarmerFieldLandOwner update(FarmerFieldLandOwner farmerFieldLandOwner) {
        LOG.debug("Request to update FarmerFieldLandOwner : {}", farmerFieldLandOwner);
        return farmerFieldLandOwnerRepository.save(farmerFieldLandOwner);
    }

    /**
     * Partially update a farmerFieldLandOwner.
     *
     * @param farmerFieldLandOwner the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FarmerFieldLandOwner> partialUpdate(FarmerFieldLandOwner farmerFieldLandOwner) {
        LOG.debug("Request to partially update FarmerFieldLandOwner : {}", farmerFieldLandOwner);

        return farmerFieldLandOwnerRepository
            .findById(farmerFieldLandOwner.getId())
            .map(existingFarmerFieldLandOwner -> {
                if (farmerFieldLandOwner.getCreatedAt() != null) {
                    existingFarmerFieldLandOwner.setCreatedAt(farmerFieldLandOwner.getCreatedAt());
                }
                if (farmerFieldLandOwner.getAddedBy() != null) {
                    existingFarmerFieldLandOwner.setAddedBy(farmerFieldLandOwner.getAddedBy());
                }

                return existingFarmerFieldLandOwner;
            })
            .map(farmerFieldLandOwnerRepository::save);
    }

    /**
     * Get one farmerFieldLandOwner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FarmerFieldLandOwner> findOne(Long id) {
        LOG.debug("Request to get FarmerFieldLandOwner : {}", id);
        return farmerFieldLandOwnerRepository.findById(id);
    }

    /**
     * Delete the farmerFieldLandOwner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FarmerFieldLandOwner : {}", id);
        farmerFieldLandOwnerRepository.deleteById(id);
    }
}
