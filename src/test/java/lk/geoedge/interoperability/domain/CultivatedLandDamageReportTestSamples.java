package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandDamageReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandDamageReport getCultivatedLandDamageReportSample1() {
        return new CultivatedLandDamageReport()
            .id(1L)
            .damageReasonId("damageReasonId1")
            .damageServerityId("damageServerityId1")
            .damageDateMonitor("damageDateMonitor1")
            .description("description1")
            .farmerComment("farmerComment1")
            .estimatedYield("estimatedYield1")
            .addedBy("addedBy1");
    }

    public static CultivatedLandDamageReport getCultivatedLandDamageReportSample2() {
        return new CultivatedLandDamageReport()
            .id(2L)
            .damageReasonId("damageReasonId2")
            .damageServerityId("damageServerityId2")
            .damageDateMonitor("damageDateMonitor2")
            .description("description2")
            .farmerComment("farmerComment2")
            .estimatedYield("estimatedYield2")
            .addedBy("addedBy2");
    }

    public static CultivatedLandDamageReport getCultivatedLandDamageReportRandomSampleGenerator() {
        return new CultivatedLandDamageReport()
            .id(longCount.incrementAndGet())
            .damageReasonId(UUID.randomUUID().toString())
            .damageServerityId(UUID.randomUUID().toString())
            .damageDateMonitor(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .farmerComment(UUID.randomUUID().toString())
            .estimatedYield(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
