package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandDamageReasonDamageTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandDamageReasonDamageType getCultivatedLandDamageReasonDamageTypeSample1() {
        return new CultivatedLandDamageReasonDamageType().id(1L).typeName("typeName1");
    }

    public static CultivatedLandDamageReasonDamageType getCultivatedLandDamageReasonDamageTypeSample2() {
        return new CultivatedLandDamageReasonDamageType().id(2L).typeName("typeName2");
    }

    public static CultivatedLandDamageReasonDamageType getCultivatedLandDamageReasonDamageTypeRandomSampleGenerator() {
        return new CultivatedLandDamageReasonDamageType().id(longCount.incrementAndGet()).typeName(UUID.randomUUID().toString());
    }
}
