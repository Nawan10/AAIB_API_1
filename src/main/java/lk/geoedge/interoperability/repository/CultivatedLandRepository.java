package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandRepository extends JpaRepository<CultivatedLand, Long>, JpaSpecificationExecutor<CultivatedLand> {}
