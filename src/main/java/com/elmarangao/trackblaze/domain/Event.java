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
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "details", "event" }, allowSetters = true)
    private Set<TrackFieldEntry> trackFieldEntries = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trackFieldEntryAthleteDetails", "events", "school" }, allowSetters = true)
    private Set<Athlete> athletes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Event name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TrackFieldEntry> getTrackFieldEntries() {
        return this.trackFieldEntries;
    }

    public void setTrackFieldEntries(Set<TrackFieldEntry> trackFieldEntries) {
        if (this.trackFieldEntries != null) {
            this.trackFieldEntries.forEach(i -> i.setEvent(null));
        }
        if (trackFieldEntries != null) {
            trackFieldEntries.forEach(i -> i.setEvent(this));
        }
        this.trackFieldEntries = trackFieldEntries;
    }

    public Event trackFieldEntries(Set<TrackFieldEntry> trackFieldEntries) {
        this.setTrackFieldEntries(trackFieldEntries);
        return this;
    }

    public Event addTrackFieldEntry(TrackFieldEntry trackFieldEntry) {
        this.trackFieldEntries.add(trackFieldEntry);
        trackFieldEntry.setEvent(this);
        return this;
    }

    public Event removeTrackFieldEntry(TrackFieldEntry trackFieldEntry) {
        this.trackFieldEntries.remove(trackFieldEntry);
        trackFieldEntry.setEvent(null);
        return this;
    }

    public Set<Athlete> getAthletes() {
        return this.athletes;
    }

    public void setAthletes(Set<Athlete> athletes) {
        if (this.athletes != null) {
            this.athletes.forEach(i -> i.removeEvent(this));
        }
        if (athletes != null) {
            athletes.forEach(i -> i.addEvent(this));
        }
        this.athletes = athletes;
    }

    public Event athletes(Set<Athlete> athletes) {
        this.setAthletes(athletes);
        return this;
    }

    public Event addAthlete(Athlete athlete) {
        this.athletes.add(athlete);
        athlete.getEvents().add(this);
        return this;
    }

    public Event removeAthlete(Athlete athlete) {
        this.athletes.remove(athlete);
        athlete.getEvents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
