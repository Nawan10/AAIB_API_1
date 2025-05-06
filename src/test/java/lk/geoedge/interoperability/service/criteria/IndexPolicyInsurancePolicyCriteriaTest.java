package lk.geoedge.interoperability.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndexPolicyInsurancePolicyCriteriaTest {

    @Test
    void newIndexPolicyInsurancePolicyCriteriaHasAllFiltersNullTest() {
        var indexPolicyInsurancePolicyCriteria = new IndexPolicyInsurancePolicyCriteria();
        assertThat(indexPolicyInsurancePolicyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void indexPolicyInsurancePolicyCriteriaFluentMethodsCreatesFiltersTest() {
        var indexPolicyInsurancePolicyCriteria = new IndexPolicyInsurancePolicyCriteria();

        setAllFilters(indexPolicyInsurancePolicyCriteria);

        assertThat(indexPolicyInsurancePolicyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void indexPolicyInsurancePolicyCriteriaCopyCreatesNullFilterTest() {
        var indexPolicyInsurancePolicyCriteria = new IndexPolicyInsurancePolicyCriteria();
        var copy = indexPolicyInsurancePolicyCriteria.copy();

        assertThat(indexPolicyInsurancePolicyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyInsurancePolicyCriteria)
        );
    }

    @Test
    void indexPolicyInsurancePolicyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var indexPolicyInsurancePolicyCriteria = new IndexPolicyInsurancePolicyCriteria();
        setAllFilters(indexPolicyInsurancePolicyCriteria);

        var copy = indexPolicyInsurancePolicyCriteria.copy();

        assertThat(indexPolicyInsurancePolicyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(indexPolicyInsurancePolicyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var indexPolicyInsurancePolicyCriteria = new IndexPolicyInsurancePolicyCriteria();

        assertThat(indexPolicyInsurancePolicyCriteria).hasToString("IndexPolicyInsurancePolicyCriteria{}");
    }

    private static void setAllFilters(IndexPolicyInsurancePolicyCriteria indexPolicyInsurancePolicyCriteria) {
        indexPolicyInsurancePolicyCriteria.id();
        indexPolicyInsurancePolicyCriteria.name();
        indexPolicyInsurancePolicyCriteria.policyNo();
        indexPolicyInsurancePolicyCriteria.isActivate();
        indexPolicyInsurancePolicyCriteria.distinct();
    }

    private static Condition<IndexPolicyInsurancePolicyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPolicyNo()) &&
                condition.apply(criteria.getIsActivate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndexPolicyInsurancePolicyCriteria> copyFiltersAre(
        IndexPolicyInsurancePolicyCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPolicyNo(), copy.getPolicyNo()) &&
                condition.apply(criteria.getIsActivate(), copy.getIsActivate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
