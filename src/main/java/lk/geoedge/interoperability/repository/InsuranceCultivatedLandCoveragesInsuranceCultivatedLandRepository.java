package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCultivatedLandCoveragesInsuranceCultivatedLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCultivatedLandCoveragesInsuranceCultivatedLandRepository
    extends
        JpaRepository<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand, Long>,
        JpaSpecificationExecutor<InsuranceCultivatedLandCoveragesInsuranceCultivatedLand> {}
