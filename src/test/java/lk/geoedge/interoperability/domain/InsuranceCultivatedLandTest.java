package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandTestSamples.*;
import static lk.geoedge.interoperability.domain.FarmerTestSamples.*;
import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCultivatedLand.class);
        InsuranceCultivatedLand insuranceCultivatedLand1 = getInsuranceCultivatedLandSample1();
        InsuranceCultivatedLand insuranceCultivatedLand2 = new InsuranceCultivatedLand();
        assertThat(insuranceCultivatedLand1).isNotEqualTo(insuranceCultivatedLand2);

        insuranceCultivatedLand2.setId(insuranceCultivatedLand1.getId());
        assertThat(insuranceCultivatedLand1).isEqualTo(insuranceCultivatedLand2);

        insuranceCultivatedLand2 = getInsuranceCultivatedLandSample2();
        assertThat(insuranceCultivatedLand1).isNotEqualTo(insuranceCultivatedLand2);
    }

    @Test
    void farmerTest() {
        InsuranceCultivatedLand insuranceCultivatedLand = getInsuranceCultivatedLandRandomSampleGenerator();
        Farmer farmerBack = getFarmerRandomSampleGenerator();

        insuranceCultivatedLand.setFarmer(farmerBack);
        assertThat(insuranceCultivatedLand.getFarmer()).isEqualTo(farmerBack);

        insuranceCultivatedLand.farmer(null);
        assertThat(insuranceCultivatedLand.getFarmer()).isNull();
    }

    @Test
    void cultivatedLandTest() {
        InsuranceCultivatedLand insuranceCultivatedLand = getInsuranceCultivatedLandRandomSampleGenerator();
        CultivatedLand cultivatedLandBack = getCultivatedLandRandomSampleGenerator();

        insuranceCultivatedLand.setCultivatedLand(cultivatedLandBack);
        assertThat(insuranceCultivatedLand.getCultivatedLand()).isEqualTo(cultivatedLandBack);

        insuranceCultivatedLand.cultivatedLand(null);
        assertThat(insuranceCultivatedLand.getCultivatedLand()).isNull();
    }

    @Test
    void cropTest() {
        InsuranceCultivatedLand insuranceCultivatedLand = getInsuranceCultivatedLandRandomSampleGenerator();
        InsuranceCultivatedLandCropType insuranceCultivatedLandCropTypeBack = getInsuranceCultivatedLandCropTypeRandomSampleGenerator();

        insuranceCultivatedLand.setCrop(insuranceCultivatedLandCropTypeBack);
        assertThat(insuranceCultivatedLand.getCrop()).isEqualTo(insuranceCultivatedLandCropTypeBack);

        insuranceCultivatedLand.crop(null);
        assertThat(insuranceCultivatedLand.getCrop()).isNull();
    }
}
