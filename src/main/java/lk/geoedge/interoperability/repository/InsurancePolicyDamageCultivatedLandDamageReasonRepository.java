package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsurancePolicyDamageCultivatedLandDamageReason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsurancePolicyDamageCultivatedLandDamageReasonRepository
    extends
        JpaRepository<InsurancePolicyDamageCultivatedLandDamageReason, Long>,
        JpaSpecificationExecutor<InsurancePolicyDamageCultivatedLandDamageReason> {}
