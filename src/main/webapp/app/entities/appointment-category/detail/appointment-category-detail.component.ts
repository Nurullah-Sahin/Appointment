import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppointmentCategory } from '../appointment-category.model';

@Component({
  selector: 'jhi-appointment-category-detail',
  templateUrl: './appointment-category-detail.component.html',
})
export class AppointmentCategoryDetailComponent implements OnInit {
  appointmentCategory: IAppointmentCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointmentCategory }) => {
      this.appointmentCategory = appointmentCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
