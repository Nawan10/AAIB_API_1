package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPolicySeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPolicySeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPolicySeason.class);
        IndexPolicySeason indexPolicySeason1 = getIndexPolicySeasonSample1();
        IndexPolicySeason indexPolicySeason2 = new IndexPolicySeason();
        assertThat(indexPolicySeason1).isNotEqualTo(indexPolicySeason2);

        indexPolicySeason2.setId(indexPolicySeason1.getId());
        assertThat(indexPolicySeason1).isEqualTo(indexPolicySeason2);

        indexPolicySeason2 = getIndexPolicySeasonSample2();
        assertThat(indexPolicySeason1).isNotEqualTo(indexPolicySeason2);
    }
}
