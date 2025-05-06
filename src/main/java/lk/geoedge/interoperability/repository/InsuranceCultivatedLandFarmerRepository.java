package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCultivatedLandFarmer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCultivatedLandFarmerRepository
    extends JpaRepository<InsuranceCultivatedLandFarmer, Long>, JpaSpecificationExecutor<InsuranceCultivatedLandFarmer> {}
