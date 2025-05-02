package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReportDamageCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReportDamageCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandDamageReportDamageCategory.class);
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory1 =
            getCultivatedLandDamageReportDamageCategorySample1();
        CultivatedLandDamageReportDamageCategory cultivatedLandDamageReportDamageCategory2 = new CultivatedLandDamageReportDamageCategory();
        assertThat(cultivatedLandDamageReportDamageCategory1).isNotEqualTo(cultivatedLandDamageReportDamageCategory2);

        cultivatedLandDamageReportDamageCategory2.setId(cultivatedLandDamageReportDamageCategory1.getId());
        assertThat(cultivatedLandDamageReportDamageCategory1).isEqualTo(cultivatedLandDamageReportDamageCategory2);

        cultivatedLandDamageReportDamageCategory2 = getCultivatedLandDamageReportDamageCategorySample2();
        assertThat(cultivatedLandDamageReportDamageCategory1).isNotEqualTo(cultivatedLandDamageReportDamageCategory2);
    }
}
