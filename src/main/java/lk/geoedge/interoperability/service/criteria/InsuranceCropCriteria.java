package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.InsuranceCrop} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.InsuranceCropResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-crops?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCropCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter policyId;

    private DoubleFilter yield;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private LongFilter cropId;

    private Boolean distinct;

    public InsuranceCropCriteria() {}

    public InsuranceCropCriteria(InsuranceCropCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.policyId = other.optionalPolicyId().map(StringFilter::copy).orElse(null);
        this.yield = other.optionalYield().map(DoubleFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.cropId = other.optionalCropId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsuranceCropCriteria copy() {
        return new InsuranceCropCriteria(this);
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

    public StringFilter getPolicyId() {
        return policyId;
    }

    public Optional<StringFilter> optionalPolicyId() {
        return Optional.ofNullable(policyId);
    }

    public StringFilter policyId() {
        if (policyId == null) {
            setPolicyId(new StringFilter());
        }
        return policyId;
    }

    public void setPolicyId(StringFilter policyId) {
        this.policyId = policyId;
    }

    public DoubleFilter getYield() {
        return yield;
    }

    public Optional<DoubleFilter> optionalYield() {
        return Optional.ofNullable(yield);
    }

    public DoubleFilter yield() {
        if (yield == null) {
            setYield(new DoubleFilter());
        }
        return yield;
    }

    public void setYield(DoubleFilter yield) {
        this.yield = yield;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new LocalDateFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getAddedBy() {
        return addedBy;
    }

    public Optional<StringFilter> optionalAddedBy() {
        return Optional.ofNullable(addedBy);
    }

    public StringFilter addedBy() {
        if (addedBy == null) {
            setAddedBy(new StringFilter());
        }
        return addedBy;
    }

    public void setAddedBy(StringFilter addedBy) {
        this.addedBy = addedBy;
    }

    public LongFilter getCropId() {
        return cropId;
    }

    public Optional<LongFilter> optionalCropId() {
        return Optional.ofNullable(cropId);
    }

    public LongFilter cropId() {
        if (cropId == null) {
            setCropId(new LongFilter());
        }
        return cropId;
    }

    public void setCropId(LongFilter cropId) {
        this.cropId = cropId;
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
        final InsuranceCropCriteria that = (InsuranceCropCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(policyId, that.policyId) &&
            Objects.equals(yield, that.yield) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(cropId, that.cropId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, policyId, yield, createdAt, addedBy, cropId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCropCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPolicyId().map(f -> "policyId=" + f + ", ").orElse("") +
            optionalYield().map(f -> "yield=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalCropId().map(f -> "cropId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
