package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropType.class);
        CropType cropType1 = getCropTypeSample1();
        CropType cropType2 = new CropType();
        assertThat(cropType1).isNotEqualTo(cropType2);

        cropType2.setId(cropType1.getId());
        assertThat(cropType1).isEqualTo(cropType2);

        cropType2 = getCropTypeSample2();
        assertThat(cropType1).isNotEqualTo(cropType2);
    }
}
