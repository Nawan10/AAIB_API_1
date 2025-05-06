package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPolicySeason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPolicySeason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPolicySeasonRepository extends JpaRepository<IndexPolicySeason, Long>, JpaSpecificationExecutor<IndexPolicySeason> {}
