package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexCoveragesCultivatedLandDamageReasonTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexCoveragesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexCoveragesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexCoverages.class);
        IndexCoverages indexCoverages1 = getIndexCoveragesSample1();
        IndexCoverages indexCoverages2 = new IndexCoverages();
        assertThat(indexCoverages1).isNotEqualTo(indexCoverages2);

        indexCoverages2.setId(indexCoverages1.getId());
        assertThat(indexCoverages1).isEqualTo(indexCoverages2);

        indexCoverages2 = getIndexCoveragesSample2();
        assertThat(indexCoverages1).isNotEqualTo(indexCoverages2);
    }

    @Test
    void damageReasonTest() {
        IndexCoverages indexCoverages = getIndexCoveragesRandomSampleGenerator();
        IndexCoveragesCultivatedLandDamageReason indexCoveragesCultivatedLandDamageReasonBack =
            getIndexCoveragesCultivatedLandDamageReasonRandomSampleGenerator();

        indexCoverages.setDamageReason(indexCoveragesCultivatedLandDamageReasonBack);
        assertThat(indexCoverages.getDamageReason()).isEqualTo(indexCoveragesCultivatedLandDamageReasonBack);

        indexCoverages.damageReason(null);
        assertThat(indexCoverages.getDamageReason()).isNull();
    }
}
