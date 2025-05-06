package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCultivatedLandCoveragesInsuranceCultivatedLand.class);
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand1 =
            getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandSample1();
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand2 =
            new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand();
        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLand1).isNotEqualTo(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand2
        );

        insuranceCultivatedLandCoveragesInsuranceCultivatedLand2.setId(insuranceCultivatedLandCoveragesInsuranceCultivatedLand1.getId());
        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLand1).isEqualTo(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand2
        );

        insuranceCultivatedLandCoveragesInsuranceCultivatedLand2 = getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandSample2();
        assertThat(insuranceCultivatedLandCoveragesInsuranceCultivatedLand1).isNotEqualTo(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLand2
        );
    }
}
