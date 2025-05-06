package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MahaweliCriteriaTest {

    @Test
    void newMahaweliCriteriaHasAllFiltersNullTest() {
        var mahaweliCriteria = new MahaweliCriteria();
        assertThat(mahaweliCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void mahaweliCriteriaFluentMethodsCreatesFiltersTest() {
        var mahaweliCriteria = new MahaweliCriteria();

        setAllFilters(mahaweliCriteria);

        assertThat(mahaweliCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void mahaweliCriteriaCopyCreatesNullFilterTest() {
        var mahaweliCriteria = new MahaweliCriteria();
        var copy = mahaweliCriteria.copy();

        assertThat(mahaweliCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(mahaweliCriteria)
        );
    }

    @Test
    void mahaweliCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mahaweliCriteria = new MahaweliCriteria();
        setAllFilters(mahaweliCriteria);

        var copy = mahaweliCriteria.copy();

        assertThat(mahaweliCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(mahaweliCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mahaweliCriteria = new MahaweliCriteria();

        assertThat(mahaweliCriteria).hasToString("MahaweliCriteria{}");
    }

    private static void setAllFilters(MahaweliCriteria mahaweliCriteria) {
        mahaweliCriteria.id();
        mahaweliCriteria.mahaweli();
        mahaweliCriteria.code();
        mahaweliCriteria.addedBy();
        mahaweliCriteria.addedDate();
        mahaweliCriteria.distinct();
    }

    private static Condition<MahaweliCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMahaweli()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getAddedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MahaweliCriteria> copyFiltersAre(MahaweliCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMahaweli(), copy.getMahaweli()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getAddedDate(), copy.getAddedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
