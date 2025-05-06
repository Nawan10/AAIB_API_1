package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPolicyWeatherStationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexPolicyWeatherStation getIndexPolicyWeatherStationSample1() {
        return new IndexPolicyWeatherStation().id(1L).name("name1").code("code1").gnId(1).districtId(1).provinceId(1).dsId(1).addedBy(1);
    }

    public static IndexPolicyWeatherStation getIndexPolicyWeatherStationSample2() {
        return new IndexPolicyWeatherStation().id(2L).name("name2").code("code2").gnId(2).districtId(2).provinceId(2).dsId(2).addedBy(2);
    }

    public static IndexPolicyWeatherStation getIndexPolicyWeatherStationRandomSampleGenerator() {
        return new IndexPolicyWeatherStation()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .gnId(intCount.incrementAndGet())
            .districtId(intCount.incrementAndGet())
            .provinceId(intCount.incrementAndGet())
            .dsId(intCount.incrementAndGet())
            .addedBy(intCount.incrementAndGet());
    }
}
