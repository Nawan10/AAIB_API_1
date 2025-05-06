package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandDamageReasonDamageType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandDamageReasonDamageTypeRepository
    extends JpaRepository<CultivatedLandDamageReasonDamageType, Long>, JpaSpecificationExecutor<CultivatedLandDamageReasonDamageType> {}
