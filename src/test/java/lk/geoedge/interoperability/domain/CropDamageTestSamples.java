package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CropDamageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CropDamage getCropDamageSample1() {
        return new CropDamage().id(1L).addedBy("addedBy1");
    }

    public static CropDamage getCropDamageSample2() {
        return new CropDamage().id(2L).addedBy("addedBy2");
    }

    public static CropDamage getCropDamageRandomSampleGenerator() {
        return new CropDamage().id(longCount.incrementAndGet()).addedBy(UUID.randomUUID().toString());
    }
}
