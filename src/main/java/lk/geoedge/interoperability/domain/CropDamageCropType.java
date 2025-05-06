package lk.geoedge.interoperability.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A CropDamageCropType.
 */
@Entity
@Table(name = "crop_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CropDamageCropType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "crop")
    private String crop;

    @Column(name = "image")
    private String image;

    @Column(name = "main_crop")
    private Integer mainCrop;

    @Column(name = "crop_code")
    private String cropCode;

    @Column(name = "no_of_stages")
    private String noOfStages;

    @Column(name = "description")
    private String description;

    @Column(name = "crop_types_id")
    private Integer cropTypesId;

    @Column(name = "units_id")
    private Integer unitsId;

    @Column(name = "area")
    private Double area;

    @Column(name = "sum_insured")
    private Double sumInsured;

    @Column(name = "min_sum_insured")
    private Double minSumInsured;

    @Column(name = "max_sum_insured")
    private Double maxSumInsured;

    @Column(name = "subsidised_premium_rate")
    private Double subsidisedPremiumRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CropDamageCropType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrop() {
        return this.crop;
    }

    public CropDamageCropType crop(String crop) {
        this.setCrop(crop);
        return this;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getImage() {
        return this.image;
    }

    public CropDamageCropType image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getMainCrop() {
        return this.mainCrop;
    }

    public CropDamageCropType mainCrop(Integer mainCrop) {
        this.setMainCrop(mainCrop);
        return this;
    }

    public void setMainCrop(Integer mainCrop) {
        this.mainCrop = mainCrop;
    }

    public String getCropCode() {
        return this.cropCode;
    }

    public CropDamageCropType cropCode(String cropCode) {
        this.setCropCode(cropCode);
        return this;
    }

    public void setCropCode(String cropCode) {
        this.cropCode = cropCode;
    }

    public String getNoOfStages() {
        return this.noOfStages;
    }

    public CropDamageCropType noOfStages(String noOfStages) {
        this.setNoOfStages(noOfStages);
        return this;
    }

    public void setNoOfStages(String noOfStages) {
        this.noOfStages = noOfStages;
    }

    public String getDescription() {
        return this.description;
    }

    public CropDamageCropType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCropTypesId() {
        return this.cropTypesId;
    }

    public CropDamageCropType cropTypesId(Integer cropTypesId) {
        this.setCropTypesId(cropTypesId);
        return this;
    }

    public void setCropTypesId(Integer cropTypesId) {
        this.cropTypesId = cropTypesId;
    }

    public Integer getUnitsId() {
        return this.unitsId;
    }

    public CropDamageCropType unitsId(Integer unitsId) {
        this.setUnitsId(unitsId);
        return this;
    }

    public void setUnitsId(Integer unitsId) {
        this.unitsId = unitsId;
    }

    public Double getArea() {
        return this.area;
    }

    public CropDamageCropType area(Double area) {
        this.setArea(area);
        return this;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getSumInsured() {
        return this.sumInsured;
    }

    public CropDamageCropType sumInsured(Double sumInsured) {
        this.setSumInsured(sumInsured);
        return this;
    }

    public void setSumInsured(Double sumInsured) {
        this.sumInsured = sumInsured;
    }

    public Double getMinSumInsured() {
        return this.minSumInsured;
    }

    public CropDamageCropType minSumInsured(Double minSumInsured) {
        this.setMinSumInsured(minSumInsured);
        return this;
    }

    public void setMinSumInsured(Double minSumInsured) {
        this.minSumInsured = minSumInsured;
    }

    public Double getMaxSumInsured() {
        return this.maxSumInsured;
    }

    public CropDamageCropType maxSumInsured(Double maxSumInsured) {
        this.setMaxSumInsured(maxSumInsured);
        return this;
    }

    public void setMaxSumInsured(Double maxSumInsured) {
        this.maxSumInsured = maxSumInsured;
    }

    public Double getSubsidisedPremiumRate() {
        return this.subsidisedPremiumRate;
    }

    public CropDamageCropType subsidisedPremiumRate(Double subsidisedPremiumRate) {
        this.setSubsidisedPremiumRate(subsidisedPremiumRate);
        return this;
    }

    public void setSubsidisedPremiumRate(Double subsidisedPremiumRate) {
        this.subsidisedPremiumRate = subsidisedPremiumRate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CropDamageCropType)) {
            return false;
        }
        return getId() != null && getId().equals(((CropDamageCropType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CropDamageCropType{" +
            "id=" + getId() +
            ", crop='" + getCrop() + "'" +
            ", image='" + getImage() + "'" +
            ", mainCrop=" + getMainCrop() +
            ", cropCode='" + getCropCode() + "'" +
            ", noOfStages='" + getNoOfStages() + "'" +
            ", description='" + getDescription() + "'" +
            ", cropTypesId=" + getCropTypesId() +
            ", unitsId=" + getUnitsId() +
            ", area=" + getArea() +
            ", sumInsured=" + getSumInsured() +
            ", minSumInsured=" + getMinSumInsured() +
            ", maxSumInsured=" + getMaxSumInsured() +
            ", subsidisedPremiumRate=" + getSubsidisedPremiumRate() +
            "}";
    }
}
