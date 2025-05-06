package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPolicySeasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IndexPolicySeason getIndexPolicySeasonSample1() {
        return new IndexPolicySeason().id(1L).name("name1").period("period1");
    }

    public static IndexPolicySeason getIndexPolicySeasonSample2() {
        return new IndexPolicySeason().id(2L).name("name2").period("period2");
    }

    public static IndexPolicySeason getIndexPolicySeasonRandomSampleGenerator() {
        return new IndexPolicySeason()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .period(UUID.randomUUID().toString());
    }
}
