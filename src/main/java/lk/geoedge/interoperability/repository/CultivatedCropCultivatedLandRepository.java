package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedCropCultivatedLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedCropCultivatedLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedCropCultivatedLandRepository
    extends JpaRepository<CultivatedCropCultivatedLand, Long>, JpaSpecificationExecutor<CultivatedCropCultivatedLand> {}
