package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UnitPriceList.
 */
@Entity
@Table(name = "unit_price_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UnitPriceList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "unit_price_list_total")
    private Float unitPriceListTotal;

    @OneToMany(mappedBy = "unitPriceList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "projectItemListItem", "unitPriceList" }, allowSetters = true)
    private Set<UnitPriceListItem> unitPriceListItems = new HashSet<>();

    @OneToMany(mappedBy = "unitPriceList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitPriceList", "computedPriceList" }, allowSetters = true)
    private Set<ComputedPriceListItem> computedPriceListItems = new HashSet<>();

    @JsonIgnoreProperties(value = { "unitPriceList", "computedPriceList", "project" }, allowSetters = true)
    @OneToOne(mappedBy = "unitPriceList")
    private ProjectItemList projectItemList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitPriceList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public UnitPriceList code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getUnitPriceListTotal() {
        return this.unitPriceListTotal;
    }

    public UnitPriceList unitPriceListTotal(Float unitPriceListTotal) {
        this.setUnitPriceListTotal(unitPriceListTotal);
        return this;
    }

    public void setUnitPriceListTotal(Float unitPriceListTotal) {
        this.unitPriceListTotal = unitPriceListTotal;
    }

    public Set<UnitPriceListItem> getUnitPriceListItems() {
        return this.unitPriceListItems;
    }

    public void setUnitPriceListItems(Set<UnitPriceListItem> unitPriceListItems) {
        if (this.unitPriceListItems != null) {
            this.unitPriceListItems.forEach(i -> i.setUnitPriceList(null));
        }
        if (unitPriceListItems != null) {
            unitPriceListItems.forEach(i -> i.setUnitPriceList(this));
        }
        this.unitPriceListItems = unitPriceListItems;
    }

    public UnitPriceList unitPriceListItems(Set<UnitPriceListItem> unitPriceListItems) {
        this.setUnitPriceListItems(unitPriceListItems);
        return this;
    }

    public UnitPriceList addUnitPriceListItem(UnitPriceListItem unitPriceListItem) {
        this.unitPriceListItems.add(unitPriceListItem);
        unitPriceListItem.setUnitPriceList(this);
        return this;
    }

    public UnitPriceList removeUnitPriceListItem(UnitPriceListItem unitPriceListItem) {
        this.unitPriceListItems.remove(unitPriceListItem);
        unitPriceListItem.setUnitPriceList(null);
        return this;
    }

    public Set<ComputedPriceListItem> getComputedPriceListItems() {
        return this.computedPriceListItems;
    }

    public void setComputedPriceListItems(Set<ComputedPriceListItem> computedPriceListItems) {
        if (this.computedPriceListItems != null) {
            this.computedPriceListItems.forEach(i -> i.setUnitPriceList(null));
        }
        if (computedPriceListItems != null) {
            computedPriceListItems.forEach(i -> i.setUnitPriceList(this));
        }
        this.computedPriceListItems = computedPriceListItems;
    }

    public UnitPriceList computedPriceListItems(Set<ComputedPriceListItem> computedPriceListItems) {
        this.setComputedPriceListItems(computedPriceListItems);
        return this;
    }

    public UnitPriceList addComputedPriceListItem(ComputedPriceListItem computedPriceListItem) {
        this.computedPriceListItems.add(computedPriceListItem);
        computedPriceListItem.setUnitPriceList(this);
        return this;
    }

    public UnitPriceList removeComputedPriceListItem(ComputedPriceListItem computedPriceListItem) {
        this.computedPriceListItems.remove(computedPriceListItem);
        computedPriceListItem.setUnitPriceList(null);
        return this;
    }

    public ProjectItemList getProjectItemList() {
        return this.projectItemList;
    }

    public void setProjectItemList(ProjectItemList projectItemList) {
        if (this.projectItemList != null) {
            this.projectItemList.setUnitPriceList(null);
        }
        if (projectItemList != null) {
            projectItemList.setUnitPriceList(this);
        }
        this.projectItemList = projectItemList;
    }

    public UnitPriceList projectItemList(ProjectItemList projectItemList) {
        this.setProjectItemList(projectItemList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitPriceList)) {
            return false;
        }
        return id != null && id.equals(((UnitPriceList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitPriceList{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", unitPriceListTotal=" + getUnitPriceListTotal() +
            "}";
    }
}
