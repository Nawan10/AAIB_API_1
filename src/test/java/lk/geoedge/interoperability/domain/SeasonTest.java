package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.SeasonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeasonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Season.class);
        Season season1 = getSeasonSample1();
        Season season2 = new Season();
        assertThat(season1).isNotEqualTo(season2);

        season2.setId(season1.getId());
        assertThat(season1).isEqualTo(season2);

        season2 = getSeasonSample2();
        assertThat(season1).isNotEqualTo(season2);
    }
}
