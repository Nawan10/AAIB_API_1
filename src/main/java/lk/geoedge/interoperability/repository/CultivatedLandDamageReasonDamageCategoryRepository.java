package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandDamageReasonDamageCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandDamageReasonDamageCategoryRepository
    extends
        JpaRepository<CultivatedLandDamageReasonDamageCategory, Long>, JpaSpecificationExecutor<CultivatedLandDamageReasonDamageCategory> {}
