package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexCoveragesTestSamples.*;
import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesInsuranceCultivatedLandTestSamples.*;
import static lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoveragesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsuranceCultivatedLandCoveragesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsuranceCultivatedLandCoverages.class);
        InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages1 = getInsuranceCultivatedLandCoveragesSample1();
        InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages2 = new InsuranceCultivatedLandCoverages();
        assertThat(insuranceCultivatedLandCoverages1).isNotEqualTo(insuranceCultivatedLandCoverages2);

        insuranceCultivatedLandCoverages2.setId(insuranceCultivatedLandCoverages1.getId());
        assertThat(insuranceCultivatedLandCoverages1).isEqualTo(insuranceCultivatedLandCoverages2);

        insuranceCultivatedLandCoverages2 = getInsuranceCultivatedLandCoveragesSample2();
        assertThat(insuranceCultivatedLandCoverages1).isNotEqualTo(insuranceCultivatedLandCoverages2);
    }

    @Test
    void insuranceCultivatedLandTest() {
        InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages = getInsuranceCultivatedLandCoveragesRandomSampleGenerator();
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLandBack =
            getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandRandomSampleGenerator();

        insuranceCultivatedLandCoverages.setInsuranceCultivatedLand(insuranceCultivatedLandCoveragesInsuranceCultivatedLandBack);
        assertThat(insuranceCultivatedLandCoverages.getInsuranceCultivatedLand()).isEqualTo(
            insuranceCultivatedLandCoveragesInsuranceCultivatedLandBack
        );

        insuranceCultivatedLandCoverages.insuranceCultivatedLand(null);
        assertThat(insuranceCultivatedLandCoverages.getInsuranceCultivatedLand()).isNull();
    }

    @Test
    void indexCoverageTest() {
        InsuranceCultivatedLandCoverages insuranceCultivatedLandCoverages = getInsuranceCultivatedLandCoveragesRandomSampleGenerator();
        IndexCoverages indexCoveragesBack = getIndexCoveragesRandomSampleGenerator();

        insuranceCultivatedLandCoverages.setIndexCoverage(indexCoveragesBack);
        assertThat(insuranceCultivatedLandCoverages.getIndexCoverage()).isEqualTo(indexCoveragesBack);

        insuranceCultivatedLandCoverages.indexCoverage(null);
        assertThat(insuranceCultivatedLandCoverages.getIndexCoverage()).isNull();
    }
}
