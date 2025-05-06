package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.InsurancePolicyDamage} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.InsurancePolicyDamageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-policy-damages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePolicyDamageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter percentage;

    private IntegerFilter isFree;

    private IntegerFilter isPaid;

    private LongFilter insurancePolicyId;

    private LongFilter damageReasonId;

    private Boolean distinct;

    public InsurancePolicyDamageCriteria() {}

    public InsurancePolicyDamageCriteria(InsurancePolicyDamageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.percentage = other.optionalPercentage().map(DoubleFilter::copy).orElse(null);
        this.isFree = other.optionalIsFree().map(IntegerFilter::copy).orElse(null);
        this.isPaid = other.optionalIsPaid().map(IntegerFilter::copy).orElse(null);
        this.insurancePolicyId = other.optionalInsurancePolicyId().map(LongFilter::copy).orElse(null);
        this.damageReasonId = other.optionalDamageReasonId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsurancePolicyDamageCriteria copy() {
        return new InsurancePolicyDamageCriteria(this);
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

    public DoubleFilter getPercentage() {
        return percentage;
    }

    public Optional<DoubleFilter> optionalPercentage() {
        return Optional.ofNullable(percentage);
    }

    public DoubleFilter percentage() {
        if (percentage == null) {
            setPercentage(new DoubleFilter());
        }
        return percentage;
    }

    public void setPercentage(DoubleFilter percentage) {
        this.percentage = percentage;
    }

    public IntegerFilter getIsFree() {
        return isFree;
    }

    public Optional<IntegerFilter> optionalIsFree() {
        return Optional.ofNullable(isFree);
    }

    public IntegerFilter isFree() {
        if (isFree == null) {
            setIsFree(new IntegerFilter());
        }
        return isFree;
    }

    public void setIsFree(IntegerFilter isFree) {
        this.isFree = isFree;
    }

    public IntegerFilter getIsPaid() {
        return isPaid;
    }

    public Optional<IntegerFilter> optionalIsPaid() {
        return Optional.ofNullable(isPaid);
    }

    public IntegerFilter isPaid() {
        if (isPaid == null) {
            setIsPaid(new IntegerFilter());
        }
        return isPaid;
    }

    public void setIsPaid(IntegerFilter isPaid) {
        this.isPaid = isPaid;
    }

    public LongFilter getInsurancePolicyId() {
        return insurancePolicyId;
    }

    public Optional<LongFilter> optionalInsurancePolicyId() {
        return Optional.ofNullable(insurancePolicyId);
    }

    public LongFilter insurancePolicyId() {
        if (insurancePolicyId == null) {
            setInsurancePolicyId(new LongFilter());
        }
        return insurancePolicyId;
    }

    public void setInsurancePolicyId(LongFilter insurancePolicyId) {
        this.insurancePolicyId = insurancePolicyId;
    }

    public LongFilter getDamageReasonId() {
        return damageReasonId;
    }

    public Optional<LongFilter> optionalDamageReasonId() {
        return Optional.ofNullable(damageReasonId);
    }

    public LongFilter damageReasonId() {
        if (damageReasonId == null) {
            setDamageReasonId(new LongFilter());
        }
        return damageReasonId;
    }

    public void setDamageReasonId(LongFilter damageReasonId) {
        this.damageReasonId = damageReasonId;
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
        final InsurancePolicyDamageCriteria that = (InsurancePolicyDamageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(percentage, that.percentage) &&
            Objects.equals(isFree, that.isFree) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(insurancePolicyId, that.insurancePolicyId) &&
            Objects.equals(damageReasonId, that.damageReasonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, percentage, isFree, isPaid, insurancePolicyId, damageReasonId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancePolicyDamageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPercentage().map(f -> "percentage=" + f + ", ").orElse("") +
            optionalIsFree().map(f -> "isFree=" + f + ", ").orElse("") +
            optionalIsPaid().map(f -> "isPaid=" + f + ", ").orElse("") +
            optionalInsurancePolicyId().map(f -> "insurancePolicyId=" + f + ", ").orElse("") +
            optionalDamageReasonId().map(f -> "damageReasonId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
