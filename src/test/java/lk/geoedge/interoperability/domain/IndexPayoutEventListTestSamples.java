package lk.geoedge.interoperability.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IndexPayoutEventListTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static IndexPayoutEventList getIndexPayoutEventListSample1() {
        return new IndexPayoutEventList()
            .id(1L)
            .indexPayoutEventId(1)
            .ascId(1)
            .confirmedBy(1)
            .confirmedDate("confirmedDate1")
            .rejectedBy(1)
            .rejectedDate("rejectedDate1")
            .reason("reason1")
            .indexPayoutEventStatus(1)
            .isApproved(1)
            .isInsurance(1)
            .insuranceCultivatedLand(1)
            .indexChequeId(1)
            .indexProductId(1);
    }

    public static IndexPayoutEventList getIndexPayoutEventListSample2() {
        return new IndexPayoutEventList()
            .id(2L)
            .indexPayoutEventId(2)
            .ascId(2)
            .confirmedBy(2)
            .confirmedDate("confirmedDate2")
            .rejectedBy(2)
            .rejectedDate("rejectedDate2")
            .reason("reason2")
            .indexPayoutEventStatus(2)
            .isApproved(2)
            .isInsurance(2)
            .insuranceCultivatedLand(2)
            .indexChequeId(2)
            .indexProductId(2);
    }

    public static IndexPayoutEventList getIndexPayoutEventListRandomSampleGenerator() {
        return new IndexPayoutEventList()
            .id(longCount.incrementAndGet())
            .indexPayoutEventId(intCount.incrementAndGet())
            .ascId(intCount.incrementAndGet())
            .confirmedBy(intCount.incrementAndGet())
            .confirmedDate(UUID.randomUUID().toString())
            .rejectedBy(intCount.incrementAndGet())
            .rejectedDate(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .indexPayoutEventStatus(intCount.incrementAndGet())
            .isApproved(intCount.incrementAndGet())
            .isInsurance(intCount.incrementAndGet())
            .insuranceCultivatedLand(intCount.incrementAndGet())
            .indexChequeId(intCount.incrementAndGet())
            .indexProductId(intCount.incrementAndGet());
    }
}
