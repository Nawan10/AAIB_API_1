package lk.geoedge.interoperability.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link lk.geoedge.interoperability.domain.IndexPolicyInsurancePolicy} entity. This class is used
 * in {@link lk.geoedge.interoperability.web.rest.IndexPolicyInsurancePolicyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /index-policy-insurance-policies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicyInsurancePolicyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter policyNo;

    private IntegerFilter isActivate;

    private Boolean distinct;

    public IndexPolicyInsurancePolicyCriteria() {}

    public IndexPolicyInsurancePolicyCriteria(IndexPolicyInsurancePolicyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.policyNo = other.optionalPolicyNo().map(StringFilter::copy).orElse(null);
        this.isActivate = other.optionalIsActivate().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IndexPolicyInsurancePolicyCriteria copy() {
        return new IndexPolicyInsurancePolicyCriteria(this);
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

    public StringFilter getPolicyNo() {
        return policyNo;
    }

    public Optional<StringFilter> optionalPolicyNo() {
        return Optional.ofNullable(policyNo);
    }

    public StringFilter policyNo() {
        if (policyNo == null) {
            setPolicyNo(new StringFilter());
        }
        return policyNo;
    }

    public void setPolicyNo(StringFilter policyNo) {
        this.policyNo = policyNo;
    }

    public IntegerFilter getIsActivate() {
        return isActivate;
    }

    public Optional<IntegerFilter> optionalIsActivate() {
        return Optional.ofNullable(isActivate);
    }

    public IntegerFilter isActivate() {
        if (isActivate == null) {
            setIsActivate(new IntegerFilter());
        }
        return isActivate;
    }

    public void setIsActivate(IntegerFilter isActivate) {
        this.isActivate = isActivate;
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
        final IndexPolicyInsurancePolicyCriteria that = (IndexPolicyInsurancePolicyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(policyNo, that.policyNo) &&
            Objects.equals(isActivate, that.isActivate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, policyNo, isActivate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicyInsurancePolicyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPolicyNo().map(f -> "policyNo=" + f + ", ").orElse("") +
            optionalIsActivate().map(f -> "isActivate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
