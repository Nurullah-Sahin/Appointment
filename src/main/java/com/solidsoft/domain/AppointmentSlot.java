package com.solidsoft.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AppointmentSlot.
 */
@Document(collection = "appointment_slot")
public class AppointmentSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @DBRef
    @Field("definitionId")
    private AppointmentDefinition definitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AppointmentSlot id(String id) {
        this.id = id;
        return this;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public AppointmentSlot startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public AppointmentSlot endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public AppointmentDefinition getDefinitionId() {
        return this.definitionId;
    }

    public AppointmentSlot definitionId(AppointmentDefinition appointmentDefinition) {
        this.setDefinitionId(appointmentDefinition);
        return this;
    }

    public void setDefinitionId(AppointmentDefinition appointmentDefinition) {
        this.definitionId = appointmentDefinition;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentSlot)) {
            return false;
        }
        return id != null && id.equals(((AppointmentSlot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentSlot{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
