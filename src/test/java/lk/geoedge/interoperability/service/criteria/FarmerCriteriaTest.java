package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FarmerCriteriaTest {

    @Test
    void newFarmerCriteriaHasAllFiltersNullTest() {
        var farmerCriteria = new FarmerCriteria();
        assertThat(farmerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void farmerCriteriaFluentMethodsCreatesFiltersTest() {
        var farmerCriteria = new FarmerCriteria();

        setAllFilters(farmerCriteria);

        assertThat(farmerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void farmerCriteriaCopyCreatesNullFilterTest() {
        var farmerCriteria = new FarmerCriteria();
        var copy = farmerCriteria.copy();

        assertThat(farmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerCriteria)
        );
    }

    @Test
    void farmerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var farmerCriteria = new FarmerCriteria();
        setAllFilters(farmerCriteria);

        var copy = farmerCriteria.copy();

        assertThat(farmerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var farmerCriteria = new FarmerCriteria();

        assertThat(farmerCriteria).hasToString("FarmerCriteria{}");
    }

    private static void setAllFilters(FarmerCriteria farmerCriteria) {
        farmerCriteria.id();
        farmerCriteria.farmerId();
        farmerCriteria.farmerName();
        farmerCriteria.nicNo();
        farmerCriteria.addressFirstLine();
        farmerCriteria.contactNoEmail();
        farmerCriteria.provinceId();
        farmerCriteria.districtId();
        farmerCriteria.dsId();
        farmerCriteria.gnId();
        farmerCriteria.city();
        farmerCriteria.addedDate();
        farmerCriteria.distinct();
    }

    private static Condition<FarmerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFarmerId()) &&
                condition.apply(criteria.getFarmerName()) &&
                condition.apply(criteria.getNicNo()) &&
                condition.apply(criteria.getAddressFirstLine()) &&
                condition.apply(criteria.getContactNoEmail()) &&
                condition.apply(criteria.getProvinceId()) &&
                condition.apply(criteria.getDistrictId()) &&
                condition.apply(criteria.getDsId()) &&
                condition.apply(criteria.getGnId()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getAddedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FarmerCriteria> copyFiltersAre(FarmerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFarmerId(), copy.getFarmerId()) &&
                condition.apply(criteria.getFarmerName(), copy.getFarmerName()) &&
                condition.apply(criteria.getNicNo(), copy.getNicNo()) &&
                condition.apply(criteria.getAddressFirstLine(), copy.getAddressFirstLine()) &&
                condition.apply(criteria.getContactNoEmail(), copy.getContactNoEmail()) &&
                condition.apply(criteria.getProvinceId(), copy.getProvinceId()) &&
                condition.apply(criteria.getDistrictId(), copy.getDistrictId()) &&
                condition.apply(criteria.getDsId(), copy.getDsId()) &&
                condition.apply(criteria.getGnId(), copy.getGnId()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getAddedDate(), copy.getAddedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
