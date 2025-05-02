package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FarmerFieldOwnerCriteriaTest {

    @Test
    void newFarmerFieldOwnerCriteriaHasAllFiltersNullTest() {
        var farmerFieldOwnerCriteria = new FarmerFieldOwnerCriteria();
        assertThat(farmerFieldOwnerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void farmerFieldOwnerCriteriaFluentMethodsCreatesFiltersTest() {
        var farmerFieldOwnerCriteria = new FarmerFieldOwnerCriteria();

        setAllFilters(farmerFieldOwnerCriteria);

        assertThat(farmerFieldOwnerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void farmerFieldOwnerCriteriaCopyCreatesNullFilterTest() {
        var farmerFieldOwnerCriteria = new FarmerFieldOwnerCriteria();
        var copy = farmerFieldOwnerCriteria.copy();

        assertThat(farmerFieldOwnerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerFieldOwnerCriteria)
        );
    }

    @Test
    void farmerFieldOwnerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var farmerFieldOwnerCriteria = new FarmerFieldOwnerCriteria();
        setAllFilters(farmerFieldOwnerCriteria);

        var copy = farmerFieldOwnerCriteria.copy();

        assertThat(farmerFieldOwnerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(farmerFieldOwnerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var farmerFieldOwnerCriteria = new FarmerFieldOwnerCriteria();

        assertThat(farmerFieldOwnerCriteria).hasToString("FarmerFieldOwnerCriteria{}");
    }

    private static void setAllFilters(FarmerFieldOwnerCriteria farmerFieldOwnerCriteria) {
        farmerFieldOwnerCriteria.id();
        farmerFieldOwnerCriteria.landPlotName();
        farmerFieldOwnerCriteria.landRegistryNo();
        farmerFieldOwnerCriteria.totalLandExtent();
        farmerFieldOwnerCriteria.calculatedArea();
        farmerFieldOwnerCriteria.provinceId();
        farmerFieldOwnerCriteria.districtId();
        farmerFieldOwnerCriteria.dsId();
        farmerFieldOwnerCriteria.gnId();
        farmerFieldOwnerCriteria.centerLat();
        farmerFieldOwnerCriteria.centerLng();
        farmerFieldOwnerCriteria.cropId();
        farmerFieldOwnerCriteria.distinct();
    }

    private static Condition<FarmerFieldOwnerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLandPlotName()) &&
                condition.apply(criteria.getLandRegistryNo()) &&
                condition.apply(criteria.getTotalLandExtent()) &&
                condition.apply(criteria.getCalculatedArea()) &&
                condition.apply(criteria.getProvinceId()) &&
                condition.apply(criteria.getDistrictId()) &&
                condition.apply(criteria.getDsId()) &&
                condition.apply(criteria.getGnId()) &&
                condition.apply(criteria.getCenterLat()) &&
                condition.apply(criteria.getCenterLng()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FarmerFieldOwnerCriteria> copyFiltersAre(
        FarmerFieldOwnerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLandPlotName(), copy.getLandPlotName()) &&
                condition.apply(criteria.getLandRegistryNo(), copy.getLandRegistryNo()) &&
                condition.apply(criteria.getTotalLandExtent(), copy.getTotalLandExtent()) &&
                condition.apply(criteria.getCalculatedArea(), copy.getCalculatedArea()) &&
                condition.apply(criteria.getProvinceId(), copy.getProvinceId()) &&
                condition.apply(criteria.getDistrictId(), copy.getDistrictId()) &&
                condition.apply(criteria.getDsId(), copy.getDsId()) &&
                condition.apply(criteria.getGnId(), copy.getGnId()) &&
                condition.apply(criteria.getCenterLat(), copy.getCenterLat()) &&
                condition.apply(criteria.getCenterLng(), copy.getCenterLng()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
