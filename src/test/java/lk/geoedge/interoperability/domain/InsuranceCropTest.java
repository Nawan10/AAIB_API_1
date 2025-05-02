package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsuranceCropCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.InsuranceCropTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCropTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCrop.class);
        InsuranceCrop insuranceCrop1 = getInsuranceCropSample1();
        InsuranceCrop insuranceCrop2 = new InsuranceCrop();
        assertThat(insuranceCrop1).isNotEqualTo(insuranceCrop2);

        insuranceCrop2.setId(insuranceCrop1.getId());
        assertThat(insuranceCrop1).isEqualTo(insuranceCrop2);

        insuranceCrop2 = getInsuranceCropSample2();
        assertThat(insuranceCrop1).isNotEqualTo(insuranceCrop2);
    }

    @Test
    void cropTest() {
        InsuranceCrop insuranceCrop = getInsuranceCropRandomSampleGenerator();
        InsuranceCropCropType insuranceCropCropTypeBack = getInsuranceCropCropTypeRandomSampleGenerator();

        insuranceCrop.setCrop(insuranceCropCropTypeBack);
        assertThat(insuranceCrop.getCrop()).isEqualTo(insuranceCropCropTypeBack);

        insuranceCrop.crop(null);
        assertThat(insuranceCrop.getCrop()).isNull();
    }
}
