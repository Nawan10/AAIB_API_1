package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandDamageReasonDamageCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandDamageReasonDamageCategory getCultivatedLandDamageReasonDamageCategorySample1() {
        return new CultivatedLandDamageReasonDamageCategory().id(1L).categoryName("categoryName1");
    }

    public static CultivatedLandDamageReasonDamageCategory getCultivatedLandDamageReasonDamageCategorySample2() {
        return new CultivatedLandDamageReasonDamageCategory().id(2L).categoryName("categoryName2");
    }

    public static CultivatedLandDamageReasonDamageCategory getCultivatedLandDamageReasonDamageCategoryRandomSampleGenerator() {
        return new CultivatedLandDamageReasonDamageCategory().id(longCount.incrementAndGet()).categoryName(UUID.randomUUID().toString());
    }
}
