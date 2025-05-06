package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CropDamageDamage.
 */
@Entity
@Table(name = "damage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropDamageDamage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "damage_name")
    private String damageName;

    @Column(name = "damage_code")
    private String damageCode;

    @Column(name = "damage_family")
    private String damageFamily;

    @Column(name = "damage_genus")
    private String damageGenus;

    @Column(name = "damage_species")
    private String damageSpecies;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "added_by")
    private String addedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropDamageDamage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDamageName() {
        return this.damageName;
    }

    public CropDamageDamage damageName(String damageName) {
        this.setDamageName(damageName);
        return this;
    }

    public void setDamageName(String damageName) {
        this.damageName = damageName;
    }

    public String getDamageCode() {
        return this.damageCode;
    }

    public CropDamageDamage damageCode(String damageCode) {
        this.setDamageCode(damageCode);
        return this;
    }

    public void setDamageCode(String damageCode) {
        this.damageCode = damageCode;
    }

    public String getDamageFamily() {
        return this.damageFamily;
    }

    public CropDamageDamage damageFamily(String damageFamily) {
        this.setDamageFamily(damageFamily);
        return this;
    }

    public void setDamageFamily(String damageFamily) {
        this.damageFamily = damageFamily;
    }

    public String getDamageGenus() {
        return this.damageGenus;
    }

    public CropDamageDamage damageGenus(String damageGenus) {
        this.setDamageGenus(damageGenus);
        return this;
    }

    public void setDamageGenus(String damageGenus) {
        this.damageGenus = damageGenus;
    }

    public String getDamageSpecies() {
        return this.damageSpecies;
    }

    public CropDamageDamage damageSpecies(String damageSpecies) {
        this.setDamageSpecies(damageSpecies);
        return this;
    }

    public void setDamageSpecies(String damageSpecies) {
        this.damageSpecies = damageSpecies;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CropDamageDamage createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public CropDamageDamage addedBy(String addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropDamageDamage)) {
            return false;
        }
        return getId() != null && getId().equals(((CropDamageDamage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropDamageDamage{" +
            "id=" + getId() +
            ", damageName='" + getDamageName() + "'" +
            ", damageCode='" + getDamageCode() + "'" +
            ", damageFamily='" + getDamageFamily() + "'" +
            ", damageGenus='" + getDamageGenus() + "'" +
            ", damageSpecies='" + getDamageSpecies() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", addedBy='" + getAddedBy() + "'" +
            "}";
    }
}
