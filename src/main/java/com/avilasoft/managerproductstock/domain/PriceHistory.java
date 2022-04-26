package com.avilasoft.managerproductstock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PriceHistory.
 */
@Entity
@Table(name = "price_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PriceHistory implements Serializable {

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

    @JsonIgnoreProperties(value = { "parent", "child", "price" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PriceHistory parent;

    @JsonIgnoreProperties(value = { "parent", "child", "price" }, allowSetters = true)
    @OneToOne(mappedBy = "parent")
    private PriceHistory child;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "priceHistories" }, allowSetters = true)
    private Price price;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PriceHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PriceHistory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PriceHistory description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriceHistory getParent() {
        return this.parent;
    }

    public void setParent(PriceHistory priceHistory) {
        this.parent = priceHistory;
    }

    public PriceHistory parent(PriceHistory priceHistory) {
        this.setParent(priceHistory);
        return this;
    }

    public PriceHistory getChild() {
        return this.child;
    }

    public void setChild(PriceHistory priceHistory) {
        if (this.child != null) {
            this.child.setParent(null);
        }
        if (priceHistory != null) {
            priceHistory.setParent(this);
        }
        this.child = priceHistory;
    }

    public PriceHistory child(PriceHistory priceHistory) {
        this.setChild(priceHistory);
        return this;
    }

    public Price getPrice() {
        return this.price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public PriceHistory price(Price price) {
        this.setPrice(price);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistory)) {
            return false;
        }
        return id != null && id.equals(((PriceHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
