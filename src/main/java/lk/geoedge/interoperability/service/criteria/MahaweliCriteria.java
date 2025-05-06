package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.Mahaweli} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.MahaweliResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mahawelis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MahaweliCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter mahaweli;

    private StringFilter code;

    private IntegerFilter addedBy;

    private LocalDateFilter addedDate;

    private Boolean distinct;

    public MahaweliCriteria() {}

    public MahaweliCriteria(MahaweliCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.mahaweli = other.optionalMahaweli().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(IntegerFilter::copy).orElse(null);
        this.addedDate = other.optionalAddedDate().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MahaweliCriteria copy() {
        return new MahaweliCriteria(this);
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

    public StringFilter getMahaweli() {
        return mahaweli;
    }

    public Optional<StringFilter> optionalMahaweli() {
        return Optional.ofNullable(mahaweli);
    }

    public StringFilter mahaweli() {
        if (mahaweli == null) {
            setMahaweli(new StringFilter());
        }
        return mahaweli;
    }

    public void setMahaweli(StringFilter mahaweli) {
        this.mahaweli = mahaweli;
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

    public LocalDateFilter getAddedDate() {
        return addedDate;
    }

    public Optional<LocalDateFilter> optionalAddedDate() {
        return Optional.ofNullable(addedDate);
    }

    public LocalDateFilter addedDate() {
        if (addedDate == null) {
            setAddedDate(new LocalDateFilter());
        }
        return addedDate;
    }

    public void setAddedDate(LocalDateFilter addedDate) {
        this.addedDate = addedDate;
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
        final MahaweliCriteria that = (MahaweliCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mahaweli, that.mahaweli) &&
            Objects.equals(code, that.code) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mahaweli, code, addedBy, addedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MahaweliCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMahaweli().map(f -> "mahaweli=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalAddedDate().map(f -> "addedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
