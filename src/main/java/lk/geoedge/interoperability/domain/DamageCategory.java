package lk.geoedge.interoperability.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DamageCategory.
 */
@Entity
@Table(name = "damage_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DamageCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "damageCategory")
    @JsonIgnoreProperties(value = { "damageCategory", "damageType" }, allowSetters = true)
    private Set<Damage> damages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DamageCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public DamageCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Damage> getDamages() {
        return this.damages;
    }

    public void setDamages(Set<Damage> damages) {
        if (this.damages != null) {
            this.damages.forEach(i -> i.setDamageCategory(null));
        }
        if (damages != null) {
            damages.forEach(i -> i.setDamageCategory(this));
        }
        this.damages = damages;
    }

    public DamageCategory damages(Set<Damage> damages) {
        this.setDamages(damages);
        return this;
    }

    public DamageCategory addDamage(Damage damage) {
        this.damages.add(damage);
        damage.setDamageCategory(this);
        return this;
    }

    public DamageCategory removeDamage(Damage damage) {
        this.damages.remove(damage);
        damage.setDamageCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DamageCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((DamageCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DamageCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            "}";
    }
}
