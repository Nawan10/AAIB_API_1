package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DamagesCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DamagesCategory getDamagesCategorySample1() {
        return new DamagesCategory().id(1L).categoryName("categoryName1");
    }

    public static DamagesCategory getDamagesCategorySample2() {
        return new DamagesCategory().id(2L).categoryName("categoryName2");
    }

    public static DamagesCategory getDamagesCategoryRandomSampleGenerator() {
        return new DamagesCategory().id(longCount.incrementAndGet()).categoryName(UUID.randomUUID().toString());
    }
}
