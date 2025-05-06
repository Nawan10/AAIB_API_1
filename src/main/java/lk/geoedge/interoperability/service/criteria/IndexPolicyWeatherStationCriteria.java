package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.IndexPolicyWeatherStation} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.IndexPolicyWeatherStationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /index-policy-weather-stations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicyWeatherStationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private IntegerFilter gnId;

    private IntegerFilter districtId;

    private IntegerFilter provinceId;

    private IntegerFilter dsId;

    private IntegerFilter addedBy;

    private LocalDateFilter createdAt;

    private Boolean distinct;

    public IndexPolicyWeatherStationCriteria() {}

    public IndexPolicyWeatherStationCriteria(IndexPolicyWeatherStationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(DoubleFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(DoubleFilter::copy).orElse(null);
        this.gnId = other.optionalGnId().map(IntegerFilter::copy).orElse(null);
        this.districtId = other.optionalDistrictId().map(IntegerFilter::copy).orElse(null);
        this.provinceId = other.optionalProvinceId().map(IntegerFilter::copy).orElse(null);
        this.dsId = other.optionalDsId().map(IntegerFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(IntegerFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IndexPolicyWeatherStationCriteria copy() {
        return new IndexPolicyWeatherStationCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public Optional<DoubleFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            setLatitude(new DoubleFilter());
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public Optional<DoubleFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            setLongitude(new DoubleFilter());
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public IntegerFilter getGnId() {
        return gnId;
    }

    public Optional<IntegerFilter> optionalGnId() {
        return Optional.ofNullable(gnId);
    }

    public IntegerFilter gnId() {
        if (gnId == null) {
            setGnId(new IntegerFilter());
        }
        return gnId;
    }

    public void setGnId(IntegerFilter gnId) {
        this.gnId = gnId;
    }

    public IntegerFilter getDistrictId() {
        return districtId;
    }

    public Optional<IntegerFilter> optionalDistrictId() {
        return Optional.ofNullable(districtId);
    }

    public IntegerFilter districtId() {
        if (districtId == null) {
            setDistrictId(new IntegerFilter());
        }
        return districtId;
    }

    public void setDistrictId(IntegerFilter districtId) {
        this.districtId = districtId;
    }

    public IntegerFilter getProvinceId() {
        return provinceId;
    }

    public Optional<IntegerFilter> optionalProvinceId() {
        return Optional.ofNullable(provinceId);
    }

    public IntegerFilter provinceId() {
        if (provinceId == null) {
            setProvinceId(new IntegerFilter());
        }
        return provinceId;
    }

    public void setProvinceId(IntegerFilter provinceId) {
        this.provinceId = provinceId;
    }

    public IntegerFilter getDsId() {
        return dsId;
    }

    public Optional<IntegerFilter> optionalDsId() {
        return Optional.ofNullable(dsId);
    }

    public IntegerFilter dsId() {
        if (dsId == null) {
            setDsId(new IntegerFilter());
        }
        return dsId;
    }

    public void setDsId(IntegerFilter dsId) {
        this.dsId = dsId;
    }

    public IntegerFilter getAddedBy() {
        return addedBy;
    }

    public Optional<IntegerFilter> optionalAddedBy() {
        return Optional.ofNullable(addedBy);
    }

    public IntegerFilter addedBy() {
        if (addedBy == null) {
            setAddedBy(new IntegerFilter());
        }
        return addedBy;
    }

    public void setAddedBy(IntegerFilter addedBy) {
        this.addedBy = addedBy;
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
        final IndexPolicyWeatherStationCriteria that = (IndexPolicyWeatherStationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(gnId, that.gnId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(provinceId, that.provinceId) &&
            Objects.equals(dsId, that.dsId) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, latitude, longitude, gnId, districtId, provinceId, dsId, addedBy, createdAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicyWeatherStationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalGnId().map(f -> "gnId=" + f + ", ").orElse("") +
            optionalDistrictId().map(f -> "districtId=" + f + ", ").orElse("") +
            optionalProvinceId().map(f -> "provinceId=" + f + ", ").orElse("") +
            optionalDsId().map(f -> "dsId=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
