package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.InsurancePolicyDamageCultivatedLandDamageReason} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.InsurancePolicyDamageCultivatedLandDamageReasonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-policy-damage-cultivated-land-damage-reasons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePolicyDamageCultivatedLandDamageReasonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter damageCategoryId;

    private IntegerFilter damageTypeId;

    private Boolean distinct;

    public InsurancePolicyDamageCultivatedLandDamageReasonCriteria() {}

    public InsurancePolicyDamageCultivatedLandDamageReasonCriteria(InsurancePolicyDamageCultivatedLandDamageReasonCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.damageCategoryId = other.optionalDamageCategoryId().map(IntegerFilter::copy).orElse(null);
        this.damageTypeId = other.optionalDamageTypeId().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsurancePolicyDamageCultivatedLandDamageReasonCriteria copy() {
        return new InsurancePolicyDamageCultivatedLandDamageReasonCriteria(this);
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

    public IntegerFilter getDamageCategoryId() {
        return damageCategoryId;
    }

    public Optional<IntegerFilter> optionalDamageCategoryId() {
        return Optional.ofNullable(damageCategoryId);
    }

    public IntegerFilter damageCategoryId() {
        if (damageCategoryId == null) {
            setDamageCategoryId(new IntegerFilter());
        }
        return damageCategoryId;
    }

    public void setDamageCategoryId(IntegerFilter damageCategoryId) {
        this.damageCategoryId = damageCategoryId;
    }

    public IntegerFilter getDamageTypeId() {
        return damageTypeId;
    }

    public Optional<IntegerFilter> optionalDamageTypeId() {
        return Optional.ofNullable(damageTypeId);
    }

    public IntegerFilter damageTypeId() {
        if (damageTypeId == null) {
            setDamageTypeId(new IntegerFilter());
        }
        return damageTypeId;
    }

    public void setDamageTypeId(IntegerFilter damageTypeId) {
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
        final InsurancePolicyDamageCultivatedLandDamageReasonCriteria that = (InsurancePolicyDamageCultivatedLandDamageReasonCriteria) o;
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
        return "InsurancePolicyDamageCultivatedLandDamageReasonCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDamageCategoryId().map(f -> "damageCategoryId=" + f + ", ").orElse("") +
            optionalDamageTypeId().map(f -> "damageTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
