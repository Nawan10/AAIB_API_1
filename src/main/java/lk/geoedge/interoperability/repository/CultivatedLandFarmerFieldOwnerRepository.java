package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandFarmerFieldOwner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandFarmerFieldOwnerRepository
    extends JpaRepository<CultivatedLandFarmerFieldOwner, Long>, JpaSpecificationExecutor<CultivatedLandFarmerFieldOwner> {}
