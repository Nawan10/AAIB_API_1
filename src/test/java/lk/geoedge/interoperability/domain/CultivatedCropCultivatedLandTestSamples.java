package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedCropCultivatedLandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedCropCultivatedLand getCultivatedCropCultivatedLandSample1() {
        return new CultivatedCropCultivatedLand().id(1L).landStatus("landStatus1").addedBy("addedBy1");
    }

    public static CultivatedCropCultivatedLand getCultivatedCropCultivatedLandSample2() {
        return new CultivatedCropCultivatedLand().id(2L).landStatus("landStatus2").addedBy("addedBy2");
    }

    public static CultivatedCropCultivatedLand getCultivatedCropCultivatedLandRandomSampleGenerator() {
        return new CultivatedCropCultivatedLand()
            .id(longCount.incrementAndGet())
            .landStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
