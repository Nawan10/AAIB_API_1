package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageCategoryTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandDamageReason.class);
        CultivatedLandDamageReason cultivatedLandDamageReason1 = getCultivatedLandDamageReasonSample1();
        CultivatedLandDamageReason cultivatedLandDamageReason2 = new CultivatedLandDamageReason();
        assertThat(cultivatedLandDamageReason1).isNotEqualTo(cultivatedLandDamageReason2);

        cultivatedLandDamageReason2.setId(cultivatedLandDamageReason1.getId());
        assertThat(cultivatedLandDamageReason1).isEqualTo(cultivatedLandDamageReason2);

        cultivatedLandDamageReason2 = getCultivatedLandDamageReasonSample2();
        assertThat(cultivatedLandDamageReason1).isNotEqualTo(cultivatedLandDamageReason2);
    }

    @Test
    void damageCategoryTest() {
        CultivatedLandDamageReason cultivatedLandDamageReason = getCultivatedLandDamageReasonRandomSampleGenerator();
        CultivatedLandDamageReasonDamageCategory cultivatedLandDamageReasonDamageCategoryBack =
            getCultivatedLandDamageReasonDamageCategoryRandomSampleGenerator();

        cultivatedLandDamageReason.setDamageCategory(cultivatedLandDamageReasonDamageCategoryBack);
        assertThat(cultivatedLandDamageReason.getDamageCategory()).isEqualTo(cultivatedLandDamageReasonDamageCategoryBack);

        cultivatedLandDamageReason.damageCategory(null);
        assertThat(cultivatedLandDamageReason.getDamageCategory()).isNull();
    }

    @Test
    void damageTypeTest() {
        CultivatedLandDamageReason cultivatedLandDamageReason = getCultivatedLandDamageReasonRandomSampleGenerator();
        CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageTypeBack =
            getCultivatedLandDamageReasonDamageTypeRandomSampleGenerator();

        cultivatedLandDamageReason.setDamageType(cultivatedLandDamageReasonDamageTypeBack);
        assertThat(cultivatedLandDamageReason.getDamageType()).isEqualTo(cultivatedLandDamageReasonDamageTypeBack);

        cultivatedLandDamageReason.damageType(null);
        assertThat(cultivatedLandDamageReason.getDamageType()).isNull();
    }
}
