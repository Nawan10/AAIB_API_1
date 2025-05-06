package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CropVarietyCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropVarietyCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropVarietyCropType.class);
        CropVarietyCropType cropVarietyCropType1 = getCropVarietyCropTypeSample1();
        CropVarietyCropType cropVarietyCropType2 = new CropVarietyCropType();
        assertThat(cropVarietyCropType1).isNotEqualTo(cropVarietyCropType2);

        cropVarietyCropType2.setId(cropVarietyCropType1.getId());
        assertThat(cropVarietyCropType1).isEqualTo(cropVarietyCropType2);

        cropVarietyCropType2 = getCropVarietyCropTypeSample2();
        assertThat(cropVarietyCropType1).isNotEqualTo(cropVarietyCropType2);
    }
}
