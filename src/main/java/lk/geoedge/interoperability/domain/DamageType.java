package lk.geoedge.interoperability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DamageType.
 */
@Entity
@Table(name = "damage_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DamageType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type_name")
    private String typeName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "damageType")
    @JsonIgnoreProperties(value = { "damageCategory", "damageType" }, allowSetters = true)
    private Set<Damage> damages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DamageType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public DamageType typeName(String typeName) {
        this.setTypeName(typeName);
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Damage> getDamages() {
        return this.damages;
    }

    public void setDamages(Set<Damage> damages) {
        if (this.damages != null) {
            this.damages.forEach(i -> i.setDamageType(null));
        }
        if (damages != null) {
            damages.forEach(i -> i.setDamageType(this));
        }
        this.damages = damages;
    }

    public DamageType damages(Set<Damage> damages) {
        this.setDamages(damages);
        return this;
    }

    public DamageType addDamage(Damage damage) {
        this.damages.add(damage);
        damage.setDamageType(this);
        return this;
    }

    public DamageType removeDamage(Damage damage) {
        this.damages.remove(damage);
        damage.setDamageType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DamageType)) {
            return false;
        }
        return getId() != null && getId().equals(((DamageType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DamageType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            "}";
    }
}
