package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.InsurancePolicy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsurancePolicy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long>, JpaSpecificationExecutor<InsurancePolicy> {}
