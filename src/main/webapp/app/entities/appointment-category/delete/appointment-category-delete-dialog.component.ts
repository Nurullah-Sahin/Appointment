import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentCategory } from '../appointment-category.model';
import { AppointmentCategoryService } from '../service/appointment-category.service';

@Component({
  templateUrl: './appointment-category-delete-dialog.component.html',
})
export class AppointmentCategoryDeleteDialogComponent {
  appointmentCategory?: IAppointmentCategory;

  constructor(protected appointmentCategoryService: AppointmentCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.appointmentCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
