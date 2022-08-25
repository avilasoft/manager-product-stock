package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProjectItemListItem.
 */
@Entity
@Table(name = "project_item_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectItemListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "dimension", nullable = false)
    private String dimension;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Float quantity;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "projectItemListItem", "unitPriceList" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private UnitPriceListItem unitPriceListItem;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "unitPriceList", "computedPriceList", "project" }, allowSetters = true)
    private ProjectItemList projectItemList;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "providerItems" }, allowSetters = true)
    private Item item;

    @ManyToOne
    @JsonIgnoreProperties(value = { "provider", "unit", "item" }, allowSetters = true)
    private ProviderItem providerItem;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "projectItemListItems", "providerItems" }, allowSetters = true)
    private Unit unit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProjectItemListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ProjectItemListItem code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDimension() {
        return this.dimension;
    }

    public ProjectItemListItem dimension(String dimension) {
        this.setDimension(dimension);
        return this;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public ProjectItemListItem quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return this.description;
    }

    public ProjectItemListItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnitPriceListItem getUnitPriceListItem() {
        return this.unitPriceListItem;
    }

    public void setUnitPriceListItem(UnitPriceListItem unitPriceListItem) {
        this.unitPriceListItem = unitPriceListItem;
    }

    public ProjectItemListItem unitPriceListItem(UnitPriceListItem unitPriceListItem) {
        this.setUnitPriceListItem(unitPriceListItem);
        return this;
    }

    public ProjectItemList getProjectItemList() {
        return this.projectItemList;
    }

    public void setProjectItemList(ProjectItemList projectItemList) {
        this.projectItemList = projectItemList;
    }

    public ProjectItemListItem projectItemList(ProjectItemList projectItemList) {
        this.setProjectItemList(projectItemList);
        return this;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ProjectItemListItem item(Item item) {
        this.setItem(item);
        return this;
    }

    public ProviderItem getProviderItem() {
        return this.providerItem;
    }

    public void setProviderItem(ProviderItem providerItem) {
        this.providerItem = providerItem;
    }

    public ProjectItemListItem providerItem(ProviderItem providerItem) {
        this.setProviderItem(providerItem);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ProjectItemListItem unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectItemListItem)) {
            return false;
        }
        return id != null && id.equals(((ProjectItemListItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectItemListItem{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dimension='" + getDimension() + "'" +
            ", quantity=" + getQuantity() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
