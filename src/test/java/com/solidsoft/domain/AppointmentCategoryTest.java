package com.solidsoft.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.solidsoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentCategory.class);
        AppointmentCategory appointmentCategory1 = new AppointmentCategory();
        appointmentCategory1.setId("id1");
        AppointmentCategory appointmentCategory2 = new AppointmentCategory();
        appointmentCategory2.setId(appointmentCategory1.getId());
        assertThat(appointmentCategory1).isEqualTo(appointmentCategory2);
        appointmentCategory2.setId("id2");
        assertThat(appointmentCategory1).isNotEqualTo(appointmentCategory2);
        appointmentCategory1.setId(null);
        assertThat(appointmentCategory1).isNotEqualTo(appointmentCategory2);
    }
}
