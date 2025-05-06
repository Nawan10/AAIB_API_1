package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCoverages} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.InsuranceCultivatedLandCoveragesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-cultivated-land-coverages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCultivatedLandCoveragesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter converageAmount;

    private BooleanFilter isSelect;

    private InstantFilter createdAt;

    private StringFilter addedBy;

    private LongFilter insuranceCultivatedLandId;

    private LongFilter indexCoverageId;

    private Boolean distinct;

    public InsuranceCultivatedLandCoveragesCriteria() {}

    public InsuranceCultivatedLandCoveragesCriteria(InsuranceCultivatedLandCoveragesCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.converageAmount = other.optionalConverageAmount().map(DoubleFilter::copy).orElse(null);
        this.isSelect = other.optionalIsSelect().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.insuranceCultivatedLandId = other.optionalInsuranceCultivatedLandId().map(LongFilter::copy).orElse(null);
        this.indexCoverageId = other.optionalIndexCoverageId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsuranceCultivatedLandCoveragesCriteria copy() {
        return new InsuranceCultivatedLandCoveragesCriteria(this);
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

    public DoubleFilter getConverageAmount() {
        return converageAmount;
    }

    public Optional<DoubleFilter> optionalConverageAmount() {
        return Optional.ofNullable(converageAmount);
    }

    public DoubleFilter converageAmount() {
        if (converageAmount == null) {
            setConverageAmount(new DoubleFilter());
        }
        return converageAmount;
    }

    public void setConverageAmount(DoubleFilter converageAmount) {
        this.converageAmount = converageAmount;
    }

    public BooleanFilter getIsSelect() {
        return isSelect;
    }

    public Optional<BooleanFilter> optionalIsSelect() {
        return Optional.ofNullable(isSelect);
    }

    public BooleanFilter isSelect() {
        if (isSelect == null) {
            setIsSelect(new BooleanFilter());
        }
        return isSelect;
    }

    public void setIsSelect(BooleanFilter isSelect) {
        this.isSelect = isSelect;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
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

    public LongFilter getInsuranceCultivatedLandId() {
        return insuranceCultivatedLandId;
    }

    public Optional<LongFilter> optionalInsuranceCultivatedLandId() {
        return Optional.ofNullable(insuranceCultivatedLandId);
    }

    public LongFilter insuranceCultivatedLandId() {
        if (insuranceCultivatedLandId == null) {
            setInsuranceCultivatedLandId(new LongFilter());
        }
        return insuranceCultivatedLandId;
    }

    public void setInsuranceCultivatedLandId(LongFilter insuranceCultivatedLandId) {
        this.insuranceCultivatedLandId = insuranceCultivatedLandId;
    }

    public LongFilter getIndexCoverageId() {
        return indexCoverageId;
    }

    public Optional<LongFilter> optionalIndexCoverageId() {
        return Optional.ofNullable(indexCoverageId);
    }

    public LongFilter indexCoverageId() {
        if (indexCoverageId == null) {
            setIndexCoverageId(new LongFilter());
        }
        return indexCoverageId;
    }

    public void setIndexCoverageId(LongFilter indexCoverageId) {
        this.indexCoverageId = indexCoverageId;
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
        final InsuranceCultivatedLandCoveragesCriteria that = (InsuranceCultivatedLandCoveragesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(converageAmount, that.converageAmount) &&
            Objects.equals(isSelect, that.isSelect) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(insuranceCultivatedLandId, that.insuranceCultivatedLandId) &&
            Objects.equals(indexCoverageId, that.indexCoverageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, converageAmount, isSelect, createdAt, addedBy, insuranceCultivatedLandId, indexCoverageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCultivatedLandCoveragesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalConverageAmount().map(f -> "converageAmount=" + f + ", ").orElse("") +
            optionalIsSelect().map(f -> "isSelect=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalInsuranceCultivatedLandId().map(f -> "insuranceCultivatedLandId=" + f + ", ").orElse("") +
            optionalIndexCoverageId().map(f -> "indexCoverageId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
