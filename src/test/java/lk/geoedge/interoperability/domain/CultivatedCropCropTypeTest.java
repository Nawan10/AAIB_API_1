package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedCropCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedCropCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedCropCropType.class);
        CultivatedCropCropType cultivatedCropCropType1 = getCultivatedCropCropTypeSample1();
        CultivatedCropCropType cultivatedCropCropType2 = new CultivatedCropCropType();
        assertThat(cultivatedCropCropType1).isNotEqualTo(cultivatedCropCropType2);

        cultivatedCropCropType2.setId(cultivatedCropCropType1.getId());
        assertThat(cultivatedCropCropType1).isEqualTo(cultivatedCropCropType2);

        cultivatedCropCropType2 = getCultivatedCropCropTypeSample2();
        assertThat(cultivatedCropCropType1).isNotEqualTo(cultivatedCropCropType2);
    }
}
