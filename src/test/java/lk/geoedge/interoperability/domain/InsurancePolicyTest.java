package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.InsurancePolicyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InsurancePolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsurancePolicy.class);
        InsurancePolicy insurancePolicy1 = getInsurancePolicySample1();
        InsurancePolicy insurancePolicy2 = new InsurancePolicy();
        assertThat(insurancePolicy1).isNotEqualTo(insurancePolicy2);

        insurancePolicy2.setId(insurancePolicy1.getId());
        assertThat(insurancePolicy1).isEqualTo(insurancePolicy2);

        insurancePolicy2 = getInsurancePolicySample2();
        assertThat(insurancePolicy1).isNotEqualTo(insurancePolicy2);
    }
}
