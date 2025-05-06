package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandFarmerLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandFarmerLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandFarmerLandRepository
    extends JpaRepository<CultivatedLandFarmerLand, Long>, JpaSpecificationExecutor<CultivatedLandFarmerLand> {}
