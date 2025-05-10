package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandEntity getCultivatedLandEntitySample1() {
        return new CultivatedLandEntity().id(1L).landStatus("landStatus1").addedBy("addedBy1");
    }

    public static CultivatedLandEntity getCultivatedLandEntitySample2() {
        return new CultivatedLandEntity().id(2L).landStatus("landStatus2").addedBy("addedBy2");
    }

    public static CultivatedLandEntity getCultivatedLandEntityRandomSampleGenerator() {
        return new CultivatedLandEntity()
            .id(longCount.incrementAndGet())
            .landStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
