package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPolicyCropVarietyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPolicyCropVarietyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPolicyCropVariety.class);
        IndexPolicyCropVariety indexPolicyCropVariety1 = getIndexPolicyCropVarietySample1();
        IndexPolicyCropVariety indexPolicyCropVariety2 = new IndexPolicyCropVariety();
        assertThat(indexPolicyCropVariety1).isNotEqualTo(indexPolicyCropVariety2);

        indexPolicyCropVariety2.setId(indexPolicyCropVariety1.getId());
        assertThat(indexPolicyCropVariety1).isEqualTo(indexPolicyCropVariety2);

        indexPolicyCropVariety2 = getIndexPolicyCropVarietySample2();
        assertThat(indexPolicyCropVariety1).isNotEqualTo(indexPolicyCropVariety2);
    }
}
