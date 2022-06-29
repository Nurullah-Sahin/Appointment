package com.solidsoft.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.solidsoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentDefinitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentDefinition.class);
        AppointmentDefinition appointmentDefinition1 = new AppointmentDefinition();
        appointmentDefinition1.setId("id1");
        AppointmentDefinition appointmentDefinition2 = new AppointmentDefinition();
        appointmentDefinition2.setId(appointmentDefinition1.getId());
        assertThat(appointmentDefinition1).isEqualTo(appointmentDefinition2);
        appointmentDefinition2.setId("id2");
        assertThat(appointmentDefinition1).isNotEqualTo(appointmentDefinition2);
        appointmentDefinition1.setId(null);
        assertThat(appointmentDefinition1).isNotEqualTo(appointmentDefinition2);
    }
}
