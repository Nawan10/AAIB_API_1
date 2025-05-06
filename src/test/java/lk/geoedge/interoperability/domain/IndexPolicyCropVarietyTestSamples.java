package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPolicyCropVarietyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexPolicyCropVariety getIndexPolicyCropVarietySample1() {
        return new IndexPolicyCropVariety().id(1L).name("name1").noOfStages(1).image("image1").description("description1").addedBy(1);
    }

    public static IndexPolicyCropVariety getIndexPolicyCropVarietySample2() {
        return new IndexPolicyCropVariety().id(2L).name("name2").noOfStages(2).image("image2").description("description2").addedBy(2);
    }

    public static IndexPolicyCropVariety getIndexPolicyCropVarietyRandomSampleGenerator() {
        return new IndexPolicyCropVariety()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .noOfStages(intCount.incrementAndGet())
            .image(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .addedBy(intCount.incrementAndGet());
    }
}
