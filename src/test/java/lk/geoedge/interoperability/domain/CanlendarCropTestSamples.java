package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CanlendarCropTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CanlendarCrop getCanlendarCropSample1() {
        return new CanlendarCrop().id(1L).canlendarCropStatus(1).reason("reason1").addedBy("addedBy1");
    }

    public static CanlendarCrop getCanlendarCropSample2() {
        return new CanlendarCrop().id(2L).canlendarCropStatus(2).reason("reason2").addedBy("addedBy2");
    }

    public static CanlendarCrop getCanlendarCropRandomSampleGenerator() {
        return new CanlendarCrop()
            .id(longCount.incrementAndGet())
            .canlendarCropStatus(intCount.incrementAndGet())
            .reason(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
