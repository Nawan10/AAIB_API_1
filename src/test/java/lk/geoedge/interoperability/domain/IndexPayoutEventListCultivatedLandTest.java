package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLandTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListCultivatedLandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPayoutEventListCultivatedLand.class);
        IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand1 = getIndexPayoutEventListCultivatedLandSample1();
        IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLand2 = new IndexPayoutEventListCultivatedLand();
        assertThat(indexPayoutEventListCultivatedLand1).isNotEqualTo(indexPayoutEventListCultivatedLand2);

        indexPayoutEventListCultivatedLand2.setId(indexPayoutEventListCultivatedLand1.getId());
        assertThat(indexPayoutEventListCultivatedLand1).isEqualTo(indexPayoutEventListCultivatedLand2);

        indexPayoutEventListCultivatedLand2 = getIndexPayoutEventListCultivatedLandSample2();
        assertThat(indexPayoutEventListCultivatedLand1).isNotEqualTo(indexPayoutEventListCultivatedLand2);
    }
}
