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
 * A Price.
 */
@Entity
@Table(name = "price")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Price implements Serializable {

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

    @OneToMany(mappedBy = "price")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "child", "price" }, allowSetters = true)
    private Set<PriceHistory> priceHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Price id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Price name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Price description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PriceHistory> getPriceHistories() {
        return this.priceHistories;
    }

    public void setPriceHistories(Set<PriceHistory> priceHistories) {
        if (this.priceHistories != null) {
            this.priceHistories.forEach(i -> i.setPrice(null));
        }
        if (priceHistories != null) {
            priceHistories.forEach(i -> i.setPrice(this));
        }
        this.priceHistories = priceHistories;
    }

    public Price priceHistories(Set<PriceHistory> priceHistories) {
        this.setPriceHistories(priceHistories);
        return this;
    }

    public Price addPriceHistory(PriceHistory priceHistory) {
        this.priceHistories.add(priceHistory);
        priceHistory.setPrice(this);
        return this;
    }

    public Price removePriceHistory(PriceHistory priceHistory) {
        this.priceHistories.remove(priceHistory);
        priceHistory.setPrice(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        return id != null && id.equals(((Price) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Price{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
