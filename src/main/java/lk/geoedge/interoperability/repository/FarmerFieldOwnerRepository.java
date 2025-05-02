package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.FarmerFieldOwner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FarmerFieldOwner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FarmerFieldOwnerRepository extends JpaRepository<FarmerFieldOwner, Long>, JpaSpecificationExecutor<FarmerFieldOwner> {}
