package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A IndexPolicyInsurancePolicy.
 */
@Entity
@Table(name = "insurance_policy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicyInsurancePolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "policy_no")
    private String policyNo;

    @Column(name = "is_activate")
    private Integer isActivate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexPolicyInsurancePolicy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IndexPolicyInsurancePolicy name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPolicyNo() {
        return this.policyNo;
    }

    public IndexPolicyInsurancePolicy policyNo(String policyNo) {
        this.setPolicyNo(policyNo);
        return this;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Integer getIsActivate() {
        return this.isActivate;
    }

    public IndexPolicyInsurancePolicy isActivate(Integer isActivate) {
        this.setIsActivate(isActivate);
        return this;
    }

    public void setIsActivate(Integer isActivate) {
        this.isActivate = isActivate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexPolicyInsurancePolicy)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexPolicyInsurancePolicy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicyInsurancePolicy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", policyNo='" + getPolicyNo() + "'" +
            ", isActivate=" + getIsActivate() +
            "}";
    }
}
