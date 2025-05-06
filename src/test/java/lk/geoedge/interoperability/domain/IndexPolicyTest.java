package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPolicyCropTypeTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPolicyCropVarietyTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicyTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPolicySeasonTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPolicyTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPolicyWeatherStationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPolicy.class);
        IndexPolicy indexPolicy1 = getIndexPolicySample1();
        IndexPolicy indexPolicy2 = new IndexPolicy();
        assertThat(indexPolicy1).isNotEqualTo(indexPolicy2);

        indexPolicy2.setId(indexPolicy1.getId());
        assertThat(indexPolicy1).isEqualTo(indexPolicy2);

        indexPolicy2 = getIndexPolicySample2();
        assertThat(indexPolicy1).isNotEqualTo(indexPolicy2);
    }

    @Test
    void policyTest() {
        IndexPolicy indexPolicy = getIndexPolicyRandomSampleGenerator();
        IndexPolicyInsurancePolicy indexPolicyInsurancePolicyBack = getIndexPolicyInsurancePolicyRandomSampleGenerator();

        indexPolicy.setPolicy(indexPolicyInsurancePolicyBack);
        assertThat(indexPolicy.getPolicy()).isEqualTo(indexPolicyInsurancePolicyBack);

        indexPolicy.policy(null);
        assertThat(indexPolicy.getPolicy()).isNull();
    }

    @Test
    void seasonTest() {
        IndexPolicy indexPolicy = getIndexPolicyRandomSampleGenerator();
        IndexPolicySeason indexPolicySeasonBack = getIndexPolicySeasonRandomSampleGenerator();

        indexPolicy.setSeason(indexPolicySeasonBack);
        assertThat(indexPolicy.getSeason()).isEqualTo(indexPolicySeasonBack);

        indexPolicy.season(null);
        assertThat(indexPolicy.getSeason()).isNull();
    }

    @Test
    void cropVarietyTest() {
        IndexPolicy indexPolicy = getIndexPolicyRandomSampleGenerator();
        IndexPolicyCropVariety indexPolicyCropVarietyBack = getIndexPolicyCropVarietyRandomSampleGenerator();

        indexPolicy.setCropVariety(indexPolicyCropVarietyBack);
        assertThat(indexPolicy.getCropVariety()).isEqualTo(indexPolicyCropVarietyBack);

        indexPolicy.cropVariety(null);
        assertThat(indexPolicy.getCropVariety()).isNull();
    }

    @Test
    void cropTest() {
        IndexPolicy indexPolicy = getIndexPolicyRandomSampleGenerator();
        IndexPolicyCropType indexPolicyCropTypeBack = getIndexPolicyCropTypeRandomSampleGenerator();

        indexPolicy.setCrop(indexPolicyCropTypeBack);
        assertThat(indexPolicy.getCrop()).isEqualTo(indexPolicyCropTypeBack);

        indexPolicy.crop(null);
        assertThat(indexPolicy.getCrop()).isNull();
    }

    @Test
    void weatherStationTest() {
        IndexPolicy indexPolicy = getIndexPolicyRandomSampleGenerator();
        IndexPolicyWeatherStation indexPolicyWeatherStationBack = getIndexPolicyWeatherStationRandomSampleGenerator();

        indexPolicy.setWeatherStation(indexPolicyWeatherStationBack);
        assertThat(indexPolicy.getWeatherStation()).isEqualTo(indexPolicyWeatherStationBack);

        indexPolicy.weatherStation(null);
        assertThat(indexPolicy.getWeatherStation()).isNull();
    }
}
