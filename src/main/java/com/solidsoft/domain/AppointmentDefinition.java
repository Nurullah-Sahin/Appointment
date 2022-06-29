package com.solidsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AppointmentDefinition.
 */
@Document(collection = "appointment_definition")
public class AppointmentDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start_time")
    private LocalDate startTime;

    @Field("end_time")
    private LocalDate endTime;

    @Field("name")
    private String name;

    @Field("allow_reschedule_no_sooner_than")
    private Integer allowRescheduleNoSoonerThan;

    @Field("allow_reschedule_no_later_than")
    private Integer allowRescheduleNoLaterThan;

    @Field("allow_schedule_no_sooner_than")
    private Integer allowScheduleNoSoonerThan;

    @Field("allow_schedule_no_later_than")
    private Integer allowScheduleNoLaterThan;

    @Field("number_of_reschedule")
    private Integer numberOfReschedule;

    @Field("duration")
    private Duration duration;

    @DBRef
    @Field("categoryName")
    private AppointmentCategory categoryName;

    @DBRef
    @Field("categoryId")
    private AppointmentCategory categoryId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AppointmentDefinition id(String id) {
        this.id = id;
        return this;
    }

    public LocalDate getStartTime() {
        return this.startTime;
    }

    public AppointmentDefinition startTime(LocalDate startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return this.endTime;
    }

    public AppointmentDefinition endTime(LocalDate endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return this.name;
    }

    public AppointmentDefinition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAllowRescheduleNoSoonerThan() {
        return this.allowRescheduleNoSoonerThan;
    }

    public AppointmentDefinition allowRescheduleNoSoonerThan(Integer allowRescheduleNoSoonerThan) {
        this.allowRescheduleNoSoonerThan = allowRescheduleNoSoonerThan;
        return this;
    }

    public void setAllowRescheduleNoSoonerThan(Integer allowRescheduleNoSoonerThan) {
        this.allowRescheduleNoSoonerThan = allowRescheduleNoSoonerThan;
    }

    public Integer getAllowRescheduleNoLaterThan() {
        return this.allowRescheduleNoLaterThan;
    }

    public AppointmentDefinition allowRescheduleNoLaterThan(Integer allowRescheduleNoLaterThan) {
        this.allowRescheduleNoLaterThan = allowRescheduleNoLaterThan;
        return this;
    }

    public void setAllowRescheduleNoLaterThan(Integer allowRescheduleNoLaterThan) {
        this.allowRescheduleNoLaterThan = allowRescheduleNoLaterThan;
    }

    public Integer getAllowScheduleNoSoonerThan() {
        return this.allowScheduleNoSoonerThan;
    }

    public AppointmentDefinition allowScheduleNoSoonerThan(Integer allowScheduleNoSoonerThan) {
        this.allowScheduleNoSoonerThan = allowScheduleNoSoonerThan;
        return this;
    }

    public void setAllowScheduleNoSoonerThan(Integer allowScheduleNoSoonerThan) {
        this.allowScheduleNoSoonerThan = allowScheduleNoSoonerThan;
    }

    public Integer getAllowScheduleNoLaterThan() {
        return this.allowScheduleNoLaterThan;
    }

    public AppointmentDefinition allowScheduleNoLaterThan(Integer allowScheduleNoLaterThan) {
        this.allowScheduleNoLaterThan = allowScheduleNoLaterThan;
        return this;
    }

    public void setAllowScheduleNoLaterThan(Integer allowScheduleNoLaterThan) {
        this.allowScheduleNoLaterThan = allowScheduleNoLaterThan;
    }

    public Integer getNumberOfReschedule() {
        return this.numberOfReschedule;
    }

    public AppointmentDefinition numberOfReschedule(Integer numberOfReschedule) {
        this.numberOfReschedule = numberOfReschedule;
        return this;
    }

    public void setNumberOfReschedule(Integer numberOfReschedule) {
        this.numberOfReschedule = numberOfReschedule;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public AppointmentDefinition duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public AppointmentCategory getCategoryName() {
        return this.categoryName;
    }

    public AppointmentDefinition categoryName(AppointmentCategory appointmentCategory) {
        this.setCategoryName(appointmentCategory);
        return this;
    }

    public void setCategoryName(AppointmentCategory appointmentCategory) {
        this.categoryName = appointmentCategory;
    }

    public AppointmentCategory getCategoryId() {
        return this.categoryId;
    }

    public AppointmentDefinition categoryId(AppointmentCategory appointmentCategory) {
        this.setCategoryId(appointmentCategory);
        return this;
    }

    public void setCategoryId(AppointmentCategory appointmentCategory) {
        this.categoryId = appointmentCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentDefinition)) {
            return false;
        }
        return id != null && id.equals(((AppointmentDefinition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentDefinition{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", name='" + getName() + "'" +
            ", allowRescheduleNoSoonerThan=" + getAllowRescheduleNoSoonerThan() +
            ", allowRescheduleNoLaterThan=" + getAllowRescheduleNoLaterThan() +
            ", allowScheduleNoSoonerThan=" + getAllowScheduleNoSoonerThan() +
            ", allowScheduleNoLaterThan=" + getAllowScheduleNoLaterThan() +
            ", numberOfReschedule=" + getNumberOfReschedule() +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
