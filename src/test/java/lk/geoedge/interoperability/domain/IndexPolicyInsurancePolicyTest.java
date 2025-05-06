package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPolicyInsurancePolicyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPolicyInsurancePolicy.class);
        IndexPolicyInsurancePolicy indexPolicyInsurancePolicy1 = getIndexPolicyInsurancePolicySample1();
        IndexPolicyInsurancePolicy indexPolicyInsurancePolicy2 = new IndexPolicyInsurancePolicy();
        assertThat(indexPolicyInsurancePolicy1).isNotEqualTo(indexPolicyInsurancePolicy2);

        indexPolicyInsurancePolicy2.setId(indexPolicyInsurancePolicy1.getId());
        assertThat(indexPolicyInsurancePolicy1).isEqualTo(indexPolicyInsurancePolicy2);

        indexPolicyInsurancePolicy2 = getIndexPolicyInsurancePolicySample2();
        assertThat(indexPolicyInsurancePolicy1).isNotEqualTo(indexPolicyInsurancePolicy2);
    }
}
