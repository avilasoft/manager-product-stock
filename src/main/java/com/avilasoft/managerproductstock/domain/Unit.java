package com.avilasoft.managerproductstock.domain;

import com.avilasoft.managerproductstock.domain.enumeration.UnitType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Unit.
 */
@Entity
@Table(name = "unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UnitType type;

    @OneToMany(mappedBy = "unit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitPriceListItems", "projectItemList", "item", "providerItem", "unit" }, allowSetters = true)
    private Set<ProjectItemListItem> projectItemListItems = new HashSet<>();

    @OneToMany(mappedBy = "unit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "provider", "unit", "item" }, allowSetters = true)
    private Set<ProviderItem> providerItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Unit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Unit name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Unit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Unit symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public UnitType getType() {
        return this.type;
    }

    public Unit type(UnitType type) {
        this.setType(type);
        return this;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public Set<ProjectItemListItem> getProjectItemListItems() {
        return this.projectItemListItems;
    }

    public void setProjectItemListItems(Set<ProjectItemListItem> projectItemListItems) {
        if (this.projectItemListItems != null) {
            this.projectItemListItems.forEach(i -> i.setUnit(null));
        }
        if (projectItemListItems != null) {
            projectItemListItems.forEach(i -> i.setUnit(this));
        }
        this.projectItemListItems = projectItemListItems;
    }

    public Unit projectItemListItems(Set<ProjectItemListItem> projectItemListItems) {
        this.setProjectItemListItems(projectItemListItems);
        return this;
    }

    public Unit addProjectItemListItem(ProjectItemListItem projectItemListItem) {
        this.projectItemListItems.add(projectItemListItem);
        projectItemListItem.setUnit(this);
        return this;
    }

    public Unit removeProjectItemListItem(ProjectItemListItem projectItemListItem) {
        this.projectItemListItems.remove(projectItemListItem);
        projectItemListItem.setUnit(null);
        return this;
    }

    public Set<ProviderItem> getProviderItems() {
        return this.providerItems;
    }

    public void setProviderItems(Set<ProviderItem> providerItems) {
        if (this.providerItems != null) {
            this.providerItems.forEach(i -> i.setUnit(null));
        }
        if (providerItems != null) {
            providerItems.forEach(i -> i.setUnit(this));
        }
        this.providerItems = providerItems;
    }

    public Unit providerItems(Set<ProviderItem> providerItems) {
        this.setProviderItems(providerItems);
        return this;
    }

    public Unit addProviderItem(ProviderItem providerItem) {
        this.providerItems.add(providerItem);
        providerItem.setUnit(this);
        return this;
    }

    public Unit removeProviderItem(ProviderItem providerItem) {
        this.providerItems.remove(providerItem);
        providerItem.setUnit(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Unit)) {
            return false;
        }
        return id != null && id.equals(((Unit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Unit{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
