package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPayoutEventListCultivatedLandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IndexPayoutEventListCultivatedLand getIndexPayoutEventListCultivatedLandSample1() {
        return new IndexPayoutEventListCultivatedLand().id(1L).landStatus("landStatus1").addedBy("addedBy1");
    }

    public static IndexPayoutEventListCultivatedLand getIndexPayoutEventListCultivatedLandSample2() {
        return new IndexPayoutEventListCultivatedLand().id(2L).landStatus("landStatus2").addedBy("addedBy2");
    }

    public static IndexPayoutEventListCultivatedLand getIndexPayoutEventListCultivatedLandRandomSampleGenerator() {
        return new IndexPayoutEventListCultivatedLand()
            .id(longCount.incrementAndGet())
            .landStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
