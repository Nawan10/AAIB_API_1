package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.DamageType} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.DamageTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /damage-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DamageTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter typeName;

    private LongFilter damageId;

    private Boolean distinct;

    public DamageTypeCriteria() {}

    public DamageTypeCriteria(DamageTypeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.typeName = other.optionalTypeName().map(StringFilter::copy).orElse(null);
        this.damageId = other.optionalDamageId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DamageTypeCriteria copy() {
        return new DamageTypeCriteria(this);
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

    public StringFilter getTypeName() {
        return typeName;
    }

    public Optional<StringFilter> optionalTypeName() {
        return Optional.ofNullable(typeName);
    }

    public StringFilter typeName() {
        if (typeName == null) {
            setTypeName(new StringFilter());
        }
        return typeName;
    }

    public void setTypeName(StringFilter typeName) {
        this.typeName = typeName;
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
        final DamageTypeCriteria that = (DamageTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeName, that.typeName) &&
            Objects.equals(damageId, that.damageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName, damageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DamageTypeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTypeName().map(f -> "typeName=" + f + ", ").orElse("") +
            optionalDamageId().map(f -> "damageId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
