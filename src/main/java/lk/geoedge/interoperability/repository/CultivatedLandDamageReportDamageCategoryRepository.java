package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandDamageReportDamageCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandDamageReportDamageCategoryRepository
    extends
        JpaRepository<CultivatedLandDamageReportDamageCategory, Long>, JpaSpecificationExecutor<CultivatedLandDamageReportDamageCategory> {}
