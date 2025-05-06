package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceCultivatedLandCultivatedLandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsuranceCultivatedLandCultivatedLand getInsuranceCultivatedLandCultivatedLandSample1() {
        return new InsuranceCultivatedLandCultivatedLand().id(1L).landStatus("landStatus1").addedBy("addedBy1");
    }

    public static InsuranceCultivatedLandCultivatedLand getInsuranceCultivatedLandCultivatedLandSample2() {
        return new InsuranceCultivatedLandCultivatedLand().id(2L).landStatus("landStatus2").addedBy("addedBy2");
    }

    public static InsuranceCultivatedLandCultivatedLand getInsuranceCultivatedLandCultivatedLandRandomSampleGenerator() {
        return new InsuranceCultivatedLandCultivatedLand()
            .id(longCount.incrementAndGet())
            .landStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
