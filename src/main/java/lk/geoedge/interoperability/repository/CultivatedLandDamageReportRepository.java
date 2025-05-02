package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandDamageReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandDamageReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandDamageReportRepository
    extends JpaRepository<CultivatedLandDamageReport, Long>, JpaSpecificationExecutor<CultivatedLandDamageReport> {}
