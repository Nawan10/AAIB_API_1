package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A InsurancePolicyDamage.
 */
@Entity
@Table(name = "insurance_policy_damage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePolicyDamage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "is_free")
    private Integer isFree;

    @Column(name = "is_paid")
    private Integer isPaid;

    @ManyToOne(fetch = FetchType.EAGER)
    private InsurancePolicy insurancePolicy;

    @ManyToOne(fetch = FetchType.EAGER)
    private InsurancePolicyDamageCultivatedLandDamageReason damageReason;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsurancePolicyDamage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public InsurancePolicyDamage percentage(Double percentage) {
        this.setPercentage(percentage);
        return this;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Integer getIsFree() {
        return this.isFree;
    }

    public InsurancePolicyDamage isFree(Integer isFree) {
        this.setIsFree(isFree);
        return this;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getIsPaid() {
        return this.isPaid;
    }

    public InsurancePolicyDamage isPaid(Integer isPaid) {
        this.setIsPaid(isPaid);
        return this;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public InsurancePolicy getInsurancePolicy() {
        return this.insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public InsurancePolicyDamage insurancePolicy(InsurancePolicy insurancePolicy) {
        this.setInsurancePolicy(insurancePolicy);
        return this;
    }

    public InsurancePolicyDamageCultivatedLandDamageReason getDamageReason() {
        return this.damageReason;
    }

    public void setDamageReason(InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason) {
        this.damageReason = insurancePolicyDamageCultivatedLandDamageReason;
    }

    public InsurancePolicyDamage damageReason(
        InsurancePolicyDamageCultivatedLandDamageReason insurancePolicyDamageCultivatedLandDamageReason
    ) {
        this.setDamageReason(insurancePolicyDamageCultivatedLandDamageReason);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsurancePolicyDamage)) {
            return false;
        }
        return getId() != null && getId().equals(((InsurancePolicyDamage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancePolicyDamage{" +
            "id=" + getId() +
            ", percentage=" + getPercentage() +
            ", isFree=" + getIsFree() +
            ", isPaid=" + getIsPaid() +
            "}";
    }
}
