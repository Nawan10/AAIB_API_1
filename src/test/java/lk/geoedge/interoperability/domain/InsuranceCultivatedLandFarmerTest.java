package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandFarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandFarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCultivatedLandFarmer.class);
        InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer1 = getInsuranceCultivatedLandFarmerSample1();
        InsuranceCultivatedLandFarmer insuranceCultivatedLandFarmer2 = new InsuranceCultivatedLandFarmer();
        assertThat(insuranceCultivatedLandFarmer1).isNotEqualTo(insuranceCultivatedLandFarmer2);

        insuranceCultivatedLandFarmer2.setId(insuranceCultivatedLandFarmer1.getId());
        assertThat(insuranceCultivatedLandFarmer1).isEqualTo(insuranceCultivatedLandFarmer2);

        insuranceCultivatedLandFarmer2 = getInsuranceCultivatedLandFarmerSample2();
        assertThat(insuranceCultivatedLandFarmer1).isNotEqualTo(insuranceCultivatedLandFarmer2);
    }
}
