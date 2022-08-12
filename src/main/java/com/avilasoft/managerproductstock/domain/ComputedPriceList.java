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
 * A ComputedPriceList.
 */
@Entity
@Table(name = "computed_price_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ComputedPriceList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "computed_price_list_total")
    private Float computedPriceListTotal;

    @OneToMany(mappedBy = "computedPriceList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "unitPriceList", "computedPriceList" }, allowSetters = true)
    private Set<ComputedPriceListItem> computedPriceListItems = new HashSet<>();

    @JsonIgnoreProperties(value = { "unitPriceList", "computedPriceList", "project" }, allowSetters = true)
    @OneToOne(mappedBy = "computedPriceList")
    private ProjectItemList projectItemList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ComputedPriceList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ComputedPriceList code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getComputedPriceListTotal() {
        return this.computedPriceListTotal;
    }

    public ComputedPriceList computedPriceListTotal(Float computedPriceListTotal) {
        this.setComputedPriceListTotal(computedPriceListTotal);
        return this;
    }

    public void setComputedPriceListTotal(Float computedPriceListTotal) {
        this.computedPriceListTotal = computedPriceListTotal;
    }

    public Set<ComputedPriceListItem> getComputedPriceListItems() {
        return this.computedPriceListItems;
    }

    public void setComputedPriceListItems(Set<ComputedPriceListItem> computedPriceListItems) {
        if (this.computedPriceListItems != null) {
            this.computedPriceListItems.forEach(i -> i.setComputedPriceList(null));
        }
        if (computedPriceListItems != null) {
            computedPriceListItems.forEach(i -> i.setComputedPriceList(this));
        }
        this.computedPriceListItems = computedPriceListItems;
    }

    public ComputedPriceList computedPriceListItems(Set<ComputedPriceListItem> computedPriceListItems) {
        this.setComputedPriceListItems(computedPriceListItems);
        return this;
    }

    public ComputedPriceList addComputedPriceListItem(ComputedPriceListItem computedPriceListItem) {
        this.computedPriceListItems.add(computedPriceListItem);
        computedPriceListItem.setComputedPriceList(this);
        return this;
    }

    public ComputedPriceList removeComputedPriceListItem(ComputedPriceListItem computedPriceListItem) {
        this.computedPriceListItems.remove(computedPriceListItem);
        computedPriceListItem.setComputedPriceList(null);
        return this;
    }

    public ProjectItemList getProjectItemList() {
        return this.projectItemList;
    }

    public void setProjectItemList(ProjectItemList projectItemList) {
        if (this.projectItemList != null) {
            this.projectItemList.setComputedPriceList(null);
        }
        if (projectItemList != null) {
            projectItemList.setComputedPriceList(this);
        }
        this.projectItemList = projectItemList;
    }

    public ComputedPriceList projectItemList(ProjectItemList projectItemList) {
        this.setProjectItemList(projectItemList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComputedPriceList)) {
            return false;
        }
        return id != null && id.equals(((ComputedPriceList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComputedPriceList{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", computedPriceListTotal=" + getComputedPriceListTotal() +
            "}";
    }
}
