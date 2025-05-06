package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPolicyCropTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexPolicyCropType getIndexPolicyCropTypeSample1() {
        return new IndexPolicyCropType()
            .id(1L)
            .crop("crop1")
            .image("image1")
            .mainCrop(1)
            .cropCode("cropCode1")
            .noOfStages("noOfStages1")
            .description("description1")
            .cropTypesId(1)
            .unitsId(1);
    }

    public static IndexPolicyCropType getIndexPolicyCropTypeSample2() {
        return new IndexPolicyCropType()
            .id(2L)
            .crop("crop2")
            .image("image2")
            .mainCrop(2)
            .cropCode("cropCode2")
            .noOfStages("noOfStages2")
            .description("description2")
            .cropTypesId(2)
            .unitsId(2);
    }

    public static IndexPolicyCropType getIndexPolicyCropTypeRandomSampleGenerator() {
        return new IndexPolicyCropType()
            .id(longCount.incrementAndGet())
            .crop(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString())
            .mainCrop(intCount.incrementAndGet())
            .cropCode(UUID.randomUUID().toString())
            .noOfStages(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .cropTypesId(intCount.incrementAndGet())
            .unitsId(intCount.incrementAndGet());
    }
}
