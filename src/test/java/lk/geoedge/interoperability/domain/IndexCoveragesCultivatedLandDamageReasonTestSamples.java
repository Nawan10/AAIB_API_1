package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IndexCoveragesCultivatedLandDamageReasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IndexCoveragesCultivatedLandDamageReason getIndexCoveragesCultivatedLandDamageReasonSample1() {
        return new IndexCoveragesCultivatedLandDamageReason().id(1L).name("name1");
    }

    public static IndexCoveragesCultivatedLandDamageReason getIndexCoveragesCultivatedLandDamageReasonSample2() {
        return new IndexCoveragesCultivatedLandDamageReason().id(2L).name("name2");
    }

    public static IndexCoveragesCultivatedLandDamageReason getIndexCoveragesCultivatedLandDamageReasonRandomSampleGenerator() {
        return new IndexCoveragesCultivatedLandDamageReason().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
