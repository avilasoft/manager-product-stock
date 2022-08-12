package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UnitPriceListItem.
 */
@Entity
@Table(name = "unit_price_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UnitPriceListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "unit_cost_total", nullable = false)
    private Float unitCostTotal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "unitPriceListItems", "projectItemList", "item", "providerItem", "unit" }, allowSetters = true)
    private ProjectItemListItem projectItemListItem;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "unitPriceListItems", "computedPriceListItems", "projectItemList" }, allowSetters = true)
    private UnitPriceList unitPriceList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitPriceListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public UnitPriceListItem code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public UnitPriceListItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getUnitCostTotal() {
        return this.unitCostTotal;
    }

    public UnitPriceListItem unitCostTotal(Float unitCostTotal) {
        this.setUnitCostTotal(unitCostTotal);
        return this;
    }

    public void setUnitCostTotal(Float unitCostTotal) {
        this.unitCostTotal = unitCostTotal;
    }

    public ProjectItemListItem getProjectItemListItem() {
        return this.projectItemListItem;
    }

    public void setProjectItemListItem(ProjectItemListItem projectItemListItem) {
        this.projectItemListItem = projectItemListItem;
    }

    public UnitPriceListItem projectItemListItem(ProjectItemListItem projectItemListItem) {
        this.setProjectItemListItem(projectItemListItem);
        return this;
    }

    public UnitPriceList getUnitPriceList() {
        return this.unitPriceList;
    }

    public void setUnitPriceList(UnitPriceList unitPriceList) {
        this.unitPriceList = unitPriceList;
    }

    public UnitPriceListItem unitPriceList(UnitPriceList unitPriceList) {
        this.setUnitPriceList(unitPriceList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitPriceListItem)) {
            return false;
        }
        return id != null && id.equals(((UnitPriceListItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitPriceListItem{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", unitCostTotal=" + getUnitCostTotal() +
            "}";
    }
}
