package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandDamageReasonDamageTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandDamageReasonDamageTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandDamageReasonDamageType.class);
        CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType1 = getCultivatedLandDamageReasonDamageTypeSample1();
        CultivatedLandDamageReasonDamageType cultivatedLandDamageReasonDamageType2 = new CultivatedLandDamageReasonDamageType();
        assertThat(cultivatedLandDamageReasonDamageType1).isNotEqualTo(cultivatedLandDamageReasonDamageType2);

        cultivatedLandDamageReasonDamageType2.setId(cultivatedLandDamageReasonDamageType1.getId());
        assertThat(cultivatedLandDamageReasonDamageType1).isEqualTo(cultivatedLandDamageReasonDamageType2);

        cultivatedLandDamageReasonDamageType2 = getCultivatedLandDamageReasonDamageTypeSample2();
        assertThat(cultivatedLandDamageReasonDamageType1).isNotEqualTo(cultivatedLandDamageReasonDamageType2);
    }
}
