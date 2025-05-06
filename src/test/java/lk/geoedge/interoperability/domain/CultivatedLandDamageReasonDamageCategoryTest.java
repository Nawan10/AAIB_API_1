package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReasonDamageCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandDamageReasonDamageCategory.class);
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory1 =
            getCultivatedLandDamageReasonDamageCategorySample1();
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategory2 = new CultivatedLandDamageReasonDamageCategory();
        assertThat(cultivatedLandDamageReasonDamageCategory1).isNotEqualTo(cultivatedLandDamageReasonDamageCategory2);

        cultivatedLandDamageReasonDamageCategory2.setId(cultivatedLandDamageReasonDamageCategory1.getId());
        assertThat(cultivatedLandDamageReasonDamageCategory1).isEqualTo(cultivatedLandDamageReasonDamageCategory2);

        cultivatedLandDamageReasonDamageCategory2 = getCultivatedLandDamageReasonDamageCategorySample2();
        assertThat(cultivatedLandDamageReasonDamageCategory1).isNotEqualTo(cultivatedLandDamageReasonDamageCategory2);
    }
}
