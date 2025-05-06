package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListSeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListSeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPayoutEventListSeason.class);
        IndexPayoutEventListSeason indexPayoutEventListSeason1 = getIndexPayoutEventListSeasonSample1();
        IndexPayoutEventListSeason indexPayoutEventListSeason2 = new IndexPayoutEventListSeason();
        assertThat(indexPayoutEventListSeason1).isNotEqualTo(indexPayoutEventListSeason2);

        indexPayoutEventListSeason2.setId(indexPayoutEventListSeason1.getId());
        assertThat(indexPayoutEventListSeason1).isEqualTo(indexPayoutEventListSeason2);

        indexPayoutEventListSeason2 = getIndexPayoutEventListSeasonSample2();
        assertThat(indexPayoutEventListSeason1).isNotEqualTo(indexPayoutEventListSeason2);
    }
}
