package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A IndexPolicyCropVariety.
 */
@Entity
@Table(name = "crop_variety")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndexPolicyCropVariety implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "no_of_stages")
    private Integer noOfStages;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "added_by")
    private Integer addedBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndexPolicyCropVariety id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IndexPolicyCropVariety name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNoOfStages() {
        return this.noOfStages;
    }

    public IndexPolicyCropVariety noOfStages(Integer noOfStages) {
        this.setNoOfStages(noOfStages);
        return this;
    }

    public void setNoOfStages(Integer noOfStages) {
        this.noOfStages = noOfStages;
    }

    public String getImage() {
        return this.image;
    }

    public IndexPolicyCropVariety image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return this.description;
    }

    public IndexPolicyCropVariety description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAddedBy() {
        return this.addedBy;
    }

    public IndexPolicyCropVariety addedBy(Integer addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public IndexPolicyCropVariety createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexPolicyCropVariety)) {
            return false;
        }
        return getId() != null && getId().equals(((IndexPolicyCropVariety) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndexPolicyCropVariety{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", noOfStages=" + getNoOfStages() +
            ", image='" + getImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", addedBy=" + getAddedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
