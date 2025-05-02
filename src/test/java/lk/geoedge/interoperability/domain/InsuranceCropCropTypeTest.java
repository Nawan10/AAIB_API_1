package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsuranceCropCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCropCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCropCropType.class);
        InsuranceCropCropType insuranceCropCropType1 = getInsuranceCropCropTypeSample1();
        InsuranceCropCropType insuranceCropCropType2 = new InsuranceCropCropType();
        assertThat(insuranceCropCropType1).isNotEqualTo(insuranceCropCropType2);

        insuranceCropCropType2.setId(insuranceCropCropType1.getId());
        assertThat(insuranceCropCropType1).isEqualTo(insuranceCropCropType2);

        insuranceCropCropType2 = getInsuranceCropCropTypeSample2();
        assertThat(insuranceCropCropType1).isNotEqualTo(insuranceCropCropType2);
    }
}
