package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropDurationCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropDurationCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropDurationCropType.class);
        CropDurationCropType cropDurationCropType1 = getCropDurationCropTypeSample1();
        CropDurationCropType cropDurationCropType2 = new CropDurationCropType();
        assertThat(cropDurationCropType1).isNotEqualTo(cropDurationCropType2);

        cropDurationCropType2.setId(cropDurationCropType1.getId());
        assertThat(cropDurationCropType1).isEqualTo(cropDurationCropType2);

        cropDurationCropType2 = getCropDurationCropTypeSample2();
        assertThat(cropDurationCropType1).isNotEqualTo(cropDurationCropType2);
    }
}
