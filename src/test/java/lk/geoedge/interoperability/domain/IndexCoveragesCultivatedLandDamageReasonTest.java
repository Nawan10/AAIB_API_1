package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexCoveragesCultivatedLandDamageReasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexCoveragesCultivatedLandDamageReason.class);
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason1 =
            getIndexCoveragesCultivatedLandDamageReasonSample1();
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReason2 = new IndexCoveragesCultivatedLandDamageReason();
        assertThat(indexCoveragesCultivatedLandDamageReason1).isNotEqualTo(indexCoveragesCultivatedLandDamageReason2);

        indexCoveragesCultivatedLandDamageReason2.setId(indexCoveragesCultivatedLandDamageReason1.getId());
        assertThat(indexCoveragesCultivatedLandDamageReason1).isEqualTo(indexCoveragesCultivatedLandDamageReason2);

        indexCoveragesCultivatedLandDamageReason2 = getIndexCoveragesCultivatedLandDamageReasonSample2();
        assertThat(indexCoveragesCultivatedLandDamageReason1).isNotEqualTo(indexCoveragesCultivatedLandDamageReason2);
    }
}
