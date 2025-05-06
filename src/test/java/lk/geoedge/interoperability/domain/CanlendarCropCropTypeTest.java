package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CanlendarCropCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanlendarCropCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanlendarCropCropType.class);
        CanlendarCropCropType canlendarCropCropType1 = getCanlendarCropCropTypeSample1();
        CanlendarCropCropType canlendarCropCropType2 = new CanlendarCropCropType();
        assertThat(canlendarCropCropType1).isNotEqualTo(canlendarCropCropType2);

        canlendarCropCropType2.setId(canlendarCropCropType1.getId());
        assertThat(canlendarCropCropType1).isEqualTo(canlendarCropCropType2);

        canlendarCropCropType2 = getCanlendarCropCropTypeSample2();
        assertThat(canlendarCropCropType1).isNotEqualTo(canlendarCropCropType2);
    }
}
