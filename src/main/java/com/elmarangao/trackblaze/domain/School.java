package com.elmarangao.trackblaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A School.
 */
@Entity
@Table(name = "school")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "school")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trackFieldEntryAthleteDetails", "events", "school" }, allowSetters = true)
    private Set<Athlete> athletes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public School id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public School name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Athlete> getAthletes() {
        return this.athletes;
    }

    public void setAthletes(Set<Athlete> athletes) {
        if (this.athletes != null) {
            this.athletes.forEach(i -> i.setSchool(null));
        }
        if (athletes != null) {
            athletes.forEach(i -> i.setSchool(this));
        }
        this.athletes = athletes;
    }

    public School athletes(Set<Athlete> athletes) {
        this.setAthletes(athletes);
        return this;
    }

    public School addAthlete(Athlete athlete) {
        this.athletes.add(athlete);
        athlete.setSchool(this);
        return this;
    }

    public School removeAthlete(Athlete athlete) {
        this.athletes.remove(athlete);
        athlete.setSchool(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof School)) {
            return false;
        }
        return id != null && id.equals(((School) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "School{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
