package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandFarmers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandFarmers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandFarmersRepository
    extends JpaRepository<CultivatedLandFarmers, Long>, JpaSpecificationExecutor<CultivatedLandFarmers> {}
