package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandFarmersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CultivatedLandFarmers getCultivatedLandFarmersSample1() {
        return new CultivatedLandFarmers().id(1L).relationId(1).addedBy("addedBy1");
    }

    public static CultivatedLandFarmers getCultivatedLandFarmersSample2() {
        return new CultivatedLandFarmers().id(2L).relationId(2).addedBy("addedBy2");
    }

    public static CultivatedLandFarmers getCultivatedLandFarmersRandomSampleGenerator() {
        return new CultivatedLandFarmers()
            .id(longCount.incrementAndGet())
            .relationId(intCount.incrementAndGet())
            .addedBy(UUID.randomUUID().toString());
    }
}
