package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceCultivatedLandCoveragesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InsuranceCultivatedLandCoverages getInsuranceCultivatedLandCoveragesSample1() {
        return new InsuranceCultivatedLandCoverages().id(1L).addedBy("addedBy1");
    }

    public static InsuranceCultivatedLandCoverages getInsuranceCultivatedLandCoveragesSample2() {
        return new InsuranceCultivatedLandCoverages().id(2L).addedBy("addedBy2");
    }

    public static InsuranceCultivatedLandCoverages getInsuranceCultivatedLandCoveragesRandomSampleGenerator() {
        return new InsuranceCultivatedLandCoverages().id(longCount.incrementAndGet()).addedBy(UUID.randomUUID().toString());
    }
}
