package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceCropTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsuranceCrop getInsuranceCropSample1() {
        return new InsuranceCrop().id(1L).policyId("policyId1").addedBy("addedBy1");
    }

    public static InsuranceCrop getInsuranceCropSample2() {
        return new InsuranceCrop().id(2L).policyId("policyId2").addedBy("addedBy2");
    }

    public static InsuranceCrop getInsuranceCropRandomSampleGenerator() {
        return new InsuranceCrop()
            .id(longCount.incrementAndGet())
            .policyId(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
