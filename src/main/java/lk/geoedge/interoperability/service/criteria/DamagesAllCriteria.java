package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.DamagesAll} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.DamagesAllResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /damages-alls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DamagesAllCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter damageName;

    private StringFilter damageCode;

    private StringFilter damageFamily;

    private StringFilter damageGenus;

    private StringFilter damageSpecies;

    private LocalDateFilter createdAt;

    private StringFilter addedBy;

    private LongFilter damageCategoryId;

    private LongFilter damageTypeId;

    private Boolean distinct;

    public DamagesAllCriteria() {}

    public DamagesAllCriteria(DamagesAllCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.damageName = other.optionalDamageName().map(StringFilter::copy).orElse(null);
        this.damageCode = other.optionalDamageCode().map(StringFilter::copy).orElse(null);
        this.damageFamily = other.optionalDamageFamily().map(StringFilter::copy).orElse(null);
        this.damageGenus = other.optionalDamageGenus().map(StringFilter::copy).orElse(null);
        this.damageSpecies = other.optionalDamageSpecies().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.addedBy = other.optionalAddedBy().map(StringFilter::copy).orElse(null);
        this.damageCategoryId = other.optionalDamageCategoryId().map(LongFilter::copy).orElse(null);
        this.damageTypeId = other.optionalDamageTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DamagesAllCriteria copy() {
        return new DamagesAllCriteria(this);
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

    public StringFilter getDamageName() {
        return damageName;
    }

    public Optional<StringFilter> optionalDamageName() {
        return Optional.ofNullable(damageName);
    }

    public StringFilter damageName() {
        if (damageName == null) {
            setDamageName(new StringFilter());
        }
        return damageName;
    }

    public void setDamageName(StringFilter damageName) {
        this.damageName = damageName;
    }

    public StringFilter getDamageCode() {
        return damageCode;
    }

    public Optional<StringFilter> optionalDamageCode() {
        return Optional.ofNullable(damageCode);
    }

    public StringFilter damageCode() {
        if (damageCode == null) {
            setDamageCode(new StringFilter());
        }
        return damageCode;
    }

    public void setDamageCode(StringFilter damageCode) {
        this.damageCode = damageCode;
    }

    public StringFilter getDamageFamily() {
        return damageFamily;
    }

    public Optional<StringFilter> optionalDamageFamily() {
        return Optional.ofNullable(damageFamily);
    }

    public StringFilter damageFamily() {
        if (damageFamily == null) {
            setDamageFamily(new StringFilter());
        }
        return damageFamily;
    }

    public void setDamageFamily(StringFilter damageFamily) {
        this.damageFamily = damageFamily;
    }

    public StringFilter getDamageGenus() {
        return damageGenus;
    }

    public Optional<StringFilter> optionalDamageGenus() {
        return Optional.ofNullable(damageGenus);
    }

    public StringFilter damageGenus() {
        if (damageGenus == null) {
            setDamageGenus(new StringFilter());
        }
        return damageGenus;
    }

    public void setDamageGenus(StringFilter damageGenus) {
        this.damageGenus = damageGenus;
    }

    public StringFilter getDamageSpecies() {
        return damageSpecies;
    }

    public Optional<StringFilter> optionalDamageSpecies() {
        return Optional.ofNullable(damageSpecies);
    }

    public StringFilter damageSpecies() {
        if (damageSpecies == null) {
            setDamageSpecies(new StringFilter());
        }
        return damageSpecies;
    }

    public void setDamageSpecies(StringFilter damageSpecies) {
        this.damageSpecies = damageSpecies;
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
        final DamagesAllCriteria that = (DamagesAllCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(damageName, that.damageName) &&
            Objects.equals(damageCode, that.damageCode) &&
            Objects.equals(damageFamily, that.damageFamily) &&
            Objects.equals(damageGenus, that.damageGenus) &&
            Objects.equals(damageSpecies, that.damageSpecies) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(addedBy, that.addedBy) &&
            Objects.equals(damageCategoryId, that.damageCategoryId) &&
            Objects.equals(damageTypeId, that.damageTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            damageName,
            damageCode,
            damageFamily,
            damageGenus,
            damageSpecies,
            createdAt,
            addedBy,
            damageCategoryId,
            damageTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DamagesAllCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDamageName().map(f -> "damageName=" + f + ", ").orElse("") +
            optionalDamageCode().map(f -> "damageCode=" + f + ", ").orElse("") +
            optionalDamageFamily().map(f -> "damageFamily=" + f + ", ").orElse("") +
            optionalDamageGenus().map(f -> "damageGenus=" + f + ", ").orElse("") +
            optionalDamageSpecies().map(f -> "damageSpecies=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAddedBy().map(f -> "addedBy=" + f + ", ").orElse("") +
            optionalDamageCategoryId().map(f -> "damageCategoryId=" + f + ", ").orElse("") +
            optionalDamageTypeId().map(f -> "damageTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
