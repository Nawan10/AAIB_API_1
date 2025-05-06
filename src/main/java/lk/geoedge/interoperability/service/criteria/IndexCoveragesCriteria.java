package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.IndexCoverages} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.IndexCoveragesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /index-coverages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexCoveragesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter indexProductId;

    private IntegerFilter premiumRate;

    private IntegerFilter isFree;

    private IntegerFilter isPaid;

    private LongFilter damageReasonId;

    private Boolean distinct;

    public IndexCoveragesCriteria() {}

    public IndexCoveragesCriteria(IndexCoveragesCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.indexProductId = other.optionalIndexProductId().map(IntegerFilter::copy).orElse(null);
        this.premiumRate = other.optionalPremiumRate().map(IntegerFilter::copy).orElse(null);
        this.isFree = other.optionalIsFree().map(IntegerFilter::copy).orElse(null);
        this.isPaid = other.optionalIsPaid().map(IntegerFilter::copy).orElse(null);
        this.damageReasonId = other.optionalDamageReasonId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IndexCoveragesCriteria copy() {
        return new IndexCoveragesCriteria(this);
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

    public IntegerFilter getIndexProductId() {
        return indexProductId;
    }

    public Optional<IntegerFilter> optionalIndexProductId() {
        return Optional.ofNullable(indexProductId);
    }

    public IntegerFilter indexProductId() {
        if (indexProductId == null) {
            setIndexProductId(new IntegerFilter());
        }
        return indexProductId;
    }

    public void setIndexProductId(IntegerFilter indexProductId) {
        this.indexProductId = indexProductId;
    }

    public IntegerFilter getPremiumRate() {
        return premiumRate;
    }

    public Optional<IntegerFilter> optionalPremiumRate() {
        return Optional.ofNullable(premiumRate);
    }

    public IntegerFilter premiumRate() {
        if (premiumRate == null) {
            setPremiumRate(new IntegerFilter());
        }
        return premiumRate;
    }

    public void setPremiumRate(IntegerFilter premiumRate) {
        this.premiumRate = premiumRate;
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
        final IndexCoveragesCriteria that = (IndexCoveragesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(indexProductId, that.indexProductId) &&
            Objects.equals(premiumRate, that.premiumRate) &&
            Objects.equals(isFree, that.isFree) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(damageReasonId, that.damageReasonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indexProductId, premiumRate, isFree, isPaid, damageReasonId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexCoveragesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIndexProductId().map(f -> "indexProductId=" + f + ", ").orElse("") +
            optionalPremiumRate().map(f -> "premiumRate=" + f + ", ").orElse("") +
            optionalIsFree().map(f -> "isFree=" + f + ", ").orElse("") +
            optionalIsPaid().map(f -> "isPaid=" + f + ", ").orElse("") +
            optionalDamageReasonId().map(f -> "damageReasonId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
