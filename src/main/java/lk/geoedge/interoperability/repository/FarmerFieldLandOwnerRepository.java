package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.FarmerFieldLandOwner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FarmerFieldLandOwner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FarmerFieldLandOwnerRepository
    extends JpaRepository<FarmerFieldLandOwner, Long>, JpaSpecificationExecutor<FarmerFieldLandOwner> {}
