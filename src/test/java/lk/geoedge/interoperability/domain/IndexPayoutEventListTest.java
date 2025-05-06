package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPayoutEventListCultivatedLandTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPayoutEventListFarmerTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPayoutEventListSeasonTestSamples.*;
import static lk.geoedge.interoperability.domain.IndexPayoutEventListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPayoutEventListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPayoutEventList.class);
        IndexPayoutEventList indexPayoutEventList1 = getIndexPayoutEventListSample1();
        IndexPayoutEventList indexPayoutEventList2 = new IndexPayoutEventList();
        assertThat(indexPayoutEventList1).isNotEqualTo(indexPayoutEventList2);

        indexPayoutEventList2.setId(indexPayoutEventList1.getId());
        assertThat(indexPayoutEventList1).isEqualTo(indexPayoutEventList2);

        indexPayoutEventList2 = getIndexPayoutEventListSample2();
        assertThat(indexPayoutEventList1).isNotEqualTo(indexPayoutEventList2);
    }

    @Test
    void cultivatedFarmerTest() {
        IndexPayoutEventList indexPayoutEventList = getIndexPayoutEventListRandomSampleGenerator();
        IndexPayoutEventListFarmer indexPayoutEventListFarmerBack = getIndexPayoutEventListFarmerRandomSampleGenerator();

        indexPayoutEventList.setCultivatedFarmer(indexPayoutEventListFarmerBack);
        assertThat(indexPayoutEventList.getCultivatedFarmer()).isEqualTo(indexPayoutEventListFarmerBack);

        indexPayoutEventList.cultivatedFarmer(null);
        assertThat(indexPayoutEventList.getCultivatedFarmer()).isNull();
    }

    @Test
    void cultivatedLandTest() {
        IndexPayoutEventList indexPayoutEventList = getIndexPayoutEventListRandomSampleGenerator();
        IndexPayoutEventListCultivatedLand indexPayoutEventListCultivatedLandBack =
            getIndexPayoutEventListCultivatedLandRandomSampleGenerator();

        indexPayoutEventList.setCultivatedLand(indexPayoutEventListCultivatedLandBack);
        assertThat(indexPayoutEventList.getCultivatedLand()).isEqualTo(indexPayoutEventListCultivatedLandBack);

        indexPayoutEventList.cultivatedLand(null);
        assertThat(indexPayoutEventList.getCultivatedLand()).isNull();
    }

    @Test
    void seasonTest() {
        IndexPayoutEventList indexPayoutEventList = getIndexPayoutEventListRandomSampleGenerator();
        IndexPayoutEventListSeason indexPayoutEventListSeasonBack = getIndexPayoutEventListSeasonRandomSampleGenerator();

        indexPayoutEventList.setSeason(indexPayoutEventListSeasonBack);
        assertThat(indexPayoutEventList.getSeason()).isEqualTo(indexPayoutEventListSeasonBack);

        indexPayoutEventList.season(null);
        assertThat(indexPayoutEventList.getSeason()).isNull();
    }
}
