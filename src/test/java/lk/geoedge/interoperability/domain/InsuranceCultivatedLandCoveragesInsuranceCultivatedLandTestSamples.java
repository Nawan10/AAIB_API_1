package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceCultivatedLandCoveragesInsuranceCultivatedLandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InsuranceCultivatedLandCoveragesInsuranceCultivatedLand getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandSample1() {
        return new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand()
            .id(1L)
            .cropDurationId("cropDurationId1")
            .insurancePoliceId("insurancePoliceId1")
            .sumInsuredPerAcre(1)
            .insuranceExtent(1)
            .sumAmount(1)
            .insuranceStatus("insuranceStatus1")
            .addedBy("addedBy1");
    }

    public static InsuranceCultivatedLandCoveragesInsuranceCultivatedLand getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandSample2() {
        return new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand()
            .id(2L)
            .cropDurationId("cropDurationId2")
            .insurancePoliceId("insurancePoliceId2")
            .sumInsuredPerAcre(2)
            .insuranceExtent(2)
            .sumAmount(2)
            .insuranceStatus("insuranceStatus2")
            .addedBy("addedBy2");
    }

    public static InsuranceCultivatedLandCoveragesInsuranceCultivatedLand getInsuranceCultivatedLandCoveragesInsuranceCultivatedLandRandomSampleGenerator() {
        return new InsuranceCultivatedLandCoveragesInsuranceCultivatedLand()
            .id(longCount.incrementAndGet())
            .cropDurationId(UUID.randomUUID().toString())
            .insurancePoliceId(UUID.randomUUID().toString())
            .sumInsuredPerAcre(intCount.incrementAndGet())
            .insuranceExtent(intCount.incrementAndGet())
            .sumAmount(intCount.incrementAndGet())
            .insuranceStatus(UUID.randomUUID().toString())
            .addedBy(UUID.randomUUID().toString());
    }
}
