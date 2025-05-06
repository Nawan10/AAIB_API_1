package lk.geoedge.interoperability.repository;

import lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndexCoveragesCultivatedLandDamageReason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexCoveragesCultivatedLandDamageReasonRepository
    extends
        JpaRepository<IndexCoveragesCultivatedLandDamageReason, Long>, JpaSpecificationExecutor<IndexCoveragesCultivatedLandDamageReason> {}
