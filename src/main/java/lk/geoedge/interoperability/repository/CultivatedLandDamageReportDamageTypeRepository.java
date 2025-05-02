package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandDamageReportDamageType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandDamageReportDamageTypeRepository
    extends JpaRepository<CultivatedLandDamageReportDamageType, Long>, JpaSpecificationExecutor<CultivatedLandDamageReportDamageType> {}
