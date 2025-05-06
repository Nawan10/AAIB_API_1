package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPayoutEventList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPayoutEventList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPayoutEventListRepository
    extends JpaRepository<IndexPayoutEventList, Long>, JpaSpecificationExecutor<IndexPayoutEventList> {}
