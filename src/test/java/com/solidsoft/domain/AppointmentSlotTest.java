package com.solidsoft.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.solidsoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentSlotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentSlot.class);
        AppointmentSlot appointmentSlot1 = new AppointmentSlot();
        appointmentSlot1.setId("id1");
        AppointmentSlot appointmentSlot2 = new AppointmentSlot();
        appointmentSlot2.setId(appointmentSlot1.getId());
        assertThat(appointmentSlot1).isEqualTo(appointmentSlot2);
        appointmentSlot2.setId("id2");
        assertThat(appointmentSlot1).isNotEqualTo(appointmentSlot2);
        appointmentSlot1.setId(null);
        assertThat(appointmentSlot1).isNotEqualTo(appointmentSlot2);
    }
}
