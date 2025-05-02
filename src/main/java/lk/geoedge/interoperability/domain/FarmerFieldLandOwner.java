package lk.geoedge.interoperability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A FarmerFieldLandOwner.
 */
@Entity
@Table(name = "farmer_field_land_owner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FarmerFieldLandOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "crop" }, allowSetters = true)
    private FarmerFieldOwner farmerFieldOwner;

    @ManyToOne(fetch = FetchType.EAGER)
    private FarmerFieldLandOwnerFarmer farmer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FarmerFieldLandOwner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public FarmerFieldLandOwner createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public FarmerFieldLandOwner addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public FarmerFieldOwner getFarmerFieldOwner() {
        return this.farmerFieldOwner;
    }

    public void setFarmerFieldOwner(FarmerFieldOwner farmerFieldOwner) {
        this.farmerFieldOwner = farmerFieldOwner;
    }

    public FarmerFieldLandOwner farmerFieldOwner(FarmerFieldOwner farmerFieldOwner) {
        this.setFarmerFieldOwner(farmerFieldOwner);
        return this;
    }

    public FarmerFieldLandOwnerFarmer getFarmer() {
        return this.farmer;
    }

    public void setFarmer(FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer) {
        this.farmer = farmerFieldLandOwnerFarmer;
    }

    public FarmerFieldLandOwner farmer(FarmerFieldLandOwnerFarmer farmerFieldLandOwnerFarmer) {
        this.setFarmer(farmerFieldLandOwnerFarmer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FarmerFieldLandOwner)) {
            return false;
        }
        return getId() != null && getId().equals(((FarmerFieldLandOwner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FarmerFieldLandOwner{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
