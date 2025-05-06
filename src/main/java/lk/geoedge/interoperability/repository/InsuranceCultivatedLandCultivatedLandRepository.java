package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCultivatedLandCultivatedLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCultivatedLandCultivatedLandRepository
    extends JpaRepository<InsuranceCultivatedLandCultivatedLand, Long>, JpaSpecificationExecutor<InsuranceCultivatedLandCultivatedLand> {}
