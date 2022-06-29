import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppointmentDefinition } from '../appointment-definition.model';

@Component({
  selector: 'jhi-appointment-definition-detail',
  templateUrl: './appointment-definition-detail.component.html',
})
export class AppointmentDefinitionDetailComponent implements OnInit {
  appointmentDefinition: IAppointmentDefinition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentDefinition }) => {
      this.appointmentDefinition = appointmentDefinition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
