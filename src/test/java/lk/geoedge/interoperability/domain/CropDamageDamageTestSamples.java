package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CropDamageDamageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CropDamageDamage getCropDamageDamageSample1() {
        return new CropDamageDamage()
            .id(1L)
            .damageName("damageName1")
            .damageCode("damageCode1")
            .damageFamily("damageFamily1")
            .damageGenus("damageGenus1")
            .damageSpecies("damageSpecies1")
            .addedBy("addedBy1");
    }

    public static CropDamageDamage getCropDamageDamageSample2() {
        return new CropDamageDamage()
            .id(2L)
            .damageName("damageName2")
            .damageCode("damageCode2")
            .damageFamily("damageFamily2")
            .damageGenus("damageGenus2")
            .damageSpecies("damageSpecies2")
            .addedBy("addedBy2");
    }

    public static CropDamageDamage getCropDamageDamageRandomSampleGenerator() {
        return new CropDamageDamage()
            .id(longCount.incrementAndGet())
            .damageName(UUID.randomUUID().toString())
            .damageCode(UUID.randomUUID().toString())
            .damageFamily(UUID.randomUUID().toString())
            .damageGenus(UUID.randomUUID().toString())
            .damageSpecies(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
