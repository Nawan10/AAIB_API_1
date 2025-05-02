package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategoryTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandDamageReport.class);
        CultivatedLandDamageReport cultivatedLandDamageReport1 = getCultivatedLandDamageReportSample1();
        CultivatedLandDamageReport cultivatedLandDamageReport2 = new CultivatedLandDamageReport();
        assertThat(cultivatedLandDamageReport1).isNotEqualTo(cultivatedLandDamageReport2);

        cultivatedLandDamageReport2.setId(cultivatedLandDamageReport1.getId());
        assertThat(cultivatedLandDamageReport1).isEqualTo(cultivatedLandDamageReport2);

        cultivatedLandDamageReport2 = getCultivatedLandDamageReportSample2();
        assertThat(cultivatedLandDamageReport1).isNotEqualTo(cultivatedLandDamageReport2);
    }

    @Test
    void cropTest() {
        CultivatedLandDamageReport cultivatedLandDamageReport = getCultivatedLandDamageReportRandomSampleGenerator();
        CropType cropTypeBack = getCropTypeRandomSampleGenerator();

        cultivatedLandDamageReport.setCrop(cropTypeBack);
        assertThat(cultivatedLandDamageReport.getCrop()).isEqualTo(cropTypeBack);

        cultivatedLandDamageReport.crop(null);
        assertThat(cultivatedLandDamageReport.getCrop()).isNull();
    }

    @Test
    void damageCategoryTest() {
        CultivatedLandDamageReport cultivatedLandDamageReport = getCultivatedLandDamageReportRandomSampleGenerator();
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategoryBack =
            getCultivatedLandDamageReportDamageCategoryRandomSampleGenerator();

        cultivatedLandDamageReport.setDamageCategory(cultivatedLandDamageReportDamageCategoryBack);
        assertThat(cultivatedLandDamageReport.getDamageCategory()).isEqualTo(cultivatedLandDamageReportDamageCategoryBack);

        cultivatedLandDamageReport.damageCategory(null);
        assertThat(cultivatedLandDamageReport.getDamageCategory()).isNull();
    }

    @Test
    void damageTypeTest() {
        CultivatedLandDamageReport cultivatedLandDamageReport = getCultivatedLandDamageReportRandomSampleGenerator();
        CultivatedLandDamageReportDamageType cultivatedLandDamageReportDamageTypeBack =
            getCultivatedLandDamageReportDamageTypeRandomSampleGenerator();

        cultivatedLandDamageReport.setDamageType(cultivatedLandDamageReportDamageTypeBack);
        assertThat(cultivatedLandDamageReport.getDamageType()).isEqualTo(cultivatedLandDamageReportDamageTypeBack);

        cultivatedLandDamageReport.damageType(null);
        assertThat(cultivatedLandDamageReport.getDamageType()).isNull();
    }
}
