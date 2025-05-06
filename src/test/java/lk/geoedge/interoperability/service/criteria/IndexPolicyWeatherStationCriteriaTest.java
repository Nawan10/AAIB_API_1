package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPolicyWeatherStationCriteriaTest {

    @Test
    void newIndexPolicyWeatherStationCriteriaHasAllFiltersNullTest() {
        var indexPolicyWeatherStationCriteria = new IndexPolicyWeatherStationCriteria();
        assertThat(indexPolicyWeatherStationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPolicyWeatherStationCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPolicyWeatherStationCriteria = new IndexPolicyWeatherStationCriteria();

        setAllFilters(indexPolicyWeatherStationCriteria);

        assertThat(indexPolicyWeatherStationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPolicyWeatherStationCriteriaCopyCreatesNullFilterTest() {
        var indexPolicyWeatherStationCriteria = new IndexPolicyWeatherStationCriteria();
        var copy = indexPolicyWeatherStationCriteria.copy();

        assertThat(indexPolicyWeatherStationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyWeatherStationCriteria)
        );
    }

    @Test
    void indexPolicyWeatherStationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPolicyWeatherStationCriteria = new IndexPolicyWeatherStationCriteria();
        setAllFilters(indexPolicyWeatherStationCriteria);

        var copy = indexPolicyWeatherStationCriteria.copy();

        assertThat(indexPolicyWeatherStationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyWeatherStationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPolicyWeatherStationCriteria = new IndexPolicyWeatherStationCriteria();

        assertThat(indexPolicyWeatherStationCriteria).hasToString("IndexPolicyWeatherStationCriteria{}");
    }

    private static void setAllFilters(IndexPolicyWeatherStationCriteria indexPolicyWeatherStationCriteria) {
        indexPolicyWeatherStationCriteria.id();
        indexPolicyWeatherStationCriteria.name();
        indexPolicyWeatherStationCriteria.code();
        indexPolicyWeatherStationCriteria.latitude();
        indexPolicyWeatherStationCriteria.longitude();
        indexPolicyWeatherStationCriteria.gnId();
        indexPolicyWeatherStationCriteria.districtId();
        indexPolicyWeatherStationCriteria.provinceId();
        indexPolicyWeatherStationCriteria.dsId();
        indexPolicyWeatherStationCriteria.addedBy();
        indexPolicyWeatherStationCriteria.createdAt();
        indexPolicyWeatherStationCriteria.distinct();
    }

    private static Condition<IndexPolicyWeatherStationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getGnId()) &&
                condition.apply(criteria.getDistrictId()) &&
                condition.apply(criteria.getProvinceId()) &&
                condition.apply(criteria.getDsId()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPolicyWeatherStationCriteria> copyFiltersAre(
        IndexPolicyWeatherStationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getGnId(), copy.getGnId()) &&
                condition.apply(criteria.getDistrictId(), copy.getDistrictId()) &&
                condition.apply(criteria.getProvinceId(), copy.getProvinceId()) &&
                condition.apply(criteria.getDsId(), copy.getDsId()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
