package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.CultivatedLandEntityTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandFarmerFieldOwnerTestSamples.*;
import static lk.geoedge.interoperability.domain.CultivatedLandSeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CultivatedLandEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CultivatedLandEntity.class);
        CultivatedLandEntity cultivatedLandEntity1 = getCultivatedLandEntitySample1();
        CultivatedLandEntity cultivatedLandEntity2 = new CultivatedLandEntity();
        assertThat(cultivatedLandEntity1).isNotEqualTo(cultivatedLandEntity2);

        cultivatedLandEntity2.setId(cultivatedLandEntity1.getId());
        assertThat(cultivatedLandEntity1).isEqualTo(cultivatedLandEntity2);

        cultivatedLandEntity2 = getCultivatedLandEntitySample2();
        assertThat(cultivatedLandEntity1).isNotEqualTo(cultivatedLandEntity2);
    }

    @Test
    void farmFieldTest() {
        CultivatedLandEntity cultivatedLandEntity = getCultivatedLandEntityRandomSampleGenerator();
        CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwnerBack = getCultivatedLandFarmerFieldOwnerRandomSampleGenerator();

        cultivatedLandEntity.setFarmField(cultivatedLandFarmerFieldOwnerBack);
        assertThat(cultivatedLandEntity.getFarmField()).isEqualTo(cultivatedLandFarmerFieldOwnerBack);

        cultivatedLandEntity.farmField(null);
        assertThat(cultivatedLandEntity.getFarmField()).isNull();
    }

    @Test
    void seasonTest() {
        CultivatedLandEntity cultivatedLandEntity = getCultivatedLandEntityRandomSampleGenerator();
        CultivatedLandSeason cultivatedLandSeasonBack = getCultivatedLandSeasonRandomSampleGenerator();

        cultivatedLandEntity.setSeason(cultivatedLandSeasonBack);
        assertThat(cultivatedLandEntity.getSeason()).isEqualTo(cultivatedLandSeasonBack);

        cultivatedLandEntity.season(null);
        assertThat(cultivatedLandEntity.getSeason()).isNull();
    }
}
