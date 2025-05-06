package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsurancePolicyDamageCultivatedLandDamageReasonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InsurancePolicyDamageCultivatedLandDamageReason getInsurancePolicyDamageCultivatedLandDamageReasonSample1() {
        return new InsurancePolicyDamageCultivatedLandDamageReason().id(1L).name("name1").damageCategoryId(1).damageTypeId(1);
    }

    public static InsurancePolicyDamageCultivatedLandDamageReason getInsurancePolicyDamageCultivatedLandDamageReasonSample2() {
        return new InsurancePolicyDamageCultivatedLandDamageReason().id(2L).name("name2").damageCategoryId(2).damageTypeId(2);
    }

    public static InsurancePolicyDamageCultivatedLandDamageReason getInsurancePolicyDamageCultivatedLandDamageReasonRandomSampleGenerator() {
        return new InsurancePolicyDamageCultivatedLandDamageReason()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .damageCategoryId(intCount.incrementAndGet())
            .damageTypeId(intCount.incrementAndGet());
    }
}
