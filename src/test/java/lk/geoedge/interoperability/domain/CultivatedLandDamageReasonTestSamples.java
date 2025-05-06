package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandDamageReasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandDamageReason getCultivatedLandDamageReasonSample1() {
        return new CultivatedLandDamageReason().id(1L).name("name1");
    }

    public static CultivatedLandDamageReason getCultivatedLandDamageReasonSample2() {
        return new CultivatedLandDamageReason().id(2L).name("name2");
    }

    public static CultivatedLandDamageReason getCultivatedLandDamageReasonRandomSampleGenerator() {
        return new CultivatedLandDamageReason().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
