package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPayoutEventListFarmer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPayoutEventListFarmer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPayoutEventListFarmerRepository
    extends JpaRepository<IndexPayoutEventListFarmer, Long>, JpaSpecificationExecutor<IndexPayoutEventListFarmer> {}
