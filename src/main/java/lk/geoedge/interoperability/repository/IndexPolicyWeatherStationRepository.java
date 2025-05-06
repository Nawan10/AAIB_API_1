package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexPolicyWeatherStation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexPolicyWeatherStation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexPolicyWeatherStationRepository
    extends JpaRepository<IndexPolicyWeatherStation, Long>, JpaSpecificationExecutor<IndexPolicyWeatherStation> {}
