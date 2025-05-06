package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CultivatedLand.
 */
@Entity
@Table(name = "cultivated_land")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CultivatedLand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "land_status")
    private String landStatus;

    @Column(name = "urea")
    private Double urea;

    @Column(name = "mop")
    private Double mop;

    @Column(name = "tsp")
    private Double tsp;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandFarmerFieldOwner farmField;

    @ManyToOne(fetch = FetchType.EAGER)
    private CultivatedLandSeason season;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CultivatedLand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLandStatus() {
        return this.landStatus;
    }

    public CultivatedLand landStatus(String landStatus) {
        this.setLandStatus(landStatus);
        return this;
    }

    public void setLandStatus(String landStatus) {
        this.landStatus = landStatus;
    }

    public Double getUrea() {
        return this.urea;
    }

    public CultivatedLand urea(Double urea) {
        this.setUrea(urea);
        return this;
    }

    public void setUrea(Double urea) {
        this.urea = urea;
    }

    public Double getMop() {
        return this.mop;
    }

    public CultivatedLand mop(Double mop) {
        this.setMop(mop);
        return this;
    }

    public void setMop(Double mop) {
        this.mop = mop;
    }

    public Double getTsp() {
        return this.tsp;
    }

    public CultivatedLand tsp(Double tsp) {
        this.setTsp(tsp);
        return this;
    }

    public void setTsp(Double tsp) {
        this.tsp = tsp;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CultivatedLand createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CultivatedLand addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public CultivatedLandFarmerFieldOwner getFarmField() {
        return this.farmField;
    }

    public void setFarmField(CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner) {
        this.farmField = cultivatedLandFarmerFieldOwner;
    }

    public CultivatedLand farmField(CultivatedLandFarmerFieldOwner cultivatedLandFarmerFieldOwner) {
        this.setFarmField(cultivatedLandFarmerFieldOwner);
        return this;
    }

    public CultivatedLandSeason getSeason() {
        return this.season;
    }

    public void setSeason(CultivatedLandSeason cultivatedLandSeason) {
        this.season = cultivatedLandSeason;
    }

    public CultivatedLand season(CultivatedLandSeason cultivatedLandSeason) {
        this.setSeason(cultivatedLandSeason);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CultivatedLand)) {
            return false;
        }
        return getId() != null && getId().equals(((CultivatedLand) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CultivatedLand{" +
            "id=" + getId() +
            ", landStatus='" + getLandStatus() + "'" +
            ", urea=" + getUrea() +
            ", mop=" + getMop() +
            ", tsp=" + getTsp() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
