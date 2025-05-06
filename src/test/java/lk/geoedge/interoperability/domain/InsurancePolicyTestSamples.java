package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsurancePolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InsurancePolicy getInsurancePolicySample1() {
        return new InsurancePolicy().id(1L).name("name1").policyNo("policyNo1").isActivate(1);
    }

    public static InsurancePolicy getInsurancePolicySample2() {
        return new InsurancePolicy().id(2L).name("name2").policyNo("policyNo2").isActivate(2);
    }

    public static InsurancePolicy getInsurancePolicyRandomSampleGenerator() {
        return new InsurancePolicy()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .policyNo(UUID.randomUUID().toString())
            .isActivate(intCount.incrementAndGet());
    }
}
