import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointmentDefinition } from '../appointment-definition.model';
import { AppointmentDefinitionService } from '../service/appointment-definition.service';

@Component({
  templateUrl: './appointment-definition-delete-dialog.component.html',
})
export class AppointmentDefinitionDeleteDialogComponent {
  appointmentDefinition?: IAppointmentDefinition;

  constructor(protected appointmentDefinitionService: AppointmentDefinitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.appointmentDefinitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
