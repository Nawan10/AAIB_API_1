package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPolicyCropTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPolicyCropTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPolicyCropType.class);
        IndexPolicyCropType indexPolicyCropType1 = getIndexPolicyCropTypeSample1();
        IndexPolicyCropType indexPolicyCropType2 = new IndexPolicyCropType();
        assertThat(indexPolicyCropType1).isNotEqualTo(indexPolicyCropType2);

        indexPolicyCropType2.setId(indexPolicyCropType1.getId());
        assertThat(indexPolicyCropType1).isEqualTo(indexPolicyCropType2);

        indexPolicyCropType2 = getIndexPolicyCropTypeSample2();
        assertThat(indexPolicyCropType1).isNotEqualTo(indexPolicyCropType2);
    }
}
