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
 * A UnitEquivalence.
 */
@Entity
@Table(name = "unit_equivalence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UnitEquivalence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "child")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "unit", "child" }, allowSetters = true)
    private Set<UnitEquivalence> parents = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "products", "providerProductPrices", "unitEquivalences", "unitType" }, allowSetters = true)
    private Unit unit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parents", "unit", "child" }, allowSetters = true)
    private UnitEquivalence child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitEquivalence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UnitEquivalence name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public UnitEquivalence description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UnitEquivalence> getParents() {
        return this.parents;
    }

    public void setParents(Set<UnitEquivalence> unitEquivalences) {
        if (this.parents != null) {
            this.parents.forEach(i -> i.setChild(null));
        }
        if (unitEquivalences != null) {
            unitEquivalences.forEach(i -> i.setChild(this));
        }
        this.parents = unitEquivalences;
    }

    public UnitEquivalence parents(Set<UnitEquivalence> unitEquivalences) {
        this.setParents(unitEquivalences);
        return this;
    }

    public UnitEquivalence addParent(UnitEquivalence unitEquivalence) {
        this.parents.add(unitEquivalence);
        unitEquivalence.setChild(this);
        return this;
    }

    public UnitEquivalence removeParent(UnitEquivalence unitEquivalence) {
        this.parents.remove(unitEquivalence);
        unitEquivalence.setChild(null);
        return this;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public UnitEquivalence unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public UnitEquivalence getChild() {
        return this.child;
    }

    public void setChild(UnitEquivalence unitEquivalence) {
        this.child = unitEquivalence;
    }

    public UnitEquivalence child(UnitEquivalence unitEquivalence) {
        this.setChild(unitEquivalence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitEquivalence)) {
            return false;
        }
        return id != null && id.equals(((UnitEquivalence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitEquivalence{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
