package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MahaweliTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Mahaweli getMahaweliSample1() {
        return new Mahaweli().id(1L).mahaweli("mahaweli1").code("code1").addedBy(1);
    }

    public static Mahaweli getMahaweliSample2() {
        return new Mahaweli().id(2L).mahaweli("mahaweli2").code("code2").addedBy(2);
    }

    public static Mahaweli getMahaweliRandomSampleGenerator() {
        return new Mahaweli()
            .id(longCount.incrementAndGet())
            .mahaweli(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .addedBy(intCount.incrementAndGet());
    }
}
