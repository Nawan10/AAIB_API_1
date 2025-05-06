package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.InsuranceCultivatedLandCultivatedLand} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.InsuranceCultivatedLandCultivatedLandResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-cultivated-land-cultivated-lands?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCultivatedLandCultivatedLandCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter landStatus;

    private DoubleFilter urea;

    private DoubleFilter mop;

    private DoubleFilter tsp;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private Boolean distinct;

    public InsuranceCultivatedLandCultivatedLandCriteria() {}

    public InsuranceCultivatedLandCultivatedLandCriteria(InsuranceCultivatedLandCultivatedLandCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.landStatus = other.optionalLandStatus().map(StringFilter::copy).orElse(null);
        this.urea = other.optionalUrea().map(DoubleFilter::copy).orElse(null);
        this.mop = other.optionalMop().map(DoubleFilter::copy).orElse(null);
        this.tsp = other.optionalTsp().map(DoubleFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsuranceCultivatedLandCultivatedLandCriteria copy() {
        return new InsuranceCultivatedLandCultivatedLandCriteria(this);
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

    public StringFilter getLandStatus() {
        return landStatus;
    }

    public Optional<StringFilter> optionalLandStatus() {
        return Optional.ofNullable(landStatus);
    }

    public StringFilter landStatus() {
        if (landStatus == null) {
            setLandStatus(new StringFilter());
        }
        return landStatus;
    }

    public void setLandStatus(StringFilter landStatus) {
        this.landStatus = landStatus;
    }

    public DoubleFilter getUrea() {
        return urea;
    }

    public Optional<DoubleFilter> optionalUrea() {
        return Optional.ofNullable(urea);
    }

    public DoubleFilter urea() {
        if (urea == null) {
            setUrea(new DoubleFilter());
        }
        return urea;
    }

    public void setUrea(DoubleFilter urea) {
        this.urea = urea;
    }

    public DoubleFilter getMop() {
        return mop;
    }

    public Optional<DoubleFilter> optionalMop() {
        return Optional.ofNullable(mop);
    }

    public DoubleFilter mop() {
        if (mop == null) {
            setMop(new DoubleFilter());
        }
        return mop;
    }

    public void setMop(DoubleFilter mop) {
        this.mop = mop;
    }

    public DoubleFilter getTsp() {
        return tsp;
    }

    public Optional<DoubleFilter> optionalTsp() {
        return Optional.ofNullable(tsp);
    }

    public DoubleFilter tsp() {
        if (tsp == null) {
            setTsp(new DoubleFilter());
        }
        return tsp;
    }

    public void setTsp(DoubleFilter tsp) {
        this.tsp = tsp;
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
        final InsuranceCultivatedLandCultivatedLandCriteria that = (InsuranceCultivatedLandCultivatedLandCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(landStatus, that.landStatus) &&
            Objects.equals(urea, that.urea) &&
            Objects.equals(mop, that.mop) &&
            Objects.equals(tsp, that.tsp) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, landStatus, urea, mop, tsp, createdAt, addedBy, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCultivatedLandCultivatedLandCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLandStatus().map(f -> "landStatus=" + f + ", ").orElse("") +
            optionalUrea().map(f -> "urea=" + f + ", ").orElse("") +
            optionalMop().map(f -> "mop=" + f + ", ").orElse("") +
            optionalTsp().map(f -> "tsp=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
