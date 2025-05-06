package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InsuranceCultivatedLandFarmerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InsuranceCultivatedLandFarmer getInsuranceCultivatedLandFarmerSample1() {
        return new InsuranceCultivatedLandFarmer()
            .id(1L)
            .farmerId("farmerId1")
            .farmerName("farmerName1")
            .nicNo("nicNo1")
            .addressFirstLine("addressFirstLine1")
            .contactNoEmail("contactNoEmail1")
            .provinceId(1)
            .districtId(1)
            .dsId(1)
            .gnId(1)
            .city("city1");
    }

    public static InsuranceCultivatedLandFarmer getInsuranceCultivatedLandFarmerSample2() {
        return new InsuranceCultivatedLandFarmer()
            .id(2L)
            .farmerId("farmerId2")
            .farmerName("farmerName2")
            .nicNo("nicNo2")
            .addressFirstLine("addressFirstLine2")
            .contactNoEmail("contactNoEmail2")
            .provinceId(2)
            .districtId(2)
            .dsId(2)
            .gnId(2)
            .city("city2");
    }

    public static InsuranceCultivatedLandFarmer getInsuranceCultivatedLandFarmerRandomSampleGenerator() {
        return new InsuranceCultivatedLandFarmer()
            .id(longCount.incrementAndGet())
            .farmerId(UUID.randomUUID().toString())
            .farmerName(UUID.randomUUID().toString())
            .nicNo(UUID.randomUUID().toString())
            .addressFirstLine(UUID.randomUUID().toString())
            .contactNoEmail(UUID.randomUUID().toString())
            .provinceId(intCount.incrementAndGet())
            .districtId(intCount.incrementAndGet())
            .dsId(intCount.incrementAndGet())
            .gnId(intCount.incrementAndGet())
            .city(UUID.randomUUID().toString());
    }
}
