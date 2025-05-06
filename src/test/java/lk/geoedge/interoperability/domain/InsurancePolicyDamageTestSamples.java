package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsurancePolicyDamageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InsurancePolicyDamage getInsurancePolicyDamageSample1() {
        return new InsurancePolicyDamage().id(1L).isFree(1).isPaid(1);
    }

    public static InsurancePolicyDamage getInsurancePolicyDamageSample2() {
        return new InsurancePolicyDamage().id(2L).isFree(2).isPaid(2);
    }

    public static InsurancePolicyDamage getInsurancePolicyDamageRandomSampleGenerator() {
        return new InsurancePolicyDamage()
            .id(longCount.incrementAndGet())
            .isFree(intCount.incrementAndGet())
            .isPaid(intCount.incrementAndGet());
    }
}
