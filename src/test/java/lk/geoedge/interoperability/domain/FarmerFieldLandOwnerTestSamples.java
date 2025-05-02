package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FarmerFieldLandOwnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FarmerFieldLandOwner getFarmerFieldLandOwnerSample1() {
        return new FarmerFieldLandOwner().id(1L).addedBy("addedBy1");
    }

    public static FarmerFieldLandOwner getFarmerFieldLandOwnerSample2() {
        return new FarmerFieldLandOwner().id(2L).addedBy("addedBy2");
    }

    public static FarmerFieldLandOwner getFarmerFieldLandOwnerRandomSampleGenerator() {
        return new FarmerFieldLandOwner().id(longCount.incrementAndGet()).addedBy(UUID.randomUUID().toString());
    }
}
