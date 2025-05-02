package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A InsuranceCrop.
 */
@Entity
@Table(name = "insurance_crop")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCrop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "policy_id")
    private String policyId;

    @Column(name = "yield")
    private Double yield;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private InsuranceCropCropType crop;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsuranceCrop id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyId() {
        return this.policyId;
    }

    public InsuranceCrop policyId(String policyId) {
        this.setPolicyId(policyId);
        return this;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public Double getYield() {
        return this.yield;
    }

    public InsuranceCrop yield(Double yield) {
        this.setYield(yield);
        return this;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public InsuranceCrop createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public InsuranceCrop addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public InsuranceCropCropType getCrop() {
        return this.crop;
    }

    public void setCrop(InsuranceCropCropType insuranceCropCropType) {
        this.crop = insuranceCropCropType;
    }

    public InsuranceCrop crop(InsuranceCropCropType insuranceCropCropType) {
        this.setCrop(insuranceCropCropType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceCrop)) {
            return false;
        }
        return getId() != null && getId().equals(((InsuranceCrop) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCrop{" +
            "id=" + getId() +
            ", policyId='" + getPolicyId() + "'" +
            ", yield=" + getYield() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
