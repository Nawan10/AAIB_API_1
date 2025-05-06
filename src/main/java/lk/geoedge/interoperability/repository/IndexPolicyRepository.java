package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPolicy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPolicy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPolicyRepository extends JpaRepository<IndexPolicy, Long>, JpaSpecificationExecutor<IndexPolicy> {}
