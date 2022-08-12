package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ComputedPriceListItem.
 */
@Entity
@Table(name = "computed_price_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ComputedPriceListItem implements Serializable {

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
    @Column(name = "computed_cost_total", nullable = false)
    private Float computedCostTotal;

    @NotNull
    @Column(name = "computed_quantity_total", nullable = false)
    private Float computedQuantityTotal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "unitPriceListItems", "computedPriceListItems", "projectItemList" }, allowSetters = true)
    private UnitPriceList unitPriceList;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "computedPriceListItems", "projectItemList" }, allowSetters = true)
    private ComputedPriceList computedPriceList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ComputedPriceListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ComputedPriceListItem code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public ComputedPriceListItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getComputedCostTotal() {
        return this.computedCostTotal;
    }

    public ComputedPriceListItem computedCostTotal(Float computedCostTotal) {
        this.setComputedCostTotal(computedCostTotal);
        return this;
    }

    public void setComputedCostTotal(Float computedCostTotal) {
        this.computedCostTotal = computedCostTotal;
    }

    public Float getComputedQuantityTotal() {
        return this.computedQuantityTotal;
    }

    public ComputedPriceListItem computedQuantityTotal(Float computedQuantityTotal) {
        this.setComputedQuantityTotal(computedQuantityTotal);
        return this;
    }

    public void setComputedQuantityTotal(Float computedQuantityTotal) {
        this.computedQuantityTotal = computedQuantityTotal;
    }

    public UnitPriceList getUnitPriceList() {
        return this.unitPriceList;
    }

    public void setUnitPriceList(UnitPriceList unitPriceList) {
        this.unitPriceList = unitPriceList;
    }

    public ComputedPriceListItem unitPriceList(UnitPriceList unitPriceList) {
        this.setUnitPriceList(unitPriceList);
        return this;
    }

    public ComputedPriceList getComputedPriceList() {
        return this.computedPriceList;
    }

    public void setComputedPriceList(ComputedPriceList computedPriceList) {
        this.computedPriceList = computedPriceList;
    }

    public ComputedPriceListItem computedPriceList(ComputedPriceList computedPriceList) {
        this.setComputedPriceList(computedPriceList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComputedPriceListItem)) {
            return false;
        }
        return id != null && id.equals(((ComputedPriceListItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComputedPriceListItem{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", computedCostTotal=" + getComputedCostTotal() +
            ", computedQuantityTotal=" + getComputedQuantityTotal() +
            "}";
    }
}
