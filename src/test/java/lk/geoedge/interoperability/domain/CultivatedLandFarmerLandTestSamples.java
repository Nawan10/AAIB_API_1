package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandFarmerLandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandFarmerLand getCultivatedLandFarmerLandSample1() {
        return new CultivatedLandFarmerLand().id(1L).landStatus("landStatus1").addedBy("addedBy1");
    }

    public static CultivatedLandFarmerLand getCultivatedLandFarmerLandSample2() {
        return new CultivatedLandFarmerLand().id(2L).landStatus("landStatus2").addedBy("addedBy2");
    }

    public static CultivatedLandFarmerLand getCultivatedLandFarmerLandRandomSampleGenerator() {
        return new CultivatedLandFarmerLand()
            .id(longCount.incrementAndGet())
            .landStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
