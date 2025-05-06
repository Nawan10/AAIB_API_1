package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCultivatedLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCultivatedLandCultivatedLand.class);
        InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand1 = getInsuranceCultivatedLandCultivatedLandSample1();
        InsuranceCultivatedLandCultivatedLand insuranceCultivatedLandCultivatedLand2 = new InsuranceCultivatedLandCultivatedLand();
        assertThat(insuranceCultivatedLandCultivatedLand1).isNotEqualTo(insuranceCultivatedLandCultivatedLand2);

        insuranceCultivatedLandCultivatedLand2.setId(insuranceCultivatedLandCultivatedLand1.getId());
        assertThat(insuranceCultivatedLandCultivatedLand1).isEqualTo(insuranceCultivatedLandCultivatedLand2);

        insuranceCultivatedLandCultivatedLand2 = getInsuranceCultivatedLandCultivatedLandSample2();
        assertThat(insuranceCultivatedLandCultivatedLand1).isNotEqualTo(insuranceCultivatedLandCultivatedLand2);
    }
}
