package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPolicyCropVarietyCriteriaTest {

    @Test
    void newIndexPolicyCropVarietyCriteriaHasAllFiltersNullTest() {
        var indexPolicyCropVarietyCriteria = new IndexPolicyCropVarietyCriteria();
        assertThat(indexPolicyCropVarietyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPolicyCropVarietyCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPolicyCropVarietyCriteria = new IndexPolicyCropVarietyCriteria();

        setAllFilters(indexPolicyCropVarietyCriteria);

        assertThat(indexPolicyCropVarietyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPolicyCropVarietyCriteriaCopyCreatesNullFilterTest() {
        var indexPolicyCropVarietyCriteria = new IndexPolicyCropVarietyCriteria();
        var copy = indexPolicyCropVarietyCriteria.copy();

        assertThat(indexPolicyCropVarietyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyCropVarietyCriteria)
        );
    }

    @Test
    void indexPolicyCropVarietyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPolicyCropVarietyCriteria = new IndexPolicyCropVarietyCriteria();
        setAllFilters(indexPolicyCropVarietyCriteria);

        var copy = indexPolicyCropVarietyCriteria.copy();

        assertThat(indexPolicyCropVarietyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyCropVarietyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPolicyCropVarietyCriteria = new IndexPolicyCropVarietyCriteria();

        assertThat(indexPolicyCropVarietyCriteria).hasToString("IndexPolicyCropVarietyCriteria{}");
    }

    private static void setAllFilters(IndexPolicyCropVarietyCriteria indexPolicyCropVarietyCriteria) {
        indexPolicyCropVarietyCriteria.id();
        indexPolicyCropVarietyCriteria.name();
        indexPolicyCropVarietyCriteria.noOfStages();
        indexPolicyCropVarietyCriteria.image();
        indexPolicyCropVarietyCriteria.description();
        indexPolicyCropVarietyCriteria.addedBy();
        indexPolicyCropVarietyCriteria.createdAt();
        indexPolicyCropVarietyCriteria.distinct();
    }

    private static Condition<IndexPolicyCropVarietyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNoOfStages()) &&
                condition.apply(criteria.getImage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPolicyCropVarietyCriteria> copyFiltersAre(
        IndexPolicyCropVarietyCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNoOfStages(), copy.getNoOfStages()) &&
                condition.apply(criteria.getImage(), copy.getImage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAddedBy(), copy.getAddedBy()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
