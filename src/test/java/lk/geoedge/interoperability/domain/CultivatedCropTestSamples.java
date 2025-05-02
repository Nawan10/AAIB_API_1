package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedCropTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedCrop getCultivatedCropSample1() {
        return new CultivatedCrop().id(1L).unitId("unitId1").addedBy("addedBy1");
    }

    public static CultivatedCrop getCultivatedCropSample2() {
        return new CultivatedCrop().id(2L).unitId("unitId2").addedBy("addedBy2");
    }

    public static CultivatedCrop getCultivatedCropRandomSampleGenerator() {
        return new CultivatedCrop()
            .id(longCount.incrementAndGet())
            .unitId(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
