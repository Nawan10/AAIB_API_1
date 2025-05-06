package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandFarmersFarmer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandFarmersFarmer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandFarmersFarmerRepository
    extends JpaRepository<CultivatedLandFarmersFarmer, Long>, JpaSpecificationExecutor<CultivatedLandFarmersFarmer> {}
