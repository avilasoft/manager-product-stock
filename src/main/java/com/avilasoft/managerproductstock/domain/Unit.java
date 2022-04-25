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

    @Column(name = "is_base")
    private Boolean isBase;

    @ManyToOne
    @JsonIgnoreProperties(value = { "units" }, allowSetters = true)
    private UnitType unitType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "providerProduct", "units" }, allowSetters = true)
    private ProviderProductPrice providerProductPrice;

    @ManyToMany
    @JoinTable(name = "rel_unit__parent", joinColumns = @JoinColumn(name = "unit_id"), inverseJoinColumns = @JoinColumn(name = "parent_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitType", "providerProductPrice", "parents", "product", "children" }, allowSetters = true)
    private Set<Unit> parents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "units", "providerProducts", "productCategories" }, allowSetters = true)
    private Product product;

    @ManyToMany(mappedBy = "parents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitType", "providerProductPrice", "parents", "product", "children" }, allowSetters = true)
    private Set<Unit> children = new HashSet<>();

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

    public Boolean getIsBase() {
        return this.isBase;
    }

    public Unit isBase(Boolean isBase) {
        this.setIsBase(isBase);
        return this;
    }

    public void setIsBase(Boolean isBase) {
        this.isBase = isBase;
    }

    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public Unit unitType(UnitType unitType) {
        this.setUnitType(unitType);
        return this;
    }

    public ProviderProductPrice getProviderProductPrice() {
        return this.providerProductPrice;
    }

    public void setProviderProductPrice(ProviderProductPrice providerProductPrice) {
        this.providerProductPrice = providerProductPrice;
    }

    public Unit providerProductPrice(ProviderProductPrice providerProductPrice) {
        this.setProviderProductPrice(providerProductPrice);
        return this;
    }

    public Set<Unit> getParents() {
        return this.parents;
    }

    public void setParents(Set<Unit> units) {
        this.parents = units;
    }

    public Unit parents(Set<Unit> units) {
        this.setParents(units);
        return this;
    }

    public Unit addParent(Unit unit) {
        this.parents.add(unit);
        unit.getChildren().add(this);
        return this;
    }

    public Unit removeParent(Unit unit) {
        this.parents.remove(unit);
        unit.getChildren().remove(this);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Unit product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Set<Unit> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Unit> units) {
        if (this.children != null) {
            this.children.forEach(i -> i.removeParent(this));
        }
        if (units != null) {
            units.forEach(i -> i.addParent(this));
        }
        this.children = units;
    }

    public Unit children(Set<Unit> units) {
        this.setChildren(units);
        return this;
    }

    public Unit addChild(Unit unit) {
        this.children.add(unit);
        unit.getParents().add(this);
        return this;
    }

    public Unit removeChild(Unit unit) {
        this.children.remove(unit);
        unit.getParents().remove(this);
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
            ", isBase='" + getIsBase() + "'" +
            "}";
    }
}
