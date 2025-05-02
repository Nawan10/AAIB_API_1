package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.FarmerFieldLandOwnerFarmer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FarmerFieldLandOwnerFarmer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FarmerFieldLandOwnerFarmerRepository
    extends JpaRepository<FarmerFieldLandOwnerFarmer, Long>, JpaSpecificationExecutor<FarmerFieldLandOwnerFarmer> {}
