package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DamagesTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DamagesType getDamagesTypeSample1() {
        return new DamagesType().id(1L).typeName("typeName1");
    }

    public static DamagesType getDamagesTypeSample2() {
        return new DamagesType().id(2L).typeName("typeName2");
    }

    public static DamagesType getDamagesTypeRandomSampleGenerator() {
        return new DamagesType().id(longCount.incrementAndGet()).typeName(UUID.randomUUID().toString());
    }
}
