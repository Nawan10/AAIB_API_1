package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CanlendarCropSeasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CanlendarCropSeason getCanlendarCropSeasonSample1() {
        return new CanlendarCropSeason().id(1L).name("name1").period("period1");
    }

    public static CanlendarCropSeason getCanlendarCropSeasonSample2() {
        return new CanlendarCropSeason().id(2L).name("name2").period("period2");
    }

    public static CanlendarCropSeason getCanlendarCropSeasonRandomSampleGenerator() {
        return new CanlendarCropSeason()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .period(UUID.randomUUID().toString());
    }
}
