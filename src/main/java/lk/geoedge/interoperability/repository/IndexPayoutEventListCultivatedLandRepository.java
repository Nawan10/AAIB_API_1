package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPayoutEventListCultivatedLand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPayoutEventListCultivatedLandRepository
    extends JpaRepository<IndexPayoutEventListCultivatedLand, Long>, JpaSpecificationExecutor<IndexPayoutEventListCultivatedLand> {}
