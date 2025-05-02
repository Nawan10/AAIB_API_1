package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCultivatedLandCropType.class);
        InsuranceCultivatedLandCropType insuranceCultivatedLandCropType1 = getInsuranceCultivatedLandCropTypeSample1();
        InsuranceCultivatedLandCropType insuranceCultivatedLandCropType2 = new InsuranceCultivatedLandCropType();
        assertThat(insuranceCultivatedLandCropType1).isNotEqualTo(insuranceCultivatedLandCropType2);

        insuranceCultivatedLandCropType2.setId(insuranceCultivatedLandCropType1.getId());
        assertThat(insuranceCultivatedLandCropType1).isEqualTo(insuranceCultivatedLandCropType2);

        insuranceCultivatedLandCropType2 = getInsuranceCultivatedLandCropTypeSample2();
        assertThat(insuranceCultivatedLandCropType1).isNotEqualTo(insuranceCultivatedLandCropType2);
    }
}
