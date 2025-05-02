package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsuranceCultivatedLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsuranceCultivatedLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceCultivatedLandRepository
    extends JpaRepository<InsuranceCultivatedLand, Long>, JpaSpecificationExecutor<InsuranceCultivatedLand> {}
