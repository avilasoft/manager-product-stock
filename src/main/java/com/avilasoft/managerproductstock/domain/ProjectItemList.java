package com.avilasoft.managerproductstock.domain;

import com.avilasoft.managerproductstock.domain.enumeration.ProjectItemListStatus;
import com.avilasoft.managerproductstock.domain.enumeration.ProjectItemListType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProjectItemList.
 */
@Entity
@Table(name = "project_item_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectItemList implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProjectItemListType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectItemListStatus status;

    @JsonIgnoreProperties(value = { "unitPriceListItems", "computedPriceListItems", "projectItemList" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private UnitPriceList unitPriceList;

    @JsonIgnoreProperties(value = { "computedPriceListItems", "projectItemList" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ComputedPriceList computedPriceList;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "businessAssociate", "projectGroup" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProjectItemList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProjectItemList name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ProjectItemList description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectItemListType getType() {
        return this.type;
    }

    public ProjectItemList type(ProjectItemListType type) {
        this.setType(type);
        return this;
    }

    public void setType(ProjectItemListType type) {
        this.type = type;
    }

    public ProjectItemListStatus getStatus() {
        return this.status;
    }

    public ProjectItemList status(ProjectItemListStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProjectItemListStatus status) {
        this.status = status;
    }

    public UnitPriceList getUnitPriceList() {
        return this.unitPriceList;
    }

    public void setUnitPriceList(UnitPriceList unitPriceList) {
        this.unitPriceList = unitPriceList;
    }

    public ProjectItemList unitPriceList(UnitPriceList unitPriceList) {
        this.setUnitPriceList(unitPriceList);
        return this;
    }

    public ComputedPriceList getComputedPriceList() {
        return this.computedPriceList;
    }

    public void setComputedPriceList(ComputedPriceList computedPriceList) {
        this.computedPriceList = computedPriceList;
    }

    public ProjectItemList computedPriceList(ComputedPriceList computedPriceList) {
        this.setComputedPriceList(computedPriceList);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectItemList project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectItemList)) {
            return false;
        }
        return id != null && id.equals(((ProjectItemList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectItemList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
