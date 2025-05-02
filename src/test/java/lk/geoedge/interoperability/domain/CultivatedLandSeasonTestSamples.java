package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedLandSeasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedLandSeason getCultivatedLandSeasonSample1() {
        return new CultivatedLandSeason().id(1L).name("name1").period("period1");
    }

    public static CultivatedLandSeason getCultivatedLandSeasonSample2() {
        return new CultivatedLandSeason().id(2L).name("name2").period("period2");
    }

    public static CultivatedLandSeason getCultivatedLandSeasonRandomSampleGenerator() {
        return new CultivatedLandSeason()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .period(UUID.randomUUID().toString());
    }
}
