package lk.geoedge.interoperability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A InsuranceCultivatedLandCoverages.
 */
@Entity
@Table(name = "insurance_cultivated_land_coverages")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsuranceCultivatedLandCoverages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "converage_amount")
    private Double converageAmount;

    @Column(name = "is_select")
    private Boolean isSelect;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "damageReason" }, allowSetters = true)
    private IndexCoverages indexCoverage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsuranceCultivatedLandCoverages id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getConverageAmount() {
        return this.converageAmount;
    }

    public InsuranceCultivatedLandCoverages converageAmount(Double converageAmount) {
        this.setConverageAmount(converageAmount);
        return this;
    }

    public void setConverageAmount(Double converageAmount) {
        this.converageAmount = converageAmount;
    }

    public Boolean getIsSelect() {
        return this.isSelect;
    }

    public InsuranceCultivatedLandCoverages isSelect(Boolean isSelect) {
        this.setIsSelect(isSelect);
        return this;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public InsuranceCultivatedLandCoverages createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public InsuranceCultivatedLandCoverages addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public InsuranceCultivatedLandCoveragesInsuranceCultivatedLand getInsuranceCultivatedLand() {
        return this.insuranceCultivatedLand;
    }

    public void setInsuranceCultivatedLand(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        this.insuranceCultivatedLand = insuranceCultivatedLandCoveragesInsuranceCultivatedLand;
    }

    public InsuranceCultivatedLandCoverages insuranceCultivatedLand(
        InsuranceCultivatedLandCoveragesInsuranceCultivatedLand insuranceCultivatedLandCoveragesInsuranceCultivatedLand
    ) {
        this.setInsuranceCultivatedLand(insuranceCultivatedLandCoveragesInsuranceCultivatedLand);
        return this;
    }

    public IndexCoverages getIndexCoverage() {
        return this.indexCoverage;
    }

    public void setIndexCoverage(IndexCoverages indexCoverages) {
        this.indexCoverage = indexCoverages;
    }

    public InsuranceCultivatedLandCoverages indexCoverage(IndexCoverages indexCoverages) {
        this.setIndexCoverage(indexCoverages);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsuranceCultivatedLandCoverages)) {
            return false;
        }
        return getId() != null && getId().equals(((InsuranceCultivatedLandCoverages) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsuranceCultivatedLandCoverages{" +
            "id=" + getId() +
            ", converageAmount=" + getConverageAmount() +
            ", isSelect='" + getIsSelect() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
