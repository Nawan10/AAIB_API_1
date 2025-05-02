package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandDamageReportDamageCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandDamageReportDamageCategory getCultivatedLandDamageReportDamageCategorySample1() {
        return new CultivatedLandDamageReportDamageCategory().id(1L).categoryName("categoryName1");
    }

    public static CultivatedLandDamageReportDamageCategory getCultivatedLandDamageReportDamageCategorySample2() {
        return new CultivatedLandDamageReportDamageCategory().id(2L).categoryName("categoryName2");
    }

    public static CultivatedLandDamageReportDamageCategory getCultivatedLandDamageReportDamageCategoryRandomSampleGenerator() {
        return new CultivatedLandDamageReportDamageCategory().id(longCount.incrementAndGet()).categoryName(UUID.randomUUID().toString());
    }
}
