package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CultivatedLandFarmerFieldOwnerCriteriaTest {

    @Test
    void newCultivatedLandFarmerFieldOwnerCriteriaHasAllFiltersNullTest() {
        var cultivatedLandFarmerFieldOwnerCriteria = new CultivatedLandFarmerFieldOwnerCriteria();
        assertThat(cultivatedLandFarmerFieldOwnerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cultivatedLandFarmerFieldOwnerCriteriaFluentMethodsCreatesFiltersTest() {
        var cultivatedLandFarmerFieldOwnerCriteria = new CultivatedLandFarmerFieldOwnerCriteria();

        setAllFilters(cultivatedLandFarmerFieldOwnerCriteria);

        assertThat(cultivatedLandFarmerFieldOwnerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cultivatedLandFarmerFieldOwnerCriteriaCopyCreatesNullFilterTest() {
        var cultivatedLandFarmerFieldOwnerCriteria = new CultivatedLandFarmerFieldOwnerCriteria();
        var copy = cultivatedLandFarmerFieldOwnerCriteria.copy();

        assertThat(cultivatedLandFarmerFieldOwnerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmerFieldOwnerCriteria)
        );
    }

    @Test
    void cultivatedLandFarmerFieldOwnerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cultivatedLandFarmerFieldOwnerCriteria = new CultivatedLandFarmerFieldOwnerCriteria();
        setAllFilters(cultivatedLandFarmerFieldOwnerCriteria);

        var copy = cultivatedLandFarmerFieldOwnerCriteria.copy();

        assertThat(cultivatedLandFarmerFieldOwnerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cultivatedLandFarmerFieldOwnerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cultivatedLandFarmerFieldOwnerCriteria = new CultivatedLandFarmerFieldOwnerCriteria();

        assertThat(cultivatedLandFarmerFieldOwnerCriteria).hasToString("CultivatedLandFarmerFieldOwnerCriteria{}");
    }

    private static void setAllFilters(CultivatedLandFarmerFieldOwnerCriteria cultivatedLandFarmerFieldOwnerCriteria) {
        cultivatedLandFarmerFieldOwnerCriteria.id();
        cultivatedLandFarmerFieldOwnerCriteria.landPlotName();
        cultivatedLandFarmerFieldOwnerCriteria.landRegistryNo();
        cultivatedLandFarmerFieldOwnerCriteria.totalLandExtent();
        cultivatedLandFarmerFieldOwnerCriteria.calculatedArea();
        cultivatedLandFarmerFieldOwnerCriteria.provinceId();
        cultivatedLandFarmerFieldOwnerCriteria.districtId();
        cultivatedLandFarmerFieldOwnerCriteria.dsId();
        cultivatedLandFarmerFieldOwnerCriteria.gnId();
        cultivatedLandFarmerFieldOwnerCriteria.centerLat();
        cultivatedLandFarmerFieldOwnerCriteria.centerLng();
        cultivatedLandFarmerFieldOwnerCriteria.distinct();
    }

    private static Condition<CultivatedLandFarmerFieldOwnerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CultivatedLandFarmerFieldOwnerCriteria> copyFiltersAre(
        CultivatedLandFarmerFieldOwnerCriteria copy,
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
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
