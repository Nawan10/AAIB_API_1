package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CropVariety.
 */
@Entity
@Table(name = "crop_variety")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropVariety implements Serializable {

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

    @ManyToOne(fetch = FetchType.EAGER)
    private CropVarietyCropType crop;

    @ManyToOne(fetch = FetchType.EAGER)
    private CropVarietyCropDuration cropDuration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropVariety id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CropVariety name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNoOfStages() {
        return this.noOfStages;
    }

    public CropVariety noOfStages(Integer noOfStages) {
        this.setNoOfStages(noOfStages);
        return this;
    }

    public void setNoOfStages(Integer noOfStages) {
        this.noOfStages = noOfStages;
    }

    public String getImage() {
        return this.image;
    }

    public CropVariety image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return this.description;
    }

    public CropVariety description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAddedBy() {
        return this.addedBy;
    }

    public CropVariety addedBy(Integer addedBy) {
        this.setAddedBy(addedBy);
        return this;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public CropVariety createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public CropVarietyCropType getCrop() {
        return this.crop;
    }

    public void setCrop(CropVarietyCropType cropVarietyCropType) {
        this.crop = cropVarietyCropType;
    }

    public CropVariety crop(CropVarietyCropType cropVarietyCropType) {
        this.setCrop(cropVarietyCropType);
        return this;
    }

    public CropVarietyCropDuration getCropDuration() {
        return this.cropDuration;
    }

    public void setCropDuration(CropVarietyCropDuration cropVarietyCropDuration) {
        this.cropDuration = cropVarietyCropDuration;
    }

    public CropVariety cropDuration(CropVarietyCropDuration cropVarietyCropDuration) {
        this.setCropDuration(cropVarietyCropDuration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropVariety)) {
            return false;
        }
        return getId() != null && getId().equals(((CropVariety) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropVariety{" +
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
