package com.elmarangao.trackblaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrackFieldEntryAthleteDetails.
 */
@Entity
@Table(name = "track_field_entry_athlete_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackFieldEntryAthleteDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_rank")
    private Integer rank;

    @Column(name = "time_height_distance")
    private String timeHeightDistance;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne
    @JsonIgnoreProperties(value = { "trackFieldEntryAthleteDetails", "events", "school" }, allowSetters = true)
    private Athlete athlete;

    @ManyToMany(mappedBy = "details")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "details", "event" }, allowSetters = true)
    private Set<TrackFieldEntry> trackFieldEntries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrackFieldEntryAthleteDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return this.rank;
    }

    public TrackFieldEntryAthleteDetails rank(Integer rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getTimeHeightDistance() {
        return this.timeHeightDistance;
    }

    public TrackFieldEntryAthleteDetails timeHeightDistance(String timeHeightDistance) {
        this.setTimeHeightDistance(timeHeightDistance);
        return this;
    }

    public void setTimeHeightDistance(String timeHeightDistance) {
        this.timeHeightDistance = timeHeightDistance;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public TrackFieldEntryAthleteDetails remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Athlete getAthlete() {
        return this.athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public TrackFieldEntryAthleteDetails athlete(Athlete athlete) {
        this.setAthlete(athlete);
        return this;
    }

    public Set<TrackFieldEntry> getTrackFieldEntries() {
        return this.trackFieldEntries;
    }

    public void setTrackFieldEntries(Set<TrackFieldEntry> trackFieldEntries) {
        if (this.trackFieldEntries != null) {
            this.trackFieldEntries.forEach(i -> i.removeDetails(this));
        }
        if (trackFieldEntries != null) {
            trackFieldEntries.forEach(i -> i.addDetails(this));
        }
        this.trackFieldEntries = trackFieldEntries;
    }

    public TrackFieldEntryAthleteDetails trackFieldEntries(Set<TrackFieldEntry> trackFieldEntries) {
        this.setTrackFieldEntries(trackFieldEntries);
        return this;
    }

    public TrackFieldEntryAthleteDetails addTrackFieldEntry(TrackFieldEntry trackFieldEntry) {
        this.trackFieldEntries.add(trackFieldEntry);
        trackFieldEntry.getDetails().add(this);
        return this;
    }

    public TrackFieldEntryAthleteDetails removeTrackFieldEntry(TrackFieldEntry trackFieldEntry) {
        this.trackFieldEntries.remove(trackFieldEntry);
        trackFieldEntry.getDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackFieldEntryAthleteDetails)) {
            return false;
        }
        return id != null && id.equals(((TrackFieldEntryAthleteDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackFieldEntryAthleteDetails{" +
            "id=" + getId() +
            ", rank=" + getRank() +
            ", timeHeightDistance='" + getTimeHeightDistance() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
