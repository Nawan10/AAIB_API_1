package lk.geoedge.interoperability.domain;

import static lk.geoedge.interoperability.domain.MahaweliTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import lk.geoedge.interoperability.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MahaweliTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mahaweli.class);
        Mahaweli mahaweli1 = getMahaweliSample1();
        Mahaweli mahaweli2 = new Mahaweli();
        assertThat(mahaweli1).isNotEqualTo(mahaweli2);

        mahaweli2.setId(mahaweli1.getId());
        assertThat(mahaweli1).isEqualTo(mahaweli2);

        mahaweli2 = getMahaweliSample2();
        assertThat(mahaweli1).isNotEqualTo(mahaweli2);
    }
}
