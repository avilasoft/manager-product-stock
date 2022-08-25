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

    @NotNull
    @Column(name = "unit_price_total", nullable = false)
    private Float unitPriceTotal;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "unitPriceListItem", "projectItemList", "item", "providerItem", "unit" }, allowSetters = true)
    @OneToOne(mappedBy = "unitPriceListItem")
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

    public Float getUnitPriceTotal() {
        return this.unitPriceTotal;
    }

    public UnitPriceListItem unitPriceTotal(Float unitPriceTotal) {
        this.setUnitPriceTotal(unitPriceTotal);
        return this;
    }

    public void setUnitPriceTotal(Float unitPriceTotal) {
        this.unitPriceTotal = unitPriceTotal;
    }

    public String getDescription() {
        return this.description;
    }

    public UnitPriceListItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectItemListItem getProjectItemListItem() {
        return this.projectItemListItem;
    }

    public void setProjectItemListItem(ProjectItemListItem projectItemListItem) {
        if (this.projectItemListItem != null) {
            this.projectItemListItem.setUnitPriceListItem(null);
        }
        if (projectItemListItem != null) {
            projectItemListItem.setUnitPriceListItem(this);
        }
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
            ", unitPriceTotal=" + getUnitPriceTotal() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
