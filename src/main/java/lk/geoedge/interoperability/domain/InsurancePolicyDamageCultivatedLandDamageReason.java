package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A InsurancePolicyDamageCultivatedLandDamageReason.
 */
@Entity
@Table(name = "cultivated_land_damage_reason")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePolicyDamageCultivatedLandDamageReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "damage_category_id")
    private Integer damageCategoryId;

    @Column(name = "damage_type_id")
    private Integer damageTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InsurancePolicyDamageCultivatedLandDamageReason id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public InsurancePolicyDamageCultivatedLandDamageReason name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDamageCategoryId() {
        return this.damageCategoryId;
    }

    public InsurancePolicyDamageCultivatedLandDamageReason damageCategoryId(Integer damageCategoryId) {
        this.setDamageCategoryId(damageCategoryId);
        return this;
    }

    public void setDamageCategoryId(Integer damageCategoryId) {
        this.damageCategoryId = damageCategoryId;
    }

    public Integer getDamageTypeId() {
        return this.damageTypeId;
    }

    public InsurancePolicyDamageCultivatedLandDamageReason damageTypeId(Integer damageTypeId) {
        this.setDamageTypeId(damageTypeId);
        return this;
    }

    public void setDamageTypeId(Integer damageTypeId) {
        this.damageTypeId = damageTypeId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsurancePolicyDamageCultivatedLandDamageReason)) {
            return false;
        }
        return getId() != null && getId().equals(((InsurancePolicyDamageCultivatedLandDamageReason) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancePolicyDamageCultivatedLandDamageReason{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", damageCategoryId=" + getDamageCategoryId() +
            ", damageTypeId=" + getDamageTypeId() +
            "}";
    }
}
