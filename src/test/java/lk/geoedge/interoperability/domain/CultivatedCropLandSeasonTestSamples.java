package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CultivatedCropLandSeasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CultivatedCropLandSeason getCultivatedCropLandSeasonSample1() {
        return new CultivatedCropLandSeason().id(1L).name("name1").period("period1");
    }

    public static CultivatedCropLandSeason getCultivatedCropLandSeasonSample2() {
        return new CultivatedCropLandSeason().id(2L).name("name2").period("period2");
    }

    public static CultivatedCropLandSeason getCultivatedCropLandSeasonRandomSampleGenerator() {
        return new CultivatedCropLandSeason()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .period(UUID.randomUUID().toString());
    }
}
