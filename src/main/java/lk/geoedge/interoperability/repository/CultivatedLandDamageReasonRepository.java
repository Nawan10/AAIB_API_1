package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.CultivatedLandDamageReason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CultivatedLandDamageReason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CultivatedLandDamageReasonRepository
    extends JpaRepository<CultivatedLandDamageReason, Long>, JpaSpecificationExecutor<CultivatedLandDamageReason> {}
