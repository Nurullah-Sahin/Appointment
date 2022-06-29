package com.solidsoft.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AppointmentCategory.
 */
@Document(collection = "appointment_category")
public class AppointmentCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("category_name")
    private String categoryName;

    @Field("hosts")
    private String hosts;

    @DBRef
    @Field("userId")
    private User userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AppointmentCategory id(String id) {
        this.id = id;
        return this;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public AppointmentCategory categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getHosts() {
        return this.hosts;
    }

    public AppointmentCategory hosts(String hosts) {
        this.hosts = hosts;
        return this;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public User getUserId() {
        return this.userId;
    }

    public AppointmentCategory userId(User user) {
        this.setUserId(user);
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentCategory)) {
            return false;
        }
        return id != null && id.equals(((AppointmentCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", hosts='" + getHosts() + "'" +
            "}";
    }
}
