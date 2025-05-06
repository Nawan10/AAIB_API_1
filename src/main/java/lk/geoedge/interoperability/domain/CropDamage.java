package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CropDamage.
 */
@Entity
@Table(name = "crop_damage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropDamage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private CropDamageCropType crop;

    @ManyToOne(fetch = FetchType.EAGER)
    private CropDamageDamage damage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropDamage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CropDamage addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CropDamage createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public CropDamageCropType getCrop() {
        return this.crop;
    }

    public void setCrop(CropDamageCropType cropDamageCropType) {
        this.crop = cropDamageCropType;
    }

    public CropDamage crop(CropDamageCropType cropDamageCropType) {
        this.setCrop(cropDamageCropType);
        return this;
    }

    public CropDamageDamage getDamage() {
        return this.damage;
    }

    public void setDamage(CropDamageDamage cropDamageDamage) {
        this.damage = cropDamageDamage;
    }

    public CropDamage damage(CropDamageDamage cropDamageDamage) {
        this.setDamage(cropDamageDamage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropDamage)) {
            return false;
        }
        return getId() != null && getId().equals(((CropDamage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropDamage{" +
            "id=" + getId() +
            ", addedBy='" + getAddedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
