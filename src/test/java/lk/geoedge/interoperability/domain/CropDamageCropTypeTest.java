package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropDamageCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropDamageCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropDamageCropType.class);
        CropDamageCropType cropDamageCropType1 = getCropDamageCropTypeSample1();
        CropDamageCropType cropDamageCropType2 = new CropDamageCropType();
        assertThat(cropDamageCropType1).isNotEqualTo(cropDamageCropType2);

        cropDamageCropType2.setId(cropDamageCropType1.getId());
        assertThat(cropDamageCropType1).isEqualTo(cropDamageCropType2);

        cropDamageCropType2 = getCropDamageCropTypeSample2();
        assertThat(cropDamageCropType1).isNotEqualTo(cropDamageCropType2);
    }
}
