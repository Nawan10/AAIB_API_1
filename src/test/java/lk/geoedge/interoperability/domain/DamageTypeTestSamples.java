package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DamageTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DamageType getDamageTypeSample1() {
        return new DamageType().id(1L).typeName("typeName1");
    }

    public static DamageType getDamageTypeSample2() {
        return new DamageType().id(2L).typeName("typeName2");
    }

    public static DamageType getDamageTypeRandomSampleGenerator() {
        return new DamageType().id(longCount.incrementAndGet()).typeName(UUID.randomUUID().toString());
    }
}
