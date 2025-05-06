package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.CultivatedLandDamageReason} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.CultivatedLandDamageReasonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cultivated-land-damage-reasons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandDamageReasonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter damageCategoryId;

    private LongFilter damageTypeId;

    private Boolean distinct;

    public CultivatedLandDamageReasonCriteria() {}

    public CultivatedLandDamageReasonCriteria(CultivatedLandDamageReasonCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.damageCategoryId = other.optionalDamageCategoryId().map(LongFilter::copy).orElse(null);
        this.damageTypeId = other.optionalDamageTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CultivatedLandDamageReasonCriteria copy() {
        return new CultivatedLandDamageReasonCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getDamageCategoryId() {
        return damageCategoryId;
    }

    public Optional<LongFilter> optionalDamageCategoryId() {
        return Optional.ofNullable(damageCategoryId);
    }

    public LongFilter damageCategoryId() {
        if (damageCategoryId == null) {
            setDamageCategoryId(new LongFilter());
        }
        return damageCategoryId;
    }

    public void setDamageCategoryId(LongFilter damageCategoryId) {
        this.damageCategoryId = damageCategoryId;
    }

    public LongFilter getDamageTypeId() {
        return damageTypeId;
    }

    public Optional<LongFilter> optionalDamageTypeId() {
        return Optional.ofNullable(damageTypeId);
    }

    public LongFilter damageTypeId() {
        if (damageTypeId == null) {
            setDamageTypeId(new LongFilter());
        }
        return damageTypeId;
    }

    public void setDamageTypeId(LongFilter damageTypeId) {
        this.damageTypeId = damageTypeId;
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
        final CultivatedLandDamageReasonCriteria that = (CultivatedLandDamageReasonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(damageCategoryId, that.damageCategoryId) &&
            Objects.equals(damageTypeId, that.damageTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, damageCategoryId, damageTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandDamageReasonCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDamageCategoryId().map(f -> "damageCategoryId=" + f + ", ").orElse("") +
            optionalDamageTypeId().map(f -> "damageTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
