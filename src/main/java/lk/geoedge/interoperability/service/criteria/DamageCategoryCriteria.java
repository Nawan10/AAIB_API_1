package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.DamageCategory} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.DamageCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /damage-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DamageCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter categoryName;

    private LongFilter damageId;

    private Boolean distinct;

    public DamageCategoryCriteria() {}

    public DamageCategoryCriteria(DamageCategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.categoryName = other.optionalCategoryName().map(StringFilter::copy).orElse(null);
        this.damageId = other.optionalDamageId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DamageCategoryCriteria copy() {
        return new DamageCategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public Optional<StringFilter> optionalCategoryName() {
        return Optional.ofNullable(categoryName);
    }

    public StringFilter categoryName() {
        if (categoryName == null) {
            setCategoryName(new StringFilter());
        }
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
    }

    public LongFilter getDamageId() {
        return damageId;
    }

    public Optional<LongFilter> optionalDamageId() {
        return Optional.ofNullable(damageId);
    }

    public LongFilter damageId() {
        if (damageId == null) {
            setDamageId(new LongFilter());
        }
        return damageId;
    }

    public void setDamageId(LongFilter damageId) {
        this.damageId = damageId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DamageCategoryCriteria that = (DamageCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(damageId, that.damageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryName, damageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DamageCategoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCategoryName().map(f -> "categoryName=" + f + ", ").orElse("") +
            optionalDamageId().map(f -> "damageId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
