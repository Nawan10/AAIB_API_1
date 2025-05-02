package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.FarmerFieldOwnerCropType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FarmerFieldOwnerCropType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FarmerFieldOwnerCropTypeRepository
    extends JpaRepository<FarmerFieldOwnerCropType, Long>, JpaSpecificationExecutor<FarmerFieldOwnerCropType> {}
