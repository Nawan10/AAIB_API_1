package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPolicyInsurancePolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexPolicyInsurancePolicy getIndexPolicyInsurancePolicySample1() {
        return new IndexPolicyInsurancePolicy().id(1L).name("name1").policyNo("policyNo1").isActivate(1);
    }

    public static IndexPolicyInsurancePolicy getIndexPolicyInsurancePolicySample2() {
        return new IndexPolicyInsurancePolicy().id(2L).name("name2").policyNo("policyNo2").isActivate(2);
    }

    public static IndexPolicyInsurancePolicy getIndexPolicyInsurancePolicyRandomSampleGenerator() {
        return new IndexPolicyInsurancePolicy()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .policyNo(UUID.randomUUID().toString())
            .isActivate(intCount.incrementAndGet());
    }
}
