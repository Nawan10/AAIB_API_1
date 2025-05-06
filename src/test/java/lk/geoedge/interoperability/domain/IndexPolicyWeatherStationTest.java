package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.IndexPolicyWeatherStationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndexPolicyWeatherStationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexPolicyWeatherStation.class);
        IndexPolicyWeatherStation indexPolicyWeatherStation1 = getIndexPolicyWeatherStationSample1();
        IndexPolicyWeatherStation indexPolicyWeatherStation2 = new IndexPolicyWeatherStation();
        assertThat(indexPolicyWeatherStation1).isNotEqualTo(indexPolicyWeatherStation2);

        indexPolicyWeatherStation2.setId(indexPolicyWeatherStation1.getId());
        assertThat(indexPolicyWeatherStation1).isEqualTo(indexPolicyWeatherStation2);

        indexPolicyWeatherStation2 = getIndexPolicyWeatherStationSample2();
        assertThat(indexPolicyWeatherStation1).isNotEqualTo(indexPolicyWeatherStation2);
    }
}
