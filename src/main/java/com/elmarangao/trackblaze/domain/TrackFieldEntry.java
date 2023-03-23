package com.elmarangao.trackblaze.domain;

import com.elmarangao.trackblaze.domain.enumeration.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrackFieldEntry.
 */
@Entity
@Table(name = "track_field_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackFieldEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_track_field_entry__details",
        joinColumns = @JoinColumn(name = "track_field_entry_id"),
        inverseJoinColumns = @JoinColumn(name = "details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "athlete", "trackFieldEntries" }, allowSetters = true)
    private Set<TrackFieldEntryAthleteDetails> details = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "trackFieldEntries", "athletes" }, allowSetters = true)
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrackFieldEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public TrackFieldEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public TrackFieldEntry category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<TrackFieldEntryAthleteDetails> getDetails() {
        return this.details;
    }

    public void setDetails(Set<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetails) {
        this.details = trackFieldEntryAthleteDetails;
    }

    public TrackFieldEntry details(Set<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetails) {
        this.setDetails(trackFieldEntryAthleteDetails);
        return this;
    }

    public TrackFieldEntry addDetails(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        this.details.add(trackFieldEntryAthleteDetails);
        trackFieldEntryAthleteDetails.getTrackFieldEntries().add(this);
        return this;
    }

    public TrackFieldEntry removeDetails(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        this.details.remove(trackFieldEntryAthleteDetails);
        trackFieldEntryAthleteDetails.getTrackFieldEntries().remove(this);
        return this;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public TrackFieldEntry event(Event event) {
        this.setEvent(event);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackFieldEntry)) {
            return false;
        }
        return id != null && id.equals(((TrackFieldEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackFieldEntry{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
