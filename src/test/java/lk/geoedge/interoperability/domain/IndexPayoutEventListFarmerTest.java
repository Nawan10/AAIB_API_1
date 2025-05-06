package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListFarmerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListFarmerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPayoutEventListFarmer.class);
        IndexPayoutEventListFarmer indexPayoutEventListFarmer1 = getIndexPayoutEventListFarmerSample1();
        IndexPayoutEventListFarmer indexPayoutEventListFarmer2 = new IndexPayoutEventListFarmer();
        assertThat(indexPayoutEventListFarmer1).isNotEqualTo(indexPayoutEventListFarmer2);

        indexPayoutEventListFarmer2.setId(indexPayoutEventListFarmer1.getId());
        assertThat(indexPayoutEventListFarmer1).isEqualTo(indexPayoutEventListFarmer2);

        indexPayoutEventListFarmer2 = getIndexPayoutEventListFarmerSample2();
        assertThat(indexPayoutEventListFarmer1).isNotEqualTo(indexPayoutEventListFarmer2);
    }
}
