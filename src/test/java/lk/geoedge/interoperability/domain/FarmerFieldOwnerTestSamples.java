package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FarmerFieldOwnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FarmerFieldOwner getFarmerFieldOwnerSample1() {
        return new FarmerFieldOwner()
            .id(1L)
            .landPlotName("landPlotName1")
            .landRegistryNo("landRegistryNo1")
            .provinceId("provinceId1")
            .districtId("districtId1")
            .dsId("dsId1")
            .gnId("gnId1");
    }

    public static FarmerFieldOwner getFarmerFieldOwnerSample2() {
        return new FarmerFieldOwner()
            .id(2L)
            .landPlotName("landPlotName2")
            .landRegistryNo("landRegistryNo2")
            .provinceId("provinceId2")
            .districtId("districtId2")
            .dsId("dsId2")
            .gnId("gnId2");
    }

    public static FarmerFieldOwner getFarmerFieldOwnerRandomSampleGenerator() {
        return new FarmerFieldOwner()
            .id(longCount.incrementAndGet())
            .landPlotName(UUID.randomUUID().toString())
            .landRegistryNo(UUID.randomUUID().toString())
            .provinceId(UUID.randomUUID().toString())
            .districtId(UUID.randomUUID().toString())
            .dsId(UUID.randomUUID().toString())
            .gnId(UUID.randomUUID().toString());
    }
}
