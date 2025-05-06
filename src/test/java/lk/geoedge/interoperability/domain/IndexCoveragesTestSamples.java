package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexCoveragesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexCoverages getIndexCoveragesSample1() {
        return new IndexCoverages().id(1L).indexProductId(1).premiumRate(1).isFree(1).isPaid(1);
    }

    public static IndexCoverages getIndexCoveragesSample2() {
        return new IndexCoverages().id(2L).indexProductId(2).premiumRate(2).isFree(2).isPaid(2);
    }

    public static IndexCoverages getIndexCoveragesRandomSampleGenerator() {
        return new IndexCoverages()
            .id(longCount.incrementAndGet())
            .indexProductId(intCount.incrementAndGet())
            .premiumRate(intCount.incrementAndGet())
            .isFree(intCount.incrementAndGet())
            .isPaid(intCount.incrementAndGet());
    }
}
