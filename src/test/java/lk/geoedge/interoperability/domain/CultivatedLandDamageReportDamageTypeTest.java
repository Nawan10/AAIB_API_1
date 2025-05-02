package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReportDamageTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandDamageReportDamageType.class);
        CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType1 = getCultivatedLandDamageReportDamageTypeSample1();
        CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageType2 = new CultivatedLandDamageReportDamageType();
        assertThat(cultivatedLandDamageReportDamageType1).isNotEqualTo(cultivatedLandDamageReportDamageType2);

        cultivatedLandDamageReportDamageType2.setId(cultivatedLandDamageReportDamageType1.getId());
        assertThat(cultivatedLandDamageReportDamageType1).isEqualTo(cultivatedLandDamageReportDamageType2);

        cultivatedLandDamageReportDamageType2 = getCultivatedLandDamageReportDamageTypeSample2();
        assertThat(cultivatedLandDamageReportDamageType1).isNotEqualTo(cultivatedLandDamageReportDamageType2);
    }
}
