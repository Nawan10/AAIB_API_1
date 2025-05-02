package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLand getCultivatedLandSample1() {
        return new CultivatedLand().id(1L).landStatus("landStatus1").addedBy("addedBy1");
    }

    public static CultivatedLand getCultivatedLandSample2() {
        return new CultivatedLand().id(2L).landStatus("landStatus2").addedBy("addedBy2");
    }

    public static CultivatedLand getCultivatedLandRandomSampleGenerator() {
        return new CultivatedLand()
            .id(longCount.incrementAndGet())
            .landStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
