package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexPolicy getIndexPolicySample1() {
        return new IndexPolicy().id(1L).stageNo(1).indexStatus(1);
    }

    public static IndexPolicy getIndexPolicySample2() {
        return new IndexPolicy().id(2L).stageNo(2).indexStatus(2);
    }

    public static IndexPolicy getIndexPolicyRandomSampleGenerator() {
        return new IndexPolicy()
            .id(longCount.incrementAndGet())
            .stageNo(intCount.incrementAndGet())
            .indexStatus(intCount.incrementAndGet());
    }
}
