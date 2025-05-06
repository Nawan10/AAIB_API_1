package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CanlendarCropSeasonCriteriaTest {

    @Test
    void newCanlendarCropSeasonCriteriaHasAllFiltersNullTest() {
        var canlendarCropSeasonCriteria = new CanlendarCropSeasonCriteria();
        assertThat(canlendarCropSeasonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void canlendarCropSeasonCriteriaFluentMethodsCreatesFiltersTest() {
        var canlendarCropSeasonCriteria = new CanlendarCropSeasonCriteria();

        setAllFilters(canlendarCropSeasonCriteria);

        assertThat(canlendarCropSeasonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void canlendarCropSeasonCriteriaCopyCreatesNullFilterTest() {
        var canlendarCropSeasonCriteria = new CanlendarCropSeasonCriteria();
        var copy = canlendarCropSeasonCriteria.copy();

        assertThat(canlendarCropSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(canlendarCropSeasonCriteria)
        );
    }

    @Test
    void canlendarCropSeasonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var canlendarCropSeasonCriteria = new CanlendarCropSeasonCriteria();
        setAllFilters(canlendarCropSeasonCriteria);

        var copy = canlendarCropSeasonCriteria.copy();

        assertThat(canlendarCropSeasonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(canlendarCropSeasonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var canlendarCropSeasonCriteria = new CanlendarCropSeasonCriteria();

        assertThat(canlendarCropSeasonCriteria).hasToString("CanlendarCropSeasonCriteria{}");
    }

    private static void setAllFilters(CanlendarCropSeasonCriteria canlendarCropSeasonCriteria) {
        canlendarCropSeasonCriteria.id();
        canlendarCropSeasonCriteria.name();
        canlendarCropSeasonCriteria.period();
        canlendarCropSeasonCriteria.distinct();
    }

    private static Condition<CanlendarCropSeasonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPeriod()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CanlendarCropSeasonCriteria> copyFiltersAre(
        CanlendarCropSeasonCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPeriod(), copy.getPeriod()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
