package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CultivatedLandFarmers.
 */
@Entity
@Table(name = "cultivated_land_farmers")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLandFarmers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "relation_id")
    private Integer relationId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandFarmersFarmer farmer;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandFarmerLand cultivatedLand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLandFarmers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRelationId() {
        return this.relationId;
    }

    public CultivatedLandFarmers relationId(Integer relationId) {
        this.setRelationId(relationId);
        return this;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CultivatedLandFarmers createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CultivatedLandFarmers addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public CultivatedLandFarmersFarmer getFarmer() {
        return this.farmer;
    }

    public void setFarmer(CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer) {
        this.farmer = cultivatedLandFarmersFarmer;
    }

    public CultivatedLandFarmers farmer(CultivatedLandFarmersFarmer cultivatedLandFarmersFarmer) {
        this.setFarmer(cultivatedLandFarmersFarmer);
        return this;
    }

    public CultivatedLandFarmerLand getCultivatedLand() {
        return this.cultivatedLand;
    }

    public void setCultivatedLand(CultivatedLandFarmerLand cultivatedLandFarmerLand) {
        this.cultivatedLand = cultivatedLandFarmerLand;
    }

    public CultivatedLandFarmers cultivatedLand(CultivatedLandFarmerLand cultivatedLandFarmerLand) {
        this.setCultivatedLand(cultivatedLandFarmerLand);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLandFarmers)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLandFarmers) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLandFarmers{" +
            "id=" + getId() +
            ", relationId=" + getRelationId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
