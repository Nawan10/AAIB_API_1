package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPolicyInsurancePolicy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPolicyInsurancePolicyRepository
    extends JpaRepository<IndexPolicyInsurancePolicy, Long>, JpaSpecificationExecutor<IndexPolicyInsurancePolicy> {}
