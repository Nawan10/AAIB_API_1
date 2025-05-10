package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DamageEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DamageEntity getDamageEntitySample1() {
        return new DamageEntity()
            .id(1L)
            .damageName("damageName1")
            .damageCode("damageCode1")
            .damageFamily("damageFamily1")
            .damageGenus("damageGenus1")
            .damageSpecies("damageSpecies1")
            .addedBy("addedBy1");
    }

    public static DamageEntity getDamageEntitySample2() {
        return new DamageEntity()
            .id(2L)
            .damageName("damageName2")
            .damageCode("damageCode2")
            .damageFamily("damageFamily2")
            .damageGenus("damageGenus2")
            .damageSpecies("damageSpecies2")
            .addedBy("addedBy2");
    }

    public static DamageEntity getDamageEntityRandomSampleGenerator() {
        return new DamageEntity()
            .id(longCount.incrementAndGet())
            .damageName(UUID.randomUUID().toString())
            .damageCode(UUID.randomUUID().toString())
            .damageFamily(UUID.randomUUID().toString())
            .damageGenus(UUID.randomUUID().toString())
            .damageSpecies(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
