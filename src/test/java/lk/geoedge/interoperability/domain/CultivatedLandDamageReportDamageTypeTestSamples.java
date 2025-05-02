package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandDamageReportDamageTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandDamageReportDamageType getCultivatedLandDamageReportDamageTypeSample1() {
        return new CultivatedLandDamageReportDamageType().id(1L).typeName("typeName1");
    }

    public static CultivatedLandDamageReportDamageType getCultivatedLandDamageReportDamageTypeSample2() {
        return new CultivatedLandDamageReportDamageType().id(2L).typeName("typeName2");
    }

    public static CultivatedLandDamageReportDamageType getCultivatedLandDamageReportDamageTypeRandomSampleGenerator() {
        return new CultivatedLandDamageReportDamageType().id(longCount.incrementAndGet()).typeName(UUID.randomUUID().toString());
    }
}
