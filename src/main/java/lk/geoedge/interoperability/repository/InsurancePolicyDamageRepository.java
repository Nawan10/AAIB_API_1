package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsurancePolicyDamage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsurancePolicyDamage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsurancePolicyDamageRepository
    extends JpaRepository<InsurancePolicyDamage, Long>, JpaSpecificationExecutor<InsurancePolicyDamage> {}
