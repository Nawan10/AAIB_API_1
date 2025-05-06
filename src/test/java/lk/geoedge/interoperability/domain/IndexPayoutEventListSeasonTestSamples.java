package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPayoutEventListSeasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IndexPayoutEventListSeason getIndexPayoutEventListSeasonSample1() {
        return new IndexPayoutEventListSeason().id(1L).name("name1").period("period1");
    }

    public static IndexPayoutEventListSeason getIndexPayoutEventListSeasonSample2() {
        return new IndexPayoutEventListSeason().id(2L).name("name2").period("period2");
    }

    public static IndexPayoutEventListSeason getIndexPayoutEventListSeasonRandomSampleGenerator() {
        return new IndexPayoutEventListSeason()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .period(UUID.randomUUID().toString());
    }
}
