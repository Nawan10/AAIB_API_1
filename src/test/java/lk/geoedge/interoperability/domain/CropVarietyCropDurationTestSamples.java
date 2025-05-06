package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CropVarietyCropDurationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CropVarietyCropDuration getCropVarietyCropDurationSample1() {
        return new CropVarietyCropDuration().id(1L).duration(1).name("name1").stages("stages1").addedBy(1);
    }

    public static CropVarietyCropDuration getCropVarietyCropDurationSample2() {
        return new CropVarietyCropDuration().id(2L).duration(2).name("name2").stages("stages2").addedBy(2);
    }

    public static CropVarietyCropDuration getCropVarietyCropDurationRandomSampleGenerator() {
        return new CropVarietyCropDuration()
            .id(longCount.incrementAndGet())
            .duration(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .stages(UUID.randomUUID().toString())
            .addedBy(intCount.incrementAndGet());
    }
}
