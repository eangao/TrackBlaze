package com.elmarangao.trackblaze.domain;

import com.elmarangao.trackblaze.domain.enumeration.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Athlete.
 */
@Entity
@Table(name = "athlete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Athlete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Size(max = 50)
    @Column(name = "middle_name", length = 50)
    private String middleName;

    @NotNull
    @Size(max = 100)
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "athlete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "athlete", "trackFieldEntries" }, allowSetters = true)
    private Set<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetails = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_athlete__event",
        joinColumns = @JoinColumn(name = "athlete_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trackFieldEntries", "athletes" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "athletes" }, allowSetters = true)
    private School school;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Athlete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Athlete firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Athlete middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Athlete lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Athlete birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Athlete gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Athlete picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Athlete pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Athlete userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<TrackFieldEntryAthleteDetails> getTrackFieldEntryAthleteDetails() {
        return this.trackFieldEntryAthleteDetails;
    }

    public void setTrackFieldEntryAthleteDetails(Set<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetails) {
        if (this.trackFieldEntryAthleteDetails != null) {
            this.trackFieldEntryAthleteDetails.forEach(i -> i.setAthlete(null));
        }
        if (trackFieldEntryAthleteDetails != null) {
            trackFieldEntryAthleteDetails.forEach(i -> i.setAthlete(this));
        }
        this.trackFieldEntryAthleteDetails = trackFieldEntryAthleteDetails;
    }

    public Athlete trackFieldEntryAthleteDetails(Set<TrackFieldEntryAthleteDetails> trackFieldEntryAthleteDetails) {
        this.setTrackFieldEntryAthleteDetails(trackFieldEntryAthleteDetails);
        return this;
    }

    public Athlete addTrackFieldEntryAthleteDetails(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        this.trackFieldEntryAthleteDetails.add(trackFieldEntryAthleteDetails);
        trackFieldEntryAthleteDetails.setAthlete(this);
        return this;
    }

    public Athlete removeTrackFieldEntryAthleteDetails(TrackFieldEntryAthleteDetails trackFieldEntryAthleteDetails) {
        this.trackFieldEntryAthleteDetails.remove(trackFieldEntryAthleteDetails);
        trackFieldEntryAthleteDetails.setAthlete(null);
        return this;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Athlete events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public Athlete addEvent(Event event) {
        this.events.add(event);
        event.getAthletes().add(this);
        return this;
    }

    public Athlete removeEvent(Event event) {
        this.events.remove(event);
        event.getAthletes().remove(this);
        return this;
    }

    public School getSchool() {
        return this.school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Athlete school(School school) {
        this.setSchool(school);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Athlete)) {
            return false;
        }
        return id != null && id.equals(((Athlete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Athlete{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
