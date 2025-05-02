package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DamageCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DamageCategory getDamageCategorySample1() {
        return new DamageCategory().id(1L).categoryName("categoryName1");
    }

    public static DamageCategory getDamageCategorySample2() {
        return new DamageCategory().id(2L).categoryName("categoryName2");
    }

    public static DamageCategory getDamageCategoryRandomSampleGenerator() {
        return new DamageCategory().id(longCount.incrementAndGet()).categoryName(UUID.randomUUID().toString());
    }
}
